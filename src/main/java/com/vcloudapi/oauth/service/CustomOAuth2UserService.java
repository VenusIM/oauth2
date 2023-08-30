package com.vcloudapi.oauth.service;

import com.vcloudapi.member.dto.user.User;
import com.vcloudapi.member.mapper.UserMapper;
import com.vcloudapi.oauth.entity.ProviderType;
import com.vcloudapi.oauth.entity.RoleType;
import com.vcloudapi.oauth.entity.UserPrincipal;
import com.vcloudapi.global.exception.OAuthProviderMissMatchException;
import com.vcloudapi.oauth.info.OAuth2UserInfo;
import com.vcloudapi.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Provider;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private UserMapper userMapper;
    private RedisTemplate<String, Map<String, String>> redisTemplate;
    private ValueOperations<String, Map<String, String>> valueOperations;
    public CustomOAuth2UserService(UserMapper userMapper, RedisTemplate<String, Map<String, String>> redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        if(redisTemplate != null) {
            valueOperations = redisTemplate.opsForValue();
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        log.info("======= process userInfo ========");
        log.info(userInfo.getAttributes().toString());
        log.info("======= process userInfo end ========");
        // https://devtalk.kakao.com/t/id/124958 reference
        User savedUser =null;
                valueOperations.get(userInfo.getId());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                userInfo.getImageUrl(),
                providerType,
                RoleType.USER,
                now,
                now
        );

        //TODO Redis

        return user;
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
            user.setUsername(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !user.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            user.setProfileImageUrl(userInfo.getImageUrl());
        }

        //TODO Redis

        return user;
    }
}

package com.vcloudapi.oauth.service;

import com.vcloudapi.api.member.dto.User;
import com.vcloudapi.api.member.mapper.UserMapper;
import com.vcloudapi.oauth.vo.ProviderType;
import com.vcloudapi.oauth.vo.RoleType;
import com.vcloudapi.oauth.vo.UserPrincipal;
import com.vcloudapi.global.exception.OAuthProviderMissMatchException;
import com.vcloudapi.oauth.info.OAuth2Info;
import com.vcloudapi.oauth.info.OAuth2UserInfo;
import com.vcloudapi.oauth.info.OAuth2UserInfoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private UserMapper userMapper;
    private RedisTemplate<String, OAuth2Info> redisTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            OAuth2User auth2User = this.process(userRequest, user);
            return auth2User;
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
        OAuth2Info oAuth2Info = null;
//                redisTemplate.opsForValue().get(userInfo.getId());

        User savedUser = null;
        if(oAuth2Info != null) {
            savedUser = oAuth2Info.getUser();
        } else {
            oAuth2Info = OAuth2Info.builder(userRequest.getAccessToken().getTokenValue(), userRequest.getAccessToken().getExpiresAt().toEpochMilli())
                    .build();
        }

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
                userInfo.getImageUrl(),
                providerType,
                RoleType.USER,
                now,
                now
        );

//        userMapper.insertOAuthUser(user);
        //TODO Redis
//        redisTemplate.opsForValue().set(user.getUserId(), );

        return user;
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {

        //TODO Redis

        return user;
    }
}

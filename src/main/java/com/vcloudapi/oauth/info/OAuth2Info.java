package com.vcloudapi.oauth.info;

import com.vcloudapi.api.member.dto.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder(builderMethodName = "essentialBuilder")
public class OAuth2Info {
    private String accessToken;
    private Long expiredIn;
    private String refreshToken;
    private Long refreshTokenExpiredIn;
    private User user;


    public static OAuth2InfoBuilder builder(String accessToken, Long expiredIn) {
        return essentialBuilder().accessToken(accessToken).expiredIn(expiredIn);
    }
}

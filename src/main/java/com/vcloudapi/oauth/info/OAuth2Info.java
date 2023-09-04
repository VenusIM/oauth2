package com.vcloudapi.oauth.info;

import com.vcloudapi.api.member.dto.User;
import lombok.Builder;
import lombok.Getter;

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

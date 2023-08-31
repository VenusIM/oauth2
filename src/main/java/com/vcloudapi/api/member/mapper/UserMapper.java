package com.vcloudapi.api.member.mapper;

import com.vcloudapi.api.member.dto.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertOAuthUser(User user);

    User selectOAuthUserById(String id);
}

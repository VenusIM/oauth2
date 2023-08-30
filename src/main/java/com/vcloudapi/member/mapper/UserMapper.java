package com.vcloudapi.member.mapper;

import com.vcloudapi.member.dto.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertOAuthUser(User user);

    User selectOAuthUserById(String id);
}

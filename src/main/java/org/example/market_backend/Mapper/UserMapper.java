package org.example.market_backend.Mapper;


import org.example.market_backend.Entity.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    void updateUserInfo(User update, User user);

    User findUserByToken(String token);
}

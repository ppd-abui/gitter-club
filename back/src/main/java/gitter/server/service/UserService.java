package gitter.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.entity.User;

public interface UserService extends IService<User> {
    //用户登录账号密码验证
    User selectUserLogin(String userAccount,String userPassword);
    //通过账号搜索用户
    User selectByUserAccount(String userAccount);
    //用户注册
    boolean register(User user);
}

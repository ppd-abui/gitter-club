package gitter.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.entity.User;

public interface UserService extends IService<User> {
    //用户登录账号密码验证
    User selectUserLogin(String userAccount,String userPassword);
    User selectByUserAccount(String userAccount);
}

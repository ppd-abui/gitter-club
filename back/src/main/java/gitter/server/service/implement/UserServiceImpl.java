package gitter.server.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.entity.User;
import gitter.server.mapper.UserMapper;
import gitter.server.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;


    @Override   //用户登录账号密码验证
    public User selectUserLogin(String userAccount,String userPassword) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserAccount,userAccount)
                .eq(User::getUserPassword,userPassword));
    }
}

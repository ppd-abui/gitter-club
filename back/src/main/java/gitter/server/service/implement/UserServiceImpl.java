package gitter.server.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.mapper.UserMapper;
import gitter.server.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    Map<String,User> userMap = new HashMap<>();

    @Override   //用户登录账号密码验证
    public User selectUserLogin(String userAccount,String userPassword) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserAccount,userAccount)
                .eq(User::getUserPassword,userPassword));
    }

    @Override   //通过账号搜索用户
    public User selectByUserAccount(String userAccount){
        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserAccount,userAccount));
    }

    @Override   //用户注册
    public boolean register(User user){
        try{
            userMapper.insert(user);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override   //存储用户token
    public boolean storeToken(String token,User user){
        try{
            userMap.put(token,user);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override   //通过token查找用户
    public User selectByToken(String token){
        return userMap.get(token);
    }

    @Override
    public List<User> selectListByKeyword(String keyword){
        return userMapper
                .selectList(Wrappers.<User>lambdaQuery()
                        .like(User::getUserAccount,keyword));
    }

    @Override
    public boolean updateUser(User user){
        try {
            userMapper.updateById(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

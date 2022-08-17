package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.User;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping()
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        //搜索当前账号密码是否有对应的用户
        User res = userService.selectUserLogin(user.getUserAccount(),user.getUserPassword());

        //查询不到用户返回错误
        if (res == null){
            return new Result<>(500,null,"用户名或密码错误！");
        }

        return new Result<>(200,null,"登录成功");
    }
}

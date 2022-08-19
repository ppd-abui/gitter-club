package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.User;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.*;

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
            return new Result<>(500,null,"Wrong user account or password！");
        }

        return new Result<>(200,null,"Login successfully!");
    }

    @GetMapping("/register/account")
    public Result<?> checkUserAccount(@RequestParam(defaultValue = "") String userAccount){
        User res = userService.selectByUserAccount(userAccount);
        if(res == null)
            return new Result<>(200,null,"The account is available");

        return new Result<>(500,null,"");
    }
}

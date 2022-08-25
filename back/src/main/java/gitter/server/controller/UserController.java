package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.User;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

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

        String token = UUID.randomUUID()+"";
        userService.storeToken(token,res);
        System.out.println(token);
        return new Result<>(200,token,"Login successfully!");
    }

    @GetMapping("/register/account")
    public Result<?> checkUserAccount(@RequestParam(defaultValue = "") String userAccount){
        User res = userService.selectByUserAccount(userAccount);

        if(res == null)
            return new Result<>(200,null,"The account is available!");
        else
            return new Result<>(500,null,"");
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        if(userService.register(user))
            return new Result<>(200,null,"Register successfully!");
        else
            return new Result<>(500,null,"");
    }
}

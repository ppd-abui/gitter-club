package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
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

    @GetMapping("/user/search")
    public Result<?> repoSearch(@RequestParam String keyWord){
        try {
            List<User> resUsers = userService.selectListByKeyword(keyWord);
            return new Result<>(200,resUsers,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Search failed!");
        }
    }

    @GetMapping("/user/star")
    public Result<?> getUserStar(@RequestParam String userAccount) {
        try {
            User user = userService.selectByUserAccount(userAccount);
            String userStarsString = user.getUserStars();

            String [] starsList ;

            if(userStarsString != null)
                starsList = userStarsString.split("_");
            else
                starsList = null;
            return new Result<>(200,starsList,"All the star repos");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Stars failed!");
        }
    }
    @GetMapping("user/star/change")
    public Result<?> changeUserStar(@RequestParam String userAccount,@RequestParam String repoName,@RequestParam boolean testStar){
        //获得Star列表
        User user = userService.selectByUserAccount(userAccount);
        System.out.println(user);
        String userStarsString = user.getUserStars();
        System.out.println(userStarsString);

        String [] starsList ;
        if( userStarsString != null || !userStarsString.equals(""))
            starsList = userStarsString.split("_");
        else
            starsList = null;

        String userStarsStringNew = "";

        System.out.println(testStar);

        //根据testStar选择操作
        try{
            if(testStar == true){
                for(int i = 0;i < starsList.length;i++)
                    if(!starsList[i].equals(repoName))
                        userStarsStringNew += (starsList[i]+"_");
                System.out.println("delete"+userStarsStringNew);
            }
            else{
                if(userStarsString == null || userStarsString.equals(""))
                    userStarsStringNew = repoName+"_";
                else
                    userStarsStringNew = userStarsString+repoName+"_";
                System.out.println(userStarsStringNew);
            }

            System.out.println("7277272727272");
            user.setUserStars(userStarsStringNew);
            user.setUserName("111111111");
            System.out.println(user);
            userService.updateById(user);

            return  new Result<>(200,null,"The change is completed!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result<>(500, null, "Stars failed!");
        }
    }

    @GetMapping("/user/star/check")
    public Result<?> checkUserStar(@RequestParam String userAccount,@RequestParam String repoName) {
        try {
            User user = userService.selectByUserAccount(userAccount);
            String userStarsString = user.getUserStars();

            String [] starsList ;

            if(userStarsString != null) {
                starsList = userStarsString.split("_");
                for(String item : starsList){
                    System.out.println(item);
                    System.out.println(repoName);
                    if(item.equals(repoName))
                        return new Result<>(200,1,"Exist");
                }
            }
            return new Result<>(200,0,"nop");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Stars failed!");
        }
    }
}

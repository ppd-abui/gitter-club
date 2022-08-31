package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.service.RepoService;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping()
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RepoService repoService;


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
            return new Result<>(500,res,"");
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

    @GetMapping("/user/star/repo")
    public Result<?> getUserStarRepo(@RequestParam String userAccount) {
        try {
            User user = userService.selectByUserAccount(userAccount);
            String userStarsString = user.getUserStars();

            String [] starsList ;
            String [] repoSomething;

            List<Repo> repos  = new ArrayList<>();

            if(userStarsString != null && !userStarsString.equals("")) {
                starsList = userStarsString.split("_");
                for(String item:starsList){
                    repoSomething = item.split("/");
                    Repo repo = new Repo();
                    repo.setRepoOwner(repoSomething[0]);
                    repo.setRepoName(repoSomething[1]);
                    repos.add(repoService.selectByRepoName(repo));
                }
            }
            else
                return new Result<>(200,null,"No repos");

            return new Result<>(200,repos,"All the star repos");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Stars failed!!!!");
        }
    }

    //改变收藏的仓库
    //改变收藏的仓库
    @GetMapping("user/star/change")
    public Result<?> changeUserStar(@RequestParam String userAccount, @RequestParam String repoOwner, @RequestParam String repoName,@RequestParam boolean testStar){
        //获得Star列表
        User user = userService.selectByUserAccount(userAccount);
        String userStarsString = user.getUserStars();
        String [] starsList ;
        String [] repoSomething;

        String [] followersList;

        if( userStarsString != null || !userStarsString.equals(""))
            starsList = userStarsString.split("_");
        else
            starsList = null;
        String userStarsStringNew = "";
        String followersStringNew="";


        //根据testStar选择操作
        try{
            if(testStar == true){
                for(int i = 0;i < starsList.length;i++){
                    repoSomething = starsList[i].split("/");

                    Repo repo = new Repo();
                    repo.setRepoOwner(repoSomething[0]);
                    repo.setRepoName(repoSomething[1]);
                    Repo res = repoService.selectByRepoName(repo);
                    String repoFollowers = res.getRepoFollowers();
                    Integer numOfFollowers = res.getRepoFollowNum();

                    if (repoFollowers==null || repoFollowers.equals("")) followersList=null;
                    else followersList = repoFollowers.split("_");


                    if(!repoSomething[0].equals(repoOwner) || !repoSomething[1].equals(repoName))
                        userStarsStringNew += (starsList[i]+"_");
                    else {
                        if (followersList!=null) {
                            for (int j = 0; j < followersList.length; j++) {
                                System.out.println(repoOwner);
                                System.out.println(repoName);
                                System.out.println(followersList[j]);
                                if (!followersList[j].equals(userAccount))
                                    followersStringNew += (followersList[j] + "_");
                            }
                            numOfFollowers--;

                            res.setRepoFollowers(followersStringNew);
                            res.setRepoFollowNum(numOfFollowers);
                            repoService.updateById(res);
                        }
                    }

                }
            }
            else{
                if(userStarsString == null || userStarsString.equals("")) {
                    userStarsStringNew = repoOwner + "/" + repoName + "_";

                    Repo repo = new Repo();
                    repo.setRepoOwner(repoOwner);
                    repo.setRepoName(repoName);
                    Repo res = repoService.selectByRepoName(repo);
                    String repoFollowers = res.getRepoFollowers();
                    Integer numOfFollowers = res.getRepoFollowNum();

                    if(repoFollowers == null || repoFollowers.equals(""))
                        followersStringNew = userAccount+"_";
                    else
                        followersStringNew = repoFollowers+userAccount+"_";

                    numOfFollowers++;

                    res.setRepoFollowers(followersStringNew);
                    res.setRepoFollowNum(numOfFollowers);

                    repoService.updateById(res);

                }
                else {
                    userStarsStringNew = userStarsString + repoOwner + "/" + repoName + "_";

                    Repo repo = new Repo();
                    repo.setRepoOwner(repoOwner);
                    repo.setRepoName(repoName);
                    Repo res = repoService.selectByRepoName(repo);
                    String repoFollowers = res.getRepoFollowers();
                    Integer numOfFollowers = res.getRepoFollowNum();

                    if(repoFollowers == null || repoFollowers.equals(""))
                        followersStringNew = userAccount+"_";
                    else
                        followersStringNew = repoFollowers+userAccount+"_";

                    numOfFollowers++;

                    res.setRepoFollowers(followersStringNew);
                    res.setRepoFollowNum(numOfFollowers);

                    repoService.updateById(res);
                }
            }
            user.setUserStars(userStarsStringNew);
            userService.updateById(user);
            return  new Result<>(200,null,"The change is completed!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result<>(500, null, "Stars failed!");
        }
    }

    //对于仓库是否收藏进行检查
    @GetMapping("/user/star/check")
    public Result<?> checkUserStar(@RequestParam String userAccount,@RequestParam String repoOwner,@RequestParam String repoName) {
        try {
            User user = userService.selectByUserAccount(userAccount);
            String userStarsString = user.getUserStars();

            String [] starsList ;
            String [] repoSomething;

            if(userStarsString != null) {
                starsList = userStarsString.split("_");
                for(String item : starsList){
                    repoSomething = item.split("/");
                    if(repoSomething[0].equals(repoOwner) && repoSomething[1].equals(repoName))
                        return new Result<>(200,1,"Exist");
                }
            }
            return new Result<>(200,0,"nop");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Stars failed!");
        }
    }

    @GetMapping("/user/change/name")
    public Result<?> changeUserName(@RequestParam String userAccount,@RequestParam String userNameNew) {
        User res = userService.selectByUserAccount(userAccount);
        res.setUserName(userNameNew);

        if(userService.updateUser(res))
            return new Result<>(200,res,"Change the name successfully");
        else
            return new Result<>(500,null,"The change meets some mistake");
    }

    @GetMapping("/user/change/account")
    public Result<?> changeUserAccount(@RequestParam String userAccount,@RequestParam String userAccountNew) {
        User res = userService.selectByUserAccount(userAccount);
        res.setUserAccount(userAccountNew);

        if(userService.updateUser(res))
            return new Result<>(200,res,"Change the account successfully");
        else
            return new Result<>(500,res,"The change meets some mistake");
    }
    @GetMapping("/user/change/password")
    public Result<?> changeUserPassword(@RequestParam String userAccount,@RequestParam String userPasswordNew) {
        User res = userService.selectByUserAccount(userAccount);
        res.setUserPassword(userPasswordNew);

        if(userService.updateUser(res))
            return new Result<>(200,res,"Change the password successfully");
        else
            return new Result<>(500,res,"The change meets some mistake");
    }
    @GetMapping("/user/change/sex")
    public Result<?> changeUserSex(@RequestParam String userAccount,@RequestParam String userSexNew) {
        User res = userService.selectByUserAccount(userAccount);
        res.setUserSex(userSexNew);

        if(userService.updateUser(res))
            return new Result<>(200,res,"Change the sex successfully");
        else
            return new Result<>(500,res,"The change meets some mistake");
    }

    @GetMapping("/user/star/search")
    public Result<?>searchUserStar(@RequestParam String userAccount,@RequestParam String searchValue,@RequestParam String searchVisibility,@RequestParam String searchSort){
        try {

            User user = userService.selectByUserAccount(userAccount);
            String userStarsString = user.getUserStars();

            String [] starsList ;
            String [] repoSomething;

            List<Repo> repos  = new ArrayList<>();

            if(userStarsString != null && !userStarsString.equals("")) {
                starsList = userStarsString.split("_");
                for(String item:starsList){
                    repoSomething = item.split("/");
                    Repo repo = new Repo();
                    repo.setRepoOwner(repoSomething[0]);
                    repo.setRepoName(repoSomething[1]);
                    repos.add(repoService.selectByRepoName(repo));
                }
            }
            else
                return  new Result<>(200,null,"No result 1");

            if(!searchValue.equals("")){
                Iterator<Repo> iterator = repos.iterator();
                while(iterator.hasNext()) {
                    Repo next = iterator.next();
                    String name = next.getRepoName();
                    if (!name.equals(searchValue)) {
                        iterator.remove();
                    }
                }
                if(repos.isEmpty())
                    return new Result<>(200,null,"No result 2");
            }

            if(!searchVisibility.equals("") && !searchVisibility.equals("all")){
                Iterator<Repo> iterator = repos.iterator();
                while(iterator.hasNext()) {
                    Repo next = iterator.next();
                    String visibility = next.getRepoVisibility();
                    if (!visibility.equals(searchVisibility)) {
                        iterator.remove();
                    }
                }
                if(repos.isEmpty())
                    return new Result<>(200,null,"No result 3");
            }


            if(searchSort.equals("name")){
                Collections.sort(repos, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        if( o1.getRepoName().compareTo( o2.getRepoName()) >= 0 )
                            return 0;
                        else
                            return 1;
                    }
                });
            }else if(searchSort.equals("stars")){
                Collections.sort(repos, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        if( o1.getRepoFollowNum().compareTo( o2.getRepoFollowNum()) >= 0 )
                            return 0;
                        else
                            return 1;
                    }
                });
            }

            return new Result<>(200,repos,"获取成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Search fails!");
        }
    }

}

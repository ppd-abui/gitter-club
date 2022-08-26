package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.service.RepoService;
import gitter.server.service.UserService;
import gitter.server.utils.JGitUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping()
public class RepoController {

    @Resource
    RepoService repoService;

    @Resource
    UserService userService;

    @PostMapping("/repo/new")
    public Result<?> newRepo(@RequestHeader("token") String token, @RequestBody Repo repo) {
        User res = userService.selectByToken(token);
        if (res == null)
            return new Result<>(500, null, "Create failed!");

        repo.setRepoOwner(res.getUserAccount());

        if (repoService.createRepo(repo))
            return new Result<>(200, null, "Create successfully!");
        else
            return new Result<>(500, null, "Create failed!");
    }

    @PostMapping("/repo/name")
    public Result<?> isRepoNameExist(@RequestHeader("token") String token, @RequestBody Repo repo) {
        User resUser = userService.selectByToken(token);
        if (resUser == null)
            return new Result<>(500, null, "Create failed!");

        repo.setRepoOwner(resUser.getUserAccount());
        Repo resRepo = repoService.selectByRepoName(repo);

        if (resRepo == null)
            return new Result<>(500, null, "This repository name is available");
        else
            return new Result<>(200, resRepo, "The repository name already exists on this account");
    }


    @GetMapping("/repo/name")
    public Result<?> isRepoNameExist(@RequestParam String repoOwner, @RequestParam String repoName) {
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);

        if (res == null)
            return new Result<>(500, null, "This repository name is available");
        else
            return new Result<>(200, res, "The repository name already exists on this account");
    }

    @GetMapping("/url")
    public Result<?> getUrl(@RequestParam String repoOwner, @RequestParam String repoName){

        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        try {
            String url = JGitUtils.getRepoUrl(repo);
            return new Result<>(200,url,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,"error","获取失败!");
        }
    }

    @GetMapping("/repo/info")
    public Result<?> getRepos(@RequestParam String userAccount){
        try{
            System.out.println(userAccount);
            User user = userService.selectByUserAccount(userAccount);
            Repo repo = new Repo();
            repo.setRepoOwner(user.getUserAccount());
            List<Repo> repos = repoService.selectListByRepoOwner(repo);
            return new Result<>(200,repos,"获取成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"获取失败！");
        }
    }

    @PostMapping("/fork")
    public Result<?> fork(@RequestHeader("token") String token, @RequestBody Repo repo){

        User forkUser = userService.selectByToken(token);
        if (forkUser==null)
            return new Result<>(500,null,"Couldn't find user!");

        if (repoService.forkRepo(repo,forkUser))
            return new Result<>(200,null,"Fork successfully!");
        else
            return new Result<>(500,null,"System error, fork failed!");
    }

    @GetMapping("/repo/search")
    public Result<?> repoSearch(@RequestParam String keyWord){
        try {
            List<Repo> resRepos = repoService.selectListByKeyword(keyWord);
            return new Result<>(200,resRepos,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Search failed!");
        }
    }

}

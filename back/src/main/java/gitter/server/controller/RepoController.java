package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.service.RepoService;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping()
public class RepoController {

    @Resource
    RepoService repoService;

    @Resource
    UserService userService;

    @PostMapping("/repo/new")
    public Result<?> newRepo(@RequestHeader("token") String token, @RequestBody Repo repo){
        User res = userService.selectByToken(token);
        if(res==null)
            return new Result<>(500,null,"Create failed!");

        repo.setRepoOwner(res.getUserAccount());

        if(repoService.createRepo(repo))
            return new Result<>(200,null,"Create successfully!");
        else
            return new Result<>(500,null,"Create failed!");
    }

    @PostMapping("/repo/name")
    public Result<?> checkRepoName(@RequestHeader("token") String token, @RequestBody Repo repo){
        User resUser = userService.selectByToken(token);
        if(resUser==null)
            return new Result<>(500,null,"Create failed!");

        repo.setRepoOwner(resUser.getUserAccount());
        Repo resRepo = repoService.selectByRepoName(repo);

        if(resRepo==null)
            return new Result<>(200,null,"This repository name is available");
        else
            return new Result<>(500,null,"The repository name already exists on this account");
    }


    @GetMapping("/repo/name")
    public Result<?> checkRepoName(@RequestParam String repoOwner,@RequestParam String repoName){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);

        if(res==null)
            return new Result<>(200,null,"This repository name is available");
        else
            return new Result<>(500,null,"The repository name already exists on this account");
    }
    /**
     * type:repository, user
     * key:查询关键字
     */
}

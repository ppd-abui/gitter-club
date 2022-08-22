package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.service.RepoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping()
public class RepoController {

    @Resource
    RepoService repoService;

    @PostMapping("/repo/new")
    public Result<?> newRepo(@RequestBody Repo repo){
        if(repoService.createRepo(repo))
            return new Result<>(200,null,"Create successfully!");
        else
            return new Result<>(500,null,"Create failed!");
    }

    @PostMapping("/repo/name")
    public Result<?> checkRepoName(@RequestBody Repo repo,@RequestHeader("token") String token){


        Repo res = repoService.selectByRepoName(repo);

        if(res==null)
            return new Result<>(200,null,"This repository name is available");
        else
            return new Result<>(500,null,"The repository name already exists on this account");
    }
}

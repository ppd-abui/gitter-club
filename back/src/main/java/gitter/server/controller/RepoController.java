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

    @PostMapping("/new")
    public Result<?> newRepo(@RequestBody Repo repo){
        if(repoService.createRepo(repo.getRepoOwner(),repo.getRepoName()))
            return new Result<>(200,null,"Create successfully!");
        else
            return new Result<>(500,null,"Create failed!");
    }

}

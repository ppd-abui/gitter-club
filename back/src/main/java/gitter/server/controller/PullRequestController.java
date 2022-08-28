package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.PullRequest;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.service.PullRequestService;
import gitter.server.service.UserService;
import gitter.server.utils.CmdUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
public class PullRequestController {

    @Resource
    PullRequestService pullRequestService;

    @PostMapping("pulls/new")
    public Result<?> createPullRequest(@RequestBody PullRequest pullRequest){

        if(pullRequestService.createPullRequest(pullRequest))
            return new Result<>(200,null,"New pull successfully!");
        else
            return new Result<>(500,null,"New pull failed!");
    }

    @GetMapping("/pulls/info")
    public Result<?> getPulls(@RequestParam String repoOwner,@RequestParam String repoName){
        try {
            Repo repo = new Repo();
            repo.setRepoOwner(repoOwner);
            repo.setRepoName(repoName);
            List<PullRequest> pullRequests = pullRequestService.selectListByRepo(repo);
            return new Result<>(200,pullRequests,"Get pull request info successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Get pull request info failed！");
        }
    }

    @GetMapping("/pulls/diff")
    public Result<?> diff(@RequestParam String pullRequestId){
        PullRequest pullRequest = pullRequestService.selectByPullRequestId(Integer.valueOf(pullRequestId));

        if(pullRequest == null)
            return new Result<>(500,null,"System error!");
        else {
            try {
                String diff = pullRequestService.diff(pullRequest);
                return new Result<>(200, diff, "Review difference successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result<>(500, null, "Review difference successfully!");
            }
        }
    }

    @GetMapping("pulls/merge")
    public Result<?> merge(@RequestParam String pullRequestId){
        PullRequest pullRequest = pullRequestService.selectByPullRequestId(Integer.valueOf(pullRequestId));

        if(pullRequest == null)
            return new Result<>(500,null,"System error!");
        else {
            if(pullRequestService.merge(pullRequest))
                return new Result<>(200,null,"Merge successfully!");
            else
                return new Result<>(500,null,"Merge failed!");
        }
    }
}

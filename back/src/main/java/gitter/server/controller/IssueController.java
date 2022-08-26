package gitter.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import gitter.server.common.Result;
import gitter.server.entity.Issue;
import gitter.server.entity.User;
import gitter.server.mapper.IssueMapper;
import gitter.server.service.IssueService;
import gitter.server.service.RepoService;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping()
public class IssueController {
    @Resource
    RepoService repoService;

    @Resource
    UserService userService;

    @Resource
    IssueService issueService;

    @PostMapping("/issue/new")
    public Result<?> newIssue(@RequestHeader("token") String token, @RequestBody Issue issue){
        User res = userService.selectByToken(token);
        if(res==null)
            return new Result<>(500,null,"Create failed!");
        issue.setIssueUsername(res.getUserAccount());

        if(issueService.createIssue(issue)) {
            return new Result<>(200, null, "Create successfully!");
        }
       else
            return new Result<>(500,null,"Create failed!");
    }

    @GetMapping("/issue/get")
    public  Result<?> findAll (@RequestParam String repoOwner, @RequestParam String repoName) {
        try {
            List<Issue> issues = issueService.selectListByRepoName(repoOwner,repoName);
            return new Result<>(200,issues,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Get issues failed!");
        }
    }
}

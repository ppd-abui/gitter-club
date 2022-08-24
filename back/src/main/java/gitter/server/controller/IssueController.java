package gitter.server.controller;
import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.entity.Issue;
import gitter.server.service.IssueService;
import gitter.server.service.RepoService;
import gitter.server.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping()
public class IssueController {

    @Resource
    IssueService issueService;

    @PostMapping("/issue/new")
    public Result<?> newIssue(@RequestHeader("token") String token, @RequestBody Issue issue){

        if(issueService.createIssue(issue)) {
            return new Result<>(200, null, "Create successfully!");
        }
       else
            return new Result<>(500,null,"Create failed!");
    }
}

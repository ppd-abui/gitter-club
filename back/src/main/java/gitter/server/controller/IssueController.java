package gitter.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import gitter.server.common.Result;
import gitter.server.entity.Issue;
import gitter.server.entity.Repo;
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
            List<Issue> issues = issueService.selectByRepoName(repoOwner,repoName);
            return new Result<>(200,issues,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Get issues failed!");
        }
    }
    @GetMapping("/issue/get/content")
    public  Result<?> findContent(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String issueTitle) {
        try {
            Issue issue = issueService.selectByIssueTitle(repoOwner,repoName,issueTitle);
            return new Result<>(200, issue, "Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Get issues failed!");
        }
    }
    @GetMapping("/repo/assigner")
    public Result<?> manageAssignees(@RequestParam String issueTitle,@RequestParam String repoOwner, @RequestParam String repoName)
    {
        Issue issue=new Issue();
        issue.setIssueTitle(issueTitle);
        Issue res=issueService.selectByIssueTitle(repoOwner,repoName,issueTitle);
        String[] list;

        if(res.getIssueAssigners() != null)
            list = res.getIssueAssigners().split("_");
        else
            list = null;

        return  new Result<>(200,list,"Successfully!");
    }
    @GetMapping("/issue/search")
    public Result<?> repoSearch(@RequestParam String keyWord,@RequestParam  String repoName,@RequestParam String repoOwner){
        try {
            List<Issue> resIssue = issueService.selectListByKeyword(keyWord,repoOwner,repoName);
            return new Result<>(200,resIssue,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Search failed!");
        }
    }

    @GetMapping("/repo/assigner/add")
    public Result<?>addCollaborators(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String issueTitle,@RequestParam String newAsi){
        try {
            Issue issue=new Issue();
            issue.setIssueTitle(issueTitle);
            Issue res=issueService.selectByIssueTitle(repoOwner,repoName,issueTitle);
            String assignersString = res.getIssueAssigners();
            String[] list;
            list = assignersString.split("_");

            for(String item:list){
                if (item.equals(newAsi))
                    return new Result<>(500, null, "The user has been added as a assigner");
            }
            if(assignersString == null||assignersString.equals(""))
                assignersString = (newAsi+"_");
            else
                assignersString += (newAsi+"_");
            res.setIssueAssigners(assignersString);
            issueService.updateById(res);
            return new Result<>(200, null, "The repository collaborators");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Meet some mistake");
        }
    }
    @GetMapping("/repo/assigner/delete")
    public Result<?>deleteCollaborators(@RequestParam String issueTitle,@RequestParam String repoOwner,@RequestParam String repoName,@RequestParam String deleteAsi){
        try {
            Issue issue=new Issue();
            issue.setIssueTitle(issueTitle);
            Issue res=issueService.selectByIssueTitle(repoOwner,repoName,issueTitle);
            String assignersString = res.getIssueAssigners();


            String[] list;
            list = assignersString.split("_");
            String assignersStringNew = "";
            if(assignersString == null||assignersString.equals(""))
                return new Result<>(300, null, "Delete successfully");

            for (String s : list) {
                if (!s.equals(deleteAsi))
                    assignersStringNew += (s + "_");
            }

            res.setIssueAssigners(assignersStringNew);
            issueService.updateById(res);

            return new Result<>(200, null, "Delete successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Meet some mistake");
        }
    }
}

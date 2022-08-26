package gitter.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import  gitter.server.entity.Issue;

import java.util.List;


public interface IssueService  extends IService<Issue> {
    //创建工单
    boolean createIssue(Issue issue);
    //通过工单名搜索工单（账号+仓库名)
    Issue selectByIssueTitle(Issue issue);
    List<Issue> selectListByRepoName(String repoOwner, String repoName);
}

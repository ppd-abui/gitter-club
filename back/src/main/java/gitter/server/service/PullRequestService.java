package gitter.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.entity.PullRequest;
import gitter.server.entity.Repo;

import java.util.List;

public interface PullRequestService extends IService<PullRequest> {
    PullRequest selectByPullRequestId(Integer pullRequestId);
    boolean createPullRequest(PullRequest pullRequest);
    String diff(PullRequest pullRequest);
    boolean merge(PullRequest pullRequest);
    List<PullRequest> selectListByRepo(Repo repo);
}

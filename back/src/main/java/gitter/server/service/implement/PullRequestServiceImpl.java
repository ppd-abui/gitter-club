package gitter.server.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.entity.PullRequest;
import gitter.server.entity.Repo;
import gitter.server.mapper.PullRequestMapper;
import gitter.server.service.PullRequestService;
import gitter.server.utils.CmdUtils;
import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PullRequestServiceImpl extends ServiceImpl<PullRequestMapper, PullRequest> implements PullRequestService {

    @Resource
    PullRequestMapper pullRequestMapper;

    @Override
    public PullRequest selectByPullRequestId(Integer pullRequestId){
        return pullRequestMapper
                .selectOne(Wrappers.<PullRequest>lambdaQuery()
                        .eq(PullRequest::getPullRequestId,pullRequestId));
    }

    @Override
    public boolean createPullRequest(PullRequest pullRequest){
        try {
            pullRequestMapper.insert(pullRequest);

            String RepoOwner = pullRequest.getPullRequestRepoOwner();
            String RepoName = pullRequest.getPullRequestRepoName();

            //用户提交分支名
            String localBranch = pullRequest.getPullRequestLocalBranch();
            String pullCreator = pullRequest.getPullRequestCreator();

            Git git = JGitUtils.getRepository(RepoOwner, RepoName);

            //setRefSpecs：将用户提交的localBranch分支以用户名userAccount的形式存储
            git.fetch()
                    .setRemote(pullCreator)
                    .setRefSpecs("refs/heads/" + localBranch + ":refs/heads/" + pullCreator)
                    .call();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String diff(PullRequest pullRequest){

        String repoOwner = pullRequest.getPullRequestRepoOwner();
        String repoName = pullRequest.getPullRequestRepoName();

        //在fetch的时候仓库新建了一个与用户同名的分支
        String branch = pullRequest.getPullRequestCreator();

        String op = "cd " + JGitUtils.getBaseDir() + repoOwner + '/' + repoName + ";"
                + "git diff " + branch;

        return CmdUtils.run(op);
    }

    @Override
    public boolean merge(PullRequest pullRequest){
        String repoOwner = pullRequest.getPullRequestRepoOwner();
        String repoName = pullRequest.getPullRequestRepoName();

        //在fetch的时候仓库新建了一个与用户同名的分支
        String branch = pullRequest.getPullRequestCreator();

        //合并分支命令
        String opMerge = "cd " + JGitUtils.getBaseDir() + repoOwner + '/' + repoName + ";"
                + "git merge " + branch;
        CmdUtils.run(opMerge);

        //合并后删除fetch的分支
        String opDelete = "cd " + JGitUtils.getBaseDir() + repoOwner + '/' + repoName + ";"
                + "git branch -D " + branch;
        CmdUtils.run(opDelete);

        return true;
    }

    @Override
    public List<PullRequest> selectListByRepo(Repo repo){
        return pullRequestMapper
                .selectList(Wrappers.<PullRequest>lambdaQuery()
                .eq(PullRequest::getPullRequestRepoOwner,repo.getRepoOwner())
                .eq(PullRequest::getPullRequestRepoName,repo.getRepoName()));
    }
}

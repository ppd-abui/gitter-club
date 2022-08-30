package gitter.server.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.common.Commit;
import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.mapper.RepoMapper;
import gitter.server.service.RepoService;
import gitter.server.utils.CmdUtils;
import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.URIish;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepoServiceImpl extends ServiceImpl<RepoMapper, Repo> implements RepoService {

    @Resource
    RepoMapper repoMapper;

    @Override   //创建仓库
    public boolean createRepo(Repo repo){
        try {
            //在本地文件中新建仓库
            JGitUtils.createRepository(repo.getRepoOwner(),repo.getRepoName());

            //数据库中生成仓库记录
            repoMapper.insert(repo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean forkRepo(Repo repo, User forkUser){
        String remoteUrl = JGitUtils.getRepoUrl(repo);
        String localUrl = JGitUtils.getBaseDir() + forkUser.getUserAccount() + '/' + repo.getRepoName();

        //fork失败
        if(!JGitUtils.forkRepository(remoteUrl, localUrl))
            return false;

        try {
            //获取被fork的仓库，将其与用户fork的仓库进行关联，用于pull request
            Git remoteGit = JGitUtils.getRepository(repo.getRepoOwner(),repo.getRepoName());
            remoteGit.remoteAdd()
                    .setName(forkUser.getUserAccount())
                    .setUri(new URIish(localUrl))
                    .call();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //fork成功
        Repo forkRepo = new Repo();
        forkRepo.setRepoOwner(forkUser.getUserAccount());
        forkRepo.setRepoName(repo.getRepoName());
        forkRepo.setRepoVisibility(repo.getRepoVisibility());
        repoMapper.insert(forkRepo);
        return true;
    }

    @Override
    public List<Commit> selectCommitListByRepoAndBranch(Repo repo,String branch){
        String opCheckout = "cd " + JGitUtils.getBaseDir() + repo.getRepoOwner() + '/' + repo.getRepoName() + ";"
                + "git checkout " + branch;
        CmdUtils.run(opCheckout);

        String opGitLog = "cd " + JGitUtils.getBaseDir() + repo.getRepoOwner() + '/' + repo.getRepoName() + ";"
                + "git log --pretty=%h/%s/%cd/%cn --date=short";
        String resCommits = CmdUtils.run(opGitLog);

        assert resCommits != null;
        List<Commit> commitList = new ArrayList<>();
        for(String resLine : resCommits.split("\n")){
            String[] commitLine = resLine.split("/");
            Commit commit = new Commit();
            commit.setSHA(commitLine[0]);
            commit.setMessage(commitLine[1]);
            commit.setTime(commitLine[2]);
            commit.setCommitter(commitLine[3]);
            commitList.add(commit);
        }

        return commitList;
    }

    @Override   //通过仓库名搜索仓库（账号+仓库名）
    public Repo selectByRepoName(Repo repo){
        return repoMapper
                .selectOne(Wrappers.<Repo>lambdaQuery()
                .eq(Repo::getRepoOwner,repo.getRepoOwner())
                .eq(Repo::getRepoName,repo.getRepoName()));
    }

    @Override
    public List<Repo> selectListByRepoOwner(Repo repo){
        return repoMapper
                .selectList(Wrappers.<Repo>lambdaQuery()
                .eq(Repo::getRepoOwner,repo.getRepoOwner()));
    }

    @Override
    public List<Repo> selectListByKeyword(String keyword){
        return repoMapper.selectList(Wrappers.<Repo>lambdaQuery()
                .eq(Repo::getRepoVisibility,"public")
                .like(Repo::getRepoName,keyword).or()
                .like(Repo::getRepoBio,keyword));
    }

    @Override
    public boolean createNewBranch(Repo repo, String sourceBranch,String newBranch){
        return JGitUtils.createNewBranch(repo.getRepoOwner(),repo.getRepoName(),sourceBranch,newBranch);
    }

    @Override
    public boolean updateRepo(Repo repo){
        try {
            repoMapper.updateById(repo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRepo(Repo repo){
        try {
            repoMapper.deleteById(repo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Repo selectByRepoNameOnly(Repo repo){
        return repoMapper
                .selectOne(Wrappers.<Repo>lambdaQuery()
                        .eq(Repo::getRepoName,repo.getRepoName()));
    }
}

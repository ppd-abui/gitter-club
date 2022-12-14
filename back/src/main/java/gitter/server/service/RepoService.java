package gitter.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.common.Commit;
import gitter.server.entity.Repo;
import gitter.server.entity.User;

import java.util.List;

public interface RepoService extends IService<Repo> {
    //创建仓库
    boolean createRepo(Repo repo);
    //fork仓库
    boolean forkRepo(Repo repo,User forkUser);
    //通过仓库名搜索仓库（账号+仓库名)
    Repo selectByRepoName(Repo repo);
    List<Repo> selectListByRepoOwner(Repo repo);
    List<Repo> selectListByKeyword(String keyword);
    boolean createNewBranch(Repo repo, String sourceBranch,String newBranch);
    List<Commit> selectCommitListByRepoAndBranch(Repo repo,String branch);
    boolean updateRepo(Repo repo);
    boolean deleteRepo(Repo repo);
    Repo selectByRepoNameOnly(Repo repo);

}

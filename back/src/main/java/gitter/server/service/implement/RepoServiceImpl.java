package gitter.server.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.mapper.RepoMapper;
import gitter.server.service.RepoService;
import gitter.server.utils.JGitUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        String localUrl = JGitUtils.getBaseDir() + forkUser.getUserAccount() + '/';

        //fork失败
        if(!JGitUtils.forkRepository(remoteUrl, localUrl))
            return false;

        //fork成功
        Repo forkRepo = new Repo();
        forkRepo.setRepoOwner(forkUser.getUserAccount());
        forkRepo.setRepoName(repo.getRepoName());
        forkRepo.setRepoVisibility(repo.getRepoVisibility());
        repoMapper.insert(forkRepo);
        return true;
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
        return repoMapper
                .selectList(Wrappers.<Repo>lambdaQuery()
                .like(Repo::getRepoName,keyword)
                .like(Repo::getRepoBio,keyword));
    }
}

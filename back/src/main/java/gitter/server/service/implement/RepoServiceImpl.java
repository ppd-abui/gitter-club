package gitter.server.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.entity.Repo;
import gitter.server.mapper.RepoMapper;
import gitter.server.service.RepoService;
import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class RepoServiceImpl extends ServiceImpl<RepoMapper, Repo> implements RepoService {

    @Resource
    RepoMapper repoMapper;

    @Override
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
    public Repo selectByRepoName(Repo repo){
        return repoMapper
                .selectOne(Wrappers.<Repo>lambdaQuery()
                .eq(Repo::getRepoOwner,repo.getRepoOwner())
                .eq(Repo::getRepoName,repo.getRepoName()));
    }
}

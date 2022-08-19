package gitter.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.entity.Repo;

public interface RepoService extends IService<Repo> {
    //创建仓库
    boolean createRepo(Repo repo);
    //通过仓库名搜索仓库（账号+仓库名)
    Repo selectByRepoName(Repo repo);
}

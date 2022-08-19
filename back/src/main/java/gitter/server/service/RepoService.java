package gitter.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.entity.Repo;

public interface RepoService extends IService<Repo> {
    boolean createRepo(Repo repo);
    Repo selectByRepoName(Repo repo);
}

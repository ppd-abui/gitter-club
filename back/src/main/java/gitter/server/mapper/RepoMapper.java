package gitter.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gitter.server.entity.Repo;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoMapper extends BaseMapper<Repo> {
}

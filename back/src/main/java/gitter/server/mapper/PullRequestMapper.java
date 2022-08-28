package gitter.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gitter.server.entity.PullRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface PullRequestMapper extends BaseMapper<PullRequest> {
}

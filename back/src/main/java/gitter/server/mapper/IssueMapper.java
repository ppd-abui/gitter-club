package gitter.server.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gitter.server.entity.Issue;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueMapper  extends BaseMapper<Issue> {
}

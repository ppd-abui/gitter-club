package gitter.server.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gitter.server.entity.Event;
import org.springframework.stereotype.Repository;
@Repository
public interface EventMapper extends BaseMapper <Event>{
}

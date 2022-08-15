package gitter.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gitter.server.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}

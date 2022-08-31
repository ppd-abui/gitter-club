package gitter.server.service.implement;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.entity.Event;
import gitter.server.entity.Issue;
import gitter.server.entity.Repo;
import gitter.server.mapper.EventMapper;
import gitter.server.service.EventService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class EventServiceIml extends ServiceImpl <EventMapper,Event>  implements EventService {
    @Resource
    EventMapper eventMapper;
    @Override
     public boolean createEvent(Event event){
        eventMapper.insert(event);
        return  true;
    }

    @Override
    public   List<Event> selectByUserAccount(String userAccount) {
        return eventMapper.selectList(Wrappers.<Event>lambdaQuery()
                .eq(Event::getEventUsername,userAccount));
    }

}

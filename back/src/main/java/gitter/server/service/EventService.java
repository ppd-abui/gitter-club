package gitter.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import gitter.server.entity.Event;

import java.util.List;


public interface EventService extends IService<Event> {
    boolean createEvent(Event event);

    List<Event> selectByUserAccount(String userAccount);
}

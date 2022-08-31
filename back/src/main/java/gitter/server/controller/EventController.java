package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Event;
import gitter.server.entity.Issue;
import gitter.server.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping()
public class EventController {
    @Resource
    EventService eventService;

    @GetMapping("/event/get")
    public Result<?> findEvent(@RequestParam String userAccount) {
        try {
            List<Event> events = eventService.selectByUserAccount(userAccount);
            return new Result<>(200, events, "Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Get issues failed!");
        }
    }
}
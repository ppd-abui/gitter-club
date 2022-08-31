package gitter.server.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("event")
@Data
public class Event {
    @TableId(type = IdType.AUTO)
    private Integer eventId;
    private String eventType;
    private String eventTitle;
    private String eventUsername;
    private String eventReponame;
    private String eventAssigners;
}

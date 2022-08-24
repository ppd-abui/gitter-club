package gitter.server.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("issue")
@Data
public class Issue {
    @TableId(type = IdType.AUTO)
    private  Integer issueId;
    private  String issueTitle;
    private  String issueContent;
    private  String issueUsername;
    private  String issueReponame;
}

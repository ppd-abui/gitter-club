package gitter.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//仓库类，由于和@Repository重名，简写为Repo
@TableName("repository")
@Data
public class Repo {
    @TableId(type = IdType.AUTO)
    private Integer repoId;
    private String repoOwner;
    private String repoName;
    private String repoBio;
    private String repoVisibility;
    private String repoFollowers;
    private String repoIssues;
    private String repoCollaborators;
    private Integer repoFollowNum;
}

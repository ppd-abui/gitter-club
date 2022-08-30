package gitter.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("pull_request")
@Data
public class PullRequest {
    private Integer pullRequestId;
    private String pullRequestRepoOwner;
    private String pullRequestRepoName;
    private String pullRequestRemoteBranch;
    private String pullRequestLocalBranch;
    private String pullRequestCreator;
    private String pullRequestTheme;
}

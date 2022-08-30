package gitter.server.common;

import lombok.Data;

@Data
public class Commit {
    private String SHA; //历史版本号哈希值7位
    private String message;
    private String time;
    private String committer;
}

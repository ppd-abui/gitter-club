package gitter.server.common;

import lombok.Data;

@Data
public class FileType {
    private String FileType;
    private String FileName;
    private String lastCommitMessage;
}

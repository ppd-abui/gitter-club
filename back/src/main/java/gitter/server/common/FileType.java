package gitter.server.common;

import lombok.Data;

@Data
public class FileType {
    private String FileType;
    private String FileName;
    public FileType(String fileType,String fileName){
        this.FileType=fileType;
        this.FileName=fileName;
    }
}

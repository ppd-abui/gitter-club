package gitter.server.controller;

import ch.qos.logback.core.util.FileUtil;
import gitter.server.common.Result;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String rootFilePath = System.getProperty("user.dir") + "/back/src/resources/files/" + originalFileName;
        System.out.println(originalFileName);
        System.out.println(rootFilePath);
        return new Result<>();
    }
}

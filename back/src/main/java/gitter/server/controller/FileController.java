package gitter.server.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import gitter.server.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping()
public class FileController {

    @Value("${server.port}")
    private String port;
    private static final String ip = "http://localhost";

    private final String root = "E:/idea/repository/fileUploadTest/";

    @PostMapping("/files/upload")
    public Result<?> upload(MultipartFile file){
        String originalFileName = file.getOriginalFilename();

        String rootPath = root + originalFileName;

        try {
            FileUtil.writeBytes(file.getBytes(), rootPath);
            return new Result<>(200,root,"上传成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<>(500,null,"上传失败！");
        }
    }

    @GetMapping("{flag}")
    public void download(@PathVariable String flag, HttpServletResponse response){
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(root);
        String fileName = fileNames.stream().filter(name -> name.equals(flag)).findAny().orElse("");
        try{
            if(StrUtil.isNotEmpty(fileName)){
                response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(root + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

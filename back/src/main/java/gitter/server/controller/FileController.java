package gitter.server.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import gitter.server.common.FileType;
import gitter.server.common.Result;
import gitter.server.service.UserService;
import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class FileController {

    @Value("${server.port}")
    private String port;
    private static final String ip = "http://localhost";

    @PostMapping("/files/upload")
    public Result<?> upload(MultipartFile file, @RequestParam Map<Object,Object> map) {
        //原始文件名
        String originalFileName = file.getOriginalFilename();

        //获取repoName、dir和commitMessage
        String userAccount = map.get("repoOwner").toString();
        String repoName = map.get("repoName").toString();
        String dir = map.get("dir").toString();
        String commitMessage = map.get("commitMessage").toString();

        //目标写入路径
        String desDir;
        if (!dir.equals(""))
            desDir = JGitUtils.getBaseDir() + userAccount + '/' + repoName + '/' + dir + '/' + originalFileName;
        else
            desDir = JGitUtils.getBaseDir() + userAccount + '/' + repoName + '/' + originalFileName;

        try {
            FileUtil.writeBytes(file.getBytes(), desDir);
            //git add *
            Git git = JGitUtils.getRepository(userAccount,repoName);
            git.add().addFilepattern(".").call();
            //git commit
            git.commit().setMessage(commitMessage).call();

            return new Result<>(200, desDir, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "上传失败！");
        }
    }

    @GetMapping("/{flag}")
    public void download(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(JGitUtils.getBaseDir());
        String fileName = fileNames.stream().filter(name -> name.equals(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(JGitUtils.getBaseDir() + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/tree")
    public Result<?> getFiles(@RequestParam String repoOwner,@RequestParam String repoName,
                         @RequestParam String branch,@RequestParam String suffixDir){

        String path = JGitUtils.getBaseDir() + repoOwner + '/' + repoName + '/' + suffixDir;
        File file = new File(path);
        if (file.isFile()){
            try {
                String data = fileToString(file.getPath());
                return new Result<>(200,data,"File");
            } catch (IOException e) {
                e.printStackTrace();
                return new Result<>(500,null,"System error!");
            }
        }

        File[] files = file.listFiles();
        List<FileType> fileTypes = new ArrayList<>();
        assert files != null;

        for(File f:files){
            if (f.isHidden())
                continue;
            else if (f.isDirectory())
                fileTypes.add(new FileType("Dir",f.getName()));
            else if (f.isFile())
                fileTypes.add(new FileType("File",f.getName()));
        }
        return new Result<>(200,fileTypes,"Directory");
    }


    //将File转化成String
    private static String fileToString(String filePath) throws IOException {
        //对一串字符进行操作
        StringBuffer fileData = new StringBuffer();
        //
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        //缓冲区使用完必须关掉
        reader.close();
        System.out.println(fileData.toString());
        return fileData.toString();
    }

    @GetMapping("/test")
    public Result<?> test(HttpServletResponse response){
        String path = JGitUtils.getBaseDir() + "admin/testToken/test.cpp";
        String data = null;
        try {
            data = fileToString(path);
            System.out.println(data);
            return new Result<>(200,data,"");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<>(500,null,"System error!");
        }
    }
}
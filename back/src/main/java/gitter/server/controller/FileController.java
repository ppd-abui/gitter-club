package gitter.server.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import gitter.server.common.FileType;
import gitter.server.common.Result;
import gitter.server.utils.CmdUtils;
import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

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

    @GetMapping("/download")
    public void download(@RequestParam String repoOwner,
                         @RequestParam String repoName,
                         @RequestParam(defaultValue = "HEAD") String sha,
                         HttpServletResponse response) {
        OutputStream os;
        String fileName = repoName + ".zip";

        //压缩
        String opZip = "cd " + JGitUtils.getBaseDir() + repoOwner + '/' + repoName + ";"
                + "git archive -o " + fileName + " " + sha;
        CmdUtils.run(opZip);

        try {

            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            byte[] bytes = FileUtil.readBytes(JGitUtils.getBaseDir() + repoOwner + '/' + repoName + '/' + fileName);
            os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();

            //删除压缩包
            String opRm = "cd " + JGitUtils.getBaseDir() + repoOwner + '/' + repoName + ";"
                    + "rm " + fileName;
            CmdUtils.run(opRm);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/tree")
    public Result<?> getFiles(@RequestParam(defaultValue = "") String repoOwner,
                              @RequestParam(defaultValue = "") String repoName,
                              @RequestParam(defaultValue = "") String branch,
                              @RequestParam(defaultValue = "" ) String suffixDir){


        if (repoOwner.equals("")||repoName.equals("")||branch.equals(""))
            return new Result<>(200,null,"不应该的请求！");

        Git git;
        //切换分支
        try {
            git = JGitUtils.getRepository(repoOwner,repoName);
            git.checkout().setName(branch).call();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Change branch failed!");
        }

        //获取路径
        String path = JGitUtils.getBaseDir() + repoOwner + '/' + repoName + '/' + suffixDir;
        File file = new File(path);

        //若为文件: File->String 回传
        if (file.isFile()){
            try {
                String data = fileToString(file.getPath());
                git.checkout().setName("master").call();
                return new Result<>(200,data,"File");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result<>(500,null,"System error!");
            }
        }

        //路径为目录：获得文件夹信息回传
        File[] files = file.listFiles();
        List<FileType> fileTypes = new ArrayList<>();

        assert files != null;
        for(File f:files){
            FileType fileType = fileToFileType(f,git,suffixDir);
            if(fileType!=null)
                fileTypes.add(fileType);
        }
        try {
            git.checkout().setName("master").call();
            return new Result<>(200,fileTypes,"Directory");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"System error!");
        }
    }

    @GetMapping("/files/save")
    public Result<?> fileSave(@RequestParam String fileContent,@RequestParam String suffixDir){
        String filePath = JGitUtils.getBaseDir() + suffixDir;
        if(stringToFile(fileContent,filePath)){
            try {
                String[] suffixDirs = suffixDir.split("/");

                //git add *
                Git git = JGitUtils.getRepository(suffixDirs[0],suffixDirs[1]);
                git.add().addFilepattern(".").call();
                //git commit
                git.commit().setMessage("Modify " + suffixDirs[suffixDirs.length-1]).call();

                return new Result<>(200,null,"File written successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result<>(500,null,"File save failed!");
            }
        }
        else
            return new Result<>(500,null,"File save failed!");
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
        return fileData.toString();
    }

    private static FileType fileToFileType(File file, Git git,String suffixDir){

        if (file.isHidden())
            return null;

        try{
            FileType fileType = new FileType();

            if(!suffixDir.equals(""))
                suffixDir+='/';

            String fileName = file.getName();
            String filePath = suffixDir + fileName;
            String lastCommitMessage =
                    git.blame()
                            .setFilePath(filePath)
                            .call()
                            .getSourceCommit(0)
                            .getFullMessage();

            fileType.setFileName(fileName);
            fileType.setLastCommitMessage(lastCommitMessage);

            if (file.isDirectory())
                fileType.setFileType("Dir");
            else
                fileType.setFileType("File");

            return fileType;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean stringToFile(String fileContent,String filePath){
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new StringReader(fileContent));
            bw = new BufferedWriter(new FileWriter(filePath));
            char[] buf = new char[1024 * 64];          //字符缓冲区
            int len;
            while ((len = br.read(buf)) != -1) {
                bw.write(buf, 0, len);
            }
            bw.flush();
            br.close();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/command")
    public Result<?> command(@RequestParam String repoOwner,
                             @RequestParam(defaultValue = "") String dir,
                             @RequestParam(defaultValue = "") String cmd) throws Exception{
        //存储操作历史
        List<String> cmdRecords = new ArrayList<>();

        //存储返回数据（"dir": ,"result":)
        Map<Object,Object> map = new HashMap<>();

        //存储命令执行结果
        List<String> result = new ArrayList<>();

        String originPath;

        File file = new File(JGitUtils.getBaseDir() + repoOwner + "/cmdHistory.txt");
        if(!file.exists())
            file.createNewFile();

        //无命令则返回路径:第一次获取请求
        if(dir.equals("")){
            result.addAll(Arrays.asList(fileToString(JGitUtils.getBaseDir() + repoOwner + "/cmdHistory.txt").split("\n")));
            originPath=CmdUtils.run("pwd").split("\n")[3];
        } else {
            originPath=dir;
        }

        //将路径存储至map
        map.put("dir",originPath);
        map.put("result",result);

        //将当前行命令添加
        cmdRecords.add("PS " + originPath + "> " + cmd);
        result.add("PS " + originPath + "> " + cmd);

        //无命令，命令行写入文件返回结果
        if(cmd.equals("")){
            appendFile(repoOwner,cmdRecords);
            return new Result<>(200,map,"No command!");
        }

        //若为路径跳转，更新路径originPath
        if(cmd.startsWith("cd ")){
            String op = "cd " + dir + ";" + cmd + ";pwd";
            originPath=CmdUtils.run(op).split("\n")[3];
            map.put("dir",originPath);

            //result即为跟新后的路径originPath
            result.add("PS " + originPath + "> ");
            cmdRecords.add("PS " + originPath + "> ");
            map.put("result",result);
        } else {
            //正常命令，存储执行结果
            String op = "cd " + dir + ";" + cmd;
            result = Arrays.asList(CmdUtils.run(op).split("\n"));
            cmdRecords.addAll(result);
            map.put("result",result);
        }

        //将本次运行结果存储至历史记录文件
        appendFile(repoOwner,cmdRecords);
        return new Result<>(200,map,"Cmd successfully!");
    }

    private static boolean appendFile(String repoOwner,List<String> cmdRecords){
        String filePath = JGitUtils.getBaseDir() + repoOwner + "/cmdHistory.txt";
        File file=new File(filePath);
        try {
            RandomAccessFile raf=new RandomAccessFile(file, "rw");

            //将写文件指针移到文件尾。
            raf.seek(raf.length());
            for(String line:cmdRecords)
                raf.writeBytes(line + "\n");
            raf.close();

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
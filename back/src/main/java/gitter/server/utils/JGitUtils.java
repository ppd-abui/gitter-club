package gitter.server.utils;

import gitter.server.entity.Repo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;

public class JGitUtils {

    private final static String baseDir = "D:/Coding/repository/";

    //创建并初始化Git仓库, dir为创建的仓库路径：用户名/仓库名
    public static Git createRepository(String userAccount, String repoName) throws Exception{
        String repoDir = baseDir + userAccount + "/" + repoName;
        File repoFile = new File(repoDir);

        //检查仓库是否已经存在
        if(repoFile.exists()){
            throw new Exception("Repository has already existed!");
        }

        //不存在则创建一个仓库
        Repository repo = FileRepositoryBuilder.create(new File(repoDir + "/.git"));
        repo.create();

        return new Git(repo);
   }

   public static Git getRepository(String userAccount,String repoName) throws IOException{
       String repoDir = baseDir + userAccount + "/" + repoName;

       Repository repository = new FileRepositoryBuilder()
               .setGitDir(Paths.get(repoDir,".git").toFile())
               .build();

       return new Git(repository);
   }

   public static String getRepoUrl(Repo repo){
        return baseDir + repo.getRepoOwner() + "/" + repo.getRepoName();
   }

   public static boolean forkRepository(String remoteUrl,String localUrl){
        try{
            Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(new File(localUrl))
                    .call();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
   }

   public static String getBaseDir(){
        return baseDir;
   }
}

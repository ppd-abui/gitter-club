package gitter.server.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;

public class JGitUtils {

    private final static String baseDir = "D:/Coding/Repo";

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

   public static Git getRepository(String userAccount,String repoName) throws IOException, GitAPIException{
       String repoDir = baseDir + userAccount + "/" + repoName;

       Repository repository = new FileRepositoryBuilder()
               .setGitDir(Paths.get(repoDir,".git").toFile())
               .build();

       return new Git(repository);
   }
}

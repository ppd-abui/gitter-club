package gitter.server.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GitUtils {

    private final static String baseDir = "E:/idea/repository/";

    //创建并初始化Git仓库, dir为创建的仓库路径：用户名/仓库名
    public static Git createRepository(String userName,String repoName) throws IOException, GitAPIException{
        String repoDir = baseDir + userName + "/" + repoName;
        File repoFile = new File(repoDir);

        //检查仓库是否已经存在
        if(repoFile.exists())
            return null;

        //不存在则创建一个仓库
        Repository repo = FileRepositoryBuilder.create(new File(repoDir + "/.git"));
        repo.create();

        return new Git(repo);
   }

   public static Git getRepository(String userName,String repoName) throws IOException, GitAPIException{
       String repoDir = baseDir + userName + "/" + repoName;

       Repository repository = new FileRepositoryBuilder()
               .setGitDir(Paths.get(repoDir,".git").toFile())
               .build();

       return new Git(repository);
   }
}

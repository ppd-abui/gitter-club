package gitter.server;

import java.io.*;
import java.util.Arrays;
import java.util.List;


import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.NameRevCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.junit.jupiter.api.Test;


/**
 * 用户搜索："/user/search"  请求方式：Get      模糊匹配匹配用户名
 * 仓库搜索："/repo/search"  请求方式：Get      模糊匹配仓库名、仓库简介
 * 搜索变量名：keyWord
 */

/**
 * 返回格式：
 * {
 *      code:
 *      data:[]     数组，每一项为一个Repo(User)对象
 *      msg:
 * }
 */


public class JGit {

    public static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }

    //查看Git仓库文件状态status
    public static void statusOperation() throws IOException, GitAPIException{
        Git git = getTestGit();

        //获取仓库的Status对象
        Status status = git.status().call();

        //查看已添加的文件,返回Set<String>
        status.getAdded();

        //查看发生变化的文件
        status.getChanged();

        //查看未追踪的文件
        status.getUntracked();

        //在日志中查看
        System.out.println(status.getUntracked().toString());
    }

    //分支遍历、创建、删除、签出
    public static void branchOperation() throws IOException, GitAPIException{

        Git git = getTestGit();

        List<Ref> refs = git.branchList().call();
        for(Ref ref:refs){
            System.out.println(ref.getName());
        }

        String branchName = "dev";

        for(Ref ref:refs){
            if(ref.getName().equals("refs/heads/" + branchName)){
                System.out.println("Removing branch before");
                git.branchDelete()
                        .setBranchNames(branchName)
                        .setForce(true)
                        .call();
                break;
            }
        }

        //删除分支
        git.branchDelete().setBranchNames(branchName).call();

        git.branchCreate().setName(branchName).call();
        git.checkout().setName(branchName).call();
    }

    //查看commit差异
    public static void diff() throws IOException, GitAPIException{

        Git git = getTestGit();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AbstractTreeIterator newTreeIter = prepareTreeParser(git.getRepository(), git.getRepository().resolve("HEAD").getName());
        AbstractTreeIterator oldTreeIter = prepareTreeParser(git.getRepository(), git.getRepository().resolve("HEAD^").getName());
        git.diff()
                .setNewTree(newTreeIter)  //设置源，不设置则默认工作区和历史最新commit版本比较
                .setOldTree(oldTreeIter)
//                    .setPathFilter(PathFilter.create(".txt"))  //设置过滤
                .setOutputStream(outputStream) //输出流  用outputStream，后面转成字符串
                .call();
        System.out.println(outputStream);
    }

    //日志查看
    public static void log() throws Exception{
        Git git = getTestGit();
        String msg = git.blame().setFilePath("level1/sss.txt").call().getSourceCommit(0).getFullMessage();

        Iterable<RevCommit> commits = git.log().call();
        int count = 0;
        for (RevCommit commit : commits){
            System.out.println("Commit:" + commit.getName());
            System.out.println("Committer:" + commit.getAuthorIdent().getName());
            System.out.println("Message:" + commit.getFullMessage());
        }
    }

    /**
     * 命令行操作git
     */
    public static void cmdOperation(String op){
        Runtime run = Runtime.getRuntime();

//        op = "cd /idea/repository/admin/bareRepo;ls";

        try {
            String[] cmd =new String[]{"powershell.exe",op};

            Process p = run.exec(cmd);

            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            p.waitFor();
            if (p.exitValue() != 0) {
                //说明命令执行失败
                //可以进入到错误处理步骤中
            }
            String result = null;
            String s = null;
            while ((s = reader.readLine()) != null) {
                result+=s + '\n';
//                System.out.println(s);
                p.waitFor();
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //获取测试仓库的Git对象
    public static Git getTestGit() throws IOException, GitAPIException{
        String userName = "admin";
        String repoName = "bareRepo";
        //仓库路径
        String dir = "E:/idea/repository/" + userName + "/" + repoName + "/";

//        现实仓库路径
//        System.out.println(git2.getRepository().getDirectory());

        return JGitUtils.getRepository(userName,repoName);
    }


    public static void main(String[] args) throws Exception{

        Git git = getTestGit();

//        git.remoteAdd()
//                .setName("origin")
//                .setUri(new URIish("E:/idea/repository/ppd-abui/bareRepo"))
//                .call();

//        git.pull()
//                .setRemote("origin")
//                .setRemoteBranchName("master")
//                .call();

//        git.fetch()
//                .setRemote("origin")
//                .setRefSpecs("refs/heads/master:refs/heads/temp")
//                .call();

        cmdOperation("cd /idea/repository/admin/bareRepo;ls");
    }
}
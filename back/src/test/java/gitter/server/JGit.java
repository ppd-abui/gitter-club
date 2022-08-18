package gitter.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


import gitter.server.utils.GitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

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

    //获取测试仓库的Git对象
    public static Git getTestGit() throws IOException, GitAPIException{
        String userName = "ppd-abui";
        String repoName = "testJGit";
        //仓库路径
        String dir = "E:/idea/repository/" + userName + "/" + repoName + "/";

//        现实仓库路径
//        System.out.println(git2.getRepository().getDirectory());

        return GitUtils.getRepository(userName,repoName);
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

    //分支遍历、创建、删除
    public static void branchOperation() throws IOException, GitAPIException{

        Git git = getTestGit();

        List<Ref> refs = git.branchList().call();
        for(Ref ref:refs){
            System.out.println(ref.getName());
        }

        git.branchCreate().setName("test").call();
        git.branchDelete().setBranchNames("test").call();
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

    public static void main(String[] args) throws IOException, GitAPIException {

        Git git = getTestGit();
        git.add().addFilepattern(".").call();
    }
}
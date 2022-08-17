package gitter.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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


    public static void main(String[] args) throws IOException, GitAPIException {
        String userName = "zyyyyyyyy";
        String repoName = "testJGit2";
        //仓库路径
        String dir = "E:/idea/repository/" + userName + "/" + repoName + "/";


        Git git = GitUtils.getRepository(userName,repoName);
//        System.out.println(git2.getRepository().getDirectory());
//        git2.add().addFilepattern("test.txt").call();

//        Status status = git.status().call();
//        System.out.println(status.getUntracked().toString());
//        System.out.println("-------------------------");

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
}

/*
        List<Ref> refs = git2.branchList().call();
        for(Ref ref:refs){
            System.out.println(ref.getName());
        }

        git2.branchCreate().setName("test").call();
        git2.branchDelete().setBranchNames("test").call();
        System.out.println("-------------------------");
* */
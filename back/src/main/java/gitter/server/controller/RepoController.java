package gitter.server.controller;

import gitter.server.common.Result;
import gitter.server.entity.Repo;
import gitter.server.entity.User;
import gitter.server.service.RepoService;
import gitter.server.service.UserService;
import gitter.server.utils.CmdUtils;
import gitter.server.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping()
public class RepoController {

    @Resource
    RepoService repoService;

    @Resource
    UserService userService;

    @PostMapping("/repo/new")
    public Result<?> newRepo(@RequestHeader("token") String token, @RequestBody Repo repo) {
        User res = userService.selectByToken(token);
        if (res == null)
            return new Result<>(500, null, "Couldn't find user!");

        repo.setRepoOwner(res.getUserAccount());

        if (repoService.createRepo(repo))
            return new Result<>(200, null, "Create successfully!");
        else
            return new Result<>(500, null, "Create failed!");
    }

    @PostMapping("/repo/name")
    public Result<?> isRepoNameExist(@RequestHeader("token") String token, @RequestBody Repo repo) {
        User resUser = userService.selectByToken(token);
        if (resUser == null)
            return new Result<>(500, null, "Create failed!");

        repo.setRepoOwner(resUser.getUserAccount());
        Repo resRepo = repoService.selectByRepoName(repo);

        if (resRepo == null)
            return new Result<>(500, null, "This repository name is available");
        else
            return new Result<>(200, resRepo, "The repository name already exists on this account");
    }


    @GetMapping("/repo/name")
    public Result<?> isRepoNameExist(@RequestParam String repoOwner, @RequestParam String repoName) {
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);

        if (res == null)
            return new Result<>(500, null, "This repository name is available");
        else
            return new Result<>(200, res, "The repository name already exists on this account");
    }

    @GetMapping("/url")
    public Result<?> getUrl(@RequestParam String repoOwner, @RequestParam String repoName){

        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        try {
            String url = JGitUtils.getRepoUrl(repo);
            return new Result<>(200,url,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,"error","获取失败!");
        }
    }

    @GetMapping("/repo/info")
    public Result<?> getRepos(@RequestParam String userAccount){
        try{
            User user = userService.selectByUserAccount(userAccount);
            Repo repo = new Repo();
            repo.setRepoOwner(user.getUserAccount());
            List<Repo> repos = repoService.selectListByRepoOwner(repo);
            return new Result<>(200,repos,"获取成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"获取失败！");
        }
    }

    @PostMapping("/fork")
    public Result<?> fork(@RequestHeader("token") String token, @RequestBody Repo repo){

        User forkUser = userService.selectByToken(token);
        if (forkUser==null)
            return new Result<>(500,null,"Couldn't find user!");

        if (repoService.forkRepo(repo,forkUser))
            return new Result<>(200,null,"Fork successfully!");
        else
            return new Result<>(500,null,"System error, fork failed!");
    }

    @GetMapping("/repo/search")
    public Result<?> repoSearch(@RequestParam String keyWord, @RequestParam String searchSort){
        try {
            List<Repo> resRepos = repoService.selectListByKeyword(keyWord);

            if(searchSort.equals("name")){
                Collections.sort(resRepos, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        return o1.getRepoName().compareTo( o2.getRepoName());
                    }
                });
            }else if(searchSort.equals("stars")){
                Collections.sort(resRepos, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        return o2.getRepoFollowNum() - o1.getRepoFollowNum();
                    }
                });
            }
            return new Result<>(200,resRepos,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"Search failed!");
        }
    }

    @GetMapping("/repo/branches")
    public Result<?> getBranches(@RequestParam String repoOwner,@RequestParam String repoName){

        try {
            Git git = JGitUtils.getRepository(repoOwner,repoName);
            List<Ref> branchRefs = git.branchList().call();

            List<String> branches = new ArrayList<>();
            for(Ref branchRef:branchRefs)
                branches.add(branchRef.getName().split("/")[2]);

            return new Result<>(200,branches,"Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500,null,"System error!");
        }
    }

    @GetMapping("/branch/new")
    public Result<?> createNewBranch(@RequestParam String repoOwner,
                                     @RequestParam String repoName,
                                     @RequestParam String sourceBranch,
                                     @RequestParam String newBranch){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);

        if(repoService.createNewBranch(repo,sourceBranch,newBranch))
            return new Result<>(200,null,"Successfully!");
        else
            return new Result<>(500,null,"Create failed!");
    }

    @DeleteMapping("/branch/delete")
    public Result<?> deleteBranch(@RequestParam String repoOwner,
                                  @RequestParam String repoName,
                                  @RequestParam String branch){

        String op = "cd " + JGitUtils.getBaseDir() + repoOwner + '/' + repoName + ";"
                + "git branch -D " + branch;
        if(CmdUtils.run(op)!=null)
            return new Result<>(200,null,"Delete branch " + branch + "successfully!");
        else
            return new Result<>(500,null,"Delete branch " + branch + "failed!");
    }

    @GetMapping("/commits/info")
    public Result<?> getCommitsInfo(@RequestParam String repoOwner,
                                    @RequestParam String repoName,
                                    @RequestParam String branch){
        if (repoOwner.equals("")||repoName.equals("")||branch.equals(""))
            return new Result<>(200,null,"不应该的请求！");

        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        try{
            return new Result<>(200,repoService.selectCommitListByRepoAndBranch(repo,branch),"Get commits info successfully!");
        } catch (Exception e){
            e.printStackTrace();
            return new Result<>(500,null,"Get commits info failed!");
        }
    }

    @GetMapping("/repo/col")
    public Result<?>getCollaborators(@RequestParam String repoOwner, @RequestParam String repoName){
        try {
            Repo repo = new Repo();
            repo.setRepoOwner(repoOwner);
            repo.setRepoName(repoName);
            Repo res = repoService.selectByRepoName(repo);

            String[] list;

            if(res.getRepoCollaborators() != null)
                list = res.getRepoCollaborators().split("_");
            else
                list = null;

            return new Result<>(200, list, "The repository collaborators");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Meet some mistake");
        }
    }

    @GetMapping("/repo/col/add")
    public Result<?>addCollaborators(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String newCol){
        try {
            Repo repo = new Repo();
            repo.setRepoOwner(repoOwner);
            repo.setRepoName(repoName);
            Repo res = repoService.selectByRepoName(repo);
            String collaboratorsString = res.getRepoCollaborators();

            if (newCol .equals( repoOwner))
                return new Result<>(500, null, "You are the owner!!!");

            String[] list;
            list = collaboratorsString.split("_");

            for(String item:list){
                if (item .equals( newCol))
                    return new Result<>(500, null, "The user has been added as a collaborator");
            }


            if (collaboratorsString == null)
                collaboratorsString = (newCol + "_");
            else
                collaboratorsString += (newCol + "_");


            res.setRepoCollaborators(collaboratorsString);
            repoService.updateById(res);


            return new Result<>(200, null, "The repository collaborators");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Meet some mistake");
        }
    }

    @GetMapping("/repo/col/delete")
    public Result<?>deleteCollaborators(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String deleteCol){
        try {
            Repo repo = new Repo();
            repo.setRepoOwner(repoOwner);
            repo.setRepoName(repoName);
            Repo res = repoService.selectByRepoName(repo);
            String collaboratorsString = res.getRepoCollaborators();


            String[] list;
            list = collaboratorsString.split("_");
            String collaboratorsStringNew = "";

            for(int i = 0;i <list.length;i++){
                System.out.println(list[i]);
                if(!list[i].equals(deleteCol))
                    collaboratorsStringNew += (list[i]+"_");
            }

            System.out.println(collaboratorsStringNew);
            System.out.println(deleteCol);
            System.out.println(collaboratorsStringNew.equals(""));

            res.setRepoCollaborators(collaboratorsStringNew);
            repoService.updateById(res);


            return new Result<>(200, null, "Delete successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Meet some mistake");
        }
    }

    @GetMapping("/repo/change/name")
    public Result<?> changeRepoName(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String repoNameNew){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);
        res.setRepoName(repoNameNew);
        boolean test = repoService.updateRepo(res);

        if (test == true)
            return new Result<>(200, null, "Change the repo name successfully");
        else
            return new Result<>(500, null, "fail to change the repo name");

    }

    @GetMapping("/repo/change/bio")
    public Result<?> changeRepoBio(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String repoBioNew){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);
        res.setRepoBio(repoBioNew);
        boolean test = repoService.updateRepo(res);

        if (test == true)
            return new Result<>(200, null, "Change the repo bio successfully");
        else
            return new Result<>(500, null, "fail to change the repo bio");

    }
    @GetMapping("/repo/change/visibility")
    public Result<?> changeRepoVisibility(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String repoVisibilityNew){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);
        res.setRepoVisibility(repoVisibilityNew);
        boolean test = repoService.updateRepo(res);

        if (test == true)
            return new Result<>(200, null, "Change the repo visibility successfully");
        else
            return new Result<>(500, null, "fail to change the repo visibility");

    }
    @GetMapping("/repo/change/Owner")
    public Result<?> changeRepoOwner(@RequestParam String repoOwner, @RequestParam String repoName,@RequestParam String repoOwnerNew){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);
        res.setRepoOwner(repoOwnerNew);
        boolean test = repoService.updateRepo(res);

        if (test == true)
            return new Result<>(200, null, "Change the repo owner successfully");
        else
            return new Result<>(500, null, "fail to change the repo owner");

    }

    @GetMapping("/repo/delete")
    public Result<?>deleteRepo(@RequestParam String repoOwner, @RequestParam String repoName){
        Repo repo = new Repo();
        repo.setRepoOwner(repoOwner);
        repo.setRepoName(repoName);
        Repo res = repoService.selectByRepoName(repo);
        boolean test = repoService.deleteRepo(res);

        if (test == true)
            return new Result<>(200, null, "Change the repo owner successfully");
        else
            return new Result<>(500, null, "fail to change the repo owner");
    }

    @GetMapping("/user/repo/search")
    public Result<?> searchUserRepo(@RequestParam String userAccount,@RequestParam String searchValue,@RequestParam String searchVisibility,@RequestParam String searchSort) {
        try {
            User user = userService.selectByUserAccount(userAccount);
            Repo repo = new Repo();
            repo.setRepoOwner(user.getUserAccount());
            List<Repo> repos = repoService.selectListByRepoOwner(repo);


            if(repos.isEmpty())
                return  new Result<>(200,null,"No result 1");

            if(!searchValue.equals("")){
                Iterator<Repo> iterator = repos.iterator();
                while(iterator.hasNext()) {
                    Repo next = iterator.next();
                    String name = next.getRepoName();
                    if (!name.equals(searchValue)) {
                        iterator.remove();
                    }
                }
                if(repos.isEmpty())
                    return new Result<>(200,null,"No result 2");
            }

            if(!searchVisibility.equals("") && !searchVisibility.equals("all")){
                Iterator<Repo> iterator = repos.iterator();
                while(iterator.hasNext()) {
                    Repo next = iterator.next();
                    String visibility = next.getRepoVisibility();
                    if (!visibility.equals(searchVisibility)) {
                        iterator.remove();
                    }
                }
                if(repos.isEmpty())
                    return new Result<>(200,null,"No result 3");
            }


            if(searchSort.equals("name")){
                Collections.sort(repos, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        if( o1.getRepoName().compareTo( o2.getRepoName()) >= 0 )
                            return 0;
                        else
                            return 1;
                    }
                });
            }else if(searchSort.equals("stars")){
                Collections.sort(repos, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        if( o1.getRepoFollowNum().compareTo( o2.getRepoFollowNum()) >= 0 )
                            return 0;
                        else
                            return 1;
                    }
                });
            }

            return new Result<>(200,repos,"获取成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, null, "Search fails!");
        }
    }

}

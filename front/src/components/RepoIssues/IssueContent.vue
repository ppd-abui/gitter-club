<script>
export default {
  name: "IssueContent"
}
</script>
<template>
  <div style="width: 100%">
    <el-container style="margin: 20px auto; width: 80%; border: #d1d1d1 solid; border-radius: 20px; background-color: #f6f8fa">
      <el-main style="padding-left: 30px;">
        <div style="font-family: Calibri; font-size: 28px;margin-top: 10px; margin-left: 5px;">{{issue.issueTitle}}</div>
        <div style="margin-top: 10px">
          <el-tag size="large">{{issue.issueState}}</el-tag>
          <span style="font-size: 14px;margin-left: 5px;color: #4a4849">{{issue.issueUsername}}</span>
          <el-divider style="margin-top: 10px; margin-bottom: 20px"/>
        </div>
<!--        <div style="font-family: Calibri; font-size: 20px; margin-left: 10px; color: grey">Content:</div>-->
        <div style="font-size: 20px; border: #d1d1d1 solid; min-height: 150px; margin: 0px; border-radius: 10px;padding: 20px; background-color: white">
          {{issue.issueContent}}
        </div>
      </el-main>
      <el-aside width="30%" style=" margin-top: 70px; margin-bottom: 20px; padding-left: 20px">
        <div>
          <div>
            <div style="font-size: 16px; position:relative; top: 10px">Assignees</div>
            <el-icon @click="SetAssignees()" style="float: right; position: relative; right: 20px; bottom: 10px" size="20px">
              <Setting />
            </el-icon>
          </div>
          <el-divider/>
        </div>

        <!--      悬浮框-->
        <div v-show="ifSetAssignees" style="z-index: 5; position: absolute; width: 300px;">
          <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 3px; background-color: white">
            <div style="display: flex; margin-top: 10px;">
              <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Assign up to 10 people to this issue</span>
            </div>

            <div v-if="collaborator[0]===''">none of collaborator</div>
            <el-table v-else :data="collaborator" style="width: 100%; background-color: #f6f8fa">
              <el-table-column prop=""  label="">
                <template #default="scope">
                  <div @click="AddAssigners(collaborator[scope.$index])" style="height: 20px; width: 100%; display: flex">
                    <!--                    <el-icon><Select/></el-icon>-->
                    <div style="position: absolute; margin-left: 20px; bottom: 10px; font-family: Calibri; font-size: 16px">{{collaborator[scope.$index]}}</div>
                  </div>
                </template>
              </el-table-column>
              <el-divider style="margin-top: 10px; margin-bottom: 0px;"/>
            </el-table>
          </div>
        </div>

        <div style="border: #d1d1d1 solid; z-index: 1; padding: 0 20px 0 20px; min-height: 190px; margin-right: 10px; background-color: white; border-radius: 10px">
          <div v-if="assigners[0]===''" style="color:#b1b1b1;"> None of Assignees</div>
          <div v-else style="width: 100%">
            <el-table :data="assigners" style="width: 100%">
              <el-table-column prop=""  label="">
                <template #default="scope">
                  <el-icon @click="DeleteAssigner(assigners[scope.$index])" color="gray" style="position: absolute; bottom: 10px; right: 10px;"><CloseBold/></el-icon>
                  <div style="margin-left: 10px; bottom: 0px; font-family: Calibri; font-size: 16px">{{assigners[scope.$index]}}</div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

      </el-aside>
    </el-container>
  </div>

</template>

<script  setup>
import router from "../../router";
import {onBeforeMount, reactive, ref} from "vue";
import request from "../../utils/request";
import {ElMessage} from "element-plus";
let path = router.currentRoute.value.fullPath;
let ifSetAssignees = ref(false)
let collaborator=ref([])
let assigners =ref([])
let pathList = path.substr(1).split('/')
let  issue = reactive({
  issueTitle: '',
  issueContent: '',
  issueUsername: '',
  issueReponame: '',
  issueState: '',
  issueAssigners: ''
})

onBeforeMount(() => {
  lista();
  getCollaborators();
  getAssigners();
});
// 页面一开始获得的数据
const lista = () => {
  //发送请求
  request.get("/issue/get/content",{
    params: {
      repoOwner: pathList[0],
      repoName: pathList[1],
      issueTitle:pathList[3]
    }
  }).then((res) => {
        issue.issueTitle=res.data.issueTitle
        issue.issueContent=res.data.issueContent
        issue.issueUsername=res.data.issueUsername
        issue.issueReponame=res.data.issueReponame
        issue.issueState=res.data.issueState
        issue.issueAssigners=res.data.issueAssigners
   })
};
  function DeleteAssigner(deleteAssigner) {
    request.get("/repo/assigner/delete",{
      params:
          {
            deleteAsi:deleteAssigner,
            repoOwner: pathList[0],
            repoName: pathList[1],
            issueTitle:pathList[3]
          }
    }).then((res)=>
        {
          if(res.code==200)
          {
            ElMessage({
              type:'success',
              message:"Delete successfully"
            })
            getAssigners()
          }
          if(res.code==300)
          {
            ElMessage({
              type:'warning',
              message:"There is no assigners "
            })
          }
        }
    )

  }
  function AddAssigners(account) {
    request.get("/repo/assigner/add",{
      params: {
        newAsi:account,
        repoOwner: pathList[0],
        repoName: pathList[1],
        issueTitle:pathList[3]
      }
    }).then((res)=>
    {
      if(res.code==200)
      {
        ElMessage({
          type:'success',
          message:"Create successfully "
        });
        getAssigners()
      }
      if(res.code==500)
      {
        ElMessage({
          type:'warning',
          message:'The user has been added as a assigner'
        })
      }
    }
    )
  }
  function getCollaborators() {
    request.get("/repo/col",{
      params: {
        repoOwner: pathList[0],
        repoName: pathList[1],
      }
    }).then((res)=>
        {
          collaborator.value=res.data;
          console.log('collaborator',collaborator.value)
          getCollaborators()
        }
    )
  }

  function getAssigners() {
    request.get("/repo/assigner",{
      params:{
        repoOwner: pathList[0],
        repoName: pathList[1],
        issueTitle:pathList[3]
      }
    }).then((res)=>
    {
      assigners.value=res.data;
      console.log('assigners',assigners.value)
    }
    )
  }
  function SetAssignees(){
    ifSetAssignees.value=!ifSetAssignees.value
  }
  function SetAssigneesHide(){
    ifSetAssignees.value=false;
  }
</script>

<style scoped>

</style>
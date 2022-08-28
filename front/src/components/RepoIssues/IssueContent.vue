<script>
export default {
  name: "IssueContent"
}
</script>
<template>
  <el-container>
    <el-main><div style="font-size: 20px;margin-top: 10px;margin-left: 15%">
     {{pathList[3]}}
      </div>
      <div>
        <el-button v-if="assigners[0]===nullString" style="margin-top: 10px;margin-left: 15%;border-radius: 15px;vertical-align: -10%" >Unassigned</el-button>
        <el-button v-else style="margin-top: 10px;margin-left: 15%;border-radius: 15px;vertical-align: -10%" >Assigned</el-button>
        <span style="font-size: small;margin-left: 5px;">{{pathList[0]}}</span>
        <el-divider style="width:70% ;margin-left: 15%"/>
      </div>
      <div class="rectangle">
      {{arr.issueContent}}
      </div>
      <div class="rectangle2">
        <v-md-editor
            v-model="text"
            height="200px">
        </v-md-editor>
      </div></el-main>
    <el-aside width="30%">
        <div style="margin-top: 50px;margin-top:30%">
          <span style="font-size: small">Assignees  <el-button style="width: 5%"> <el-icon @click="SetAssignees()" style="margin-left: 2px;vertical-align: -15%"><Setting /></el-icon></el-button></span>
        </div>
<!--      悬浮框-->
      <div>
        <div v-show="ifSetAssignees" style="z-index: 2; position: absolute;">
          <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 3px; width: 300px; background-color: white">
            <div style="display: flex; margin-top: 10px;">
              <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Assign up to 10 people to this issue</span>
              <el-icon @click="SetAssigneesHide" color="gray" style="position: absolute; right: 10px;"><CloseBold/></el-icon>
            </div>
            <el-divider style="margin-top: 10px; margin-bottom: 10px;height: 10px"/>
            <el-table :data="collaborator" style="width: 100%">
              <el-table-column prop=""  label="Collaborator" width="120">
              <template #default="scope">
                <div @click="AddAssigners(collaborator[scope.$index])" style="height: 20px; width: 100%; display: flex">
                  <el-icon ><Select/></el-icon>
                  <div style="position: absolute; margin-left: 20px; bottom: 10px; font-family: Calibri; font-size: 16px">{{collaborator[scope.$index]}}</div>
                </div>

              </template>
              </el-table-column>
            <el-divider style="margin-top: 10px; margin-bottom: 0px;"/>
            </el-table>
          </div>
        </div>
      </div>


      <div>
        <div v-if="assigners[0]===nullString"> None of Assignees</div>
        <div v-else>
          <el-table :data="assigners" style="width: 100%">
            <el-table-column prop=""  label="" width="120">
              <template #default="scope">
                <el-icon @click="DeleteAssigner(assigners[scope.$index])" color="gray" style="position: absolute;bottom:5px; right: 10px;"><CloseBold/></el-icon>
                <div style="position: absolute; margin-left: 10px; bottom: 0px; font-family: Calibri; font-size: 16px">{{assigners[scope.$index]}}</div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

    </el-aside>
  </el-container>


</template>

<script  setup>
import router from "../../router";
import {onBeforeMount, reactive, ref} from "vue";
import request from "../../utils/request";
import {ElMessage} from "element-plus";
let path = router.currentRoute.value.fullPath;
let isEmpty=ref(true);
let ifSetAssignees = ref(false)
let collaborator=ref([])
let assigners =ref([])
let pathList = path.substr(1).split('/')
let nullString=""
let  arr = reactive({
  issueContent:""
});

onBeforeMount(() => {
  lista();
  getCollaborators();
  getAssigners();
});

const lista = () => {
  //发送请求
  request.get("/issue/get/content",{
    params: {
      repoOwner: pathList[0],
      repoName: pathList[1],
      issueTitle:pathList[3]
    }
  }).then((res) => {
        arr.issueContent=res.data.issueContent;
      })
      .catch((error) => {
      });
};
function DeleteAssigner(deleteAssigner)
{
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
          router.go(0);
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
        router.go(0);
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
    }
    )
  }
function SetAssignees(){
  ifSetAssignees.value=!ifSetAssignees.value
}
function SetAssigneesHide(){
  ifSetAssignees.value=!ifSetAssignees.value;

}
</script>

<style scoped>
.rectangle{
  height: 100px;
  width: 800px;
  border-radius: 10px ;
  border: solid #f7f7f7;
  margin-left: 15%;
}
.rectangle2{
  height: 200px;
  width:800px;
  margin-left: 15%;
  border-radius: 10px;
  border: solid #f7f7f7;
}
</style>
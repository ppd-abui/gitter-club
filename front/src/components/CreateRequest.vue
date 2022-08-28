<script>
export default {
  name: "CreateRequest"
}
</script>
<template>
  <div>
    <div style="display: flex; font-size: 20px;margin-top: 25px;margin-left: 15%">
      <!--悬浮窗-->
      <div style="z-index: 2;">
        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 10px; width: 300px; background-color: white">
          <div style="display: flex; margin-top: 10px;">
            <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Switch branches</span>
          </div>
          <el-table :data="thisBranchList" style="width: 100%; border-radius: 10px;">
            <el-table-column prop="" label="">
              <template #default="scope">
                <div @click="changeThisBranch(scope.$index)" style="height: 20px; width: 100%; display: flex">
                  <el-icon v-if="thisBranchList[scope.$index]===nowThisBranch"><Select/></el-icon>
                  <div style="position: absolute; left: 30px; bottom: 10px; font-family: Calibri; font-size: 16px">{{thisBranchList[scope.$index]}}</div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <div style="z-index: 2;">
        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 10px; width: 300px; background-color: white">
          <div style="display: flex; margin-top: 10px;">
            <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Switch branches</span>
          </div>
          <el-table :data="yourBranchList" style="width: 100%; border-radius: 10px;">
            <el-table-column prop="" label="">
              <template #default="scope">
                <div @click="changeYourBranch(scope.$index)" style="height: 20px; width: 100%; display: flex">
                  <el-icon v-if="yourBranchList[scope.$index]===nowYourBranch"><Select/></el-icon>
                  <div style="position: absolute; left: 30px; bottom: 10px; font-family: Calibri; font-size: 16px">{{yourBranchList[scope.$index]}}</div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

    </div>
    <el-button style="margin-left: 100px; margin-top: 40px;" @click="createPullRequest">create</el-button>
  </div>
</template>


<script setup>
import {reactive, ref} from "vue";
import request from "../utils/request";
import router from "../router";
import {ElMessage} from "element-plus";
  let thisBranchList = ref(['master','newBranch','anotherNewBranch'])
  let yourBranchList = ref(['master','newBranch','anotherNewBranch'])
  let nowThisBranch = ref('')
  let nowYourBranch = ref('')

  function changeThisBranch(index){
    nowThisBranch.value=thisBranchList.value[index]
  }

  function changeYourBranch(index){
    nowYourBranch.value=yourBranchList.value[index]
  }

  let path;
  path = router.currentRoute.value.fullPath
  //通过路由获取仓库信息及路径
  let pathList = path.substr(1).split('/')

  request.get('/repo/branches',{
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1]
    }
  }).then(res=>{
    console.log(res)
    if(res.code === 200)
      thisBranchList.value = res.data
    else
      ElMessage({
        type:'error',
        message:'System error!'
      })
  })

  request.get('/repo/branches',{
    params:{
      repoOwner:localStorage.getItem("userAccount"),
      repoName:pathList[1]
    }
  }).then(res=>{
    console.log(res)
    if(res.code === 200)
      yourBranchList.value = res.data
    else
      ElMessage({
        type:'error',
        message:'System error!'
      })
  })

  let pullRequest = reactive({
    pullRequestRepoOwner:'',
    pullRequestRepoName:'',
    pullRequestRemoteBranch:'',
    pullRequestLocalBranch:'',
    pullRequestCreator:localStorage.getItem("userAccount")
  })

  function createPullRequest(){
    console.log('nowThisBranch:',nowThisBranch.value)
    console.log('nowYourBranch:',nowYourBranch.value)

    pullRequest.pullRequestRepoOwner = pathList[0]
    pullRequest.pullRequestRepoName = pathList[1]
    pullRequest.pullRequestRemoteBranch = nowThisBranch.value
    pullRequest.pullRequestLocalBranch = nowYourBranch.value

    request.post('/pulls/new',pullRequest).then(res=>{
      console.log(res)
      if(res.code === 200)
        ElMessage({
          type:'success',
          message:'Create pull request successfully!'
        })
      else
        ElMessage({
          type:'error!',
          message:'Create pull request failed!'
        })
    })
  }
</script>

<style scoped>

</style>
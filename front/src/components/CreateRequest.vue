<script>
export default {
  name: "CreateRequest"
}
</script>
<template>
  <div style="width: 100%; height: 100%">
    <div style="border: #e4e7ed solid; border-radius: 20px; background-color: #f6f8fa; padding:20px; width: 60%; min-width: 700px; height: 100%; font-size: 20px;margin: 25px auto; padding-bottom: 60px">
      <div style="text-align: center; font-family: Calibri; font-size: 30px; color: #4a4849"><el-icon style="position: relative; top:5px; right: 10px"><SetUp/></el-icon>Create Pull Request</div>
      <div style="width: 80%; margin: 20px auto; display: flex">
        <!--悬浮窗-->
        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; width: 300px; background-color: white">
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

        <el-icon size="30px" style="margin: 10px 20px 20px 20px; color: grey"><Back/></el-icon>

        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; width: 300px; background-color: white">
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
      <el-divider/>
      <div style="border: #e4e7ed solid;border-radius: 10px; background-color: white; padding: 20px;">
        <div style="font-size: 20px; font-family: Calibri; color: #4a4849">Leave your message<span style="color:indianred;">*</span></div>
        <el-input autosize v-model="pullMessage" type="textarea" style="margin-top: 5px;"/>
      </div>
      <el-button style="margin-top: 15px; margin-right: 20px; width: 100px; float: right" type="primary" @click="createPullRequest">create</el-button>
    </div>
  </div>
</template>


<script setup>
  import {reactive, ref} from "vue";
  import request from "../utils/request";
  import router from "../router";
  import {ElMessage} from "element-plus";
  let thisBranchList = ref([])
  let yourBranchList = ref([])
  let nowThisBranch = ref('master')
  let nowYourBranch = ref('master')
  let pullMessage = ref('')

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
    pullRequestCreator:localStorage.getItem("userAccount"),
    pullRequestTheme: ''
  })

  function createPullRequest() {
    pullRequest.pullRequestRepoOwner = pathList[0]
    pullRequest.pullRequestRepoName = pathList[1]
    pullRequest.pullRequestRemoteBranch = nowThisBranch.value
    pullRequest.pullRequestLocalBranch = nowYourBranch.value
    pullRequest.pullRequestTheme = pullMessage.value

    if (pullRequest.pullRequestTheme !== '') {
      request.post('/pulls/new', pullRequest).then(res => {
        if (res.code === 200) {
          ElMessage({
            type: 'success',
            message: 'Create pull request successfully!'
          })
          router.push('/'+pathList[0]+'/'+pathList[1]+'/pull')
        }else
          ElMessage({
            type: 'error',
            message: 'Create pull request failed!'
          })
      })
    } else {
      ElMessage({
        type: 'error',
        message: 'Message can not be null!'
      })
    }
  }
</script>

<style scoped>

</style>
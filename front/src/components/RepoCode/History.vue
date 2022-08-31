<script>
export default {
  name: "History.vue"
}
</script>
<template>
  <div style="width: 90%;">

    <div style="margin-left: 10%;margin-top: 1%;width: 200px; display: flex">
      <el-icon size="large" style="margin-top: 5%;margin-left: 5%">
        <operation/>
      </el-icon>

      <el-select v-model="selectedBranch" style="margin-left: 10%">
        <el-option
            v-for="branch in branchList"
            :key="branch"
            :label="branch"
            :value="branch"
            @click="changeBranch"
        />
      </el-select>
    </div>

    <div style="min-width: 800px;  width: 80%;margin-left: 10%;margin-top: 15px; margin-bottom: 20px">
      <el-table :data="commitsData" style="width: 100%; border-radius: 10px;border: #b1b1b1 2px solid; box-shadow: #b1b1b1 2px 2px 4px">
        <el-table-column prop="SHA" label="SHA">
          <template #default="scope">
            <el-link @click="gotoHistoryFile(scope.$index)">{{ commitsData[scope.$index].sha }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="message" label="Commit message">
          <template #default="scope">
            <el-link>{{ commitsData[scope.$index].message }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="Commit time">
          <template #default="scope">
            {{ commitsData[scope.$index].time }}
          </template>
        </el-table-column>
        <el-table-column prop="committer" label="Committer">
          <template #default="scope">
            <el-link @click="goToUser(scope.$index)">{{ commitsData[scope.$index].committer }}</el-link>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

</template>

<script setup>

import request from '../../utils/request.js'
import router from  '../../router'
import {ref} from "vue";
import {ElMessage} from "element-plus";

let selectedBranch = ref('master')
let branchList = ref([])
let commitsData = ref([])
let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')

getCommits()

function getCommits(){
  request.get('/commits/info',{
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1],
      branch:selectedBranch.value
    }
  }).then(res=>{
    if(res.code === 200)
      commitsData.value=res.data
    else
      ElMessage({
        type:'error',
        message:'System error! Get history failed!'
      })
  })
}

getBranchInfo()

function getBranchInfo(){
  request.get('/repo/branches',{
    params: {
      repoOwner: pathList[0],
      repoName: pathList[1]
    }
  }).then(res => {
    console.log(res)
    branchList.value=res.data
  })
}

function changeBranch(){
  getCommits()
}

function gotoHistoryFile(index){
  //路由格式：/userAccount/repoName/code/sha/filePaht
  let branch = commitsData.value[index].sha
  request.get('/tree',{
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1],
      branch:branch
    }
  }).then(res=>{
    if(res.code === 200)
      router.push('/' + pathList[0] + '/' + pathList[1] + '/code/' + branch)
    else
      ElMessage({
        type:'error',
        message:'System error!'
      })
  })
}

function goToUser(index){
  console.log('committer:',commitsData.value[index].committer)
  router.push('/' + commitsData.value[index].committer)
}

</script>

<style scoped>

</style>
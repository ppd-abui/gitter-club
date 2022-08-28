<script lang = "ts">
  export default {
    name: "Pull"
  }
</script>

<template>
  <router-view v-if="pathList.length>3"></router-view>
  <div v-else style="width: 100%">
    <div style="display: flex; width: 100%">
      <div style="height: 100%; min-width: 300px; width: 80%; margin-left: 50px; margin-top: 20px">
        <div style="display: flex">
          <el-input
              v-model="keyWord"
              placeholder="Please input"
              style = "width: 1000px;height: 32px;margin-left: 10%;"
          >
            <template #prepend>
              <el-select v-model="select" placeholder="Select" style="height: 100%">
                <el-option label="Open issues and pull requests" value="1" />
                <el-option label="Your issues" value="2" />
                <el-option label="Your pull requests" value="3" />
                <el-option label="Everything assigned to you" value="4" />
                <el-option label="Everything mentioning you" value="5" />
              </el-select>
            </template>
            <template #append>
              <el-button :icon="Search" />
            </template>
          </el-input>
          <el-button @click="gotoCreateRequest()" type="success" height="32px">
            New pull request
          </el-button>
        </div>

        <div style="min-width: 800px;  width: 80%; margin: 30px auto">
          <el-table :data="pullData" style="width: 100%; border-radius: 10px;">
            <el-table-column prop="pullRequestCreator" label="Pull creator">
              <template #default="scope">
                <el-link @click="gotoPullRequest(scope.$index)">{{ pullData[scope.$index].pullRequestCreator }}</el-link>
              </template>
            </el-table-column>
          </el-table>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang = "ts" setup>
  import {reactive, ref} from "vue";
  import request from "../utils/request"
  import router from "../router";
  import {ElMessage} from "element-plus";
  let path = router.currentRoute.value.fullPath
  let pathList = path.substr(1).split('/')
  let keyWord =ref("")
  let select = ref ("")
  let pullData = ref([])

  request.get('pulls/info', {
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1]
    }
  }).then(res=>{
    if(res.code === 200)
      pullData.value = res.data
    else
      ElMessage({
        type:'error',
        message:'Get pull request info failed!'
      })
  })

  function  gotoCreateRequest()
  {
    router.push({path: '/'+pathList[0]+'/'+pathList[1]+'/newrequest'})
  }

  function gotoPullRequest(index){
    router.push({path: '/'+pathList[0]+'/'+pathList[1]+'/pull/'+pullData.value[index].pullRequestId})
  }
</script>

<style scoped>
  :deep(.select-trigger.el-tooltip__trigger){
    height: 100%;
  }
  :deep(.el-input.input--suffix){
    height: 100%;
  }
</style>

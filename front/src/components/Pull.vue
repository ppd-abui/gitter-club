<script lang = "ts">
  export default {
    name: "Pull"
  }
</script>

<template>
  <router-view/>
  <div style="width: 100%">
    <div style="display: flex; width: 100%">
      <div style="height: 100%; min-width: 300px; width: 80%; margin-left: 50px; margin-top: 20px">
        <div style="display: flex">
          <el-input
              v-model="keyWord"
              placeholder="Please input"
              style = "width: 1000px;height: 32px;margin-left: 10%;"
          >
            <template #append>
              <el-button><el-icon><Search/></el-icon></el-button>
            </template>
          </el-input>
          <el-button @click="gotoCreateRequest()" type="primary" height="32px">
            New pull request
          </el-button>
        </div>

        <div style="min-width: 800px;  width: 80%; margin: 30px auto">
          <el-table :data="pullData" style="width: 100%; border-radius: 10px;">
            <el-table-column prop="pullRequestCreator" label="Pull require">
              <template #default="scope">
                <el-link @click="gotoPullRequest(scope.$index)">{{ pullData[scope.$index].pullRequestTheme }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="pullRequestCreator" label="Pull creator">
              <template #default="scope">
                <el-link @click="gotoSpace(scope.$index)">{{ pullData[scope.$index].pullRequestCreator }}</el-link>
              </template>
            </el-table-column>
          </el-table>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang = "ts" setup>
import {onMounted, reactive, ref} from "vue";
  import request from "../utils/request"
  import router from "../router";
  import {ElMessage} from "element-plus";

  let path
  let pathList = ref([])
  let keyWord =ref("")
  let select = ref ("")
  let pullData = ref([])
  let ifListShow = ref(true)

  function getPathList(){
    path = router.currentRoute.value.fullPath
    pathList.value= path.substr(1).split('/')
    console.log("pathList",pathList.value)
    ifListShow.value = true
  }
  getPathList()

  request.get('pulls/info', {
    params:{
      repoOwner:pathList.value[0],
      repoName:pathList.value[1]
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
    router.push({path: '/'+pathList.value[0]+'/'+pathList.value[1]+'/newrequest'})
  }

  function gotoPullRequest(index){
    router.push({path: '/'+pathList.value[0]+'/'+pathList.value[1]+'/pull/'+pullData.value[index].pullRequestId})
  }

  function gotoSpace(index){
    router.push({path: '/'+pullData.value[index].pullRequestCreator})
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

<template>

  <div v-if="isDir" style="z-index: 1; border: #d1d1d1 solid; width: 100%; margin-top: 20px; border-radius: 10px; box-shadow: #e4e7ed 3px 3px 5px;">
    <el-table :data="pathData" style="width: 100%; border-radius: 10px;">
      <el-table-column prop="fileType" width="40px">
        <template #default="scope">
          <el-icon v-if="pathData[scope.$index].fileType==='Dir'" color="blue"><Folder/></el-icon>
          <el-icon v-else><Document/></el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="fileName" label="文件名">
        <template #default="scope">
          <el-link @click="openFile(pathData[scope.$index].fileName)">{{pathData[scope.$index].fileName}}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="fileCommitMsg" label="提交信息">
        <template #default="scope">
          <el-link @click="openFile(pathData[scope.$index].lastCommitMessage)">{{pathData[scope.$index].lastCommitMessage}}</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div v-else style="margin: 20px auto">
    <v-md-editor
        v-model="fileContent"
        height="500px">
    </v-md-editor>
    <div style="width: 100px; margin-top: 30px; position: relative; left: 60%; display: flex">
      <el-button type="success" plain @click="saveFile">Propose change</el-button>
      <el-button type="danger" plain @click="flesh">cancel</el-button>
    </div>
  </div>

</template>

<script>
export default {
  name: "File"
}
</script>

<script setup>
import router from '../../router'
import {ref} from "vue";
import request from "../../utils/request";
import {watchEffect} from "vue";
import {ElMessage} from "element-plus";

  let isDir = ref(true)
  let path = router.currentRoute.value.fullPath
  let pathData = ref([])
  let fileContent = ref('')

  function updateTree() {
    path = router.currentRoute.value.fullPath
    //通过路由获取仓库信息及路径
    let pathList = path.substr(1).split('/')

    let branch = pathList[3]
    let suffixDir = ''

    let iter = 4
    while (pathList.length > 4 && iter < pathList.length) {
      suffixDir = suffixDir + '/' + pathList[iter]
      iter++
    }

    if(suffixDir!=='')
      suffixDir=suffixDir.substr(1)

    console.log('---------branch',branch,'----------suffixDir:',suffixDir)
    //发送请求获取目录
    request.get('/tree', {
      params: {
        repoOwner: pathList[0],
        repoName: pathList[1],
        branch: branch,
        suffixDir: suffixDir
      }
    }).then(res => {
      console.log(res)
      if (res.code === 200) {
        if (res.msg==='Directory') {
          console.log('this is Directory')
          isDir.value=true
          pathData.value = res.data
        } else {
          console.log('this is File')
          isDir.value=false
          fileContent.value = '```\n'+res.data+'\n```'
        }
      }
    })
  }

  function openFile(fileName){
    router.push({path: router.currentRoute.value.fullPath+'/'+fileName})
    // path=router.currentRoute.value.fullPath+'/'+fileName
    // updateTree()
  }

  watchEffect(() =>{
    updateTree()
  })

  function saveFile(){

    let pathList = path.substr(1).split('/')

    //包含repoOwner和repoName的suffixDir
    let suffixDir = pathList[0] + '/' + pathList[1]

    let iter = 4
    while (pathList.length > 4 && iter < pathList.length) {
      suffixDir = suffixDir + '/' + pathList[iter]
      iter++
    }

    console.log('suffixDir:',suffixDir)
    let contentLength = fileContent.value.length - 8
    console.log('fileContent:',fileContent.value.substr(4,contentLength))

    request.get("/files/save",{
      params:{
        fileContent:fileContent.value.substr(4,contentLength),
        suffixDir:suffixDir
      }
    }).then(res=>{
      if(res.code === 200){
        ElMessage({
          type:'success',
          message:'Save successfully!'
        })
      } else {
        ElMessage({
          type:'error',
          message:'System error!'
        })
      }

    })
  }

  function flesh(){
    router.back()
  }

</script>
<style scoped>

</style>
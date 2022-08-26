<script lang="ts">

export default {
  name: "CreateIssue.vue",
}
</script>
<template>
  <div class="common-layout">
    <el-container>

      <el-main>
        <div class="rectangle">
          <el-input  style="margin-top: 10px;" v-model="issue.issueTitle" placeholder="Title" />
          <v-md-editor
              v-model="issue.issueContent"
              height="500px">
          </v-md-editor>
          <el-button  style="margin-left:640px;background-color: #f7f7f7 ;margin-top: 20px;" @click="submit()">Submit new issue</el-button>
        </div>
      </el-main>
      <el-aside width="300px">
        <div style="width: 200px; padding: 0 50px 0 50px">
          <div style="margin: 50px 0 0 0px; font-family: Calibri; font-size: 24px;">
            About
          </div>
          <div style="font-style: italic; font-family: Calibri; font-size: 18px; color: #b1b1b1; margin-top: 20px">
            No description, website, or topics provided.
          </div>
          <el-divider/>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import RepositoryS from "../components/RepositoryS.vue";
import index1 from "../components/Index.vue";
import request from "../utils/request";
import router from '../router';
import {reactive} from 'vue'
import {ElMessage} from "element-plus";



let issue = reactive({
  issueTitle: '',
  issueContent: '',
  issueReponame:''
})

function submit() {
  let path = router.currentRoute.value.fullPath
  let pathList = path.substr(1).split('/')
  console.log(pathList)
  issue.issueReponame=pathList[1]

  if(issue.issueTitle===''){
    ElMessage({
      type:'error',
      message:'Please input a issue title！'
    })
    return
  }
  request.post('/issue/new', issue).then(res=>{
    console.log(res.code);
    if (res.code === 200){
      ElMessage({
        type:"success",
        message:"issue created successfully!"
      })
      router.push('/' + pathList[0] + '/' + pathList[1] + '/issues/list')
    } else {
      ElMessage({
        type:"error",
        message:"System error!"
      })
    }
  })
}
function go(name)
{
 router.push(name)
}
</script>

<style scoped>
.rectangle{
  height: 600px;
  width: 800px;
  margin-left:200px;
  border-radius: 10px;
  border: 3px solid #f7f7f7;
}
</style>
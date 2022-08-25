<script lang="ts">
export default {
  name: "UploadFile"
}
</script>

<template>
<!--  upload area-->
  <div>
  <div style="width: 1200px;height: 250px;margin-left: 150px;margin-top: 65px;border: #dddddd 1px solid;border-radius: 10px">
    <el-upload
        ref="uploadRef"
        style="width: 1200px;height: 250px;margin-top: 20px"
        drag
        action="http://localhost:9090/files/upload/"
        multiple
        :auto-upload="false"
        :on-success="filesUploadSuccess"
        :before-upload="beforeUpload"
        :data="commitInfo"
    >
      <el-icon size="50px"><Document /></el-icon>
      <div class="el-upload__text" style="font-size: 20px; color: black">
        Drag file here to add them to your repository <br>
        or <em>click to upload</em>
      </div>
    </el-upload>
  </div>

<!--  commit message-->
  <div style="width: 1200px;height: 150px;margin-left: 150px;margin-top: 15px;border: #dddddd 1px solid;border-radius: 10px">
    <div style="margin-top: 20px;margin-left: 20px; font-size: 20px"> Commit changes </div>

    <el-input v-model="commitInfo.commitMessage" placeholder="Add files via upload" style="width: 1100px; margin-top: 20px;margin-left: 20px"> </el-input>

    <div style="color: grey;margin-top: 20px;margin-left: 20px;">
      Commit directly to the <em style="color: #1E97D4"> master </em> branch !
    </div>
  </div>


<!--  commit and cancel button -->
  <div style="height: 50px;margin-left: 150px;margin-top: 10px">
    <el-button color="#2da44e" @click="commitChanges" style="color: white; border: 1px #ccc solid">
      Commit changes
    </el-button>
    <el-button color="#f6f8fa" @click="cancel" style="color: #cf222e; border: 1px #ccc solid">
      Cancel
    </el-button>
  </div>
  </div>
</template>

<script setup lang="ts">
import router from "../../router"
import {createApp, reactive, ref} from 'vue'
import type { UploadInstance } from 'element-plus'

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')

const uploadRef = ref<UploadInstance>()
let commitInfo = reactive({
  repoOwner: '',
  repoName:'',
  dir:'',
  commitMessage:''
})

function beforeUpload() {
  commitInfo.repoOwner = pathList[0]
  commitInfo.repoName = pathList[1]
  commitInfo.dir=''
}

function commitChanges(){
  uploadRef.value!.submit()
}

function filesUploadSuccess(res){
  console.log(res)
}

function cancel(){
  router.push('/repo')
}

</script>

<style scoped>
  :deep(.el-upload-dragger){
    border: none;
  }
</style>
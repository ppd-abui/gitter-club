<script lang="ts">
import Index from "../components/Index.vue";
export default {
  name: "CreateRepo",
  components: {
    Index
  }
}
</script>

<template>
  <el-container style="height: 100%;">
    <el-header style="padding: 0;"><Index/></el-header>

    <el-main style="padding: 0 20% 0 20%; height: 100%">
      <div style="width: 100%; margin-top: 20px">
        <div style="font-size: 32px; font-family: 'Calibri Light'">Create a new repository</div>
        <div style="font-size: 16px; font-family: 'Calibri Light'; color: #808080">
          A repository contains all project files, including the revision history.
        </div>
        <el-divider/>
        <div style="width: 100%; height: 80px; display: flex">
          <div style="width: v-bind(nameWidth); height: 100%;">
            <span style="font-size: 16px; margin-left: 5px; font-family: Calibri; font-weight: bold; ">Owner</span><span style="color: indianred;">*</span>
            <div style="display: flex; height: 35px; margin-top: 5px; border: #d1d1d1 solid 1px; border-radius: 10px; background-color: #f7f7f7">
              <img style="margin-left: 10px; margin-top: 5px; height: 80%;" src="src/assets/black.png"/>
              <span style="margin-top: 10px; margin-left: 10px; margin-right: 15px; font-size: 16px; font-family: Calibri">Account</span>
            </div>
          </div>
          <div style="font-size: 30px; font-weight: lighter; position: relative; top: 20px; left: 20px;">/</div>
          <div style="margin-left: 50px; height: 100%;">
            <span style="font-size: 16px; margin-left: 5px; font-family: Calibri; font-weight: bold; ">Repository name</span><span style="color: indianred">*</span>
            <el-input @blur="checkRepoName" :suffix-icon="isAbleIcon" placeholder="Please input the repository name" class="repoInput" v-model="repo.repoName" style="margin-top: 5px; height: 35px"/>
          </div>
        </div>
        <div style="font-size: 16px; font-family: 'Calibri Light'; color: #808080">Great repository names are short and memorable. </div>
        <div style="margin-top: 20px;">
          <span style="font-size: 16px; margin-left: 5px; font-family: Calibri; font-weight: bold; ">Description</span>
          <span style="margin-left: 3px; color: #b1b1b1; font-size: 12px">(optional)</span>
          <el-input placeholder="Give a short introduce to your repository" class="repoInput" :autosize="{maxRows: 2}" type="textarea" v-model="repo.repoBio" style="margin-top: 5px; height: 35px"/>
        </div>
        <el-divider/>
        <div style="height: 100px;">
          <el-radio-group v-model="repo.repoVisibility" style="width: 100%; display: flex">
            <div style="width: 100%">
              <el-radio label="public" size="large">
                <div style="display: flex">
                  <div style="width: 30px; height: 30px; margin-top: 5px"><Collection/></div>
                  <div style="margin-left: 10px">
                    <div style="font-size: 16px; color: black; font-family: Calibri; font-weight: bold">Public</div>
                    <div style="font-size: 14px; color: grey; font-family: Calibri">Anyone on the internet can see this repository. You choose who can commit.</div>
                  </div>
                </div>
              </el-radio>
            </div>

            <div style="width: 100%; margin-top: 10px">
              <el-radio label="private" size="large">
                <div style="display: flex">
                  <div style="width: 30px; height: 30px; margin-top: 5px"><Lock/></div>
                  <div style="margin-left: 10px;">
                    <div style="font-size: 16px; color: black; font-family: Calibri; font-weight: bold">Private</div>
                    <div style="font-size: 14px; color: grey; font-family: Calibri">You choose who can see and commit to this repository.</div>
                  </div>
                </div>
              </el-radio>
            </div>
          </el-radio-group>
        </div>
        <el-divider style="margin-bottom: 15px"/>
        <div style="display: flex">
          <el-icon style="height: 20px; width: 20px; margin-right: 10px; color: grey;" class="is-loading"><Loading/></el-icon>
          <span style="font-size: 14px; color: #b1b1b1">You are creating a public repository in your personal account.</span>
        </div>
        <el-divider style="margin-top: 15px"/>
        <el-button :disabled="!isAble" @click="createRepo">Create Repository</el-button>
      </div>
      <el-divider style="margin-bottom: 0"/>
    </el-main>

  </el-container>
</template>

<script lang="ts" setup>

import {reactive, ref} from "vue";
import request from "../utils/request";
import { ElMessage } from "element-plus";
import router from '../router'

let isAble = ref(false)

let isAbleIcon = ref('')

function checkRepoName() {
  if (repo.repoName != ''){
    request.post('/repo/name', repo).then(res=>{
      if(res.code===500) {
        ElMessage({
          type: 'warning',
          message: 'The repository ' + repo.repoName + " already exists on this account",
        })
        isAble.value=false
        isAbleIcon.value='CircleCloseFilled'
      } else {
        isAble.value=true
        isAbleIcon.value='SuccessFilled'
      }
    })
  }
}

let repo = reactive({
  repoName: '',
  repoBio: '',
  repoVisibility: 'public'
})

function createRepo(){
  if(repo.repoName===''){
    ElMessage({
      type:'error',
      message:'Please input a repository name！'
    })
    return
  }
  request.post('/repo/new', repo).then(res=>{
    if (res.code === 200){
      ElMessage({
        type:"success",
        message:"Repository created successfully!"
      })
      router.push('/repo')
    } else {
      ElMessage({
        type:"error",
        message:"System error!"
      })
    }
  })
}

</script>

<style scoped>
  .repoInput{}

  :deep(.el-radio__label){

  }

</style>
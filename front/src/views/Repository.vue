<script lang="ts">
import Index from '../components/Index.vue'
import RepositoryS from "../components/RepositoryS.vue"
import Code from "../components/Code.vue"
export default {
  name: "Index",
  components:{
    Index,
    RepositoryS,
    Code
  }
}
</script>

<template>
  <el-container>
<!--    index-->
    <el-header  style="padding:0px; margin: 0"><index/></el-header>
      <div style="background-color: #f6f8fa; padding: 0">
  <!--    用户名 仓库名-->
        <div  style="display: flex; margin-left: 20px; margin-top: 20px">
          <div style="width: 20px; height: 20px; margin-left: 10px; position: relative; top: 8px"><Collection/></div>
          <div style="margin: 0px 0 0 10px; display: flex">
            <el-link @click="goToPersonCenter" type="primary" style="font-size: 24px; font-family: 'Calibri Light'">{{repo.repoOwner}}</el-link>
            <div style="font-size: 24px; margin: 0 10px 0 10px">/</div>
            <el-link type="primary" style="font-size: 24px; font-family: 'Calibri Light'; font-weight: bold">{{repo.repoName}}</el-link>
          </div>
          <el-tag style="margin-top: 5px; margin-left: 10px" round type="info">{{repo.repoVisibility}}</el-tag>
          <div style="width: 60%;"></div>

<!--          fork-->
          <el-button style="position:relative; top: 5px;" @click="forkRepo">
            <el-icon><Eleme/></el-icon>
            <span style="font-family: Calibri; font-size: 14px; font-weight: bold; margin: 2px 10px 0 10px">Fork</span>
            <div style="width: 20px; height: 20px; border-radius: 50%; background-color: #e7e7e7">
              <span style="color: black; position: relative; top: 4px">{{0}}</span>
            </div>
          </el-button>
<!--          fork-->

<!--          <el-button style="position:relative; top: 5px;" @click="changeStar">-->
<!--            <el-icon v-if="!isStar"><Star/></el-icon>-->
<!--            <el-icon v-if="isStar"><StarFilled/></el-icon>-->
<!--            <span style="font-family: Calibri; font-size: 14px; font-weight: bold; margin: 2px 10px 0 10px">Star</span>-->
<!--            <div style="width: 20px; height: 20px; border-radius: 50%; background-color: #e7e7e7">-->
<!--              <span style="color: black; position: relative; top: 4px">{{1}}</span>-->
<!--            </div>-->
<!--          </el-button>-->

          <el-button
              style="position:relative; top: 5px;left:10px"
              split-button
              :icon="test?StarFilled:Star"
              @click="changeStar()"
          >
            <span style="font-family: Calibri; font-size: 14px; font-weight: bold; margin: 2px 10px 0 10px">Star</span>
            <div style="width: 20px; height: 20px; border-radius: 50%; background-color: #e7e7e7">
              <span style="color: black; position: relative; top: 4px">{{repo.repoFollowNum}}</span>
            </div>
          </el-button>
        </div>

  <!--      辅助线-->
        <div style="width: 60px; height: 2px; position: relative; top: 60px; background-color: #e4e7ed"/>
  <!--      菜单-->
        <el-tabs v-model="chooseTab" style="margin-top: 20px; margin-left: 60px" class="demo-tabs">
          <el-tab-pane label="code" name="code">
            <template #label>
              <div @click="goto('code/master')" style="display: flex;">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><Edit /></div>
                <div style="margin-left: 10px">Code</div>
              </div>
            </template>
          </el-tab-pane>

          <el-tab-pane label="issues" name="issues">
            <template #label>
              <div @click="goto('issues')" style="display: flex">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><ChatSquare /></div>
                <div style="margin-left: 10px">Issues</div>
              </div>
            </template>
          </el-tab-pane>

          <el-tab-pane label="pull" name="pull">
            <template #label>
              <div @click="goto('pull')" style="display: flex">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><SetUp /></div>
                <div style="margin-left: 10px">Pull requests</div>
              </div>
            </template>
           </el-tab-pane>
          <div v-if="checkPower()">
            <el-tab-pane label="settings" name="settings">
              <template #label>
                <div @click="goto('settings')" style="display: flex">
                  <div style="width: 20px; height: 20px; position: relative; top: 5px"><Setting /></div>
                  <div style="margin-left: 10px">Settings</div>
                </div>
              </template>
            </el-tab-pane>
          </div>
        </el-tabs>
      </div>

    <el-container>
        <router-view/>
    </el-container>
  </el-container>
</template>


<script lang="ts" setup>
import {onMounted, reactive, ref, watch, watchEffect, onBeforeMount, computed} from 'vue'
import router from '../router'
import request from '../utils/request.js'
import {ElMessage,ElMessageBox} from "element-plus";
import { Star,StarFilled } from '@element-plus/icons-vue'

  let chooseTab = ref('')

  let repo = reactive({
    repoId: '',
    repoName: '',
    repoOwner: '',
    repoBio: '',
    repoVisibility: '',
    repoFollowNum: 0
  })

  let path = router.currentRoute.value.fullPath
  let pathList = path.substr(1).split('/')

  request.get('/repo/name',{
    params: {
      repoOwner: pathList[0],
      repoName: pathList[1]
    }
  }).then(res => {
    repo.repoId=res.data.repoId
    repo.repoName=res.data.repoName
    repo.repoOwner=res.data.repoOwner
    repo.repoBio=res.data.repoBio
    repo.repoVisibility=res.data.repoVisibility
    repo.repoFollowNum=res.data.repoFollowNum
  })
  function goto(pathName){
    router.push('/'+pathList[0] + '/' + pathList[1] + '/'+pathName)
  }

  function checkPower(){
    if(localStorage.getItem("userAccount") === pathList[0]) {
      return true
    }
    else {
      return false
    }
  }

  function forkRepo(){
    request.post('/fork',repo).then(res=>{
      if(res.code === 200){
        ElMessage({
          type:'success',
          message:'Fork successfully!'
        })
        router.push('/' + localStorage.getItem("userAccount") + '/' + repo.repoName)
      }
      else
        ElMessage({
          type:'warning',
          message:'Repository ' + repo.repoName + ' already exists in your account!'
        })
    })
  }

let test = computed(()=>{
  return check.value == true
})


onBeforeMount(()=>{
  isStar();
})

let check = ref();

function isStar(){
  request.get('/user/star/check',{
    params:{
      userAccount:localStorage.getItem("userAccount"),
      repoOwner:pathList[0],
      repoName:pathList[1],
    }
  }).then(res=>{

    if(res.code === 200)
      if(res.data === 1)
        check.value = true
    if(res.data === 0)
      check.value = false
  })
}

function changeStarFunction(){
  request.get('/user/star/change',{
    params:{
      userAccount:localStorage.getItem("userAccount"),
      repoOwner:pathList[0],
      repoName:pathList[1],
      testStar:check.value,
    }
  }).then(res=>{
    if(res.code === 200)
      ElMessage({
        type:'success',
        message:res.msg,
      })
    if(res.code === 500)
      ElMessage({
        type:'warning',
        message:res.msg,
      })
    console.log(res)
  })
  check.value=!check.value
  if (!check.value) repo.repoFollowNum--
  else repo.repoFollowNum++
}

function changeStar(){
  changeStarFunction()
}

function goToPersonCenter(){
  router.push('/' + pathList[0])
}

</script>


<style scoped>
  :deep(.el-tabs__header){
    margin: 0;
  }

</style>

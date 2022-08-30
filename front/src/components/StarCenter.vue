<template>
  <el-container>
    <el-main>
      <!-- 查询窗口-->
      <el-row style="margin-top:12px;">
        <el-col :span="12" >
          <el-input   v-model="input" placeholder="find a repository" :suffix-icon="Search"></el-input>
        </el-col>
        <el-col :span="5" style="margin-left:10px">
          <el-select v-model="type" placeholder="select type">
            <el-option label="All" value="0" />
            <el-option label="Public" value="1" />
            <el-option label="Private" value="2" />
          </el-select>
        </el-col>
        <el-col :span="4" style="margin-left:10px">
          <el-select v-model="sort_type" placeholder="sort">
            <el-option label="Last updated" value="0" />
            <el-option label="Name" value="1" />
            <el-option label="Stars" value="2" />
          </el-select>
        </el-col>
        <el-col :span="2" style="margin-left:10px">
          <el-button @click="gotoNew" type="success">new</el-button>
        </el-col>
      </el-row>
      <!-- 仓库信息展示-->
      <el-col :span ="30" v-for = "repo in repoInfo"  mode = "vertical">
        <!-- 是否有收藏的仓库 -->
          <el-divider>
            <el-icon><star-filled /></el-icon>
          </el-divider>
          <el-button text style="font-family: Calibri;font-size: 35px;color: limegreen" @click="gotoRepo(repo.repoName)">{{repo.repoName}}</el-button>
          <el-tag style="margin-left:10px" size = small >{{ repo.repoVisibility }}</el-tag>
          <div style = "float:right;margin-right: 20px">
            <el-button  :icon="StarFilled"  @click=" changeStar(true,repo.repoName )" > Star</el-button>
          </div>
          <br/>
          <span style="font-size: 20px;margin-top: 8px;margin-left: 14px">
                  {{repo.repoBio}}
                  <br/><br/>
                  <div style="font-size: 15px;font-family: Cambria;color:#808080">
                   {{repo.time}}
                  </div>
          </span>
      </el-col>
    </el-main>
  </el-container>

</template>

<script lang="ts">
export default {
  name: "StarCenter",
}
</script>

<script lang="ts" setup>
import {ref, reactive, onBeforeMount} from 'vue'
import { Search,Star,StarFilled } from '@element-plus/icons-vue'
import request from "../utils/request";
import router from "../router/index.js"
import {ElMessageBox,ElMessage} from "element-plus";

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('?')

let repoInfo = ref([]);


const type = ref("")
const sort_type = ref("")
const input = ref("")

//获取仓库信息
onBeforeMount(()=>{
  set();
  getStarInfo();
})

function set(){
  request.get('/user/star/repo',{
    params:{
      userAccount:pathList[0]
    }
  }).then(res=>{
        repoInfo.value = res.data;
      }
  )
}

function gotoRepo(name){
  let repoUrl = pathList[0]+'/'+name+'/code';
  console.log("111111");
  console.log(repoUrl);
  router.push(repoUrl);
}


let repo = reactive({
  repoID:'',
  repoName:'',
  repoBio:'',
  repoVisibility:'',
})

function gotoNew(){
  router.push({path: '/new'})
}

//--------------------------------------------------------------------------------------

let starInfo= ref([])

//获得用户收藏夹
function getStarInfo(){
  request.get('/user/star/repo',{
    params:{
      userAccount:pathList[0]
    }
  }).then(res=>{
        console.log(res);
        starInfo.value = res.data;
      }
  )
}

function isStar(name){
  console.log(name)
  console.log(starInfo.value)
  console.log(starInfo.value.indexOf(name))
  if(starInfo.value.indexOf(name) !== -1) {
    console.log("the name is exist")
    return true;
  }
  console.log("the name is aaaaaa")
  return false;
}


const  changeStar = (isStarred,repoNameTemp) => {
  ElMessageBox.confirm(
      'Do you want to change the star?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        console.log("The function is ddddddddd")
        console.log(isStarred)
        console.log(repoNameTemp)
        changeStarFunction(isStarred,repoNameTemp)
        router.go(0)
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'change canceled',
        })
      })
}

function changeStarFunction(isStarred,repoNameTemp){
  console.log("The function is eqewwqed")
  console.log(isStarred)
  console.log(repoNameTemp)
  request.get('user/star/change',{
    params:{
      userAccount:pathList[0],
      repoName:repoNameTemp,
      testStar:isStarred,
    }
  }).then(res=>{
    if(res.code == 200)ElMessage({
      type: 'success',
      message: res.msg,
    })
    if(res.code == 500)
      ElMessage({
        type: 'info',
        message: res.msg,
      })
  })
}


</script>

<style  scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}

</style>

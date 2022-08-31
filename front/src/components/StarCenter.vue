<template>
  <el-container>
    <el-main>
      <!-- 查询窗口-->
      <el-row style="margin-top:12px;">
        <el-col :span="12" >
          <el-input   v-model="input" placeholder="find a repository">
            <template #append>
              <el-button :icon="Search" @click="handleSearch"/>
            </template>
          </el-input>
        </el-col>
        <el-col :span="5" style="margin-left:10px">
          <el-select v-model="type" placeholder="select type">
            <el-option label="all" value="all" />
            <el-option label="public" value="public" />
            <el-option label="private" value="private" />
          </el-select>
        </el-col>
        <el-col :span="4" style="margin-left:10px">
          <el-select v-model="sort_type" placeholder="sort">
            <el-option label="None" value="None" />
            <el-option label="Name" value="name" />
            <el-option label="Stars" value="stars" />
          </el-select>
        </el-col>
        <el-col :span="2" style="margin-left:10px">
          <el-button @click="gotoNew" type="success">new</el-button>
        </el-col>
      </el-row>
      <!-- 仓库信息展示-->
      <el-col :span ="30" v-for = "(repo,index) in thisPersonStarInfo"  mode = "vertical">
        <!-- 是否有收藏的仓库 -->
        <div v-if="testVisibility(repo.repoVisibility)">
          <el-divider>
<!--            <el-icon><star-filled /></el-icon>-->
          </el-divider>
          <el-button text style="font-family: Calibri;font-size: 35px;color: limegreen" @click="gotoRepo(repo.repoOwner,repo.repoName)">{{repo.repoOwner}}/{{repo.repoName}}</el-button>
          <el-tag style="margin-left:10px" size = small >{{ repo.repoVisibility }}</el-tag>
          <div style = "float:right;margin-right: 20px">
            <div v-if= isStarList[index] >
              <el-button  :icon="StarFilled"  @click=" changeStar(true,repo.repoOwner,repo.repoName,index )" > Star</el-button>
            </div>
            <div v-else>
              <el-button  :icon="Star"  @click=" changeStar(false,repo.repoOwner, repo.repoName,index )" > Star</el-button>
            </div>
          </div>
          <br/>
          <span style="font-size: 20px;margin-top: 8px;margin-left: 14px">
                  {{repo.repoBio}}
                  <br/><br/>
                  <div style="font-size: 15px;font-family: Cambria;color:#808080">
                   {{repo.time}}
                  </div>
          </span>
        </div>
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
import { Search,StarFilled } from '@element-plus/icons-vue'
import request from "../utils/request";
import router from "../router/index.js"
import {ElMessageBox,ElMessage} from "element-plus";

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('?')

const type = ref("")
const sort_type = ref("")
const input = ref("")

let thisPersonStarInfo = ref([])
let nowUserStarInfo = ref([])
let isStarList = ref([])


const publicString = "public"

function testVisibility(visibility){
  if(localStorage.getItem("userAccount") === pathList[0])
    return true
  else {
    if (visibility === publicString)
      return true
    else
      return false
  }
}

//获取仓库信息
onBeforeMount(()=>{
  set();
})

function gotoRepo(repoOwner,repoName){
  let repoUrl = repoOwner+'/'+repoName + '/code';
  router.push(repoUrl);
}

function gotoNew(){
  router.push({path: '/new'})
}

//--------------------------------------------------------------------------------------

//获得用户收藏夹
async function set(){
  await request.get('/user/star/repo',{
    params:{
      userAccount:pathList[0]
    }
  }).then(res=>{
        thisPersonStarInfo.value = res.data;
      }
  )
  await request.get('/user/star/repo',{
    params:{
      userAccount:localStorage.getItem("userAccount")
    }
  }).then(res=>{
        nowUserStarInfo.value = res.data;
      }
  )
  let i = 0
  for (i = 0; i < thisPersonStarInfo.value.length; i++) {
    isStarList.value.push(isStar(thisPersonStarInfo.value[i].repoOwner, thisPersonStarInfo.value[i].repoName))
  }
}

function isStar(repoOwnerTemp,repoNameTemp){
  if (nowUserStarInfo.value!==null) {
    return nowUserStarInfo.value.some((item, i) => {
      return (item.repoOwner === repoOwnerTemp) && (item.repoName === repoNameTemp)
    })
  } else return false
}


const  changeStar = (isStarred,repoOwnerTemp,repoNameTemp,index) => {
  changeStarFunction(isStarred,repoOwnerTemp, repoNameTemp)
  isStarList.value[index]=!isStarList.value[index]
}

function changeStarFunction(isStarred,repoOwnerTemp,repoNameTemp){
  request.get('user/star/change',{
    params:{
      userAccount:localStorage.getItem('userAccount'),
      repoOwner:repoOwnerTemp,
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

function handleSearch() {
  request.get('/user/star/search', {
    params: {
      userAccount: pathList[0],
      searchValue: input.value,
      searchVisibility: type.value,
      searchSort: sort_type.value,
    }
  }).then(res => {
    thisPersonStarInfo.value = res.data
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

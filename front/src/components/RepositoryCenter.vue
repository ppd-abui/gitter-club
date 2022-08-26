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
        <div id = "card">
          <el-divider>
            <el-icon><star-filled /></el-icon>
          </el-divider>
          <el-button text style="font-family: Calibri;font-size: 35px;color: limegreen" @click="gotoRepo(repo.repoName)">{{repo.repoName}}</el-button>
          <el-tag style="margin-left:10px" size = small >{{ repo.repoVisibility }}</el-tag>
<!--          <div style = "float:right;margin-right: 20px">-->
<!--            <el-button  :icon="item.star==0 ?Star:StarFilled"  @click="testStar(item.star)" > Star</el-button>-->
<!--          </div>-->
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
  name: "RepositoryCenter",
}
</script>

<script lang="ts" setup>
import {ref, reactive, onBeforeMount} from 'vue'
import { Search,Star,StarFilled } from '@element-plus/icons-vue'
import request from "../utils/request";
import router from "../router/index.js"

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')

let repoInfo = ref([]);


const type = ref("")
const sort_type = ref("")
const input = ref("")

//获取仓库信息
onBeforeMount(()=>{
  set();
})

function set(){
    request.get('/repo/info',{
      params:{
        userAccount:pathList[0]
      }
    }).then(res=>{
          console.log(res);
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

function testStar(bool){
  if(bool == 1)
    bool = 0;
  else
    bool = 1;
  this.reload();
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

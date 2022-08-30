<script>
export default {
  name: "IssueList"
}
</script>

<template>
  <div style="width: 100%;">
    <div style="width: 80%; min-width: 600px; margin: auto">
      <div class="rectangle" style="text-align: center; margin-top: 20px">
        <div style="margin-top: 30px;font-family: Bahnschrift">
          Label issues and pull requests for new contributors
        </div>
      </div>
      <div style="margin-top: 20px; display: flex">
        <el-input v-model="input"  placeholder="Select one issue" >
          <template #append>
            <el-button @click="searchIssue(input)"><el-icon><Search/></el-icon></el-button>
          </template>
        </el-input>
        <el-button style="" @click="gotoCreateIssue()" type="primary">New issue</el-button>
      </div>
<!--      <div class="rectangle2">-->
<!--        <el-icon style="vertical-align: -10%;margin-left: 10px"><Aim /></el-icon>-->
<!--        <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Open</el-button>-->
<!--        <el-icon style="vertical-align: -10%;margin-left: 10px"><Check /></el-icon>-->
<!--        <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>-->
<!--        <el-button style="margin-left:600px;background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>-->
<!--      <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>-->
<!--      <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>-->
<!--      </div>-->
      <el-table :data="searchDateList" style="margin-top:20px; border: 1px solid; border-radius:10px;border-color: #d1d1d1;border-right-color: #d1d1d1;">
        <el-table-column type="selection" width="55px" />
        <el-table-column prop="issueTitle" label="IssueTitle" width="" >
          <template #default="scope">
            <el-link @click="openIssue(searchDateList[scope.$index].issueTitle)" > {{searchDateList[scope.$index].issueTitle}}
            </el-link>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>

import router from "../../router";
import {onBeforeMount, ref} from 'vue'
import request from "../../utils/request";


//正则表达匹配获取路由中的单个路径
//path[0]为 repoOwner, path[1] 为 repoName
  let path = router.currentRoute.value.fullPath
  let pathList = path.substr(1).split('/')
  let input = ref('')
  let searchDateList = ref([]);

//--------------------------------------
function openIssue(issueTitle){
  router.push({path: '/'+pathList[0]+'/'+pathList[1]+'/issues/'+issueTitle})
}
function searchIssue(){
  console.log('search Issue')
  isSearch.value=true;
  request.get('/issue/search', {
    params: {
      keyWord: input.value,
      repoOwner: pathList[0],
      repoName: pathList[1]
    }
  }).then(res => {
    if (res.code === 200) {
      searchDateList.value=res.data
    }
  })
}
  function gotoCreateIssue()
  {
    router.push({path: '/'+pathList[0]+'/'+pathList[1]+'/newissue'})
  }

  onBeforeMount(() => {
    request.get("/issue/get",{
      params: {
        repoOwner: pathList[0],
        repoName: pathList[1]
      }
    })
        .then((res) => {
          searchDateList.value= res.data;   //赋值
        })
        .catch((error) => {
        });
  });
</script>

<style scoped>
  .rectangle{
    height: 80px;
    border: 1px solid #d1d1d1;
    border-radius: 10px;
  }
</style>
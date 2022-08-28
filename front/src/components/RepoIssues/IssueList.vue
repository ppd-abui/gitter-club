<script>
export default {
  name: "IssueList"
}
</script>

<template>
  <div style="width: 100%">
    <div class="rectangle" style="text-align: center;margin-top: 20px">
      <div style="margin-top: 30px;font-family: Bahnschrift">
        Label issues and pull requests for new contributors
      </div>
    </div>
    <div style="margin-left: 320px;margin-top: 20px;">
      <el-popconfirm
          confirm-button-text="OK"
          cancel-button-text="No, Thanks"
          :icon="InfoFilled"
          icon-color="#626AEF"
          title="Are you sure to delete this?"
      >
        <template #reference>
          <el-button style="border-radius: 10px;background-color: #f7f7f7">Filters</el-button>
        </template>
      </el-popconfirm>
      <el-input style="width: 500px;" v-model="input"  placeholder="Select one issue" />
      <el-icon @click="searchIssue(input)" color="black" style="position: relative; top: 5px; left: 0px"><Search/></el-icon>
      <el-button style="margin-left: 20px;border-radius: 10px">Labels</el-button>
      <el-button style="border-radius: 10px">Milestones</el-button>
      <el-button style="background-color: aquamarine;border-radius: 10px" @click="gotoCreateIssue()">New issue</el-button>
    </div>
    <div class="rectangle2">
      <el-icon style="vertical-align: -10%;margin-left: 10px"><Aim /></el-icon>
      <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Open</el-button>
      <el-icon style="vertical-align: -10%;margin-left: 10px"><Check /></el-icon>
      <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>
      <el-button style="margin-left:600px;background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>
<!--      <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>-->
<!--      <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>-->
    </div>
    <div v-if="isSearch">
      <el-table :data="searchDateList" style="width: 900px;border: 1px solid;border-radius:10px;border-color: #d1d1d1;border-right-color: #d1d1d1;margin:auto">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="issueTitle" label="IssueTitle" width="900" >
          <template #default="scope">
            <el-link @click="openIssue(searchDateList[scope.$index].issueTitle)" > {{searchDateList[scope.$index].issueTitle}}
            </el-link>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-else>
      <el-table :data="arr" style="width: 900px;border: 1px solid;border-radius:10px;border-color: #d1d1d1;border-right-color: #d1d1d1;margin:auto">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="issueTitle" label="IssueTitle" width="900" >
          <template #default="scope">
            <el-link @click="openIssue(arr[scope.$index].issueTitle)" > {{arr[scope.$index].issueTitle}}
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
  let isSearch=ref(false);
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
  let  arr = ref([]);
  onBeforeMount(() => {
    lista();
  });

  const lista = () => {
    //发送请求

    request.get("/issue/get",{
      params: {
        repoOwner: pathList[0],
        repoName: pathList[1]
      }
    })
        .then((res) => {
          arr.value= res.data;   //赋值
        })
        .catch((error) => {
        });
  };
</script>

<style scoped>
  .rectangle{
    height: 80px;
    width: 900px;
    margin: auto;
    border: 1px solid #d1d1d1;
    border-radius: 10px;
  }
  .rectangle2{
    height: 30px;
    width: 900px;
    border:1px solid #d1d1d1 ;
    background-color: #f7f7f7;
    border-radius: 10px;
    margin: 10px auto auto;
  }
</style>
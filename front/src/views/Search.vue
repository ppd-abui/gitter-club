<script lang="ts">
import Index from '../components/Index.vue'
export default {
  name: "SearchFile",
  components: {
    Index
  }
}
</script>

<template>
  <el-container style="padding: 0">
    <el-header style="padding: 0"><Index/></el-header>
    <el-main>

      <el-container style="margin: 40px auto">
<!--   搜索方式  -->
        <el-aside style="padding-left: 50px; height: 100%">
          <el-menu
              default-active="1"
              mode="vertical"
              @select="selectSearchType"
          >
            <el-menu-item index="1">Repositories</el-menu-item>
            <el-menu-item index="2">Users</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <div style="display:flex;">
<!--     搜索框-->
            <el-input v-model="keyword"></el-input>
            <el-button @click="research" style="margin-left: 30px; margin-right: 10px"><el-icon><Search/></el-icon></el-button>
          </div>
          <div style="margin-top: 20px">
            <span v-if="ifSearchRepo">{{searchDateList.length}} repository results</span>
            <span v-else>{{searchDateList.length}} users results</span>
            <el-select v-model="sortClassify" class="m-2" placeholder="Sort" size="large" style="position: absolute; right: 50px">
<!--              <el-option-->
<!--                  v-for="item in sortClassify"-->
<!--                  :key="item.value"-->
<!--                  :label="item.label"-->
<!--                  :value="item.value"-->
<!--              />-->
            </el-select>
          </div>
<!--搜索仓库-->
          <div v-if="ifSearchRepo" style="width: 100%; margin-top: 20px; ">
            <el-table :data="searchDateList" style="width: 100%; border-radius: 10px;">
              <el-table-column prop="" label="">
                <template #default="scope">
                  <div style="height: 40px; width: 100%; display: flex">
                    <div style="width: 20px; height: 20px; position: relative; top: 3px;">
                      <el-icon size="20px"><Collection/></el-icon>
                    </div>
                    <div style="position: relative; bottom: 7px; left: 6px; display: flex" >
                      <el-link @click="goRepo(scope.$index)" type="primary" style="font-size: 20px">{{searchDateList[scope.$index].repoOwner}}/{{searchDateList[scope.$index].repoName}}</el-link>
                    </div>
                  </div>
                  <div style="font-family: Calibri; font-size: 16px; color: grey; margin-left: 30px">{{searchDateList[scope.$index].repoBio}}</div>
                </template>
              </el-table-column>
            </el-table>
          </div>
<!--搜索用户-->
          <div v-else style="width: 100%; margin-top: 20px; ">
            <el-table :data="searchDateList" style="width: 100%; border-radius: 10px;">
              <el-table-column prop="" label="">
                <template #default="scope">
                  <div style="height: 40px; width: 100%; display: flex">
                    <div style="width: 20px; height: 20px; position: relative; top: 3px;">
                      <el-icon size="20px"><Avatar/></el-icon>
                    </div>
                    <div style="position: relative; bottom: 6px; left: 10px; display: flex" >
                      <el-link @click="goUser(scope.$index)" type="primary" style="font-size: 20px">{{searchDateList[scope.$index].userAccount}}</el-link>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>

        </el-main>
      </el-container>

    </el-main>
  </el-container>

</template>


<script lang="ts" setup>
import {onMounted, ref} from "vue";
import router from '../router'
import request from '../utils/request.js'

  const sortClassify = [
    'Sort: default',
  ]
  let ifSearchRepo=ref(true)

  let keyword=ref(router.currentRoute.value.query.keyWord)

  let searchDateList = ref([])

  function searchRepoRequest(){
    console.log('searchRepo')
    request.get('/repo/search', {
      params: {
        keyWord: keyword.value
      }
    }).then(res => {
      if (res.code === 200) {
        searchDateList.value=res.data
      }
    })
  }

  function searchUserRequest(){
    console.log('search user')
    request.get('/user/search', {
      params: {
        keyWord: keyword.value
      }
    }).then(res => {
      if (res.code === 200) {
        searchDateList.value=res.data
        console.log(searchDateList.value)
      }
    })
  }

  function goRepo(index){
    let direction = JSON.parse(JSON.stringify(searchDateList.value))[index]
    router.push({path: '/'+direction.repoOwner+'/'+direction.repoName})
  }
  function goUser(index){
    let direction = JSON.parse(JSON.stringify(searchDateList.value))[index]
    router.push({path: '/'+direction.userAccount})
  }

  function research(){
    if (keyword.value!=='')
      if (ifSearchRepo.value) searchRepoRequest()
      else searchUserRequest()
  }

  const selectSearchType = (key: string, keyPath: string[]) => {
    if (key==='1') ifSearchRepo.value=true
    else ifSearchRepo.value=false
    console.log(ifSearchRepo.value)
    research()
  }

  onMounted(()=>{
    research()
  })
</script>

<style scoped>

</style>
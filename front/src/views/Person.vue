<script>
import Index from '../components/Index.vue'
import RepositoryCenter from "../components/RepositoryCenter.vue";
import StarCenter from "../components/StarCenter.vue";
export default {
  name: "Person",
  components: {
    Index,
    RepositoryCenter,
    StarCenter
  }
}
</script>

<template>
  <el-container>
<!--Index-->
    <el-header style="padding: 0"> <Index/> </el-header>
    <el-main style="padding: 0">
      <el-container style="display:flex;">
        <el-header style="padding: 0; z-index: 1">
<!--Menu（仓库/关注）-->
          <el-menu
              :default-active="'1'"
              mode="horizontal"
              style="height: 80px"
          >
            <el-menu-item style="margin-left: 50%" index="1" @click="go('RepositoryCenter')"><el-icon><HomeFilled /></el-icon> Repository</el-menu-item>
            <el-menu-item index="2" @click="go('StarCenter')"> <el-icon><Star /></el-icon> Star</el-menu-item>
          </el-menu>
        </el-header>
        <el-container>
  <!--个人信息-->
          <el-aside style="width: 300px">
            <div style="width: 100%">
              <img style="width: 100%; object-fit: fill;" src="../assets/white.png">
            </div>
            <el-button color="#f0f0f4" style="height:40px; width: 130px">Edit Profile</el-button>
          </el-aside>
    <!--子界面路由-->
          <el-main>
            <RepositoryCenter v-if="showTab='RepositoryCenter'"/>
            <StarCenter v-if="showTab='StarCenter'"/>
          </el-main>
        </el-container>
      </el-container>
    </el-main>
  </el-container>
<!--  <div id = "hello">-->
<!--    <router-view/>-->
<!--  </div>-->
</template>

<script setup>
  import router from "../router";
  import {useRoute} from "vue-router/dist/vue-router";

  function go(path){
    router.push({name: 'space', query: {tab: path}})
    router
  }
  const route=useRoute()
  const showTab=route.query.tab

</script>

<style scoped>

</style>

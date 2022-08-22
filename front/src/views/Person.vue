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
            <el-menu-item style="margin-left: 370px" index="1" @click="go('RepositoryCenter')"><el-icon><HomeFilled /></el-icon> Repository</el-menu-item>
            <el-menu-item index="2" @click="go('StarCenter')"> <el-icon><Star /></el-icon> Star</el-menu-item>
          </el-menu>
        </el-header>
        <el-container style="z-index: 2">
  <!--个人信息-->
          <el-aside style="width: 400px; position: relative;  bottom: 60px; z-index: 1">
            <div style="position: relative; width: 100%; margin: 0">
              <img style="width: 100%; object-fit: fill;" src="../assets/black.png">
            </div>
            <div style="width: 350px; margin-left: 50px; position: relative; bottom: 30px">
              <div style="font-family: 'Calibri Light'; font-size: 20px; color: grey">ppd-abui</div>
              <el-button style="width: 80%; margin-top: 10px">Edit Profile</el-button>
            </div>
          </el-aside>
    <!--子界面路由-->
          <el-main style="padding: 0; z-index: 2; position: relative; right: 40px">
            <RepositoryCenter v-if="tab==='RepositoryCenter'"/>
            <StarCenter v-if="tab==='StarCenter'"/>
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
  import {ref} from "vue";

  let tab = ref('RepositoryCenter')

  function go(path){
    router.push({name: 'space', query: {tab: path}})
    tab.value=path
  }
  const route=useRoute()

</script>

<style scoped>

</style>

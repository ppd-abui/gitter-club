<script lang = "ts" >
export default {
  name: "RepositorySetting"
}
</script>

<template>
  <el-container style = "margin-left: 20px;margin-top: 10px">
    <el-aside width = "200px">
      <el-menu
        default-active="choose"
        >
        <el-menu-item index = "1" @click = "go('general')">
          <el-icon><More /></el-icon>
          <span>General</span>
        </el-menu-item>
        <el-menu-item index = "2" @click = "go('collaborator')">
          <el-icon><User /></el-icon>
          <span>Collaborator</span>
        </el-menu-item>

      </el-menu>
    </el-aside>
    <el-main>
      <router-view/>
    </el-main>
  </el-container>
</template>

<script lang = "ts" setup>
import {ref} from "vue";
import router from '../router'
import General from "../components/RepositorySetting/General.vue"

const choose = ref("1")
function go(path){
  router.push({name: path})
}
</script>

<style scoped>

</style>

<script lang = "ts" >
export default {
  name: "RepositorySetting"
}
</script>

<template>
  <el-container style = "margin-left: 20px;margin-top: 10px">
    <el-aside width = "13%">
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

      <el-divider direction="vertical" style="height: 950px;margin-left: 99.9%"/>
    </el-aside>
    <el-main>
      <router-view/>
    </el-main>
  </el-container>
</template>

<script lang = "ts" setup>
import {ref} from "vue";
import router from '../router'

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')

const choose = ref("1")
function go(path){
  router.push({path: '/'+pathList[0]+'/'+pathList[1]+'/settings/'+path})
}
</script>

<style scoped>

</style>

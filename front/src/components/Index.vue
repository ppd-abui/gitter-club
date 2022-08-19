<script lang = "ts">
export default {
  name: 'Index'
}
</script>

<template>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      background-color="#000000"
      style="height: 60px"
  >
    <img style="margin-top: 5px ;margin-bottom: 7px;margin-left: 15px; height:80% " src="../assets/white.png">

    <el-input style=" background-color: transparent; border: none; width: 300px; height: 25px; margin-top:17px; margin-left: 15px" v-model="input" placeholder="Search or jump to...">
    </el-input>

    <img @click=goCenter style="position: absolute; right: 20px; margin-top: 5px ; height:80%" src="src/assets/white.png">
  </el-menu>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import router from '../router'
  const input = ref("")
  function goCenter(){
    router.push({name: 'space'})
  }
</script>

<style lang="scss" scoped>
  :deep(.el-input__inner){
    background: transparent;
    font-family: "Calibri";
    font-size: 14px;
    color: #d1d1d1;
  }

  :deep(.el-input__wrapper){
    background-color: transparent;
    height: 100%;
    box-shadow: none;
    border: #808080 1px solid ;
  }

  :deep(.el-input__wrapper):focus-within{
    border: #d1d1d1 1px solid;
  }

</style>



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
    <img style="margin-top: 5px ;margin-bottom: 7px;margin-left: 15px; height:80% " src="src/assets/white.png">
    <el-col :span="8">
      <el-input style="margin-top:12px;margin-left: 5px"  v-model="input" placeholder="Please input" :suffix-icon="Search"></el-input>
    </el-col>
    <img style="position: absolute; right: 20px; margin-top: 5px ; height:80%" src="src/assets/white.png">
  </el-menu>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
const input = ref("")

</script>
<style scoped>

</style>



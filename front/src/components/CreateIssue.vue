<script lang="ts">
export default {
  name: "CreateIssue.vue",
}
</script>
<template>
  <div class="common-layout">
    <el-container>

      <el-header style="padding:0px"><index1></index1></el-header>
      <el-header > <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          style="padding: 0px"
      >
        <el-menu-item index="1" @click="go('code')"><el-icon><Edit /></el-icon>Code</el-menu-item>
        <el-menu-item index="2" @click="go('issues')"><el-icon><ChatSquare /></el-icon>Issues</el-menu-item>
        <el-menu-item index="3" @click="go('pull')"><el-icon><SetUp /></el-icon>Pull requests</el-menu-item>
        <el-menu-item index="4" @click="go('settings')"><el-icon><Setting /></el-icon>Settings</el-menu-item>
      </el-menu></el-header>
      <el-main>
        <div class="rectangle">
          <el-input  style="margin-top: 10px;" v-model="input" placeholder="Title" />
          <el-tabs  style="margin-top: 5px;margin-left: 10px" v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane  label="Write" name="first"><el-input
                v-model="textarea"
                :rows="8"
                type="textarea"
                placeholder="Please input"
            /></el-tab-pane>

          </el-tabs>
          <el-button  style="margin-left:620px;background-color: chartreuse" @click="submit()">Submit new issue</el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import RepositoryS from "../components/RepositoryS.vue";
import index1 from "../components/Index.vue";
import router from '../router'
import { ref } from 'vue'
const textarea = ref('')
function submit() {

}
</script>

<style scoped>
.rectangle{
  height: 320px;
  width: 800px;
  margin: 30px auto auto;
  border-radius: 10px;
  border: 3px solid #f7f7f7;
}
</style>
<template>
    <div class="common-layout">
      <el-container>
        <el-header  style="font-size: 30px;padding:0px">
          <index1></index1>
        </el-header>
        <el--header style="margin-top: 10px">
          <div  class="icon">
            <el-icon style="margin-left: 20px;vertical-align: middle"><SetUp /></el-icon>
          </div>
          <div class="info">
            <repositoryS style="margin-left: 20px"></repositoryS>
          </div>
          <div style="display: flex;justify-content: flex-end">
          <div class="border">
            <el-popover v-model:visible="visible" placement="top" :width="160">
              <p>Are you sure to delete this?</p>
              <div style="text-align: right; margin: 0">
                <el-button size="small" text @click="visible = false">cancel</el-button>
                <el-button size="small" type="primary" @click="visible = false"
                >confirm</el-button
                >
              </div>
              <template #reference>
                <el-button style="margin-right: 80px" @click="visible = true">Delete</el-button>
              </template>
            </el-popover>
            <el-popover v-model:visible="visible" placement="top" :width="160">
              <p>Are you sure to delete this?</p>
              <div style="text-align: right; margin: 0">
                <el-button size="small" text @click="visible = false">cancel</el-button>
                <el-button size="small" type="primary" @click="visible = false"
                >confirm</el-button
                >
              </div>
              <template #reference>
                <el-button style="margin-right: 100px" @click="visible = true">Delete</el-button>
              </template>
            </el-popover>

     </div>
          </div>
        </el--header>
        <el-header>
          <el-menu
              :default-active="activeIndex"
              class="el-menu-demo"
              mode="horizontal"
              @select="handleSelect"
          >
            <el-menu-item index="1"><el-icon><Edit /></el-icon>Code</el-menu-item>
            <el-menu-item index="2"><el-icon><ChatSquare /></el-icon>Issues</el-menu-item>
            <el-menu-item index="3" ><el-icon><SetUp /></el-icon>Pull requests</el-menu-item>
            <el-menu-item index="4"><el-icon><Setting /></el-icon>Settings</el-menu-item>
          </el-menu>
        </el-header>
<!--        <左侧界面>-->
        <el-container>
          <el-aside width="1100px">
        <code1></code1>
          </el-aside>
<!--          <右侧界面></右侧界面>-->
          <el-main style="border-left:1px solid ;height:500px;font-size: x-large;font-weight: bold">About</el-main>
        </el-container>
      </el-container>
    </div>
</template>

<script lang="ts">
import index1 from '../components/Index.vue';
import repositoryS from "../components/RepositoryS.vue";
import code1 from "../components/Code.vue"
export default {
  name: "Index",
  components:{
   index1,
    repositoryS
  }
}
</script>
<script lang="ts" setup>
import { ref } from 'vue'
import RepositoryS from "../components/RepositoryS.vue";
const visible = ref(false)
const activeIndex = ref('1')
const activeIndex2 = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>
<style>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
.icon,.info {
  display: inline-block;
  vertical-align: middle;
}
.border
{
  display: inline-block;

}

</style>

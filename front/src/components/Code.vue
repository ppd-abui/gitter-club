<script lang="ts">
export default {
  name: "Code"
}
</script>

<template>
  <div style="display: flex; width: 100%">
    <div style="height: 100%; min-width: 300px; width: 80%; margin-left: 50px; margin-top: 20px">
      <div style="display: flex">
        <el-button @click="branchSwitchShow">
          <el-icon><Operation/></el-icon>
          main
          <el-icon><CaretBottom/></el-icon>
        </el-button>
        <div style="margin-top: 5px; margin-left: 15px; display: flex">
          <el-icon style="margin-top: 3px; margin-right: 3px"><Operation/></el-icon>
          {{1}}
          <span style="margin-left: 5px; font-family: Calibri; font-size: 18px; color: grey">branch</span>
        </div>
        <div style="width: 50%"></div>
        <el-button style="margin-right: 10px">Go to file</el-button>
        <el-dropdown>
          <el-button>Add file
            <el-icon><CaretBottom/></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>create new file</el-dropdown-item>
              <el-dropdown-item>upload files</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button type="primary" style="margin-left: 10px">Clone</el-button>
      </div>
<!--悬浮窗-->
      <div v-show="ifBranchSwitchShow" style="z-index: 2; position: absolute;">
        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 10px; width: 300px; background-color: white">
          <div style="display: flex; margin-top: 10px;">
            <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Switch branches</span>
            <el-icon @click="branchSwitchHide" color="gray" style="position: absolute; right: 10px;"><CloseBold/></el-icon>
          </div>
          <el-divider style="margin-top: 10px; margin-bottom: 10px;"/>
          <el-input placeholder="Find or create a branch..." style="width: 90%; margin: 0px 14px 0px 14px;"/>
          <el-divider style="margin-top: 10px; margin-bottom: 0px;"/>

        </div>
      </div>
<!--主体-->
      <div style="z-index: 1; border: #b1b1b1 solid; width: 100%; height: 400px; margin-top: 20px; border-radius: 10px; box-shadow: #e4e7ed 2px 2px;">

      </div>

    </div>

<!--about-->
    <div style="width: 350px; padding: 0 50px 0 50px">
      <div style="margin: 50px 0 0 0px; font-family: Calibri; font-size: 24px;">
        About
      </div>
      <div style="font-style: italic; font-family: Calibri; font-size: 18px; color: #b1b1b1; margin-top: 20px">
        No description, website, or topics provided.
      </div>
      <el-divider/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

let ifBranchSwitchShow = ref(false)

function branchSwitchShow(){
  ifBranchSwitchShow.value=!ifBranchSwitchShow.value
}
function branchSwitchHide(){
  ifBranchSwitchShow.value=false
}
</script>

<style scoped>

</style>
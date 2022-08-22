<script lang="ts">
import Index from '../components/Index.vue'
import RepositoryS from "../components/RepositoryS.vue"
import Code from "../components/Code.vue"
export default {
  name: "Index",
  components:{
    Index,
    RepositoryS,
    Code
  }
}
</script>

<template>
  <el-container>
<!--    index-->
    <el-header  style="padding:0px; margin: 0"><index/></el-header>
      <div style="background-color: #f6f8fa; padding: 0">
  <!--    用户名 仓库名-->
        <div  style="display: flex; margin-left: 20px; margin-top: 20px">
          <div style="width: 20px; height: 20px; margin-left: 10px; position: relative; top: 8px"><Collection/></div>
          <div style="margin: 0px 0 0 10px; display: flex">
            <el-link href="/space" type="primary" style="font-size: 24px; font-family: 'Calibri Light'">{{repo.repoOwner}}</el-link>
            <div style="font-size: 24px; margin: 0 10px 0 10px">/</div>
            <el-link type="primary" style="font-size: 24px; font-family: 'Calibri Light'; font-weight: bold">{{repo.repoName}}</el-link>
          </div>
          <el-tag style="margin-top: 5px; margin-left: 10px" round type="info">{{repo.repoVisibility}}</el-tag>
          <div style="width: 35%;"></div>
          <el-button style="position:relative; top: 5px;" @click="changeStar">
            <el-icon v-if="!isStar"><Star/></el-icon>
            <el-icon v-if="isStar"><StarFilled/></el-icon>
            <span style="font-family: Calibri; font-size: 14px; font-weight: bold; margin: 2px 10px 0 10px">Star</span>
            <div style="width: 20px; height: 20px; border-radius: 50%; background-color: #e7e7e7">
              <span style="color: black; position: relative; top: 4px">{{1}}</span>
            </div>
          </el-button>

        </div>

  <!--      辅助线-->
        <div style="width: 60px; height: 2px; position: relative; top: 60px; background-color: #e4e7ed"/>
  <!--      菜单-->
        <el-tabs v-model="chooseTab" style="margin-top: 20px; margin-left: 60px" class="demo-tabs">
          <el-tab-pane label="code" name="code">
            <template #label>
              <div style="display: flex;">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><Edit /></div>
                <div style="margin-left: 10px">Code</div>
              </div>
            </template>
          </el-tab-pane>

          <el-tab-pane label="issues" name="issues">
            <template #label>
              <div style="display: flex">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><ChatSquare /></div>
                <div style="margin-left: 10px">Issues</div>
              </div>
            </template>
          </el-tab-pane>

          <el-tab-pane label="pull" name="pull">
            <template #label>
              <div style="display: flex">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><SetUp /></div>
                <div style="margin-left: 10px">Pull requests</div>
              </div>
            </template>
           </el-tab-pane>

          <el-tab-pane label="settings" name="settings">
            <template #label>
              <div style="display: flex">
                <div style="width: 20px; height: 20px; position: relative; top: 5px"><Setting /></div>
                <div style="margin-left: 10px">Settings</div>
              </div>
            </template>
          </el-tab-pane>
        </el-tabs>
      </div>

    <el-container>
        <router-view/>
    </el-container>
  </el-container>
</template>


<script lang="ts" setup>
import {onMounted, reactive, ref, watch, watchEffect} from 'vue'
  import router from '../router'
  let chooseTab = ref('')

  let repo = reactive({
    repoOwner: 'ppd-abui',
    repoName: 'gitter-club',
    repoBio: '',
    repoVisibility: 'public',
    repoFollowers: '',
    repoIssues: '',
    repoCollaborators: '',
  })

  watchEffect(() => router.push({name: chooseTab.value}))

  let isStar = ref(true)
  function changeStar(){
    isStar.value=!isStar.value
  }

</script>


<style scoped>
  :deep(.el-tabs__header){
    margin: 0;
  }

</style>

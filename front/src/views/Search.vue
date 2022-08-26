<script lang="ts">
import Index from '../components/Index.vue'
export default {
  name: "SearchFile",
  components: {
    Index
  }
}
</script>

<template>
  <el-container style="padding: 0">
    <el-header style="padding: 0"><Index/></el-header>
    <el-main>

      <el-container style="margin: 40px auto">
        <el-aside style="padding-left: 50px; height: 100%">
          <el-menu
              default-active="1"
              mode="vertical"
              @select="selectSearchType"
          >
            <el-menu-item index="1">Repositories</el-menu-item>
            <el-menu-item index="2">Users</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <div style="display:flex;">
            <el-input></el-input>
            <el-button style="margin-left: 30px; margin-right: 10px"><el-icon><Search/></el-icon></el-button>
          </div>
          <div style="margin-top: 20px">
            <span>625 repository results</span>
            <el-select v-model="sortClassify" class="m-2" placeholder="Sort" size="large" style="position: absolute; right: 50px">
<!--              <el-option-->
<!--                  v-for="item in sortClassify"-->
<!--                  :key="item.value"-->
<!--                  :label="item.label"-->
<!--                  :value="item.value"-->
<!--              />-->
            </el-select>
          </div>

          <div style="width: 100%; margin-top: 20px; ">
            <el-table :data="searchDateList" style="width: 100%; border-radius: 10px;">
              <el-table-column prop="fileName" label="">
                <template #default="scope">
                  <div style="height: 40px; width: 100%; display: flex">
                    <div style="width: 20px; height: 20px; position: relative; top: 3px;">
                      <el-icon size="20px"><Collection/></el-icon>
                    </div>
                    <div style="position: relative; bottom: 7px; left: 6px; display: flex" >
                      <el-link @click="goRepo(scope.$index)" type="primary" style="font-size: 20px">{{searchDateList[scope.$index].repoOwner}}/{{searchDateList[scope.$index].repoName}}</el-link>
                    </div>
                  </div>
                  <div style="font-family: Calibri; font-size: 16px; color: grey; margin-left: 30px">{{searchDateList[scope.$index].repoBio}}</div>
                </template>
              </el-table-column>
            </el-table>
          </div>

        </el-main>
      </el-container>

    </el-main>
  </el-container>

</template>


<script lang="ts" setup>
import {ref} from "vue";
import router from '../router'

  const sortClassify = [
    'Sort: default'
  ]

  let keyword=ref(router.currentRoute.value.query.keyWord)

  let searchDateList = ref([{
    repoOwner: 'admin',
    repoName: 'test',
    repoBio: 'admin test bio'
  }])

  function goRepo(index){

  }

  const selectSearchType = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
  }
</script>

<style scoped>

</style>
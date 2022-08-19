
<script lang="ts">
  import RepositoryS from '../components/RepositoryS.vue'
  import Index from'../components/Index.vue'
  export default {
    name: 'Home',
    components:{
        RepositoryS,
        Index
    }
  }
</script>

<template>
  <el-container>
    <el-header style="padding: 0"><Index/></el-header>
    <el-container>
      <el-aside style="border-right: #e7e7e7 solid 1px; padding: 0 25px 0 25px" width="300px">
        <div style="display: flex; margin-top: 30px; ">
          <div style="margin: 10px 0 0 5px; font-family: Calibri; font-size: 16px; font-weight: bold">Recent Repositories</div>
          <el-button @click="goto('createRepo')" style="margin-left: 43px; padding: 0 10px 0 10px">
            <el-icon style="margin-right: 4px"><Collection/></el-icon>
            New
          </el-button>
        </div>
        <el-input style="margin-top: 10px" placeholder="Find a repository..."/>
        <div>
          <RepositoryS/>
          <RepositoryS/>

        </div>
        <el-divider/>
      </el-aside>

      <el-main style="background-color: #f6f8fa; display: flex">
        <div style="min-width: 600px; width: 60%; height: 600px;">
          <div style="position: relative; left: 15px; top: 10px; display: flex">
            <div style="position: relative; font-family: 'Calibri Light'; font-size: 36px;">Start with</div>
            <div style="font-family: Cinzel; font-size: 36px; margin: 0 0 0 20px">GitterClub</div>
          </div>
          <el-divider/>
          <div style="margin-top: 30px; background-color: white; padding: 20px; border-radius: 10px; border: #d1d1d1 solid 1px">

            <div style="font-family: Calibri; font-size: 28px; margin: 20px">
              Discover interesting projects and people to populate your personal news feed.
            </div>
            <div style="background-color: #f6f8fa; border: #d1d1d1 solid 1px; border-radius: 10px; margin: 30px 10px">
              <div class="helpList">
                <div class="listBio">To Create a Repository</div>
                <el-button class="buttonList">New Repository</el-button>
              </div>
              <div class="helpList">
                <div class="listBio">To Explore</div>
                <el-button class="buttonList">Search</el-button>
              </div>
            </div>
          </div>
        </div>
        <div style="margin: 30px">
          <div style="margin-bottom: 20px; font-family: 'Calibri'; font-size: 24px">
            Notifications
          </div>
          <el-timeline>
            <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :icon="activity.icon"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :hollow="activity.hollow"
                :timestamp="activity.timestamp"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-main>
    </el-container>
  </el-container>

</template>

<script lang="ts" setup>
import router from '../router'
  const activities = [
    {
      content: '注册页面，实时检测用户名是否重复',
      timestamp: '1小时前',
      type: '',
    },
    {
      content: '新建仓库页面，实时检测仓库名是否重复',
      timestamp: '4小时前',
      color: '',
    },
    {
      content: '创建仓库前后端实现及连接',
      timestamp: '7小时前',
    },
    {
      content: '登录后端实现及前后端连接',
      timestamp: '1天前',
      type: '',
      hollow: true,
    },
    {
      content: '登录前端页面实现',
      timestamp: '2天前',
    },
  ]
  function goto(path){
    router.push({name: path})
  }
</script>

<style scoped>
  .helpList{
    display: flex;
    margin: 20px;
  }
  .listBio{
    width: 70%;
    margin-top: 7px;
  }
  .buttonList{
    width: 150px;
  }
</style>

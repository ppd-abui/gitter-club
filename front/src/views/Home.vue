
<script>
import RepositoryS from '../components/RepositoryS.vue'
import Index from'../components/Index.vue'
// import notification1 from'../components/Notification.vue'
export default {
  name: 'Home',
  components:{
      RepositoryS,
      Index
  }
}
</script>

<template>

  <div class="common-layout">
    <el-container style="height: 100%">
      <el-header style="padding: 0">
        <Index/>
      </el-header >
      <el-container>
<!--        左边框-->
        <el-aside style="width:300px; height:700px" >
          <div style="height:700px; border-right: #d1d1d1 3px solid">
            <div style="font-size:15px;font-weight: bold">
              Recent Repositories
              <el-button style="border-radius: 20px;margin-left:60px;background-color: aquamarine">new</el-button>
            </div>
            <el-input style="border-right: #d1d1d1 3px;margin-left: 20px;width:200px" v-model="input" placeholder="Find a repository" />
            <ul>
              <li style="font-size:18px;margin-left: 40px;margin-top:30px;color: #d1d1d1"><repository></repository></li>

            </ul>
          </div>
        </el-aside>
<!--        右侧主页面-->
        <el-main style="background-color:#f7f7f7;height:800px">
          <div class="rectangle">
           <span style="font-size: x-large;font-weight:bold;margin-left: 30px">Welcome to GitterClub</span>
          </div>
          <div class="right">
            <div style="font-size: x-large;font-weight:bold;margin-left: 30px; position: relative; top: 50px">
              Notification
            </div>

            <notification1></notification1>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style lang='postcss'>
.rectangle {
  width: 600px;
  height: 500px;
  margin-bottom: 5px;
  margin-left: 50px;
  border-color: #d1d1d1;
  border-style: solid;
  border-radius: 15px;
  display: inline-block;
}
.right{
  display: inline-block;
  margin-left: 20px;
}
</style>

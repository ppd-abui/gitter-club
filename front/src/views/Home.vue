
<script>
import Index from "../components/Index.vue";
import RepositoryS from "../components/RepositoryS.vue";
export default {
  name: 'Home',
  components:{
    Index,
    RepositoryS
  }
}
</script>

<template>

  <div class="common-layout">
    <el-container style="height: 100%; display: flex">
      <el-header style="padding: 0"><Index/>
        </el-header >
      <el-container>
        <el-aside style="border-radius: 4px;background-color: azure;width:240px;height:700px" >
          <div style="font-size:15px">
            Recent Repositories
            <el-button style="border-radius: 20px;background-color: aquamarine">new</el-button>
          </div>
          <el-input v-model="input" placeholder="Find a repository" />
          <ul>
            <li style="font-size:18px"><repository></repository></li>
            <li style="font-size:18px"><repository></repository></li>
          </ul>
        </el-aside>
        <el-main style="background-color: aliceblue">

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

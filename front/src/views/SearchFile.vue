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
          <el-menu-item index="1"><el-icon><Edit /></el-icon >Code</el-menu-item>
          <el-menu-item index="2"><el-icon><ChatSquare /></el-icon>Issues</el-menu-item>
          <el-menu-item index="3" ><el-icon><SetUp /></el-icon>Pull requests</el-menu-item>
          <el-menu-item index="4"><el-icon><Setting /></el-icon>Settings</el-menu-item>
        </el-menu></el-header>
        <el-main> <div >
          <RepositoryS style="margin-top:5px;margin-left: 260px"></RepositoryS>
          <el-input style="width:400px;margin-left: 250px;margin-top: 10px" v-model="input" placeholder="Please input" />
        </div>
          <div class="rectangle2" style="font-size: small;display: flex">
            You’ve activated the file finder. Start typing to filter the file list. Use ↑ and ↓ to navigate, enter to view files, esc to exit.
          </div>
          <el-table :data="tableData" style="width: 1000px;margin-left: 250px;border: 2px solid">
            <el-table-column prop="date" label="Date" width="180"  />
            <el-table-column prop="name" label="Name" width="180" />
            <el-table-column prop="address" label="Address" />
          </el-table></el-main>
      </el-container>
    </div>

</template>


<script lang="ts" setup>
import RepositoryS from "../components/RepositoryS.vue";
import index1 from "../components/Index.vue";
const tableData = [
  {
    date: '2016-05-03',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
]
</script>
<style scoped>
.rectangle2{
  width: 1000px;
  height: 50px;
  background-color: lightblue;
  margin-top: 20px;
  margin-left: 250px;
  position: center;
  align-items: center;
}
</style>
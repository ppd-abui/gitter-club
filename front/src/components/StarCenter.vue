
<script lang="ts">
export default {
  name: "StarCenter"
}
</script>

<template>
  <el-container>
    <el-main>
      <el-col :span="20">
        <el-input style="margin-top:12px;margin-left: 5px"  v-model="input" placeholder="Please input" :suffix-icon="Search"></el-input>
      </el-col>
      <el-col :span ="30" v-for = "(item) in tableData" :key = "item.ID" mode = "vertical">
        <div id = "card">
          <el-divider>
            <el-icon><star-filled /></el-icon>
          </el-divider>
          <span>
                My wishes are fools, they shout across thy song, my Master. Let me but listen.
              </span>

        </div>
      </el-col>
    </el-main>
  </el-container>
</template>


<script lang="ts" setup>
import router from '../router/index.js'
import { ref } from 'vue'
import { Search,HomeFilled,Star } from '@element-plus/icons-vue'

const input = ref("")

//数据
const tableData = [
  {
    date: '2016-05-02',
    name: '王小虎',
    address: '上海市普陀区金沙江路 1518 弄'
  },
  {
    date: '2016-05-04',
    name: '王小虎',
    address: '上海市普陀区金沙江路 1517 弄'
  },
  {
    date: '2016-05-01',
    name: '王小虎',
    address: '上海市普陀区金沙江路 1519 弄'
  },
  {
    date: '2016-05-03',
    name: '王小虎',
    address: '上海市普陀区金沙江路 1516 弄'
  }
]

</script>



<style  scoped>

</style>

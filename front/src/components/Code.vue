<template>
  <div style="display: flex">
    <div style="margin-top: 20px;">
      <el-popover v-model:visible="visible" placement="top" :width="160">
        <p>Are you sure to delete this?</p>
        <div style="text-align: right; margin: 0">
          <el-button size="small" text @click="visible = false">cancel</el-button>
          <el-button size="small" type="primary" @click="visible = false"
          >confirm</el-button
          >
        </div>
        <template #reference>
          <el-button @click="visible = true">master</el-button>
        </template>
      </el-popover>
      <el-icon style="margin-left: 20px"><Operation /></el-icon><el-button>Branch</el-button>
      <el-icon style="margin-left: 20px"><Flag /></el-icon><el-button>Tags</el-button>
    </div>
    <div style="display: flex;margin-left:100px;margin-top:25px;justify-content: flex-end">
      <el-button  @click="file">Go to file</el-button>
      <el-popover v-model:visible="visible" placement="top" :width="160">
        <p>Are you sure to delete this?</p>
        <div style="text-align: right; margin: 0">
          <el-button size="small" text @click="visible = false">cancel</el-button>
          <el-button size="small" type="primary" @click="visible = false"
          >confirm</el-button
          >
        </div>
        <template #reference>
          <el-button @click="visible = true">Delete</el-button>
        </template>
      </el-popover>
      <el-button style="margin-left: 20px;">Tags</el-button>
    </div>
  </div>
<!--    <z主页面>-->
    <el-table :data="tableData" stripe style="width: 800px;margin-left: 50px">
      <el-table-column prop="date"  width="180" />
      <el-table-column prop="name" width="180" />
      <el-table-column prop="address"  />
    </el-table>
  <div class="rectangle2" style="font-size: small;display: flex">
      Help people interested in this repository understand your project by adding a README.
       <el-button style="margin-left: 40px">Add a README</el-button>
  </div>
</template>
<script lang="ts">
export default {
  name: "Code",
  methods: {
    file()
        { this.$router.push('/search')}
  }
}
</script>
<script lang="ts" setup>
const visible = ref(false)
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
import { ref } from 'vue'

const value = ref('')

const options = [
  {
    value: 'Option1',
    label: 'Option1',
  },
  {
    value: 'Option2',
    label: 'Option2',
  }
]
</script>

<style scoped>
.fromHeader .el-select .el-input {
  border-color: #409EFF;
  width: 15px;
  margin-left: 20px;
}
.rectangle2{
  width: 800px;
  height: 50px;
  background-color: lightblue;
  margin-top: 50px;
  margin-left: 50px;
  align-items: center;
}
</style>
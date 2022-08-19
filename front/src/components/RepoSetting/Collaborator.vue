<script>
export default {
  name: "Collaborator"
}
</script>

<template>
  <!--大标题-->
  <span style="font-size:30px;font-family: 'Arial'">
    Who has access
  </span>
  <el-divider></el-divider>
  <!--card-->
  <div v-if="visible == 1" >
    <el-row>
      <el-col
          v-for="(item) in datatable1"
          :key="item.ID"
          :span="8"
          :offset="item.ID > 1 ? 2 : 0"
      >
          <el-card style = "width:300px;font-size: 14px" >
            <template #header>
              <div class="card-header"
                style = "display: flex;
                         justify-content: space-between;
                         align-items: center;"
              >
                <span>{{item.title}}</span>
                <div v-if="item.ID == 1">
                  <el-icon><View /></el-icon>
                </div>
                <div v-else>
                  <el-icon><User /></el-icon>
                </div>
              </div>
            </template>
            <span style = "font-size: 10px">
              {{item.main}}
            </span>
          </el-card>
      </el-col>
    </el-row>
  </div>

  <div v-else>
    <el-row>
      <el-col
          v-for="(item) in datatable2"
          :key="item.ID"
          :span="8"
          :offset="item.ID > 1 ? 2 : 0"
      >
        <el-card style = "width:300px;font-size: 14px" >
          <template #header>
            <div class="card-header"
                 style = "display: flex;
                         justify-content: space-between;
                         align-items: center;"
            >
              <span>{{item.title}}</span>
              <div v-if="item.ID == 1">
                <el-icon><Hide /></el-icon>
              </div>
              <div v-else>
                <el-icon><User /></el-icon>
              </div>
            </div>
          </template>
          <span style = "font-size: 10px">
              {{item.main}}
            </span>
        </el-card>
      </el-col>
    </el-row>
  </div>

  <!--管理col-->
  <el-divider></el-divider>
  <span style="font-size:30px;font-family: 'Arial'">
    Manage access
  </span>
  <el-divider></el-divider>
  <!--col表格-->
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="date" label="Date" width="180" />
    <el-table-column prop="name" label="Name" width="180" />
    <el-table-column prop="address" label="Address" />
  </el-table>
</template>

<script setup>
import {ref} from "vue";
const visible = ref("1")

const datatable1 = [
  {
    ID:'1',
    title:"public repository",
    main:"Every can view this repository",
  },
  {
    ID:'2',
    title:"direct access",
    main:"this is a repository",
  },
]
const datatable2 = [
  {
    ID:'1',
    title:"private repository",
    main:"Only those with access to this repository can view it.",
  },
  {
    ID:'2',
    title:"direct access",
    main:"this is a repository",
  },
]

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

</style>

<script lang = "ts">
export default {
  name: "RepositoryCenter"
}
</script>

<template>

  <el-container>
    <el-main>
      <!-- 查询窗口-->
        <el-row style="margin-top:12px;">
          <el-col :span="12" >
            <el-input   v-model="input" placeholder="find a repository" :suffix-icon="Search"></el-input>
          </el-col>
          <el-col :span="5" style="margin-left:10px">
           <el-select v-model="type" placeholder="select type">
             <el-option label="All" value="0" />
             <el-option label="Public" value="1" />
             <el-option label="Private" value="2" />
           </el-select>
          </el-col>
          <el-col :span="4" style="margin-left:10px">
            <el-select v-model="sort_type" placeholder="sort">
              <el-option label="Last updated" value="0" />
              <el-option label="Name" value="1" />
              <el-option label="Stars" value="2" />
            </el-select>
          </el-col>
          <el-col :span="2" style="margin-left:10px">
            <el-button type="success">new</el-button>
          </el-col>
        </el-row>
      <!-- 仓库信息展示-->
      <el-col :span ="30" v-for = "(item) in tableData" :key = "item.ID" mode = "vertical">
        <div id = "card">
          <el-divider>
            <el-icon><star-filled /></el-icon>
          </el-divider>
          <el-link type="success" href="https://element.eleme.io" style="font-family: Calibri;font-size: 35px">{{item.name}}</el-link>
          <el-tag style="margin-left:10px" size = small >{{ item.look }}</el-tag>
          <div style = "float:right;margin-right: 20px">
            <el-button  :icon="item.star==0 ?Star:StarFilled"  @click="testStar(item.star)" > Star</el-button>
          </div>

          <br/>
          <span style="font-size: 20px">
                  {{item.info}}
                  <br/><br/>
                  <div style="font-size: 15px;font-family: Cambria;color:#808080">
                   {{item.time}}
                  </div>
                </span>

        </div>
      </el-col>
    </el-main>
  </el-container>

</template>


<script lang="ts" setup>
import router from '../router/index.js'
import { ref,inject } from 'vue'
import { Search,Star,StarFilled } from '@element-plus/icons-vue'

const type = ref("")
const sort_type = ref("")
const input = ref("")
function testStar(bool){
  if(bool == 1)
    bool = 0;
  else
    bool = 1;
  this.reload();
}

const tableData = [
  {
    ID:'1',
    name:'2',
    look:'public',
    info:'1111111',
    num:'2',
    time:'1111',
    star:'1',
  },
  {
    ID:'2',
    name:'2',
    look:'public',
    info:'1111111',
    num:'2',
    time:'1111',
    star:'0',
  },
  {
    ID:'3',
    name:'2',
    look:'public',
    info:'1111111',
    num:'2',
    time:'1111',
    star:'0',
  },
]

</script>

<style  scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}

</style>

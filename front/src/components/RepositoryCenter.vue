<script lang = "ts">
export default {
  name: "/RepositoryCenter"
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
              <el-divider/>
                <el-link type="primary" href="https://element.eleme.io" style="font-family: Calibri; font-size: 24px">{{item.name}}</el-link>
                <el-button  size = small style="color:black;margin-left: 10px" round>
                  {{ item.look }}
                </el-button>
                <div style = "float:right;margin-right: 20px">
                  <el-button type="warning" :icon="Star" circle  @click="testStar(item.num)" />
                </div>
                <br/>
                <span style="font-size: 20px">
                  {{item.info}}
                  <br/>
                  <br/>
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
import { ref } from 'vue'
import { Search,Star,StarFilled } from '@element-plus/icons-vue'

const input = ref("")
const getValue = (type: string) => {
  const getCssVarValue = (prefix, type) =>
      getComputedStyle(document.documentElement).getPropertyValue(
          `--el-${prefix}-${type}`
      )
  return getCssVarValue('border-radius', type)
}
function testStar(bool){
  if(bool)
    ;
  else
    ;
}

const tableData = [
  {
    ID:'1',
    name:'GitterClub',
    look:'public',
    info:'Git代码服务管理平台',
    num:'2',
    time:'1111',
  },
  {
    ID:'2',
    name:'LibrarySystem',
    look:'public',
    info:'图书管理系统',
    num:'2',
    time:'1111',
  },
  {
    ID:'3',
    name:'Market',
    look:'public',
    info:'商品管理系统',
    num:'2',
    time:'1111',
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

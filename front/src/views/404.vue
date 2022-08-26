<script lang="ts">
export default {
  name: "404.vue"
}
</script>

<template>
  <div style="font-size: 60px;">404 not found</div>

  <el-button @click="readFile()">
    测试
  </el-button>

</template>

<script setup lang="ts">

import request from '../utils/request.js'

function readFile(){
  request.get('/test').then(res=>{
    console.log(res)
    console.log(res.data)
  })
}

</script>
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
<script lang="ts">
export default {
  name: "PullRequest.vue"
}
</script>


<template>
  <div>
    <el-input v-model="diff" style="margin-top: 50px;margin-left: 50px; height: 500px" type="textarea"> </el-input>
  </div>

  <div>
    <el-button style="margin-left: 50px;margin-top: 50px" @click="merge">
      merge
    </el-button>
  </div>

</template>

<script setup lang="ts">
import request from "../utils/request"
import router from "../router";
import {ref} from "vue";
import {ElMessage} from "element-plus";

let diff = ref('')

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')

request.get('/pulls/diff',{
  params:{
    pullRequestId:pathList[3]
  }
}).then(res=>{
  if (res.code === 200 && res.data !=null)
    diff.value = res.data.substr(4)
  else
    diff.value = "Nothing changes!"
})

function merge(){
  if (diff.value === 'Nothing changes!'){
    ElMessage({
      type:'warning',
      message:'Nothing changes! Couldn\'t merge'
    })
  } else {
    request.get('/pulls/merge',{
      params:{
        pullRequestId:pathList[3]
      }
    }).then(res=>{
      if(res.code === 200){
        ElMessage({
          type:'success',
          message:'Merge successfully!'
        })
      } else {
        ElMessage({
          type:'error',
          message:'Automatic merge failed!\nThere may be a conflicts.\nFix conflicts and then commit the result'
        })
      }
    })
  }
}

</script>


<style scoped>

</style>
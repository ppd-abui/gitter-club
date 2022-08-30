<script lang="ts">
export default {
  name: "PullRequest.vue"
}
</script>


<template>
  <div style="width: 100%">
    <div style="margin: 20px auto; width: 60%">
      <div style="font-family: Calibri; font-size: 36px; text-align: center">Confirm to merge</div>
      <div style="border: #e7e7e7 solid 2px; border-radius: 10px; padding: 10px; margin: 20px auto; width: 100%">
          <el-input v-model="diff" autosize style="width: 100%; font-size: 20px" type="textarea"> </el-input>
      </div>
      <div>
        <el-button style="float: right; width: 100px" type="primary" @click="merge">
          merge
        </el-button>
      </div>
    </div>
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
    diff.value = res.data
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
        router.push('/'+pathList[0] + '/'+pathList[1]+ '/pull')
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
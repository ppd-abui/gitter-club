<script>
export default {
  name: "RepositoryS"
}
</script>

<template>
  <div style="width: 200px; height: 40px; display: flex; margin: 5px 5px 5px 5px">
    <img style="width: 30px; height: 30px; margin-top: 5px" src="../assets/black.png"/>
    <el-link style="margin-left: 10px" @click="goto">{{repo.repoName}}</el-link>
  </div>
</template>

<script setup>
import {onMounted, reactive, toRef, toRefs} from "vue";
  import request from "../utils/request";
  import router from "../router";

  console.log('new repoS')
  const props =defineProps({
    repo: Object
  })
  const {repo} = toRefs(props)
  console.log('repo', repo.value)

  function goto(){
    router.push({path: '/'+repo.value.repoOwner+'/'+repo.value.repoName})
  }


</script>

<style scoped>

</style>
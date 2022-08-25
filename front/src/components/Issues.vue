<script>
export default {
  name: "Issues"
}
</script>

<template>
  <div style="width: 100%">
    <router-view/>
  </div>
</template>


<script setup>

import router from "../router";
import {onMounted} from "vue";


  onMounted(()=>{
    let path = router.currentRoute.value.fullPath
    let pathList = path.substr(1).split('/')

    router.push({path: '/'+pathList[0]+'/'+pathList[1]+'/issues/list'})
  })
</script>
<style scoped>

</style>
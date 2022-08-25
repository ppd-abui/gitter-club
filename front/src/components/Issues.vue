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
    let regexp = /(\w)+/g
    path = path.match(regexp)
    router.push({path: '/'+path[0]+'/'+path[1]+'/issues/list'})
  })
</script>
<style scoped>

</style>
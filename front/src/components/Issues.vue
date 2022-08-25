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
    router.push({path: path+'/list'})
  })
</script>
<style scoped>

</style>
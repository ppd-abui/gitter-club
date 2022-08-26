<script>
export default {
  name: "IssueContent"
}
</script >
<template>
  <el-container>
    <el-main><div style="font-size: 20px;margin-top: 10px;margin-left: 15%">
      111
    </div>
      <div>
        <el-button style="margin-top: 10px;margin-left: 15%;border-radius: 15px;" >Open</el-button>
        <span style="font-size: small;margin-left: 5px;">1111111</span>
        <el-divider style="width:70% ;margin-left: 15%"/>
      </div>
      <div class="rectangle">

      </div>
      <div class="rectangle2">
        <v-md-editor
            v-model="text"
            height="200px">
        </v-md-editor>
      </div></el-main>
    <el-aside width="30%">
        <div style="margin-top: 50px;margin-top:30%">
          <span style="font-size: small">Assignees</span>
        </div>
    </el-aside>
  </el-container>


</template>

<script  setup>
import router from "../../router";
let path = router.currentRoute.value.fullPath;

</script>

<style scoped>
.rectangle{
  height: 100px;
  width: 800px;
  border-radius: 10px ;
  border: solid #f7f7f7;
  margin-left: 15%;
}
.rectangle2{
  height: 200px;
  width:800px;
  margin-left: 15%;
  border-radius: 10px;
  border: solid #f7f7f7;
}
</style>
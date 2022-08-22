<template>
  <div class="rectangle" style="text-align: center;margin-top: 20px">
    <div style="margin-top: 20px">
    Label issues and pull requests for new contributors
    </div>
  </div>
  <div style="margin-left: 100px;margin-top: 20px;">
    <el-popconfirm
        confirm-button-text="OK"
        cancel-button-text="No, Thanks"
        :icon="InfoFilled"
        icon-color="#626AEF"
        title="Are you sure to delete this?"
    >
      <template #reference>
        <el-button style="border-radius: 10px;background-color: #f7f7f7">Filters</el-button>
      </template>
    </el-popconfirm>
    <el-input style="width: 600px;" v-model="input"  placeholder="Please input" />
    <el-button style="margin-left: 20px;border-radius: 10px">Labels</el-button>
    <el-button style="border-radius: 10px">Milestones</el-button>
    <el-button style="background-color: aquamarine;border-radius: 10px">New issue</el-button>
  </div>
  <div class="rectangle2">
      <el-icon style="vertical-align: -10%;margin-left: 10px"><Aim /></el-icon>
    <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Open</el-button>
    <el-icon style="vertical-align: -10%;margin-left: 10px"><Check /></el-icon>
    <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>
    <el-button style="margin-left:450px;background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>
    <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>
    <el-button style="background-color: #f7f7f7;border-color: #f7f7f7;">Closed</el-button>
  </div>
  <el-table :data="tableData" style="width: 900px;border: 1px solid;border-radius:10px;border-color: #d1d1d1;border-right-color: #d1d1d1;margin:auto">
    <el-table-column prop="date" label="Date" width="180" />
    <el-table-column prop="name" label="Name" width="180" />
    <el-table-column prop="address" label="Address" />
  </el-table>
</template>

<script>
export default {
  name: "Issues"
}
</script>

<style scoped>
.rectangle{
  height: 80px;
  width: 900px;
  margin: auto;
  border: 1px solid #d1d1d1;
  border-radius: 10px;
}
.rectangle2{
 height: 30px;
  width: 900px;
  border:1px solid #d1d1d1 ;
  background-color: #f7f7f7;
  border-radius: 10px;
  margin: 10px auto auto;
}
</style>
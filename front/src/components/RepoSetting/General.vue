<script lang = "ts">
export default {
  name: "General",
  components: {},
}

</script>

<template>
  <!--大标题-->
  <span style="font-size:30px;font-family: 'Arial'">
    General
  </span>

  <el-divider />
  <!--表单展示-->
  <span style = "font-size: 25px">
    Basic
    <br/> <br/>
  </span>
  <el-form
      label-width="160px"
      >
    <el-form-item label = "Repository Name">
      <el-input v-model="input_name" placeholder="Repository Name" />
    </el-form-item>
    <el-form-item label = "Repository Visibility">
      <el-select v-model="visibility" placeholder="Repository Visibility">
        <el-option label="Public" value="1" />
        <el-option label="Private" value="0" />
      </el-select>
    </el-form-item>
    <el-form-item label="Activity form">
      <el-input v-model="profile" type="textarea" />
    </el-form-item>
  </el-form>
  <el-divider />
  <!--photo-->
  <span style = "font-size: 25px">
    Social view
    <br/> <br/>
  </span>
  <el-alert title="warning alert" type="warning" show-icon />
  <el-form-item label="上传图片" required>
    <el-upload
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
    >
      <img
          v-if="imageUrl"
          :src="imageUrl"
          />
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      <p style="color: #666; margin-top: 6px; margin-bottom: 12px">
        点击添加
      </p>
    </el-upload>
  </el-form-item>

  <el-divider></el-divider>

</template>

<script lang = "ts" setup>
import {ref} from "vue";

const input_name = ref('')
const visibility = ref('1')
const profile= ref("")
const imageUrl = ref("")
const fileData= {   // 接口需要的额外参数
  category: 12
}
const headers= {  // 请求头部参数
  accessToken: ''
}

 function handleAvatarSuccess(res, file) {
  if (res.msgCode === 200) {
    this.imageUrl = URL.createObjectURL(file.raw)
  } else {
    this.$message.error(res.msgContent)
  }
}
// 图片上传前的判断
function beforeAvatarUpload(file) {
  const isLt1M = file.size / 1024 / 1024
  if (isLt1M > 1) {
    this.$message.error('上传头像图片大小不能超过1MB')
  }
  return isLt1M
}
</script>

<style scoped>

</style>

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
<!--Basic-->
  <span style = "font-size: 22px">
    Basic
    <br/> <br/>
  </span>

  <el-form
      label-width="160px"
  >
    <el-form-item label = "Repository Name">
      <el-col :span="8">
        <el-input v-model="input_name" placeholder="Repository Name" />
      </el-col>
      <el-col :span="5" style="margin-left: 20px">
        <el-button @click="handleClick">Rename</el-button>
      </el-col>
    </el-form-item>
    <el-form-item label="Activity form">
      <el-input v-model="profile" type="textarea" />
    </el-form-item>
  </el-form>
  <el-divider />
<!--Social view-->
  <span style = "font-size: 22px">
    Social view
    <br/><br/>
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
<!--Danger Zone-->
  <span style = "font-size: 22px">
    Danger Zone
    <br/> <br/>
  </span>

  <div class="rectangle" style="margin-top: 30px;position: relative">
<!--Repository Visibility-->
    <div style="margin-left: 20px;margin-top: 20px">
      <span style="font-size: 20px">
        Repository Visibility
      </span>
      <el-button style="margin-left: 20px;position: absolute;right: 10px" @click="visiVisible = true">
        Change Visibility
      </el-button>
      <el-dialog
          v-model= "visiVisible"
          title="Change repository visibility"
          width="30%"
          :before-close="handleClose"
      >
        <el-alert  title=" Warning: this is a potentially destructive action." type="warning" show-icon />
        <br/>
        <span>Choose the visibility:</span>
        <el-select v-model="visibility" style="margin-left: 20px">
          <el-option  :value="1" :label="Public">Public</el-option>
          <el-option  :value="0" :label="Private">Private</el-option>
        </el-select>
        <el-divider></el-divider>
        <span style="font-size: 18px;font-family: Arial">Please type ck837/wwwqqq to confirm.</span>
        <el-input style="margin-top: 20px"></el-input>
        <el-button style="margin-top: 10px;height: 32px;width: 430px" >I understand,change repository visibility</el-button>
      </el-dialog>

      <br/><br/>

      <span style="font-size: 15px">
        Change the visibility of the repository
      </span>

<!--      <el-select v-model="visibility" placeholder="Repository Visibility" style="margin-left: 20px;position: absolute;right: 10px">-->
<!--        <el-option label="Public" value="1" @click="handleClickPublic"/>-->
<!--        <el-option label="Private" value="0" @click="handleClickPrivate"/>-->
<!--      </el-select>-->
    </div>
    <el-divider></el-divider>
<!--Transfer Visibility-->
    <div style="margin-left: 20px;margin-top: 20px">
      <span style="font-size: 20px">
        Transfer ownership
      </span>
      <el-button style="margin-left: 20px;position: absolute;right: 10px" @click="repoVisible = true">
        Transfer
      </el-button>
      <el-dialog
          v-model= "repoVisible"
          title="Transfer repository"
          width="30%"
          :before-close="handleClose"
      >
        <el-alert  title=" Warning: this is a potentially destructive action." type="warning" show-icon />
        <br/>
        <span style="font-size: 15px">Transferring may be delayed until the new owner approves the transfer.</span>
        <br/><br/>
        <span style="font-size: 15px; ">New owner's GitHub username or organization name.</span>

        <el-input style="margin-top: 20px;"></el-input>

        <el-divider></el-divider>
        <span style="font-size: 18px;font-">Please type ck837/wwwqqq to confirm.</span>
        <el-input style="margin-top: 20px"></el-input>
        <el-button style="margin-top: 10px;height: 32px;width: 430px" >I understand,change repository visibility</el-button>
      </el-dialog>
      <br/><br/>
      <span style="font-size: 15px">
       Transfer this repository to another user or to an organization where you have the ability to create repositories.
      </span>
    </div>
    <el-divider/>
<!-- Delete Repository -->
    <div style="margin-left: 20px;margin-top: 20px">
      <span style="font-size: 20px">
        Delete this repository
      </span>
      <el-button style="margin-left: 20px;position: absolute;right: 10px" @click="delVisible = true">
        Transfer
      </el-button>
      <el-dialog
          v-model= "delVisible"
          title="Are you absolutely sure?"
          width="30%"
          :before-close="handleClose"
      >
        <el-alert  title=" Unexpected bad things will happen if you don’t read this!" type="warning" show-icon />
        <br/>
        <span style="font-size: 15px">This action cannot be undone. This will permanently delete the ck837/wwwqqq repository, wiki, issues, comments, packages, secrets, workflow runs, and remove all collaborator associations.</span>
        <br/>
        <span style="font-size: 15px">This will not change your billing plan. If you want to downgrade, you can do so in your Billing Settings.</span>
        <br/><br/>
        <span style="font-size: 18px;font-">Please type ck837/wwwqqq to confirm.</span>
        <el-input style="margin-top: 20px"></el-input>
        <el-button style="margin-top: 10px;height: 32px;width: 430px" >I understand the consequences,delete this repository visibility</el-button>
      </el-dialog>
      <br/><br/>
      <span style="font-size: 15px">
        Once you delete a repository, there is no going back. Please be certain.
      </span>
    </div>
  </div>
</template>

<script lang = "ts" setup>
import {ref} from "vue";
import { ElMessage, ElMessageBox } from 'element-plus'

let visiVisible = ref(false)
let repoVisible = ref(false)
let delVisible = ref(false)

let Public= ref("Public")
let Private = ref("Private")
let dialogVisible = ref(false)
const input_name = ref('')
let visibility = ref('1')
const profile= ref("")
const imageUrl = ref("")


const handleClose = (done: () => void) => {
  ElMessageBox.confirm('Are you sure to close this dialog?')
      .then(() => {
        done()
      })
      .catch(() => {
        // catch error
      })
}
const data=[
  {
    name: 'Tom',
  },
  {
    name: 'Tom',
  },
]

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


const  handleClick = () => {
  ElMessageBox.confirm(
      'Do you want to change the name?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        ElMessage({
          type: 'success',
          message: 'Rename completed',
          visibility : '0',
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

function handleClickPublic(){
  ElMessageBox.confirm(
      'Do you want to change the name?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        ElMessage({
          type: 'success',
          message: 'Rename completed',
          visibility : '1',
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

function handleClickPrivate(){
  ElMessageBox.confirm(
      'Do you want to change the name?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        ElMessage({
          type: 'success',
          message: 'Rename completed',
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

</script>

<style scoped>
.rectangle{
  height: 380px;
  width: 1150px;
  margin-left: 15px;
  margin-top: 20px;
  border-radius: 10px;
  border: 1px solid #b1b1b1;
}
</style>

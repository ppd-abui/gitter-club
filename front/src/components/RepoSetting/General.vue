<template>
  <!--大标题-->
  <span style="font-size:30px;margin-left: 10px;font-family: 'Arial'">
    General
  </span>
  <el-divider />
<!--Basic-->
  <span style = "font-size: 22px;margin-left: 10px">
    Basic
    <br/> <br/>
  </span>
  <el-form label-width="10px">
    <el-form-item><div> Repository Name </div> <br></el-form-item>

    <el-form-item style="margin-top: -18px;display: flex">
        <el-input style="width: 15%" v-model=localRepo.localRepoName placeholder="Repository Name" />
        <el-button style="margin-left: 0.5%" @click="handleChangeRepoName">Rename</el-button>
    </el-form-item>

    <el-form-item><div> Repository Introduction </div> <br></el-form-item>

    <el-form-item  style="margin-top: -18px">
      <el-input style="width: 80%" v-model="localRepo.localRepoBio" type="textarea" :rows="4" />
    </el-form-item>

    <el-form-item>
      <el-button @click="handleChangeRepoBio">Change the Repository Introduction</el-button>
    </el-form-item>

  </el-form>
  <el-divider />
<!--Social view-->
  <span style = "font-size: 22px;margin-left: 10px">
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
  <span style = "font-size: 22px;margin-left: 10px ;color: #cf222e">
    Danger Zone
  </span>

  <div class="rectangle" style="margin-top: 30px;position: relative;width: 80%; height: 350px">
<!--Repository Visibility-->
    <div style="margin-left: 20px;margin-top: 20px">
      <span style="font-size: 20px;font-weight: bold">
        Repository Visibility
      </span>
      <el-button color="#f6f8fa"
                 style="margin-top: 15px; margin-right: 5%; position: absolute;right: 10px;color:#d6442e;border: 1px #dddddd solid"
                 @click="visiVisible = true">
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
        <el-select v-model="localRepo.localRepoVisibility" style="margin-left: 20px">
          <el-option :key=repoPublic  :value=repoPublic :label=repoPublic>Public</el-option>
          <el-option :key=repoPrivate :value=repoPrivate :label=repoPrivate>Private</el-option>
        </el-select>
        <el-divider></el-divider>
        <span style="font-size: 18px;font-family: Arial">Please type {{pathList[0]}}/{{ pathList[1] }} to confirm.</span>
        <el-input style="margin-top: 20px" v-model=queryInput > </el-input>
        <el-button  style="width: 100%;margin-top: 15px" @click="handleChangeVisibility">I understand,change repository visibility</el-button>
      </el-dialog>

      <br> <br>
      <span style="font-size: 15px">
        Change the visibility of the repository
      </span>

<!--      <el-select v-model="visibility" placeholder="Repository Visibility" style="margin-left: 20px;position: absolute;right: 10px">-->
<!--        <el-option label="Public" value="1" @click="handleClickPublic"/>-->
<!--        <el-option label="Private" value="0" @click="handleClickPrivate"/>-->
<!--      </el-select>-->

    </div>
    <el-divider></el-divider>
<!--Transfer Ownership-->
    <div style="margin-left: 20px;margin-top: 20px">
      <span style="font-size: 20px;font-weight: bold">
        Transfer ownership
      </span>

      <el-button color="#f6f8fa"
                 style="margin-top: 15px; margin-right: 5%; position: absolute;right: 10px;color:#d6442e;border: 1px #dddddd solid"
                 @click="repoVisible = true">
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

        <el-input style="margin-top: 20px;" v-model="localRepo.localRepoTransName"></el-input>

        <el-divider></el-divider>
        <span style="font-size: 18px;font-family: Arial">Please type {{pathList[0]}}/{{ pathList[1] }} to confirm.</span>
        <el-input v-model="queryInput" style="margin-top: 20px"></el-input>
        <el-button style="width: 100%;margin-top: 15px" @click="handleTransRepo">I understand,change repository visibility</el-button>
      </el-dialog>
      <br/><br/>
      <span style="font-size: 15px">
       Transfer this repository to another user or to an organization where you have the ability to create repositories.
      </span>
    </div>
    <el-divider/>
<!-- Delete Repository -->
    <div style="margin-left: 20px;margin-top: 20px">
      <span style="font-size: 20px;font-weight: bold">
        Delete this repository
      </span>
      <el-button color="#f6f8fa"
                 style="margin-top: 15px; margin-right: 5%; position: absolute;right: 10px;color:#d6442e;border: 1px #dddddd solid"
                 @click="delVisible = true">
        Delete
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
        <span style="font-size: 18px;font-family: Arial">Please type {{pathList[0]}}/{{ pathList[1] }} to confirm.</span>
        <el-input style="margin-top: 20px" v-model="queryInput"></el-input>
        <el-button style="width: 100%;margin-top: 15px" @click="handleDeleteRepo">I understand consequences,delete this repository visibility</el-button>
      </el-dialog>
      <br/><br/>
      <span style="font-size: 15px">
        Once you delete a repository, there is no going back. Please be certain.
      </span>
    </div>
  </div>
</template>

<script lang = "ts">

export default {
  name: "General"
}


</script>

<script lang = "ts" setup>
import {onBeforeMount, reactive, ref} from "vue";
import request from "../../utils/request.js"
import { ElMessage, ElMessageBox } from 'element-plus'
import router from "../../router/index.js"

let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')

//弹出框可见性确认函数
let visiVisible = ref(false)
let repoVisible = ref(false)
let delVisible = ref(false)

//本地暂时存储的修改后信
let localRepo = reactive({
  localRepoName:"",
  localRepoBio: "",
  localRepoVisibility :"",
  localRepoTransName:""
})

//danger zone 确认输入框的值

let queryInput=ref("")

let repoPublic= ref("public")
let repoPrivate = ref("private")
const imageUrl = ref("")


let setting =ref({
})

//界面初始化获取数据
onBeforeMount(()=>{
  set();
})

function set(){
  request.get('/repo/name', {
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1]
    }
      }
  ).then(res=>{
        console.log("11111111");
        setting.value = res.data;
        console.log(res);
        localRepo.localRepoName = setting.value.repoName;
        localRepo.localRepoBio = setting.value.repoBio;
        localRepo.localRepoVisibility = setting.value.repoVisibility;
      }
  )
}

//确认并发送修改仓库名请求

const  handleChangeRepoName = () => {
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
          changeRepoName()
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}
//change repo name
function changeRepoName(){
  request.get('/repo/change/name', {
        params:{
          repoOwner:pathList[0],
          repoName:pathList[1],
          repoNameNew: localRepo.localRepoName,
        }
      }
  ).then(res=>{
        console.log("hello")
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the repository name successfully!',
            type: 'success',
          })
          let repoUrl = '/#/'+pathList[0]+'/'+localRepo.localRepoName+'/settings/general'
          router.push(repoUrl)
        }
        if(res.code == 500) {
          ElMessage.error('Oops, the change meets a mistake...')
        }
      }
  )
}

//确认并发送修改仓库简介请求

const  handleChangeRepoBio = () => {
  ElMessageBox.confirm(
      'Do you want to change the introduction?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        changeRepoBio()
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Modify repository bio canceled',
        })
      })
}
//change repo Bio
function changeRepoBio(){
  request.get('/repo/change/bio', {
        params:{
          repoOwner:pathList[0],
          repoName:pathList[1],
          repoBioNew: localRepo.localRepoBio,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the repository introduction successfully!',
            type: 'success',
          })
          router.go(0);
        }
        if(res.code == 500) {
          ElMessage.error('Oops, the change meets a mistake...')
        }
      }
  )
}

//处理关闭窗口

const handleClose = (done: () => void) => {
  ElMessageBox.confirm('Are you sure to close this dialog?')
      .then(() => {
        done()
      })
      .catch(() => {
        // catch error
      })
}

//处理修改仓库可见性

const  handleChangeVisibility = () => {
  ElMessageBox.confirm(
      'Do you want to change the visibility?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        let temp1 = ref(pathList[0]+"/"+pathList[1]);
        let a = (temp1.value != queryInput.value);
        console.log("llllllllllll")
        console.log(localRepo.localRepoVisibility)

        if(temp1.value != queryInput.value) {
          ElMessage({
            type: 'info',
            message: 'The qualification is wrong',
          })
        }else {changeRepoVisibility();}
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

//change repo Visibility
function changeRepoVisibility(){
  request.get('/repo/change/visibility', {
        params:{
          repoOwner:pathList[0],
          repoName:pathList[1],
          repoVisibilityNew: localRepo.localRepoVisibility,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the repository visibility successfully!',
            type: 'success',
          })
          router.go(0);
        }
        if(res.code == 500)
          ElMessage.error('Oops, the change meets a mistake...')
      }
  )
}

//转让仓库
const  handleTransRepo = () => {
  ElMessageBox.confirm(
      'Do you want to change the owner?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        let temp1 = ref(pathList[0]+"/"+pathList[1]);
        let a = (temp1.value != queryInput.value);
        console.log("llllllllllll")
        console.log(localRepo.localRepoTransName)

        checkUser()

        console.log(check.value)

        if(temp1.value != queryInput.value) {
            ElMessage({
              type: 'info',
              message: 'The qualification is wrong',
            })
          }
        else if(check.value == false){
          ElMessage({
            type: 'info',
            message: 'The name is undefined',
          })
        }
        else {transRepo();}
        check.value = false;
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

let check = ref(false)
function checkUser(){
  request.get('/register/account',{
    params:{
      userAccount:localRepo.localRepoTransName
    }
  })
  .then(res=>{
    if(res.code == 500) {
      check.value = true;
    }
    console.log(res)
  })
}

//change repo Visibility
function transRepo(){
  request.get('/repo/change/Owner', {
        params:{
          repoOwner:pathList[0],
          repoName:pathList[1],
          repoOwnerNew: localRepo.localRepoTransName,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Trans the repository  successfully!',
            type: 'success',
          })
          router.go(0);
        }
        if(res.code == 500)
          ElMessage.error('Oops, the change meets a mistake...')
      }
  )
}

//删除仓库

const  handleDeleteRepo = () => {
  ElMessageBox.confirm(
      'Do you want to delete the repo?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        let temp1 = ref(pathList[0]+"/"+pathList[1]);
        console.log("llllllllllll")
        console.log(localRepo.localRepoTransName)

        if(temp1.value != queryInput.value) {
          ElMessage({
            type: 'info',
            message: 'The qualification is wrong',
          })
        }
        else {deleteRepo();}
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

//delete repo
function deleteRepo(){
  request.get('/repo/delete', {
        params:{
          repoOwner:pathList[0],
          repoName:pathList[1]
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Delete the repository successfully!',
            type: 'success',
          })
          router.go(0);
        }
        if(res.code == 500)
          ElMessage.error('Oops, the delete meets a mistake...')
      }
  )
}


//图片
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
.rectangle{
  height: 380px;
  width: 1150px;
  margin-left: 15px;
  margin-top: 20px;
  border-radius: 10px;
  border: 1px solid #b1b1b1;
}
</style>

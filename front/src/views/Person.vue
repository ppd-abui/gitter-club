<script lang = "ts">
import Index from '../components/Index.vue'
import RepositoryCenter from "../components/RepositoryCenter.vue";
import StarCenter from "../components/StarCenter.vue";
export default {
  name: "Person",
  components: {
    Index,
    RepositoryCenter,
    StarCenter
  }
}
</script>

<template>
  <el-container>
<!--Index-->
    <el-header style="padding: 0"> <Index/> </el-header>
    <el-main style="padding: 0">
      <el-container style="display:flex;">
        <el-header style="padding: 0; z-index: 1">
<!--Menu（仓库/关注）-->
          <el-menu
              :default-active="'1'"
              mode="horizontal"
              style="height: 80px"
          >
            <el-menu-item style="margin-left: 370px" index="1" @click="go('RepositoryCenter')"><el-icon><HomeFilled /></el-icon> Repository</el-menu-item>
            <el-menu-item index="2" @click="go('StarCenter')"> <el-icon><Star /></el-icon> Star</el-menu-item>
          </el-menu>
        </el-header>
        <el-container style="z-index: 2">
  <!--个人信息-->
          <el-aside style="width: 400px; position: relative;  bottom: 60px; z-index: 1">
            <div style="position: relative; width: 100%; margin: 0">
              <img style="width: 100%; object-fit: fill;" src="../assets/black.png">
            </div>
            <div style="width: 350px; margin-left: 50px; position: relative; bottom: 30px">
              <div style="font-family: 'Calibri Light'; font-size: 20px; color: grey">{{pathList[0]}}</div>
              <el-button style="width: 80%; margin-top: 10px" @click="handleDrawer">Edit Profile</el-button>
            </div>
          </el-aside>
    <!--子界面路由-->
          <el-main style="padding: 0; z-index: 2; position: relative; right: 40px">
            <RepositoryCenter v-show="tab==='RepositoryCenter'"/>
            <StarCenter v-show="tab==='StarCenter'"/>
          </el-main>
        </el-container>
      </el-container>
    </el-main>
    <!--抽屉-->
    <el-drawer
        v-model="drawer"
        title="Personal Information"
        :direction=direction
        :before-close="handleClose"
        size="50%"
    >
      <el-form
          label-width="200px"
      >
        <el-form-item label = "User Name">
          <el-col :span="9">
            <el-input v-model="user.name" placeholder="User Name" />
          </el-col>
          <el-col :span="5" style="margin-left: 20px">
            <el-button @click="handleChangeUserName">Rename</el-button>
          </el-col>
        </el-form-item>

        <el-form-item label = "User Account">
          <el-col :span="9">
            <el-input v-model="user.account" placeholder="User Account" />
          </el-col>
          <el-col :span="5" style="margin-left: 20px">
            <el-button @click="handleChangeUserAccount">Change Account</el-button>
          </el-col>
        </el-form-item>

        <el-form-item label = "User Password">
          <el-col :span="9">
            <el-input v-model="user.password" autocomplete="off" show-password/>
          </el-col>
          <el-col :span="5" style="margin-left: 20px">
            <el-button  @click="handleChangeUserPassword">Change password</el-button>
          </el-col>
        </el-form-item>

        <el-form-item label = "User Birthday">
          <el-col :span="9">
            <el-input v-model="user.birthday" placeholder="User Birthday" />
          </el-col>
          <el-col :span="5" style="margin-left: 20px">
            <el-button>Change birthday</el-button>
          </el-col>
        </el-form-item>

        <el-form-item label = "User Sex">
          <el-col :span="9">
            <el-input v-model="user.sex" placeholder="User Sex" />
          </el-col>
          <el-col :span="5" style="margin-left: 20px">
            <el-button @click="handleChangeUserSex">Change sex</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-drawer>
  </el-container>
<!--  <div id = "hello">-->
<!--    <router-view/>-->
<!--  </div>-->
</template>

<script lang = "ts" setup>
import router from "../router/index.js";
import {onBeforeMount, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from '../utils/request.js';


let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('?')


let direction = ref('ltr')
let drawer = ref(false)
let tab = ref('RepositoryCenter')

let user=reactive({
  name:"",
  account:"",
  password:"",
  birthday:"",
  sex:"",
})



const  handleDrawer = () => {
  if (localStorage.getItem("userAccount") !== pathList[0])
    ElMessage({
      type:'warning',
      message:"You don't have the power"
    })
  else
  {
    drawer.value = true;
  }
}


function go(path){
  router.push({path: '/'+pathList[0], query: {tab: path}})
  tab.value=path
}

const handleClose = (done: () => void) => {
  ElMessageBox.confirm('Are you sure you want to close this?')
      .then(() => {
        done()
      })
      .catch(() => {
        // catch error
      })
}

onBeforeMount(()=>{
  begin();
})

function begin(){
  request.get('/register/account', {
        params:{
          userAccount:pathList[0],
        }
      }
  ).then(res=>{
        user.name = res.data.userName;
        user.account = res.data.userAccount;
        user.password = res.data.userPassword;
        user.birthday = res.data.userBirthday;
        user.sex = res.data.userSex;
      }
  )
}


//修改name
const  handleChangeUserName = () => {
  if (localStorage.getItem("userAccount") !== pathList[0])
  ElMessage({
    type:'warning',
    message:"You don't have the power"
  })
  else
  {
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
          changeUserName()
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: 'Change canceled',
          })
        })
  }
}
//change userName
function changeUserName(){
  request.get('/user/change/name', {
        params:{
          userAccount:pathList[0],
          userNameNew:user.name,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the user name successfully!',
            type: 'success',
          })
          router.go(0)
        }
        if(res.code == 500) {
          ElMessage.error('Oops, the change meets a mistake...')
        }
      }
  )
}
//修改account

const  handleChangeUserAccount = () => {
  ElMessageBox.confirm(
      'Do you want to change the account?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        changeUserAccount()
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Change canceled',
        })
      })
}
//change userAccount
function changeUserAccount(){
  request.get('/user/change/account', {
        params:{
          userAccount:pathList[0],
          userAccountNew:user.account,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the user account successfully!',
            type: 'success',
          })
          let repoUrl = '/#/'+pathList[0]
          router.push(repoUrl)
        }
        if(res.code == 500) {
          ElMessage.error('Oops, the change meets a mistake...')
        }
      }
  )
}
//修改password
const  handleChangeUserPassword = () => {
  ElMessageBox.confirm(
      'Do you want to change the password?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        changeUserPassword()
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Change canceled',
        })
      })
}
//change userName
function changeUserPassword(){
  request.get('/user/change/password', {
        params:{
          userAccount:pathList[0],
          userPasswordNew:user.password,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the user password successfully!',
            type: 'success',
          })
          router.go(0)
        }
        if(res.code == 500) {
          ElMessage.error('Oops, the change meets a mistake...')
        }
      }
  )
}


//修改birthday

//修改sex
const  handleChangeUserSex = () => {
  ElMessageBox.confirm(
      'Do you want to change the sex?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        changeUserSex()
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}
//change usersex
function changeUserSex(){
  request.get('/user/change/sex', {
        params:{
          userAccount:pathList[0],
          userSexNew:user.sex,
        }
      }
  ).then(res=>{
        if(res.code == 200) {
          ElMessage({
            message: 'Ohhhhh,Change the repository name successfully!',
            type: 'success',
          })
          router.go(0)
        }
        if(res.code == 500) {
          ElMessage.error('Oops, the change meets a mistake...')
        }
      }
  )
}
</script>

<style scoped>

</style>

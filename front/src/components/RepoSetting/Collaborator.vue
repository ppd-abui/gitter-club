<script>
export default {
  name: "Collaborator"
}
</script>

<template>
  <!--管理col-->

  <div>
    <span style="font-size:30px;font-family: 'Arial';position: relative">
      Manage access
    </span>
    <el-button style="position:absolute;right:20px" type="success" @click="addVisible = true" > add new collaborator</el-button>
  </div>

  <el-divider></el-divider>
  <!--col表格-->

  <!--为空时的信息-->
  <div v-if="collaboratorInfo[0] === nullString ">
    <div style="font-size: 30px">
      <el-icon ><Remove /></el-icon>
      <span style="margin-left: 20px;font-size: 50px;margin-top: 20px">
      There is no collaborator in this repository!{{length}}
      </span>
    </div>
  </div>

  <!--展示信息-->
  <div v-else>
    <el-table :data="collaboratorInfo" style="width: 100%; border-radius: 10px;">
      <el-table-column prop="" label="">
        <template #default="scope">
          <div style="height: 40px; width: 100%; display: flex">
            <div style="width: 20px; height: 20px; position: relative; top: 3px;">
              <el-icon size="20px"><Avatar/></el-icon>
            </div>
            <div style="position: relative; bottom: 6px; left: 10px; display: flex" >
              <el-link @click="goUser(scope.$index)" type="primary" style="font-size: 20px">{{collaboratorInfo[scope.$index]}}</el-link>
            </div>
            <div style="position:absolute; bottom: 6px; right: 20px; display: flex" >
              <el-button  type="warning" @click="deleteCollaborator(collaboratorInfo[scope.$index])">delete</el-button>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog
      v-model= "addVisible"
      title="Add new collaborator"
      width="30%"
  >
    <span style="font-size: 18px;font-family: Arial">Please enter the collaborator account</span>
    <el-input style="margin-top: 20px" v-model="inputValue"></el-input>

    <el-button style="margin-top: 20px;width: 100%" type="success" @click="handleAddCollaborator" >I understand,just do it!</el-button>
  </el-dialog>

</template>

<script setup>
import router from '../../router/index.js';
import request from "../../utils/request";
import {ref, onBeforeMount, reactive} from 'vue'
import {ElMessageBox, ElMessage} from "element-plus";

//输入框输入
let inputValue = ref("")

//private,public show
let visible = ref("0")

//add new collaborator
let addVisible = ref(false)

let nullString = ""
let resultMessage = reactive("")


let path = router.currentRoute.value.fullPath
let pathList = path.substr(1).split('/')


let repo = reactive({
  visibility:"",
})
//col数组
let collaboratorInfo = ref([]);

//获取仓库信息
onBeforeMount(()=>{
  getVisibility();
  getCollaborators();
  setWindow();
})
function setWindow(){
  if(repo.visibility === "public")
    visible.value = "1";
  else
    visible.value = "0";
}
function getVisibility(){
  request.get('/repo/name',{
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1],
    }
  }).then(res=>{
        console.log("1111")
        if(res.code === 200) {
          repo.visibility = res.data.repoVisibility;
          resultMessage = res.msg;
        }
     }
  )
}
function getCollaborators(){
  request.get('/repo/col',{
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1],
    }
  }).then(res=>{
    if(res.status)
        console.log(res);
        collaboratorInfo.value = res.data
        console.log(length)
      }
  )
}

function goUser(index){
  router.push({path: '/'+collaboratorInfo.value[index]})
}

//----------------------------------------------------------------------


// 增加col
const  handleAddCollaborator = () => {
  ElMessageBox.confirm(
      'Do you want to add the user?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        checkUser()
        console.log(check.value)
        if(check.value === false) {
          ElMessage({
            type: 'info',
            message: 'The user is undefined',
          })
        }
        else {addCol();}
        check.value = false;
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Rename canceled',
        })
      })
}

function addCol(){
 request.get('/repo/col/add',{
   params:{
     repoOwner:pathList[0],
     repoName:pathList[1],
     newCol:inputValue.value,
   }
 }).then(res=>{
   if(res.code == 200) {
     ElMessage({
       message: res.msg,
       type: 'success',
     })
     router.go(0);
   }
   if(res.code == 500){
     ElMessage.error(res.msg)
   }
 })
}

let check = ref()
//检查用户存在
function checkUser(){
  request.get('/register/account',{
    params:{
      userAccount:inputValue.value,
    }
  })
      .then(res=>{
        if(res.code === 500) {
          check.value = true;
        }
        console.log(res)
        console.log(check.value)
        console.log("check it")
      })
}

//----------------------------------------------------------------------------------------------------------//


//删除用户
const  deleteCollaborator = (name) => {
  ElMessageBox.confirm(
      'Do you want to delete the collaborator?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(() => {
        deleteCollaboratorName(name);
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'delete canceled',
        })
      })
}
function  deleteCollaboratorName(deleteName){
  request.get('/repo/col/delete',{
    params:{
      repoOwner:pathList[0],
      repoName:pathList[1],
      deleteCol:deleteName,
    }
  })
      .then(res=>{

        if(res.code == 200) {
          ElMessage({
            type: 'success',
            message: 'delete success',
          })
          router.go(0)
        }
        if(res.code == 500) {
          ElMessage({
            type: 'success',
            message: 'delete canceled',
          })
        }
      })
}


//public\private 分开展示
const datatable1 = [
  {
    ID:'1',
    title:"public repository",
    main:"Every can view this repository",
  },
  {
    ID:'2',
    title:"direct access",
    main:"Everyone you select have the power to change the repository",
  },
]
const datatable2 = [
  {
    ID:'1',
    title:"private repository",
    main:"Only those with access to this repository can view it.",
  },
  {
    ID:'2',
    title:"direct access",
    main:"Everyone you select have the power to change the repository",
  },
]
</script>
<style scoped>
</style>

<script lang="ts">
export default {
  name: "Code"
}
</script>

<template>
  <div style="display: flex; width: 100%">
    <div style="height: 100%; min-width: 300px; width: 80%; margin-left: 50px; margin-top: 20px">
      <div style="display: flex">
        <el-button @click="branchSwitchShow">
          <el-icon><Operation/></el-icon>
          main
          <el-icon><CaretBottom/></el-icon>
        </el-button>
        <div style="margin-top: 5px; margin-left: 15px; display: flex">
          <el-icon style="margin-top: 3px; margin-right: 3px"><Operation/></el-icon>
          {{1}}
          <span style="margin-left: 5px; font-family: Calibri; font-size: 18px; color: grey">branch</span>
        </div>
        <div style="width: 50%"></div>
        <el-button style="margin-right: 10px">Go to file</el-button>
        <el-dropdown>
          <el-button>Add file
            <el-icon><CaretBottom/></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goto('new')">create new file</el-dropdown-item>
              <el-dropdown-item @click="goto('upload')">upload files</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button type="primary" style="margin-left: 10px" @click="cloneShow">
          Clone
          <el-icon><CaretBottom/></el-icon>
        </el-button>
      </div>
<!--悬浮窗-->
      <div v-show="ifBranchSwitchShow" style="z-index: 2; position: absolute;">
        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 10px; width: 300px; background-color: white">
          <div style="display: flex; margin-top: 10px;">
            <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Switch branches</span>
            <el-icon @click="branchSwitchHide" color="gray" style="position: absolute; right: 10px;"><CloseBold/></el-icon>
          </div>
          <el-divider style="margin-top: 10px; margin-bottom: 10px;"/>
          <el-input placeholder="Find or create a branch..." style="width: 90%; margin: 0px 14px 0px 14px;"/>
          <el-divider style="margin-top: 10px; margin-bottom: 0px;"/>

        </div>
      </div>

      <div v-show="ifCloneShow" style="z-index: 2; position: absolute; right:400px">
        <div style="box-shadow: #e7e7e7 5px 5px 10px; border: #d1d1d1 solid 1px; border-radius: 5px; margin-top: 10px; width: 300px; background-color: white">
          <div style="display: flex; margin-top: 10px;">
            <span style="margin-left:20px; color: grey; font-family: Calibri; font-weight: bold; font-size: 14px;">Clone</span>
            <el-icon @click="cloneHide" color="gray" style="position: absolute; right: 10px;"><CloseBold/></el-icon>
          </div>
          <el-divider style="margin-top: 10px; margin-bottom: 10px;"/>
          <el-tabs style="margin-left: 15px;margin-bottom: 20px;margin-right: 20px">
            <el-tab-pane label="SSH" name="first" />
          </el-tabs>
          <div style="display: flex;margin-left: 0px">
            <el-input
                id="url"
                readonly v-model="url"
                style="width: 80%; margin: 0px 14px 0px 14px;"
            />
            <el-button
                style="width: 30px;margin-left: -17px; margin-right: 10px;
                border-bottom-left-radius: 0px;border-top-left-radius: 0px;z-index: 1"
                @click="copyUrl">
              <el-icon><CopyDocument /></el-icon>
            </el-button>
          </div>
          <el-divider style="margin-top: 10px; margin-bottom: 0px;"/>

        </div>
      </div>

<!--主体-->
      <div style="z-index: 1; border: #d1d1d1 solid; width: 100%; margin-top: 20px; border-radius: 10px; box-shadow: #e4e7ed 3px 3px 5px;">
        <el-table :data="pathData" style="width: 100%; border-radius: 10px;">
          <el-table-column prop="fileType" width="40px">
            <template #default="scope">
              <el-icon v-if="pathData[scope.$index].fileType==='Dir'" color="blue"><Folder/></el-icon>
              <el-icon v-else><Document/></el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="fileName" label="文件名">
            <template #default="scope">
              <el-link @click="addQuery(pathData[scope.$index].fileName)">{{pathData[scope.$index].fileName}}</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>

<!--about-->
    <div style="width: 350px; padding: 0 50px 0 50px">
      <div style="margin: 50px 0 0 0px; font-family: Calibri; font-size: 24px;">
        About
      </div>
      <div v-if="repo.repoBio===undefined" style="font-style: italic; font-family: Calibri; font-size: 18px; color: #b1b1b1; margin-top: 20px">
        No description, website, or topics provided.
      </div>
      <div v-else style="font-style: italic; font-family: Calibri; font-size: 18px; color: #b1b1b1; margin-top: 20px">{{repo.repoBio}}</div>
      <el-divider/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {pushScopeId, reactive, ref} from 'vue'
import router from '../router'
import {useRouter} from "vue-router";
import request from '../utils/request.js'
import {ElMessage} from "element-plus";

  let path = router.currentRoute.value.fullPath
  let regexp = /(\w)+/g
  path = path.match(regexp)

  let query = router.currentRoute.value.query
  let branch, suffixDir
  if (query !== undefined) {
    branch=query.branch
    suffixDir=query.suffixDir
  }
  if (branch===undefined) branch='master'

  let repo = reactive({
    repoId: '',
    repoName: '',
    repoOwner: '',
    repoBio: '',
    repoVisibility: '',
  })

  request.get('/repo/name',{
    params: {
      repoOwner: path[0],
      repoName: path[1]
    }
  }).then(res => {
    repo.repoId=res.data.repoId
    repo.repoName=res.data.repoName
    repo.repoOwner=res.data.repoOwner
    repo.repoBio=res.data.repoBio
    repo.repoVisibility=res.data.repoVisibility
  })

let ifBranchSwitchShow = ref(false)

function branchSwitchShow(){
  ifBranchSwitchShow.value=!ifBranchSwitchShow.value
}
function branchSwitchHide(){
  ifBranchSwitchShow.value=false
}

const pathData = ref([
  {fileType: 'Dir', fileName: '.idea'},
  {fileType: 'Dir', fileName: 'back'},
  {fileType: 'Dir', fileName: 'front'},
  {fileType: 'File', fileName: '.gitignore'},
  {fileType: 'File', fileName: 'README.md'},
  {fileType: 'File', fileName: 'package-lock.json'},
])

let url=ref('')
let ifCloneShow = ref(false)
function cloneShow(){
  ifCloneShow.value=!ifCloneShow.value
  request.get('/url', {
    params: {
      repoOwner: path[0],
      repoName: path[1]
    }
  }).then(res=>{
    url.value = res.data
  })
}
function cloneHide(){
  ifCloneShow.value=false
}

function copyUrl(){
  var url_ = document.getElementById('url')
  url_.select()
  document.execCommand("Copy")
  ElMessage({
    type:'success',
    message:'Copy successfully!'
  })
}

function addQuery(queryObj){
  router.push({path: router.currentRoute.value.fullPath,
    query: {branch: branch, suffixDir: queryObj}})
}

function goto(direct){
  router.push('/'+path[0]+'/'+path[1]+'/'+direct)
}
</script>

<style scoped>

</style>
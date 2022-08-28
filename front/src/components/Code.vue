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
            <div style="margin-left: 5px; margin-right: 5px">{{ nowBranch }}</div>
          <el-icon><CaretBottom/></el-icon>
        </el-button>
        <div style="margin-top: 5px; margin-left: 15px; display: flex">
          <el-icon style="margin-top: 3px; margin-right: 3px"><Operation/></el-icon>
          {{branchList.length}}
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
          <el-table :data="branchList" style="width: 100%; border-radius: 10px;">
            <el-table-column prop="" label="">
              <template #default="scope">
                <div @click="changeBranch(scope.$index)" style="height: 20px; width: 100%; display: flex">
                  <el-icon v-if="branchList[scope.$index]===nowBranch"><Select/></el-icon>
                  <div style="position: absolute; left: 30px; bottom: 10px; font-family: Calibri; font-size: 16px">{{branchList[scope.$index]}}</div>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <el-divider style="margin: 1px 0 1px 0;" border-style="dashed"/>
          <el-button  @click="dialogVisible = true" style="width: 100%; padding-right: 10px"><div style="color: steelblue">New Branch</div></el-button>

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
      <div>
        <router-view/>
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
<!--弹窗-->
  <el-dialog
      v-model="dialogVisible"
      title="Create a Branch"
      width="30%"
      :before-close="handleClose"
  >
    <div>
      <div>Branch name</div>
      <el-input v-model="newBranchName"></el-input>
      <div style="margin-top: 10px">Branch source</div>
      <el-select v-model="nowBranch" placeholder="Select" size="large" style="margin-top: 10px">
        <el-option
            v-for="item in branchList"
            :value="item"
        />
      </el-select>
    </div>


    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="createBranchButtonClicked">
          Confirm
        </el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {pushScopeId, reactive, ref, watch, watchEffect} from 'vue'
import router from '../router'
import {useRouter} from "vue-router";
import request from '../utils/request.js'
import {ElMessage} from "element-plus";


  let path = router.currentRoute.value.fullPath
  let pathList = path.substr(1).split('/')
  let newBranchName=ref('')
  let repo = reactive({
    repoId: '',
    repoName: '',
    repoOwner: '',
    repoBio: '',
    repoVisibility: '',
  })

  let nowBranch = ref('')
  nowBranch.value=pathList[3]
  let branchList = ref([])

  request.get('/repo/name',{
    params: {
      repoOwner: pathList[0],
      repoName: pathList[1]
    }
  }).then(res => {
    repo.repoId=res.data.repoId
    repo.repoName=res.data.repoName
    repo.repoOwner=res.data.repoOwner
    repo.repoBio=res.data.repoBio
    repo.repoVisibility=res.data.repoVisibility
  })

  request.get('/repo/branches',{
    params: {
      repoOwner: pathList[0],
      repoName: pathList[1]
    }
  }).then(res => {
    console.log(res)
    branchList.value=res.data
  })

  const dialogVisible = ref(false)
  let ifBranchSwitchShow = ref(false)

  function branchSwitchShow(){
    ifBranchSwitchShow.value=!ifBranchSwitchShow.value
  }
  function branchSwitchHide(){
    ifBranchSwitchShow.value=false
  }

  let url=ref('')
  let ifCloneShow = ref(false)
  function cloneShow(){
    ifCloneShow.value=!ifCloneShow.value
    request.get('/url', {
      params: {
        repoOwner: pathList[0],
        repoName: pathList[1]
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

  function changeBranch(index){
    console.log(pathList)
    pathList[3]=branchList.value[index]
    console.log(pathList)
    let nextPath='', iter = 0
    while (iter<pathList.length){
      nextPath=nextPath + '/' + pathList[iter]
      iter++
      console.log(nextPath)
    }
    nowBranch.value=branchList.value[index]
    branchSwitchHide()
    router.push({path: nextPath})
  }

  function goto(direct){
    router.push('/'+pathList[0]+'/'+pathList[1]+'/'+direct)
  }

  function createBranchButtonClicked(){
    dialogVisible.value=false
  }

</script>

<style scoped>

</style>
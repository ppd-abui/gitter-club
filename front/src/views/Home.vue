
<script lang="ts">
  import RepositoryS from '../components/RepositoryS.vue'
  import Index from'../components/Index.vue'
  export default {
    name: 'Home',
    components:{
        RepositoryS,
        Index
    }
  }
</script>

<template>
  <el-container>
    <el-header style="padding: 0"><Index/></el-header>
    <el-container>
      <el-aside style="border-right: #e7e7e7 solid 1px; padding: 0 25px 0 25px" width="300px">
        <div style="display: flex; margin-top: 30px; ">
          <div style="margin: 10px 0 0 5px; font-family: Calibri; font-size: 16px; font-weight: bold">Recent Repositories</div>
          <el-button @click="goto('new')" style="margin-left: 43px; padding: 0 10px 0 10px">
            <el-icon style="margin-right: 4px"><Collection/></el-icon>
            New
          </el-button>
        </div>
        <el-input style="margin-top: 10px" placeholder="Find a repository..."/>
        <div>
          <RepositoryS v-for="(item, i) in repoList" :repo="item"/>
        </div>
        <el-divider/>
      </el-aside>

      <el-main style="background-color: #f6f8fa; display: flex">
        <div style="min-width: 600px; width: 60%; height: 600px;">
          <div style="position: relative; left: 15px; top: 10px; display: flex">
            <div style="position: relative; font-family: 'Calibri Light'; font-size: 36px;">Start with</div>
            <div style="font-family: Cinzel; font-size: 36px; margin: 0 0 0 20px">GitterClub</div>
          </div>
          <el-divider/>
          <div style="margin-top: 30px; background-color: white; padding: 20px; border-radius: 10px; border: #d1d1d1 solid 1px">

            <div style="font-family: Calibri; font-size: 28px; margin: 20px">
              Discover interesting projects and people to populate your personal news feed.
            </div>
            <div style="background-color: #f6f8fa; border: #d1d1d1 solid 1px; border-radius: 10px; margin: 30px 10px">
              <div class="helpList">
                <div class="listBio">To Create a Repository</div>
                <el-button @click="goto('new')" class="buttonList">New Repository</el-button>
              </div>
              <div class="helpList">
                <div class="listBio">To Explore</div>
                <el-button @click="goto('search')" class="buttonList">Search</el-button>
              </div>
            </div>
          </div>
        </div>
        <div style="margin: 30px">
          <div style="margin-bottom: 20px; font-family: 'Calibri'; font-size: 24px">
            Notifications
          </div>
          <el-timeline>

            <el-timeline-item
                v-for="(event) in allEvent">
              <div  v-if="event.eventType===EventType.createIssue">
                <div>
                  事件类型:{{event.eventType}}
                </div>
                <div style="display: flex">
                  <div>
                    用户{{event.eventUsername}}
                  </div>
                  <div>
                    在仓库{{event.eventReponame}}中
                  </div>
                </div>
                <div style="display: flex">
                  创建了标题为
                  <el-link style="color: #1E97D4;" @click="gotoIssue(event.eventReponame,event.eventTitle)">
                    {{event.eventTitle}}
                  </el-link>
                  的工单
                </div>
              </div>
              <div v-if="event.eventType===EventType.addAssigners">
                <div>
                  事件类型:{{event.eventType}}
                </div>
                <div style="display: flex">
                  用户<div >
                  {{event.eventUsername}}
                </div>
                  <div>
                    在仓库{{event.eventReponame}}中
                  </div>
                </div>
                <div style="display: flex">
                  指派{{event.eventAssigners}}
                  为工单
                  <el-link style="color: #1E97D4; text-underline: #cf222e " @click="gotoIssue(event.eventReponame,event.eventTitle)">
                    {{event.eventTitle}}
                  </el-link>
                  的负责人
                </div>
              </div>
              <div v-if="event.eventType===EventType.deleteAssigners">
                <div>
                  事件类型:{{event.eventType}}
                </div>
                <div style="display: flex">
                  <div>
                    用户{{event.eventUsername}}
                  </div>
                  <div>
                    在仓库{{event.eventReponame}}中
                  </div>
                </div>
                <div style="display: flex">
                    <div>
                      取消指派{{event.eventAssigners}}
                    </div>

                </div>
                <div style="display: flex">
                  为工单
                  <el-link style="color: #1E97D4;" @click="gotoIssue(event.eventUsername,event.eventReponame)">
                    {{event.eventTitle}}
                  </el-link>
                  <div>的负责人</div>
                </div>
              </div>
            </el-timeline-item>

          </el-timeline>
        </div>
      </el-main>
    </el-container>
  </el-container>

</template>

<script lang="ts" setup>
import router from '../router'
import require from '../utils/request.js'
import {onBeforeMount, reactive, ref, toRef} from "vue";
import request from "../utils/request.js";

  const EventType={
    createIssue:"CreateIssue",
    addAssigners:"AddAssigner",
    deleteAssigners:"DeleteAssigner"
  }
  let allEvent =ref([])
  let path = router.currentRoute.value.fullPath;
  let pathList = path.substr(1).split('/')
  let userAccount=localStorage.getItem("userAccount");
  onBeforeMount(() => {

    request.get("/event/get",{
      params:{
        userAccount:userAccount
      }
    }).then((res)=>
        {
          allEvent.value=res.data
        }
    )
  });

  function goto(path){
    router.push('/'+path)
  }

  function gotoIssue(repoOwner,repoName)
  {
    router.push({path: '/'+repoOwner+'/'+repoName})
  }


let repoList= ref([])

  require.get('/repo/info',{
    params:{
      userAccount:localStorage.getItem("userAccount")
    }
  }).then(res => {
        if (res.code === 200) {
          res.data.forEach(function (item, index){
            repoList.value.push(item)
          })
          console.log('repoList',repoList.value)
        }
      })
</script>

<style scoped>
  .helpList{
    display: flex;
    margin: 20px;
  }
  .listBio{
    width: 70%;
    margin-top: 7px;
  }
  .buttonList{
    width: 150px;
  }
</style>

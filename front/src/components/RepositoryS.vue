<script>
export default {
  name: "RepositoryS"
}
</script>

<template>
  <div style="width: 200px; height: 30px; display: flex; margin: 5px 5px 5px 5px">
    <img style="width: 30px; height: 30px;" src="../assets/white.png"/>
    <el-link style="margin-left: 10px" @click="goto">{{repo.repoName}}</el-link>
  </div>
</template>

<script setup>
import {onMounted, reactive, toRef} from "vue";
  import request from "../utils/request";
  import router from "../router";

  console.log('new repoS')
  const props =defineProps({
    repo: Object
  })
  const repo = props.repo
  console.log('repo', repo)

  onMounted(() => {
    console.log(router.getRoutes())
    router.addRoute({
      path: '/'+repo.repoOwner+'/'+repo.repoName,
      name: repo.repoName+'repo',
      component: () => import('../views/Repository.vue'),
      redirect: '/'+repo.repoOwner+'/'+repo.repoName+'/code',
      children: [{
        path: 'code',
        component: () => import('../components/Code.vue')
      },{
        path: 'issues',
        component: () => import('../components/Issues.vue'),
        children: [{
          path: 'new',
          component: () => import('../components/CreateIssue.vue')
        }]
      },{
        path: 'pull',
        component: () => import('../components/Pull.vue')
      },{
        path: 'settings',
        component: () => import('../components/RepositorySetting.vue'),
        redirect: '/'+repo.repoOwner+'/'+repo.repoName+'/settings/general',
        children: [{
          path: 'general',
          component: () => import('../components/RepoSetting/General.vue')
        },{
          path: 'collaborator',
          component: () => import('../components/RepoSetting/Collaborator.vue')
        }]
      },{
        path: 'upload',
        component: () => import('../components/RepoCode/UploadFile.vue')
      },{
        path: 'new',
        component: () => import('../components/RepoCode/NewFile.vue')
      }]
    })
  })

  function goto(){
    router.push('/'+repo.repoOwner+'/'+repo.repoName)
  }


</script>

<style scoped>

</style>
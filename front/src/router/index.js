import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import request from "../utils/request";
import {ElMessage} from "element-plus";

const routes = [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue')
    },{
      path: '/new',
      name: 'createRepo',
      component: () => import('../views/CreateRepo.vue')
    },,{
      path: '/search',
      name: 'search',
      component: () => import('../views/SearchFile.vue')
    },,{
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
    },{
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    },{
      path: '/404',
      name: '404',
      component: () => import('../views/404.vue')
    }
  ]
  const router = createRouter({
    history: createWebHashHistory(),
    routes
  })

  router.beforeEach((to, from, next)=>{
    if (localStorage.getItem('token')){
      console.log('has token')
      console.log('to',to.path)
      if (to.matched.length === 0) {
        let pathList = to.path.substr(1).split('/')


        let userAccount = pathList[0]

        //addAccount
        request.get('/register/account', {
          params: {
            userAccount: userAccount
          }
        }).then(res => {
          //接口为查重接口，存在重复返回500
          if (res.code === 500) {
            console.log('add account route')
            router.addRoute({
              path: '/' + userAccount,
              name: userAccount + 'space',
              component: () => import('../views/Person.vue'),
            })
            console.log('added account route',router.getRoutes())
            if (pathList.length===1) next(to.path)
          } else if (res.code === 200) {
            next('/404')
          }
        })
        if (pathList.length > 1) {
          let repoName = pathList[1]
          let repoOwner = userAccount

          request.get('/repo/name',{
            params: {
              repoOwner: repoOwner,
              repoName: repoName
            }
          }).then(res => {
            //addRepo
            if (res.code === 200) {
              console.log('add repo route')
              router.addRoute({
                path: '/' + repoOwner + '/' + repoName,
                name: repoName + 'repo',
                component: () => import('../views/Repository.vue'),
                redirect: '/' + repoOwner + '/' + repoName + '/code',
                children: [{
                  path: 'code',
                  component: () => import('../components/Code.vue'),
                  children: [{
                    path: ':tab*',
                    component: () => import('../components/RepoCode/File.vue')
                  }]
                }, {
                  path: 'issues',
                  component: () => import('../components/Issues.vue'),
                  children: [{
                    path: 'list',
                    component: () => import('../components/RepoIssues/IssueList.vue')
                  },{
                    path: ':issueName',
                    component: () => import('../components/RepoIssues/IssueContent.vue')
                  }]
                },{
                  path: 'newissue',
                  component: () => import('../components/CreateIssue.vue')
                }, {
                  path: 'pull',
                  component: () => import('../components/Pull.vue')
                }, {
                  path: 'settings',
                  component: () => import('../components/RepositorySetting.vue'),
                  redirect: '/' + repoOwner + '/' + repoName + '/settings/general',
                  children: [{
                    path: 'general',
                    component: () => import('../components/RepoSetting/General.vue')
                  }, {
                    path: 'collaborator',
                    component: () => import('../components/RepoSetting/Collaborator.vue')
                  }]
                }, {
                  path: 'upload',
                  component: () => import('../components/RepoCode/UploadFile.vue')
                }, {
                  path: 'new',
                  component: () => import('../components/RepoCode/NewFile.vue')
                }]
              })
            }
            console.log('added repo route',router.getRoutes())
            let set = ['code','issues','pull','settings','upload','new']
            if (pathList.length===2) next(to.path)
            else if (set.indexOf(path[2])===-1) next('/404')
            else next(to.path)
          })
        }

      } else next()
    } else {
      console.log('has not token')
      if (to.path==='/login') next()
      else next('/login')
    }
  })


  function saveData(name, data) {
    localStorage.setItem(name, JSON.stringify(data))
  }

  function getData(name) {
    if (!localStorage.getItem(name)){
      return localStorage.getItem(name)
    } else {
      return JSON.parse(localStorage.getItem(name));
    }
  }

  function deleteData(name) {
    if (!localStorage.getItem(name)){
      return
    } else {
      localStorage.removeItem(name)
    }
  }


export default router

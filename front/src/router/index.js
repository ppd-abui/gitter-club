import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import request from "../utils/request";
import {ElMessage} from "element-plus";

//   path: '/space',
//   name: 'space',
//   component: () => import('../views/Person.vue'),
//   children: [{
//     path: '/space/:tab',
//     name: 'tab',
//     component: () => import('../components/RepositoryCenter.vue')
//   }]
// }
// {
//   path: '/repo',
//   name: 'repository',
//   component: () => import('../views/Repository.vue'),
//   children: [{
//     path: 'code',
//     name: 'code',
//     component: () => import('../components/Code.vue')
//   },{
//     path: 'issues',
//     name: 'issues',
//     component: () => import('../components/Issues.vue')
//   },{
//     path: 'pull',
//     name: 'pull',
//     component: () => import('../components/Pull.vue')
//   },{
//     path: 'settings',
//     name: 'settings',
//     component: () => import('../components/RepositorySetting.vue'),
//     redirect: '/repo/settings/general',
//     children: [{
//       path: 'general',
//       name: 'general',
//       component: () => import('../components/RepoSetting/General.vue')
//     },{
//       path: 'collaborator',
//       name: 'collaborator',
//       component: () => import('../components/RepoSetting/Collaborator.vue')
//     }]
//   },{
//     path: 'new',
//     name: 'newFile',
//     component: () => import('../components/RepoCode/NewFile.vue')
//   },{
//     path: 'upload',
//     name: 'uploadFile',
//     component: () => import('../components/RepoCode/UploadFile.vue')
//   }]
// }

let catRouter = [{
  path: '/slphx',
  name: 'space',
  component: () => import('../views/Person.vue'),
  children: [{
    path: '/slphx/:tab',
    name: 'tab',
    component: () => import('../components/RepositoryCenter.vue')
  }]
}]

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
    if (getData('token')){
      console.log('has token')
      console.log(to.path)
      console.log(to.matched.length)
      if (to.matched.length === 0) {
        let s = to.path
        let endList = []
        for (let i = 0; i < s.length; i++) {
          if (s[i] === '/') {
            endList.push(i)
          }
        }
        let userAccount = ''
        if (endList.length > 1) {
          userAccount = to.path.substring(endList[0] + 1, endList[1])
        } else {
          userAccount = to.path.substring(endList[0] + 1, s.length)
        }
        console.log(userAccount)
        requestAdd(endList, userAccount).then(r => {})
      }

      if (!getData('routeAdd')) next()
      else {
        let routeAdds= getData('routeAdd')
        console.log('add '+routeAdds.path)
        // routeAdds = getRouter(routeAdds)
        RouterAddHelp(routeAdds)
        deleteData('routeAdd')
        next()
      }
    } else {
      console.log('has not token')
      if (to.path==='/login') next()
      else next('/login')
    }
    if (to.matched.length===0) router.push('/404')
  })

  // function getRouter(routerMap) {
  //   let accessedRouters = []
  //   routerMap.forEach((item, index) => {
  //     accessedRouters.push({
  //       path: item.path,
  //       name: item.name,
  //       redirect: item.redirect,
  //       component: () => import(item.component)
  //     })
  //     if (item.children && item.children.length) {
  //       accessedRouters[index].children = getRouter(item.children)
  //     }
  //   })
  //   return accessedRouters
  // }
  function RouterAddHelp(routeAdds){
    console.log('addRoute '+ routeAdds.path)
    router.addRoute({
      path: routeAdds.path,
      name: routeAdds.name,
      component: () => import(routeAdds.component),
      children: routeAdds.children
    })
    if (routeAdds.children!=undefined)
      if (routeAdds.children.length>0){
        for (let i=0; i<routeAdds.children.length; i++){
          console.log(routeAdds.children[i])
          RouterAddHelp(routeAdds.children[i])
        }
      }
  }

  async function requestAdd(endList, userAccount){
    await request.get('/register/account', {
      params: {
        userAccount: userAccount
      }
    }).then(res => {
      console.log(res.code)
      //接口为查重接口，存在重复返回500
      if (res.code === 500) {
        console.log('has account')
        localStorage.setItem('routeAdd', JSON.stringify({
          path: '/' + userAccount,
          name: userAccount + 'space',
          component: '../views/Person.vue',
          // children: [{
          //   path: '/'+userAccount+'/:tab',
          //   name: 'tab',
          // }]
        }))
        if (endList.length === 1) router.push('/' + userAccount)
      } else if (res.code === 200) {
        console.log('no account')
      }
    })
  }

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
  // router.beforeEach((to, from, next) =>{
  //   console.log(getRouter)
  //   if (!getRouter){
  //     console.log(1)
  //     if (!getObjArr('router')){
  //       getRouter = catRouter
  //       saveObjArr('router',getRouter)
  //
  //       routerGo(to, next)
  //     } else {
  //       getRouter = getObjArr('router')
  //       routerGo(to,next)
  //     }
  //   } else if (to.matched.length == 0){
  //     next("/404")
  //   } else {
  //     next()
  //   }
  // })

  // function  routerGo(to, next){
  //   // getRouter = filterAsyncRouter(getRouter)
  //   router.addRoute(getRouter)
  //   console.log(getRouter[0].path)
  //
  //   router.push({path: getRouter[0].path})
  // }


export default router

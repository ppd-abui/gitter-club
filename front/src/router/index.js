import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue')
    },{
      path: '/new',
      name: 'createRepo',
      component: () => import('../views/CreateRepo.vue')
    },{
      path: '/repo',
      name: 'repository',
      component: () => import('../views/Repository.vue'),
      redirect: '/repo/code',
      children: [{
        path: '/repo/code',
        name: 'code',
        component: () => import('../components/Code.vue')
      },{
        path: '/repo/issues',
        name: 'issues',
        component: () => import('../components/Issues.vue')
      },{
        path: '/repo/pull',
        name: 'pull',
        component: () => import('../components/Pull.vue')
      },{
        path: '/repo/settings',
        name: 'settings',
        component: () => import('../components/RepositorySetting.vue'),
        redirect: '/repo/settings/general',
        children: [{
          path: '/repo/settings/general',
          name: 'general',
          component: () => import('../components/RepoSetting/General.vue')
        },{
          path: '/repo/collaborator',
          name: 'collaborator',
          component: () => import('../components/RepoSetting/Collaborator.vue')
        }]
      }]
    },{
      path: '/search',
      name: 'search',
      component: () => import('../views/SearchFile.vue')
    },{
      path: '/space',
      name: 'space',
      component: () => import('../views/Person.vue'),
      children: [{
        path: '/space/:tab',
        name: 'tab',
        component: () => import('../components/RepositoryCenter.vue')
      }]
    },{
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
    },{
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    },{
      path: '/:catchAll(.*)',
      redirect: '/404'
    },{
      path: '/404',
      name: '404',
      component: () => import('../views/404.vue')
    }
  ]
})

export default router

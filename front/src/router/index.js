import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home'
    }, {
      path: '/home',
      name: 'home',
      component: () => import('../views/Home.vue')
    },{
      path: '/person',
      name: 'person',
      component: () => import('../views/Person.vue'),
      redirect: '/RepositoryCenter',
      children: [{
        path: '/RepositoryCenter',
        name: 'RepositoryCenter',
        component: () => import('../components/RepositoryCenter.vue')
      },{
        path: '/StarCenter',
        name: 'StarCenter',
        component: () => import('../components/StarCenter.vue')
      }]
    },{
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
    },{
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    }, {
      path: '/404',
      name: '404',
      component: () => import('../views/404.vue')
    },{
      path: '/:catchAll(.*)',
      redirect: '/404'
    }
  ]
})

export default router

import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue')
    },{
      path: '/space',
      name: 'space',
      component: () => import('../views/Person.vue'),
      children: [{
        path: '/:tab',
        name: 'tab',
        component: () => import('../components/RepositoryCenter.vue')
      }]
    }, {
      path: '/new',
      name: 'createRepo',
      component: () => import('../views/CreateRepo.vue')
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

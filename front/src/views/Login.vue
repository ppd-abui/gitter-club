<script lang="ts">
export default {
  name: "Login"
}
</script>

<template>
  <el-container style="height: 100%; display: flex;">
<!--Logo-->
    <el-header style="margin: auto; height: 180px">
      <img style="margin-top: 40px; object-fit: contain; height: 70%" src="src/assets/black.png">
    </el-header>
<!--Title-->
    <el-main>
      <div style="text-align: center; font-family: Cinzel; font-size: 30px; position: relative; bottom: 20px">Sign in to GitterClub</div>

      <el-form ref="formRef" :model="user" :rules="rules" status-icon style="width: 350px; border: 1.5px solid #d1d1d1; border-radius: 20px; background: #f7f7f7; margin: auto;">
        <div style="margin: 0 0 30px 0"/>

        <p style="position: relative; right: 95px; font-size: 14px; text-align: center; margin: auto;">Account</p>
<!--userAccount-->
        <el-form-item prop="userAccount" style="width: 250px; margin: auto;" >
          <el-input v-model="user.userAccount" autocomplete="off"/>
        </el-form-item>

        <p style="margin-top: 10px">
          <span style="position: relative; left: 51px; font-size: 14px; text-align: center;">Password</span>
<!--  forgot link-->
          <el-link style="position: relative; left: 140px; color: #1E97D4; font-size: 12px; text-align: center; margin: auto;">forgot password?</el-link>
        </p>
<!--Password-->
        <el-form-item prop="userPassword" style="width: 250px; margin: auto auto 20px;" >
          <el-input v-model="user.userPassword" autocomplete="off" show-password/>
        </el-form-item>
<!--sign in button-->
        <el-form-item>
          <el-button type="primary" @click=login(formRef); style="color: white; font-size: 16px; font-family: 'Calibri'; letter-spacing: 2px; width: 250px; margin: auto;">
            sign in
          </el-button>
        </el-form-item>
      </el-form>
    </el-main>
<!--Register-->
    <el-footer style="height: 100%;">
      <el-form style="width: 350px; height: 80px;border: 1.5px solid #d1d1d1; border-radius: 20px; background: #f7f7f7; margin: 50px auto;">
        <el-form-item style="margin-bottom: 0">
          <div style="width: 100%; height: 70%; font-size: 20px; padding-top: 10px; font-family: Centaur; text-align: center">New to
            <span style="font-family: Cinzel; font-size: 20px">GitterClub?</span>
          </div>
        </el-form-item>
        <el-form-item style="margin-top: 0;">
          <div style="width: 100%; display: flex">
<!--  register link-->
            <el-link style="font-size: 20px; font-family: Centaur; margin: auto;
                        color: #1E97D4" href="../register">Create an Account</el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-footer>

  </el-container>
</template>

<script lang="ts" setup>
  import {ref, reactive} from "vue";
  import request from "../utils/request";
  import type { FormInstance } from 'element-plus'
  import router from "../router"

  const formRef = ref<FormInstance>()

  let user = reactive({
    userAccount: '',
    userPassword: ''
  })

  const checkPass = (rule: any, value: any, callback: any) => {
    if (value === '') {
      callback(new Error('Please input the password'))
    } else {
      callback()
    }
  }
  const checkAccount = (rule: any, value: any, callback: any) => {
    if (!value) {
      callback(new Error('Please input the account'))
    } else {
      callback()
    }
  }

  const rules = reactive({
    userAccount: [{ validator: checkAccount, trigger: 'blur' }],
    userPassword: [{ validator: checkPass, trigger: 'blur' }],
  })

  function login(formEl: FormInstance | undefined){
    if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        console.log('login')
        request.post('/login', user).then(res=>{
          console.log(res)
          router.push('/')
        })
      } else {
        console.log('error')
        return false
      }
    })


  }
</script>

<style scoped>

</style>
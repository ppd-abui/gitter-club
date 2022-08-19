<script lang="ts">
export default {
  name: "Register.vue"
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
      <div style="text-align: center; font-family: Cinzel; font-size: 30px; position: relative; bottom: 20px">
        Sign up to GitterClub
      </div>

      <el-form
          :model="user"
          :rules="rules"
          label-position="right"
          style="width: 400px; border:1.5px solid #d1d1d1; border-radius: 20px; background: #f7f7f7; margin: auto;"
      >
        <el-form-item prop="userAccount"
                      style="width: 300px; margin: 20px auto 20px" label="Account" label-width="50px">
          <el-input v-model="user.userAccount" />
        </el-form-item>
        <el-form-item prop="userPassword" style="width: 300px; margin: auto auto 20px;" label="Password" label-width="50px">
          <el-input v-model="user.userPassword" show-password/>
        </el-form-item>
        <el-form-item prop="confirmPassword" style="width: 300px; margin: auto auto 20px;" label="Confirm" label-width="50px">
          <el-input v-model="user.confirmPassword" show-password/>
        </el-form-item>
        <el-form-item style="width: 300px; margin: auto auto 15px;" >
          <div style="margin: auto">
            <el-button type="primary" @click="submitForm(ruleFormRef)">
              Submit
            </el-button>

            <el-button @click="reset"> Reset </el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-main>


    <el-footer style="height: 100%;">
      <el-form style="width: 350px; height: 80px;border: 1.5px solid #d1d1d1; border-radius: 20px; background: #f7f7f7; margin: 50px auto;">
        <el-form-item style="margin-bottom: 0;margin-top: -5px;">
          <div style="width: 100%; height: 70%; font-size: 20px; padding-top: 10px; font-family: Centaur; text-align: center">
            I am a
            <span style="font-family: Cinzel; font-size: 20px">Gitter</span>
          </div>
        </el-form-item>
        <el-form-item style="margin-top: 0;">
          <div style="width: 100%; display: flex">
            <!--  register link-->
            <el-link
                style="font-size: 20px;
                font-family: Centaur;
                margin: auto;
                color: #1E97D4"
                href="../login">
              Sign in for GitterClub
            </el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-footer>


  </el-container>
</template>


<script lang="ts" setup>
import { reactive, ref } from 'vue'
import request from "../utils/request";
import type { FormInstance } from 'element-plus'
import { ElMessage } from "element-plus";

const ruleFormRef = ref<FormInstance>()

//检查输入账号是否合法
const checkUserAccount = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('Please input the account'))
  }

  request.get("/register/account", {
    params:{
      userAccount: user.userAccount
    }
  }).then(res => {
    if(res.code === 500){
        ElMessage({
          type: 'warning',
          message: 'this account has been used',
        })
      user.userAccount=''
    }
  })
  callback()
}

const checkUserPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please input the password'))
  } else {
    if (user.userPassword !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('userPassword', () => null)
    }
    callback()
  }
  callback()
}

const checkConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please input the password again'))
  } else if (value !== user.userPassword) {
    callback(new Error("Two inputs don't match!"))
  } else {
    callback()
  }
}

const user = reactive({
  userAccount: '',
  userPassword: '',
  confirmPassword: '',
})

const rules = reactive({
  userAccount: [{ validator: checkUserAccount, trigger: 'blur' }],
  userPassword: [{ validator: checkUserPassword, trigger: 'blur' }],
  confirmPassword: [{ validator: checkConfirmPassword, trigger: 'blur' }],
})

// const submitForm = (formEl: FormInstance | undefined) => {
//   if (!formEl) return
//   formEl.validate((valid) => {
//     if (valid) {
//       console.log('submit!')
//     } else {
//       console.log('error submit!')
//       return false
//     }
//   })
// }

function reset(){
  user.userAccount=''
  user.userPassword=''
  user.confirmPassword=''
}
</script>

<style scoped>

</style>
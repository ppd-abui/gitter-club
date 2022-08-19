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
      <div style="text-align: center; font-family: Cinzel; font-size: 30px; position: relative; bottom: 20px">Sign up to GitterClub</div>

        <el-form
            :model="ruleForm"
            status-icon
            :rules="rules"
        >
          <el-form-item prop="pass">
            <el-input v-model="ruleForm.pass" autocomplete="off" />
          </el-form-item>
          <el-form-item prop="age">
            <el-input v-model.number="ruleForm.age" />
          </el-form-item>
<!--          <el-form-item>-->
<!--            <el-button type="primary" @click="submitForm(ruleFormRef)"-->
<!--            >Submit</el-button-->
<!--            >-->
<!--            <el-button @click="resetForm(ruleFormRef)">Reset</el-button>-->
<!--          </el-form-item>-->
        </el-form>
    </el-main>

  </el-container>
</template>


<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormInstance } from 'element-plus'

const ruleFormRef = ref<FormInstance>()

const checkAge = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('Please input the age'))
  }
    callback()
  // setTimeout(() => {
  //   if (!Number.isInteger(value)) {
  //     callback(new Error('Please input digits'))
  //   } else {
  //     if (value < 18) {
  //       callback(new Error('Age must be greater than 18'))
  //     } else {
  //       callback()
  //     }
  //   }
  // }, 1000)
}

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please input the password'))
  } else {
    // if (ruleForm.checkPass !== '') {
    //   if (!ruleFormRef.value) return
    //   ruleFormRef.value.validateField('checkPass', () => null)
    // }
    callback()
  }
}
// const validatePass2 = (rule: any, value: any, callback: any) => {
//   if (value === '') {
//     callback(new Error('Please input the password again'))
//   }
//   // else if (value !== ruleForm.pass) {
//   //   callback(new Error("Two inputs don't match!"))
//   // }
//   else {
//     callback()
//   }
// }

const ruleForm = reactive({
  pass: '',
  checkPass: '',
  age: '',
})

const rules = reactive({
  pass: [{ validator: validatePass, trigger: 'blur' }],
  // checkPass: [{ validator: validatePass2, trigger: 'blur' }],
  age: [{ validator: checkAge, trigger: 'blur' }],
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!')
      return false
    }
  })
}

// const resetForm = (formEl: FormInstance | undefined) => {
//   if (!formEl) return
//   formEl.resetFields()
// }
</script>

<style scoped>

</style>
<script>
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
      <div style="text-align: center; font-family: Cinzel; font-size: 30px; position: relative; bottom: 20px">Sign In to GitterClub</div>

      <el-form style="width: 350px; border: 1.5px solid #d1d1d1; border-radius: 20px; background: #f7f7f7; margin: auto;">
        <div style="margin: 0 0 30px 0"/>
<!--Username-->
        <p style="position: relative; right: 90px; font-size: 14px; text-align: center; margin: auto;">Username</p>
        <el-form-item>
          <el-input style="width: 250px; margin: auto"/>
<!--Password-->
        </el-form-item>
        <p>
          <span style="position: relative; left: 51px; font-size: 14px; text-align: center;">Password</span>
          <span style="position: relative; left: 140px; color: #1E97D4; font-size: 12px; text-align: center; margin: auto;">forgot password?</span>
        </p>
        <el-form-item>
          <el-input style="width: 250px; margin: auto"/>
        </el-form-item>
<!--sign in button-->
        <el-form-item>
          <el-button type="primary" style="color: white; font-size: 16px; font-family: 'Calibri'; letter-spacing: 2px; width: 250px; margin: auto;">
            sign in
          </el-button>
        </el-form-item>
      </el-form>
    </el-main>
<!--Register-->
    <el-footer style="height: 100%;">
      <el-form style="width: 350px; height: 100px;border: 1.5px solid #d1d1d1; border-radius: 20px; background: #f7f7f7; margin: 50px auto;">
        <el-form-item style="margin-bottom: 0">
          <div style="width: 100%; height: 70%; font-size: 20px; padding-top: 10px; font-family: Centaur; text-align: center">New to
            <span style="font-family: Cinzel; font-size: 20px">GitterClub?</span>
          </div>
        </el-form-item>
        <el-form-item style="margin-top: 0;">
          <div style="width: 100%; height: 30%; font-size: 20px; font-family: Centaur; text-align: center;
                      color: #1E97D4">Create an Account</div>
        </el-form-item>
      </el-form>
    </el-footer>

  </el-container>
</template>

<script setup>

</script>

<style scoped>

</style>
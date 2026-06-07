<template>
  <div class="register-page">
    <div class="register-card card">
      <h2 class="title">注册</h2>
      <el-form ref="formRef" :model="registerForm" :rules="rules" label-width="100px">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码(6-20位)"
            show-password
          />
        </el-form-item>
        <el-form-item label="验证码" prop="verifyCode">
          <el-input v-model="registerForm.verifyCode" placeholder="请输入验证码" maxlength="6">
            <template #append>
              <el-button @click="handleSendCode" :disabled="sendingCode">
                {{ sendingCode ? `${countDown}s` : '获取验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="registerForm.nickname" placeholder="请输入昵称(可选)" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading">注册</el-button>
          <router-link to="/login" class="login-link">已有账号？立即登录</router-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { authApi } from '@/api'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const sendingCode = ref(false)
const countDown = ref(0)

const registerForm = ref({
  phone: '',
  password: '',
  verifyCode: '',
  nickname: ''
})

const rules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20位之间', trigger: 'blur' }
  ],
  verifyCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码为6位数字', trigger: 'blur' }
  ]
}

// 发送验证码
const handleSendCode = async () => {
  if (!registerForm.value.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(registerForm.value.phone)) {
    ElMessage.warning('手机号格式不正确')
    return
  }

  try {
    await authApi.sendCode(registerForm.value.phone)
    ElMessage.success('验证码已发送，验证码为123456')
    sendingCode.value = true
    countDown.value = 60
    const timer = setInterval(() => {
      countDown.value--
      if (countDown.value <= 0) {
        clearInterval(timer)
        sendingCode.value = false
      }
    }, 1000)
  } catch (error) {
    console.error('发送验证码失败')
  }
}

// 注册
const handleRegister = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  loading.value = true
  try {
    const res = await authApi.register(registerForm.value)
    userStore.setToken(res.data.token)
    userStore.setUserInfo(res.data)
    ElMessage.success('注册成功')
    router.push('/')
  } catch (error) {
    console.error('注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;

  .register-card {
    width: 450px;
    padding: 40px;

    .title {
      text-align: center;
      margin-bottom: 30px;
      color: #3b99fc;
    }

    .login-link {
      margin-left: 20px;
      color: #3b99fc;
    }
  }
}
</style>
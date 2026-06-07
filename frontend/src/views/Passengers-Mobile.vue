<template>
  <div class="mobile-passengers-page">
    <!-- 顶部导航栏 -->
    <header class="top-nav">
      <button class="back-btn" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <polyline points="15,18 9,12 15,6"/>
        </svg>
      </button>
      <h1 class="nav-title">乘车人</h1>
      <button class="add-btn" @click="showAddDialog">添加</button>
    </header>

    <!-- 刷新提示 -->
    <div class="refresh-tip">
      <span>下拉刷新可获取12306乘车人最新状态</span>
    </div>

    <!-- 主内容区 -->
    <main class="passenger-list" v-loading="loading">
      <!-- 当前用户 -->
      <div v-if="currentUser" class="section current-user-section">
        <div class="section-label">当前用户</div>
        <div class="passenger-item current-user" @click="handleEdit(currentUser)">
          <div class="item-main">
            <div class="name-row">
              <span class="name">{{ currentUser.name }}</span>
              <span :class="['type-tag', getTypeClass(currentUser.passengerType)]">
                {{ getPassengerTypeName(currentUser.passengerType) }}
              </span>
              <span v-if="currentUser.nationality" class="nationality-tag">
                {{ currentUser.nationality }}
              </span>
            </div>
            <div class="id-info">
              <span class="id-type">{{ getIdTypeName(currentUser.idType) }}</span>
              <span class="id-card">{{ maskIdCard(currentUser.idCard) }}</span>
            </div>
          </div>
          <div class="item-action">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="2">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- 按首字母分组的乘客列表 -->
      <div v-for="group in groupedPassengers" :key="group.letter" class="section">
        <div class="section-letter">{{ group.letter }}</div>
        <div
          v-for="passenger in group.list"
          :key="passenger.id"
          class="passenger-item"
          @click="handleEdit(passenger)"
        >
          <div class="item-main">
            <div class="name-row">
              <span class="name">{{ passenger.name }}</span>
              <span :class="['type-tag', getTypeClass(passenger.passengerType)]">
                {{ getPassengerTypeName(passenger.passengerType) }}
              </span>
              <span v-if="passenger.nationality" class="nationality-tag">
                {{ passenger.nationality }}
              </span>
            </div>
            <div class="id-info">
              <span class="id-type">{{ getIdTypeName(passenger.idType) }}</span>
              <span class="id-card">{{ maskIdCard(passenger.idCard) }}</span>
            </div>
          </div>
          <div class="item-action">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="2">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && passengers.length === 0 && !currentUser" class="empty-state">
        <div class="empty-icon">
          <svg width="80" height="80" viewBox="0 0 120 100" fill="none">
            <rect x="20" y="20" width="80" height="60" rx="4" fill="#e8f4fd" stroke="#1890ff" stroke-width="2"/>
            <circle cx="45" cy="45" r="10" fill="#1890ff" opacity="0.3"/>
            <path d="M35 65c0-5.5 4.5-10 10-10s10 4.5 10 10" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
            <line x1="60" y1="35" x2="80" y2="35" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
            <line x1="60" y1="45" x2="75" y2="45" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
            <line x1="60" y1="55" x2="70" y2="55" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <p class="empty-title">暂无乘车人</p>
        <p class="empty-desc">添加常用联系人，购票更便捷</p>
        <el-button type="primary" round @click="showAddDialog">立即添加</el-button>
      </div>
    </main>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="null"
      width="100%"
      :show-close="false"
      :close-on-click-modal="false"
      class="add-passenger-dialog"
      append-to-body
    >
      <!-- 自定义标题栏 -->
      <div class="dialog-header">
        <button class="back-btn" @click="dialogVisible = false">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
            <polyline points="15,18 9,12 15,6"/>
          </svg>
        </button>
        <h2 class="dialog-title">{{ isEdit ? '编辑乘车人' : '添加乘车人' }}</h2>
        <button v-if="isEdit" class="delete-btn" @click="handleDeleteConfirm">删除</button>
      </div>

      <div class="dialog-body">
        <!-- 扫描/上传证件快速填写（仅添加模式显示） -->
        <div v-if="!isEdit" class="scan-section">
          <div class="scan-header">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#1890ff" stroke-width="2">
              <path d="M3 7V5a2 2 0 012-2h2"/>
              <path d="M17 3h2a2 2 0 012 2v2"/>
              <path d="M21 17v2a2 2 0 01-2 2h-2"/>
              <path d="M7 21H5a2 2 0 01-2-2v-2"/>
              <line x1="7" y1="12" x2="17" y2="12"/>
            </svg>
            <span class="scan-title">扫描/上传证件快速填写</span>
            <span class="recommend-tag">推荐</span>
          </div>
          <p class="scan-desc">
            您可以通过扫描证件或上传证件照片快速填写信息，建议您使用此方式自动填写。
            <a href="#" @click.prevent>查看示例</a>
          </p>
        </div>

        <!-- 表单区域 -->
        <el-form ref="formRef" :model="passengerForm" :rules="rules" class="passenger-form">
          <!-- 证件类型 -->
          <div class="form-row">
            <label class="form-label">证件类型</label>
            <div class="form-control" v-if="isEdit">
              <span>{{ getIdTypeName(passengerForm.idType) }}</span>
            </div>
            <div v-else class="form-control select-control" @click="showIdTypePicker = true">
              <span>{{ getIdTypeName(passengerForm.idType) }}</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
                <polyline points="9,18 15,12 9,6"/>
              </svg>
            </div>
          </div>

          <!-- 查看填写示例（仅复杂证件类型显示） -->
          <div v-if="!isEdit && needComplexFields" class="example-link">
            <a href="#" @click.prevent>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#ff9500" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/>
                <line x1="12" y1="17" x2="12.01" y2="17"/>
              </svg>
              查看填写示例
            </a>
          </div>

          <!-- 国籍（仅外国人永久居留身份证和外国护照显示） -->
          <div v-if="!isEdit && showNationality" class="form-row">
            <label class="form-label">
              国 籍<br/>
              <span class="sub-label">(国家/地区)</span>
            </label>
            <div class="form-control select-control" @click="showNationalityPicker = true">
              <span>{{ passengerForm.nationality || '请选择国家/地区' }}</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
                <polyline points="9,18 15,12 9,6"/>
              </svg>
            </div>
          </div>

          <!-- 姓名 -->
          <div class="form-row">
            <label class="form-label">
              姓 名
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#1890ff" stroke-width="2" class="help-icon">
                <circle cx="12" cy="12" r="10"/>
                <path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/>
                <line x1="12" y1="17" x2="12.01" y2="17"/>
              </svg>
            </label>
            <div class="form-control" v-if="isEdit">
              <span>{{ passengerForm.name }}</span>
              <span class="verified-tag">已通过</span>
            </div>
            <el-input
              v-else
              v-model="passengerForm.name"
              :placeholder="getPlaceholder('name')"
              maxlength="50"
              class="custom-input"
            />
          </div>

          <!-- 证件号码 -->
          <div class="form-row">
            <label class="form-label">证件号码</label>
            <div class="form-control" v-if="isEdit">
              <span>{{ maskIdCard(passengerForm.idCard) }}</span>
            </div>
            <el-input
              v-else
              v-model="passengerForm.idCard"
              placeholder="用于身份核验，请正确填写"
              maxlength="30"
              class="custom-input"
            />
          </div>

          <!-- 出生日期（仅复杂证件类型显示） -->
          <div v-if="!isEdit && needComplexFields" class="form-row">
            <label class="form-label">出生日期</label>
            <div class="form-control select-control" @click="showBirthDatePicker = true">
              <span>{{ passengerForm.birthDate || '请选择出生日期' }}</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
                <polyline points="9,18 15,12 9,6"/>
              </svg>
            </div>
          </div>

          <!-- 性别（仅复杂证件类型显示） -->
          <div v-if="!isEdit && needComplexFields" class="form-row gender-row">
            <label class="form-label">性 别</label>
            <div class="gender-options">
              <label class="gender-option">
                <input type="radio" v-model="passengerForm.gender" value="male"/>
                <span class="radio-dot"></span>
                <span>男</span>
              </label>
              <label class="gender-option">
                <input type="radio" v-model="passengerForm.gender" value="female"/>
                <span class="radio-dot"></span>
                <span>女</span>
              </label>
            </div>
          </div>

          <!-- 有效期至（仅复杂证件类型显示） -->
          <div v-if="!isEdit && needComplexFields" class="form-row">
            <label class="form-label">有效期至</label>
            <div class="form-control select-control" @click="showExpireDatePicker = true">
              <span>{{ passengerForm.expireDate || '请选择证件有效期截止日期' }}</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
                <polyline points="9,18 15,12 9,6"/>
              </svg>
            </div>
          </div>

          <!-- 优惠(待)类型 -->
          <div class="form-row">
            <label class="form-label">优惠(待)类型</label>
            <div class="form-control select-control" @click="showTypePicker = true">
              <span>{{ getTypeName(passengerForm.passengerType) }}</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
                <polyline points="9,18 15,12 9,6"/>
              </svg>
            </div>
          </div>

          <!-- 联系方式 -->
          <div class="section-title">联系方式{{ showEmail ? '（二选一）' : '' }}</div>

          <!-- 手机号码 -->
          <div class="form-row phone-row">
            <label class="form-label">手机号码</label>
            <div class="phone-input-wrapper">
              <span class="phone-prefix">+86</span>
              <span class="phone-divider">|</span>
              <input
                v-model="passengerForm.phone"
                type="tel"
                :placeholder="isEdit ? '' : '请填写乘车人手机号'"
                maxlength="11"
                class="phone-input"
                :readonly="isEdit"
              />
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="2" class="contact-icon">
                <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
            </div>
          </div>

          <!-- 电子邮箱（仅外国护照显示） -->
          <div v-if="!isEdit && showEmail" class="form-row">
            <label class="form-label">电子邮箱</label>
            <el-input
              v-model="passengerForm.email"
              placeholder="请填写您的电子邮箱"
              maxlength="50"
              class="custom-input"
            />
          </div>

          <!-- 提示文字 -->
          <div class="warning-tip">
            <template v-if="showEmail">
              请在手机号和电子邮箱中任选一个进行核验，联系方式核验通过后方可购票
            </template>
            <template v-else>
              请您填写乘车人真实有效的联系方式，以便接收铁路部门推送的重要服务信息，以及在紧急特殊情况下的联系。
            </template>
          </div>

          <!-- 提交按钮 -->
          <button
            type="button"
            class="submit-btn"
            @click="handleSubmit"
            :disabled="submitting"
          >
            {{ submitting ? '提交中...' : (isEdit ? '提交' : '下一步') }}
          </button>

          <!-- 温馨提示 -->
          <div class="tips-section">
            <div class="tips-title">温馨提示：</div>
            <ol class="tips-list">
              <li>请提供乘车人真实有效的联系方式。对于未成年人、老年人等重点旅客以及无手机的旅客，可提供监护人或能及时联系的亲友手机号码。</li>
              <li>互联网售票实行实名制，请准确填写乘车人基本信息。</li>
              <li>如旅客身份信息未能在添加后的24小时内通过核验，请乘车人持有效身份证件原件到车站办理身份核验。</li>
              <li>乘车人在添加后30天内不允许删除，每个账号最多允许添加30个乘客。</li>
            </ol>
          </div>
        </el-form>
      </div>
    </el-dialog>

    <!-- 证件类型选择弹窗 -->
    <div v-if="showIdTypePicker" class="picker-overlay" @click="showIdTypePicker = false">
      <div class="picker-modal" @click.stop>
        <div class="picker-header">
          <span class="picker-title">证件类型</span>
          <button class="picker-close" @click="showIdTypePicker = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="picker-content">
          <div
            v-for="item in idTypeOptions"
            :key="item.value"
            class="picker-item"
            :class="{ active: passengerForm.idType === item.value }"
            @click="selectIdType(item.value)"
          >
            <span class="item-label">{{ item.label }}</span>
            <div class="radio-wrapper">
              <svg v-if="passengerForm.idType === item.value" width="20" height="20" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="#1890ff" stroke-width="2"/>
                <circle cx="12" cy="12" r="5" fill="#1890ff"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="#ccc" stroke-width="2"/>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 优惠类型选择弹窗 -->
    <div v-if="showTypePicker" class="picker-overlay" @click="showTypePicker = false">
      <div class="picker-modal" @click.stop>
        <div class="picker-header">
          <span class="picker-title">优惠(待)类型</span>
          <button class="picker-close" @click="showTypePicker = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="picker-content">
          <div
            v-for="item in passengerTypeOptions"
            :key="item.value"
            class="picker-item"
            :class="{ active: passengerForm.passengerType === item.value }"
            @click="selectPassengerType(item.value)"
          >
            <span class="item-label">{{ item.label }}</span>
            <div class="radio-wrapper">
              <svg v-if="passengerForm.passengerType === item.value" width="20" height="20" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="#1890ff" stroke-width="2"/>
                <circle cx="12" cy="12" r="5" fill="#1890ff"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="#ccc" stroke-width="2"/>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 出生日期选择器 -->
    <div v-if="showBirthDatePicker" class="date-picker-overlay" @click="showBirthDatePicker = false">
      <div class="date-picker-modal" @click.stop>
        <div class="date-picker-header">
          <button class="date-picker-btn cancel-btn" @click="showBirthDatePicker = false">取消</button>
          <span class="date-picker-title">选择出生日期</span>
          <button class="date-picker-btn confirm-btn" @click="confirmBirthDate">确定</button>
        </div>
        <div class="date-picker-wheels">
          <div class="wheel-column" ref="birthYearWheel">
            <div
              v-for="(year, index) in yearOptions"
              :key="'by-' + year"
              class="wheel-item"
              :class="{ selected: tempBirthDate.year === year }"
              @click="selectWheelItem('birth', 'year', year)"
            >
              {{ year }}年
            </div>
          </div>
          <div class="wheel-column" ref="birthMonthWheel">
            <div
              v-for="(month, index) in monthOptions"
              :key="'bm-' + month"
              class="wheel-item"
              :class="{ selected: tempBirthDate.month === month }"
              @click="selectWheelItem('birth', 'month', month)"
            >
              {{ month }}月
            </div>
          </div>
          <div class="wheel-column" ref="birthDayWheel">
            <div
              v-for="(day, index) in birthDayOptions"
              :key="'bd-' + day"
              class="wheel-item"
              :class="{ selected: tempBirthDate.day === day }"
              @click="selectWheelItem('birth', 'day', day)"
            >
              {{ day }}日
            </div>
          </div>
        </div>
        <div class="wheel-indicator"></div>
      </div>
    </div>

    <!-- 有效期截止日期选择器 -->
    <div v-if="showExpireDatePicker" class="date-picker-overlay" @click="showExpireDatePicker = false">
      <div class="date-picker-modal" @click.stop>
        <div class="date-picker-header">
          <button class="date-picker-btn cancel-btn" @click="showExpireDatePicker = false">取消</button>
          <span class="date-picker-title">选择有效期至</span>
          <button class="date-picker-btn confirm-btn" @click="confirmExpireDate">确定</button>
        </div>
        <div class="date-picker-wheels">
          <div class="wheel-column" ref="expireYearWheel">
            <div
              v-for="(year, index) in expireYearOptions"
              :key="'ey-' + year"
              class="wheel-item"
              :class="{ selected: tempExpireDate.year === year }"
              @click="selectWheelItem('expire', 'year', year)"
            >
              {{ year }}年
            </div>
          </div>
          <div class="wheel-column" ref="expireMonthWheel">
            <div
              v-for="(month, index) in monthOptions"
              :key="'em-' + month"
              class="wheel-item"
              :class="{ selected: tempExpireDate.month === month }"
              @click="selectWheelItem('expire', 'month', month)"
            >
              {{ month }}月
            </div>
          </div>
          <div class="wheel-column" ref="expireDayWheel">
            <div
              v-for="(day, index) in expireDayOptions"
              :key="'ed-' + day"
              class="wheel-item"
              :class="{ selected: tempExpireDate.day === day }"
              @click="selectWheelItem('expire', 'day', day)"
            >
              {{ day }}日
            </div>
          </div>
        </div>
        <div class="wheel-indicator"></div>
      </div>
    </div>

    <!-- 国家/地区选择器 -->
    <div v-if="showNationalityPicker" class="nationality-picker-overlay" @click="showNationalityPicker = false">
      <div class="nationality-picker-modal" @click.stop>
        <div class="nationality-picker-header">
          <div class="search-box">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2" class="search-icon">
              <circle cx="11" cy="11" r="8"/>
              <path d="M21 21l-4.35-4.35"/>
            </svg>
            <input
              v-model="nationalitySearchKeyword"
              type="text"
              placeholder="请输入国家名称/拼音"
              class="search-input"
              ref="nationalitySearchInput"
            />
          </div>
          <button class="cancel-search-btn" @click="showNationalityPicker = false">取消</button>
        </div>
        <div class="nationality-list">
          <div
            v-for="country in filteredCountries"
            :key="country.code"
            class="nationality-item"
            :class="{ selected: passengerForm.nationality === country.name }"
            @click="selectNationality(country.name)"
          >
            <span class="country-name">{{ country.name }}</span>
            <span class="country-en">{{ country.en }}</span>
          </div>
          <div v-if="filteredCountries.length === 0" class="no-result">
            未找到匹配的国家/地区
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { passengerApi, authApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

interface Passenger {
  id: number
  name: string
  idType: string
  idCard: string
  passengerType: number
  phone: string
  nationality?: string
  birthDate?: string
  gender?: string
  expireDate?: string
  email?: string
  isVerified: number
  isDefault?: number
}

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const passengers = ref<Passenger[]>([])
const currentUser = ref<Passenger | null>(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInstance>()

const passengerForm = ref({
  name: '',
  idType: 'ID_CARD',
  idCard: '',
  passengerType: 1,
  phone: '',
  nationality: '',
  birthDate: '',
  gender: '',
  expireDate: '',
  email: ''
})

const showIdTypePicker = ref(false)
const showTypePicker = ref(false)
const showNationalityPicker = ref(false)
const showBirthDatePicker = ref(false)
const showExpireDatePicker = ref(false)
const nationalitySearchKeyword = ref('')

const countries = [
  { code: 'AF', name: '阿富汗', en: 'Afghanistan' },
  { code: 'AL', name: '阿尔巴尼亚', en: 'Albania' },
  { code: 'DZ', name: '阿尔及利亚', en: 'Algeria' },
  { code: 'AD', name: '安道尔', en: 'Andorra' },
  { code: 'AO', name: '安哥拉', en: 'Angola' },
  { code: 'AG', name: '安提瓜和巴布达', en: 'Antigua and Barbuda' },
  { code: 'AR', name: '阿根廷', en: 'Argentina' },
  { code: 'AM', name: '亚美尼亚', en: 'Armenia' },
  { code: 'AU', name: '澳大利亚', en: 'Australia' },
  { code: 'AT', name: '奥地利', en: 'Austria' },
  { code: 'AZ', name: '阿塞拜疆', en: 'Azerbaijan' },
  { code: 'BS', name: '巴哈马', en: 'Bahamas' },
  { code: 'BH', name: '巴林', en: 'Bahrain' },
  { code: 'BD', name: '孟加拉国', en: 'Bangladesh' },
  { code: 'BB', name: '巴巴多斯', en: 'Barbados' },
  { code: 'BY', name: '白俄罗斯', en: 'Belarus' },
  { code: 'BE', name: '比利时', en: 'Belgium' },
  { code: 'BZ', name: '伯利兹', en: 'Belize' },
  { code: 'BJ', name: '贝宁', en: 'Benin' },
  { code: 'BT', name: '不丹', en: 'Bhutan' },
  { code: 'BO', name: '玻利维亚', en: 'Bolivia' },
  { code: 'BA', name: '波斯尼亚和黑塞哥维那', en: 'Bosnia and Herzegovina' },
  { code: 'BW', name: '博茨瓦纳', en: 'Botswana' },
  { code: 'BR', name: '巴西', en: 'Brazil' },
  { code: 'BN', name: '文莱', en: 'Brunei' },
  { code: 'BG', name: '保加利亚', en: 'Bulgaria' },
  { code: 'BF', name: '布基纳法索', en: 'Burkina Faso' },
  { code: 'BI', name: '布隆迪', en: 'Burundi' },
  { code: 'KH', name: '柬埔寨', en: 'Cambodia' },
  { code: 'CM', name: '喀麦隆', en: 'Cameroon' },
  { code: 'CA', name: '加拿大', en: 'Canada' },
  { code: 'CV', name: '佛得角', en: 'Cape Verde' },
  { code: 'CF', name: '中非共和国', en: 'Central African Republic' },
  { code: 'TD', name: '乍得', en: 'Chad' },
  { code: 'CL', name: '智利', en: 'Chile' },
  { code: 'CN', name: '中国', en: 'China' },
  { code: 'CO', name: '哥伦比亚', en: 'Colombia' },
  { code: 'KM', name: '科摩罗', en: 'Comoros' },
  { code: 'CG', name: '刚果(布)', en: 'Congo (Brazzaville)' },
  { code: 'CD', name: '刚果(金)', en: 'Congo (Kinshasa)' },
  { code: 'CR', name: '哥斯达黎加', en: 'Costa Rica' },
  { code: 'CI', name: '科特迪瓦', en: "Côte d'Ivoire" },
  { code: 'HR', name: '克罗地亚', en: 'Croatia' },
  { code: 'CU', name: '古巴', en: 'Cuba' },
  { code: 'CY', name: '塞浦路斯', en: 'Cyprus' },
  { code: 'CZ', name: '捷克', en: 'Czech Republic' },
  { code: 'DK', name: '丹麦', en: 'Denmark' },
  { code: 'DJ', name: '吉布提', en: 'Djibouti' },
  { code: 'DM', name: '多米尼克', en: 'Dominica' },
  { code: 'DO', name: '多米尼加共和国', en: 'Dominican Republic' },
  { code: 'EC', name: '厄瓜多尔', en: 'Ecuador' },
  { code: 'EG', name: '埃及', en: 'Egypt' },
  { code: 'SV', name: '萨尔瓦多', en: 'El Salvador' },
  { code: 'GQ', name: '赤道几内亚', en: 'Equatorial Guinea' },
  { code: 'ER', name: '厄立特里亚', en: 'Eritrea' },
  { code: 'EE', name: '爱沙尼亚', en: 'Estonia' },
  { code: 'SZ', name: '斯威士兰', en: 'Eswatini' },
  { code: 'ET', name: '埃塞俄比亚', en: 'Ethiopia' },
  { code: 'FJ', name: '斐济', en: 'Fiji' },
  { code: 'FI', name: '芬兰', en: 'Finland' },
  { code: 'FR', name: '法国', en: 'France' },
  { code: 'GA', name: '加蓬', en: 'Gabon' },
  { code: 'GM', name: '冈比亚', en: 'Gambia' },
  { code: 'GE', name: '格鲁吉亚', en: 'Georgia' },
  { code: 'DE', name: '德国', en: 'Germany' },
  { code: 'GH', name: '加纳', en: 'Ghana' },
  { code: 'GR', name: '希腊', en: 'Greece' },
  { code: 'GD', name: '格林纳达', en: 'Grenada' },
  { code: 'GT', name: '危地马拉', en: 'Guatemala' },
  { code: 'GN', name: '几内亚', en: 'Guinea' },
  { code: 'GW', name: '几内亚比绍', en: 'Guinea-Bissau' },
  { code: 'GY', name: '圭亚那', en: 'Guyana' },
  { code: 'HT', name: '海地', en: 'Haiti' },
  { code: 'HN', name: '洪都拉斯', en: 'Honduras' },
  { code: 'HU', name: '匈牙利', en: 'Hungary' },
  { code: 'IS', name: '冰岛', en: 'Iceland' },
  { code: 'IN', name: '印度', en: 'India' },
  { code: 'ID', name: '印度尼西亚', en: 'Indonesia' },
  { code: 'IR', name: '伊朗', en: 'Iran' },
  { code: 'IQ', name: '伊拉克', en: 'Iraq' },
  { code: 'IE', name: '爱尔兰', en: 'Ireland' },
  { code: 'IL', name: '以色列', en: 'Israel' },
  { code: 'IT', name: '意大利', en: 'Italy' },
  { code: 'JM', name: '牙买加', en: 'Jamaica' },
  { code: 'JP', name: '日本', en: 'Japan' },
  { code: 'JO', name: '约旦', en: 'Jordan' },
  { code: 'KZ', name: '哈萨克斯坦', en: 'Kazakhstan' },
  { code: 'KE', name: '肯尼亚', en: 'Kenya' },
  { code: 'KI', name: '基里巴斯', en: 'Kiribati' },
  { code: 'KP', name: '朝鲜', en: "Korea (North)" },
  { code: 'KR', name: '韩国', en: 'Korea (South)' },
  { code: 'KW', name: '科威特', en: 'Kuwait' },
  { code: 'KG', name: '吉尔吉斯斯坦', en: 'Kyrgyzstan' },
  { code: 'LA', name: '老挝', en: 'Laos' },
  { code: 'LV', name: '拉脱维亚', en: 'Latvia' },
  { code: 'LB', name: '黎巴嫩', en: 'Lebanon' },
  { code: 'LS', name: '莱索托', en: 'Lesotho' },
  { code: 'LR', name: '利比里亚', en: 'Liberia' },
  { code: 'LY', name: '利比亚', en: 'Libya' },
  { code: 'LI', name: '列支敦士登', en: 'Liechtenstein' },
  { code: 'LT', name: '立陶宛', en: 'Lithuania' },
  { code: 'LU', name: '卢森堡', en: 'Luxembourg' },
  { code: 'MG', name: '马达加斯加', en: 'Madagascar' },
  { code: 'MW', name: '马拉维', en: 'Malawi' },
  { code: 'MY', name: '马来西亚', en: 'Malaysia' },
  { code: 'MV', name: '马尔代夫', en: 'Maldives' },
  { code: 'ML', name: '马里', en: 'Mali' },
  { code: 'MT', name: '马耳他', en: 'Malta' },
  { code: 'MH', name: '马绍尔群岛', en: 'Marshall Islands' },
  { code: 'MR', name: '毛里塔尼亚', en: 'Mauritania' },
  { code: 'MU', name: '毛里求斯', en: 'Mauritius' },
  { code: 'MX', name: '墨西哥', en: 'Mexico' },
  { code: 'FM', name: '密克罗尼西亚', en: 'Micronesia' },
  { code: 'MD', name: '摩尔多瓦', en: 'Moldova' },
  { code: 'MC', name: '摩纳哥', en: 'Monaco' },
  { code: 'MN', name: '蒙古', en: 'Mongolia' },
  { code: 'ME', name: '黑山', en: 'Montenegro' },
  { code: 'MA', name: '摩洛哥', en: 'Morocco' },
  { code: 'MZ', name: '莫桑比克', en: 'Mozambique' },
  { code: 'MM', name: '缅甸', en: 'Myanmar' },
  { code: 'NA', name: '纳米比亚', en: 'Namibia' },
  { code: 'NR', name: '瑙鲁', en: 'Nauru' },
  { code: 'NP', name: '尼泊尔', en: 'Nepal' },
  { code: 'NL', name: '荷兰', en: 'Netherlands' },
  { code: 'NZ', name: '新西兰', en: 'New Zealand' },
  { code: 'NI', name: '尼加拉瓜', en: 'Nicaragua' },
  { code: 'NE', name: '尼日尔', en: 'Niger' },
  { code: 'NG', name: '尼日利亚', en: 'Nigeria' },
  { code: 'MK', name: '北马其顿', en: 'North Macedonia' },
  { code: 'NO', name: '挪威', en: 'Norway' },
  { code: 'OM', name: '阿曼', en: 'Oman' },
  { code: 'PK', name: '巴基斯坦', en: 'Pakistan' },
  { code: 'PW', name: '帕劳', en: 'Palau' },
  { code: 'PS', name: '巴勒斯坦', en: 'Palestine' },
  { code: 'PA', name: '巴拿马', en: 'Panama' },
  { code: 'PG', name: '巴布亚新几内亚', en: 'Papua New Guinea' },
  { code: 'PY', name: '巴拉圭', en: 'Paraguay' },
  { code: 'PE', name: '秘鲁', en: 'Peru' },
  { code: 'PH', name: '菲律宾', en: 'Philippines' },
  { code: 'PL', name: '波兰', en: 'Poland' },
  { code: 'PT', name: '葡萄牙', en: 'Portugal' },
  { code: 'QA', name: '卡塔尔', en: 'Qatar' },
  { code: 'RO', name: '罗马尼亚', en: 'Romania' },
  { code: 'RU', name: '俄罗斯', en: 'Russia' },
  { code: 'RW', name: '卢旺达', en: 'Rwanda' },
  { code: 'KN', name: '圣基茨和尼维斯', en: 'Saint Kitts and Nevis' },
  { code: 'LC', name: '圣卢西亚', en: 'Saint Lucia' },
  { code: 'VC', name: '圣文森特和格林纳丁斯', en: 'Saint Vincent and the Grenadines' },
  { code: 'WS', name: '萨摩亚', en: 'Samoa' },
  { code: 'SM', name: '圣马力诺', en: 'San Marino' },
  { code: 'ST', name: '圣多美和普林西比', en: 'São Tomé and Príncipe' },
  { code: 'SA', name: '沙特阿拉伯', en: 'Saudi Arabia' },
  { code: 'SN', name: '塞内加尔', en: 'Senegal' },
  { code: 'RS', name: '塞尔维亚', en: 'Serbia' },
  { code: 'SC', name: '塞舌尔', en: 'Seychelles' },
  { code: 'SL', name: '塞拉利昂', en: 'Sierra Leone' },
  { code: 'SG', name: '新加坡', en: 'Singapore' },
  { code: 'SK', name: '斯洛伐克', en: 'Slovakia' },
  { code: 'SI', name: '斯洛文尼亚', en: 'Slovenia' },
  { code: 'SB', name: '所罗门群岛', en: 'Solomon Islands' },
  { code: 'SO', name: '索马里', en: 'Somalia' },
  { code: 'ZA', name: '南非', en: 'South Africa' },
  { code: 'SS', name: '南苏丹', en: 'South Sudan' },
  { code: 'ES', name: '西班牙', en: 'Spain' },
  { code: 'LK', name: '斯里兰卡', en: 'Sri Lanka' },
  { code: 'SD', name: '苏丹', en: 'Sudan' },
  { code: 'SR', name: '苏里南', en: 'Suriname' },
  { code: 'SE', name: '瑞典', en: 'Sweden' },
  { code: 'CH', name: '瑞士', en: 'Switzerland' },
  { code: 'SY', name: '叙利亚', en: 'Syria' },
  { code: 'TW', name: '中国台湾', en: 'Taiwan' },
  { code: 'TJ', name: '塔吉克斯坦', en: 'Tajikistan' },
  { code: 'TZ', name: '坦桑尼亚', en: 'Tanzania' },
  { code: 'TH', name: '泰国', en: 'Thailand' },
  { code: 'TL', name: '东帝汶', en: 'Timor-Leste' },
  { code: 'TG', name: '多哥', en: 'Togo' },
  { code: 'TO', name: '汤加', en: 'Tonga' },
  { code: 'TT', name: '特立尼达和多巴哥', en: 'Trinidad and Tobago' },
  { code: 'TN', name: '突尼斯', en: 'Tunisia' },
  { code: 'TR', name: '土耳其', en: 'Turkey' },
  { code: 'TM', name: '土库曼斯坦', en: 'Turkmenistan' },
  { code: 'TV', name: '图瓦卢', en: 'Tuvalu' },
  { code: 'UG', name: '乌干达', en: 'Uganda' },
  { code: 'UA', name: '乌克兰', en: 'Ukraine' },
  { code: 'AE', name: '阿拉伯联合酋长国', en: 'United Arab Emirates' },
  { code: 'GB', name: '英国', en: 'United Kingdom' },
  { code: 'US', name: '美国', en: 'United States' },
  { code: 'UY', name: '乌拉圭', en: 'Uruguay' },
  { code: 'UZ', name: '乌兹别克斯坦', en: 'Uzbekistan' },
  { code: 'VU', name: '瓦努阿图', en: 'Vanuatu' },
  { code: 'VA', name: '梵蒂冈', en: 'Vatican City' },
  { code: 'VE', name: '委内瑞拉', en: 'Venezuela' },
  { code: 'VN', name: '越南', en: 'Vietnam' },
  { code: 'YE', name: '也门', en: 'Yemen' },
  { code: 'ZM', name: '赞比亚', en: 'Zambia' },
  { code: 'ZW', name: '津巴布韦', en: 'Zimbabwe' },
  { code: 'HK', name: '中国香港', en: 'Hong Kong, China' },
  { code: 'MO', name: '中国澳门', en: 'Macau, China' }
]

const filteredCountries = computed(() => {
  if (!nationalitySearchKeyword.value) {
    return countries
  }
  const keyword = nationalitySearchKeyword.value.toLowerCase()
  return countries.filter(country => 
    country.name.includes(keyword) || 
    country.en.toLowerCase().includes(keyword) ||
    country.name.toLowerCase().includes(keyword)
  )
})

const currentYear = new Date().getFullYear()

const yearOptions = computed(() => {
  const years = []
  for (let y = currentYear - 100; y <= currentYear; y++) {
    years.push(y)
  }
  return years
})

const expireYearOptions = computed(() => {
  const years = []
  for (let y = currentYear; y <= currentYear + 20; y++) {
    years.push(y)
  }
  return years
})

const monthOptions = computed(() => {
  return Array.from({ length: 12 }, (_, i) => i + 1)
})

const tempBirthDate = ref({
  year: currentYear - 20,
  month: 1,
  day: 1
})

const tempExpireDate = ref({
  year: currentYear + 10,
  month: 1,
  day: 1
})

const birthDayOptions = computed(() => {
  const daysInMonth = new Date(tempBirthDate.value.year, tempBirthDate.value.month, 0).getDate()
  return Array.from({ length: daysInMonth }, (_, i) => i + 1)
})

const expireDayOptions = computed(() => {
  const daysInMonth = new Date(tempExpireDate.value.year, tempExpireDate.value.month, 0).getDate()
  return Array.from({ length: daysInMonth }, (_, i) => i + 1)
})

// 需要复杂字段的证件类型（后5种）
const complexIdTypes = ['FOREIGN_PERMANENT', 'HK_MACAO_PASS', 'TAIWAN_PASS', 'CHINA_PASSPORT', 'FOREIGN_PASSPORT']

// 需要显示国籍的证件类型
const nationalityIdTypes = ['FOREIGN_PERMANENT', 'FOREIGN_PASSPORT']

// 需要显示邮箱的证件类型
const emailIdTypes = ['FOREIGN_PASSPORT']

// 是否需要复杂字段
const needComplexFields = computed(() => {
  return complexIdTypes.includes(passengerForm.value.idType)
})

// 是否显示国籍
const showNationality = computed(() => {
  return nationalityIdTypes.includes(passengerForm.value.idType)
})

// 是否显示邮箱
const showEmail = computed(() => {
  return emailIdTypes.includes(passengerForm.value.idType)
})

// 根据证件类型获取姓名占位符
const getPlaceholder = (field: string) => {
  const idType = passengerForm.value.idType
  if (field === 'name') {
    if (idType === 'FOREIGN_PASSPORT') {
      return '外国护照按姓名顺序填写英文姓名'
    } else if (idType === 'CHINA_PASSPORT') {
      return '中国护照用户请填写中文姓名'
    } else if (['HK_MACAO_PASS', 'TAIWAN_PASS'].includes(idType)) {
      return '请输入真实姓名，以便购票'
    }
    return '请填写真实姓名'
  }
  return ''
}

const idTypeOptions = [
  { label: '中国居民身份证', value: 'ID_CARD' },
  { label: '港澳居民居住证', value: 'HK_MACAO_RESIDENCE' },
  { label: '台湾居民居住证', value: 'TAIWAN_RESIDENCE' },
  { label: '外国人永久居留身份证', value: 'FOREIGN_PERMANENT' },
  { label: '港澳居民来往内地通行证(含非中国籍)', value: 'HK_MACAO_PASS' },
  { label: '台湾居民来往大陆通行证', value: 'TAIWAN_PASS' },
  { label: '中国护照', value: 'CHINA_PASSPORT' },
  { label: '外国护照', value: 'FOREIGN_PASSPORT' }
]

const passengerTypeOptions = [
  { label: '成人', value: 1 },
  { label: '儿童', value: 2 },
  { label: '学生', value: 3 },
  { label: '残疾军人', value: 4 }
]

const rules: FormRules = {
  name: [
    { required: true, message: '请输入乘车人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入证件号码', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadCurrentUser()
  loadPassengers()
})

// Mock当前用户数据
const mockCurrentUser: Passenger = {
  id: 0,
  name: '徐明洋',
  idType: 'ID_CARD',
  idCard: '411628199501011237',
  passengerType: 3,
  phone: '13812345678',
  isVerified: 1,
  isDefault: 1
}

// Mock乘车人列表数据 - 包含所有证件类型
const mockPassengersList: Passenger[] = [
  // 中国居民身份证
  { id: 1, name: '张三', idType: 'ID_CARD', idCard: '110101199001011234', passengerType: 1, phone: '13800138001', isVerified: 1, isDefault: 0 },
  { id: 2, name: '李小红', idType: 'ID_CARD', idCard: '310101199505055678', passengerType: 2, phone: '13800138002', isVerified: 1, isDefault: 0 },
  { id: 3, name: '王建国', idType: 'ID_CARD', idCard: '440101198806015432', passengerType: 1, phone: '13800138003', isVerified: 1, isDefault: 0 },
  { id: 4, name: '陈思思', idType: 'ID_CARD', idCard: '510101200101013456', passengerType: 3, phone: '13800138004', isVerified: 0, isDefault: 0 },
  { id: 5, name: '刘伟', idType: 'ID_CARD', idCard: '320101198505201234', passengerType: 4, phone: '13800138005', isVerified: 1, isDefault: 0 },

  // 港澳居民居住证
  { id: 6, name: '陳大文', idType: 'HK_MACAO_RESIDENCE', idCard: '830123199001011234', passengerType: 1, phone: '13912340001', nationality: '中国香港', isVerified: 1, isDefault: 0 },

  // 台湾居民居住证
  { id: 7, name: '王小明', idType: 'TAIWAN_RESIDENCE', idCard: '830123199501015678', passengerType: 1, phone: '13912340002', nationality: '中国台湾', isVerified: 1, isDefault: 0 },

  // 外国人永久居留身份证
  { id: 8, name: 'Michael Johnson', idType: 'FOREIGN_PERMANENT', idCard: 'USA123456789012', passengerType: 1, phone: '13912340003', nationality: '美国', isVerified: 1, isDefault: 0 },

  // 港澳居民来往内地通行证
  { id: 9, name: '何小美', idType: 'HK_MACAO_PASS', idCard: 'H12345678', passengerType: 1, phone: '13912340004', nationality: '中国香港', isVerified: 1, isDefault: 0 },

  // 台湾居民来往大陆通行证
  { id: 10, name: '林小明', idType: 'TAIWAN_PASS', idCard: 'T12345678', passengerType: 1, phone: '13912340005', nationality: '中国台湾', isVerified: 1, isDefault: 0 },

  // 中国护照
  { id: 11, name: '李明', idType: 'CHINA_PASSPORT', idCard: 'E12345678', passengerType: 1, phone: '13912340006', isVerified: 1, isDefault: 0 },

  // 外国护照
  { id: 12, name: 'John Smith', idType: 'FOREIGN_PASSPORT', idCard: 'AB1234567', passengerType: 1, phone: '13912340007', nationality: '美国', birthDate: '1990-01-01', gender: 'male', expireDate: '2030-12-31', email: 'johnsmith@email.com', isVerified: 1, isDefault: 0 },
  { id: 13, name: '田中太郎', idType: 'FOREIGN_PASSPORT', idCard: 'JP9876543', passengerType: 1, phone: '13912340008', nationality: '日本', isVerified: 1, isDefault: 0 },
  { id: 14, name: 'Maria Garcia', idType: 'FOREIGN_PASSPORT', idCard: 'ES2468135', passengerType: 1, phone: '13912340009', nationality: '西班牙', isVerified: 1, isDefault: 0 },
]

let mockId = 15

const loadCurrentUser = async () => {
  // 使用Mock数据
  await new Promise(resolve => setTimeout(resolve, 300))
  currentUser.value = { ...mockCurrentUser }
}

const loadPassengers = async () => {
  loading.value = true
  // 使用Mock数据
  await new Promise(resolve => setTimeout(resolve, 500))
  passengers.value = [...mockPassengersList]
  loading.value = false
}

const groupedPassengers = computed(() => {
  const groups: { letter: string; list: Passenger[] }[] = []
  const sorted = [...passengers.value].sort((a, b) => a.name.localeCompare(b.name, 'zh-CN'))

  let currentLetter = ''
  let currentList: Passenger[] = []

  sorted.forEach(p => {
    const letter = p.name.charAt(0).toUpperCase()
    if (letter !== currentLetter) {
      if (currentList.length > 0) {
        groups.push({ letter: currentLetter, list: currentList })
      }
      currentLetter = letter
      currentList = [p]
    } else {
      currentList.push(p)
    }
  })

  if (currentList.length > 0) {
    groups.push({ letter: currentLetter, list: currentList })
  }

  return groups
})

const goBack = () => {
  router.push('/user')
}

const showAddDialog = () => {
  isEdit.value = false
  editingId.value = null
  passengerForm.value = {
    name: '',
    idType: 'ID_CARD',
    idCard: '',
    passengerType: 1,
    phone: '',
    nationality: '',
    birthDate: '',
    gender: '',
    expireDate: '',
    email: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row: Passenger) => {
  isEdit.value = true
  editingId.value = row.id
  passengerForm.value = {
    name: row.name,
    idType: row.idType || 'ID_CARD',
    idCard: row.idCard,
    passengerType: row.passengerType,
    phone: maskPhone(row.phone) || '',
    nationality: row.nationality || '',
    birthDate: row.birthDate || '',
    gender: row.gender || '',
    expireDate: row.expireDate || '',
    email: row.email || ''
  }
  dialogVisible.value = true
}

const handleDelete = async (row: Passenger) => {
  try {
    await ElMessageBox.confirm(`确定要删除乘车人"${row.name}"吗？`, '提示', { type: 'warning' })
    // Mock删除
    passengers.value = passengers.value.filter(p => p.id !== row.id)
    ElMessage.success('删除成功')
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleDeleteConfirm = async () => {
  if (!editingId.value) return
  try {
    await ElMessageBox.confirm(`确定要删除乘车人"${passengerForm.value.name}"吗？`, '提示', { type: 'warning' })
    // Mock删除
    passengers.value = passengers.value.filter(p => p.id !== editingId.value)
    dialogVisible.value = false
    ElMessage.success('删除成功')
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  submitting.value = true
  await new Promise(resolve => setTimeout(resolve, 300))
  try {
    if (isEdit.value && editingId.value) {
      // Mock更新 - 保留原有字段，只更新表单字段
      const idx = passengers.value.findIndex(p => p.id === editingId.value)
      if (idx !== -1) {
        passengers.value[idx] = {
          ...passengers.value[idx],
          name: passengerForm.value.name,
          idType: passengerForm.value.idType,
          idCard: passengerForm.value.idCard,
          passengerType: passengerForm.value.passengerType,
          phone: passengerForm.value.phone,
          nationality: passengerForm.value.nationality,
          birthDate: passengerForm.value.birthDate,
          gender: passengerForm.value.gender,
          expireDate: passengerForm.value.expireDate,
          email: passengerForm.value.email
        }
      }
      ElMessage.success('修改成功')
    } else {
      // Mock添加
      const newPassenger: Passenger = {
        id: mockId++,
        name: passengerForm.value.name,
        idType: passengerForm.value.idType,
        idCard: passengerForm.value.idCard,
        passengerType: passengerForm.value.passengerType,
        phone: passengerForm.value.phone,
        nationality: passengerForm.value.nationality,
        birthDate: passengerForm.value.birthDate,
        gender: passengerForm.value.gender,
        expireDate: passengerForm.value.expireDate,
        email: passengerForm.value.email,
        isVerified: 1,
        isDefault: 0
      }
      passengers.value.push(newPassenger)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
  } catch {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

const maskIdCard = (id: string) => {
  if (!id || id.length < 8) return id
  return id.slice(0, 4) + '********' + id.slice(-4)
}

const maskPhone = (phone: string) => {
  if (!phone || phone.length < 11) return phone
  return phone.slice(0, 3) + '****' + phone.slice(-4)
}

const getPassengerTypeName = (type: number) => {
  const types: Record<number, string> = { 1: '成人', 2: '儿童', 3: '学生', 4: '残疾军人' }
  return types[type] || '成人'
}

const getIdTypeName = (type: string) => {
  const types: Record<string, string> = {
    ID_CARD: '中国居民身份证',
    HK_MACAO_RESIDENCE: '港澳居民居住证',
    TAIWAN_RESIDENCE: '台湾居民居住证',
    FOREIGN_PERMANENT: '外国人永久居留身份证',
    HK_MACAO_PASS: '港澳居民来往内地通行证(含非中国籍)',
    TAIWAN_PASS: '台湾居民来往大陆通行证',
    CHINA_PASSPORT: '中国护照',
    FOREIGN_PASSPORT: '外国护照'
  }
  return types[type] || '中国居民身份证'
}

const getTypeName = (type: number) => {
  const types: Record<number, string> = { 1: '成人', 2: '儿童', 3: '学生', 4: '残疾军人' }
  return types[type] || '成人'
}

const selectIdType = (value: string) => {
  passengerForm.value.idType = value
  showIdTypePicker.value = false
}

const selectPassengerType = (value: number) => {
  passengerForm.value.passengerType = value
  showTypePicker.value = false
}

const selectWheelItem = (type: 'birth' | 'expire', field: 'year' | 'month' | 'day', value: number) => {
  if (type === 'birth') {
    tempBirthDate.value[field] = value
    if (field === 'year' || field === 'month') {
      const maxDay = new Date(tempBirthDate.value.year, tempBirthDate.value.month, 0).getDate()
      if (tempBirthDate.value.day > maxDay) {
        tempBirthDate.value.day = maxDay
      }
    }
  } else {
    tempExpireDate.value[field] = value
    if (field === 'year' || field === 'month') {
      const maxDay = new Date(tempExpireDate.value.year, tempExpireDate.value.month, 0).getDate()
      if (tempExpireDate.value.day > maxDay) {
        tempExpireDate.value.day = maxDay
      }
    }
  }
}

const confirmBirthDate = () => {
  const { year, month, day } = tempBirthDate.value
  passengerForm.value.birthDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
  showBirthDatePicker.value = false
}

const confirmExpireDate = () => {
  const { year, month, day } = tempExpireDate.value
  passengerForm.value.expireDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
  showExpireDatePicker.value = false
}

const selectNationality = (name: string) => {
  passengerForm.value.nationality = name
  showNationalityPicker.value = false
  nationalitySearchKeyword.value = ''
}

const getTypeClass = (type: number) => {
  const classes: Record<number, string> = { 1: 'adult', 2: 'child', 3: 'student', 4: 'disabled' }
  return classes[type] || 'adult'
}
</script>

<style lang="scss" scoped>
.mobile-passengers-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.top-nav {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .back-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
    display: flex;
    align-items: center;
    opacity: 0.95;

    &:active {
      opacity: 0.8;
    }
  }

  .nav-title {
    font-size: 17px;
    font-weight: 600;
    margin: 0;
    flex: 1;
    text-align: center;
  }

  .add-btn {
    background: none;
    border: none;
    color: white;
    font-size: 16px;
    font-weight: 500;
    cursor: pointer;
    padding: 6px 12px;

    &:active {
      opacity: 0.8;
    }
  }
}

.refresh-tip {
  background: #fffbe6;
  border-bottom: 1px solid #ffe58f;
  padding: 10px 16px;
  text-align: center;

  span {
    font-size: 13px;
    color: #d48806;
  }
}

.passenger-list {
  padding: 12px 16px;
  padding-bottom: 40px;
}

.section {
  margin-bottom: 16px;
}

.current-user-section {
  .section-label {
    font-size: 14px;
    font-weight: 600;
    color: #333;
    margin-bottom: 8px;
    padding-left: 4px;
  }
}

.section-letter {
  font-size: 14px;
  font-weight: 700;
  color: #999;
  margin-bottom: 8px;
  padding-left: 4px;
}

.passenger-item {
  background: white;
  border-radius: 8px;
  padding: 14px 16px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  transition: all 0.2s;
  cursor: pointer;

  &:active {
    transform: scale(0.99);
    background: #fafafa;
  }

  &.current-user {
    border-left: 3px solid #1890ff;
  }

  .item-main {
    flex: 1;
    min-width: 0;
  }

  .name-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 6px;

    .name {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }

    .type-tag {
      display: inline-block;
      padding: 2px 8px;
      border-radius: 10px;
      font-size: 11px;
      font-weight: 500;

      &.student {
        background: #e6f7ff;
        color: #1890ff;
        border: 1px solid #91d5ff;
      }

      &.adult {
        background: #f6ffed;
        color: #52c41a;
        border: 1px solid #b7eb8f;
      }

      &.child {
        background: #fff7e6;
        color: #fa8c16;
        border: 1px solid #ffd591;
      }

      &.disabled {
        background: #fff1f0;
        color: #f5222d;
        border: 1px solid #ffa39e;
      }
    }
  }

  .id-card {
    font-size: 13px;
    color: #999;
    font-family: 'SF Mono', 'Consolas', monospace;
    letter-spacing: 0.5px;
  }

  .nationality-tag {
    font-size: 10px;
    padding: 2px 6px;
    background: #f0f0f0;
    color: #666;
    border-radius: 4px;
    font-weight: 500;
  }

  .id-info {
    display: flex;
    flex-direction: column;
    gap: 2px;
  }

  .id-type {
    font-size: 11px;
    color: #1890ff;
  }

  .item-action {
    flex-shrink: 0;
    margin-left: 12px;
    padding: 4px;

    svg {
      display: block;
    }
  }
}

.empty-state {
  text-align: center;
  padding: 60px 24px;
  background: white;
  border-radius: 12px;
  margin-top: 20px;

  .empty-icon {
    margin-bottom: 16px;

    svg {
      width: 100%;
      max-width: 120px;
      margin: 0 auto;
      display: block;
    }
  }

  .empty-title {
    font-size: 16px;
    color: #333;
    font-weight: 600;
    margin: 0 0 8px;
  }

  .empty-desc {
    font-size: 13px;
    color: #999;
    margin: 0 0 24px;
  }
}

:deep(.add-passenger-dialog) {
  .el-overlay {
    z-index: 2000 !important;
  }

  .el-dialog {
    --el-dialog-margin-top: 0 !important;
    margin: 0 !important;
    border-radius: 0;
    overflow: hidden;
    max-height: 100vh;
    display: flex;
    flex-direction: column;

    .el-dialog__header {
      display: none;
    }

    .el-dialog__body {
      padding: 0 !important;
      flex: 1;
      overflow-y: auto;
    }
  }
}

.dialog-header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  padding: 12px 16px;
  display: flex;
  align-items: center;
  position: relative;

  .back-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
    display: flex;
    align-items: center;
  }

  .dialog-title {
    font-size: 17px;
    font-weight: 600;
    color: white;
    margin: 0;
    flex: 1;
    text-align: center;
    padding-right: 28px;
  }

  .delete-btn {
    background: none;
    border: none;
    color: white;
    font-size: 16px;
    font-weight: 500;
    cursor: pointer;
    padding: 6px 12px;
    opacity: 0.95;

    &:active {
      opacity: 0.8;
    }
  }
}

.dialog-body {
  background: #f5f7fa;
  min-height: calc(100vh - 52px);
  padding-bottom: env(safe-area-inset-bottom);
}

.scan-section {
  background: white;
  margin: 12px;
  border-radius: 8px;
  padding: 14px 16px;

  .scan-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    .scan-title {
      font-size: 15px;
      color: #333;
      font-weight: 500;
      flex: 1;
    }

    .recommend-tag {
      background: #ff9500;
      color: white;
      font-size: 11px;
      padding: 2px 8px;
      border-radius: 10px;
      font-weight: 500;
    }
  }

  .scan-desc {
    font-size: 13px;
    color: #666;
    line-height: 1.5;
    margin: 0;

    a {
      color: #1890ff;
      text-decoration: none;
    }
  }

  .verified-tag {
    background: #f6ffed;
    color: #52c41a;
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 12px;
    font-weight: 500;
    margin-left: 8px;
  }
}

.passenger-form {
  padding: 0 12px;
}

.form-row {
  background: white;
  border-radius: 8px;
  padding: 14px 16px;
  margin-bottom: 10px;

  .form-label {
    display: block;
    font-size: 14px;
    color: #666;
    margin-bottom: 8px;
    font-weight: 500;

    .help-icon {
      vertical-align: middle;
      margin-left: 2px;
    }

    .sub-label {
      font-size: 12px;
      color: #999;
      font-weight: 400;
    }
  }

  .form-control {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #eee;
    font-size: 15px;
    color: #333;

    &.select-control {
      cursor: pointer;

      &:active {
        background: #fafafa;
      }
    }
  }

  &.gender-row {
    .gender-options {
      display: flex;
      gap: 30px;
    }

    .gender-option {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      input[type="radio"] {
        display: none;
      }

      .radio-dot {
        width: 18px;
        height: 18px;
        border-radius: 50%;
        border: 2px solid #ccc;
        position: relative;
        transition: all 0.2s;

        &::after {
          content: '';
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          width: 10px;
          height: 10px;
          border-radius: 50%;
          background: transparent;
          transition: background 0.2s;
        }
      }

      input[type="radio"]:checked + .radio-dot {
        border-color: #1890ff;

        &::after {
          background: #1890ff;
        }
      }
    }
  }
}

.example-link {
  padding: 0 16px;
  margin-bottom: 10px;

  a {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    color: #ff9500;
    font-size: 13px;
    text-decoration: none;
  }
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 16px 0 8px;
  padding: 0 4px;
}

.phone-row {
  .phone-input-wrapper {
    display: flex;
    align-items: center;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 0 12px;
    background: white;
    position: relative;

    &:focus-within {
      border-color: #1890ff;
    }

    .phone-prefix {
      color: #333;
      font-size: 15px;
      font-weight: 500;
      flex-shrink: 0;
    }

    .phone-divider {
      color: #dcdfe6;
      margin: 0 10px;
      flex-shrink: 0;
    }

    .phone-input {
      flex: 1;
      border: none;
      outline: none;
      font-size: 15px;
      padding: 10px 0;
      color: #333;
      background: transparent;

      &::placeholder {
        color: #c0c4cc;
      }
    }

    .contact-icon {
      flex-shrink: 0;
      margin-left: 8px;
    }
  }
}

.custom-input {
  :deep(.el-input__wrapper) {
    box-shadow: 0 0 0 1px #dcdfe6 inset;

    &.is-focus {
      box-shadow: 0 0 0 1px #1890ff inset;
    }
  }

  :deep(.el-input__inner) {
    height: 40px;
    line-height: 40px;
  }
}

.warning-tip {
  background: #fffbe6;
  border: 1px solid #ffe58f;
  border-radius: 6px;
  padding: 10px 12px;
  margin: 12px 0;
  font-size: 12px;
  color: #d48806;
  line-height: 1.5;
}

.submit-btn {
  width: 100%;
  height: 44px;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  border: none;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin: 16px 0;
  transition: opacity 0.2s;

  &:active {
    opacity: 0.9;
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.tips-section {
  background: white;
  border-radius: 8px;
  padding: 14px 16px;
  margin: 0 12px 20px;

  .tips-title {
    font-size: 14px;
    font-weight: 600;
    color: #333;
    margin-bottom: 10px;
  }

  .tips-list {
    margin: 0;
    padding-left: 18px;
    font-size: 12px;
    color: #666;
    line-height: 1.7;

    li {
      margin-bottom: 6px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

/* 选择弹窗样式 */
.picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 3000;
  display: flex;
  align-items: flex-end;
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.picker-modal {
  width: 100%;
  background: white;
  border-radius: 16px 16px 0 0;
  animation: slideUp 0.3s ease;
  max-height: 70vh;
  display: flex;
  flex-direction: column;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.picker-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;

  .picker-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }

  .picker-close {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
    opacity: 0.7;
    transition: opacity 0.2s;

    &:hover,
    &:active {
      opacity: 1;
    }
  }
}

.picker-content {
  flex: 1;
  overflow-y: auto;
  max-height: calc(70vh - 52px);
}

.picker-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: background 0.2s;

  &:active {
    background: #f5f5f5;
  }

  &.active {
    background: #f0f7ff;
  }

  .item-label {
    font-size: 15px;
    color: #333;
    flex: 1;
    text-align: left;
  }

  .radio-wrapper {
    flex-shrink: 0;
    margin-left: 12px;
  }
}

/* 日期选择器样式 */
.date-picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 3000;
  display: flex;
  align-items: flex-end;
  animation: fadeIn 0.2s ease;
}

.date-picker-modal {
  width: 100%;
  background: white;
  border-radius: 16px 16px 0 0;
  animation: slideUp 0.3s ease;
  position: relative;
}

.date-picker-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  border-bottom: 1px solid #f0f0f0;

  .date-picker-btn {
    background: none;
    border: none;
    font-size: 16px;
    cursor: pointer;
    padding: 6px 12px;

    &.cancel-btn {
      color: #999;
    }

    &.confirm-btn {
      color: #1890ff;
      font-weight: 600;
    }
  }

  .date-picker-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
}

.date-picker-wheels {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
  position: relative;
  height: 220px;
  overflow: hidden;
}

.wheel-column {
  flex: 1;
  height: 100%;
  overflow-y: auto;
  scroll-snap-type: y mandatory;
  scrollbar-width: none;
  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }
}

.wheel-item {
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #333;
  scroll-snap-align: center;
  cursor: pointer;
  transition: all 0.2s;

  &:hover,
  &:active {
    background: #f5f5f5;
  }

  &.selected {
    color: #1890ff;
    font-weight: 600;
    font-size: 20px;
  }
}

.wheel-indicator {
  position: absolute;
  left: 16px;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  height: 44px;
  border-top: 1px solid #e8e8e8;
  border-bottom: 1px solid #e8e8e8;
  pointer-events: none;
  z-index: 1;
}

/* 国家/地区选择器样式 */
.nationality-picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 3000;
  display: flex;
  align-items: flex-end;
  animation: fadeIn 0.2s ease;
}

.nationality-picker-modal {
  width: 100%;
  background: white;
  border-radius: 16px 16px 0 0;
  animation: slideUp 0.3s ease;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.nationality-picker-header {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 12px;

  .search-box {
    flex: 1;
    display: flex;
    align-items: center;
    background: #f5f7fa;
    border-radius: 20px;
    padding: 8px 14px;
    gap: 8px;

    .search-icon {
      flex-shrink: 0;
      opacity: 0.6;
    }

    .search-input {
      flex: 1;
      border: none;
      outline: none;
      background: transparent;
      font-size: 15px;
      color: #333;

      &::placeholder {
        color: #c0c4cc;
      }
    }
  }

  .cancel-search-btn {
    background: none;
    border: none;
    color: #1890ff;
    font-size: 15px;
    cursor: pointer;
    padding: 6px 10px;
    font-weight: 500;
    flex-shrink: 0;
  }
}

.nationality-list {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;

  .nationality-item {
    padding: 14px 16px;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;
    transition: all 0.2s;
    display: flex;
    justify-content: space-between;
    align-items: center;

    &:active {
      background: #f5f5f5;
    }

    &.selected {
      background: #f0f7ff;

      .country-name {
        color: #1890ff;
        font-weight: 600;
      }
    }

    .country-name {
      font-size: 15px;
      color: #333;
      flex: 1;
    }

    .country-en {
      font-size: 13px;
      color: #999;
      margin-left: 12px;
      flex-shrink: 0;
    }
  }

  .no-result {
    text-align: center;
    padding: 40px 20px;
    color: #999;
    font-size: 14px;
  }
}
</style>

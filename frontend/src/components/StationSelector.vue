<template>
  <div class="station-selector">
    <el-popover
      :visible="popoverVisible"
      placement="bottom"
      :width="selectorWidth || 400"
      trigger="click"
      @update:visible="handleVisibleChange"
    >
      <template #reference>
        <div class="station-input" @click="handleInputClick">
          <el-input
            v-model="displayText"
            :placeholder="placeholder || '请选择车站'"
            readonly
            :suffix-icon="ArrowDown"
            @clear="handleClear"
          />
        </div>
      </template>

      <div class="station-panel">
        <!-- 搜索框 -->
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索车站名称或拼音"
            :prefix-icon="Search"
            clearable
            @input="handleSearch"
            ref="searchInputRef"
          />
        </div>

        <!-- 热门车站 -->
        <div class="hot-stations" v-if="!searchKeyword && showHot">
          <div class="section-title">热门车站</div>
          <div class="station-tags">
            <el-tag
              v-for="station in hotStations"
              :key="station.id"
              class="station-tag"
              @click="handleSelectStation(station)"
            >
              {{ station.stationName }}
            </el-tag>
          </div>
        </div>

        <!-- 搜索结果 -->
        <div class="search-results" v-if="searchKeyword">
          <div class="section-title">搜索结果</div>
          <div v-if="searchResults.length === 0" class="no-result">
            未找到相关车站
          </div>
          <div
            v-else
            v-for="station in searchResults"
            :key="station.id"
            class="station-item"
            :class="{ selected: station.id === selectedId }"
            @click="handleSelectStation(station)"
          >
            <span class="name">{{ station.stationName }}</span>
            <span class="pinyin">{{ station.stationPinyin }}</span>
          </div>
        </div>

        <!-- 所有车站（未搜索时显示首字母分类） -->
        <div class="all-stations" v-if="!searchKeyword" ref="stationListRef">
          <div
            v-for="(stations, letter) in groupedStations"
            :key="letter"
            class="letter-group"
          >
            <div class="letter-title">{{ letter }}</div>
            <div
              v-for="station in stations"
              :key="station.id"
              class="station-item"
              :class="{ selected: station.id === selectedId }"
              @click="handleSelectStation(station)"
            >
              <span class="name">{{ station.stationName }}</span>
              <span class="pinyin">{{ station.stationPinyin }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue'
import { Search, ArrowDown } from '@element-plus/icons-vue'
import { stationApi } from '@/api'

interface Station {
  id: number
  stationCode: string
  stationName: string
  stationPinyin: string
  cityName?: string
}

interface Props {
  modelValue?: number | string  // 车站ID或名称
  placeholder?: string
  showHot?: boolean
  selectorWidth?: number
}

const props = withDefaults(defineProps<Props>(), {
  showHot: true,
  selectorWidth: 400
})

const emit = defineEmits<{
  'update:modelValue': [value: number | string]
  'change': [station: Station | null]
}>()

const popoverVisible = ref(false)
const searchKeyword = ref('')
const searchResults = ref<Station[]>([])
const hotStations = ref<Station[]>([])
const allStations = ref<Station[]>([])
const searchInputRef = ref()
const stationListRef = ref()

// 显示文本
const displayText = computed(() => {
  if (props.modelValue) {
    const station = allStations.value.find(s =>
      s.id === props.modelValue || s.stationName === props.modelValue
    )
    return station?.stationName || String(props.modelValue)
  }
  return ''
})

// 选中的ID
const selectedId = computed(() => {
  if (typeof props.modelValue === 'number') {
    return props.modelValue
  }
  const station = allStations.value.find(s => s.stationName === props.modelValue)
  return station?.id
})

// 按首字母分组的车站
const groupedStations = computed(() => {
  const grouped: Record<string, Station[]> = {}
  for (const station of allStations.value) {
    const letter = station.stationPinyin?.charAt(0).toUpperCase() || '#'
    if (!grouped[letter]) {
      grouped[letter] = []
    }
    grouped[letter].push(station)
  }
  // 按字母排序
  const sorted: Record<string, Station[]> = {}
  Object.keys(grouped).sort().forEach(key => {
    sorted[key] = grouped[key]
  })
  return sorted
})

// 加载数据
const loadData = async () => {
  try {
    const [hotRes, allRes] = await Promise.all([
      stationApi.getHot(),
      stationApi.getAll()
    ])
    hotStations.value = hotRes.data || []
    allStations.value = allRes.data || []
  } catch (error) {
    console.error('加载车站数据失败:', error)
  }
}

// 搜索车站
const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    searchResults.value = []
    return
  }
  try {
    const res = await stationApi.search(searchKeyword.value)
    searchResults.value = res.data || []
  } catch (error) {
    console.error('搜索失败:', error)
  }
}

// 选择车站
const handleSelectStation = (station: Station) => {
  emit('update:modelValue', station.id)
  emit('change', station)
  popoverVisible.value = false
  searchKeyword.value = ''
  searchResults.value = []
}

// 清除选择
const handleClear = () => {
  emit('update:modelValue', '')
  emit('change', null)
}

// 点击输入框
const handleInputClick = () => {
  loadData()
}

// 显示/隐藏变化
const handleVisibleChange = (visible: boolean) => {
  popoverVisible.value = visible
  if (visible) {
    loadData()
    nextTick(() => {
      searchInputRef.value?.focus()
    })
  }
}

// 初始加载
loadData()
</script>

<style lang="scss" scoped>
.station-selector {
  width: 100%;
}

.station-input {
  cursor: pointer;
}

.station-panel {
  max-height: 400px;
  overflow-y: auto;

  .search-box {
    padding: 10px 0;
    border-bottom: 1px solid #eee;
    position: sticky;
    top: 0;
    background: #fff;
    z-index: 1;
  }

  .section-title {
    font-size: 12px;
    color: #999;
    padding: 10px 0 5px;
  }

  .hot-stations {
    padding-bottom: 10px;
    border-bottom: 1px solid #eee;

    .station-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

      .station-tag {
        cursor: pointer;
        &:hover {
          color: #409eff;
          border-color: #409eff;
        }
      }
    }
  }

  .search-results, .all-stations {
    padding: 5px 0;

    .no-result {
      padding: 20px;
      text-align: center;
      color: #999;
    }

    .letter-group {
      .letter-title {
        background: #f5f5f5;
        padding: 5px 10px;
        font-weight: bold;
        color: #409eff;
        position: sticky;
        top: 60px;
      }
    }

    .station-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 15px;
      cursor: pointer;
      transition: background 0.2s;

      &:hover {
        background: #f5f7fa;
      }

      &.selected {
        background: #ecf5ff;
        color: #409eff;
      }

      .name {
        font-size: 14px;
      }

      .pinyin {
        font-size: 12px;
        color: #999;
      }
    }
  }
}
</style>

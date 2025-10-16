<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  }
})

const chartRef = ref(null)
let chartInstance = null

const initChart = () => {
  if (!chartRef.value) return
  
  // 如果已存在实例，先销毁
  if (chartInstance) {
    chartInstance.dispose()
  }
  
  chartInstance = echarts.init(chartRef.value)
  updateChart()
}

const updateChart = () => {
  if (!chartInstance) return
  
  // 如果没有数据，清空图表并显示空状态
  if (!props.data || props.data.length === 0) {
    chartInstance.clear() // 先清空图表
    chartInstance.setOption({
      title: {
        text: '暂无数据',
        left: 'center',
        top: 'center',
        textStyle: {
          color: '#909399',
          fontSize: 14
        }
      },
      xAxis: {},
      yAxis: {},
      series: []
    }, true) // true 表示不合并，完全替换
    return
  }

  const dates = props.data.map(item => {
    if (!item.date) return ''
    const date = new Date(item.date)
    return `${date.getMonth() + 1}/${date.getDate()}`
  })
  
  const scores = props.data.map(item => item.avgScore || 0)
  const improvements = props.data.map(item => item.improvementRate || 0)

  const option = {
    title: {
      text: '写作能力成长趋势',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      },
      formatter: function(params) {
        let result = `${params[0].axisValue}<br/>`
        params.forEach(param => {
          const unit = param.seriesName === '平均分' ? '分' : '%'
          result += `${param.marker}${param.seriesName}: ${param.value}${unit}<br/>`
        })
        return result
      }
    },
    legend: {
      data: ['平均分', '提升率'],
      top: 35
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: 80,
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '分数',
        min: 0,
        max: 100,
        axisLabel: {
          formatter: '{value}分'
        }
      },
      {
        type: 'value',
        name: '提升率',
        axisLabel: {
          formatter: '{value}%'
        }
      }
    ],
    series: [
      {
        name: '平均分',
        type: 'line',
        data: scores,
        smooth: true,
        itemStyle: {
          color: '#409eff'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        }
      },
      {
        name: '提升率',
        type: 'line',
        yAxisIndex: 1,
        data: improvements,
        smooth: true,
        itemStyle: {
          color: '#67c23a'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        }
      }
    ]
  }

  chartInstance.setOption(option, true) // true 表示不合并，完全替换旧配置
}

// 清空图表
const clearChart = () => {
  if (chartInstance) {
    chartInstance.clear()
  }
}

// 监听数据变化
watch(() => props.data, (newData, oldData) => {
  console.log('📊 ChartProgress 数据变化:', {
    新数据长度: newData?.length || 0,
    旧数据长度: oldData?.length || 0,
    新数据: newData
  })
  nextTick(() => {
    updateChart()
  })
}, { deep: true, immediate: false })

// 监听窗口大小变化
const handleResize = () => {
  chartInstance?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

// 组件卸载时清理
import { onBeforeUnmount } from 'vue'
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px;
  min-height: 300px;
}
</style>






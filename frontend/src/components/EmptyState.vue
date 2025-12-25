<template>
  <div class="empty-state">
    <div class="star-emoji">🌟</div>
    <h2>美好的一天</h2>
    <p>从沟通开始</p>
  </div>
</template>

<script setup>
// 空状态组件，无逻辑
</script>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: white;
}

.star-emoji {
  font-size: 80px;
  margin-bottom: 20px;
}

h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 24px;
}

p {
  margin: 0;
  color: #666;
  font-size: 16px;
}
</style>













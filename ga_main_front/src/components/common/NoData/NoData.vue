<template>
  <div class="no-data-wrapper">
    <div class="no-data-img" />
    <span class="title">无数据</span>
  </div>
</template>

<script>
export default {
  name: "NoData",
};
</script>

<style lang="scss" scoped>
.no-data-wrapper {
  display: flex;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 64px;
  .title {
    min-height: 24px;
    color: #8995a2;
  }
  .no-data-img {
    min-height: 64px;
    width: 100%;
    flex: 1;
    background: url(./NoData.svg) center center / 60% 60% no-repeat;
  }
}
</style>
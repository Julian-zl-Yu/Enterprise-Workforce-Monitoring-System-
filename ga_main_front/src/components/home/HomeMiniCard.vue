<template>
<!-- 
 * @FileDescription: 实名制首页组件
 * @fileName: HomeMiniCard.vue 
 * @Author: lazy_cat 
 * @LastEditTime: 2021-06-23 16:25:23
!-->
  <!-- <el-row > -->
  <el-row style="margin:17px auto auto 12px">
    <el-col :span="8">
      <img :src="homeMiniCardInfo.iconUrl" style="width:46px;height:46px" alt="" />
    </el-col>
    <el-col :span="16" v-if="homeMiniCardInfo.type == 'attendance'">
      <div class="text16px">
        <div>
          {{ homeMiniCardInfo.title }}
        </div>
        <div>
          {{ homeMiniCardInfo.title1 }}
          <span class="text18px">
            {{ homeMiniCardInfo.content1 }}
          </span>
        </div>
        <div>
          {{ homeMiniCardInfo.title2 }}
          <span class="text18px">
            {{ homeMiniCardInfo.content2 }}
          </span>
        </div>
      </div>
    </el-col>
    <el-col :span="16" v-else>
      <div class="text16px">
        <div>
          {{ homeMiniCardInfo.title }}
        </div>
      </div>
      <div class="text26px">
        <div>
          {{ homeMiniCardInfo.content }}
          <span v-if="homeMiniCardInfo.type == 'totalMoney'" style="font-size:16px"
            >万</span
          >
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  props: {
    homeMiniCardInfo: Object,
  },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.text16px {
  font-size: 16px;
  color: #666;
}
.text18px {
  font-size: 18px;
  color: #333;
}
.text26px {
  font-size: 26px;
  color: #333;
}
</style>

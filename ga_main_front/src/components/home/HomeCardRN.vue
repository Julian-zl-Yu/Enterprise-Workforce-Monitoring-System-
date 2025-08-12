<template>
  <!-- 
 * @FileDescription: 实名制首页组件
 * @fileName: HomeCardRN.vue 
 * @Author: lazy_cat 
 * @LastEditTime: 2021-06-23 16:24:31
!-->
  <div>
    <p style="font-size: 16px;font-weight: 600;color: #333;margin:15px 0">
      {{ homeCardInfo.title }}
    </p>
    <div style="width:100%;height:1px;background:#f0f0f0;margin:15px 0" />
    <div
      :style="{
        width: '100%',
        height: '48px',
        backgroundImage: 'url(' + homeCardInfo.bgUrl + ')',
      }"
    >
      <el-row
        style="line-height:48px"
        :class="
          homeCardInfo.type == 'unit'
            ? 'unitTitle'
            : homeCardInfo.type == 'personnel'
            ? 'pslTitle'
            : 'aeTitle'
        "
      >
        <el-col
          style="text-indent:20px"
          :span="homeCardInfo.type != 'case' ? 8 : 18"
        >
          {{ homeCardInfo.contentTitle[0] }}
        </el-col>
        <el-col
          style="text-align:left"
          :span="homeCardInfo.type != 'case' ? 10 : 6"
        >
          {{ homeCardInfo.contentTitle[1] }}
        </el-col>
        <el-col
          style="text-indent:14px;"
          v-if="homeCardInfo.type != 'case'"
          :span="6"
        >
          {{ homeCardInfo.contentTitle[2] }}
        </el-col>
      </el-row>
    </div>
    <vue-seamless-scroll
      v-show="isSeamlessScroll"
      :data="CardPartsStatisticsList"
      class="seamless_warp"
      ref="seamlessScroll"
      :class-option="classOption"
    >
      <div
        class="DataList_top"
        v-for="(item, index) in CardPartsStatisticsList"
        :key="index"
      >
        <!-- 参建单位 -->
        <el-row v-if="homeCardInfo.type === 'unit'">
          <el-col class="textNoWrap" style="text-indent:20px" :span="8"
            ><el-tooltip
              class="item"
              effect="light"
              :content="item.type"
              placement="top"
            >
              <span>{{ item.type }}</span>
            </el-tooltip></el-col
          >
          <el-col class="textNoWrap" :span="10"
            ><el-tooltip
              class="item"
              effect="light"
              :content="item.name"
              placement="top"
            >
              <span>{{ item.name }}</span>
            </el-tooltip></el-col
          >
          <el-col style="text-indent:14px" :span="6">{{
            item.personNum
          }}</el-col>
        </el-row>
        <!-- 管理人员 -->
        <el-row v-if="homeCardInfo.type === 'personnel'">
          <el-col class="textNoWrap" style="text-indent:20px" :span="8">
            <el-tooltip
              class="item"
              effect="light"
              :content="item.managerType"
              placement="top"
            >
              <span>{{ item.managerType }}</span>
            </el-tooltip></el-col
          >
          <el-col :span="10">{{ item.managerName }}</el-col>
          <el-col style="text-indent:14px;" :span="6">
            <img
              :src="
                require(`@/assets/img/homeicon/${
                  item.managerAttendance !== '0'
                    ? 'attendance_correct'
                    : 'attendance_error'
                }.png`)
              "
              style="height:12px;width:14px"
            />
          </el-col>
        </el-row>
        <!-- 考勤设备 -->
        <el-row v-if="homeCardInfo.type === 'case'">
          <el-col class="textNoWrap" style="text-indent:20px" :span="18">
            <el-tooltip
              class="item"
              effect="light"
              :content="item.deviceserialno"
              placement="top"
            >
              <span>{{ item.deviceserialno }}</span>
            </el-tooltip>
          </el-col>
          <el-col :span="6">
            {{ item.networkStatus }}
          </el-col>
        </el-row>
      </div>
    </vue-seamless-scroll>
    <!-- 数据不够滚动条数 -->
    <div v-show="!isSeamlessScroll" class="seamless_warp">
      <div
        class="DataList_top"
        v-for="(item, index) in CardPartsStatisticsList"
        :key="index"
      >
        <!-- 参建单位 -->
        <el-row v-if="homeCardInfo.type === 'unit'">
          <el-col class="textNoWrap" style="text-indent:20px" :span="8"
            ><el-tooltip
              class="item"
              effect="light"
              :content="item.type"
              placement="top"
            >
              <span>{{ item.type }}</span>
            </el-tooltip></el-col
          >
          <el-col class="textNoWrap" :span="10"
            ><el-tooltip
              class="item"
              effect="light"
              :content="item.name"
              placement="top"
            >
              <span>{{ item.name }}</span>
            </el-tooltip></el-col
          >
          <el-col style="text-indent:14px" :span="6">{{
            item.personNum
          }}</el-col>
        </el-row>
        <!-- 管理人员 -->
        <el-row v-if="homeCardInfo.type === 'personnel'">
          <el-col class="textNoWrap" style="text-align:center" :span="8">
            <el-tooltip
              class="item"
              effect="light"
              :content="item.managerType"
              placement="top"
            >
              <span>{{ item.managerType }}</span>
            </el-tooltip></el-col
          >
          <el-col :span="10">{{ item.managerName }}</el-col>
          <el-col style="text-indent:14px;" :span="6">
            <img
              :src="
                require(`@/assets/img/homeicon/${
                  item.managerAttendance !== '0'
                    ? 'attendance_correct'
                    : 'attendance_error'
                }.png`)
              "
              style="height:12px;width:14px"
            />
          </el-col>
        </el-row>
        <!-- 考勤设备 -->
        <el-row v-if="homeCardInfo.type === 'case'">
          <el-col class="textNoWrap" style="text-indent:20px" :span="18">
            <el-tooltip
              class="item"
              effect="light"
              :content="item.deviceserialno"
              placement="top"
            >
              <span>{{ item.deviceserialno }}</span>
            </el-tooltip></el-col
          >
          <el-col :span="6">
            {{ item.networkStatus }}
          </el-col>
        </el-row>
      </div>
    </div>
    <!-- 无数据 -->
    <NoData
      v-if="CardPartsStatisticsList.length === 0"
      style="margin-top:50px;width: 100%; height: 64px"
    />
  </div>
</template>

<script>
export default {
  props: {
    homeCardInfo: Object,
  },
  computed: {
    classOption() {
      return this.option;
    },
  },
  data() {
    return {
      CardPartsStatisticsList: [],
      isSeamlessScroll: true,
      seamlessScrollLoding: true,
      tac: "text-align:center",
      tal: "text-align:left;text-indent:20px",
      option: {
        step: 0.4, // 数值越大速度滚动越快
        limitMoveNum: this.homeCardInfo.content.length, // 开始无缝滚动的数据量
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      },
    };
  },
  mounted() {
    if (this.$store.state.user.superAdmin !== 1) {
      this.isSeamlessScrollFn();
      this.CardPartsStatisticsList = this.homeCardInfo.content;
      setTimeout(() => {
        this.isSeamlessScrollFn();
        this.CardPartsStatisticsList = this.homeCardInfo.content;
        // CardPartsStatisticsList length无变化，仅仅是属性变更，手动调用下组件内部的reset方法
        this.$refs.seamlessScroll.reset();
      }, 500);
      // setInterval(() => {
      //   this.CardPartsStatisticsList.push(this.homeCardInfo.content.slice(0,100))
      //   this.$refs.seamlessScroll.reset();
      // }, 500);
    }
  },
  methods: {
    isSeamlessScrollFn() {
      if (this.homeCardInfo.content.length < 6) {
        this.isSeamlessScroll = false;
      } else {
        this.isSeamlessScroll = true;
      }
    },
    resetSeamlessScroll() {
      this.isSeamlessScrollFn();
      this.CardPartsStatisticsList = this.homeCardInfo.content;
      // this.CardPartsStatisticsList = this.homeCardInfo.content;
      this.$refs.seamlessScroll.reset();
    },
  },
};
</script>

<style lang="scss" scoped>
.unitTitle {
  font-size: 14px;
  color: #0b5ffa;
  font-weight: 600;
}
.pslTitle {
  font-size: 14px;
  color: #f4990b;
  font-weight: 600;
}
.aeTitle {
  font-size: 14px;
  color: #19d588;
  font-weight: 600;
}
.seamless_warp {
  margin-top: 10px;
  width: 100%;
  height: calc(100% - 16px);
  max-height: 188px;
  overflow: hidden;
}
.DataList_top {
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
  line-height: 44px;
  color: #666;
}
.noSelect {
  -webkit-touch-callout: none;
  /* iOS Safari */
  -webkit-user-select: none;
  /* Chrome/Safari/Opera */
  -khtml-user-select: none;
  /* Konqueror */
  -moz-user-select: none;
  /* Firefox */
  -ms-user-select: none;
  /* Internet Explorer/Edge */
  user-select: none;
}
.textNoWrap {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>

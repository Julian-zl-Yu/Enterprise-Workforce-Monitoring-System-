<template>
  <!-- 
 * @FileDescription: 扬尘系统首页插件
 * @fileName: HomeCardRD.vue 
 * @Author: lazy_cat 
 * @LastEditTime: 2021-06-23 16:25:01
!-->
  <div>
    <div style="display: flex;justify-content: space-between;">
      <p style="font-size: 16px;font-weight: 600;color: #333;margin:15px 0">
        <span style="font-weight: 600;color:#5e96fd;">丨</span>
        {{ homeCardInfo.title }}
      </p>
      <el-button
        v-show="homeCardInfo.type == 'todayWarning'"
        type="text"
        size="mini"
        @click="handleClick"
        >查看全部</el-button
      >
      <p v-show="homeCardInfo.type == 'realTimeData'">
        {{ homeCardInfo.monitoringtime }}
      </p>
    </div>
    <!-- 设备信息 -->
    <div
      style="padding:0 10px"
      v-if="homeCardInfo.type == 'facilityInfo' && homeCardInfo.isShow"
    >
      <p class="textNoWrap">
        设备名称 :
        <el-tooltip
          class="item"
          effect="light"
          :content="homeCardInfo.devicename"
          placement="top"
          ><span> {{ homeCardInfo.devicename }}</span>
        </el-tooltip>
      </p>
      <p>
        设备序列号 :
        <el-tooltip
          class="item"
          effect="light"
          :content="homeCardInfo.sn"
          placement="top"
          ><span> {{ homeCardInfo.sn }}</span>
        </el-tooltip>
      </p>
      <p>
        设备型号 :
        <el-tooltip
          class="item"
          effect="light"
          :content="homeCardInfo.devicemodel"
          placement="top"
          ><span> {{ homeCardInfo.devicemodel }}</span>
        </el-tooltip>
      </p>
      <p>
        设备状态 :
        <img
          :src="
            require(`@/assets/img/homeicon/${
              homeCardInfo.devicestate === '1' ? 'on' : 'off'
            }.png`)
          "
          style="height:22px;width:22px"
        />
      </p>
      <p>
        设备更新时间 :
        <span>
          {{ homeCardInfo.offlinetime }}
        </span>
      </p>
    </div>
    <!-- 预警 -->
    <div
      v-if="homeCardInfo.type == 'todayWarning' && homeCardInfo.isShow"
      style="max-height:264px;overflow:hidden"
    >
      <p
        style="font-size:50px;color:#FF1515;text-align: center;margin:10px 0 20px 0"
      >
        {{ homeCardInfo.todayWarnCount
        }}<span style="font-size:16px;color:#666666;margin-left:20px">次</span>
      </p>
      <p style="font-size: 16px;font-weight: 600;color: #333;margin:15px 0">
        <span style="font-weight: 600;color:#5e96fd;">丨</span>
        往日预警
      </p>
      <div
        v-for="(item, index) in homeCardInfo.content"
        :key="index"
        style="font-size: 14px;color:#333;margin:20px 5px "
      >
        <p
          style="width:8px;height:8px;border-radius:4px;margin:2px;background:#ff1515;display: inline-block;"
        ></p>
        <span style="font-size: 14px;color:##222222;margin-left:8px">{{
          item.WARDATE
        }}</span>
        <span style="font-size: 14px;color:##222222;margin-left:20px"
          >预警{{ item.WARCOUNT }}次</span
        >
      </div>
      <NoData
        v-if="homeCardInfo.content == []"
        style="margin-top:50px;width: 100%; height: 80px"
      />
    </div>
    <!-- 实时数据 -->
    <div
      v-if="homeCardInfo.type == 'realTimeData' && homeCardInfo.isShow"
      style="padding:10px 25px 0 25px"
    >
      <el-row>
        <el-col
          v-for="(item, index) in homeCardInfo.content"
          :key="index"
          :span="8"
          :style="{
            padding:
              index == 2
                ? '0 0 25px 0'
                : index == 5
                ? '0 0 25px 0'
                : '0 25px 25px 0',
          }"
        >
          <div
            :style="item.type == 'direction' ? flexStyle : ''"
            style="border:1px solid #E9EDF3;height:100%;width:100%;padding:-0 10px 10px 10px"
          >
            <div>
              <p>{{ item.titel }}</p>
              <p
                v-if="item.type == 'direction'"
                style="margin:48px 0 0 0;color:#8592A5"
              >
                {{ item.windDirection }}
              </p>
            </div>
            <div v-if="item.type != 'direction'">
              <div
                style="display: flex;justify-content: space-between;color:#8592A5"
              >
                <p style="margin:10px 0">
                  {{ item.value
                  }}<span>{{
                    item.type == "voice"
                      ? " dB "
                      : item.type == "temperature"
                      ? " ℃ "
                      : ""
                  }}</span>
                </p>
                <p style="margin:10px 0" :style="{ color: item.color }">
                  {{ item.level }}
                </p>
              </div>
              <el-progress
                style="height:12px"
                :show-text="false"
                :percentage="
                  item.type == 'temperature' && item.value <= 0
                    ? (Math.abs(item.value + 9.9) / item.maxVal) * 100
                    : (item.value / item.maxVal) * 100
                "
                :color="item.customColors"
              ></el-progress>
            </div>
            <div v-else style="position: relative;height:80px;width:80px">
              <img
                :src="require(`@/assets/img/homeicon/direction.png`)"
                style="height:80px;width:80px;margin-top:14px"
              />
              <img
                :src="require(`@/assets/img/homeicon/pointer.png`)"
                style="height:30px;width:30px;position: absolute;top:38px;right:25px;"
                :style="{ transform: `rotate(${item.Angle}deg)` }"
              />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- 无数据 -->
    <NoData
      v-if="!homeCardInfo.isShow"
      style="margin-top:120px;width: 100%; height: 64px"
    />
  </div>
</template>

<script>
export default {
  props: {
    homeCardInfo: Object,
  },
  computed: {},
  data() {
    return {
      flexStyle: "display: flex;justify-content: space-between;",
    };
  },
  mounted() {},
  methods: {
    handleClick() {
      this.$router.push({
        name: "dust-projectwarn-projectwarn",
        query: {
          wardate: new Date().format("yyyy-MM-dd"),
        },
      });
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
<style>
.el-progress-bar__outer {
  height: 12px !important;
}
</style>

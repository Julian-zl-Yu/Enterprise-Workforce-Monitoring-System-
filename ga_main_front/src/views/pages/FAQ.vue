<template>
  <div style="width:100%;height:100%;background:#F1F4F5" v-loading="isLoading">
    <el-card
      v-if="isShow"
      class="header-box-card"
      :style="{
        backgroundImage:
          'url(' + require('@/assets/img/helpscout_template_bg.jpg') + ')',
      }"
    >
      <header
        style="max-width:1200px;margin:0 auto;padding: 0 20px 0;text-align:right;"
      >
        <!-- <el-button style="margin-right:10px" @click="closeWindow"
          >返回系统</el-button
        > -->
        <el-select
          v-model="searchId"
          @change="searchChange"
          clearable
          filterable
          style="min-width:250px"
          placeholder="请输入关键词进行筛选"
        >
          <el-option
            v-for="item in titleData"
            :key="item.id"
            :label="item.title"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </header>
    </el-card>
    <div v-if="isShow" id="content" style="width:100%;height:100%;">
      <main style="margin-top:20px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-scrollbar style="height: 100%;">
              <el-menu
                :default-active="defaultActive"
                class="el-menu-vertical-demo"
                :popper-append-to-body="false"
                style="height:80vh;border: solid 1px #e6e6e6;border-radius: 4px;box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);"
              >
                <el-menu-item-group>
                  <el-menu-item
                    v-for="(item, index) in titleData"
                    :index="index + ''"
                    :key="index"
                  >
                    <p
                      style="max-width:299px;margin:0;"
                      class="textNoWrap"
                      @click="clickTitle(item)"
                    >
                      {{ item.title }}
                    </p>
                  </el-menu-item>
                </el-menu-item-group>
              </el-menu>
            </el-scrollbar>
          </el-col>
          <el-col
            :span="18"
            style="height:80vh;overflow:auto"
            v-loading="isContentLoading"
          >
            <!-- <el-scrollbar style="height: 60vh;"> -->
            <el-card class="box-card right-header" style="position: fixed;">
              <span style="font-size:16px"> {{ contentData.title }}</span>
            </el-card>
            <main style="margin-top:80px;" class="right-main">
              <el-card class="box-card ql-container ql-snow">
                <div
                  class="ql-editor"
                  v-html="contentData.content"
                  style="overflow-x:scroll"
                ></div>
              </el-card>
            </main>
            <!-- </el-scrollbar> -->
          </el-col>
        </el-row>
      </main>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isShow: false,
      isLoading: true,
      isContentLoading: true,
      titleData: [],
      defaultActive: "0",
      searchId: "",
      titleId: "",
      contentData: {},
    };
  },
  mounted() {
    this.getTitleInfo();
  },
  updated(){
    window.document.getElementsByClassName("right-header")[0].style.width=window.document.getElementsByClassName("right-main")[0].clientWidth+'px'
  },
  methods: {
    // 获取标题信息
    async getTitleInfo() {
      try {
        let { data: res } = await this.$http.get(`/demo/news/getTitle`);
        if (res.code === 0) {
          this.titleData = res.data;
          this.titleId = res.data[0].id;
          this.getContentInfo();
        }
      } catch (error) {
        console.error(error);
      } finally {
      }
    },
    // 点击标题
    clickTitle(val) {
      this.titleId = val.id;
      this.getContentInfo();
    },
    // 搜索
    searchChange(val) {
      if (val) {
        this.titleId = val;
        this.getInfoIndex(this.titleData, val);
      }
      this.getContentInfo();
    },
    // 获取详细信息
    async getContentInfo() {
      try {
        this.isContentLoading = true;
        let { data: res } = await this.$http.get(`/demo/news/${this.titleId}`);
        if (res.code === 0) {
          this.contentData = res.data;
          this.isLoading = false;
          this.isContentLoading = false;
          this.isShow = true;
        }
      } catch (error) {
        console.error(error);
      } finally {
      }
    },
    // 关闭
    closeWindow() {
      this.$router.back(-1);
      window.close();
    },
    // 获取对应数据的index
    getInfoIndex(data, val) {
      data.filter((item, index) => {
        if (item.id == val) {
          this.defaultActive = index + "";
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.header-box-card {
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}
#content {
  max-width: 1200px;
  margin: 0 auto;
}
// 超出隐藏
.textNoWrap {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
::v-deep img {
  width: 100% !important;
}
</style>
<style scoped>
::v-deep .el-menu-item-group__title {
  height: 0;
  padding: 0 !important;
}
::-webkit-scrollbar {
  width: 4px;
}
/* 滚动槽 */
::-webkit-scrollbar-track {
  -webkit-box-shadow: inset006pxrgba(0, 0, 0, 0.3);
  border-radius: 10px;
}
/* 滚动条滑块 */
::-webkit-scrollbar-thumb {
  border-radius: 10px;
  /* background: rgba(0, 0, 0, 0.1); */
  background: rgb(206, 206, 206);
  -webkit-box-shadow: inset006pxrgba(0, 0, 0, 0.5);
}
/* ::-webkit-scrollbar-thumb:window-inactive {
background:rgba(255,0,0,0.4);
} */
::-webkit-scrollbar-corner {
  background: transparent;
}
</style>

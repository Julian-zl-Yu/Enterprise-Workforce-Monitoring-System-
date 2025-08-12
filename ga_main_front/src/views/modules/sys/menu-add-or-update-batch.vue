<template>
  <el-dialog
    :visible.sync="visible"
    :title="!dataForm.id ? $t('add') : $t('update')"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form
      ref="dataForm"
      :model="dataForm"
      :rules="dataRule"
      label-width="120px"
      @keyup.enter.native="dataFormSubmitHandle()"
    >
      <el-row>
        <el-col :span="20">
          <el-form-item :label="$t('menu.type')" prop="type" size="mini">
            <el-radio-group v-model="dataForm.type" disabled>
              <el-radio :label="0" v-if="dataForm.type == 0">
                {{ $t("menu.type0") }}
              </el-radio>
              <el-radio :label="1" v-if="dataForm.type == 1">
                {{ $t("menu.type1") }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="4" v-if="dataForm.type == 0">
          <el-button
            size="mini"
            type="success"
            icon="el-icon-plus"
            circle
            @click="addInfo"
          >
          </el-button>
        </el-col>
      </el-row>
      <el-form-item
        v-if="dataForm.type == 1"
        label="权限"
        size="mini"
      >
        <el-checkbox
          :indeterminate="isIndeterminate"
          v-model="checkAll"
          @change="handleCheckAllChange"
          >全选</el-checkbox
        >
        <div style="margin: 15px 0"></div>
        <el-checkbox-group
          v-model="checkedPermissions"
          @change="handleCheckedPermissionsChange"
        >
          <el-checkbox v-for="item in permissions" :label="item" :key="item">
            {{ item }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-row v-else v-for="(item, index) in dataFormArr" :key="index">
        <el-col :span="10">
          <el-form-item :label="$t('menu.name')" prop="name">
            <el-input v-model="item.name" :placeholder="$t('menu.name')" />
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item
            v-if="item.type === 0"
            :label="$t('menu.url')"
            prop="url"
          >
            <el-input
              v-model="item.url"
              :placeholder="$t('menu.url')"
              @change="changeUrl(item.url, index)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="4"> </el-col>
      </el-row>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">
        {{ $t("cancel") }}
      </el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">
        {{ $t("confirm") }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from "lodash/debounce";
import { getIconList } from "@/utils/index.ts";
const permissionsOptions = ["分页", "新增", "修改", "导出", "查看", "详情"];
export default {
  data() {
    return {
      visible: false,
      menuList: [],
      menuListVisible: false,
      iconList: [],
      iconListVisible: false,
      dataForm: {
        id: "",
        type: "",
        name: "",
        pid: "0",
        parentName: "",
        url: "",
        permissions: "",
        sort: 0,
        icon: "",
        whether: "1",
      },
      dataFormArrSort: 0,
      dataFormArr: [
        {
          icon: "icon-play-square",
          name: "",
          parentName: "",
          permissions:
            "safety:aj06:page,safety:aj06:info,safety:aj06:save,safety:aj06:update",
          pid: "",
          sort: 4,
          type: "",
          url: "",
          whether: "1",
        },
      ],

      checkAll: false,
      checkedPermissions: [],
      permissions: permissionsOptions,
      isIndeterminate: true,
    };
  },
  computed: {
    dataRule() {
      return {
        name: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        url: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        checkedPermissions: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "change",
          },
        ],
      };
    },
  },
  methods: {
    addInfo() {
      this.dataFormArr.push({
        icon: "icon-play-square",
        name: "",
        parentName: this.dataForm.name,
        permissions: "",
        pid: this.dataForm.id,
        sort: this.dataFormArr[this.dataFormArr.length - 1].sort + 1,
        type: this.dataForm.type,
        url: "",
        whether: "1",
      });
    },
    changeUrl(url, index) {
      let permissionsTmp = url
        .substring(0, url.lastIndexOf("/"))
        .slice(1)
        .replace(/\//g, ":");
      this.dataFormArr[
        index
      ].permissions = `${permissionsTmp}:page,${permissionsTmp}:save,${permissionsTmp}:update,${permissionsTmp}:info`;
    },
    // 获取当前排序
    getSort(menuList) {
      menuList.filter((item) => {
        if (item.id === this.dataForm.id && item.children?.length > 0) {
          return (this.dataFormArrSort =
            item.children[item.children.length - 1].sort);
        } else if (item.children?.length > 0) {
          return this.getSort(item.children);
        } else {
          return (this.dataFormArrSort = 0);
        }
      });
    },

    handleCheckAllChange(val) {
      this.checkedPermissions = val ? permissionsOptions : [];
      this.isIndeterminate = false;
    },
    handleCheckedPermissionsChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.permissions.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.permissions.length;
    },
    getPermissionsStr(type) {
      let str = "";
      switch (type) {
        case "分页":
          str = "page";
          break;
        case "新增":
          str = "save";
          break;
        case "修改":
          str = "update";
          break;
        case "导出":
          str = "export";
          break;
        case "查看":
          str = "info";
          break;
        case "详情":
          str = "detail";
          break;
      }
      return str;
    },
    setDataFormArr() {
      this.dataFormArr = this.checkedPermissions.map((item, index) => {
        return {
          icon: "",
          name: item,
          parentName: this.dataForm.name,
          permissions: `${this.dataForm.url
            .substring(0, this.dataForm.url.lastIndexOf("/"))
            .slice(1)
            .replace(/\//g, ":")}:${this.getPermissionsStr(item)}`,
          pid: this.dataForm.id,
          sort: index,
          type: 1,
          url: "",
          whether: "1",
        };
      });
    },
    init() {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        this.iconList = getIconList();
        this.dataForm.parentName = this.$t("menu.parentNameDefault");
        this.getMenuList().then(() => {
          if (this.dataForm.id) {
            this.getInfo();
          }
        });
      });
    },
    // 获取菜单列表
    getMenuList() {
      return this.$http
        .get("/sys/menu/list?type=0")
        .then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
          this.menuList = res.data;
        })
        .catch((error) => console.error(error));
    },
    // 获取信息
    getInfo() {
      this.$http
        .get(`/sys/menu/${this.dataForm.id}`)
        .then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
          res.data.type = this.dataForm.type;
          this.dataForm = {
            ...this.dataForm,
            ...res.data,
          };
          this.dataFormArr[0].parentName = res.data.name;
          this.dataFormArr[0].pid = res.data.id;
          this.dataFormArr[0].type = this.dataForm.type;
          this.getSort(this.$store.state.sidebarMenuList);
          this.dataFormArr[0].sort = this.dataFormArrSort + 1;
        })
        .catch((error) => console.error(error));
    },
    // 表单提交
    dataFormSubmitHandle: debounce(
      async function () {
        let flg = true;
        if (this.dataForm.type == 0) {
          await this.dataFormArr.filter((item, index) => {
            if (!item.name && !item.url) {
              this.dataFormArr.removeByValue(item);
            } else {
              if (!item.name) {
                flg = false;
                return this.$message.warning(`路由${index + 1}的名称不能为空!`);
              }
              if (!item.url) {
                flg = false;
                return this.$message.warning(`路由${index + 1}的URL不能为空!`);
              }
            }
          });
        } else {
          this.setDataFormArr();
        }
        if (!flg) {
          return false;
        }
        for (let i = 0; i < this.dataFormArr.length; i++) {
          this.$http
            .post("/sys/menu", this.dataFormArr[i])
            .then(({ data: res }) => {
              if (res.code !== 0) {
                return this.$message.error(res.msg);
              }
            })
            .catch((error) => console.error(error));
          if (i + 1 == this.dataFormArr.length) {
            this.$message({
              message: this.$t("prompt.success"),
              type: "success",
              duration: 500,
              onClose: () => {
                this.visible = false;
                this.$emit("refreshDataList");
              },
            });
          }
        }
      },
      1000,
      { leading: true, trailing: false }
    ),
  },
};
</script>

<style lang="scss">
.mod-sys__menu {
  .menu-list,
  .icon-list {
    .el-input__inner,
    .el-input__suffix {
      cursor: pointer;
    }
  }

  &-icon-popover {
    width: 458px;
    overflow: hidden;
  }

  &-icon-inner {
    width: 478px;
    max-height: 258px;
    overflow-x: hidden;
    overflow-y: auto;
  }

  &-icon-list {
    width: 458px;
    padding: 0;
    margin: -8px 0 0 -8px;

    > .el-button {
      padding: 8px;
      margin: 8px 0 0 8px;

      > span {
        display: inline-block;
        vertical-align: middle;
        width: 18px;
        height: 18px;
        font-size: 18px;
      }
    }
  }
}
</style>

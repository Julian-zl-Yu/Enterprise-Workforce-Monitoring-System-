<template>
  <SubPage>
    <Card header="用户信息">
      <el-form
        ref="dataForm"
        :model="dataForm"
        :rules="dataRule"
        autocomplete=off
        label-width="120px"
      >
        <el-form-item
          :label="$t('user.username')"
          prop="username"
        >
          <el-input
            v-model="dataForm.username"
            :placeholder="$t('user.username')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('user.deptName')"
          prop="deptId"
        >
          <FormItem
            v-model="dataForm.deptId"
            :configs="configsDept"
          />
          
        </el-form-item>
        <el-form-item
          :label="$t('user.password')"
          :class="{ 'is-required': !dataForm.id }"
          prop="password"
        >
          <el-input
            v-model="dataForm.password"
            auto-complete="new-password"
            :placeholder="$t('user.password')"
            type="password"
          />
        </el-form-item>
        <el-form-item
          :label="$t('user.confirmPassword')"
          :class="{ 'is-required': !dataForm.id }"
          prop="confirmPassword"
        >
          <el-input
            v-model="dataForm.confirmPassword"
            auto-complete="new-password"
            :placeholder="$t('user.confirmPassword')"
            type="password"
          />
        </el-form-item>
        <el-form-item
          :label="$t('user.realName')"
          prop="realName"
        >
          <el-input
            v-model="dataForm.realName"
            :placeholder="$t('user.realName')"
          />
        </el-form-item>
         <el-form-item
          label="用户类型"
          prop="userType"
          :rules="[{required:true ,message: '必填项不能为空',trigger: 'blur'}]"
        >
          <FormItem
            v-model="dataForm.userType"
            :configs="configsUserType"
          />
          
        </el-form-item>
        <el-form-item
          :label="$t('user.gender')"
          prop="gender"
        >
          <ren-radio-group
            v-model="dataForm.gender"
            dict-type="GENDER"
          />
        </el-form-item>
        <el-form-item
          :label="$t('user.email')"
          prop="email"
        >
          <el-input
            v-model="dataForm.email"
            :placeholder="$t('user.email')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('user.mobile')"
          prop="mobile"
        >
          <el-input
            v-model="dataForm.mobile"
            :placeholder="$t('user.mobile')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('user.roleIdList')"
          prop="roleIdList"
          class="role-list"
        >
          <el-select
            v-model="dataForm.roleIdList"
            :placeholder="$t('user.roleIdList')"
            multiple
          >
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('user.status')"
          prop="status"
          size="mini"
        >
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="0">
              {{ $t("user.status0") }}
            </el-radio>
            <el-radio :label="1">
              {{ $t("user.status1") }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </Card>
    <template slot="footer">
      <Btn :configs="{ type: BTN_TYPE.back, click: goMain }" />
      <Btn
        ref="submitBtn"
        :configs="configsBtnDebounce"
      />
    </template>
  </SubPage>
</template>

<script>
import debounce from "lodash/debounce";
import { isEmail, isMobile } from "@/utils/validate.ts";
import { mixinsSubPage, subPropCurrentView } from "@/mixins/subPage.ts";
import basePage from "@/mixins/basePage.ts";
import { FORM_ITEM_TYPE } from "@/components/Types";

import { BTN_TYPE } from "@/components/Types";

export default {
  mixins: [basePage, mixinsSubPage, subPropCurrentView],
  data() {
    const handleFormSubmit = this.dataFormSubmitHandle.bind(this);
    return {
      configsDept: {
        type: FORM_ITEM_TYPE.Dept,
      },
       configsUserType: {
       prop:"userType",
        label:"用户类型",
        type: FORM_ITEM_TYPE.Dict,
        bindId: "USER_TYPE"
      },
      configsBtnDebounce: {
        type: BTN_TYPE.save,
        textLoading: "正在提交...",
        debounceClick: handleFormSubmit,
      },
      visible: false,
      roleList: [],
      roleIdListDefault: [],
      dataForm: {
        id: "",
        username: "",
        deptId: "",
        deptName: "",
        password: "",
        confirmPassword: "",
        realName: "",
        gender: 0,
        email: "",
        mobile: "",
        roleIdList: [],
        status: 1,
      },
    };
  },
  computed: {
    dataRule() {
      var validatePassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          return callback(new Error(this.$t("validate.required")));
        }
        callback();
      };
      var validateConfirmPassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          return callback(new Error(this.$t("validate.required")));
        }
        if (this.dataForm.password !== value) {
          return callback(new Error(this.$t("user.validate.confirmPassword")));
        }
        callback();
      };
      var validateEmail = (rule, value, callback) => {
        if (value && !isEmail(value)) {
          return callback(
            new Error(
              this.$t("validate.format", { attr: this.$t("user.email") })
            )
          );
        }
        callback();
      };
      var validateMobile = (rule, value, callback) => {
        if (value && !isMobile(value)) {
          return callback(
            new Error(
              this.$t("validate.format", { attr: this.$t("user.mobile") })
            )
          );
        }
        callback();
      };
      return {
        username: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        deptId: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "change",
          },
        ],
        password: [{ validator: validatePassword, trigger: "blur" }],
        confirmPassword: [
          { validator: validateConfirmPassword, trigger: "blur" },
        ],
        realName: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        email: [{ validator: validateEmail, trigger: "blur" }],
        mobile: [{ validator: validateMobile, trigger: "blur" }],
      };
    },
  },
  created() {
    this.init();
  },
  methods: {
    async init() {
      await this.getRoleList();
      if (["update"].includes(this.currentView)) {
        this.dataForm.id = this.$route.query.id;
        this.getInfo(this.$route.query.id);
      }
    },
    // 获取角色列表
    getRoleList() {
      return this.$http
        .get("/sys/role/list")
        .then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
          this.roleList = res.data;
        })
        .catch((error) => console.error(error));
    },
    // 获取信息
    getInfo(id) {
      this.$http
        .get(`/sys/user/${id}`)
        .then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
          this.dataForm = {
            ...this.dataForm,
            ...res.data,
            roleIdList: [],
          };
          // 角色配置, 区分是否为默认角色
          for (var i = 0; i < res.data.roleIdList.length; i++) {
            if (
              this.roleList.filter(
                (item) => item.id === res.data.roleIdList[i]
              )[0]
            ) {
              this.dataForm.roleIdList.push(res.data.roleIdList[i]);
              continue;
            }
            this.roleIdListDefault.push(res.data.roleIdList[i]);
          }
        })
        .catch((error) => console.error(error));
    },
    // 表单提交
    dataFormSubmitHandle: debounce(
      function () {
        this.$refs["dataForm"].validate((valid) => {
          if (!valid) {
            return false;
          }
          this.$http[!this.dataForm.id ? "post" : "put"]("/sys/user", {
            ...this.dataForm,
            roleIdList: [
              ...this.dataForm.roleIdList,
              ...this.roleIdListDefault,
            ],
          })
            .then(({ data: res }) => {
              if (res.code !== 0) {
                return this.$message.error(res.msg);
              }
              this.$message({
                message: this.$t("prompt.success"),
                type: "success",
                duration: 500,
                onClose: () => {
                  this.$emit("refreshDataList");
                  this.$nextTick(() => this.goMain());
                },
              });
            })
            .catch((error) => console.error(error));
        });
      },
      1000,
      { leading: true, trailing: false }
    ),
  },
};
</script>

<style>
</style>
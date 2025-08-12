<template>
  <main>
    <div class="login-body elevation2">
      <div class="left-bg">
        <img src="./gongren.png" alt="">
        <p>{{$t("brand.lg")}}</p>
      </div>
      <el-form
        id="login-form"
        ref="dataForm"
        :model="dataForm"
        :rules="dataRule"
        status-icon
        @keyup.enter.native="$refs.submitBtn.debounceClick()"
      >

        <div id="title" class="title202004283944">欢迎登录</div>
        <el-form-item prop="username">
          <el-input
            v-model="dataForm.username"
            :placeholder="$t('login.username')"
          >
            <span slot="prefix" class="el-input__icon">
              <svg class="icon-svg" aria-hidden="true">
                <use xlink:href="#icon-user" />
              </svg>
            </span>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="dataForm.password"
            :placeholder="$t('login.password')"
            type="password"
          >
            <span slot="prefix" class="el-input__icon">
              <svg class="icon-svg" aria-hidden="true">
                <use xlink:href="#icon-lock" />
              </svg>
            </span>
          </el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <el-row :gutter="20">
            <el-col :span="14">
              <el-input
                v-model="dataForm.captcha"
                :placeholder="$t('login.captcha')"
              >
                <span slot="prefix" class="el-input__icon">
                  <svg class="icon-svg" aria-hidden="true">
                    <use xlink:href="#icon-safetycertificate" />
                  </svg>
                </span>
              </el-input>
            </el-col>
            <el-col :span="10" class="login-captcha">
              <img alt="captcha" :src="captchaPath" @click="getCaptcha()" />
            </el-col>
          </el-row>

        </el-form-item>

        <el-form-item>

          <Btn
            id="login-submit"
            ref="submitBtn"
            class="w-percent-100 displayib"
            :configs="configsBtnDebounce"
          />
        </el-form-item>
        <!-- <button @click="jumpXt">系统跳转</button> -->
        <div style="display: flex; justify-content: space-between;">
          <router-link class="register-link" :to="{ name: 'register' }">
            注册账户
        </router-link>

        <router-link class="register-link" :to="{ name: 'resetPassword' }">
          重置密码
        </router-link>
        </div>
          
      </el-form>
    </div>
  </main>
</template>
<script>
import Cookies from "js-cookie";
import { getSysDictTypeAll, getUUID } from "@/utils/index.ts";
import { required } from "@/utils/formRule";
import { SITE_CONFIG } from "@/main.config";
import { BTN_TYPE, LS_KEY } from "@/components/Types";
import ls from "@/utils/localStorageHelper";
import axios from "axios";

export default {
  name: "LoginForm",
  data() {
    let dataRule = {
      username: [required],
      password: [required],
      captcha: [required],
    };
    const dataFormSubmitHandle = this.dataFormSubmitHandle.bind(this);

    return {
      configsBtnDebounce: {
        type: BTN_TYPE.login,
        textLoading: "正在登录...",
        debounceClick: dataFormSubmitHandle,
      },
      captchaPath: "",
      dataForm: {
        username: "",
        password: "",
        uuid: "",
        captcha: "",
      },
      dataRule,
    };
  },
  mounted() {},
  created() {
    this.getCaptcha();
  },
  methods: {
    jumpXt(){
    
     
    },
    // 获取验证码
    getCaptcha() {
      this.dataForm.uuid = getUUID();
      this.captchaPath = `${SITE_CONFIG["apiURL"]}/captcha?uuid=${this.dataForm.uuid}`;
    },
    // 表单提交
    async dataFormSubmitHandle() {
      const valid = await this.$refs["dataForm"].validate();
      if (!valid) {
        throw new Error("未通过表单验证");
      }
      const { data: res } = await this.$http.post("/login", this.dataForm);
      if (res.code !== 0) {
        this.getCaptcha();
        return this.$message.error(res.msg);
      } else {
        Cookies.set(LS_KEY.token, res.data.token);
        sessionStorage.setItem(LS_KEY.token, res.data.token);
        sessionStorage.setItem(LS_KEY.isNamed, res.data.isNamed);
        sessionStorage.setItem(LS_KEY.cp0101, res.data.cp0101);
        await getSysDictTypeAll();
        return this.$router.replace({ name: "home" });
      }
    },
  },
};
</script>
<style lang="scss" scoped>
</style>

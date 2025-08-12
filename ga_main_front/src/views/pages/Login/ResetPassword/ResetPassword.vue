<template>
  <main class="register">
    <div class="el-card is-always-shadow elevation1">
      <div class="el-card__header elevation1">
        <h2 class="title202004283944">
          重置密码
        </h2>
        <router-link
          class="register-link"
          :to="{ path: '/login/form' }"
        >
          已有账户，直接登录
        </router-link>
      </div>
      <div class="el-card__body">
        <CForm
          ref="resetPasswordForm"
          :configs="configsCForm"
          @enter="$refs.submitBtn.submit()"
        />
      </div>
      <div class="el-card__foot" style="padding: 24px">
        <Btn
          ref="submitBtn"
          :configs="configsBtnDebounce"
        />
      </div>
    </div>
  </main>
</template>
<script>
import basePage from "@/mixins/basePage.ts";
import merge from "lodash/merge";
import { DSLFormItems, DSLFormItemRule } from "./dsl";
import { state } from "./state";
import { SuccessOrFailed } from "@/utils/index.ts";
import { BTN_TYPE } from "@/components/Types";

export default {
  mixins: [basePage],
  data() {
    const handleFormSubmit = this.handleFormSubmit.bind(this);
    return {
      BTN_TYPE,
      formData:state.state.formData,
      /*提交按钮*/
      configsBtnDebounce: {
        type: BTN_TYPE.save,
        textLoading: "正在提交...",
        debounceClick: handleFormSubmit,
      },
      configsCForm: {
        moduleName: "resetPassword",
        DSLFormItems,
        DSLFormItemRule,
        DSLFormLayout: [
           [{ p: "mobile", c: 3 }],
          [{ p: "verificationCode", c: 3 }]
        ],
      }
    };
  },
  mounted() {
   
  },
  watch: {
    'formData.mobile':function(newVal){
      DSLFormItems.verificationCode.smsPhone = newVal;
    }
  },
  methods: {
    // 表单提交
    async handleFormSubmit() {
      /* validateForm */
      await this.$refs["resetPasswordForm"]?.formValidate();
      /* 91310000775785552L */
      const { data: res } = await this.$http.post(
        "/reSetPassWord",
        merge(this.$store.state.resetPassword.formData)
      );
      return SuccessOrFailed(
        res,
        {
          msg: res.msg +  "即将跳转至登录界面" ,
          fn: () => {
            setTimeout(() => {
              this.$router.replace({ name: "home" })
            }, 1500);
          },
        },
        { msg: res.msg }
      );
    },
  },
};
</script>
<style lang="scss">
$color-main: #1eaac8;
$color-main-light: mix($--color-white, $color-main, 10%) !default;

.route-name-register {
  display: flex;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;

  section.header {
    z-index: 1;
  }

  section.main {
    flex: 1;
    overflow: hidden;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    align-items: center;

    > main.register {
      flex: 1;
      overflow: auto;

      > .el-card {
        height: 100%;
        display: flex;
        flex-flow: column nowrap;
        justify-content: flex-start;

        > .el-card__header {
          position: relative;
          z-index: 1;
        }

        > .el-card__body {
          flex: 1;
          overflow: auto;
          padding: 24px;
        }

        > .el-card__foot {
          padding: 24px;
          display: flex;
          justify-content: flex-end;

          .el-button--primary,
          .el-button--primary {
            background: $color-main;
            border-color: $color-main;
            color: #fff;

            @include elevation1;
            @include elevationtransition;
          }

          .el-button--primary:focus,
          .el-button--primary:hover {
            background: $color-main-light;
            border-color: $color-main-light;

            @include elevation2;
          }

          > button {
            float: right;
          }
        }
      }
    }
  }
}
.el-radio-group{
  text-align: left;
}
</style>

<style lang="scss" scoped>
$color-main: #1eaac8;

.register {
  // height: 100%;
  margin: 20px auto;
  justify-content: center;
  display: flex;
  flex-flow: row nowrap;
  justify-content: center;
  align-items: center;

  > .el-card {
    flex: 1;

    .el-card__header {
      display: flex;
      flex-flow: row nowrap;
      justify-content: center;
      align-items: center;
      position: relative;

      .register-link {
        position: absolute;
        right: 32px;
        bottom: 16px;
        color: $color-main;
      }
    }
  }
}
</style>
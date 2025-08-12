<template>
    <fragment>
        <template>
            <div class="smsRow">
                <el-input :placeholder="attrPlaceholder"   :value="value"  @input="handleValueChange"   ref="formItem" clearable></el-input>
                <el-button type="primary" @click="getSms" :disabled="btnDisabled">{{btnText}}</el-button>
            </div>
        </template>
    </fragment>
</template>

<script>
    import {
        model as baseModel,
        subCreated,
        subComputed,
        subProps,
        subComponents,
        subData,
    } from "@/components/Form/FormItem/mixinsFormItemBase";
    import dayjs from "dayjs";

    import {
        phone
    } from "@/utils/formRule";
    import { handleValueChange } from "@/components/Form/FormItem/commonFormItem";

    export default {
        name: "ImplSMScode",
        mixins: [
            subCreated,
            subData,
            baseModel,
            subProps,
            subComputed,
            subComponents,
        ],
        data() {
            return {
                btnText: '获取验证码',
                count: 120,
                btnDisabled: false,          
            };
        },
        computed: {
          
        },
        watch: {
           
        },
        created() {
         
        },
        methods: {
            getSms() {
                // 验证手机号
                let phoneNumber = this?.configs?.smsPhone;
                if(!phoneNumber){
                    return this.$message.error('请先输入手机号！');
                }
                phone.validator('', phoneNumber, async (e) => {
                    if (e) {
                        return this.$message.error(e);
                    }
                    const {
                        data: res
                    } = await this.$http.get(`${this?.configs?.smsUrl}/${phoneNumber}`, {});
                    if (res.code !== 0) {
                        return this.$message.error(res.msg);
                    } else {
                        this.$message.success(res.msg);
                        let timeout = setInterval(() => {
                            if (this.count < 1) {
                                this.btnDisabled = false;
                                this.btnText = '获取验证码';
                                this.count = 120;
                                clearInterval(timeout)
                            } else {
                                this.btnDisabled = true;
                                this.btnText = this.count-- + 's后重发'
                            }
                        }, 1000)
                    }
                })

            },
            init() {
                
            },
            handleValueChange,
        },
    };
</script>

<style>
    .smsRow{
        display: flex;
        justify-content: space-between;
    }
</style>
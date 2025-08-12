<template>
  <fragment>
    <!--isEdit {{ isEdit }} -->
    <!--canEdit {{canEdit}}-->
    <template v-if="canEdit">
      <el-input
        ref="formItem"
        class="money input-money"
        v-bind="$attrs"
        :disabled="attrDisabled"
        :value="value"
        @input="handleMoneyChange"
      >
        <i
          slot="prefix"
          class="el-input__icon"
        >{{ symbolPaire[0] }}</i>
        <i
          slot="suffix"
          class="el-input__icon"
        >{{ symbolPaire[1] }}</i>
      </el-input>
    </template>
    <FormItemReadonly
      v-else
      :is-table="isTable"
      :value="setReadonlyText(value)||value"
    />
  </fragment>
</template>

<script>
    import {handleMoneyChange} from "@/components/Form/FormItem/commonFormItem";
    import {
        model as baseModel,
        subCreated,
        subComputed,
        subProps,
        subComponents
    } from "@/components/Form/FormItem/mixinsFormItemBase";
    import {CURRENCY_TYPE} from "@/components/Types";

    export default {
        name: "ImplMoney",
        mixins: [subCreated, baseModel, subProps, subComputed, subComponents],
        data() {
            return {
                symbolPaire: ["¥", "RMB"]
            };
        },
        methods: {
            init() {
                if (this.configs.currency) {
                    this.$watch("configs.currency", currency => {
                            const symbolMap = {
                                "RMB": "¥",
                                "USD": "$",
                                "JPY": "J￥",
                                "EUR": "€",
                                "HKD": "HK＄"
                            };
                            const symbolTag = symbolMap[[CURRENCY_TYPE[currency]]] || symbolMap.RMB;
                            const symbolName = CURRENCY_TYPE[currency] || "RMB";
                            this.symbolPaire = [symbolTag, symbolName];
                        },
                        {immediate: true});
                }
            },
            handleMoneyChange,
            setReadonlyText(value) {
                this.textWhenReadOnly = `${this.symbolPaire[0]} ${value || ""} ${this.symbolPaire[1]}`;
                return false;
            }
        }
    };
</script>
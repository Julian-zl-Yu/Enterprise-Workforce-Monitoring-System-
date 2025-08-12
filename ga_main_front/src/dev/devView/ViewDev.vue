<template>
  <div>
    <div>
      <FormItem v-model="value" :configs="configs" />
      <img id="test" />
    </div>
  </div>
</template>

<script>
import { FORM_ITEM_TYPE } from "@/components/Types";
export default {
  data() {
    return {
      value: "",
      configs: {
        type: FORM_ITEM_TYPE.InputCardReader,
        done(res) {
          console.log(res)
        },
      },
    };
  },
};
</script>

<style>
</style>
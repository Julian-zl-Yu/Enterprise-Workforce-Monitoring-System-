<template>
  <fragment>
    <div class="form-item all-item wrapper">
      <div
        v-tips
        class="cell-span"
        :data-tips="label"
      >
        {{ label }}
      </div>
    </div>
    <span class="cell-span-hidden">{{ label }}</span>
  </fragment>
</template>
<script>
export default {
  name: "AsyncLabel",
  props: {
    value: {
      required: true,
      type: String
    }
  },
  data() {
    var _this = this;
    return {
      label: _this.value
    };
  },
  async mounted() {
    this.label = await this.$getAreaCodeLabel(this.value, true);
  },
  methods: {}
};
</script>
<style lang="scss">
</style>
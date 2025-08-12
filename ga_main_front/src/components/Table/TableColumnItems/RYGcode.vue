<template>
  <div class="R-Y-G-code">
    <el-image
      style="width: 30px; height: 30px;"
      :src="value == '00'? greenCodeUrl: value == '01'
          ? yellowCodeUrl
          : value == '10'
          ? redCodeUrl
          : ''"
    >
      <div slot="error" class="image-slot"></div>
    </el-image>
  </div>
</template>
<script>
export default {
  name: "RYGcode",
  props: {
    value: {
      required: true,
    },
  },
  data() {
    return {
      greenCodeUrl: require(`@/assets/img/code_imgs/green_code.png`),
      yellowCodeUrl: require(`@/assets/img/code_imgs/yellow_code.png`),
      redCodeUrl: require(`@/assets/img/code_imgs/red_code.png`),
       
    };
  },
  mounted() {},
  methods: {
    initRYGcode() {},
  },
};
</script>
<style lang="scss"></style>

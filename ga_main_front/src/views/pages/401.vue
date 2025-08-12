<template>
  <div class="app-wrapper app-page__not-found">
    <div class="app-content__wrapper">
      <div class="app-content">
        <h2 class="title">
          401
        </h2>
        <p
          class="desc"
          v-html="$t('noPermisDesc')"
        />
        <div class="btn-bar">
          <el-button
            type="primary"
            @click="goHome"
          >
            {{ $t("notFound.home") }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goHome() {
      this.$router.push({ path: "/home", query: { _t: Date.now() } });
    },
  },
};
</script>

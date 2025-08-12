import isArray from "lodash/isArray";
export let getLavelValue = {
  methods: {
    async getLabel(prop) {
      let formItemVM = this.$refs[`formItem_${prop}`];
      if (isArray(formItemVM) && formItemVM.length > 0) {
        formItemVM = formItemVM[0];
      }
      let formItem = formItemVM.$refs.formItem;
      return formItem.setReadonlyText(formItem.value)
    }
  },
}
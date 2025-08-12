import CForm from "./CForm";
import CFormQuery from "./CFormQuery";
import DynamicRowList from "./DynamicRowList";
import Uploader from "./Uploader/Uploader";
import Viewer from "./Viewer";
import FormItem from "./FormItem/FormItem";
import CLink from "./FormItem/implementFormItem/CLink.vue";


CForm.install = function (Vue) {
    Viewer.install(Vue);
    Vue.component(CLink.name, CLink);
    Vue.component(FormItem.name, FormItem);
    Vue.component(CForm.name, CForm);
    Vue.component(CFormQuery.name, CFormQuery);
    Vue.component(DynamicRowList.name, DynamicRowList);
    Vue.component(Uploader.name, Uploader);
    /*     Vue.component("jsx-example", {
            render(h) { // <-- h must be in scope
                return (
                    <h1 id="foo"> { this.$slots.default} </h1>
                );
            }
        });
     */    // Vue.component(DatePicker.name, DatePicker);
};

export default CForm;
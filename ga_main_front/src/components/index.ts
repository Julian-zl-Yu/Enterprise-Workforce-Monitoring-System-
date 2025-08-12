import renRadioGroup from "@/components/ren-radio-group";
import renSelect from "@/components/ren-select";
import renProcessMultiple from "@/components/ren-process-multiple";
import renProcessStart from "@/components/ren-process-start";
import renProcessRunning from "@/components/ren-process-running";
import renProcessDetail from "@/components/ren-process-detail";
import renDeptTree from "@/components/ren-dept-tree";
import renRegionTree from "@/components/ren-region-tree";
import CPage from "@/components/Page";
import CTable from "@/components/Table";
import CForm from "@/components/Form";
import Btn from "@/components/Button";
import layer from "@/components/Layer";
import Card from "@/components/Card";
import NoData from "@/components/common/NoData/NoData";
import ScrollToPane from "@/components/common/ScrollToPane/ScrollToPane";
const componentList = [
    renRadioGroup,
    renSelect,
    renProcessMultiple,
    renProcessStart,
    renProcessRunning,
    renProcessDetail,
    renDeptTree,
    renRegionTree,
    CPage,
    CForm,
    CTable,
    Btn,
    Card
];

export function ImportAllCustomizeComponent(Vue) {
    componentList.forEach(c => Vue.use(c));
    Vue.component(NoData.name, NoData);
    Vue.component(ScrollToPane.name, ScrollToPane);
    window.utils = window.utils || {};
    window.utils.layer = Vue.prototype.$layer = layer(Vue);
}
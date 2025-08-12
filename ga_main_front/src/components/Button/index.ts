import Btn from "./Btn";
import Btnmini from "./Btnmini";
import BtnGroup from "./BtnGroup";

Btn.install = function (Vue) {
    Vue.component(Btn.name, Btn);
    Vue.component(Btnmini.name, Btnmini);
    Vue.component(BtnGroup.name, BtnGroup);
};

export default Btn;

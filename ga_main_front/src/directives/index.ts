import VTips from "./v-tips";
import VTitle from "./v-title";

const directiveArray = [VTips, VTitle];
export let ImportAllCustomizeDirective = (Vue) => directiveArray.forEach(directive => Vue.directive(directive.name, directive.body));
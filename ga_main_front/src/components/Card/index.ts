import Card from "./Card";
import Breadcrumb from "./Breadcrumb";
Card.install = function (Vue) {
    Vue.component(Card.name, Card);
    Vue.component(Breadcrumb.name, Breadcrumb);
};
export default Card;
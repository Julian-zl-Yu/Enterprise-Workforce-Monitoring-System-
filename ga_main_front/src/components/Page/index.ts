import SubPage from "./SubPage";
import MainPage from "./MainPage";
import Paginationbar from "./Paginationbar";

SubPage.install = function (Vue) {
    Vue.component(MainPage.name, MainPage);
    Vue.component(SubPage.name, SubPage);
    Vue.component(Paginationbar.name, Paginationbar);
};

export default SubPage;

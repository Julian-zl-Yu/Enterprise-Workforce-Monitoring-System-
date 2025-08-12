import TableColumn from "./TableColumn";
import TableColumnCheckbox from "./TableColumnCheckbox";

TableColumn.install = function (Vue) {
    Vue.component(TableColumnCheckbox.name, TableColumnCheckbox);
    Vue.component(TableColumn.name, TableColumn);
};

export default TableColumn;
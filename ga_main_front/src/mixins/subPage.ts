import {
    isObjectEqual
} from "@/utils/iQuery";
import {
    getModuleNameFromRouteName
} from "@/router";



export let subPropCurrentView = {
    props: {
        currentView: {
            type: String,
            default: "detail",
        },
    },
};

export let mixinsSubPage = {
    data() {
        return {
            isFormLoading: false
        };
    },
    methods: {
        refreshSubPage() {
            this.SUB_PAGE_TAG = true;
            this.$nextTick(() => this.SUB_PAGE_TAG = false);
        },
        checkIsSameModule(tab) {
            return isObjectEqual(tab?.query, this.$route?.query);
        },
    }
};
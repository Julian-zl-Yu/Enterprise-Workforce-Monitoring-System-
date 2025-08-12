import merge from "lodash/merge";
export function goMain(query, path) {
    query = { _t: Date.now() };
    path = path || this.$route.path;
    this.$router.push({
        path,
        query
    });
}

export function goSub(target = {
    v: "add"
}) {
    let {
        name,
        params,
        query
    } = this.$route;
    query = merge({}, query, target);
    query._t = Date.now();
    this.$router.push({
        "name": name,
        "params": {
            ...params
        },
        "query": {
            ...query
        }
    });
}

export default {
    methods: {
        goMain,
        goSub
    }
};
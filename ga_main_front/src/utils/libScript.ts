import loadJS from "@ventose/utils-loadjs";
import {
    GLOBAL_LIB,
    STATUS_TYPE
} from "@/components/Types";
import Resolver from "@/utils/Resolver";

const LIB_MAP = {
    [GLOBAL_LIB.AMap]: "https://webapi.amap.com/maps?v=1.4.15&key=429273787f4cff744917409bb0e0489e&plugin=AMap.Geocoder&plugin=AMap.DistrictSearch&plugin=AMap.MouseTool",
    // [GLOBAL_LIB.AMap]: "https://webapi.amap.com/maps?v=1.4.15&key=429273787f4cff744917409bb0e0489e&plugin=AMap.Geocoder&plugin=AMap.DistrictSearch&plugin=AMap.MouseTool",
    [GLOBAL_LIB.AMapUI]: "https://webapi.amap.com/ui/1.1/main.js"
};

const loadedLibMap = {};
let resolverCollection = {};

export async function GetLib(libName) {
    try {
        let targetLib = loadedLibMap[libName];
        if (targetLib) {
            if (targetLib !== STATUS_TYPE.pendding) {
                return targetLib;
            } else {
                return new Promise((resolve) => resolverCollection[libName].push(resolve));
            }
        } else {
            loadedLibMap[libName] = STATUS_TYPE.pendding;
            resolverCollection[libName] = resolverCollection[libName] || new Resolver();
        }

        await loadJS([
            [libName, LIB_MAP[libName]]
        ]);
        let res = window[libName];
        if (res) {
            loadedLibMap[libName] = res;
            resolverCollection[libName].trigger(res, () => resolverCollection[libName] = null);
            return res;
        } else {
            throw new Error(`failed load ${libName} from ${LIB_MAP[libName]}`);
        }
    } catch (error) {
        console.error(error);
    }
}
import reduce from "lodash/reduce";
import isPlainObject from "lodash/isPlainObject";
import isUndefined from "lodash/isUndefined";
import dayjs from "dayjs";
import isFinite from "lodash/isFinite";
import { getKVCacheValue, setKVCacheValue } from "./dataBaseTable/KeyValueCache";
type typeObj = {
    [key: string]: any
};
export function transformToServerData(originApi: any, frontData: any, prefix = ""): any {
    const targetParams: typeObj = {};
    return reduce(originApi, (taraget, value, keyName: string) => {
        /*如果是字符串代表一级*/
        const frontDataKeyName = prefix ? `${prefix}_${keyName}` : keyName;
        let targetValue = frontData[frontDataKeyName];
        if (isUndefined(targetValue)) {
            targetValue = value;
        }
        if (isPlainObject(value)) {
            /*如果是对象代表嵌套级*/
            taraget[keyName] = transformToServerData(originApi[keyName], frontData, keyName);
        } else {
            taraget[keyName] = targetValue;
        }

        return taraget;
    }, targetParams);
}

export function flattenToDefaultFormData(targetData: any, originApi: any, prefix = ""): any {
    return reduce(originApi, (target, value, keyName) => {
        const targetKeyName = prefix ? `${prefix}_${keyName}` : keyName;
        if (isPlainObject(value)) {
            return flattenToDefaultFormData(targetData, value, targetKeyName);
        } else {
            target[targetKeyName] = value ? value : isFinite(value) ? value : "";
            return target;
        }
    }, targetData);
}

export const toDate = (dateString: string, format = "YYYY-MM-DD") => dayjs(dateString).format(format);


export async function DevStoreForm(moduleName: string, data: any) {
    await setKVCacheValue(`DevStoreForm_${moduleName}`, data);
}
export async function DevRestoreForm(moduleName: string, fn: Function) {
    fn(await getKVCacheValue(`DevStoreForm_${moduleName}`));
}
import { getNationByChineseName, addBase64ImageHeader, } from "@/utils/index.ts";
import { toDate, } from "@/utils/transformData";
export { readCard } from "./idCardReaderHuaShi";
// export { readCard } from "./idCardReaderJingLun"

export function getPersonInfoFrom(info) {
  const headimageurl = info.identityPic.trim() != '' ? addBase64ImageHeader(info.identityPic) : '';
  const name = info.partyName;
  const idcardnumber = info.certNumber;
  const gender = String(info.gender);
  const bornDay = info.bornDay;
  /* 住址 */
  const address = info.certAddress;
  /* 民族 */
  const nation = getNationByChineseName(info.nation);
  /* (1-中国居民身份证，50-外国人永久居住证证，54-台湾居住证，55-港澳居住证) */
  const idcardtype = info.certType === 1 ? "1" : "2";
  /* 发证机关 */
  const grantorg = info.certOrg;
  const startdate = toDate(info.effDate);
  const isForever = !/^[\d]+$/.test(info.expDate);
  const expirydate = toDate(
    isForever ? "29991230" : info.expDate
  );
  /* 出生日期 */
  const birthday = toDate(info.bornDay);

  return {
    bornDay,
    gender,
    headimageurl,
    name,
    idcardnumber,
    address,
    nation,
    idcardtype,
    grantorg,
    startdate,
    expirydate,
    birthday,
  };
}
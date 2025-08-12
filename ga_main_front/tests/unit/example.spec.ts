import { findNodeFromTreeById } from "@/utils/index";
import { treeData } from "@/mock/tree";


type leaf = { name: string, id: string };

describe("findNodeFromTreeById", () => {
  test("find 1254252761198751746 name is 银行机构", (done) => {
    const id = "1254252761198751746";
    const name = "银行机构";
    const resNode = findNodeFromTreeById<leaf>(treeData, id);
    if (resNode && resNode.name) {
      expect(resNode.name).toBe(name);
      done();
    } else {
      done("find noting");
    }
  });
});
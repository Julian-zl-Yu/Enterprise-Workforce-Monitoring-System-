export const getMdFile = fileName => {
    const SWITCH_MAP = {
        "readme": async () => await import("./README.md"),
        "todoList": async () => await import("./ToDoList.md"),
        "test": async () => await import("./test.md"),
        "demo": async () => await import("./demo.md")
    };
    return SWITCH_MAP[fileName]();
};
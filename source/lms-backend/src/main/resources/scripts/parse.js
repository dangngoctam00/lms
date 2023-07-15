const fs = require("fs").promises;
const path = require("path");

console.log("Command args: ", process.argv);
const LMS_BACKEND_DIR = process.argv[2];
const LMS_FRONTEND_DIR = process.argv[3];

// const enumClassPath = path.join(__dirname, "test.txt");
// const outTsFilePath = path.join(__dirname, "output.ts");
// /home/thinh/IdeaProjects/lms-backend/src/main/java/com/example/lmsbackend/enums/PermissionEnum.java
const enumClassPath = path.join(LMS_BACKEND_DIR, "src", "main", "java", "com", "example", "lmsbackend", "enums", "PermissionEnum.java");
const outTsFilePath = path.join(LMS_FRONTEND_DIR, "src", "typescript", "interfaces", "generated", "PermissionMap.ts");

async function readFile(filePath) {
    try {
        const data = await fs.readFile(filePath);
        return data;
    } catch (error) {
        console.error(`Got an error trying to read the file: ${error.message}`);
    }
}

const permissionTermToTsDecl = (term) => {
    return `${term} = "${term}",`;
};
const addOneTabToLine = (line) => {
    return `  ${line}`;
};
const parseEnumclassToTS = async () => {
    const data = await readFile(enumClassPath);
    const fileContent = data.toString();
    let transformedDecl = Array.from(fileContent
        .matchAll(/([A-Z])(([A-Z]*)_([A-Z]*))+[A-Z]\(/g))
        .map(e => e[0].slice(0, -1))
        .map(permissionTermToTsDecl);
    // .forEach(decl => console.log(decl));
    console.log(transformedDecl);
    const decls = transformedDecl.map(addOneTabToLine).join("\n");
    const output = `import LimitPermission from "./LimitPermission"
export enum PermissionEnum {
${decls}
}
type PermissionMap = { 
  [key in PermissionEnum]: LimitPermission
}
export default PermissionMap`;
    console.log(output);
    try {
        await fs.writeFile(outTsFilePath, output);
    } catch (err) {
        console.log(err);
    }
};

parseEnumclassToTS();

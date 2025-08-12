package io.renren.common.utils;


import io.renren.common.exception.RenException;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp01.service.BCp01Service;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.sys.dto.SysDeptDTO;
import io.renren.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 通用的工具类
 *
 * @Author huangxi
 * @Date 2020-05-29 9:20
 */
@Component
public class CommonUtils {

    @Autowired
    private BCp01Service bCp01Service;
    @Autowired
    private SysDeptService sysDeptService;
    private static CommonUtils commonUtils;

    @PostConstruct
    public void init() {
        commonUtils = this;
    }

    /**
     * 查询当前登录用户的企业信息
     * <p>
     * 应用场景：
     * 企业用户办理业务的时候，业务表插入企业ID或者企业的其它字段信息
     * </P>
     *
     * @return dto
     */
    public static BCp01Entity userCpInfo() {
        Long deptId = SecurityUser.getDeptId();
        //查询机构类型
        SysDeptDTO sysDeptDTO = commonUtils.sysDeptService.get(SecurityUser.getDeptId());
        if(sysDeptDTO==null){
            return null;
        }
        if("0".equals(sysDeptDTO.getOrgtype())){//监管
            throw new RenException("监管机构没有绑定的企业，请用企业账号操作");
        }
        BCp01Entity userCpInfo = commonUtils.bCp01Service.getUserCpInfo(deptId);
        return userCpInfo;
    }

    /**
     * 压缩文件
     *
     * @param files   文件路径集合
     * @param zipname 压缩名
     */
//    public static void writePdfZip(List<String> files, String zipname, HttpServletResponse response) {
//        String fileName = zipname + ".zip";
//        BufferedInputStream bis = null;
//        File file1 = null;
//        try (OutputStream os = response.getOutputStream(); ZipOutputStream zos = new ZipOutputStream(os)) {
//            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Content-type", "text/html;charset=UTF-8");
//            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
//            byte[] buf = new byte[8192];
//            int len;
//            FileProper fileProper = new FileProper();
//            String toSavePdfPath = fileProper.getPath() + System.currentTimeMillis();
//            for (int i = 0; i < files.size(); i++) {
//                File file = new File(files.get(i));
//                if (!file.isFile()) continue;
//                String fileNames = file.getName();
//                int i1 = 0;
//                if (fileProper.getPath().indexOf("home") != -1) {
//                    i1 = file.getAbsolutePath().lastIndexOf("/");
//                } else {
//                    i1 = file.getAbsolutePath().lastIndexOf("\\");
//                }
//                // 找到文件类型
//                int dot = file.getName().lastIndexOf(".");
//                String newFilePath = file.getAbsolutePath().substring(0, i1);
//                file1 = new File(toSavePdfPath);
//                if (!file1.exists()) {
//                    file1.mkdirs();
//                }
//                File file2pdf = WordToPdfUtil.file2pdf(newFilePath, fileNames.substring(0, dot), fileNames.substring(dot + 1), toSavePdfPath);
//                ZipEntry ze = new ZipEntry(file2pdf.getName());
//                zos.putNextEntry(ze);
//                bis = new BufferedInputStream(new FileInputStream(file2pdf));
//                while ((len = bis.read(buf)) > 0) {
//                    zos.write(buf, 0, len);
//                }
//                if (null != bis) bis.close();  //关闭
//            }
//            if (null != zos) zos.close();  //关闭
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 删除文件
//        String delFilePath = file1.getAbsolutePath();
//        delDir(new File(delFilePath));
//    }
//
//    // 删除多余文件
//    public static void delDir(File file) {
//        if (file.isDirectory()) {
//            File zFiles[] = file.listFiles();
//            for (File file2 : zFiles) {
//                delDir(file2);
//            }
//            file.delete();
//        } else {
//            try {
//                file.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


}

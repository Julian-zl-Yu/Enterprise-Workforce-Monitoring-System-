package io.renren.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemUtil;
import io.renren.common.config.FileProperties;
import io.renren.common.exception.RenException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static cn.hutool.system.SystemUtil.OS_NAME;

/**
 * 文件上传工具
 *
 * @Author huangxi
 * @Date 2020-05-20 10:52
 */
@Component
public class FilesUploadUtils {

    private static String filePath;

    public FilesUploadUtils(FileProperties properties) {
        FilesUploadUtils.filePath = properties.getPath();
    }


    /**
     * 单文件上传
     *
     * @param file 上传的文件
     * @return 文件的保存路径
     */
    public static String uploadFile(MultipartFile file) {
        //获取系统
        String os = SystemUtil.get(OS_NAME).toLowerCase();
        // 获取文件扩展名
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
        String uuid = IdUtil.simpleUUID();
        //重新生成新的文件名
        String saveFileName = uuid + "." + extension;
        String currentDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String uploadPath = filePath + currentDate + File.separator;
        FileUtil.mkdir(uploadPath);
        File writeFile = new File(uploadPath, saveFileName);
        try {
            file.transferTo(writeFile);
        } catch (IOException e) {
            throw new RenException("文件上传失败");
        }
        return "/" + currentDate + "/" + saveFileName;
    }

}

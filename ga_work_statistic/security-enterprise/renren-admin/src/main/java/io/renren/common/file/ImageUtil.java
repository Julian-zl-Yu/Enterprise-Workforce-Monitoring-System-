package io.renren.common.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.common.exception.RenException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * 图片工具类
 *
 * @Author huangxi
 * @Date 2020-06-03 15:38
 */
@Component
public class ImageUtil {
    private static String filePath;

    public ImageUtil(FileProper properties) {
        ImageUtil.filePath = properties.getPath();
    }

    /**
     * base64字符串转image储存到服务器
     *
     * @param base64 图片base64字符串
     * @return 数据库储存的路径
     */
    public static String base64ToImage(String base64) {
        String saveFileName;
        String currentDate;
        try {
            //去掉header信息
            base64 = StrUtil.replace(base64, "data:image/png;base64,", "");
            byte[] decode = Base64.getMimeDecoder().decode(base64);
            //生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
            String uuid = IdUtil.simpleUUID();
            //重新生成新的文件名
            saveFileName = uuid + ".PNG";
            currentDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
            String uploadPath = filePath + currentDate + File.separator;
            FileUtil.mkdir(uploadPath);
            //判断目录是否存在
            FileUtil.mkdir(uploadPath);
            FileWriter writer = new FileWriter(uploadPath + saveFileName);
            writer.write(decode, 0, decode.length);
        } catch (IORuntimeException e) {
            throw new RenException("解码失败，请输入正确的Base64字符串");
        }

        return "/" + currentDate + "/" + saveFileName;
    }


    public static String getJpgImagePathBySelf(String base64,String selfPath) {
        String saveFileName;
        String currentDate;
        try {
            //去掉header信息
            base64 = StrUtil.replace(base64, "data:image/jpeg;base64,", "");
//                    .replaceAll("\r|\n", "").trim();

            byte[] decode = Base64.getDecoder().decode(base64);

            String uuid = IdUtil.simpleUUID();

            //重新生成新的文件名
            saveFileName = uuid + ".jpg";
            currentDate = selfPath + "/" + DateUtil.format(DateUtil.date(), "yyyyMMdd");
            String uploadPath = filePath  + currentDate + File.separator;
            FileUtil.mkdir(uploadPath);
            //判断目录是否存在
            FileUtil.mkdir(uploadPath);
            FileWriter writer = new FileWriter(uploadPath + saveFileName);
            writer.write(decode, 0, decode.length);
        } catch (IORuntimeException e) {
            throw new RenException("解码失败，请输入正确的Base64字符串");
        }

        return "/" + currentDate + "/" + saveFileName;
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param path 图片路径
     * @return base64字符串
     */
    public static String imageToBase64(String path) {
        byte[] data;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            throw new RenException("图片转换失败:" + e.getMessage());
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}

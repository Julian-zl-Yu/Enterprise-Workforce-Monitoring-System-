package io.renren.common.file;

import io.renren.common.constant.Constant;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author hx
 * @date 2020-7-3 11:15:54
 */
@Data
@Component
public class FileProper {
    /**
     * mac系统储存路径
     */
    private final String macPath = "~/upload/";
    /**
     * linux系统储存路径
     */
    private final String linuxPath = "/u02/upload/";
    /**
     * windows系统储存路径
     */
    private final String windowsPath = "D:/upload/";

    public String getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(Constant.WIN)) {
            return windowsPath;
        } else if (os.toLowerCase().startsWith(Constant.MAC)) {
            return macPath;
        }
        return linuxPath;
    }

}

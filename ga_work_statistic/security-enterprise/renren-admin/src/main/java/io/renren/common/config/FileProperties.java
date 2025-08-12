package io.renren.common.config;

import io.renren.common.constant.Constant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hx
 * @date 2020-7-3 11:15:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String macPath;
    private String linuxPath;
    private String windowsPath;

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

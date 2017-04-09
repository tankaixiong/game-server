package tank.http.common;

import java.io.File;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/9
 * @Version: 1.0
 * @Description:
 */
public class UploadFile {

    private File file;

    private String orgFileName;


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }
}

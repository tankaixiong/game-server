package tank.http.handler;

import org.apache.commons.io.FileUtils;
import tank.http.common.HttpAction;
import tank.http.common.HttpMethod;
import tank.http.common.UploadFile;
import tank.http.work.HttpRequest;

import java.io.File;
import java.io.IOException;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/7
 * @Version: 1.0
 * @Description:
 */
public class HttpDemoHandler {

    @HttpAction(url = "/test/demo", method = HttpMethod.GET)
    public Object demo(HttpRequest request) {


        return "test";
    }

    @HttpAction(url = "/test/file")
    public Object uploadFile(HttpRequest request) {


        UploadFile file = request.getFile("file");

        try {
            byte[] fileInput = FileUtils.readFileToByteArray(file.getFile());

            FileUtils.writeByteArrayToFile(new File("G://" + file.getOrgFileName()), fileInput);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "上传成功!";
    }
}

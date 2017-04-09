package tank.http.work;

import tank.http.common.HttpMethod;
import tank.http.common.UploadFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/7
 * @Version: 1.0
 * @Description:
 */
public class HttpRequest {

    private String url;
    private String uri;

    private HttpMethod httpMethod;

    private Map<String, List<String>> params = new HashMap<>();

    Map<String, List<UploadFile>> fileParams = new HashMap<>();


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public <T> T getParam(String name) {
        return (T) params.get(name).get(0);
    }

    public List<String> getParams(String name) {
        return params.get(name);
    }

    public Map<String, List<UploadFile>> getFileParams() {
        return fileParams;
    }

    public void addFile(String name, UploadFile file) {
        List<UploadFile> fileList = fileParams.get(name);
        if (fileList == null) {
            fileList = new ArrayList<>();
            fileParams.put(name, fileList);
        }
        fileList.add(file);
    }

    public UploadFile getFile(String name) {
        return fileParams.get(name).get(0);
    }

    public List<UploadFile> getFiles(String name) {
        return fileParams.get(name);
    }

    public void addParam(String name, String val) {
        List<String> list = params.get(name);
        if (list == null) {
            list = new ArrayList<>();
            params.put(name, list);
        }
        list.add(val);
    }

    public void addParams(Map<String, List<String>> queryParams) {
        params.putAll(queryParams);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}



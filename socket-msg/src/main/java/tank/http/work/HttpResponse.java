package tank.http.work;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/9
 * @Version: 1.0
 * @Description:
 */
public class HttpResponse {


    private Object data;

    private String contentType;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}

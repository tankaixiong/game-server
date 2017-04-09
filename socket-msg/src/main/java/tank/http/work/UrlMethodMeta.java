package tank.http.work;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/7
 * @Version: 1.0
 * @Description:
 */
public class UrlMethodMeta {

    private String methodName;

    private String url;

    private String contextType;

    private MethodAccess methodAccess;

    private Object handlerObj;


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public void setMethodAccess(MethodAccess methodAccess) {
        this.methodAccess = methodAccess;
    }

    public Object getHandlerObj() {
        return handlerObj;
    }

    public void setHandlerObj(Object handlerObj) {
        this.handlerObj = handlerObj;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }
}

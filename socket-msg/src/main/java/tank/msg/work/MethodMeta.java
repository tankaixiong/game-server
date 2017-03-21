package tank.msg.net;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/20
 * @Version: 1.0
 * @Description:
 */
public class MethodMeta {
    private String name;
    private MethodAccess access;
    private Integer type;
    //private Class clazz;
    private Object handlerObj;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MethodAccess getAccess() {
        return access;
    }

    public void setAccess(MethodAccess access) {
        this.access = access;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getHandlerObj() {
        return handlerObj;
    }

    public void setHandlerObj(Object handlerObj) {
        this.handlerObj = handlerObj;
    }
}

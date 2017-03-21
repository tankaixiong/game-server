package tank.msg.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description:
 */
public abstract class AbstractSession implements SocketSession {

    private ConcurrentHashMap<String, Object> attrMap = new ConcurrentHashMap<>();

    @Override
    public void setAttr(String key, Object value) {
        attrMap.put(key, value);
    }

    @Override
    public <T> T getAttr(String key) {
        return (T) attrMap.get(key);
    }

    @Override
    public void removeAttr(String key) {
        attrMap.remove(key);
    }
}

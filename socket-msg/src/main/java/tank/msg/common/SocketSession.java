package tank.msg.common;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description:
 */
public interface SocketSession {

    void write(Object obj);

    void write(Object obj, CallBack callBack);

    <T> T getAttr(String key);

    void setAttr(String key, Object value);

    void removeAttr(String key);

    long getId();

}

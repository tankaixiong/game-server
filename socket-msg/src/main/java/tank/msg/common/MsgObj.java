package tank.msg.common;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/14
 * @Version: 1.0
 * @Description: 请求数据包装对象
 */
public class MsgObj {

    private SocketSession session;
    private byte[] data;

    public MsgObj(SocketSession session, byte[] data) {
        this.session = session;
        this.data = data;
    }

    public SocketSession getSession() {
        return session;
    }

    public void setSession(SocketSession session) {
        this.session = session;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

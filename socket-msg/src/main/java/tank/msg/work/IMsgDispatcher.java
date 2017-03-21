package tank.msg.net;

import tank.msg.common.SocketSession;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/10
 * @Version: 1.0
 * @Description:
 */
public interface IMsgDispatcher {
    void handler(SocketSession session, byte[] data);
}

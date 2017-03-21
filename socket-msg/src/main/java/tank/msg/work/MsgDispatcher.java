package tank.msg.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.IParser;
import tank.msg.code.MsgEntity;
import tank.msg.common.SocketSession;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description: 消息派发
 */
public class MsgDispatcher implements IMsgDispatcher {

    private Logger LOG = LoggerFactory.getLogger(MsgDispatcher.class);

    private IParser parser;

    public void setParser(IParser parser) {
        this.parser = parser;
    }

    public MsgDispatcher(IParser parser) {
        this.parser = parser;
    }

    public void handler(SocketSession session, byte[] data) {


        //LOG.info("处理业务逻辑消息 :{}", data);

        MsgEntity msgEntity = parser.decode(data);

        //int type = msgEntity.getType();
        Object obj = RequestManager.execute(msgEntity.getType(), session, msgEntity.getData());

        byte[] out = parser.encode(new MsgEntity(msgEntity.getType(), obj));
        session.write(out);
    }


}

package tank.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.common.Session;
import tank.msg.protoc.MyMsg;
import tank.msg.work.RequestMethod;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/20
 * @Version: 1.0
 * @Description:
 */
public class DemoHandler {
    private Logger LOG = LoggerFactory.getLogger(DemoHandler.class);

    @RequestMethod(type = RequestType.LOGIN)
    public Object login(Session session, Object data) {
        LOG.info("处理登录请求");
        LOG.info("收到数据:{}", data);

        //session.getChannel().close();

        return new MyMsg(999, "服务器返回数据");
    }
}

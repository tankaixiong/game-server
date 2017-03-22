package tank.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.common.Session;
import tank.msg.protoc.Packet;
import tank.msg.work.RequestMethod;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/20
 * @Version: 1.0
 * @Description:
 */
public class DemoProtobufHandler {
    private Logger LOG = LoggerFactory.getLogger(DemoProtobufHandler.class);

    @RequestMethod(type = RequestType.LOGIN)
    public Object login(Session session, byte[] data) throws InvalidProtocolBufferException {
        LOG.info("处理登录请求");

        //session.getChannel().close();
        Packet.LoginRequest request = Packet.LoginRequest.parseFrom(data);
        LOG.info("\n{}", TextFormat.printToString(request));


        Packet.LoginResponse.Builder response = Packet.LoginResponse.newBuilder();
        response.setName(request.getName());
        response.setIsSuccess(true);
        response.setId(request.getId());

        return response.build();
    }
}

package tank.msg.code;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import tank.msg.common.Constant;

import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/27
 * @Version: 1.0
 * @Description:
 */
public class MsgEncoder extends MessageToMessageEncoder<byte[]> {


    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        if (msg == null || msg.length == 0) {
            return;
        }
        out.add(Unpooled.buffer(msg.length + Constant.DELIMITER.length).writeBytes(msg).writeBytes(Constant.DELIMITER));
    }
}

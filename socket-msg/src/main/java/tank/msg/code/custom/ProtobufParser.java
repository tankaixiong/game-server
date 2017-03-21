package tank.msg.code.custom;

import com.google.protobuf.MessageLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.IParser;
import tank.msg.code.MsgEntity;

import java.nio.ByteBuffer;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/21
 * @Version: 1.0
 * @Description:
 */
public class ProtobufParser implements IParser {

    private static Logger LOG = LoggerFactory.getLogger(JsonParser.class);

    @Override
    public MsgEntity decode(byte[] data) {
        if (data.length < 4) {
            LOG.error("消息解析异常");
        }

        int type = (data[0] << 24) & 0xff000000 |
                (data[1] << 16) & 0x00ff0000 |
                (data[2] << 8) & 0x0000ff00 |
                (data[3] << 0) & 0x000000ff;

        //int b=ByteBuffer.wrap(data,0,4).getInt();//同样有效
        byte[] subArray = new byte[data.length - 4];
        ByteBuffer.wrap(data, 4, data.length - 4).get(subArray);

        //byte[] subArray = Arrays.copyOfRange(data, 4, data.length);//

        return new MsgEntity(type, subArray);
    }

    @Override
    public byte[] encode(MsgEntity entity) {

        int type = entity.getType();

        MessageLite msg = null;


        if (entity.getData() instanceof MessageLite) {
            msg = (MessageLite) entity.getData();
        } else if (entity.getData() instanceof MessageLite.Builder) {
            msg = ((MessageLite.Builder) entity.getData()).build();
        }


        byte[] dataByte = msg.toByteArray();

        return ByteBuffer.allocate(4 + dataByte.length).putInt(type).put(dataByte).array();
    }
}

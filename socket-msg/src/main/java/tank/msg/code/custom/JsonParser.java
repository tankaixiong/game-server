package tank.msg.code.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.IParser;
import tank.msg.code.MsgEntity;
import tank.msg.utils.JsonUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/20
 * @Version: 1.0
 * @Description: 消息格式 type(int),byte[](数据)
 */
public class JsonParser implements IParser {

    private static Logger LOG = LoggerFactory.getLogger(JsonParser.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public MsgEntity decode(byte[] data) {

        if (data.length < 4) {
            LOG.error("消息解析异常");
        }

        int type = (data[0] << 24) & 0xff000000 |
                (data[1] << 16) & 0x00ff0000 |
                (data[2] << 8) & 0x0000ff00 |
                (data[3] << 0) & 0x000000ff;

        //int b=ByteBuffer.wrap(data,0,4).getInt();//同样有效
        byte[] subArray =new byte[data.length-4];
        ByteBuffer.wrap(data,4,data.length-4).get(subArray);

        //byte[] subArray = Arrays.copyOfRange(data, 4, data.length);//
        String dataStr = new String(subArray, UTF_8);

        return new MsgEntity(type, dataStr);

    }

    public byte[] encode(MsgEntity entity) {

        int type = entity.getType();

        String data = JsonUtil.toJson(entity.getData());
        byte[] dataByte = data.getBytes(UTF_8);

        return ByteBuffer.allocate(4 + dataByte.length).putInt(type).put(dataByte).array();

    }

    public static void main(String[] args) {

        JsonParser jsonParser = new JsonParser();


        byte[] bytes = jsonParser.encode(new MsgEntity(1, "测试"));
        System.out.println(bytes);

        MsgEntity msgEntity = jsonParser.decode(bytes);
        System.out.println(msgEntity);

    }

}

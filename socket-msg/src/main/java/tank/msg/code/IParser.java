package tank.msg.code;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/21
 * @Version: 1.0
 * @Description:
 */
public interface IParser {

    public MsgEntity decode(byte[] data);

    public byte[] encode(MsgEntity entity);
}

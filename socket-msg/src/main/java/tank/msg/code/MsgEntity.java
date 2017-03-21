package tank.msg.code;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/21
 * @Version: 1.0
 * @Description:
 */
public class MsgEntity {
    private int type;
    private Object data;

    public MsgEntity(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgEntity{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}

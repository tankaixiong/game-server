package tank.msg.code;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/21
 * @Version: 1.0
 * @Description:
 */
public class MsgEntity<T> {
    private int type;
    private T data;

    public MsgEntity(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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

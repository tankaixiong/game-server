package tank.handler;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/20
 * @Version: 1.0
 * @Description:
 */
public enum RequestType {
    LOGIN(1);

    private int id;

    RequestType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

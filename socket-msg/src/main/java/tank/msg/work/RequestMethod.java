package tank.msg.net;

import tank.handler.RequestType;

import java.lang.annotation.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description:
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMethod {
    //int id();

    RequestType type();
}

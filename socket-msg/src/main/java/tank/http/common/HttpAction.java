package tank.http.common;

import java.lang.annotation.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/6
 * @Version: 1.0
 * @Description:
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HttpAction {

    String url();

    HttpMethod method() default HttpMethod.NONE;

    String contextType() default ContentType.APPLICATION_JSON;
}

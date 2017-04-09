package tank.http.work;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/9
 * @Version: 1.0
 * @Description:
 */
public interface IHttpDispatcher {

    void scanHttpAction(String... packPath);

    HttpResponse execute(HttpRequest request);
}

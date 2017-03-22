package tank.msg.work;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.common.SocketSession;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description:
 */
public class RequestManager {

    private static Logger LOG = LoggerFactory.getLogger(RequestManager.class);

    private static HashMap<Integer, MethodMeta> requestMethodMap = new HashMap<>();

    public static MethodMeta getMethod(Integer type) {
        return requestMethodMap.get(type);
    }


    public static void load(String packPath) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(packPath))
                        .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(RequestMethod.class);

        for (Method method : methodSet) {
            RequestMethod anno = method.getAnnotation(RequestMethod.class);

            MethodAccess access = MethodAccess.get(method.getDeclaringClass());

            MethodMeta methodMeta = new MethodMeta();
            methodMeta.setAccess(access);

            //methodMeta.setClazz(method.getDeclaringClass());
            try {
                methodMeta.setHandlerObj(method.getDeclaringClass().newInstance());//TODO:如果是spring 则从spring 中获取

            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("{}", e);
                System.exit(0);
            }

            methodMeta.setName(method.getName());
            methodMeta.setType(anno.type().getId());

            requestMethodMap.put(anno.type().getId(), methodMeta);

        }


    }


    public static Object execute(Integer type, SocketSession session, Object param) {
        MethodMeta access = requestMethodMap.get(type);
        if (access != null) {
            //return access.getAccess().invoke(access.getHandlerObj(), access.getName(), new Class[]{Session.class, Object.class}, session, param);
            return access.getAccess().invoke(access.getHandlerObj(), access.getName(), session, param);
        } else {
            LOG.error("没有找到消息类型为:{}的处理", type);
        }
        return null;
    }

    public static void main(String[] args) {
        load("tank.handler");
    }
}


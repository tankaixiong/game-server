package tank.http.work;

import com.esotericsoftware.reflectasm.MethodAccess;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.http.common.ContentType;
import tank.http.common.HttpAction;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/7
 * @Version: 1.0
 * @Description: http请求，分发请求到 httpaction映射的处理方法上
 */
public class UrlDispatcher implements IHttpDispatcher {
    private static Logger LOG = LoggerFactory.getLogger(UrlDispatcher.class);

    private static Map<String, UrlMethodMeta> urlMethodMetaMap = new HashMap<>();

    public void scanHttpAction(String... packPath) {

        List<URL> pathList = new ArrayList<>();
        for (String pack : packPath) {
            pathList.addAll(ClasspathHelper.forPackage(pack));
        }

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(pathList)
                        .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(HttpAction.class);


        for (Method method : methodSet) {

            List<String> methodParamName = findMethodParamName(method);
            for (String mName : methodParamName) {
                System.out.println(mName);
            }

            HttpAction anno = method.getAnnotation(HttpAction.class);

            if (urlMethodMetaMap.containsKey(anno.url())) {
                LOG.error("重复URL:{},{}", anno.url(), method.getDeclaringClass());
            }

            UrlMethodMeta methodMeta = new UrlMethodMeta();
            methodMeta.setMethodName(method.getName());
            methodMeta.setUrl(anno.url());
            methodMeta.setContextType(anno.contextType());

            MethodAccess methodAccess = MethodAccess.get(method.getDeclaringClass());
            methodMeta.setMethodAccess(methodAccess);
            try {
                methodMeta.setHandlerObj(method.getDeclaringClass().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("{}", e);
                System.exit(0);
            }


            urlMethodMetaMap.put(anno.url(), methodMeta);

        }

    }

    public HttpResponse execute(HttpRequest request) {
        HttpResponse response = new HttpResponse();

        UrlMethodMeta access = urlMethodMetaMap.get(request.getUrl());
        if (access != null) {

            Object data = null;

            Class[][] clazz = access.getMethodAccess().getParameterTypes();

            Class[] paramsClazz = clazz[0];

            if (paramsClazz.length == 0) {
                data = access.getMethodAccess().invoke(access.getHandlerObj(), access.getMethodName());
            } else {
                Object[] params = new Object[paramsClazz.length];

                for (int i = 0; i < paramsClazz.length; i++) {
                    Class classes = paramsClazz[i];
                    if (classes == HttpRequest.class) {
                        params[i] = request;
                    }
                }
                data = access.getMethodAccess().invoke(access.getHandlerObj(), access.getMethodName(), paramsClazz, params);
            }
            response.setData(data);
            response.setContentType(access.getContextType());

        } else {
            response.setContentType(ContentType.TEXT_HTML);
            response.setData("没有找到url对应的处理");
            LOG.error("没有找到消息类型为:{}的处理", request.getUrl());
        }
        return response;
    }

//    public static Map<String, Object> parseRequestParams(FullHttpRequest fullHttpRequest) {
//
//        Map<String, Object> paramsMap = new HashMap<>();
//
//        String uri = fullHttpRequest.uri();
//        int index = uri.indexOf("?");
//        if (index > -1) {
//            String params[] = uri.substring(index + 1).split("&");
//            if (params != null && params.length > 0) {
//                for (String param : params) {
//                    if (!StringUtils.isEmpty(param)) {
//                        String[] paramItems = param.split("=");
//
//                        paramsMap.put(paramItems[0], paramItems[1]);
//                    }
//                }
//            }
//            System.out.println(params);
//        }
//        return paramsMap;
//    }

    public static List<String> findMethodParamName(Method method) {

        List<String> paramsList = new ArrayList<>();

        try {

            //HttpDemoHandler demo = new HttpDemoHandler();

            //Method[] methods = demo.getClass().getDeclaredMethods();
            Class clazz = method.getDeclaringClass();
            String methodName = method.getName();

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(clazz));
            CtClass cc = pool.get(clazz.getName());
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr =
                    (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                String[] paramNames = new String[cm.getParameterTypes().length];
                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                for (int i = 0; i < paramNames.length; i++) {
                    paramsList.add(attr.variableName(i + pos));
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return paramsList;
    }


}

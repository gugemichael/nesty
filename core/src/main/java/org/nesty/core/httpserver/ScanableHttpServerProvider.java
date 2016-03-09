package org.nesty.core.httpserver;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.Interceptor;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.commons.exception.ControllerRequestMappingException;
import org.nesty.commons.utils.ClassUtil;
import org.nesty.commons.utils.PackageScanner;
import org.nesty.core.httpserver.rest.HttpContextInterceptor;
import org.nesty.core.httpserver.rest.URLHandler;
import org.nesty.core.httpserver.rest.URLResource;
import org.nesty.core.httpserver.rest.route.RouteControlloer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * HttpServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public abstract class ScanableHttpServerProvider extends HttpServerProvider {

    private RouteControlloer routeControlloer;

    private List<HttpContextInterceptor> interceptor;

    /**
     * scan specified package's all classes
     */
    private PackageScanner scanner = new PackageScanner();

    @Override
    public HttpServer scanHttpController(String packageName) throws ControllerRequestMappingException {
        RouteControlloer.ConcurrentReadRouteMap relation = new RouteControlloer.ConcurrentReadRouteMap();
        List<HttpContextInterceptor> inters = new LinkedList<>();

        // find all Class
        List<Class<?>> classList = null;
        try {
            classList = scanner.scan(packageName);
        } catch (ClassNotFoundException e) {
            throw new ControllerRequestMappingException(String.format("scan package %s failed. %s", packageName, e.getMessage()));
        }

        RequestMapping clazzLevelRequestMapping = null;
        for (Class<?> clazz : classList) {
            // @Interceptor
            if (clazz.getAnnotation(Interceptor.class) != null) {
                checkConstructor(clazz);
                // must implements HttpContextInterceptor
                if (clazz.getSuperclass() != HttpContextInterceptor.class)
                   throw new ControllerRequestMappingException(String.format("%s must implements %s", clazz.getName(), HttpContextInterceptor.class.getName()));

                try {
                    inters.add((HttpContextInterceptor) clazz.newInstance());
                    System.err.println(clazz.getName());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ControllerRequestMappingException(String.format("%s newInstance() failed %s", clazz.getName(), e.getMessage()));
                }
            }

            // with @Controller
            if (clazz.getAnnotation(Controller.class) != null) {
                checkConstructor(clazz);
                // class level prefix RequestMapping.URL
                clazzLevelRequestMapping = clazz.getAnnotation(RequestMapping.class);

                // find all annotationed method in class
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (Modifier.isStatic(method.getModifiers()) || method.getAnnotations().length == 0)
                        continue;

                    // read @RequestMapping
                    org.nesty.commons.annotations.RequestMapping requestMapping = method.getAnnotation(org.nesty.commons.annotations.RequestMapping.class);
                    if (requestMapping == null)
                        continue;

                    String uri = requestMapping.value();
                    if (clazzLevelRequestMapping != null)
                        uri = clazzLevelRequestMapping.value() + uri;

                    if (!uri.startsWith("/"))
                        throw new ControllerRequestMappingException(String.format("%s.%s annotation must start with / ", clazz.getName(), method.getName()));

                    // default is RequestMethod.GET if method annotation is not set
                    RequestMethod requestMethod = requestMapping.method();

                    URLResource urlResource = URLResource.fromHttp(uri, requestMethod);
                    URLHandler urlHandler = URLHandler.fromProvider(uri, clazz, method);

                    // register
                    if (!relation.put(urlResource, urlHandler))
                        throw new ControllerRequestMappingException(String.format("%s.%s annotation is duplicated", clazz.getName(), method.getName()));

                    System.err.println(urlHandler.toString());
                }
            }
        }

        routeControlloer = new RouteControlloer(relation);
        interceptor = inters;

        return this;
    }

    private boolean checkConstructor(Class<?> clazz) throws ControllerRequestMappingException {
        if (!ClassUtil.hasDefaultConstructor(clazz))
            throw new ControllerRequestMappingException(String.format("%s dosn't have default constructor", clazz.getName()));
        return true;
    }

    public RouteControlloer getRouteController() {
        return routeControlloer;
    }

    public List<HttpContextInterceptor> getInterceptor() {
        return interceptor;
    }
}

package org.nesty.core.httpserver;

import org.nesty.commons.PackageScanner;
import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.commons.exception.ControllerRequestMappingException;
import org.nesty.core.httpserver.rest.handler.URLHandler;
import org.nesty.core.httpserver.rest.route.RouteControlloer;
import org.nesty.core.httpserver.rest.route.URLResource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * nesty
 *
 * HttpServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public abstract class ScanableHttpServerProvider extends HttpServerProvider {

    private RouteControlloer routeControlloer;

    /**
     * scan specified package's all classes
     */
    private PackageScanner scanner = new PackageScanner();

    @Override
    public HttpServer scanHttpController(String packageName) throws ControllerRequestMappingException {
        RouteControlloer.ConcurrentReadRouteMap relation = new RouteControlloer.ConcurrentReadRouteMap();

        // find all Class
        List<Class<?>> classList = null;
        try {
            classList = scanner.scan(packageName);
        } catch (ClassNotFoundException e) {
            throw new ControllerRequestMappingException(String.format("scan package %s failed. %s", packageName, e.getMessage()));
        }

        RequestMapping clazzLevelRequestMapping = null;
        for (Class<?> clazz : classList) {
            // only analyze annotated with @Controller
            if (clazz.getAnnotation(Controller.class) == null)
                continue;

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

                System.out.println(urlHandler.toString());
            }
        }

        routeControlloer = new RouteControlloer(relation);

        return this;
    }

    public RouteControlloer getRouteController() {
        return routeControlloer;
    }

}

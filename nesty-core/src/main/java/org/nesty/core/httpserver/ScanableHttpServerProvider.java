package org.nesty.core.httpserver;

import org.nesty.commons.PackageScanner;
import org.nesty.commons.annotations.Controller;
import org.nesty.commons.constant.http.HttpMethod;
import org.nesty.commons.exception.NestyControllerScanException;
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
    public HttpServer scanHttpController(String packageName) throws NestyControllerScanException {
        RouteControlloer.ConcurrentReadRouteMap relation = new RouteControlloer.ConcurrentReadRouteMap();

        // find all Class
        List<Class<?>> classList = null;
        try {
            classList = scanner.scan(packageName);
        } catch (ClassNotFoundException e) {
            throw new NestyControllerScanException(String.format("scan package %s failed. %s", packageName, e.getMessage()));
        }

        for (Class<?> clazz : classList) {
            // only analyze annotated with @Controller
            if (clazz.getAnnotation(Controller.class) == null)
                continue;

            // find all annotationed method in class
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers()) || method.getAnnotations().length == 0)
                    continue;

                // read @RequestMapping
                org.nesty.commons.annotations.RequestMapping requestMapping = method.getAnnotation(org.nesty.commons.annotations.RequestMapping.class);
                if (requestMapping == null || !requestMapping.value().contains("/"))
                    continue;

                String url = requestMapping.value();

                // default is HttpMethod.GET if method annotation is not set
                HttpMethod httpMethod = requestMapping.method();

                URLResource urlResource = URLResource.fromHttp(url, httpMethod);
                URLHandler urlHandler = URLHandler.fromProvider(clazz, method);

                // register
                relation.put(urlResource, urlHandler);

                System.err.println(urlHandler.toString());
            }
        }

        routeControlloer = new RouteControlloer(relation);

        return this;
    }

    public RouteControlloer getRouteController() {
        return routeControlloer;
    }

}

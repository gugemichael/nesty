package org.nesty.core.httpserver;

import org.nesty.commons.PackageScanner;
import org.nesty.commons.constant.http.HttpMethod;
import org.nesty.commons.exception.NestyControllerScanException;
import org.nesty.core.httpserver.rest.handler.URLHandler;
import org.nesty.core.httpserver.rest.route.URLResource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * nesty
 *
 * HttpServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public abstract class ScanableHttpServerProvider extends HttpServerProvider {

    private Map<URLResource, URLHandler> controller = new HashMap<>(256);

    /**
     * scan specified package's all classes
     */
    private PackageScanner scanner = new PackageScanner();

    @Override
    public HttpServer scanHttpController(String packageName) throws NestyControllerScanException {

        // find all Class
        List<Class<?>> classList = null;
        try {
            classList = scanner.scan(packageName);
        } catch (ClassNotFoundException e) {
            throw new NestyControllerScanException(String.format("scan package %s failed. %s", packageName, e.getMessage()));
        }

        for (Class<?> clazz : classList) {
            // find all annotationed method in class
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers()) || method.getAnnotations().length == 0)
                    continue;

                // read @Method
                org.nesty.commons.annotations.Method annoMethod = method.getAnnotation(org.nesty.commons.annotations.Method.class);
                if (annoMethod == null)
                    continue;
                HttpMethod httpMethod = annoMethod.value();

                // read @Path
                org.nesty.commons.annotations.Path annoPath = method.getAnnotation(org.nesty.commons.annotations.Path.class);
                if (annoPath == null || !annoPath.value().contains("/"))
                    continue;
                String url = annoPath.value();

                URLResource urlResource = URLResource.fromHttp(url, httpMethod);
                URLHandler urlHandler = URLHandler.fromProvider(clazz, method);

                // register
                controller.put(urlResource, urlHandler);

                System.err.println(urlHandler.toString());
            }
        }

        return this;
    }

    public Map<URLResource, URLHandler> getControllers() {
        return controller;
    }


//    public static void main(String[] args) {
//        List<Class<?>> classes = new PackageScanner().scan(Joiner.class.getPackage().getName());
//        for (Class<?> c : classes)
//            System.out.println(c.getName());
//    }
}

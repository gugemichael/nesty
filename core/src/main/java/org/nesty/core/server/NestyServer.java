package org.nesty.core.server;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.commons.exception.ControllerRequestMappingException;
import org.nesty.commons.utils.ClassUtil;
import org.nesty.commons.utils.PackageScanner;
import org.nesty.core.server.rest.ControllerRouter;
import org.nesty.core.server.rest.URLResource;
import org.nesty.core.server.rest.controller.DefaultController;
import org.nesty.core.server.rest.controller.URLController;
import org.nesty.core.server.rest.interceptor.Interceptor;
import org.nesty.core.server.rest.interceptor.InterceptorHelpers;
import org.nesty.core.server.springplus.ApplicationContextProvider;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * scan package route service provider
 * <p>
 * Author Michael on 03/03/2016.
 */
public abstract class NestyServer extends NestyOptionProvider implements Server {

    // controller router map collection
    private final ControllerRouter routerTable = new ControllerRouter();
    // interceptors collection
    private final List<Interceptor> interceptors = new LinkedList<>();
    // only once initail scan
    private final AtomicBoolean scanOver = new AtomicBoolean(false);
    // root controller
    private static boolean hasRootController = true;

    // scan specified package's all classes
    private PackageScanner scanner = new PackageScanner();

    public static void disableInternalController() {
        hasRootController = false;
    }

    /**
     * we install internal module here, just one time !
     */
    void firstScan() {
        if (scanOver.compareAndSet(false, true)) {
            if (hasRootController) {
                // default Controller (URI path is "/")
                Method root = DefaultController.class.getMethods()[0];
                routerTable.register(URLResource.fromHttp("/", RequestMethod.GET), URLController.fromProvider("/", DefaultController.class, root).internal());
            }

            // internal Interceptor
            for (InterceptorHelpers interceptor : InterceptorHelpers.values()) {
                interceptor.instance.install(this);
                interceptors.add(interceptor.instance);
            }
        }
    }

    // scan package controller class. NOT Threads-Safe !!
    //
    public NestyServer scanHttpController(String packageName) throws ControllerRequestMappingException {
        firstScan();

        // scan all classes and install them
        List<Class<?>> classList = null;
        try {
            classList = scanner.scan(packageName);
        } catch (ClassNotFoundException e) {
            throw new ControllerRequestMappingException(String.format("scan package %s failed. %s", packageName, e.getMessage()));
        }

        RequestMapping clazzLevelRequestMapping = null;

        for (Class<?> clazz : classList) {
            // @Interceptor
            if (clazz.getAnnotation(org.nesty.commons.annotations.Interceptor.class) != null) {
                checkConstructor(clazz);
                // must implements Interceptor
                if (clazz.getSuperclass() != Interceptor.class)
                    throw new ControllerRequestMappingException(String.format("%s must implements %s", clazz.getName(), Interceptor.class.getName()));

                try {

                    /**
                     * we register interceptors with class-name natural sequence. may
                     * be known as unordered. But make ensurance that call with registered
                     * sequence.
                     *
                     * TODO : may be we can indecate the previous {@link org.nesty.commons.annotations.Interceptor} manully
                     *
                     */

                    Interceptor wanted;
                    if (clazz.getAnnotation(Component.class) != null) {
                        wanted = (Interceptor) ApplicationContextProvider.get(clazz);
                    } else {
                        wanted = (Interceptor)clazz.newInstance();
                    }

                    interceptors.add(wanted);
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
                    if (!uri.startsWith("/"))
                        throw new ControllerRequestMappingException(String.format("%s.%s annotation must start with / ", clazz.getName(), method.getName()));

                    if (clazzLevelRequestMapping != null)
                        uri = clazzLevelRequestMapping.value() + uri;


                    // default is RequestMethod.GET if method annotation is not set
                    RequestMethod requestMethod = requestMapping.method();

                    URLResource urlResource = URLResource.fromHttp(uri, requestMethod);
                    URLController urlController = URLController.fromProvider(uri, clazz, method);

                    /**
                     * register the controller to controller map {@link ControllerRouter}.register() will return
                     * false on dupliacted URLReousource key. Duplicated URLResource means they have same
                     * url, url variabls and http method. we will confuse on them and couldn't decide which
                     * controller method to invoke.
                     *
                     * TODO : we throw exception here. let users to know and decide what to do
                     *
                     */
                    if (!routerTable.register(urlResource, urlController))
                        throw new ControllerRequestMappingException(String.format("%s.%s annotation is duplicated", clazz.getName(), method.getName()));

                    // add monitor
                    NestyServerMonitor.resourceMap.put(urlResource, urlController);
                }
            }
        }

        return this;
    }

    private boolean checkConstructor(Class<?> clazz) throws ControllerRequestMappingException {
        if (!ClassUtil.hasDefaultConstructor(clazz))
            throw new ControllerRequestMappingException(String.format("%s dosn't have default constructor", clazz.getName()));
        return true;
    }

    public ControllerRouter getRouteController() {
        return routerTable;
    }

    public List<Interceptor> getInterceptor() {
        return interceptors;
    }

    @Override
    public void shutdown() {
        if (!this.interceptors.isEmpty()) {
            for (Interceptor interceptor : this.interceptors)
                interceptor.destroy();
        }
    }
}
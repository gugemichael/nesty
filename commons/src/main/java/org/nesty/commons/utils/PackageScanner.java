package org.nesty.commons.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class PackageScanner {

    public List<Class<?>> scan(String packageName) throws ClassNotFoundException {

        List<Class<?>> classes = new LinkedList<>();

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = loader.getResources(packageName.replace('.', '/'));
            while (urls.hasMoreElements()) {
                URI uri = urls.nextElement().toURI();
                switch (uri.getScheme().toLowerCase()) {
                case "jar":
                    scanFromJarProtocol(loader, classes, uri.getRawSchemeSpecificPart());
                    break;
                case "file":
                    scanFromFileProtocol(loader, classes, uri.getPath(), packageName);
                    break;
                default:
                    throw new URISyntaxException(uri.getScheme(), "unknown schema " + uri.getScheme());
                }

            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return classes;
    }


    private Class<?> loadClass(ClassLoader loader, String classPath) throws ClassNotFoundException {
        classPath = classPath.substring(0, classPath.length() - 6);
        return loader.loadClass(classPath);
    }

    private void scanFromFileProtocol(ClassLoader loader, List<Class<?>> classes, String dir, String packageName) throws ClassNotFoundException {
        File directory = new File(dir);
        File[] files = directory.listFiles();
        if (directory.isDirectory() && files != null) {
            for (File classFile : files) {
                if (!classFile.isDirectory() && classFile.getName().endsWith(".class") && !classFile.getName().contains("$")) {
                    String className = String.format("%s.%s", packageName, classFile.getName());
                    classes.add(loadClass(loader, className));
                }
            }
        }
    }

    private void scanFromJarProtocol(ClassLoader loader, List<Class<?>> classes, String fullPath) throws ClassNotFoundException {
        final String jar = fullPath.substring(0, fullPath.lastIndexOf('!'));
        final String parent = fullPath.substring(fullPath.lastIndexOf('!') + 2);
        JarEntry e = null;

        JarInputStream jarReader = null;
        try {
            jarReader = new JarInputStream(new URL(jar).openStream());
            while ((e = jarReader.getNextJarEntry()) != null) {
                String className = e.getName();
                if (!e.isDirectory() && className.startsWith(parent) && className.endsWith(".class") && !className.contains("$")) {
                    className = className.replace('/', '.');
                    classes.add(loadClass(loader, className));
                }
                jarReader.closeEntry();
            }
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            try {
                if (jarReader != null)
                    jarReader.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }
}

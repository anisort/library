package org.example.utils.helpers;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourcesUtils {
    public static String getResourceFilePath(String resourcePath) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(resourcePath);
        if (url == null) {
            throw new IllegalStateException("Resource not found on classpath: " + resourcePath);
        }
        try {
            Path path = Paths.get(url.toURI());
            return path.toFile().getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Cannot resolve resource to file path: " + resourcePath, e);
        }
    }
}

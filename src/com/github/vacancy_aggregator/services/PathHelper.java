package com.github.vacancy_aggregator.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PathHelper {
    public static String getRelativeClassPathString(Class clazz) {
        return clazz.getPackage().getName().replace('.', '/') + "/";
    }

    public static String getAbsoluteClassPathString(Class clazz) {
        String relativePathStr = getRelativeClassPathString(clazz);
        String absPathStr = Paths.get(relativePathStr).toAbsolutePath().toString();
        return absPathStr.substring(0, absPathStr.length() - relativePathStr.length() + 1);
    }

    public static String getConfigSysAbsolutePathString() {
        return getAbsoluteClassPathString((new PathHelper()).getClass()) + "config_sys" + System.getProperty("file.separator");
    }

    public static String getConfigUserAbsolutePathString() {
        return getAbsoluteClassPathString((new PathHelper()).getClass()) + "config_user" + System.getProperty("file.separator");
    }

    public static String getHtmlResultAbsolutePathString() {
        final String dirName = "html_result";
        String result = getAbsoluteClassPathString((new PathHelper()).getClass()) + dirName + System.getProperty("file.separator");
        Path absPath = Paths.get(result);
        if (!Files.exists(absPath)) {
            try {
                Files.createDirectory(absPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String generateNextHtmlResultAbsolutePathString(String jobString) {
        Date now = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd_HH-mm.ss");
        return String.format("%s%s_%s.html", getHtmlResultAbsolutePathString(), fmt.format(now), jobString.toLowerCase());
    }

}

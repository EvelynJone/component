package com.evelyn.base.dlock.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午13:05]
 */
public class FlattenYamlUtil {

    public static String getProperty(String propertyPath) {
        Yaml yaml = new Yaml();

        Object object;

        try (InputStream inputStream = FlattenYamlUtil.class.getClassLoader().getResourceAsStream("application.yml")){

            object = yaml.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getString(object,propertyPath);
    }

    private static String getString(Object object, String propertyPath) {
        Object value = getValue(object, propertyPath);
        return value == null ? null : value.toString();
    }

    private static Object getValue(Object object, String propertyPath) {
        if (!validPath(propertyPath)) {
            throw new IllegalArgumentException("读取yaml配置项传入了不合法的key name ：" + propertyPath);
        }

        if (!(object instanceof Map)) {
            return null;
        }

        Map tempMap = (Map) object;

        String[] pathNodes = propertyPath.split("\\.");
        for (int i = 0; i < pathNodes.length - 1; i++) {
            Object tempObj = tempMap.get(pathNodes[i]);
            if (!(tempObj instanceof Map)) {
                return null;
            }

            tempMap = (Map) tempObj;
        }

        return tempMap.get(pathNodes[pathNodes.length - 1]);
    }

    /**
     * 用于校验传入的property path是合法的。例如：redis-cluster.server.url
     * @param propertyPath
     * @return
     */
    private static boolean validPath(String propertyPath) {
        if (propertyPath == null || propertyPath.length() == 0 || "".equals(propertyPath.trim())) {
            return false;
        }

        // pathNodes不能为null
        String[] pathNodes = propertyPath.split("\\.");
        for (int i = 0; i < pathNodes.length; i++) {
            String pathNode = pathNodes[i];

            if (pathNode == null || propertyPath.length() ==0 || "".equals(propertyPath.trim())) {
                return false;
            }
        }

        return true;
    }
}

/*
 * Copyright 2002-2016 XianYu Game Co. Ltd, The Inuyasha Project
 */

package tank.msg.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;

/**
 * JSON操作工具类
 */
public class JsonUtil {
    public static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);

        objectMapper.setSerializationInclusion(Include.NON_NULL); // null 不参与转换

        // 设置当遇到JSON中有而对象中没有的字段时不会失败
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("将对象序列化成JSON字符串时发生解析异常", e);
        }
    }

    /**
     * 将指定的JSON格式的字符串解析成指定类型的对象
     *
     * @param <T>  返回的对象类型
     * @param json 要解析成对象的JSON格式字符串
     * @param type 指定要解析成对象的类型
     * @return 返回解析对象
     */
    public static <T> T toBean(String json, Class<T> type) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("将对象序列化成JSON字符串时发生解析异常", e);
        }
    }

    public static <T> T toBean(String json, Class<T> type, T defaultValue) {
        T bean = toBean(json, type);
        return bean == null ? defaultValue : bean;
    }

    /**
     * 将指定的JSON格式的字符串解析成指定类型的对象
     *
     * @param <T>          返回的对象类型
     * @param json         要解析成对象的JSON格式字符串
     * @param valueTypeRef 指定要解析成对象的类型
     * @return 返回解析对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, TypeReference<T> valueTypeRef) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (T) objectMapper.readValue(json, valueTypeRef);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("将对象序列化成JSON字符串时发生解析异常", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, TypeReference<T> valueTypeRef, T defaultValue) {
        T bean = toBean(json, valueTypeRef);
        return bean == null ? defaultValue : bean;
    }

    /**
     * 将指定的JSON格式的字符串解析成指定类型的对象
     *
     * @param <T>  返回的对象类型
     * @param json 要解析成对象的JSON格式字符串
     * @param type 指定要解析成对象的类型
     * @return 返回解析对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, JavaType type) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (T) objectMapper.readValue(json, type);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("将对象序列化成JSON字符串时发生解析异常", e);
        }
    }

    public static <T> T toBean(String json, JavaType type, T defaultValue) {
        T value = toBean(json, type);
        return value == null ? defaultValue : value;
    }
}

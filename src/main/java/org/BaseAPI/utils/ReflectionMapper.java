package org.BaseAPI.utils;

import org.springframework.beans.BeanUtils;

public class ReflectionMapper {
    public static <S, T> T map(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Mapping failed", e);
        }
    }
}

package org.soujava.metadata.compiler;

import org.soujava.medatadata.api.Mapper;

import java.util.HashMap;
import java.util.Map;

public class CompileMapper implements Mapper {

    private Map<Class<?>, Object> mapper = new HashMap<>();


    @Override
    public <T> T toEntity(Map<String, Object> map, Class<T> type) {
        return null;
    }

    @Override
    public <T> Map<String, Object> toMap(T entity) {
        return null;
    }
}

package org.soujava.metadata.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

interface EntitySupplier<T> {

    T toEntity(Map<String, Object> map, Class<T> type) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    class FieldEntitySupplier<T> implements EntitySupplier<T> {

        private final Constructor<T> constructor;

        FieldEntitySupplier(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        @Override
        public T toEntity(Map<String, Object> map, Class<T> type) throws InvocationTargetException,
                InstantiationException, IllegalAccessException {
            final T instance = constructor.newInstance();
            for (Field field : type.getDeclaredFields()) {
                FieldWriter writer = FieldWriter.of(field);
                writer.write(map, instance);
            }
            return instance;
        }
    }

}

package org.soujava.medatadata.api;

import java.util.Map;

public interface EntitySupplier {

    <T> T toEntity(Map<String, Object> map, Class<T> type);


}

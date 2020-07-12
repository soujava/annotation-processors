package org.soujava.metadata.example;

import org.soujava.medatadata.api.Mapper;
import org.soujava.metadata.processor.ProcessorMap;

public class ReflectionMapTest extends AbstractMapperTest {

    @Override
    protected Mapper getMapper() {
        return new ProcessorMap();
    }


}

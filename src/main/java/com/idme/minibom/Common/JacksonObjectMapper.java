package com.idme.minibom.Common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class JacksonObjectMapper extends ObjectMapper {

    public JacksonObjectMapper(){
        super();
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance);

        this.registerModule(simpleModule);
    }
}

//https://medium.com/@piszu/feign-client-is-not-so-simple-as-we-want-case-study-920e4901e27b
//package com.bank.currencies.mappers;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//public class customObjectMapper public ObjectMapper customObjectMapper(){
//    SimpleModule simpleModule = new SimpleModule();
//    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
//            .json()
//            .modules(new JavaTimeModule(), simpleModule)
//            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            .build();
//    return objectMapper
//            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
//}
//
//
//return Feign.builder()
//        .decoder(new ResponseEntityDecoder(new JacksonDecoder(customObjectMapper.customObjectMapper())))
//        .encoder(new JacksonEncoder(customObjectMapper.customObjectMapper()))
//        .target(FeignRestClient.class, URL_ADDRESS);{
//}

package com.schwarzsword.pip.coursework.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.schwarzsword.pip.coursework.service.JsonService;
import org.springframework.stereotype.Service;

@Service("jsonService")
public class JsonServiceImpl implements JsonService {
    @Override
    public String toJson(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

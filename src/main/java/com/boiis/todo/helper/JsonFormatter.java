package com.boiis.todo.helper;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Component
public class JsonFormatter {

    public String createMapToJson(Map<String, String> data)
    {
        StringBuilder json = new StringBuilder("{ ");
        Iterator<Map.Entry<String,String> > itr = data.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<String,String> entry = itr.next();
            json.append(String.format("\"%s\" : \"%s\"", entry.getKey(),entry.getValue()));
            json = itr.hasNext() ? json.append(" , ") : json.append("}");
        }
        return json.toString();
    }
}

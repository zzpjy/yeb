package com.xxxx.server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义Authority
 */
public class CustomAuthorityDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {


           ObjectMapper mapper = (ObjectMapper) p.getCodec();
//           mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
           JsonNode jsonNode = mapper.readTree(p);
           List<GrantedAuthority> grantedAuthorities=new LinkedList<>();
           Iterator<JsonNode> elements = jsonNode.elements();
           while(elements.hasNext()){
               JsonNode next=elements.next();
               JsonNode authority = next.get("authority");
               grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
           }
           return grantedAuthorities;
    }
}

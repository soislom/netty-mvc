package cn.netty.core.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JacksonUtil {

	public static String toJson(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		return objectMapper.writeValueAsString(obj);
	}

}

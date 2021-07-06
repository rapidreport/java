package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadUtil {

	private ReadUtil(){};

	public static Map<?, ?> readJson(String path) throws JsonParseException, JsonMappingException, IOException{
		InputStream is = new FileInputStream(path);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			return mapper.readValue(is, new TypeReference<LinkedHashMap<String,Object>>(){});
		}finally {
			is.close();
		}
	}
}
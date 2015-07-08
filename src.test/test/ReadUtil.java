package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class ReadUtil {
	
	private ReadUtil(){};
	
    public static Map<?, ?> readJson(String path) throws JSONException, IOException{
    	InputStream is = new FileInputStream(path);
    	try{
    		Reader r = new InputStreamReader(is, "UTF-8");
        try{
        	return (Map<?, ?>)JSON.decode(r);
        }finally{
        	r.close();
        }
      }finally{
    	  is.close();
      }
    }
    
}

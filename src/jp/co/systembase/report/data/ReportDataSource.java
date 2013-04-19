package jp.co.systembase.report.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.DataTable;

public class ReportDataSource implements IReportDataSource {

	public List<?> data = null;

	private Map<Class<?>, Map<String, Method>> methodCache = new HashMap<Class<?>, Map<String, Method>>();
	private Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<Class<?>, Map<String, Field>>();

	public ReportDataSource(ResultSet rs) throws SQLException{
		this.data = new DataTable(rs);
	}

	public ReportDataSource(List<?> data){
		this.data = data;
	}

	public Object get(int i, String key) {
		Object r = this.data.get(i);
		if (r instanceof Map){
			return ((Map<?, ?>)r).get(key);
		}else{
			Class<?> c = r.getClass();
			this.createCache(c);
			{
				Map<String, Method> mc = this.methodCache.get(c);
				if (mc.containsKey(key)){
					try {
						return mc.get(key).invoke(r, (Object[])null);
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					} catch (InvocationTargetException e) {
					}
				}
			}
			{
				Map<String, Field> fc = this.fieldCache.get(c);
				if (fc.containsKey(key)){
					try {
						return fc.get(key).get(r);
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
				}
			}
			return null;
		}
	}

	protected synchronized void createCache(Class<?> c){
		if (this.methodCache.containsKey(c)){
			return;
		}
		Map<String, Method> mc = new HashMap<String, Method>();
		Map<String, Field> fc = new HashMap<String, Field>();
		Method methods[] = c.getMethods();
		Field fields[] = c.getFields();
		for(Method m: methods){
			if (m.getName().length() > 2 && m.getName().startsWith("is") &&
				m.getReturnType() == boolean.class){
				String k = m.getName().substring(2);
				String _k = k.substring(0, 1).toLowerCase() + k.substring(1);
				if (!mc.containsKey(k)){
					mc.put(k, m);
				}
				if (!mc.containsKey(_k)){
					mc.put(_k, m);
				}
			}
		}
		for(Method m: methods){
			if (m.getName().length() > 3 && m.getName().startsWith("get")){
				String k = m.getName().substring(3);
				String _k = k.substring(0, 1).toLowerCase() + k.substring(1);
				if (!mc.containsKey(k)){
					mc.put(k, m);
				}
				if (!mc.containsKey(_k)){
					mc.put(_k, m);
				}
			}
		}
		for(Field f: fields){
			fc.put(f.getName(), f);
		}
		this.methodCache.put(c, mc);
		this.fieldCache.put(c, fc);
	}

	public int size() {
		return this.data.size();
	}

}

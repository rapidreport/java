package jp.co.systembase.report.component;

import java.util.Map;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.data.INoCache;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.ReportDataRecord;

public class CustomField {

	public static class Stack{

		public static class Node{
			public Node caller;
			public CustomField field;
			public Node(Node caller, CustomField field){
				this.caller = caller;
				this.field = field;
			}
		}

		public Node top = null;

		private void detectCirclarReference(CustomField field) throws EvalException{
			Node n = this.top;
			while(n != null){
				if (n.field.key.equals(field.key) && n.field.data == field.data){
					throw new EvalException("循環参照が含まれています : " + n.field.key);
				}
				n = n.caller;
			}
		}

		public void push(CustomField field) throws EvalException{
			this.detectCirclarReference(field);
			this.top = new Node(this.top, field);
		}

		public void pop(){
			this.top = this.top.caller;
		}

	}
	
	public String key;
	public String exp;
	public Report report;
	public ReportData data;

	public CustomField(
			String key,
			String exp,
			Report report,
			ReportData data){
		this.key = key;
		this.exp = exp;
		this.report = report;
		this.data = data;
	}

	public Object get(int i){
		try{
			if (!(this.data.dataSource instanceof INoCache)){
				Map<Integer, Object> cache =
					this.report.dataCache.customField(this.data, key);
				if (cache.containsKey(i)){
					return cache.get(i);
				}else{
					Object ret = this.eval(i);
					cache.put(i, ret);
					return ret;
				}
			}else{
				return this.eval(i);
			}
		}catch(EvalException ex){
			return null;
		}
	}

	private Object eval(int i) throws EvalException{
		ReportDataRecord r = new ReportDataRecord(this.data, i);
		return (new Evaluator(this.report, this.data, r)).eval(this.exp);
	}


}

package jp.co.systembase.report.data.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;


public class SortedDataSource implements IReportDataSource {

	private ReportData reportData;
	private List<String> keys;
	private List<Integer> order;

	public SortedDataSource(
			ReportData reportData,
			List<String> keys){
		this.reportData = reportData;
		this.keys = keys;
		this.initialize();
	}

	private void initialize(){
		this.order = new ArrayList<Integer>();
		for(int i = 0;i < this.reportData.size();i++){
			this.order.add(i);
		}
		java.util.Collections.sort(this.order, new _Comparator(this.reportData, this.keys));
	}

	private static class _Comparator implements Comparator<Integer> {
		public ReportData reportData;
		public List<String> keys;
		public _Comparator(ReportData reportData, List<String> keys){
			this.reportData = reportData;
			this.keys = keys;
		}
		public int compare(Integer i1, Integer i2) {
			for(String k: this.keys){
				Object v1 = ReportUtil.regularize(this.reportData.get(i1, k));
				Object v2 = ReportUtil.regularize(this.reportData.get(i2, k));
				if (v1 == null && v2 == null){
					continue;
				}
				if (v1 == null){
					return -1;
				}
				if (v2 == null){
					return 1;
				}
				if (v1.equals(v2)){
					continue;
				}
				if (v1 instanceof Boolean){
					if ((Boolean)v1){
						return 1;
					}
					if ((Boolean)v2){
						return -1;
					}
				}else if (v1 instanceof BigDecimal){
					return ((BigDecimal)v1).compareTo((BigDecimal)v2);
				}else if (v1 instanceof String){
					return ((String)v1).compareTo((String)v2);
				}
			}
			return 0;
		}
	}

	public int size() {
		return this.order.size();
	}

	public Object get(int i, String key) {
		return this.reportData.get(this.order.get(i), key);
	}

}

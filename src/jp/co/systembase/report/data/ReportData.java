package jp.co.systembase.report.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.IReportLogger;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ContentDesign;
import jp.co.systembase.report.component.CustomField;
import jp.co.systembase.report.component.DataCache;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.component.GroupDesign;
import jp.co.systembase.report.component.IndexRange;
import jp.co.systembase.report.component.UnknownFieldException;
import jp.co.systembase.report.data.internal.WrapperDataSource;

public class ReportData implements IReportDataSource {

	public IReportDataSource dataSource = null;
	public Report report = null;
	public Group group = null;
	public int beginIndex;
	public int endIndex;
	public DataCache dataCache;
	public IReportLogger logger;

	public ReportData(IReportDataSource dataSource, DataCache dataCache, IReportLogger logger){
		this.initialize(dataSource, 0, dataSource.size(), null, null, dataCache, logger);
	}

    public ReportData(IReportDataSource dataSource, int beginIndex, int endIndex, DataCache dataCache, IReportLogger logger){
    	this.initialize(dataSource, beginIndex, endIndex, null, null, dataCache, logger);
    }

	public ReportData(IReportDataSource dataSource, Group group){
		this.initialize(
				dataSource,
				0,
				dataSource.size(),
				group.getReport(),
				group,
				group.getReport().dataCache,
				group.getReport().design.setting.logger);
	}

	public ReportData(IReportDataSource dataSource, Report report, Group group){
		this.initialize(
				dataSource,
				0,
				dataSource.size(),
				report,
				group,
				report.dataCache,
				report.design.setting.logger);
	}

	public ReportData(IReportDataSource dataSource, int beginIndex, int endIndex, Report report, Group group){
		this.initialize(
				dataSource,
				beginIndex,
				endIndex,
				report,
				group,
				report.dataCache,
				report.design.setting.logger);
	}

	public ReportData(ReportData data){
		this.initialize(
				data.dataSource,
				data.beginIndex,
				data.endIndex,
				data.report,
				data.group,
				data.dataCache,
				data.logger);
	}

	public ReportData(ReportData data, int fromIndex, int toIndex){
		this.initialize(
				data.dataSource,
				fromIndex,
				toIndex,
				data.report,
				data.group,
				data.dataCache,
				data.logger);
	}

	public static ReportData getPartialData(ReportData data, int beginIndex, int endIndex){
		return new ReportData(data, data.beginIndex + beginIndex, data.beginIndex + endIndex);
	}

	public static ReportData getEmptyData(ReportData data){
		return getEmptyData(
				data.dataSource,
				data.report,
				data.group);
	}

	public static ReportData getEmptyData(
			IReportDataSource dataSource,
			Report report,
			Group group){
		return new ReportData(dataSource, -1, -1, report, group);
	}

	private void initialize(
			IReportDataSource dataSource,
			int beginIndex,
			int endIndex,
			Report report,
			Group group,
			DataCache dataCache,
			IReportLogger logger){
		this.dataSource = dataSource;
		this.report = report;
		this.group = group;
		this.dataCache = dataCache;
		this.logger = logger;
		if (beginIndex >= 0 && beginIndex < endIndex){
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
		}else{
			this.beginIndex = -1;
			this.endIndex = -1;
		}
	}

	public void setGroup(Group group){
		this.group = group;
		GroupDesign groupDesign = group.getDesign();
		if (!this.report.wrapperDataSourceMap.containsKey(groupDesign)){
			this.report.wrapperDataSourceMap.put(groupDesign, new WrapperDataSource());
		}
		WrapperDataSource wrapperDataSource = this.report.wrapperDataSourceMap.get(groupDesign);
		int index = wrapperDataSource.dataList.size();
		this.group.traversalIndex = index;
		{
			Group g = this.group;
			while(true){
				if (g.parentGroups.parentContent == null){
					break;
				}
				g = g.parentGroups.parentContent.parentGroup;
				if (!g.dataIndexRangeMap.containsKey(groupDesign)){
					g.dataIndexRangeMap.put(groupDesign, new IndexRange());
				}
				IndexRange indexRange = g.dataIndexRangeMap.get(groupDesign);
				if (indexRange.beginIndex == -1){
					indexRange.beginIndex = index;
				}
				indexRange.endIndex = index + 1;
			}
		}
		wrapperDataSource.dataList.add(this);
	}

	public boolean hasSameSource(ReportData data){
		return (this.dataSource == data.dataSource);
	}

	public boolean isEmpty(){
		return !(this.beginIndex < this.endIndex);
	}

	public ReportData merge(ReportData data){
		if (!this.hasSameSource(data)){
			return this;
		}
		int bi = this.beginIndex;
		int ei = this.endIndex;
		if (data.beginIndex != -1){
			if (bi == -1 || bi > data.beginIndex){
				bi = data.beginIndex;
			}
		}
		if (data.endIndex != -1){
			if (ei == -1 || ei < data.endIndex){
				ei = data.endIndex;
			}
		}
		return new ReportData(this.dataSource, bi, ei, this.report, this.group);
	}

	public Object get(int i, String key){
		if (this.beginIndex < 0 || i < 0 || i >= this.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		CustomField customField = this.findCustomField(key);
		try{
			if (customField != null){
				if (this.report != null){
					this.report.customFieldStack.push(customField);
				}
				try{
					return customField.get(customField.data.TransIndex(this, i));
				}finally{
					if (this.report != null){
						this.report.customFieldStack.pop();
					}
				}
			}else{
				return this.dataSource.get(i + this.beginIndex, key);
			}
		}catch(EvalException ex){
			if (this.logger != null){
				this.logger.evaluateError(key, ex);
			}
			return null;
		}catch(UnknownFieldException ex){
			if (this.logger != null){
				this.logger.unknownFieldError(ex);
			}
			return null;
		}
	}

	public int size(){
		if (this.beginIndex == -1){
			return 0;
		}else{
			return this.endIndex - this.beginIndex;
		}
	}

	private CustomField findCustomField(String key){
		Group g = this.group;
		while(g != null && this.hasSameSource(g.data)){
			GroupDesign gd = g.getDesign();
			if (gd.customFields != null && gd.customFields.containsKey(key)){
				return new CustomField(
						key,
						gd.customFields.get(key),
						this.report,
						g.data);
			}
			if (g.parentGroups.parentContent != null){
				g = g.parentGroups.parentContent.parentGroup;
			}else if (g.parentGroups.dataSourceGroup != null){
				g = g.parentGroups.dataSourceGroup;
			}else{
				g = null;
			}
		}
		if (this.report != null && this.hasSameSource(this.report.data)){
			ReportDesign rd = this.report.design;
			if (rd.customFields != null && rd.customFields.containsKey(key)){
				return new CustomField(
						key,
						rd.customFields.get(key),
						this.report,
						this.report.data);
			}
		}
		return null;
	}

	public int TransIndex(ReportData data, int i){
		return i + (data.beginIndex - this.beginIndex);
	}

	public ReportData findScope(String scope){
		if (scope == null){
			return this;
		}else if (scope.equals("")){
			return this.getParentData();
		}else{
			ReportData data = this;
			while(data != null){
				if (scope.equals(data.getId())){
					return data;
				}
				data = data.getParentData();
			}
			return null;
		}
	}

	public GroupDesign findUnit(String unit){
		if (unit.equals("")){
			if (this.group != null){
				ContentDesign cd = this.group.getDesign().getAggregateSrcContentDesign();
				if (cd == null || cd.groupDesign == null){
					return null;
				}
				return cd.groupDesign;
			}else{
				return this.report.groups.design;
			}
		}else{
			if (this.group != null){
				GroupDesign ret = this.group.getDesign().findGroupDesign(unit);
				if (ret != null && ret == this.group.getDesign()){
					ret = null;
				}
				return ret;
			}else{
				return this.report.groups.design.findGroupDesign(unit);
			}
		}
	}

	public ReportData getParentData(){
		if (this.group != null){
			if (this.group.parentGroups.parentContent != null){
				return this.group.parentGroups.parentContent.parentGroup.data;
			}else{
				return this.group.parentGroups.report.data;
			}
		}else{
			return null;
		}
	}

	private static class _SummaryResult{
		public BigDecimal summary;
		public int count;
		public _SummaryResult(BigDecimal summary, int count){
			this.summary = summary;
			this.count = count;
		}
	}

	public BigDecimal getSummary(String key){
		_SummaryResult sr = this.getSummary_aux(key);
		if (sr.count > 0){
			return sr.summary;
		}else{
			return null;
		}
	}

	public int getCount(String key){
		return this.getSummary_aux(key).count;
	}

	public BigDecimal getAverage(String key){
		_SummaryResult sr = this.getSummary_aux(key);
		if (sr.count > 0){
			BigDecimal d1 = sr.summary;
			BigDecimal d2 = new BigDecimal(sr.count);
			return d1.divide(
					d2,
					Math.max(0, (d1.scale() - d1.precision()) - (d2.scale() - d2.precision()) + 16),
					RoundingMode.HALF_UP).stripTrailingZeros();
		}else{
			return null;
		}
	}

	private _SummaryResult getSummary_aux(String key){
		if (!(this.dataSource instanceof INoCache) &&
				this.dataSource.size() > 0xff && this.size() > 0xff){
			return this.getSummary_cache(key);
		}else{
			return this.getSummary_noCache(key);
		}
	}

	private _SummaryResult getSummary_cache(String key){
		BigDecimal summary = new BigDecimal(0);
		int count = 0;
		Map<Integer, BigDecimal> summaryCache;
		Map<Integer, Integer> countCache;
		int _beginIndex;
		int _endIndex;
		CustomField customField = this.findCustomField(key);
		try{
			if (this.report != null && customField != null){
				this.report.customFieldStack.push(customField);
			}
			{
				DataCache dc = this.dataCache;
				if (customField != null){
					summaryCache = dc.customFieldSummary(customField.data, key);
					countCache = dc.customFieldCount(customField.data, key);
					_beginIndex = customField.data.TransIndex(this, 0);
					_endIndex = customField.data.TransIndex(this, this.size());
				}else{
					summaryCache = dc.summary(this.dataSource, key);
					countCache = dc.count(this.dataSource, key);
					_beginIndex = this.beginIndex;
					_endIndex = this.endIndex;
				}
			}
			try{
				int segf = _beginIndex >> 8;
				int segt = _endIndex >> 8;
				for(int i = segf;i <= segt;i++){
					int offf = (i == segf) ? (_beginIndex & 0xff) : 0;
					int offt = ((i == segt) ? (_endIndex & 0xff) : 0x100) - 1;
					boolean entire = (offf == 0 && offt == 0xff);
					if (entire && summaryCache.containsKey(i)){
						summary = summary.add(summaryCache.get(i));
						count += countCache.get(i);
					}else{
						BigDecimal _summary = new BigDecimal(0);
						int _count = 0;
						for(int j = offf;j <= offt;j++){
							Object o = null;
							int _i = (i << 8) | j;
							if (customField != null){
								o = customField.get(_i);
							}else{
								o = this.dataSource.get(_i, key);
							}
							if (o != null){
								BigDecimal _o = Cast.toBigDecimal(o);
								if (_o != null){
									_summary = _summary.add(_o);
								}
								_count++;
							}
						}
						summary = summary.add(_summary);
						count += _count;
						if (entire){
							summaryCache.put(i, _summary);
							countCache.put(i, _count);
						}
					}
				}
				return new _SummaryResult(summary, count);
			}finally{
				if (this.report != null && customField != null){
					this.report.customFieldStack.pop();
				}
			}
		}catch(EvalException ex){
			if (this.logger != null){
				this.logger.evaluateError(key, ex);
			}
			return new _SummaryResult(BigDecimal.ZERO, 0);
		}catch(UnknownFieldException ex){
			if (this.logger != null){
				this.logger.unknownFieldError(ex);
			}
			return new _SummaryResult(BigDecimal.ZERO, 0);
		}
	}

	private _SummaryResult getSummary_noCache(String key){
		BigDecimal summary = new BigDecimal(0);
		int count = 0;
		CustomField customField = this.findCustomField(key);
		try{
			if (this.report != null && customField != null){
				this.report.customFieldStack.push(customField);
			}
			try{
				for(int i = 0;i < this.size();i++){
					Object o;
					if (customField != null){
						o = customField.get(customField.data.TransIndex(this, i));
					}else{
						o = this.dataSource.get(i + this.beginIndex, key);
					}
					if (o != null){
						BigDecimal _o = Cast.toBigDecimal(o);
						if (_o != null){
							summary = summary.add(_o);
						}
						count++;
					}
				}
				return new _SummaryResult(summary, count);
			}finally{
				if (this.report != null && customField != null){
					this.report.customFieldStack.pop();
				}
			}
		}catch(EvalException ex){
			if (this.logger != null){
				this.logger.evaluateError(key, ex);
			}
			return new _SummaryResult(BigDecimal.ZERO, 0);
		}catch(UnknownFieldException ex){
			if (this.logger != null){
				this.logger.unknownFieldError(ex);
			}
			return new _SummaryResult(BigDecimal.ZERO, 0);
		}
	}

	public String getId(){
		if (this.group != null){
			return this.group.getDesign().id;
		}else if (this.report != null){
			return this.report.design.id;
		}else{
			return null;
		}
	}

	public int getTraversalIndex(){
		if (this.group != null){
			return this.group.traversalIndex;
		}else{
			return -1;
		}
	}

	public boolean isFilled(){
		if (this.report != null){
			return this.report.filled;
		}else{
			return false;
		}
	}

	public boolean isAggregateSrc(){
		if (this.group != null){
			if (this.group.parentGroups.parentContent != null){
				return this.group.parentGroups.parentContent.design.aggregateSrc;
			}else{
				return true;
			}
		}else if (this.report != null){
			return true;
		}else{
			return false;
		}
	}

	private ReportDataRecord _record = null;
	public ReportDataRecord getRecord(){
		if (this.size() > 0){
			if (this._record == null){
				this._record = new ReportDataRecord(this, 0);
			}
			return this._record;
		}else{
			return null;
		}
	}

	public Iterable<ReportDataRecord> getRecords(){
		return new Records(this);
	}

	private class Records implements Iterable<ReportDataRecord>{

		private ReportData data;

		public Records(ReportData reportData){
			this.data = reportData;
		}

		public Iterator<ReportDataRecord> iterator() {
			return new RecordIterator(this.data);
		}

	}

	private class RecordIterator implements Iterator<ReportDataRecord>{

		private ReportData data;
		private int i;

		public RecordIterator(ReportData reportData){
			this.data = reportData;
			this.i = -1;
		}

		public boolean hasNext() {
			this.i++;
			return this.i < this.data.size();
		}

		public ReportDataRecord next() {
			return new ReportDataRecord(this.data, this.i);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private IndexRange _emptyIndexRange = new IndexRange();
	public IndexRange getDataIndexRange(GroupDesign groupDesign){
		if (this.group != null){
			if (this.group.dataIndexRangeMap.containsKey(groupDesign)){
				return this.group.dataIndexRangeMap.get(groupDesign);
			}else{
				return this._emptyIndexRange;
			}
		}else{
			return null;
		}
	}

	public WrapperDataSource getWrapperDataSource(GroupDesign groupDesign){
		return this.report.wrapperDataSourceMap.get(groupDesign);
	}

}

package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.data.ReportData;

public class GroupDesign {

	public Map<?, ?> desc;
	public String caption;
	public String id;
	public boolean background;
	public String dataSource;
	public List<String> keys;
	public boolean detail;
	public int maxCount;
	public boolean pageBreak;
	public boolean resetPageCount;
	public List<String> sortKeys;
	public GroupLayoutDesign layout;
	public Map<String, String> customFields;
	public List<String> customFieldsKeyList;
	public List<ContentDesign> contentDesigns;

	public ReportDesign reportDesign;
	public ContentDesign parentContentDesign;

	public GroupDesign(){
		this(null);
	}

	public GroupDesign(List<String> keys){
		this.detail = false;
		this.keys = keys;
	}

	public GroupDesign(
			Map<?, ?> desc,
			ReportDesign reportDesign,
			ContentDesign parentContentDesign){
		this.desc = desc;
		this.reportDesign = reportDesign;
		this.parentContentDesign = parentContentDesign;
		this.loadDesc();
		this.loadSubDesc();
	}

	@SuppressWarnings("unchecked")
	public void loadDesc(){
		this.caption = (String)desc.get("caption");
		this.id = (String)desc.get("id");
		if (desc.containsKey("keys")){
			this.keys = new ArrayList<String>();
			for(String k: (List<String>)desc.get("keys")){
				this.keys.add(k);
			}
		}else{
			this.keys = null;
		}
		this.background = Cast.toBool(desc.get("background"));
		this.dataSource = (String)desc.get("data_source");
		this.detail = Cast.toBool(desc.get("detail"));
		this.maxCount = Cast.toInt(desc.get("max_count"));
		this.pageBreak = Cast.toBool(desc.get("page_break"));
		this.resetPageCount = Cast.toBool(desc.get("reset_page_count"));
		if (desc.containsKey("sort_keys")){
			this.sortKeys = new ArrayList<String>();
			for(String k: (List<String>)desc.get("sort_keys")){
				this.sortKeys.add(k);
			}
		}else{
			this.sortKeys = null;
		}
		if (desc.containsKey("layout")){
			this.layout = new GroupLayoutDesign((Map<?, ?>)desc.get("layout"));
		}else{
			this.layout = new GroupLayoutDesign();
		}
		if (desc.containsKey("custom_fields")){
			this.customFields = new HashMap<String, String>();
			this.customFieldsKeyList = new ArrayList<String>();
			for(Map<?, ?> d: (List<Map<?, ?>>)desc.get("custom_fields")){
				if (d.containsKey("key") && d.containsKey("exp")){
					if (!this.customFields.containsKey((String)d.get("key"))){
						this.customFields.put((String)d.get("key"), (String)d.get("exp"));
						this.customFieldsKeyList.add((String)d.get("key"));
					}
				}
			}
		}else{
			this.customFields = null;
			this.customFieldsKeyList = null;
		}
	}

	@SuppressWarnings("unchecked")
	public void loadSubDesc(){
		if (!desc.containsKey("contents") || ((List<?>)desc.get("contents")).size() == 0){
			throw new IllegalArgumentException("group has no content");
		}
		this.contentDesigns = new ArrayList<ContentDesign>();
		for(Map<?, ?> d: (List<Map<?, ?>>)desc.get("contents")){
			this.contentDesigns.add(new ContentDesign(d, this));
		}
	}

	public List<ReportData> dataSplit(ReportData data){
		List<ReportData> ret = new ArrayList<ReportData>();
		if (this.unbreakable()){
			if (data.size() > 0){
				ret.add(new ReportData(data));
			}
		}else{
			int i = 0;
			while(i < data.size()){
				int j = i + 1;
				while (j < data.size()){
					if (this.isBreak(data, i, j)){
						break;
					}
					j++;
				}
				ret.add(ReportData.getPartialData(data, i, j));
				i = j;
			}
		}
		return ret;
	}

	private boolean unbreakable(){
		if (this.detail){
			return false;
		}
		if (this.keys != null && this.keys.size() > 0){
			return false;
		}
		if (this.maxCount > 0){
			return false;
		}
		return true;
	}

	private boolean isBreak(ReportData data, int i, int j){
		if (this.detail){
			return true;
		}else if (this.keys != null){
			for (String key : this.keys){
				if (!ReportUtil.eq(data.get(i, key), data.get(j, key))){
					return true;
				}
			}
		}
		if (this.maxCount > 0 && j - i >= this.maxCount){
			return true;
		}
		return false;
	}

	public ContentDesign findContentDesign(String id){
		if (this.contentDesigns != null){
			for(ContentDesign cd: this.contentDesigns){
				if (id.equals(cd.id)){
					return cd;
				}else if (cd.groupDesign != null){
					ContentDesign ret = cd.groupDesign.findContentDesign(id);
					if (ret != null){
						return ret;
					}
				}
			}
		}
		return null;
	}

	public GroupDesign findGroupDesign(String id){
		if (id.equals(this.id)){
			return this;
		}else if (this.contentDesigns != null){
			for(ContentDesign cd: this.contentDesigns){
				if (cd.groupDesign != null){
					GroupDesign ret = cd.groupDesign.findGroupDesign(id);
					if (ret != null){
						return ret;
					}
				}
			}
		}
		return null;
	}

	public ContentDesign getAggregateSrcContentDesign(){
		for(ContentDesign d: this.contentDesigns){
			if (d.aggregateSrc){
				return d;
			}
		}
		return null;
	}

}

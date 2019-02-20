package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;

public class ContentDesign {

	public Map<?, ?> desc;
	public String caption;
	public String id;
	public ContentSizeDesign size;
	public SubContentLayoutDesign layout;
	public boolean background;
	public boolean aggregateSrc;
	public boolean everyPage;
	public boolean everyPageBlankGroup;
	public String existenceCond;
	public String visibilityCond;
	public boolean unbreakable;
	public int weight;
	public String elementsVisibilityCond;
	public Map<String, String> variables;
	public List<String> variablesKeyList;
	public String mergeContentId;
	public GroupDesign groupDesign;
	public List<ContentDesign> subContentDesigns;

	public GroupDesign parentGroupDesign;
	public ContentDesign baseContentDesign;

	public ContentDesign(
			Map<?, ?> desc,
			GroupDesign parentGroupDesign){
		this(desc, parentGroupDesign, null);
	}

	public ContentDesign(
			Map<?, ?> desc,
			GroupDesign parentGroupDesign,
			ContentDesign baseContentDesign){
		this.desc = desc;
		this.parentGroupDesign = parentGroupDesign;
		this.baseContentDesign = baseContentDesign;
		this.loadDesc();
		this.loadSubDesc();
	}

	@SuppressWarnings("unchecked")
	public void loadDesc(){
		this.caption = (String)desc.get("caption");
		this.id = (String)desc.get("id");
		if (desc.containsKey("size")){
			this.size = new ContentSizeDesign((Map<?, ?>)desc.get("size"));
		}else{
			this.size = new ContentSizeDesign();
		}
		if (desc.containsKey("layout")){
			this.layout = new SubContentLayoutDesign((Map<?, ?>)desc.get("layout"));
		}else{
			this.layout = new SubContentLayoutDesign();
		}
		this.background = Cast.toBool(desc.get("background"));
		this.aggregateSrc = Cast.toBool(desc.get("aggregate_src"));
		this.everyPage = Cast.toBool(desc.get("every_page"));
		this.everyPageBlankGroup = Cast.toBool(desc.get("every_page_blank_group"));
		this.existenceCond = (String)desc.get("existence_cond");
		this.visibilityCond = (String)desc.get("visibility_cond");
		this.unbreakable = Cast.toBool(desc.get("unbreakable"));
		this.weight = Cast.toInt(desc.get("weight"));
		this.elementsVisibilityCond = (String)desc.get("elements_visibility_cond");
		if (desc.containsKey("variables")){
			this.variables = new HashMap<String, String>();
			this.variablesKeyList = new ArrayList<String>();
			for(Map<?, ?> d: (List<Map<?, ?>>)desc.get("variables")){
				if (d.containsKey("key") && d.containsKey("exp")){
					if (!this.variables.containsKey((String)d.get("key"))){
						this.variables.put((String)d.get("key"), (String)d.get("exp"));
						this.variablesKeyList.add((String)d.get("key"));
					}
				}
			}
		}else{
			this.variables = null;
			this.variablesKeyList = null;
		}
		this.mergeContentId = (String)desc.get("merge_content_id");
	}

	@SuppressWarnings("unchecked")
	public void loadSubDesc(){
		if (this.desc.containsKey("group")){
			this.groupDesign = new GroupDesign(
					(Map<?, ?>)this.desc.get("group"),
					this.getReportDesign(),
					this);
		}else{
			this.groupDesign = null;
		}
		if (this.desc.containsKey("sub")){
			this.subContentDesigns = new ArrayList<ContentDesign>();
			for(Map<?, ?> d: (List<Map<?, ?>>)this.desc.get("sub")){
				this.subContentDesigns.add(new ContentDesign(d, this.parentGroupDesign, this));
			}
		}else{
			this.subContentDesigns = null;
		}
	}

	public int getIndex(){
		if (this.baseContentDesign != null){
			return this.baseContentDesign.getIndex();
		}else{
			return this.parentGroupDesign.contentDesigns.indexOf(this);
		}

	}

	public int getSubIndex(){
		if (this.baseContentDesign != null){
			return this.baseContentDesign.subContentDesigns.indexOf(this);
		}else{
			return -1;
		}
	}

	public ReportDesign getReportDesign(){
		return this.parentGroupDesign.reportDesign;
	}

	public ContentDesign findContentDesign(String id){
		if (id.equals(this.id)){
			return this;
		}else if (this.groupDesign != null){
			ContentDesign ret = this.groupDesign.findContentDesign(id);
			if (ret != null){
				return ret;
			}else if (this.subContentDesigns != null){
				for(ContentDesign cd: this.subContentDesigns){
					ret = cd.findContentDesign(id);
					if (ret != null){
						return ret;
					}
				}
			}
		}
		return null;
	}

	public GroupDesign findGroupDesign(String id){
		if (this.groupDesign != null){
			GroupDesign ret = this.groupDesign.findGroupDesign(id);
			if (ret != null){
				return ret;
			}
		}
		return null;
	}

}

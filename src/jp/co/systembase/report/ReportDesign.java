package jp.co.systembase.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.ContentDesign;
import jp.co.systembase.report.component.GroupDesign;
import jp.co.systembase.report.component.PaperDesign;
import jp.co.systembase.report.component.TextDesign;

public class ReportDesign {

	public Map<?, ?> desc;
	public ReportSetting setting;

	public String caption;
	public String id;
	public PaperDesign paperDesign;
	public TextDesign.FontSetting defaultFont;
	public float defaultLineWidth;
	public int pageCapacity;
	public boolean resetPageCount;
	public Map<String, String> customFields;
	public GroupDesign groupDesign;

	private Map<Map<?, ?>, Map<String, BufferedImage>> imageCache =
		new HashMap<Map<?, ?>, Map<String, BufferedImage>>();

	public ReportDesign(Map<?, ?> desc){
		this(desc, new ReportSetting());
	}

	public ReportDesign(Map<?, ?> desc, ReportSetting setting){
		this.desc = desc;
		this.setting = setting;
		this.loadDesc();
		this.loadSubDesc();
	}

	@SuppressWarnings("unchecked")
	public void loadDesc(){
		this.caption = (String)desc.get("caption");
		this.id = (String)desc.get("id");
		if (desc.containsKey("paper")){
			this.paperDesign = new PaperDesign((Map<?, ?>)desc.get("paper"));
		}else{
			this.paperDesign = new PaperDesign();
		}
		this.defaultFont = new TextDesign.FontSetting((Map<?, ?>)desc.get("font"));
		if (desc.containsKey("line_width")){
			this.defaultLineWidth = Cast.toFloat(desc.get("line_width"));
		}else{
			this.defaultLineWidth = 1.0f;
		}
		this.pageCapacity = Cast.toInt(desc.get("page_capacity"));
		this.resetPageCount = Cast.toBool(desc.get("reset_page_count"));
		if (desc.containsKey("custom_fields")){
			this.customFields = new HashMap<String, String>();
			for(Map<?, ?> d: (List<Map<?, ?>>)desc.get("custom_fields")){
				if (d.containsKey("key") && d.containsKey("exp")){
					if (!this.customFields.containsKey((String)d.get("key"))){
						this.customFields.put((String)d.get("key"), (String)d.get("exp"));
					}
				}
			}
		}else{
			this.customFields = null;
		}
	}

	public void loadSubDesc(){
		if (!desc.containsKey("group")){
			throw new IllegalArgumentException("report has no group");
		}
		this.groupDesign = new GroupDesign((Map<?, ?>)desc.get("group"), this, null);
	}

	public ContentDesign findContentDesign(String id){
		if (this.groupDesign != null){
			ContentDesign ret = this.groupDesign.findContentDesign(id);
			if (ret != null){
				return ret;
			}
		}
		return null;
	}

	public GroupDesign findGroupDesign(String name){
		if (this.groupDesign != null){
			GroupDesign ret = this.groupDesign.findGroupDesign(name);
			if (ret != null){
				return ret;
			}
		}
		return null;
	}

	public BufferedImage getImage(Map<?, ?> desc, String key){
		if (!this.imageCache.containsKey(desc) || !this.imageCache.get(desc).containsKey(key)){
			this.createImage(desc, key);
		}
		return this.imageCache.get(desc).get(key);
	}

	private void createImage(Map<?, ?> desc, String key){
		if (!this.imageCache.containsKey(desc)){
			this.imageCache.put(desc, new HashMap<String, BufferedImage>());
		}
		if (desc.containsKey(key)){
			try{
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(Base64.decode((String)desc.get(key))));
				this.imageCache.get(desc).put(key, image);
			}catch(Exception e){
				this.imageCache.get(desc).put(key, null);
			}
		}else{
			this.imageCache.get(desc).put(key, null);
		}
	}

}

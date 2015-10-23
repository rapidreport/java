package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Region;


public class SubContentLayoutDesign {

	public float x1;
	public float y1;
	public float x2;
	public float y2;
	public boolean specX1;
	public boolean specY1;
	public boolean specX2;
	public boolean specY2;
	public boolean revX1;
	public boolean revY1;
	public boolean revX2;
	public boolean revY2;

	public SubContentLayoutDesign(){
		this(new HashMap<Object, Object>());
	}
	
	public SubContentLayoutDesign(Map<?, ?> desc){
		if (desc.containsKey("x1")){
			this.x1 = Cast.toFloat(desc.get("x1"));
			this.specX1 = true;
		}else{
			this.x1 = 0;
			this.specX1 = false;
		}
		if (desc.containsKey("y1")){
			this.y1 = Cast.toFloat(desc.get("y1"));
			this.specY1 = true;
		}else{
			this.y1 = 0;
			this.specY1 = false;
		}
		if (desc.containsKey("x2")){
			this.x2 = Cast.toFloat(desc.get("x2"));
			this.specX2 = true;
		}else{
			this.x2 = 0;
			this.specX2 = false;
		}
		if (desc.containsKey("y2")){
			this.y2 = Cast.toFloat(desc.get("y2"));
			this.specY2 = true;
		}else{
			this.y2 = 0;
			this.specY2 = false;
		}
		this.revX1 = Cast.toBool(desc.get("rev_x1"));
		this.revY1 = Cast.toBool(desc.get("rev_y1"));
		this.revX2 = Cast.toBool(desc.get("rev_x2"));
		this.revY2 = Cast.toBool(desc.get("rev_y2"));
	}

	public Region getRegion(Region parentRegion){
		Region ret = new Region();
		if (this.specY1){
			if (!this.revY1){
				ret.top = parentRegion.top + this.y1;
			}else{
				ret.top = parentRegion.bottom - this.y1;
			}
		}else{
			ret.top = parentRegion.top;
		}
		if (this.specX1){
			if (!this.revX1){
				ret.left = parentRegion.left + this.x1;
			}else{
				ret.left = parentRegion.right - this.x1;
			}
		}else{
			ret.left = parentRegion.left;
		}
		if (this.specY2){
			if (!this.revY2){
				ret.bottom = parentRegion.top + this.y2;
			}else{
				ret.bottom = parentRegion.bottom - this.y2;
			}
		}else{
			ret.bottom = parentRegion.bottom;
		}
		if (this.specX2){
			if (!this.revX2){
				ret.right = parentRegion.left + this.x2;
			}else{
				ret.right = parentRegion.right - this.x2;
			}
		}else{
			ret.right = parentRegion.right;
		}
		ret.maxBottom = ret.bottom;
		ret.maxRight = ret.right;
		return ret;
	}

}

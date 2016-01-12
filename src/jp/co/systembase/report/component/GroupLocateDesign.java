package jp.co.systembase.report.component;

import java.util.Map;

import jp.co.systembase.core.Cast;

public class GroupLocateDesign {

	public float x;
	public float y;
	public int count;

	public GroupLocateDesign(Map<?, ?> desc){
		this.x = Cast.toFloat(desc.get("x"));
		this.y = Cast.toFloat(desc.get("y"));
		this.count = Cast.toInt(desc.get("count"));
	}

	public Region getRegion(Region parentRegion){
		Region ret = new Region();
		ret.top = parentRegion.top + this.y;
		ret.left = parentRegion.left + this.x;
		ret.bottom = parentRegion.bottom;
		ret.right = parentRegion.right;
		ret.maxBottom = parentRegion.maxBottom;
		ret.maxRight = parentRegion.maxRight;
		return ret;
	}

}

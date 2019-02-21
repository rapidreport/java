package jp.co.systembase.report.component;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportUtil;

public class Region {
	public float top = 0f;
	public float left = 0f;
	public float bottom = 0f;
	public float right = 0f;
	public float maxBottom = 0f;
	public float maxRight = 0f;

	public Region(){}

	public Region(Region rg){
		this.top = rg.top;
		this.left = rg.left;
		this.bottom = rg.bottom;
		this.right = rg.right;
		this.maxBottom = rg.maxBottom;
		this.maxRight = rg.maxRight;
	}

	public Region(Region rg, float ml, float mt, float mr, float mb){
		this.top = rg.top + mt;
		this.left = rg.left + ml;
		this.bottom = rg.bottom - mb;
		this.right = rg.right - mr;
		this.maxBottom = rg.maxBottom;
		this.maxRight = rg.maxRight;
	}

	public Region getMergedRegion(Region rg){
		Region ret = new Region();
		if (this.top < rg.top){
			ret.top = this.top;
		}else{
			ret.top = rg.top;
		}
		if (this.left < rg.left){
			ret.left = this.left;
		}else{
			ret.left = rg.left;
		}
		if (this.bottom > rg.bottom){
			ret.bottom = this.bottom;
		}else{
			ret.bottom = rg.bottom;
		}
		if (this.right > rg.right){
			ret.right = this.right;
		}else{
			ret.right = rg.right;
		}
		if (this.maxBottom > rg.maxBottom){
			ret.maxBottom = this.maxBottom;
		}else{
			ret.maxBottom = rg.maxBottom;
		}
		if (this.maxRight > rg.maxRight){
			ret.maxRight = this.maxRight;
		}else{
			ret.maxRight = rg.maxRight;
		}
		return ret;
	}

	public float getHeight(){
		return this.bottom - this.top;
	}

	public float getWidth(){
		return this.right - this.left;
	}

	public float getMaxHeight(){
		return this.maxBottom - this.top;
	}

	public float getMaxWidth(){
		return this.maxRight - this.left;
	}

	public void setHeight(float h){
		this.bottom = this.top + h;
	}

	public void setWidth(float w){
		this.right = this.left + w;
	}

	public Region toPointScale(ReportDesign reportDesign){
    	return this.toPointScale(reportDesign.paperDesign.scaleUnit);
    }

	public Region toPointScale(Report.EScaleUnit scaleUnit){
		Region ret = new Region();
	    ret.top = ReportUtil.toPoint(scaleUnit, this.top);
	    ret.left = ReportUtil.toPoint(scaleUnit, this.left);
	    ret.bottom = ReportUtil.toPoint(scaleUnit, this.bottom);
	    ret.right = ReportUtil.toPoint(scaleUnit, this.right);
	    ret.maxBottom = ReportUtil.toPoint(scaleUnit, this.maxBottom);
	    ret.maxRight = ReportUtil.toPoint(scaleUnit, this.maxRight);
		return ret;
	}

	public boolean isVOverflowed(){
		return (this.bottom > this.maxBottom);
	}

	public boolean isHOverflowed(){
		return (this.right > this.maxRight);
	}

	public Region clipMargin(){
		Region ret = new Region();
		ret.top = 0;
		ret.left = 0;
		ret.bottom = this.getHeight();
		ret.right = this.getWidth();
		ret.maxBottom = ret.bottom;
		ret.maxRight = ret.right;
		return ret;
	}

	@Override
	public String toString() {
		return "(" + this.left + ", " + this.top + ") - " +
		        "(" + this.right + ", " + this.bottom + ") - " +
		        "(" + this.maxRight + ", " + this.maxBottom + ")";
	}

}

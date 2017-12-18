package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.Report.EDirection;


public class GroupLayoutDesign {

	public Report.EDirection direction = EDirection.VERTICAL;
	public float size = 0;
	public boolean specSize = false;
	public float x = 0;
	public float y = 0;
	public int maxCount;
	public String maxCountExp;
	public boolean blank;
	public boolean clipOverflow;
	public List<GroupLocateDesign> locates;

	public GroupLayoutDesign(){
		this(new HashMap<Object, Object>());
	}

	@SuppressWarnings("unchecked")
	public GroupLayoutDesign(Map<?, ?> desc){
		if (desc.containsKey("direction") &&
			desc.get("direction").equals("horizontal")){
			this.direction = Report.EDirection.HORIZONTAL;
		}else{
			this.direction = Report.EDirection.VERTICAL;
		}
		if (desc.containsKey("size")){
			this.size = Cast.toFloat(desc.get("size"));
			this.specSize = true;
		}else{
			this.size = 0;
			this.specSize = false;
		}
		this.x = Cast.toFloat(desc.get("x"));
		this.y = Cast.toFloat(desc.get("y"));
		this.maxCount = Cast.toInt(desc.get("max_count"));
		this.maxCountExp = (String)desc.get("max_count_exp");
		this.blank = Cast.toBool(desc.get("blank"));
		this.clipOverflow = Cast.toBool(desc.get("clip_overflow"));
		if (desc.containsKey("locates")){
			this.locates = new ArrayList<GroupLocateDesign>();
			for(Map<?, ?> d: (List<Map<?, ?>>)desc.get("locates")){
				this.locates.add(new GroupLocateDesign(d));
			}
		}else{
			this.locates = null;
		}
	}

	public int getCount(Evaluator evaluator){
		if (this.IsLocateEnabled()){
			int ret = 0;
			for(GroupLocateDesign l: this.locates){
				ret += Math.max(1, l.count);
			}
			return ret;
		}else if (this.maxCountExp != null){
			return Cast.toInt(evaluator.evalTry(this.maxCountExp));
		}
		return this.maxCount;
	}

	public boolean IsLocateEnabled(){
		return this.locates != null && this.locates.size() > 0;
	}

	public Region getGroupRegion(
			Region parentRegion,
			Region lastRegion,
			int i){
		if (this.IsLocateEnabled()){
			int _i = i;
			for(GroupLocateDesign l: this.locates){
				if (_i == 0 || lastRegion == null){
					return l.getRegion(parentRegion);
				}else{
					_i -= Math.max(1, l.count);
					if (_i < 0){
						Region ret = new Region();
						switch(this.direction){
						case VERTICAL:
							ret.top = lastRegion.bottom;
							ret.left = lastRegion.left;
							break;
						case HORIZONTAL:
							ret.top = lastRegion.top;
							ret.left = lastRegion.right;
							break;
						}
						ret.bottom = lastRegion.bottom;
						ret.right = lastRegion.right;
						ret.maxBottom = lastRegion.maxBottom;
						ret.maxRight = lastRegion.maxRight;
						return ret;
					}
				}
			}
		}
		Region ret = new Region();
		ret.top = parentRegion.top + this.y;
		ret.left = parentRegion.left + this.x;
		ret.bottom = parentRegion.bottom;
		ret.right = parentRegion.right;
		ret.maxBottom = parentRegion.maxBottom;
		ret.maxRight = parentRegion.maxRight;
		if (lastRegion != null){
			switch(this.direction){
			case VERTICAL:
				ret.top = lastRegion.bottom;
				break;
			case HORIZONTAL:
				ret.left = lastRegion.right;
				break;
			}
		}
		return ret;
	}

	public Region getGroupInitialRegion(Region parentRegion){
		Region ret = new Region();
		ret.top = parentRegion.top;
		ret.left = parentRegion.left;
		switch(this.direction){
		case VERTICAL:
			ret.bottom = parentRegion.top;
			ret.maxBottom = parentRegion.maxBottom;
			if (this.specSize){
				ret.right = ret.left + this.size;
			}else{
				ret.right = parentRegion.right;
			}
			ret.maxRight = ret.right;
			break;
		case HORIZONTAL:
			if (this.specSize){
				ret.bottom = ret.top + this.size;
			}else{
				ret.bottom = parentRegion.bottom;
			}
			ret.maxBottom = ret.bottom;
			ret.right = parentRegion.left;
			ret.maxRight = parentRegion.maxRight;
			break;
		}
		return ret;
	}

	public Region getContentRegion(
			ContentSizeDesign sizeDesign,
			Region contentsRegion,
			Region lastRegion,
			Evaluator evaluator){
		Region ret = new Region();
		float init = sizeDesign.initial;
		float max = sizeDesign.max;
		boolean specInit = sizeDesign.specInitial;
		boolean specMax = sizeDesign.specMax;
		if (sizeDesign.initialExp != null){
			init = Cast.toFloat(evaluator.evalTry(sizeDesign.initialExp));
			specInit = true;
		}
		if (sizeDesign.maxExp != null){
			max = Cast.toFloat(evaluator.evalTry(sizeDesign.maxExp));
			specMax = true;
		}
		switch(this.direction){
		case VERTICAL:
			if (lastRegion == null){
				ret.top = contentsRegion.top;
				ret.left = contentsRegion.left;
			}else{
				ret.top = lastRegion.bottom;
				ret.left = lastRegion.left;
			}
			if (specInit){
				if (!sizeDesign.revInitial){
					ret.bottom = ret.top + init;
				}else{
					ret.bottom = contentsRegion.maxBottom - init;
				}
			}else{
				ret.bottom = ret.top;
			}
			ret.right = contentsRegion.right;
			if (sizeDesign.notExtendable){
				ret.maxBottom = ret.bottom;
			}else if (specMax){
				if (!sizeDesign.revMax){
					ret.maxBottom = ret.top + max;
				}else{
					ret.maxBottom = contentsRegion.maxBottom - max;
				}
			}else{
				ret.maxBottom = contentsRegion.maxBottom;
			}
			if (ret.maxBottom > contentsRegion.maxBottom){
				ret.maxBottom = contentsRegion.maxBottom;
			}
			ret.maxRight = ret.right;
			break;
		case HORIZONTAL:
			if (lastRegion == null){
				ret.top = contentsRegion.top;
				ret.left = contentsRegion.left;
			}else{
				ret.top = lastRegion.top;
				ret.left = lastRegion.right;
			}
			ret.bottom = contentsRegion.bottom;
			if (specInit){
				if (!sizeDesign.revInitial){
					ret.right = ret.left + init;
				}else{
					ret.right = contentsRegion.maxRight - init;
				}
			}else{
				ret.right = ret.left;
			}
			ret.maxBottom = ret.bottom;
			if (sizeDesign.notExtendable){
				ret.maxRight = ret.right;
			}else if (specMax){
				if (!sizeDesign.revMax){
					ret.maxRight = ret.left + max;
				}else{
					ret.maxRight = contentsRegion.maxRight - max;
				}
			}else{
				ret.maxRight = contentsRegion.maxRight;
			}
			if (ret.maxRight > contentsRegion.maxRight){
				ret.maxRight = contentsRegion.maxRight;
			}
			break;
		}
		if (ret.bottom < ret.top){
			ret.bottom = ret.top;
		}
		if (ret.maxBottom < ret.top){
			ret.maxBottom = ret.top;
		}
		if (ret.right < ret.left){
			ret.right = ret.left;
		}
		if (ret.maxRight < ret.left){
			ret.maxRight = ret.left;
		}
		return ret;
	}

}

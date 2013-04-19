package jp.co.systembase.report.component;

import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.Report.EPaperType;
import jp.co.systembase.report.Report.EScaleUnit;


public class PaperSizeDesign {

	public float width;
	public float height;

	public PaperSizeDesign(){}

	public PaperSizeDesign(Map<?, ?>desc){
		this.width = Cast.toFloat(desc.get("width"));
		this.height = Cast.toFloat(desc.get("height"));
	}

	public PaperSizeDesign(EScaleUnit scaleUnit, EPaperType paperType){
		switch(scaleUnit){
		case POINT:
			this.initialize_point(paperType);
			break;
		case MM:
			this.initialize_mm(paperType);
			break;
		case INCH:
			this.initialize_inch(paperType);
			break;
		}
	}

	private void initialize_point(EPaperType paperType){
		switch(paperType){
		case A3:
			this.width = 842;
			this.height = 1191;
			break;
		case A4:
			this.width = 595;
			this.height = 842;
			break;
		case A5:
			this.width = 420;
			this.height = 595;
			break;
		case B4:
			this.width = 728;
			this.height = 1031;
			break;
		case B5:
			this.width = 515;
			this.height = 728;
			break;
		}
	}

	private void initialize_mm(EPaperType paperType){
		switch(paperType){
		case A3:
			this.width = 297;
			this.height = 420;
			break;
		case A4:
			this.width = 210;
			this.height = 297;
			break;
		case A5:
			this.width = 148;
			this.height = 210;
			break;
		case B4:
			this.width = 257;
			this.height = 364;
			break;
		case B5:
			this.width = 182;
			this.height = 257;
			break;
		}
	}

	private void initialize_inch(EPaperType paperType){
		switch(paperType){
		case A3:
			this.width = 11.69f;
			this.height = 16.54f;
			break;
		case A4:
			this.width = 8.27f;
			this.height = 11.69f;
			break;
		case A5:
			this.width = 5.83f;
			this.height = 8.27f;
			break;
		case B4:
			this.width = 10.12f;
			this.height = 14.33f;
			break;
		case B5:
			this.width = 7.17f;
			this.height = 10.12f;
			break;
		}
	}

	public PaperSizeDesign toPoint(PaperDesign paperDesign){
		return this.toPoint(paperDesign.scaleUnit);
	}

	public PaperSizeDesign toPoint(Report.EScaleUnit scaleUnit){
		PaperSizeDesign ret = new PaperSizeDesign();
		ret.width = ReportUtil.toPoint(scaleUnit, this.width);
		ret.height = ReportUtil.toPoint(scaleUnit, this.height);
		return ret;
	}


}

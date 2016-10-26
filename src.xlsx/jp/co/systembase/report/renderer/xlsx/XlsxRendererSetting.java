package jp.co.systembase.report.renderer.xlsx;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.xlsx.XlsxRendererSetting;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.BarcodeRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.CircleRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.DummyRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.FieldRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.IElementRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.ImageRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.LineRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.RectRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.SubPageRenderer;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.TextRenderer;
import jp.co.systembase.report.textformatter.DefaultTextFormatter;
import jp.co.systembase.report.textformatter.ITextFormatter;

public class XlsxRendererSetting {

	public IElementRenderer dummyElementRenderer;
	public String defaultFont;
	public ITextFormatter defaultTextFormatter;

	public Map<String, IElementRenderer> elementRendererMap = new HashMap<String, IElementRenderer>();
	public Map<String, String> fontMap = new HashMap<String, String>();
	public Map<String, ITextFormatter> textFormatterMap = new HashMap<String, ITextFormatter>();
	public float colWidthMax;
	public float rowHeightMax;
	public short vResolution;
	public short hResolution;
	public float colWidthCoefficient;
	public float rowHeightCoefficient;

	public XlsxRendererSetting(){
		this.dummyElementRenderer = new DummyRenderer();
		this.elementRendererMap.put("rect", new RectRenderer());
		this.elementRendererMap.put("circle", new CircleRenderer());
		this.elementRendererMap.put("line", new LineRenderer());
		this.elementRendererMap.put("field", new FieldRenderer());
		this.elementRendererMap.put("text", new TextRenderer());
		this.elementRendererMap.put("barcode", new BarcodeRenderer());
		this.elementRendererMap.put("image", new ImageRenderer());
		this.elementRendererMap.put("subpage", new SubPageRenderer());
		this.defaultFont = "ＭＳ ゴシック";
		this.fontMap.put("gothic", "ＭＳ ゴシック");
		this.fontMap.put("mincho", "ＭＳ 明朝");
		this.defaultTextFormatter = new DefaultTextFormatter();
		this.colWidthMax = 800f;
		this.rowHeightMax = 350f;
		this.vResolution = 600;
		this.hResolution = 600;
		this.colWidthCoefficient = 1.0f;
		this.rowHeightCoefficient = 1.0f;
	}

	public IElementRenderer getElementRenderer(String key){
		if (key != null && this.elementRendererMap.containsKey(key)){
			return this.elementRendererMap.get(key);
		}else{
			return this.dummyElementRenderer;
		}
	}

	public String getFont(String key){
		if (key != null && this.fontMap.containsKey(key)){
			return this.fontMap.get(key);
		}else{
			return this.defaultFont;
		}
	}

	@Override
	public XlsxRendererSetting clone() {
		try{
			XlsxRendererSetting ret = (XlsxRendererSetting)super.clone();
			ret.elementRendererMap = new HashMap<String, IElementRenderer>();
			for(String k: this.elementRendererMap.keySet()){
				ret.elementRendererMap.put(k, this.elementRendererMap.get(k));
			}
			ret.fontMap = new HashMap<String, String>();
			for(String k: this.fontMap.keySet()){
				ret.fontMap.put(k, this.fontMap.get(k));
			}
			ret.textFormatterMap = new HashMap<String, ITextFormatter>();
			for(String k: this.textFormatterMap.keySet()){
				ret.textFormatterMap.put(k, this.textFormatterMap.get(k));
			}
			return ret;
		}catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}

}

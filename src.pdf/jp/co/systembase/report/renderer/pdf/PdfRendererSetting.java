package jp.co.systembase.report.renderer.pdf;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.pdf.elementrenderer.BarcodeRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.CircleRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.DummyRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.FieldRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.IElementRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.ImageRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.LineRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.RectRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.SubPageRenderer;
import jp.co.systembase.report.renderer.pdf.elementrenderer.TextRenderer;

import com.lowagie.text.pdf.BaseFont;

public class PdfRendererSetting {

	public IElementRenderer dummyElementRenderer;
	public BaseFont defaultFont;
	public BaseFont gaijiFont = null;
	public Map<String, IElementRenderer> elementRendererMap = new HashMap<String, IElementRenderer>();
	public Map<String, BaseFont> fontMap = new HashMap<String, BaseFont>();
	public boolean replaceBackslashToYen;
	public float shrinkFontSizeMin;
	public float underlineWidthCoefficient = 1.0f;
	public static boolean skipInitialFontCreate = false;
	
	public PdfRendererSetting(){
		this.dummyElementRenderer = new DummyRenderer();
		this.elementRendererMap.put("rect", new RectRenderer());
		this.elementRendererMap.put("circle", new CircleRenderer());
		this.elementRendererMap.put("line", new LineRenderer());
		this.elementRendererMap.put("field", new FieldRenderer());
		this.elementRendererMap.put("text", new TextRenderer());
		this.elementRendererMap.put("barcode", new BarcodeRenderer());
		this.elementRendererMap.put("image", new ImageRenderer());
		this.elementRendererMap.put("subpage", new SubPageRenderer());
		try {
			if (!skipInitialFontCreate){
				this.defaultFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);
				this.fontMap.put("gothic", this.defaultFont);
				this.fontMap.put("mincho", BaseFont.createFont("HeiseiMin-W3", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED));
			}
			else{
				this.defaultFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.replaceBackslashToYen = false;
		this.shrinkFontSizeMin = 4.0f;
	}

	public IElementRenderer getElementRenderer(String key){
		if (key != null && this.elementRendererMap.containsKey(key)){
			return this.elementRendererMap.get(key);
		}else{
			return this.dummyElementRenderer;
		}
	}

	public BaseFont getFont(String key){
		if (key != null && this.fontMap.containsKey(key)){
			return this.fontMap.get(key);
		}else{
			return this.defaultFont;
		}
	}

	@Override
	public PdfRendererSetting clone() {
		try{
			PdfRendererSetting ret = (PdfRendererSetting)super.clone();
			ret.elementRendererMap = new HashMap<String, IElementRenderer>();
			for(String k: this.elementRendererMap.keySet()){
				ret.elementRendererMap.put(k, this.elementRendererMap.get(k));
			}
			ret.fontMap = new HashMap<String, BaseFont>();
			for(String k: this.fontMap.keySet()){
				ret.fontMap.put(k, this.fontMap.get(k));
			}
			return ret;
		}catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}
}

package jp.co.systembase.report.renderer.xlsx.component;

import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;


public class FontStyle {

	private static final short FONT_SIZE_MIN = 1;

	public TextDesign design;

	public FontStyle(TextDesign design){
		this.design = design;
	}

	public void fill(XSSFFont font, XlsxRenderer renderer){
		font.setFontName(renderer.setting.getFont(this.design.font.name));
		font.setFontHeightInPoints((short)Math.max(this.design.font.size, FONT_SIZE_MIN));
		if (this.design.font.bold){
			font.setBold(true);
		}
		if (this.design.font.italic){
			font.setItalic(true);
		}
		if (this.design.font.underline){
			font.setUnderline(XSSFFont.U_SINGLE);
		}
		if (this.design.color != null){
			XSSFColor c = ColorUtil.getFontColor(this.design.color);
			if (c != null){
				font.setColor(c);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.design.font.bold ? 1231 : 1237);
		result = prime * result + ((this.design.color == null) ? 0 : this.design.color.hashCode());
		result = prime * result + (this.design.font.italic ? 1231 : 1237);
		result = prime * result + ((this.design.font.name == null) ? 0 : this.design.font.name.hashCode());
		result = prime * result + Float.floatToIntBits(this.design.font.size);
		result = prime * result + (this.design.font.underline ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontStyle other = (FontStyle) obj;
		if (this.design.font.bold != other.design.font.bold)
			return false;
		if (this.design.color == null) {
			if (other.design.color != null)
				return false;
		} else if (!this.design.color.equals(other.design.color))
			return false;
		if (this.design.font.italic != other.design.font.italic)
			return false;
		if (this.design.font.name == null) {
			if (this.design.font.name != null)
				return false;
		} else if (!this.design.font.name.equals(this.design.font.name))
			return false;
		if (Float.floatToIntBits(this.design.font.size) != Float.floatToIntBits(other.design.font.size))
			return false;
		if (this.design.font.underline != other.design.font.underline)
			return false;
		return true;
	}

}

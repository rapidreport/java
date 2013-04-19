package jp.co.systembase.report.textformatter;

import jp.co.systembase.report.component.ElementDesign;

public class DefaultTextFormatter implements ITextFormatter {
	public String format(Object v, ElementDesign design) {
		return TextFormatterUtil.format(v, design);
	}
}

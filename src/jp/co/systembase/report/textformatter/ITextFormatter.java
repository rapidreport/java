package jp.co.systembase.report.textformatter;

import jp.co.systembase.report.component.ElementDesign;

public interface ITextFormatter {
	String format(Object v, ElementDesign design);
}

package test;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.BaseFont;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.PdfRendererSetting;

public class Test_4_36_IpaFont {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_36_ipa_font";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
        report.fill(DummyDataSource.getInstance());

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRendererSetting setting = new PdfRendererSetting();
				setting.fontMap.put("ipamjm", BaseFont.createFont("rrpt\\font\\ipamjm.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
				PdfRenderer renderer = new PdfRenderer(fos, setting);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

}

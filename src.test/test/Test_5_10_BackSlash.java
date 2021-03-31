package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.PdfRendererSetting;

public class Test_5_10_BackSlash {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_10_backslash";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(new ReportDataSource(getDataTable()));

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRendererSetting setting = new PdfRendererSetting();
				setting.replaceBackslashToYen = true;
				PdfRenderer renderer = new PdfRenderer(fos, setting);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable() throws Exception {
		DataTable ret = new DataTable();
		ret.setFieldNames("v");

		ret.addRecord().puts("1234567890\\234567890");
		ret.addRecord().puts("1234567\\90\\234567890");
		ret.addRecord().puts("1234567\\90\\23456\\890");
		ret.addRecord().puts("\\1\\2\\3\\4\\5\\6\\7\\8\\9\\0111");
		ret.addRecord().puts("123456789012345678901234567890");

		return ret;
	}

}

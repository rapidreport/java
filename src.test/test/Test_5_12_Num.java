package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.PdfRendererSetting;

public class Test_5_12_Num {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_12_num";

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
		ret.addRecord().puts("1");
		ret.addRecord().puts("2");
		ret.addRecord().puts("3");
		return ret;
	}

}

package test;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_5_9_WeekdayOperator {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_9_weekdayoperator";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(new ReportDataSource(getDataTable()));

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable() throws Exception {
		// DataTableはList<Map>を拡張したクラスで
		// 列と行からなる表データを格納することができます
		DataTable ret = new DataTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		ret.setFieldNames("v", "answer");

		ret.addRecord().puts(sdf.parse("2021/01/13"), "3");
		ret.addRecord().puts(sdf.parse("2021/01/14"), "4");
		ret.addRecord().puts(sdf.parse("2021/01/15"), "5");
		ret.addRecord().puts(sdf.parse("2021/01/16"), "6");
		ret.addRecord().puts(sdf.parse("2021/01/17"), "0");
		ret.addRecord().puts(sdf.parse("2021/01/18"), "1");
		ret.addRecord().puts(sdf.parse("2021/01/19"), "2");

		return ret;
	}

}

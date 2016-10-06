package test;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class Test_4_24_Exp {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_24_exp";

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
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet(name);
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.newSheet(name);
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

	private static DataTable getDataTable() throws Exception {
		DataTable ret = new DataTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		ret.setFieldNames("日本語", "半角 スペース", "全角　スペース","半角()","全角（）","円マーク\\","改行\n改行","シングルクォート\'",
				"bumonCd","kaCd","kingaku(1)","kingaku(2)","kingaku(3)","bool(1)", "kingaku1", "kingaku2", "kingaku3");

		ret.addRecord().puts("１番", sdf.parse("2013/03/01"),
			"株式会社　岩手商事", "半角カッコ()", "全角カッコ（）", "円マーク\\", "改行\nした", "シングルクォート\'",
			10, 1, 1000, 10, 3, true, 2000, 20, 6);
		ret.addRecord().puts("１番", sdf.parse("2013/03/01"),
			"株式会社　岩手商事", "半角カッコ()", "全角カッコ（）", "円マーク\\", "改行\nした", "シングルクォート\'",
			10, 1, 990, 20, 4, false, 1980, 40, 8);
		ret.addRecord().puts("１番", sdf.parse("2013/03/01"),
			"株式会社　岩手商事", "半角カッコ()", "全角カッコ（）", "円マーク\\", "改行\nした", "シングルクォート\'",
			10, 2, 990, 20, 4, false, 1980, 40, 8);
		ret.addRecord().puts("１番", sdf.parse("2013/03/01"),
			"株式会社　岩手商事", "半角カッコ()", "全角カッコ（）", "円マーク\\", "改行\nした", "シングルクォート\'",
			10, 2, 990, 20, 4, false, 1980, 40, 8);
		ret.addRecord().puts("１番", sdf.parse("2013/03/01"),
			"株式会社　岩手商事", "半角カッコ()", "全角カッコ（）", "円マーク\\", "改行\nした", "シングルクォート\'",
			20, 1, 980, 20, 4, false, 1980, 40, 8);
		ret.addRecord().puts("１番", sdf.parse("2013/03/01"),
			"株式会社　岩手商事", "半角カッコ()", "全角カッコ（）", "円マーク\\", "改行\nした", "シングルクォート\'",
			20, 1, 970, 20, 4, false, 1940, 40, 8);

		return ret;
	}
}

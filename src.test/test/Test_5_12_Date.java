package test;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.PdfRendererSetting;

public class Test_5_12_Date {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_12_date";

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

		{
			Calendar cal = Calendar.getInstance();
			cal.set(2020, 1, 1, 1, 1, 1);
			ret.addRecord().puts(cal.getTime());
		}
		{
			Calendar cal = Calendar.getInstance();
			cal.set(2020, 0, 2, 1, 1, 2);
			ret.addRecord().puts(cal.getTime());
		}
		{
			Calendar cal = Calendar.getInstance();
			cal.set(2020, 0, 3, 1, 1, 3);
			ret.addRecord().puts(cal.getTime());
		}
		ret.addRecord().puts(LocalDate.of(2020, 1, 1));
		ret.addRecord().puts(LocalDate.of(2020, 1, 2));
		ret.addRecord().puts(LocalDate.of(2020, 1, 3));
		ret.addRecord().puts(LocalDateTime.of(2020, 1, 1, 1, 2, 1));
		ret.addRecord().puts(LocalDateTime.of(2020, 1, 2, 1, 2, 2));
		ret.addRecord().puts(LocalDateTime.of(2020, 1, 3, 1, 2, 3));
		ret.addRecord().puts(ZonedDateTime.of(2020, 1, 1, 1, 3, 1, 0, ZoneId.systemDefault()));
		ret.addRecord().puts(ZonedDateTime.of(2020, 1, 2, 1, 3, 2, 0, ZoneId.systemDefault()));
		ret.addRecord().puts(ZonedDateTime.of(2020, 1, 3, 1, 3, 3, 0, ZoneId.systemDefault()));

		return ret;
	}

}

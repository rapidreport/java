package test;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import test.Test_4_26_UnknownField.Logger;

public class Test_4_28_MonthEn {

	public static void main(String[] args) throws Exception {
		String name = "test_4_28_month_en";

		ReportSetting setting = new ReportSetting();
		Logger logger = new Logger();
		setting.logger = logger;

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"), setting);
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
		DataTable ret = new DataTable();
		ret.setFieldNames("date");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		ret.addRecord().puts(sdf.parse("20100102010203"));
		ret.addRecord().puts(sdf.parse("20110204020304"));
		ret.addRecord().puts(sdf.parse("20120306030405"));
		ret.addRecord().puts(sdf.parse("20130408040506"));
		ret.addRecord().puts(sdf.parse("20140510050607"));
		ret.addRecord().puts(sdf.parse("20150612060708"));
		ret.addRecord().puts(sdf.parse("20160714070809"));
		ret.addRecord().puts(sdf.parse("20170816080910"));
		ret.addRecord().puts(sdf.parse("20180918091011"));
		ret.addRecord().puts(sdf.parse("20191020101112"));
		ret.addRecord().puts(sdf.parse("20201122111213"));
		ret.addRecord().puts(sdf.parse("20211224121314"));
		return ret;
	}

}

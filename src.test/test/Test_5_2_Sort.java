package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_5_2_Sort {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_2_sort";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(new ReportDataSource(_getDataTable()));

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

	private static DataTable _getDataTable() throws Throwable
	{
		DataTable ret = new DataTable();
		ret.setFieldNames("col1", "col2");
		ret.addRecord().puts(1, 10);
		ret.addRecord().puts(2, 20);
		ret.addRecord().puts(3, 30);
		ret.addRecord().puts(4, 20);
		ret.addRecord().puts(5, 10);
		ret.addRecord().puts(6, 30);
		ret.addRecord().puts(7, 10);
		ret.addRecord().puts(8, 30);
		ret.addRecord().puts(9, 20);
		ret.addRecord().puts(10, 30);
		ret.addRecord().puts(11, 20);
		ret.addRecord().puts(12, 10);
		return ret;
	}

}

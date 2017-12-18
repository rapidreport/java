package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_34_DynamicContentSize {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_34_dynamic_content_size";

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

    private static DataTable getDataTable() throws Throwable {
		DataTable ret = new DataTable();
		ret.setFieldNames("text1", "text2", "h");
		ret.addRecord().puts("1111\n2222\n3333", "1111\n2222", 40);
		ret.addRecord().puts("1111\n2222", "1111\n2222\n3333", 40);
		return ret;
    }
}

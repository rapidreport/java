package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_34_FooterIfSpace {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_34_footer_if_space";

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
		ret.setFieldNames("k", "v");
		for(int i = 1;i <= 5;i++){
			for(int j = 1;j <= 3 + i;j++){
				ret.addRecord().puts(i, j);	
			}
		}
		return ret;
    }
}

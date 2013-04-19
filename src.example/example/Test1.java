package example;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test1 {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/test1.rrpt"));
		ReportPages pages;
		{
			long t = System.currentTimeMillis();
			report.fill(new ReportDataSource(getDataTable()));
			pages = report.getPages();
			System.out.println("page count: " + pages.size());
			System.out.println("page split: " + (System.currentTimeMillis() - t));
		}
		{
			long t = System.currentTimeMillis();
			FileOutputStream fos = new FileOutputStream("output/test1.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
			System.out.println("pdf : " + (System.currentTimeMillis() - t));
		}
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("ID1", "ID2", "ID3", "NUM", "TXT1", "TXT2");
		for(int i1 = 1;i1 <= 10;i1++){
			for(int i2 = 1;i2 <= 10;i2++){
				for(int i3 = 1;i3 <= 100;i3++){
					String t = String.valueOf(i1) + String.valueOf(i2) + String.valueOf(i3);
					ret.addRecord().puts(i1, i2, i3, i3,
							"hogehogehogehogehogehogehogehoge" + t,
							"fugafugafugafugafugafugafugafuga" + t);
				}
			}
		}
		return ret;
	}

}

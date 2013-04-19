package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.customizer.DefaultCustomizer;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

public class ExamplePage {

	public static void main(String[] args) throws Throwable {
		ReportPages pages;
		{
			Report report = new Report(
					ReadUtil.readJson("report/example_page1.rrpt"),
					new Customizer());
			report.fill(new ReportDataSource(getDataTable()));
			pages = report.getPages();
		}

		{
			Report report = new Report(ReadUtil.readJson("report/example_page3.rrpt"));
			report.fill(DummyDataSource.getInstance());
			// pagesに、他の帳票のページを追加する
			pages.addAll(report.getPages());
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_page.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_page.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("example_page");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("GROUP_CD", "DATA");
		ret.addRecord().puts("A", "A-1");
		ret.addRecord().puts("A", "A-2");
		ret.addRecord().puts("A", "A-3");
		ret.addRecord().puts("A", "A-4");
		ret.addRecord().puts("A", "A-5");
		ret.addRecord().puts("A", "A-6");
		ret.addRecord().puts("A", "A-7");
		ret.addRecord().puts("A", "A-8");
		ret.addRecord().puts("A", "A-9");
		ret.addRecord().puts("A", "A-10");
		ret.addRecord().puts("A", "A-11");
		ret.addRecord().puts("A", "A-12");
		ret.addRecord().puts("A", "A-13");
		ret.addRecord().puts("A", "A-14");
		ret.addRecord().puts("A", "A-15");
		ret.addRecord().puts("A", "A-16");
		ret.addRecord().puts("A", "A-17");
		ret.addRecord().puts("A", "A-18");
		ret.addRecord().puts("A", "A-19");
		ret.addRecord().puts("A", "A-20");
		ret.addRecord().puts("B", "B-1");
		ret.addRecord().puts("B", "B-2");
		ret.addRecord().puts("B", "B-3");
		ret.addRecord().puts("B", "B-4");
		ret.addRecord().puts("B", "B-5");
		ret.addRecord().puts("B", "B-6");
		ret.addRecord().puts("B", "B-7");
		ret.addRecord().puts("B", "B-8");
		ret.addRecord().puts("B", "B-9");
		ret.addRecord().puts("B", "B-10");
		return ret;
	}

	private static class Customizer extends DefaultCustomizer{

		private ReportDesign reportDesign;

		public Customizer() throws Throwable{
			this.reportDesign = new ReportDesign(ReadUtil.readJson("report/example_page2.rrpt"));
		}

		@Override
		public void pageAdded(
				Report report,
				ReportPages pages,
				ReportPage page) {
			// このメソッドはページが追加されるたびに呼ばれる。
			// このページでgroup_example(事前に名前をつけておく必要がある)グループが終了してるかをチェック
			Group g = page.findFinishedGroup("group_example");
			if (g != null){
				// 直前に終了したグループのデータを利用して別のレイアウトのページを挿入する
				Report _report = new Report(this.reportDesign);
				_report.fill(g.data);
				pages.addAll(_report.getPages());
			}
		}

	}
}

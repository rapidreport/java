package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jp.co.systembase.core.Cast;
import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.component.Content;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.customizer.DefaultCustomizer;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

public class ExampleRegion {

	public static void main(String[] args) throws Throwable {
		ReportDesign design = new ReportDesign(ReadUtil.readJson("report/example_region.rrpt"));

		//動的にマージンを変更
		design.paperDesign.margin.left = 50f;

		Report report = new Report(design, new Customizer());
		report.fill(new ReportDataSource(getDataTable()));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/example_region.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_region.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("example_region");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("HEIGHT");
		ret.addRecord().puts(20);
		ret.addRecord().puts(30);
		ret.addRecord().puts(40);
		ret.addRecord().puts(50);
		ret.addRecord().puts(60);
		ret.addRecord().puts(70);
		ret.addRecord().puts(80);
		ret.addRecord().puts(90);
		ret.addRecord().puts(100);
		return ret;
	}

	private static class Customizer extends DefaultCustomizer{
		@Override
		public Region contentRegion(
				Content content,
				Evaluator evaluator,
				Region region) {
			if ("content_example".equals(content.design.id)){
				//regionはcontentの表示領域を表す
				//contentの高さをデータ内のHEIGHT値に設定する
				float height = Cast.toFloat(evaluator.evalTry(".HEIGHT"));
				Region ret = new Region(region);
				ret.setHeight(height);
				return ret;
			}else{
				return region;
			}
		}
	}
}

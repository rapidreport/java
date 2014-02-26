package example;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.ImageMap;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.imageloader.PdfImageLoader;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.imageloader.XlsImageLoader;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;
import jp.co.systembase.report.renderer.xlsx.imageloader.XlsxImageLoader;

public class Example1 {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/example1.rrpt"));
		report.fill(new ReportDataSource(getDataTable()));
		report.globalScope.put("today", new Date());
		ImageMap imageMap = new ImageMap();
		imageMap.put("logo", ImageIO.read(new File("image/logo.png")));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/example1.pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.imageLoaderMap.put("image", new PdfImageLoader(imageMap));
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example1.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.imageLoaderMap.put("image", new XlsImageLoader(imageMap));
				renderer.newSheet("example1");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example1.xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.imageLoaderMap.put("image", new XlsxImageLoader(imageMap));
				renderer.newSheet("example1");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("PLACE", "TITLE", "PRICE");
		ret.addRecord().puts(1, "Emacs辞典", 2980);
		ret.addRecord().puts(1, "Rubyist Magazine 正しいRubyコードの書き方", 2600);
		ret.addRecord().puts(1, "完全図解式ネットワーク再入門", 1480);
		ret.addRecord().puts(1, "SQLクイックリファレンス", 2800);
		ret.addRecord().puts(1, "MicrosoftSilverlight完全解説", 1905);
		ret.addRecord().puts(1, "Eclipse3完全攻略", 2800);
		ret.addRecord().puts(2, "Eclipse3.4プラグイン開発 徹底攻略", 3900);
		ret.addRecord().puts(2, "dRubyによる分散・Webプログラミング", 3200);
		ret.addRecord().puts(2, "デザインパターン", 4800);
		ret.addRecord().puts(2, "Apacheハンドブック", 4600);
		ret.addRecord().puts(2, "Ant", 2600);
		ret.addRecord().puts(2, "HTML&XHTML", 5800);
		ret.addRecord().puts(2, "JavaScript", 4200);
		ret.addRecord().puts(2, "iText IN ACTION", 4000);
		ret.addRecord().puts(2, "JQuery+JavaScript実践リファレンス", 2800);
		ret.addRecord().puts(2, "基礎からのサーブレット/JSP", 2800);
		ret.addRecord().puts(2, "RailsによるアジャイルWebアプリケーション開発", 3800);
		ret.addRecord().puts(2, "Tomcatハンドブック", 4700);
		ret.addRecord().puts(2, "入門UNIXシェルプログラミング", 3200);
		ret.addRecord().puts(2, "プログラマのためのSQL", 4500);
		ret.addRecord().puts(2, "PHP5徹底攻略", 2800);
		ret.addRecord().puts(2, "JQueryクックブック", 3600);
		ret.addRecord().puts(2, "PDFリファレンス", 6800);
		ret.addRecord().puts(2, "プログラミングRuby 言語編", 3800);
		ret.addRecord().puts(2, "プログラミングRuby ライブラリ編", 4200);
		ret.addRecord().puts(2, "Effective Java", 3600);
		ret.addRecord().puts(2, "Postfix実用ガイド", 3200);
		ret.addRecord().puts(3, "LINQ実践サンプル集", 3800);
		ret.addRecord().puts(3, "Cプログラミング診断室", 2427);
		ret.addRecord().puts(3, "入門csh & tcsh", 2900);
		ret.addRecord().puts(3, "ピープルウェア", 2200);
		ret.addRecord().puts(3, "JavaからRubyへ", 2200);
		ret.addRecord().puts(3, "ハッカーと画家", 2400);
		ret.addRecord().puts(3, "EffectiveC++", 3800);
		ret.addRecord().puts(3, "EffectiveSTL", 3200);
		ret.addRecord().puts(3, "GNUソフトウェアプログラミング", 3200);
		ret.addRecord().puts(3, "携帯端末用Web制作バイブル", 2980);
		ret.addRecord().puts(3, "UNIXネットワーク管理者ハンドブック", 3980);
		ret.addRecord().puts(3, "UNIXシステム管理者ハンドブック", 4800);
		ret.addRecord().puts(3, "開発者ノートシリーズ Hibernate", 2400);
		ret.addRecord().puts(3, "アジャイルプラクティス", 2400);
		ret.addRecord().puts(3, "Joel on software", 2800);
		ret.addRecord().puts(4, "オブジェクト指向入門", 4835);
		return ret;
	}

}

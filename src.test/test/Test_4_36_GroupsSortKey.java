package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.data.GroupDataProvider;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_36_GroupsSortKey {

	public static void main(String[] args) throws Exception {
		String name = "test_4_36_groups_sortkey";

		ReportSetting setting = new ReportSetting();

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"), setting);

		GroupDataProvider provider = new GroupDataProvider();
		provider.groupDataMap.put("group_id", new ReportDataSource(getGroupDataTable()));

		report.fill(new ReportDataSource(getDataTable()), provider);

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
		ret.setFieldNames("group_key");
		ret.addRecord().puts("b");
		ret.addRecord().puts("a");
		return ret;
	}

	private static DataTable getGroupDataTable() throws Exception {
		DataTable ret = new DataTable();
		ret.setFieldNames("group_key", "value");
		ret.addRecord().puts("b", "b-2");
		ret.addRecord().puts("b", "b-1");
		ret.addRecord().puts("a", "a-3");
		ret.addRecord().puts("a", "a-1");
		ret.addRecord().puts("a", "a-2");
		return ret;
	}

}

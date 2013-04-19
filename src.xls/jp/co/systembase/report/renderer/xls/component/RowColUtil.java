package jp.co.systembase.report.renderer.xls.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

public class RowColUtil {

	public static final float TOLERANCE = 1.0f;

	public static List<Integer> createColWidths(List<Float> cols, float coefficent){
		List<Integer> ret = new ArrayList<Integer>();
		int last = 0;
		for(int i = 1;i < cols.size();i++){
			int t = (int)(cols.get(i) * coefficent);
			ret.add((int)((t - last) * 36.55));
			last = t;
		}
		return ret;
	}

	public static List<Short> createRowHeights(List<Float> rows, float coefficent){
		List<Short> ret = new ArrayList<Short>();
		int last = 0;
		for(int i = 1;i < rows.size();i++){
			int t = (int)(rows.get(i) * coefficent);
			ret.add((short)((t - last) * 15));
			last = t;
		}
		return ret;
	}

	public static List<Float> createCols(ReportDesign reportDesign, XlsRenderer renderer){
		List<_IContainer> l = new ArrayList<_IContainer>();
		for(Page page: renderer.pages){
			for(Grid grid: page.grids){
				l.add(new _LeftGridContainer(grid));
				l.add(new _RightGridContainer(grid));
			}
			for(Field field: page.fields){
				l.add(new _LeftFieldContainer(field));
				l.add(new _RightFieldContainer(field));
			}
			for(Shape shape: page.shapes){
				l.add(new _LeftShapeContainer(shape));
				l.add(new _RightShapeContainer(shape));
			}
		}
		return createRowCols_aux(l, renderer.setting.colWidthMax);
	}

	public static List<Float> createRows(ReportDesign reportDesign, Page page){
		List<_IContainer> l = new ArrayList<_IContainer>();
		for(Grid grid: page.grids){
			l.add(new _TopGridContainer(grid));
			l.add(new _BottomGridContainer(grid));
		}
		for(Field field: page.fields){
			l.add(new _TopFieldContainer(field));
			l.add(new _BottomFieldContainer(field));
		}
		for(Shape shape: page.shapes){
			l.add(new _TopShapeContainer(shape));
			l.add(new _BottomShapeContainer(shape));
		}
		return createRowCols_aux(l, page.renderer.setting.rowHeightMax);
	}

	private static List<Float> createRowCols_aux(List<_IContainer> l, float max){
		List<Float> ret = new ArrayList<Float>();
		Collections.sort(l, new _ContainerComparator());
		float pointPos = 0;
		int pos = 0;
		ret.add(pointPos);
		for(_IContainer c: l){
			float _pointPos = c.getPointPos();
			if (_pointPos - pointPos < TOLERANCE){
				c.setPos(pos);
			}else{
				while(pointPos + max < _pointPos){
					pos++;
					pointPos += max;
					ret.add(pointPos);
				}
				pos++;
				pointPos = _pointPos;
				c.setPos(pos);
				ret.add(pointPos);
			}
		}
		return ret;
	}

	private static interface _IContainer{
		float getPointPos();
		void setPos(int pos);
	}

	private static class _ContainerComparator implements java.util.Comparator<_IContainer>{
		 public int compare(_IContainer o1, _IContainer o2){
			 float v = o1.getPointPos() - o2.getPointPos();
			 if (v < 0){
				 return -1;
			 }else if (v > 0){
				 return 1;
			 }else{
				 return 0;
			 }
		 }
	 }

	private static class _LeftGridContainer implements _IContainer{
		private Grid grid;
		public _LeftGridContainer(Grid grid){this.grid = grid;}
		public float getPointPos() {return this.grid.region.left;}
		public void setPos(int pos) {this.grid.cellRange.col1 = pos;}
	}

	private static class _RightGridContainer implements _IContainer{
		private Grid grid;
		public _RightGridContainer(Grid grid){this.grid = grid;}
		public float getPointPos() {return this.grid.region.right;}
		public void setPos(int pos) {this.grid.cellRange.col2 = pos;}
	}

	private static class _TopGridContainer implements _IContainer{
		private Grid grid;
		public _TopGridContainer(Grid grid){this.grid = grid;}
		public float getPointPos() {return this.grid.region.top;}
		public void setPos(int pos) {this.grid.cellRange.row1 = pos;}
	}

	private static class _BottomGridContainer implements _IContainer{
		private Grid grid;
		public _BottomGridContainer(Grid grid){this.grid = grid;}
		public float getPointPos() {return this.grid.region.bottom;}
		public void setPos(int pos) {this.grid.cellRange.row2 = pos;}
	}

	private static class _LeftFieldContainer implements _IContainer{
		private Field field;
		public _LeftFieldContainer(Field field){this.field = field;}
		public float getPointPos() {return this.field.region.left;}
		public void setPos(int pos) {this.field.cellRange.col1 = pos;}
	}

	private static class _RightFieldContainer implements _IContainer{
		private Field field;
		public _RightFieldContainer(Field field){this.field = field;}
		public float getPointPos() {return this.field.region.right;}
		public void setPos(int pos) {this.field.cellRange.col2 = pos;}
	}

	private static class _TopFieldContainer implements _IContainer{
		private Field field;
		public _TopFieldContainer(Field field){this.field = field;}
		public float getPointPos() {return this.field.region.top;}
		public void setPos(int pos) {this.field.cellRange.row1 = pos;}
	}

	private static class _BottomFieldContainer implements _IContainer{
		private Field field;
		public _BottomFieldContainer(Field field){this.field = field;}
		public float getPointPos() {return this.field.region.bottom;}
		public void setPos(int pos) {this.field.cellRange.row2 = pos;}
	}

	private static class _LeftShapeContainer implements _IContainer{
		private Shape shape;
		public _LeftShapeContainer(Shape shape){this.shape = shape;}
		public float getPointPos() {return this.shape.region.left;}
		public void setPos(int pos) {this.shape.cellRange.col1 = pos;}
	}

	private static class _RightShapeContainer implements _IContainer{
		private Shape shape;
		public _RightShapeContainer(Shape shape){this.shape = shape;}
		public float getPointPos() {return this.shape.region.right;}
		public void setPos(int pos) {this.shape.cellRange.col2 = pos;}
	}

	private static class _TopShapeContainer implements _IContainer{
		private Shape shape;
		public _TopShapeContainer(Shape shape){this.shape = shape;}
		public float getPointPos() {return this.shape.region.top;}
		public void setPos(int pos) {this.shape.cellRange.row1 = pos;}
	}

	private static class _BottomShapeContainer implements _IContainer{
		private Shape shape;
		public _BottomShapeContainer(Shape shape){this.shape = shape;}
		public float getPointPos() {return this.shape.region.bottom;}
		public void setPos(int pos) {this.shape.cellRange.row2 = pos;}
	}

}

package jp.co.systembase.report.renderer.xls.component;

import java.util.ArrayList;
import java.util.List;

public class CellMap {

	public List<List<Cell>> map;

	public CellMap(int rows, int cols, Page page){
		this.map = new ArrayList<List<Cell>>();
		for(int i = 0;i < rows + 1;i++){
			this.map.add(new ArrayList<Cell>());
			for(int j = 0;j < cols + 1;j++){
				this.map.get(i).add(null);
			}
		}
		this.fillGrids(page.grids);
		this.fillFields(page.fields);
	}

	private Cell createOrGetCell(int row, int col){
		Cell ret = this.map.get(row).get(col);
		if (ret == null){
			ret = new Cell(row, col);
			this.map.get(row).set(col, ret);
		}
		return ret;
	}

	private CellStyle createOrGetCellStyle(int row, int col){
		Cell cell = this.createOrGetCell(row, col);
		if (cell.style == null){
			cell.style = new CellStyle();
		}
		return cell.style;
	}

	private void fillGrids(List<Grid> grids){
		for(Grid grid: grids){
			if (grid.style.leftBorder != null){
				for(int r = grid.cellRange.row1;r < grid.cellRange.row2;r++){
					this.createOrGetCellStyle(r, grid.cellRange.col1).gridStyle.leftBorder = grid.style.leftBorder;
				}
			}
			if (grid.style.rightBorder != null){
				for(int r = grid.cellRange.row1;r < grid.cellRange.row2;r++){
					this.createOrGetCellStyle(r, grid.cellRange.col2 - 1).gridStyle.rightBorder = grid.style.rightBorder;
				}
			}
			if (grid.style.topBorder != null){
				for(int c = grid.cellRange.col1;c < grid.cellRange.col2;c++){
					this.createOrGetCellStyle(grid.cellRange.row1, c).gridStyle.topBorder = grid.style.topBorder;
				}
			}
			if (grid.style.bottomBorder != null){
				for(int c = grid.cellRange.col1;c < grid.cellRange.col2;c++){
					this.createOrGetCellStyle(grid.cellRange.row2 - 1, c).gridStyle.bottomBorder = grid.style.bottomBorder;
				}
			}
			if (grid.style.fillColor != null){
				for(int r = grid.cellRange.row1;r < grid.cellRange.row2;r++){
					for(int c = grid.cellRange.col1;c < grid.cellRange.col2;c++){
						this.createOrGetCellStyle(r, c).gridStyle.fillColor = grid.style.fillColor;
					}
				}
			}
		}
	}

	private void fillFields(List<Field> fields){
		for(Field field: fields){
			this.createOrGetCellStyle(field.cellRange.row1, field.cellRange.col1).fieldStyle = field.style;
			Cell cell = this.createOrGetCell(field.cellRange.row1, field.cellRange.col1);
			for(int r = field.cellRange.row1; r < field.cellRange.row2; r++){
				for(int c = field.cellRange.col1;c < field.cellRange.col2;c++){
					Cell _cell = this.createOrGetCell(r, c);
					this.relocate(_cell);
					_cell.mergedCell = cell;
				}
			}
			if (field.data instanceof String){
				String t = (String)field.data;
				t = t.replaceAll("\r\n", "\n");
				cell.data = t;
			}else{
				cell.data = field.data;
			}
			cell.range.row2 = field.cellRange.row2;
			cell.range.col2 = field.cellRange.col2;
		}
	}

	private void relocate(Cell cell){
		if (cell.mergedCell == cell){
			int r2 = cell.range.row2;
			int c2 = cell.range.col2;
			for(int r = cell.range.row1; r < r2; r++){
				for(int c = cell.range.col1; c < c2; c++){
					this.createOrGetCell(r, c).reset();
				}
			}
		}else{
			Cell _cell = cell.mergedCell;
			if (cell.range.row1 > _cell.range.row1){
				for(int r = cell.range.row1; r < _cell.range.row2; r++){
					for(int c = _cell.range.col1; c < _cell.range.col2; c++){
						this.createOrGetCell(r, c).reset();
					}
				}
				_cell.range.row2 = cell.range.row1;
			}else{
				for(int c = cell.range.col1; c < _cell.range.col2; c++){
					for(int r = _cell.range.row1; r < _cell.range.row2; r++){
						this.createOrGetCell(r, c).reset();
					}
				}
				_cell.range.col2 = cell.range.col1;
			}
		}
	}

}

package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import automation.SystemParamater;

public class FileWriter {

	public void writeColumnBasedOnSource(String sourceFile, String targetFile,
			String columnHeader, List<String> stringList, int targetColumn,
			int offset) {
		try {
			FileInputStream fileIn = new FileInputStream(sourceFile);
			XSSFWorkbook xlsWorkBook = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = xlsWorkBook.getSheetAt(0);
			try {

				Iterator itr = stringList.listIterator();
				for (Row row : sheet) {
					Cell c = row.getCell(targetColumn);

					if (c.getRowIndex() == 0) {
						Cell headerTag = row.createCell(targetColumn + offset);
						headerTag.setCellValue(columnHeader);
						continue;
					}

					if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
						// Nothing in the cell in this row, skip it
					} else {
						// cells.add(c);
						String resultStr = (String) itr.next();
						
						Cell nor = row.createCell(targetColumn + offset);
						nor.setCellValue(resultStr);

						// System.out.println(c.getStringCellValue());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			FileOutputStream fileOut = new FileOutputStream(targetFile);
			xlsWorkBook.write(fileOut);
			fileIn.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	public void writeNormalizedNameList(List<String> sourceStrList, int columnNo) {
		try {
			FileInputStream fileIn = new FileInputStream(
					SystemParamater.NON_SQL_FILE_NAME);
			XSSFWorkbook xlsWorkBook = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = xlsWorkBook.getSheetAt(0);
			try {

				Iterator itr = sourceStrList.listIterator();
				for (Row row : sheet) {
					Cell c = row.getCell(columnNo);

					if (c.getRowIndex() == 0) {
						Cell headerTag = row.createCell(columnNo + 3);
						headerTag.setCellValue("normName");
						continue;
					}

					if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
						// Nothing in the cell in this row, skip it
					} else {
						// cells.add(c);
						Cell nor = row.createCell(columnNo + 3);
						String resultStr = (String) itr.next();
						nor.setCellValue(resultStr);
						// System.out.println(c.getStringCellValue());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			FileOutputStream fileOut = new FileOutputStream(
					SystemParamater.NORM_FILE_NAME);
			xlsWorkBook.write(fileOut);
			fileIn.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import automation.SystemParamater;

public class FileSplitter {

	public void writeNonSQLJobList(int columnNo){
		try {
			FileInputStream fileIn = new FileInputStream(
					SystemParamater.SOURCE_FILE_NAME);
			FileOutputStream fileOut = new FileOutputStream(
					SystemParamater.NON_SQL_FILE_NAME);
			XSSFWorkbook xlsWorkBook = new XSSFWorkbook(fileIn);
			XSSFSheet sheetInput = xlsWorkBook.getSheetAt(0);
			
			try {
				for (int i = 0; i != sheetInput.getLastRowNum();++i) {
					Row row = sheetInput.getRow(i);
					Cell c = row.getCell(columnNo);
					if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
						// Nothing in the cell in this row, skip it
					} else {
						String str = c.getStringCellValue();
						int rowIndex = row.getRowNum();
						int lastRowNum=sheetInput.getLastRowNum();
						if(isSQLJob(str)){
							i--;
							System.out.println(str);
							if(rowIndex >= 0 && rowIndex < lastRowNum){
								sheetInput.shiftRows(rowIndex + 1, lastRowNum, -1);
							}
							if(rowIndex == lastRowNum){
								Row removed = sheetInput.getRow(rowIndex);
								if(removed != null){
									sheetInput.removeRow(removed);
								}
							}
						}
						
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		//	sheetOutput = sheetInput;
			xlsWorkBook.write(fileOut);
			fileIn.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isSQLJob(String jobName) {
		if (indexOf(Pattern.compile("INSERT INTO"), jobName.toUpperCase()) != -1) {
			return true;
		}
		if (indexOf(Pattern.compile("INSERT OVERWRITE TAB"), jobName.toUpperCase()) != -1) {
			return true;
		}
		if (indexOf(Pattern.compile("CREATE TABLE\\s+"), jobName.toUpperCase()) != -1) {
			return true;
		}
		if (indexOf(Pattern.compile("SELECT\\s+"), jobName.toUpperCase()) != -1) {
			return true;
		}
		
		
		return false;
	}
	private static int indexOf(Pattern pattern, String s) {
	    Matcher matcher = pattern.matcher(s);
	    return matcher.find() ? matcher.start() : -1;
	

}
}

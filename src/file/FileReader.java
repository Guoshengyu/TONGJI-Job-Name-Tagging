package file;

import java.io.FileInputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import automation.SystemParamater;
import preprocess.TimeNormalizer;

public class FileReader {
	
	//Return the column number in the excel file
	public Integer searchTargetColumn(String file, String target) {
		Integer columnNo = null;
		try {
			FileInputStream fileIn = new FileInputStream(
					file);

			XSSFWorkbook xlsWorkBook = new XSSFWorkbook(fileIn);

			XSSFSheet sheet = xlsWorkBook.getSheetAt(0);

			// we will search for column index containing string
			// "Your Column Name" in the row 0 (which is first row of a
			String columnWanted = target;
			// output all not null values to the list
			// List<Cell> cells = new ArrayList<Cell>();

			Row firstRow = sheet.getRow(0);

			for (Cell cell : firstRow) {
				if (cell.getStringCellValue().equals(columnWanted)) {
					columnNo = cell.getColumnIndex();
				}
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columnNo;
	}


	public List<String> getColumnCellList(String sourceFile, Integer columnNo) {
		List<String> result = new ArrayList<String>();
		try {
			FileInputStream fileIn = new FileInputStream(
					sourceFile);

			XSSFWorkbook xlsWorkBook = new XSSFWorkbook(fileIn);

			XSSFSheet sheet = xlsWorkBook.getSheetAt(0);

			if (columnNo != null) {
				for (Row row : sheet) {
					Cell c = row.getCell(columnNo);
					if (c.getRowIndex() == 0)
						continue;
					if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
						// Nothing in the cell in this row, skip it
					} else {
						// cells.add(c);
						result.add(c.getStringCellValue());
					}
				}
			} else {
				System.out
						.println("could not find column Normalized jobName in first row of "
								+ fileIn.toString());
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public Map<String, List<String>> getUserJobNameMap() {
		FileReader reader = new FileReader();
		Map<String, List<String>> mapUserJobName = new LinkedHashMap<String, List<String>>();
		int jobColumnNo = reader.searchTargetColumn(SystemParamater.NORM_FILE_NAME,
				"normName");
		int userColumnNo = reader.searchTargetColumn(SystemParamater.NORM_FILE_NAME,
				"user");
		List<String> jobList = reader.getColumnCellList(
				SystemParamater.NORM_FILE_NAME, jobColumnNo);
		//jobList.remove(0);
		List<String> userList = reader.getColumnCellList(
				SystemParamater.NORM_FILE_NAME, userColumnNo);
		//userList.remove(0);
		assert (jobList.size() == userList.size());
		ListIterator<String> jobIter = jobList.listIterator();
		ListIterator<String> userIter = userList.listIterator();
		List<String> currentJobList = new ArrayList<String>();

		int jobCount = 0;
		for (int index = 0; index != jobList.size(); ++index) {

			String currentUser = (String) userIter.next();
			if (userIter.hasNext()) {
				String nextUser = (String) userIter.next();
				userIter.previous();

				if (currentUser.equals(nextUser)) {
					currentJobList.add((String) jobIter.next());
				} else {
					currentJobList.add((String) jobIter.next());
					mapUserJobName.put(currentUser, new ArrayList(currentJobList));
					
					jobCount += currentJobList.size();
					currentJobList.clear();
				}
			}else{
				currentJobList.add((String) jobIter.next());
				mapUserJobName.put(currentUser, new ArrayList(currentJobList));
				jobCount += currentJobList.size();
			}
		}
		return mapUserJobName;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// test file is located in your project path
		FileReader reader = new FileReader();
		FileWriter writer = new FileWriter();
		FileSplitter splitter = new FileSplitter();
		TimeNormalizer norm = new TimeNormalizer();
		
		Integer columnNo = reader.searchTargetColumn(SystemParamater.NON_SQL_FILE_NAME, "jobName");
		List<String> jobList = reader.getColumnCellList(SystemParamater.NON_SQL_FILE_NAME, columnNo);
		
	
		
		List<String> normList = norm.getTimeNormalizedJobList(jobList);
		
		writer.writeNormalizedNameList(normList, columnNo + 3);
		

	}
}
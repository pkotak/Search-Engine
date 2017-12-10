import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import utilities.Constants;

public class Results {
	

	/**
	 * @param unsortedResultList
	 * @return top 100 list of sorted and rank result
	 */
	public static List<Result> sortResultAndRank(List<Result> unsortedResultList) {
		
		AtomicInteger counter = new AtomicInteger(1);
		
		List<Result> resultList = unsortedResultList.stream().sorted((a, b) -> Double.compare(b.Score(), a.Score()))
				.limit(100)
				.collect(Collectors.toCollection(ArrayList<Result>::new));
		
		resultList.stream().forEach(x -> {
			x.changeRank(counter.getAndIncrement());
		});
		
		return resultList;
	}
	
	/**
	 * @param docID
	 * @param score
	 * @param queryID
	 * @return Result Object
	 */
	public static Result make(String docID, double score, int queryID) {
		
		return new Result1(docID, score, queryID, "sys", "model");
	}
	
	public static void writeResultsToFile(String filePath, List<Query> queryList) {
		
		String fileName = filePath.substring(0, filePath.lastIndexOf(File.separator));
		fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		HSSFWorkbook workbook = new HSSFWorkbook();
		FileOutputStream out = null;
		try {
		out =  new FileOutputStream(filePath + fileName + ".xls");
			//out = new FileOutputStream(new File(filePath + fileName + ".xlsx"));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		queryList.stream().forEach(query -> {
				HSSFSheet currentSheet = workbook.createSheet(String.valueOf(query.queryID()));
				
				HSSFRow mainRow = currentSheet.createRow(0);
				mainRow.createCell(0).setCellValue("Query ID");
				mainRow.createCell(1).setCellValue("Literal");
				mainRow.createCell(2).setCellValue("Document ID");
				mainRow.createCell(3).setCellValue("Model_Score");
				mainRow.createCell(4).setCellValue("Rank");
				mainRow.createCell(5).setCellValue("System Name");
				query.resultList().stream().forEach(result -> {
					
					HSSFRow currentRow = currentSheet.createRow(result.rank());
					
					currentRow.createCell(0).setCellValue(result.queryID());
					currentRow.createCell(1).setCellValue(result.literal());
					currentRow.createCell(2).setCellValue(result.docID());
					currentRow.createCell(3).setCellValue(result.modelName() + "_" + result.Score());
					currentRow.createCell(4).setCellValue(result.rank());
					currentRow.createCell(5).setCellValue(result.systemName());
				});
		});	
		try {
			workbook.write(out);
			out.close();
			workbook.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.out.println("Results are stored in: " + filePath);
	}
	
	public static void main(String[] args) {
		
		writeResultsToFile(Constants.TASK1_PHASE1_TFIDF, null);
	}
}

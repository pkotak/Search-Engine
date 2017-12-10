import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * Helper class for Evaluation1
 */
public class Evaluations {
	

	/**
	 * @param queryList list of queries with their results
	 * @return Evaluation object
	 * @see
	 * {@link Evaluation1 class} implementing {@link Evaluation}
	 */
	public static Evaluation getEvaluation(List<Query> queryList) {
		
		return new Evaluation1(queryList.stream()
				.filter(query -> !query.listOfRelevantDocuments().isEmpty())
				.collect(Collectors.toList()));
	}
	
	/**
	 * @param q
	 * @param rank
	 * @Where System has been evaluated i.e {@link Evaluations#getEvaluation getEvaluation} method has been called
	 * @return precision at the given rank for the given query
	 */
	public static double getPAtK(Query q, int rank) {
		
		return q.resultList().stream()
				.filter(result -> result.rank() == rank)
				.findFirst().get().precision();
	}
	
	/**
	 * @param filePath
	 * @param queryList
	 * @Effects writes the evaluation data to an excel file. 
	 * File name is determined by retrieval model used.
	 * Sheet name is determined by the query ID.
	 */
	public static void writeEvaluationToFile(String filePath, Evaluation eva) {
		
		String fileName = filePath.substring(0, filePath.lastIndexOf(File.separator));
		fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		HSSFWorkbook workbook = new HSSFWorkbook();
		FileOutputStream out = null;
		try {
		out =  new FileOutputStream(filePath + fileName + "Evaluation.xls");
			//out = new FileOutputStream(new File(filePath + fileName + ".xlsx"));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		HSSFSheet mainSheet = workbook.createSheet("MainSheet");
		mainSheet.createRow(0).createCell(0).setCellValue("Mean Average Precision");
		mainSheet.createRow(1).createCell(0).setCellValue("Mean Reciprocal Rank");
		mainSheet.getRow(0).createCell(1).setCellValue(eva.MAP());
		mainSheet.getRow(1).createCell(1).setCellValue(eva.MRR());
		eva.queryListOfSystem().stream().forEach(query -> {
			
				HSSFSheet currentSheet = workbook.createSheet(String.valueOf(query.queryID()));
				
				HSSFRow mainRow = currentSheet.createRow(0);
				mainRow.createCell(0).setCellValue("P@5");
				mainRow.createCell(1).setCellValue(getPAtK(query, 5));
				mainRow.createCell(2).setCellValue("P@20");
				mainRow.createCell(3).setCellValue(getPAtK(query, 20));
				HSSFRow secondMainRow = currentSheet.createRow(1);
				secondMainRow.createCell(0).setCellValue("Document ID");
				secondMainRow.createCell(1).setCellValue("Precision");
				secondMainRow.createCell(2).setCellValue("Recall");
				query.resultList().stream().forEach(result -> {
					
					HSSFRow currentRow = currentSheet.createRow(result.rank() + 1);
					
					currentRow.createCell(0).setCellValue(result.docID());
					currentRow.createCell(1).setCellValue(result.precision());
					currentRow.createCell(2).setCellValue(result.recall());
				
				});
			
		});
		try {
			workbook.write(out);
			out.close();
			workbook.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.out.println("Evaluations are stored in: "+ filePath);
	}
	
}

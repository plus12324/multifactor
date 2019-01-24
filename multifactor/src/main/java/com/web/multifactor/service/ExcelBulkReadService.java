package com.web.multifactor.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.web.multifactor.model.SampleModel;
import com.web.multifactor.repository.mybatis.SampleRepository;

/**
 * bulk jpa  : https://www.hameister.org/SpringBootUsingIdsForBulkImports.html
 * bulk mybatis : https://araikuma.tistory.com/480
 * XSSF and SAX (Event API) basic example.
 * See {@link XLSX2CSV} for a fuller example of doing
 *  XSLX processing with the XSSF Event code.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class ExcelBulkReadService {
	
	@Autowired
	SampleRepository sampleRepository;
	
	public void processSheet(InputStream fileInputStream) throws Exception {
		try (OPCPackage opc = OPCPackage.open(fileInputStream)) {
			XSSFReader r = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) r.getSheetsData();
			
			StylesTable styles = r.getStylesTable();
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			
			List<String[]> dataList = new ArrayList<String[]>();
			
			while(itr.hasNext()) {
				try(InputStream sheetStream = itr.next();){
					InputSource sheetSource =  new InputSource(sheetStream);
					Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 10);
					ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, false);
					
					XMLReader sheetParser =  SAXHelper.newXMLReader();
					sheetParser.setContentHandler(handler);
					sheetParser.parse(sheetSource);
				}
			}
		}
	}
	
    private class Sheet2ListHandler implements org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler{
    	private int currentCol = -1;    	
    	private List<String[]> rows;
    	private String[] row;
    	private int columnCnt;
    	private int currColNum = 0;
    	
    	public Sheet2ListHandler(List<String[]> rows, int columnsCnt) {
    		this.rows = rows;
    		this.columnCnt = columnsCnt;
    	}

		@Override
		public void startRow(int rowNum) {
			// TODO Auto-generated method stub
			this.row = new String[columnCnt];
			currColNum = 0;
		}
		
		@Override
		public void endRow(int rowNum) {			
			// TODO Auto-generated method stub
			boolean addFlag = false;
			for(String data : row) {
				if(!"".equals(data))
					addFlag = true;
			}
			
			sampleRepository.insert((new SampleModel()).builder().id((long) rowNum).name(row[2]).build());			
//			if(addFlag) rows.add(row);
		}
		
		@Override
		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
			// TODO Auto-generated method stub
			int curCol = (new CellReference(cellReference))	.getCol();
			int missedCols = curCol - currentCol - 1;
			
			for(int i=0; i < missedCols ; i++) {
				row[currColNum]="";
				currColNum +=1;
			}
			
			currentCol = curCol;
			row[currColNum] = formattedValue == null ? "" : formattedValue;
			currColNum +=1;
		}

		@Override
		public void headerFooter(String text, boolean isHeader, String tagName) {
			// TODO Auto-generated method stub
			SheetContentsHandler.super.headerFooter(text, isHeader, tagName);
		}
    }
   
//    public static void main(String[] args) throws Exception {    	
//    	ExcelBulkReadService howto = new ExcelBulkReadService();
//        howto.processSheet("C:\\Users\\sungb_000\\git\\multifactor\\multifactor\\lll.xlsx");
//    }
}
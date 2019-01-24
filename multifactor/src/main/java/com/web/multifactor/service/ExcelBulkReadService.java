package com.web.multifactor.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.web.multifactor.repository.jpa.UserRepository;
import com.web.multifactor.repository.mybatis.SampleRepository;

/**
 * bulk jpa  : https://www.hameister.org/SpringBootUsingIdsForBulkImports.html
 * bulk mybatis : https://araikuma.tistory.com/480
 * XSSF and SAX (Event API) basic example.
 * See {@link XLSX2CSV} for a fuller example of doing
 *  XSLX processing with the XSSF Event code.
 */
@Transactional(rollbackFor=Exception.class)
public class ExcelBulkReadService {
	
	@Autowired
	SampleRepository sampleRepository;
	
	@Autowired
	UserRepository userRepository;
	
//	@Async
	public void processSheet(String filename) throws Exception {
		try (OPCPackage opc = OPCPackage.open(filename, PackageAccess.READ)) {
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
					
					SAXParserFactory saxFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxFactory.newSAXParser();
					
					XMLReader sheetParser = saxParser.getXMLReader();
					sheetParser.setContentHandler(handler);
					System.out.println("파싱시작");
					sheetParser.parse(sheetSource);
				}
			}
		}
	}
	
    public void processFirstSheet(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);

            // process the first sheet
            try (InputStream sheet = r.getSheetsData().next()) {
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
			}
        }
    }
	
	@Async
    public void processAllSheets(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);

            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                System.out.println("Processing new sheet:\n");
                try (InputStream sheet = sheets.next()) {
					InputSource sheetSource = new InputSource(sheet);
					parser.parse(sheetSource);
				}
                
                System.out.println();
            }
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = SAXHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }
    
  

    private class Sheet2ListHandler implements org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler{
    	private boolean firstCellOfRow = false;
    	private int currentCol = -1;
    	
    	private List<String[]> rows;
    	private String[] row;
    	private int columnCnt;
    	private int currColNum = 0;
    	
    	public Sheet2ListHandler(List<String[]> rows, int columnsCnt) {
    		this.rows = rows;
    		this.columnCnt = columnsCnt;
    	}
    	
//		@Override
//		public void startRow(int rowNum) {
//			// TODO Auto-generated method stub
//			this.row = new String[columnCnt];
//			currColNum = 0;
//		}
//		
//		@Override
//		public void endRow(int rowNum) {
//			// TODO Auto-generated method stub
//			boolean addFlag = false;
//			for(String data : row) {
//				if(!"".equals(data))
//					addFlag = true;
//			}
//			
//			if(addFlag) rows.add(row);
//		}
//		
//		@Override
//		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
//			// TODO Auto-generated method stub
//			int curCol = (new CellReference(cellReference))	.getCol();
//			int missedCols = curCol - currentCol - 1;
//			for(int i=0; i < missedCols ; i++) {
//				row[currColNum++]="";
//			}
//			currentCol = curCol;
//			row[currColNum++] = formattedValue == null ? "" : formattedValue;
//			System.out.println(row[currColNum++]);
//		}
		
		@Override
		public void headerFooter(String text, boolean isHeader, String tagName) {
			// TODO Auto-generated method stub
			SheetContentsHandler.super.headerFooter(text, isHeader, tagName);
		}

		@Override
		public void startRow(int rowNum) {
			System.out.println(rowNum);
			// TODO Auto-generated method stub
			this.row = new String[columnCnt];
			currColNum = 0;
		}
		
		@Override
		public void endRow(int rowNum) {
			System.out.println(rowNum);
			// TODO Auto-generated method stub
			boolean addFlag = false;
			for(String data : row) {
				if(!"".equals(data))
					addFlag = true;
			}
			
			if(addFlag) rows.add(row);
		}
		
		@Override
		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
			// TODO Auto-generated method stub
			int curCol = (new CellReference(cellReference))	.getCol();
			int missedCols = curCol - currentCol - 1;
			for(int i=0; i < missedCols ; i++) {
				row[currColNum++]="";
			}
			currentCol = curCol;
			row[currColNum++] = formattedValue == null ? "" : formattedValue;
			System.out.println(row[currColNum++]);
		}
    }
    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean inlineStr;
        private final LruCache<Integer,String> lruCache = new LruCache<>(50);
        //dao 멤버변수 선언 후 생성자로 주입해 디비입력
         
        private static class LruCache<A,B> extends LinkedHashMap<A, B> {
            private final int maxEntries;

            public LruCache(final int maxEntries) {
                super(maxEntries + 1, 1.0f, true);
                this.maxEntries = maxEntries;
            }

            @Override
            protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
                return super.size() > maxEntries;
            }
        }

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
                
                inlineStr = cellType != null && cellType.equals("inlineStr");
                System.out.print(" |필드명 : " + attributes.getValue("r") +" / "+ nextIsString +" | ");
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
            	
            	Integer idx = Integer.valueOf(lastContents);
                lastContents = lruCache.get(idx);
                System.out.print("값 : " + lastContents);
                
                if (lastContents == null && !lruCache.containsKey(idx)) {
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    lruCache.put(idx, lastContents);
                }
                nextIsString = false;
            }
            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v") || (inlineStr && name.equals("c"))) {
//                System.out.println("");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);
            if(!nextIsString) System.out.print("필드값 : " + lastContents);
        }
    }

    public static void main(String[] args) throws Exception {    	
    	ExcelBulkReadService howto = new ExcelBulkReadService();
//        howto.processFirstSheet("C:\\Users\\educar\\git\\multifactor\\multifactor\\lll.xls");
        howto.processSheet("C:\\Users\\educar\\git\\multifactor\\multifactor\\lll.xlsx");
    }
}
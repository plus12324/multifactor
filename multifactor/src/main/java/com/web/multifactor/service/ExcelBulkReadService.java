package com.web.multifactor.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
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
	
	@Async
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
                System.out.print(attributes.getValue("r") + " - ");
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
                System.out.print(nextIsString + "/");
                inlineStr = cellType != null && cellType.equals("inlineStr");
                System.out.print(inlineStr);
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
            	System.out.println("■B : " + lastContents);
            	Integer idx = Integer.valueOf(lastContents);
                lastContents = lruCache.get(idx);
                System.out.println("■A : " + lastContents);
                
                if (lastContents == null && !lruCache.containsKey(idx)) {
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    lruCache.put(idx, lastContents);
                }
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v") || (inlineStr && name.equals("c"))) {
                System.out.println(localName +" / "+ lastContents);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);
            System.out.println("■ : " + lastContents);
        }
    }

    public static void main(String[] args) throws Exception {    	
    	ExcelBulkReadService howto = new ExcelBulkReadService();
        howto.processFirstSheet("C:\\Users\\sungb_000\\git\\multifactor\\multifactor\\test.xlsx");
//        howto.processAllSheets("C:\\Users\\sungb_000\\git\\multifactor\\multifactor\\test.xlsx");
    }
}
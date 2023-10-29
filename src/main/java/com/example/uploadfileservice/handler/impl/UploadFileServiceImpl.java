package com.example.uploadfileservice.handler.impl;

import com.example.uploadfileservice.exception.CommonBusinessException;
import com.example.uploadfileservice.handler.UploadFileService;
import com.example.uploadfileservice.model.entity.FileDetail;
import com.example.uploadfileservice.repository.FileDetailRepository;
import com.github.pjfanning.xlsx.StreamingReader;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class UploadFileServiceImpl implements UploadFileService {

    private static final String XLSX = "xlsx";
    private static final String XLS = "xls";

    @Autowired
    FileDetailRepository fileDetailRepository;

    @Override
    public void uploadFile(Path path, String fileExtension, long fileId) {
        try {
            Workbook workbook;
            InputStream inputStream = Files.newInputStream(path);
            switch (fileExtension) {
                case XLSX:
                    workbook = StreamingReader.builder()
                            .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                            .bufferSize(4096)     // buffer size (in bytes) to use when reading InputStream to file (defaults to 1024)
                            .open(inputStream);
                    break;
                case XLS:
                    workbook = new HSSFWorkbook(inputStream);
                    break;
                default:
                    throw new CommonBusinessException("Unsupported format");
            }
            this.handleReadingSheet(workbook, fileId);
        } catch (Exception e) {
            log.error("Exception reading the XlSX file ", e);
        } finally {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleReadingSheet(Workbook workbook, long fileId) {
        for (Sheet sheet : workbook) {
            for (Row r : sheet) {
                //Header rows
                List<String> valueInEachRow = new ArrayList<>();
                for (Cell c : r) {
                    /**
                     * Handle simple case
                     */
                    switch (c.getCellType()) {
                        case STRING:
                            valueInEachRow.add(c.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(c)) {
                                valueInEachRow.add(String.valueOf(c.getDateCellValue()));
                            } else {
                                valueInEachRow.add(String.valueOf(c.getNumericCellValue()));
                            }
                            break;
                        case FORMULA:
                            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            CellValue cellValue = evaluator.evaluate(c);
                            switch (cellValue.getCellType()) {
                                case STRING:
                                    valueInEachRow.add(String.valueOf(cellValue.getStringValue()));
                                    break;
                                case NUMERIC:
                                    valueInEachRow.add(String.valueOf(cellValue.getNumberValue()));
                                    break;
                                case BOOLEAN:
                                    valueInEachRow.add(String.valueOf(cellValue.getBooleanValue()));
                                    break;
                                case BLANK:
                                    valueInEachRow.add("");
                                    break;
                                case ERROR:
                                default:
                                    System.out.print("Unknown\t");
                            }
                            break;
                        case BOOLEAN:
                            valueInEachRow.add(String.valueOf(c.getBooleanCellValue()));
                            break;
                        case BLANK:
                            valueInEachRow.add("");
                        default:
                            log.error("Invalid type");
                    }
                }
                FileDetail fileDetail = new FileDetail();
                fileDetail.setRowData(String.join(",", valueInEachRow));
                fileDetail.setHeader(r.getRowNum() == 0);
                fileDetail.setFileId(fileId);
                this.insertFileDetail(fileDetail);
            }
        }
    }

    private void insertFileDetail(FileDetail fileDetail) {
        try {
            fileDetailRepository.save(fileDetail);
        } catch (Exception e) {
            // insert to fail table to show which record fail
        }
    }

//    private void handleXlsxFile(MultipartFile file) {
//        try {
//            OPCPackage pkg = OPCPackage.open(file.getInputStream());
//            XSSFReader r = new XSSFReader( pkg );
//            SharedStringsTable sst = r.getSharedStringsTable();
//            XMLReader parser = fetchSheetParser(sst);
//            // To look up the Sheet Name / Sheet Order / rID,
//            //  you need to process the core Workbook stream.
//            // Normally it's of the form rId# or rSheet#
//            InputStream sheet2 = r.getSheet("rId2");
//            InputSource sheetSource = new InputSource(sheet2);
//            parser.parse(sheetSource);
//            sheet2.close();
//        } catch (Exception e){
//
//        }
//    }
//
//    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
//        XMLReader parser = XMLHelper.newXMLReader();
//        ContentHandler handler = new SheetHandler(sst);
//        parser.setContentHandler(handler);
//        return parser;
//    }
//
//    private void handleXlsFile(MultipartFile file) {
//
//    }
//
//    private static class SheetHandler extends DefaultHandler {
//        private SharedStringsTable sst;
//        private String lastContents;
//        private boolean nextIsString;
//        private SheetHandler(SharedStringsTable sst) {
//            this.sst = sst;
//        }
//        public void startElement(String uri, String localName, String name,
//                                 Attributes attributes) throws SAXException {
//            // c => cell
//            if(name.equals("c")) {
//                // Figure out if the value is an index in the SST
//                String cellType = attributes.getValue("t");
//                if(cellType != null && cellType.equals("s")) {
//                    nextIsString = true;
//                } else {
//                    nextIsString = false;
//                }
//            }
//            // Clear contents cache
//            lastContents = "";
//        }
//        public void endElement(String uri, String localName, String name)
//                throws SAXException {
//            // Process the last contents as required.
//            // Do now, as characters() may be called more than once
//            if(nextIsString) {
//                int idx = Integer.parseInt(lastContents);
//                lastContents = sst.getItemAt(idx).getString();
//                nextIsString = false;
//            }
//            // v => contents of a cell
//            // Output after we've seen the string contents
//            if(name.equals("v")) {
//                    System.out.println(lastContents);
//            }
//        }
//        public void characters(char[] ch, int start, int length) {
//            lastContents += new String(ch, start, length);
//        }
//    }
}

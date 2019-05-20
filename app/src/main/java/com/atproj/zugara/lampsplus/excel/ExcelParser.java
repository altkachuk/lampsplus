package com.atproj.zugara.lampsplus.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

public class ExcelParser {

    private ExcelFormat excelFormat;
    private OnExcelParserListener listener;

    public ExcelParser(ExcelFormat excelFormat) {
        this.excelFormat = excelFormat;
    }

    public void setOnExcelParserListener(OnExcelParserListener listener) {
        this.listener = listener;
    }

    public void parse(File dst) {
        try {
            POIFSFileSystem fileSystem = new POIFSFileSystem(dst, true);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);

            JSONArray items = parseExcel(sheet, excelFormat);
            if (listener != null) {
                listener.onComplete(items);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }

    private JSONArray parseExcel(Sheet sheet, ExcelFormat excelFormat) throws JSONException {
        JSONArray result = new JSONArray();

        int rowIndex = 0;
        for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
            Row row = rowIterator.next();

            // first line is description of columns
            if (rowIndex > 0) {
                JSONObject item = parseRow(row, excelFormat);
                result.put(item);
            }

            rowIndex++;
        }

        return result;
    }

    private JSONObject parseRow(Row row, ExcelFormat excelFormat) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < excelFormat.getCols().length; i++) {
            Cell cell = row.getCell(i);
            jsonObject.put(excelFormat.getCols()[i], cell.toString());
        }
        return jsonObject;
    }

    public interface OnExcelParserListener {
        void onComplete(JSONArray items);
        void onError(Exception e);
    }

}

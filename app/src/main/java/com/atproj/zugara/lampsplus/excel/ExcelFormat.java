package com.atproj.zugara.lampsplus.excel;

public class ExcelFormat {

    private String[] cols;

    public ExcelFormat(String... cols) {
        this.cols = cols;
    }

    public String[] getCols() {
        return cols;
    }
}

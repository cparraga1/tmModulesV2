package com.tmModulos.controlador.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ExcelExtract {


    public String getStringCellValue(Row row, int number) {
        Cell cell = row.getCell(number);
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_NUMERIC:
                return ""+cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
        }
        return "";
    }

    public String getStringCellDateValue(Row row, int number) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Cell cell = row.getCell(number);
        Date date = cell.getDateCellValue();
       return df.format(date);

    }

    public int getNumericCellValue(Row row, int number) {
        Cell cell = row.getCell(number);
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_BLANK:
                return 0;
            case Cell.CELL_TYPE_NUMERIC:
                return (int) cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                String valor = cell.getStringCellValue().trim();
                return Integer.parseInt(valor);
        }
        return 0;
    }



    public double getDoubleCellValue(Row row, int number) {
        Cell cell = row.getCell(number);
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_BLANK:
                return 0.0;
            case Cell.CELL_TYPE_NUMERIC:
                return  cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return Double.parseDouble(cell.getStringCellValue());
        }
        return 0.0;
    }

}

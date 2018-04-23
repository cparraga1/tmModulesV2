package com.tmModulos.controlador.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by user on 15/11/2017.
 */
public class ExcelUtilProcessor {

    public static void createCellResultados(Row row, String valor, int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
    }


    public static void createCellDateResultados(Row row, String valor, int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
    }


    public static void createCellNumberResultados(Row row, int valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_NUMERIC);
        resultadoHoraIni.setCellValue(valor);
    }

    public static void createCellNumberResultados(Row row, double valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_NUMERIC);
        resultadoHoraIni.setCellValue(valor);
    }
}

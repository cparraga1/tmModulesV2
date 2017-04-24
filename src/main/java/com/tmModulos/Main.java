package com.tmModulos;

import jxl.read.biff.BiffException;

import java.io.IOException;

public class Main {
	public static void main(String [] args) throws IOException, BiffException {
		 String EXCEL_FILE_LOCATION = "C:\\temp\\test2.xls";
//		Workbook wb = null;
//		try {
//			wb = WorkbookFactory.create(new File(EXCEL_FILE_LOCATION));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InvalidFormatException e) {
//			e.printStackTrace();
//		}
//		Sheet mySheet = wb.getSheetAt(0);
//		Iterator<Row> rowIter = mySheet.rowIterator();
//		System.out.println(mySheet.getRow(1).getCell(0));
//		Workbook workbook = Workbook.getWorkbook(new File(EXCEL_FILE_LOCATION)); //Pasamos el excel que vamos a leer
//		Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
//		String nombre;
//
//		for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
//			for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
//				nombre = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
//				System.out.print(nombre +""); // imprimir nombre
//			}
//			System.out.println("\n");
//		}

	}

}

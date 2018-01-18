package com.tmModulos.controlador.utils;

import com.tmModulos.modelo.entity.tmData.Expediciones;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProcessorUtils {

    public static final String CALCULO_PROMEDIO = "Promedio";
    public static final String CALCULO_MODA = "Moda";
    public static final String CALCULO_MINIMO = "Minimo";
    public static final String CALCULO_MAXIMO = "Maximo";

    public void copyFile(String fileName, InputStream in, String destination) {
        try {

            destination= destination+fileName;
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Date convertirATime(String stringCellValue) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if(!stringCellValue.equals("")){
            try {
                Date date = parser.parse(stringCellValue);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public  String getStringCellValue(Row row, int number) {
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

    public  static double getNumericCellValue(Row row, int number) {
        Cell cell = row.getCell(number);
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_BLANK:
                return 0;
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return Double.parseDouble(cell.getStringCellValue());
        }
        return 0;
    }




    public static String convertLongToTime(long tiempo) {
        return  String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(tiempo),
                TimeUnit.MILLISECONDS.toMinutes(tiempo) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tiempo)),
                TimeUnit.MILLISECONDS.toSeconds(tiempo) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempo)));


    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double transformarAFormatoTiempo(Time time) {
        if(time!=null){
            SimpleDateFormat format = new SimpleDateFormat("mm.ss");
            String date = format.format(new Date(time.getTime()));
            if (date.split("")[0].equals("0")) {
                String [] fecha = date.split("");
                date = fecha[1]+fecha[2]+fecha[3]+fecha[4];
            }
            return Double.parseDouble(date);
        }
        return 0.0;
    }

    public static void postProcessXLS(Object document) {
        HSSFWorkbook book = (HSSFWorkbook) document;
        HSSFSheet sheet = book.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


        HSSFCellStyle decStyle = book.createCellStyle();
        decStyle.setDataFormat((short)2);



        for(int rowInd = 1; rowInd < sheet.getPhysicalNumberOfRows(); rowInd++) {
            HSSFRow row = sheet.getRow(rowInd);
            for(int cellInd = 1; cellInd < header.getPhysicalNumberOfCells(); cellInd++) {
                HSSFCell cell = row.getCell(cellInd);
                String strVal = cell.getStringCellValue();
                String headerName = header.getCell(cellInd).getStringCellValue();
                if(headerName.equals("V-Pico AM") ||headerName.equals("V-Valle") ||headerName.equals("V-Pico PM") ||
                        headerName.equals("I-Inicio") ||headerName.equals("I-Pico AM") ||headerName.equals("I-Valle") ||
                        headerName.equals("I-Pico PM") ||headerName.equals("I-Cierre") ){
                    //Double
                    try {
                        double dblVal = Double.valueOf(strVal);
                        cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
                        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(dblVal);
                        cell.setCellStyle(decStyle);
                    }catch (Exception e){

                    }
                }



            }
        }
    }

    public List<Integer> convertInt(String hora) {
        if(!hora.equals("")){
            List<Integer> valores = new ArrayList<>();
            String[] horaSplit = hora.split(":");
            valores.add(Integer.parseInt(horaSplit[0]));
            valores.add(Integer.parseInt(horaSplit[1]));
            valores.add(Integer.parseInt(horaSplit[2]));
            return valores;
        }
        return null;
    }

    public Integer converInteger(String stringCellValue) {
        try{
            return Integer.parseInt(stringCellValue);
        }catch (Exception e){

        }
        try{
            return (int) Double.parseDouble(stringCellValue);
        }catch (Exception e){

        }
        return 0;
    }
}

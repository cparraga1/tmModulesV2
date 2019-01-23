package com.tmModulos.controlador.utils;

import com.tmModulos.modelo.entity.tmData.Expediciones;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    public void copyFileUTF8(String fileName, InputStream in, String destination) {
        try {

            destination= destination+fileName;
            // write the inputStream to a FileOutputStream
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(destination)));
//            IOUtils.copy(in, out, "UTF-8");
            IOUtils.copy(in, out, "ISO-8859-1");
            IOUtils.closeQuietly(out);

//            StringBuilder inputStringBuilder = new StringBuilder();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"8859_1"));
//            String line = bufferedReader.readLine();
//            while(line != null){
//                inputStringBuilder.append(line);inputStringBuilder.append('\n');
//                out.write(line);
//                line = bufferedReader.readLine();
//            }
////            out.flush();
//            out.close();
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

    public static void crearRowsHeader(HSSFSheet worksheet) {

        worksheet.shiftRows(0, worksheet.getLastRowNum(), 1);
        Row rowInfo1 = worksheet.createRow(0);

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_SERVICIO_BASE,TablaMaestraDEF.COL_SERVICIO_BASE);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_SERVICIO_BASE));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_MATRIZ_DISTANCIA,TablaMaestraDEF.COL_MATRIZ_DISTANCIA);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_MATRIZ_DISTANCIA));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_CAPACIDAD,TablaMaestraDEF.COL_CAPACIDAD);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_CAPACIDAD));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_HORARIO_PROGRAMACION,TablaMaestraDEF.COL_HORARIO_PROGRAMACION);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_HORARIO_PROGRAMACION));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_HORARIO_USUARIO,TablaMaestraDEF.COL_HORARIO_USUARIO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_HORARIO_USUARIO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_CICLO_OPTIMO,TablaMaestraDEF.COL_CICLO_OPTIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_CICLO_OPTIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_CICLO_MINIMO,TablaMaestraDEF.COL_CICLO_MINIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_CICLO_MINIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_CICLO_MAXIMO,TablaMaestraDEF.COL_CICLO_MAXIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_CICLO_MAXIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_INTERVALO_PROMEDIO,TablaMaestraDEF.COL_INTERVALO_PROMEDIO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_INTERVALO_PROMEDIO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_INTERVALO_MINIMO,TablaMaestraDEF.COL_INTERVALO_MINIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_INTERVALO_MINIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_INTERVALO_MAXIMO,TablaMaestraDEF.COL_INTERVALO_MAXIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_INTERVALO_MAXIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_BUSES_HORA_PROMEDIO,TablaMaestraDEF.COL_BUSES_HORA_PROMEDIO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_BUSES_HORA_PROMEDIO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_BUSES_HORA_MINIMO,TablaMaestraDEF.COL_BUSES_HORA_MINIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_BUSES_HORA_MINIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_BUSES_HORA_MAXIMO,TablaMaestraDEF.COL_BUSES_HORA_MAXIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_BUSES_HORA_MAXIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_VELOCIDAD_TEORICA_PROMEDIO,TablaMaestraDEF.COL_VELOCIDAD_TEORICA_PROMEDIO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_VELOCIDAD_TEORICA_PROMEDIO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_VELOCIDAD_TEORICA_MINIMO,TablaMaestraDEF.COL_VELOCIDAD_TEORICA_MINIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_VELOCIDAD_TEORICA_MINIMO));

        createCellResultados(rowInfo1, TablaMaestraDEF.INFO_VELOCIDAD_TEORICA_MAXIMO,TablaMaestraDEF.COL_VELOCIDAD_TEORICA_MAXIMO);
        worksheet.addMergedRegion(CellRangeAddress.valueOf(TablaMaestraDEF.RANGO_VELOCIDAD_TEORICA_MAXIMO));
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

        HSSFCellStyle numStyle = book.createCellStyle();

        crearRowsHeader(sheet);

        for(int rowInd = 1; rowInd < sheet.getPhysicalNumberOfRows(); rowInd++) {
            HSSFRow row = sheet.getRow(rowInd);
            for(int cellInd = 1; cellInd < header.getPhysicalNumberOfCells(); cellInd++) {
                HSSFCell cell = row.getCell(cellInd);
                String strVal = cell.getStringCellValue();
                String headerName = header.getCell(cellInd).getStringCellValue();
                if(headerName.equals("V-05:30-06:30") ||headerName.equals("V-06:30-08:00") ||headerName.equals("V-08:00-09:00") || headerName.equals("V-09:00-16:00") || headerName.equals("V-16:00-17:00") || headerName.equals("V-17:00-18:30") || headerName.equals("V-18:30-19:30") ||headerName.equals("V-19:30-20:30") ||
                        headerName.equals("I-00:00-05:30") ||headerName.equals("I-05:30-06:30") ||headerName.equals("I-06:30-08:00") || headerName.equals("I-08:00-09:00") ||headerName.equals("I-09:00-16:00") || headerName.equals("I-16:00-17:00") ||headerName.equals("I-17:00-18:30") ||headerName.equals("I-18:30-19:30") || headerName.equals("I-19:30-20:30") ||headerName.equals("I-20:30-00:00")){
                    //Double
                    try {
                        double dblVal = Double.valueOf(strVal);
                        cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
                        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(dblVal);
                        cell.setCellStyle(decStyle);
                    }catch (Exception e){

                    }
                }else if(headerName.equals("Línea")){
                    int number = Integer.parseInt(strVal);
                    cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(number);
                }



            }
        }
    }

    public static void createCellResultados(Row row, String valor, int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
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
        return new ArrayList<>();
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

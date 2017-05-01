package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.GisCargaDefinition;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.entity.tmData.Equivalencias;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.GisServicio;
import com.tmModulos.modelo.entity.tmData.TipoDiaDetalle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("VerificacionHorario")
public class VerificacionHorarios {

    private List<LogDatos> logDatos;
    private String destination;

    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private VeriPreHorarios veriPreHorarios;

    public List<LogDatos> compararExpediciones (String fileName, InputStream in, String tipoValidacion, String tipoDia) {
        logDatos = new ArrayList<>();
        destination="C:\\temp\\";
        processorUtils.copyFile(fileName,in,destination);
        destination="C:\\temp\\"+fileName;

        if(tipoValidacion.equals("Pre")){
         //   readExcelAndSaveData(destination);
            compareDataExcel(fileForTipoDia(tipoDia));
        }

        return logDatos;
    }

    private void compareDataExcel(String file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

//            File fileIn = new File(file);
//            File fileout = new File("C:\\temp\\copyRes.xls");
//            copyFile(fileIn,fileout);
//
//
//            FileInputStream fileInputStreamOut = new FileInputStream("C:\\temp\\copyRes.xls");
//            HSSFWorkbook workbookOut = new HSSFWorkbook(fileInputStreamOut);
//            HSSFSheet worksheetOut = workbookOut.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            Row r =rowIterator.next();
            Cell cell = null;
            // Access the cell first to update the value
            cell = r.getCell(2);
            // Get current value and then add 5 to it
            cell.setCellValue(5);
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                if( row.getCell(0) != null ){
                    String id = row.getCell(ComparadorHorarioIndex.iD).getStringCellValue();
                    List<ExpedicionesTemporal> expedicionesTemporals = veriPreHorarios.getExpedicionesTemporalsData(id);
                    if(expedicionesTemporals.size()>0){
                        Date horaInicio = convertirATime(row.getCell(ComparadorHorarioIndex.HORA_INICIO).getStringCellValue());
                        Date horaInicioB = convertirATime(row.getCell(ComparadorHorarioIndex.HORA_INICIO_2).getStringCellValue());
                        Date horaFin = convertirATime(row.getCell(ComparadorHorarioIndex.HORA_FIN).getStringCellValue());
                        Date horaFinB = convertirATime(row.getCell(ComparadorHorarioIndex.HORA_FIN_2).getStringCellValue());
                        int distancia = (int) row.getCell(ComparadorHorarioIndex.DISTANCIA).getNumericCellValue();

                       List< String> validacion = validarHorario(expedicionesTemporals,horaInicio,horaInicioB,
                                horaFin,horaFinB,distancia);

                       Cell resultadoHoraIni= row.createCell(ComparadorHorarioIndex.RES_HORA_INI);
                        resultadoHoraIni.setCellValue(validacion.get(0));
                        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
                        resultadoHoraIni.setCellValue(validacion.get(0));
                        Cell resultadoDistancia= row.createCell(ComparadorHorarioIndex.RES_DISTANCIA);
                        resultadoDistancia.setCellValue(validacion.get(1));
                        resultadoDistancia.setCellType(Cell.CELL_TYPE_STRING);
                        resultadoDistancia.setCellValue(validacion.get(1));

                    }else{
                        Cell resultadoHoraIni= row.createCell(ComparadorHorarioIndex.RES_HORA_INI);
                        resultadoHoraIni.setCellValue("No se encontro el servicio");
                        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
                        resultadoHoraIni.setCellValue("No se encontro el servicio");
                        Cell resultadoDistancia= row.createCell(ComparadorHorarioIndex.RES_DISTANCIA);
                        resultadoHoraIni.setCellValue("No se encontro el servicio");
                        resultadoDistancia.setCellType(Cell.CELL_TYPE_STRING);
                    }
                }

/*COPY temp_expediciones (hora_inicio,punto_inicio,hora_fin,punto_fin,identificador,km)
 FROM 'C:/temp/prueba.csv'  DELIMITER ';' CSV HEADER;*/
            }
            fileInputStream.close();
            FileOutputStream outFile =new FileOutputStream(new File("C:\\temp\\update.xls"));
            workbook.write(outFile);
            outFile.close();
            System.out.println("Fin");
        } catch (FileNotFoundException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        } catch (IOException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static int BUF_SIZE = 1024;

    public static void copyFile(File in, File out) throws Exception {
        FileInputStream fis  = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[BUF_SIZE];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }

    private Date convertirATime(String stringCellValue) {
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

    private List<String> validarHorario(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB, int distancia) {
        List<String> comparaciones = new ArrayList<>();
        String compHoraIni="OK";
        String compHoraFin="OK";
        String compHoraDis="OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        for(ExpedicionesTemporal temporal: expedicionesTemporals){
            Date expInicio = convertirATime(temporal.getHoraInicio());
            Date expFin = convertirATime(temporal.getHoraFin());
            String km = temporal.getKm();

            if(expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0 ){
                if(expInicio.before(horaFin)){
                    compHoraIni = "OK";
                }else if(horaInicioB!=null){
                    if(expInicio.after(horaInicioB) || expInicio.compareTo(horaInicioB)==0){
                        if(expInicio.before(horaFinB)){
                            compHoraIni ="OK";
                        }else{
                            compHoraIni = parser.format(expInicio);
                        }
                    }else{

                        compHoraIni = parser.format(expInicio);
                    }
                }else{
                    compHoraIni = parser.format(expInicio);
                }

            }else if(horaInicioB!=null){
                if(expInicio.after(horaInicioB) || expInicio.compareTo(horaInicioB)==0){
                    if(expInicio.before(horaFinB)){
                        compHoraIni ="OK";
                    }else{
                        compHoraIni = parser.format(expInicio);
                    }
                }else{

                    compHoraIni = parser.format(expInicio);
                }
            }
            else{
                compHoraIni = parser.format(expInicio);
            }


            if(km.equals(distancia+"")){
                compHoraDis = "OK";
            }else{
                compHoraDis = km;
            }
        }

        comparaciones.add(compHoraIni);
        comparaciones.add(compHoraDis);

        return comparaciones;
    }



    private String fileForTipoDia(String tipoDia) {
        if(tipoDia.equals("SABADO")){

        }else if (tipoDia.equals("FESTIVO")){

        }
        return "C:\\temp\\resumenServiciosHabil.xls";
    }

    private void readExcelAndSaveData(String destination){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(destination));
            br.readLine(); // Leer encabezados
            while ((line = br.readLine()) != null){
                String [] valores = line.split(cvsSplitBy);
                if(valores[ExpedicionesIndex.TIPO].equals("Exp") || valores[ExpedicionesIndex.TIPO].equals(" Exp") ){
                    ExpedicionesTemporal temporal = new ExpedicionesTemporal();
                    temporal.setHoraInicio(valores[ExpedicionesIndex.HORA_INICIO].replaceAll("\\s+",""));
                    temporal.setPuntoInicio(Integer.parseInt(valores[ExpedicionesIndex.PUNTO_INICIO].replaceAll("\\s+",""))-1000000);
                    temporal.setHoraFin(valores[ExpedicionesIndex.HORA_FIN].replaceAll("\\s+",""));
                    temporal.setPuntoFin(Integer.parseInt(valores[ExpedicionesIndex.PUNTO_FIN].replaceAll("\\s+",""))-1000000);
                    temporal.setKm(valores[ExpedicionesIndex.KM].replaceAll("\\s+","").replaceAll("\\,",""));
                    temporal.setIdentificador(calcularId(valores));
                    veriPreHorarios.addExpTemporal(temporal);
                }
            }


        } catch (FileNotFoundException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        } catch (IOException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
                }
            }
        }
    }

    private String calcularId(String[] valores) {
        int puntoI = Integer.parseInt(valores[ExpedicionesIndex.PUNTO_INICIO].replaceAll("\\s+",""))-1000000;
        int puntoF = Integer.parseInt(valores[ExpedicionesIndex.PUNTO_FIN].replaceAll("\\s+",""))-1000000;
        String [] lineaData = valores[ExpedicionesIndex.LINEA].replaceAll("\\s+","").split("-");
        int linea = Integer.parseInt(lineaData[0]);
        int sublinea = Integer.parseInt(lineaData[1]);
        List<Equivalencias> equivalencias = veriPreHorarios.getEquivalenciasByData(linea,sublinea,puntoI,puntoF);
        if(equivalencias.size()>0){
            return linea+"-"+sublinea+"-"+equivalencias.get(0).getRuta()+"-"+puntoI;
        }
        return linea+"-"+sublinea+"-"+puntoI;
    }


}

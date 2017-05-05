package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.GisCargaDefinition;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.entity.tmData.*;
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
import java.util.*;

@Service("VerificacionHorario")
public class VerificacionHorarios {

    private List<LogDatos> logDatos;
    private String destination;
    private Map<String,auxEp> mapaDatos;

    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private VeriPreHorarios veriPreHorarios;

    public VerificacionHorarios() {
    }

    public List<LogDatos> compararExpediciones (String fileName, InputStream in, String tipoValidacion, String tipoDia) {
        logDatos = new ArrayList<>();
        destination="C:\\temp\\";
        processorUtils.copyFile(fileName,in,destination);
        destination="C:\\temp\\"+fileName;

        if(tipoValidacion.equals("Pre")){

            veriPreHorarios.addEquivalenciasFromFile(destination);
            compareDataExcel(fileForTipoDia(tipoDia),tipoValidacion);
            veriPreHorarios.deleteEquivalencias();
            File file = new File(destination);
            file.delete();

        } else{
            veriPreHorarios.addTablaHorarioFromFile(destination);
            compareDataExcel(fileForTipoDia(tipoDia),tipoValidacion);
            veriPreHorarios.deleteTablaHorario();

        }

        return logDatos;
    }

    private void compareDataExcel(String file,String tipo) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);


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
                    Date horaInicio = convertirATime(getStringCellValue(row,ComparadorHorarioIndex.HORA_INICIO));
                    Date horaInicioB = convertirATime(getStringCellValue(row,ComparadorHorarioIndex.HORA_INICIO_2));
                    Date horaFin = convertirATime(getStringCellValue(row,ComparadorHorarioIndex.HORA_FIN));
                    Date horaFinB = convertirATime(getStringCellValue(row,ComparadorHorarioIndex.HORA_FIN_2));
                    int distancia = (int) row.getCell(ComparadorHorarioIndex.DISTANCIA).getNumericCellValue();
                    if(tipo.equals("Pre")){
                        String id = row.getCell(ComparadorHorarioIndex.iD_PRE).getStringCellValue();
                        List<ExpedicionesTemporal> expedicionesTemporals = veriPreHorarios.getExpedicionesTemporalsData(id);
                        if(expedicionesTemporals.size()>0){

                            List< String> validacion = validarHorario(expedicionesTemporals,horaInicio,horaInicioB,
                                    horaFin,horaFinB,distancia);

                            createCellResultados(row, validacion.get(0),ComparadorHorarioIndex.RES_HORA_INI);
                            createCellResultados(row, validacion.get(1),ComparadorHorarioIndex.RES_HORA_FIN);
                            createCellResultados(row, validacion.get(2),ComparadorHorarioIndex.RES_HORA_INI_2);
                            createCellResultados(row, validacion.get(3),ComparadorHorarioIndex.RES_HORA_FIN_2);
                            createCellResultados(row, validacion.get(4),ComparadorHorarioIndex.RES_DISTANCIA);

                        }else{
                            String info = "N/A";
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_INI);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_FIN);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_INI_2);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_FIN_2);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_DISTANCIA);
                        }
                    }else{
                        String id = row.getCell(ComparadorHorarioIndex.iD).getStringCellValue();
                        String[] valores = id.split("-");
                        int linea = Integer.parseInt(valores[0]);
                        int sublinea = Integer.parseInt(valores[1]);
                        int ruta = Integer.parseInt(valores[2]);
                        int punto = Integer.parseInt(valores[3]);
                        List<TempHorario> tempHorarios = veriPreHorarios.getTablaHorarioByData(linea,sublinea,ruta,punto);
                        if(tempHorarios.size()>0){

                            List< String> validacion = validarHorarioPost(tempHorarios,horaInicio,horaInicioB,
                                    horaFin,horaFinB,distancia);

                            createCellResultados(row, validacion.get(0),ComparadorHorarioIndex.RES_HORA_INI);
                            createCellResultados(row, validacion.get(1),ComparadorHorarioIndex.RES_HORA_FIN);
                            createCellResultados(row, validacion.get(2),ComparadorHorarioIndex.RES_HORA_INI_2);
                            createCellResultados(row, validacion.get(3),ComparadorHorarioIndex.RES_HORA_FIN_2);
                            createCellResultados(row, validacion.get(4),ComparadorHorarioIndex.RES_DISTANCIA);

                        }else{
                            String info = "N/A";
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_INI);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_FIN);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_INI_2);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_HORA_FIN_2);
                            createCellResultados(row, info,ComparadorHorarioIndex.RES_DISTANCIA);
                        }
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
            e.printStackTrace();
        } catch (IOException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String getStringCellValue(Row row,int number) {
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

    private void createCellResultados(Row row, String valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
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
        String compHoraIni2="OK";
        String compHoraFin="OK";
        String compHoraFin2="OK";
        String compHoraDis="OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        for(ExpedicionesTemporal temporal: expedicionesTemporals){
            Date expInicio = convertirATime(temporal.getHoraInicio());
            Double km = Double.parseDouble(temporal.getKm().replaceAll("\\,","."))*1000;

            if( horaInicioB== null && horaFinB == null){
                if(expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0 ){
                }else{
                    compHoraIni = parser.format(expInicio);
                }

                if(expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0 ){
                }else{
                    compHoraFin = parser.format(expInicio);
                }
            }else{
                if( (expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0)
                        && (expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0)){

                }else{
                    if(expInicio.after(horaInicioB) || expInicio.compareTo(horaInicioB)==0 ){

                    }else{
                        compHoraIni2 = parser.format(expInicio);
                    }

                    if(expInicio.before(horaFinB) || expInicio.compareTo(horaFinB)==0 ){

                    }else{
                        compHoraFin2 = parser.format(expInicio);
                    }
                }

            }


            if(km==(double)distancia){
                compHoraDis = "OK";
            }else{
                compHoraDis = km+"";
            }
        }

        comparaciones.add(compHoraIni);
        comparaciones.add(compHoraFin);
        comparaciones.add(compHoraIni2);
        comparaciones.add(compHoraFin2);
        comparaciones.add(compHoraDis);

        return comparaciones;
    }


    private List<String> validarHorarioPost(List<TempHorario> tempHorarios, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB, int distancia) {
        List<String> comparaciones = new ArrayList<>();
        String compHoraIni="OK";
        String compHoraIni2="OK";
        String compHoraFin="OK";
        String compHoraFin2="OK";
        String compHoraDis="OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        for(TempHorario temporal: tempHorarios){
            Date expInicio = convertirATime(temporal.getInstante());

            if( horaInicioB== null && horaFinB == null){
                if(expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0 ){
                }else{
                    compHoraIni = parser.format(expInicio);
                }

                if(expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0 ){
                }else{
                    compHoraFin = parser.format(expInicio);
                }
            }else{
                if( (expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0)
                        && (expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0)){

                }else{
                    if(expInicio.after(horaInicioB) || expInicio.compareTo(horaInicioB)==0 ){

                    }else{
                        compHoraIni2 = parser.format(expInicio);
                    }

                    if(expInicio.before(horaFinB) || expInicio.compareTo(horaFinB)==0 ){

                    }else{
                        compHoraFin2 = parser.format(expInicio);
                    }
                }

            }
        }

        comparaciones.add(compHoraIni);
        comparaciones.add(compHoraFin);
        comparaciones.add(compHoraIni2);
        comparaciones.add(compHoraFin2);
        comparaciones.add(compHoraDis);

        return comparaciones;
    }


    private String fileForTipoDia(String tipoDia) {
        if(tipoDia.equals("SABADO")){
            return "C:\\temp\\resumenServiciosSabado.xls";
        }else if (tipoDia.equals("FESTIVO")){
            return "C:\\temp\\resumenServiciosFestivo.xls";
        }
        return "C:\\temp\\resumenServiciosHabil.xls";
    }



}

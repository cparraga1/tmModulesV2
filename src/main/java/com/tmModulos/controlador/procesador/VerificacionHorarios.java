package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.ConfVeriHorario;
import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("VerificacionHorario")
public class VerificacionHorarios {

    private List<LogDatos> logDatos;
    private String destination;
    private List<String> serviciosEncontrados;


    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private ConfVeriHorario confVeriHorario;

    @Autowired
    private VeriPreHorarios veriPreHorarios;

    @Autowired
    private IntervalosVerificacionHorarios intervalosVeri;

    private CellStyle cellStyle;
    private CellStyle generalStyle;
    private Font font;
    private Date intervaloMinimo;
    private Date intervaloMaximo;

    public VerificacionHorarios() {}

    public List<LogDatos> compararExpediciones (String fileName, InputStream in, String tipoValidacion, String tipoDia,String min,String max) throws Exception {
        logDatos = new ArrayList<>();
        destination=PathFiles.PATH_FOR_FILES + "\\";
        processorUtils.copyFile(fileName,in,destination);
        destination=PathFiles.PATH_FOR_FILES+"\\"+fileName;
        serviciosEncontrados = new ArrayList<String>();

        getIntervalosComparacion(min, max);

        if(tipoValidacion.equals("Pre")){
            veriPreHorarios.deleteEquivalencias();
            veriPreHorarios.addEquivalenciasFromFile(destination);
            System.out.println("Guarde en Base de Datos");
            compareDataExcel(fileForTipoDia(tipoDia),tipoValidacion);

            veriPreHorarios.deleteEquivalencias();

        } else{
            veriPreHorarios.deleteTablaHorario();
            veriPreHorarios.addTablaHorarioFromFile(destination);
            compareDataExcel(fileForTipoDia(tipoDia),tipoValidacion);
            veriPreHorarios.deleteTablaHorario();

        }

        return logDatos;
    }

    private void validarServiciosEncontrados(String tipoValidacion,HSSFSheet workSheet) {
        if( serviciosEncontrados.size() > 0 ){
            List<String> serviciosNoEncontrados = new ArrayList<>();
            int lastRow = workSheet.getLastRowNum()+2;
            createCellResultados(workSheet.createRow(lastRow ),"Servicios No Encontrados",ComparadorHorarioIndex.iD_PRE);
            lastRow ++ ;
            if(tipoValidacion.equals("Pre")){
                serviciosNoEncontrados = veriPreHorarios.getExpedicionesNoReferenciadas(serviciosEncontrados);
            }else{
                serviciosNoEncontrados = veriPreHorarios.getTempPosNoReferenciadas(serviciosEncontrados);
            }

            for(int i=0; i< serviciosNoEncontrados.size(); i++){
                Row row = workSheet.createRow(lastRow + i);
                createCellResultados(row,serviciosNoEncontrados.get(i),ComparadorHorarioIndex.iD_PRE);
            }
        }
    }

    private void getIntervalosComparacion(String min, String max) throws Exception {
            intervaloMinimo = processorUtils.convertirATime(min);
            intervaloMaximo = processorUtils.convertirATime(max);
            if(intervaloMinimo== null || intervaloMaximo == null){
                throw new Exception("Formato de Tiempo Invalido");
            }



    }

    private void compareDataExcel(String file,String tipo) throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            cellStyle = workbook.createCellStyle();
            generalStyle = workbook.createCellStyle();
            addGeneralStyle(cellStyle);
            addGeneralStyle(generalStyle);
            font = workbook.createFont();


            Iterator<Row> rowIterator = worksheet.iterator();
            Row r =rowIterator.next(); rowIterator.next(); // Skip first two lines

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                if( row.getCell(0) != null ){
                    Date horaInicio = processorUtils.convertirATime(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_INICIO));
                    Date horaInicioB = processorUtils.convertirATime(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_INICIO_2));
                    Date horaFin = processorUtils.convertirATime(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_FIN));
                    Date horaFinB = processorUtils.convertirATime(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_FIN_2));
                    int distancia = (int) row.getCell(ComparadorHorarioIndex.DISTANCIA).getNumericCellValue();
                    intervalosVeri.cargarFranjas();
                    if(tipo.equals("Pre")){
                        verificacionPreHorario(row, horaInicio, horaInicioB, horaFin, horaFinB, distancia);
                    }else{
                        verificacionPostHorario(row, horaInicio, horaInicioB, horaFin, horaFinB, distancia);
                    }

                }

/*COPY temp_expediciones (hora_inicio,punto_inicio,hora_fin,punto_fin,identificador,km)
 FROM 'C:/temp/prueba.csv'  DELIMITER ';' CSV HEADER;*/
            }
            validarServiciosEncontrados(tipo,worksheet);
            fileInputStream.close();
            FileOutputStream outFile =new FileOutputStream(new File(PathFiles.PATH_FOR_FILES+"\\update.xls"));
            workbook.write(outFile);
            outFile.close();
            System.out.println("Fin");
        } catch (FileNotFoundException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            throw new Exception("No existe un archivo de Verificacion para ese Tipo Dia");
        } catch (IOException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void addGeneralStyle(CellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    }

    private void verificacionPostHorario(Row row, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB, int distancia) {
        String id = row.getCell(ComparadorHorarioIndex.iD).getStringCellValue();
        String[] valores = id.split("-");
        int linea = Integer.parseInt(valores[0]);
        int sublinea = Integer.parseInt(valores[1]);
        int ruta = Integer.parseInt(valores[2]);
        int punto = Integer.parseInt(valores[3]);
        List<TempPos> tempHorarios = veriPreHorarios.getTablaHorarioByData(linea,sublinea,ruta,punto);
        if(tempHorarios.size()>0){
            serviciosEncontrados.add(id);
            List< String> validacion = validarHorarioPost(tempHorarios,horaInicio,horaInicioB,
                    horaFin,horaFinB,distancia);

            incluirResultadosValidacionHorario(row, validacion.get(0), validacion.get(1), validacion.get(2), validacion.get(3), "N/A");

            List<String> intervalosExpediciones = intervalosVeri.calcularIntervalosPos(tempHorarios,horaInicio,horaInicioB,
                    horaFin,horaFinB);
            incluirResultadosIntervalos(row, intervalosExpediciones);

        }else{
            registrosNoEncontrados(row,id);
        }
    }

    private void incluirResultadosIntervalos(Row row, List<String> intervalosExpediciones) {
        createCellResultadosIntervalos(row, intervalosExpediciones.get(0), ComparadorHorarioIndex.INT_PROMEDIO_INI);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(1),ComparadorHorarioIndex.INT_MINIMO_INI);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(2),ComparadorHorarioIndex.INT_MAXIMO_INI);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(3),ComparadorHorarioIndex.INT_PROMEDIO_PAM);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(4),ComparadorHorarioIndex.INT_MINIMO_PAM);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(5),ComparadorHorarioIndex.INT_MAXIMO_PAM);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(6),ComparadorHorarioIndex.INT_PROMEDIO_VALLE);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(7),ComparadorHorarioIndex.INT_MINIMO_VALLE);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(8),ComparadorHorarioIndex.INT_MAXIMO_VALLE);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(9),ComparadorHorarioIndex.INT_PROMEDIO_PPM);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(10),ComparadorHorarioIndex.INT_MINIMO_PPM);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(11),ComparadorHorarioIndex.INT_MAXIMO_PPM);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(12),ComparadorHorarioIndex.INT_PROMEDIO_CI);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(13),ComparadorHorarioIndex.INT_MINIMO_CI);
        createCellResultadosIntervalos(row, intervalosExpediciones.get(14),ComparadorHorarioIndex.INT_MAXIMO_CI);
    }

    private void verificacionPreHorario(Row row, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB, int distancia) {
        String id = row.getCell(ComparadorHorarioIndex.iD_PRE).getStringCellValue();
        List<ExpedicionesTemporal> expedicionesTemporals = veriPreHorarios.getExpedicionesTemporalsData(id);
        if(expedicionesTemporals.size()>0){
            serviciosEncontrados.add(expedicionesTemporals.get(0).getIdentificador());
            List< String> validacion = validarHorario(expedicionesTemporals,horaInicio,horaInicioB,
                    horaFin,horaFinB,distancia);

            incluirResultadosValidacionHorario(row, validacion.get(0), validacion.get(1), validacion.get(2), validacion.get(3), validacion.get(4));

            List<String> intervalosExpediciones = intervalosVeri.calcularIntervalos(expedicionesTemporals,horaInicio,horaInicioB,
                    horaFin,horaFinB);
            incluirResultadosIntervalos(row, intervalosExpediciones);

        }else{
            registrosNoEncontrados(row,id);
        }
    }

    private void incluirResultadosValidacionHorario(Row row, String valor, String valor2, String valor3, String valor4, String valor5) {
        createCellResultados(row, valor, ComparadorHorarioIndex.RES_HORA_INI);
        createCellResultados(row, valor2, ComparadorHorarioIndex.RES_HORA_FIN);
        createCellResultados(row, valor3, ComparadorHorarioIndex.RES_HORA_INI_2);
        createCellResultados(row, valor4, ComparadorHorarioIndex.RES_HORA_FIN_2);
        createCellResultados(row, valor5, ComparadorHorarioIndex.RES_DISTANCIA);
    }

    private void registrosNoEncontrados(Row row,String id) {
        String info = "N/A";
        incluirResultadosValidacionHorario(row, info, info, info, info, info);
    }

    private List<String> validarLimites(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB) {
        List<String> limites = new ArrayList<>();
        String compHoraInicio = "OK";
        String compHoraInicioB = "OK";
        String compHoraFin = "OK";
        String compHoraFinB = "OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if(horaInicioB==null && horaFinB==null){
            if(!horaInicio.equals(processorUtils.convertirATime(expedicionesTemporals.get(0).getHoraInicio()))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+expedicionesTemporals.get(0).getHoraInicio();
            if(!horaFin.equals(processorUtils.convertirATime(expedicionesTemporals.get(expedicionesTemporals.size()-1).getHoraInicio()))) compHoraFin = ErrorMessage.ERROR_FIN+""+expedicionesTemporals.get(expedicionesTemporals.size()-1).getHoraInicio();
        }else{
            Date horarioFin = processorUtils.convertirATime(expedicionesTemporals.get(0).getHoraInicio());
            Date horarioInicioB = null;

            for(int i=0;i<expedicionesTemporals.size();i++){
                Date exp = processorUtils.convertirATime(expedicionesTemporals.get(i).getHoraInicio());
                if(exp.after(horaFin)){
                    horarioInicioB = exp;
                    break;
                }else {
                    horarioFin = exp;
                }
            }

            if(!horaInicio.equals(processorUtils.convertirATime(expedicionesTemporals.get(0).getHoraInicio()))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+expedicionesTemporals.get(0).getHoraInicio();
            if(!horaFin.equals(horarioFin)) compHoraFin = ErrorMessage.ERROR_FIN+""+parser.format(horarioFin);
            if(!horaFinB.equals(processorUtils.convertirATime(expedicionesTemporals.get(expedicionesTemporals.size()-1).getHoraInicio()))) compHoraFinB = ErrorMessage.ERROR_FIN+""+expedicionesTemporals.get(expedicionesTemporals.size()-1).getHoraInicio();
            if(horarioInicioB!=null){
                if(!horaInicioB.equals(horarioInicioB)) compHoraInicioB = ErrorMessage.ERROR_INICIO+""+parser.format(horarioInicioB);
            }else {
                compHoraInicioB = ErrorMessage.ERROR_INICIO+""+parser.format(horaInicioB);
            }
        }

        limites.add(compHoraInicio);
        limites.add(compHoraFin);
        limites.add(compHoraInicioB);
        limites.add(compHoraFinB);

        return limites;

    }


    private List<String> validarLimitesPos(List<TempPos> tempPos, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB) {
        List<String> limites = new ArrayList<>();
        String compHoraInicio = "OK";
        String compHoraInicioB = "OK";
        String compHoraFin = "OK";
        String compHoraFinB = "OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if(horaInicioB==null && horaFinB==null){
            if(!horaInicio.equals(processorUtils.convertirATime(tempPos.get(0).getInstante().toString()))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+tempPos.get(0).getInstante().toString();
            if(!horaFin.equals(processorUtils.convertirATime(tempPos.get(tempPos.size()-1).getInstante().toString()))) compHoraFin = ErrorMessage.ERROR_FIN+""+tempPos.get(tempPos.size()-1).getInstante().toString();
        }else{
            Date horarioFin = processorUtils.convertirATime(tempPos.get(0).getInstante().toString());
            Date horarioInicioB = null;

            for(int i=0;i<tempPos.size();i++){
                Date exp = processorUtils.convertirATime(tempPos.get(i).getInstante().toString());
                if(exp.after(horaFin)){
                    horarioInicioB = exp;
                    break;
                }else {
                    horarioFin = exp;
                }
            }

            if(!horaInicio.equals(processorUtils.convertirATime(tempPos.get(0).getInstante().toString()))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+tempPos.get(0).getInstante().toString();
            if(!horaFin.equals(horarioFin)) compHoraFin = ErrorMessage.ERROR_FIN+""+parser.format(horarioFin);
            if(!horaFinB.equals(processorUtils.convertirATime(tempPos.get(tempPos.size()-1).getInstante().toString()))) compHoraFinB = ErrorMessage.ERROR_FIN+""+tempPos.get(tempPos.size()-1).getInstante().toString();
            if(horarioInicioB!=null){
                if(!horaInicioB.equals(horarioInicioB)) compHoraInicioB = ErrorMessage.ERROR_INICIO+""+parser.format(horarioInicioB);
            }else {
                compHoraInicioB = ErrorMessage.ERROR_INICIO+""+parser.format(horaInicioB);
            }
        }

        limites.add(compHoraInicio);
        limites.add(compHoraFin);
        limites.add(compHoraInicioB);
        limites.add(compHoraFinB);

        return limites;

    }



    private void createCellResultados(Row row, String valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
        if(!valor.equals("OK")){
            font.setColor(IndexedColors.RED.getIndex());
            cellStyle.setFont(font);
            resultadoHoraIni.setCellStyle(cellStyle);
        }else{
            resultadoHoraIni.setCellStyle(generalStyle);
        }

    }

    private void createCellResultadosIntervalos(Row row, String valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);

        if(!valor.equals("")){
            Date tiempoIntervalo = processorUtils.convertirATime(valor);
            if(tiempoIntervalo.before(intervaloMinimo) || tiempoIntervalo.after(intervaloMaximo)){
                font.setColor(IndexedColors.RED.getIndex());
                cellStyle.setFont(font);
                resultadoHoraIni.setCellStyle(cellStyle);
            }else{
                resultadoHoraIni.setCellStyle(generalStyle);
            }
        }else{
            resultadoHoraIni.setCellStyle(generalStyle);
        }


    }




    private List<String> validarHorario(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB, int distancia) {
        System.out.println("Entre a validar horario");
        List<String> comparaciones = new ArrayList<>();
        String compHoraIni="OK";
        String compHoraIni2="OK";
        String compHoraFin="OK";
        String compHoraFin2="OK";
        String compHoraDis="OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        for(ExpedicionesTemporal temporal: expedicionesTemporals){
            Date expInicio = processorUtils.convertirATime(temporal.getHoraInicio());
            Double km = Double.parseDouble(temporal.getKm().replaceAll("\\,","."))*1000;
            km = ProcessorUtils.round(km,0);

            if( horaInicioB== null && horaFinB == null){
                if(expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0 ){
                }else{
                    compHoraIni = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                }

                if(expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0 ){
                }else{
                    compHoraFin = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                }
            }else{
                if( (expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0)
                        && (expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0)){

                }else{
                    if(expInicio.after(horaInicioB) || expInicio.compareTo(horaInicioB)==0 ){

                    }else{
                        compHoraIni2 = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                    }

                    if(expInicio.before(horaFinB) || expInicio.compareTo(horaFinB)==0 ){

                    }else{
                        compHoraFin2 = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                    }
                }

            }


            if(km==(double)distancia){
                compHoraDis = "OK";
            }else{
                compHoraDis = km+"";
            }
        }

        List<String> resultadosLimites = validarLimites(expedicionesTemporals,horaInicio,horaInicioB,
                horaFin,horaFinB);

        if(compHoraIni.equals("OK")) compHoraIni = resultadosLimites.get(0);
        if(compHoraFin.equals("OK")) compHoraFin = resultadosLimites.get(1);
        if(compHoraIni2.equals("OK")) compHoraIni2 = resultadosLimites.get(2);
        if(compHoraFin2.equals("OK")) compHoraFin2 = resultadosLimites.get(3);

        comparaciones.add(compHoraIni);
        comparaciones.add(compHoraFin);
        comparaciones.add(compHoraIni2);
        comparaciones.add(compHoraFin2);
        comparaciones.add(compHoraDis);

        return comparaciones;
    }


    private List<String> validarHorarioPost(List<TempPos> tempHorarios, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB, int distancia) {
        List<String> comparaciones = new ArrayList<>();
        String compHoraIni="OK";
        String compHoraIni2="OK";
        String compHoraFin="OK";
        String compHoraFin2="OK";
        String compHoraDis="OK";
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        for(TempPos temporal: tempHorarios){
            Date expInicio = processorUtils.convertirATime(temporal.getInstante().toString());

            if( horaInicioB== null && horaFinB == null){
                if(expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0 ){
                }else{
                    compHoraIni = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                }

                if(expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0 ){
                }else{
                    compHoraFin = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                }
            }else{
                if( (expInicio.after(horaInicio) || expInicio.compareTo(horaInicio)==0)
                        && (expInicio.before(horaFin) || expInicio.compareTo(horaFin)==0)){

                }else{
                    if(expInicio.after(horaInicioB) || expInicio.compareTo(horaInicioB)==0 ){

                    }else{
                        compHoraIni2 = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                    }

                    if(expInicio.before(horaFinB) || expInicio.compareTo(horaFinB)==0 ){

                    }else{
                        compHoraFin2 = ErrorMessage.ERROR_LIMITE+""+parser.format(expInicio);
                    }
                }

            }
        }

        comparaciones.add(compHoraIni);
        comparaciones.add(compHoraFin);
        comparaciones.add(compHoraIni2);
        comparaciones.add(compHoraFin2);
        comparaciones.add(compHoraDis);

        List<String> resultadosLimites = validarLimitesPos(tempHorarios,horaInicio,horaInicioB,
                horaFin,horaFinB);

        if(compHoraIni.equals("OK")) compHoraIni = resultadosLimites.get(0);
        if(compHoraFin.equals("OK")) compHoraFin = resultadosLimites.get(1);
        if(compHoraIni2.equals("OK")) compHoraIni2 = resultadosLimites.get(2);
        if(compHoraFin2.equals("OK")) compHoraFin2 = resultadosLimites.get(3);

        comparaciones.add(compHoraIni);
        comparaciones.add(compHoraFin);
        comparaciones.add(compHoraIni2);
        comparaciones.add(compHoraFin2);
        comparaciones.add(compHoraDis);

        return comparaciones;
    }

    public List<VerificacionTipoDia> getTiposDiasDisponibles () {
        return confVeriHorario.getTipoDiaAll();
    }


    private String fileForTipoDia(String tipoDia) {

        return PathFiles.PATH_FOR_FILES+"\\Migracion\\VerificaHorario\\"+tipoDia+".xls";
    }

    public ConfVeriHorario getConfVeriHorario() {
        return confVeriHorario;
    }

    public void setConfVeriHorario(ConfVeriHorario confVeriHorario) {
        this.confVeriHorario = confVeriHorario;
    }
}

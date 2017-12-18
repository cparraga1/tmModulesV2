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

    @Autowired
    private CargaDatosIntervalosPre intervalosPre;

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
            HashMap<String,List<PreDatos>> expediciones = intervalosPre.getExpediciones(destination);
            System.out.println("Guarde en Base de Datos");
           compareDataExcelHorario (fileForTipoDia(tipoDia),tipoValidacion,expediciones);

        } else{
            HashMap<String,List<PreDatos>> expediciones = intervalosPre.getExpedicionesPos(destination);
            compareDataExcelHorario(fileForTipoDia(tipoDia),tipoValidacion,expediciones);
//            veriPreHorarios.deleteTablaHorario();

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

    private void compareDataExcelHorario(String file,String tipo,HashMap<String,List<PreDatos>> expediciones) throws Exception {
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
                    List<Integer> horaInicio = processorUtils.convertInt(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_INICIO));
                    List<Integer> horaInicioB = processorUtils.convertInt(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_INICIO_2));
                    List<Integer> horaFin = processorUtils.convertInt(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_FIN));
                    List<Integer> horaFinB = processorUtils.convertInt(processorUtils.getStringCellValue(row,ComparadorHorarioIndex.HORA_FIN_2));
                    int distancia = (int) row.getCell(ComparadorHorarioIndex.DISTANCIA).getNumericCellValue();
                    intervalosVeri.cargarFranjas();
                    if(tipo.equals("Pre")){
                        verificacionPreHorario(row, horaInicio, horaInicioB, horaFin, horaFinB, distancia,expediciones);
                    }else{
                        verificacionPostHorario(row, horaInicio, horaInicioB, horaFin, horaFinB, distancia,expediciones);
                    }

                }
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

    private void verificacionPostHorario(Row row, List<Integer> horaInicio, List<Integer> horaInicioB, List<Integer> horaFin, List<Integer> horaFinB,
                                         int distancia,HashMap<String,List<PreDatos>> expediciones) {
        String id = row.getCell(ComparadorHorarioIndex.iD).getStringCellValue();
        String[] valores = id.split("-");
        int linea = Integer.parseInt(valores[0]);
        int sublinea = Integer.parseInt(valores[1]);
        int ruta = Integer.parseInt(valores[2]);
        int punto = Integer.parseInt(valores[3]);
        List<PreDatos> tempHorarios = expediciones.get(id);
        if(tempHorarios!=null){
            serviciosEncontrados.add(id);
            List< String> validacion = validarHorarioPost(tempHorarios,horaInicio,horaInicioB,
                    horaFin,horaFinB,distancia);

            incluirResultadosValidacionHorario(row, validacion.get(0), validacion.get(1), validacion.get(2), validacion.get(3), "N/A");

            List<String> intervalosExpediciones = intervalosVeri.calcularIntervalosPos(tempHorarios);
            incluirResultadosIntervalos(row, intervalosExpediciones);

        }else{
            registrosNoEncontrados(row,id);
        }
    }

        private List<String> validarHorarioPost(List<PreDatos> tempHorarios, List<Integer> horaInicio, List<Integer> horaInicioB,
                                                List<Integer> horaFin, List<Integer> horaFinB, int distancia) {
        List<String> comparaciones = new ArrayList<>();
        comparaciones.add("OK"); //Ini
        comparaciones.add("OK"); //Fin
        comparaciones.add("OK"); //IniB
        comparaciones.add("OK"); //FinB
        comparaciones.add("OK"); //Distancia

        for(PreDatos temporal: tempHorarios){
            comparaciones = validarLimitesHorarios(comparaciones,horaInicio,horaInicioB,horaFin,horaFinB,temporal);
        }

        comparaciones.add(comparaciones.get(0));
        comparaciones.add(comparaciones.get(1));
        comparaciones.add(comparaciones.get(2));
        comparaciones.add(comparaciones.get(3));
        comparaciones.add(comparaciones.get(4));

        List<String> resultadosLimites = validarLimites(tempHorarios,horaInicio,horaInicioB,
                horaFin,horaFinB);

        if(comparaciones.get(0).equals("OK")) comparaciones.set(0, resultadosLimites.get(0));
        if(comparaciones.get(1).equals("OK")) comparaciones.set(1, resultadosLimites.get(1));
        if(comparaciones.get(2).equals("OK")) comparaciones.set(2, resultadosLimites.get(2));
        if(comparaciones.get(3).equals("OK")) comparaciones.set(3, resultadosLimites.get(3));


        return comparaciones;
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

    private void verificacionPreHorario(Row row, List<Integer> horaInicio, List<Integer> horaInicioB, List<Integer> horaFin, List<Integer> horaFinB,
                                        int distancia,HashMap<String,List<PreDatos>> expediciones) {
        String id = " "+row.getCell(ComparadorHorarioIndex.iD_PRE).getStringCellValue();
        List<PreDatos> expedicionesTemporals = expediciones.get(id);
        if(expedicionesTemporals != null ){
            serviciosEncontrados.add(id);
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

    private List<String> validarLimites(List<PreDatos> expedicionesTemporals, List<Integer> horaInicio, List<Integer> horaInicioB, List<Integer> horaFin, List<Integer> horaFinB) {
        List<String> limites = new ArrayList<>();
        String compHoraInicio = "OK";
        String compHoraInicioB = "OK";
        String compHoraFin = "OK";
        String compHoraFinB = "OK";
        if(horaInicioB==null && horaFinB==null){
            if(!horaIgual(horaInicio,expedicionesTemporals.get(0))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+expedicionesTemporals.get(0).toString();
            if(!horaIgual(horaFin,expedicionesTemporals.get(expedicionesTemporals.size()-1))) compHoraFin = ErrorMessage.ERROR_FIN+""+expedicionesTemporals.get(expedicionesTemporals.size()-1).toString();
        }else{
            PreDatos horarioFin = expedicionesTemporals.get(0);
            PreDatos horarioInicioB = null;

            for(int i=1;i<expedicionesTemporals.size();i++){
                PreDatos exp = expedicionesTemporals.get(i);
                if(estaSoloDespuesDeLaHoraInicio(horaFin,exp)){
                    horarioInicioB = exp;
                    break;
                }else {
                    horarioFin = exp;
                }
            }

            if(!horaIgual(horaInicio,expedicionesTemporals.get(0))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+expedicionesTemporals.get(0).toString();
            if(!horaIgual(horaFin,horarioFin)) compHoraFin = ErrorMessage.ERROR_FIN+""+horarioFin.toString();
            if(!horaIgual(horaFinB,expedicionesTemporals.get(expedicionesTemporals.size()-1))) compHoraFinB = ErrorMessage.ERROR_FIN+""+expedicionesTemporals.get(expedicionesTemporals.size()-1).toString();
            if(horarioInicioB!=null){
                if(!horaIgual(horaInicioB,horarioInicioB)) compHoraInicioB = ErrorMessage.ERROR_INICIO+""+horarioInicioB.toString();
            }else {
                compHoraInicioB = ErrorMessage.ERROR_INICIO+""+expedicionesTemporals.get(expedicionesTemporals.size()-1).toString();
            }
        }

        limites.add(compHoraInicio);
        limites.add(compHoraFin);
        limites.add(compHoraInicioB);
        limites.add(compHoraFinB);

        return limites;

    }

    private boolean horaIgual(List<Integer> horaInicio, PreDatos preDatos) {
        if(horaInicio.get(0)== preDatos.getHora() && horaInicio.get(1) == preDatos.getMinutos()
                 && horaInicio.get(2) == preDatos.getSegundos() ) {
            return true;
        }

        return false;
    }


//    private List<String> validarLimitesPos(List<PreDatos> tempPos, Date horaInicio, Date horaInicioB, Date horaFin, Date horaFinB) {
//        List<String> limites = new ArrayList<>();
//        String compHoraInicio = "OK";
//        String compHoraInicioB = "OK";
//        String compHoraFin = "OK";
//        String compHoraFinB = "OK";
//        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
//        if(horaInicioB==null && horaFinB==null){
//            if(!horaInicio.equals(processorUtils.convertirATime(tempPos.get(0).getInstante().toString()))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+tempPos.get(0).getInstante().toString();
//            if(!horaFin.equals(processorUtils.convertirATime(tempPos.get(tempPos.size()-1).getInstante().toString()))) compHoraFin = ErrorMessage.ERROR_FIN+""+tempPos.get(tempPos.size()-1).getInstante().toString();
//        }else{
//            Date horarioFin = processorUtils.convertirATime(tempPos.get(0).getInstante().toString());
//            Date horarioInicioB = null;
//
//            for(int i=0;i<tempPos.size();i++){
//                Date exp = processorUtils.convertirATime(tempPos.get(i).getInstante().toString());
//                if(exp.after(horaFin)){
//                    horarioInicioB = exp;
//                    break;
//                }else {
//                    horarioFin = exp;
//                }
//            }
//
//            if(!horaInicio.equals(processorUtils.convertirATime(tempPos.get(0).getInstante().toString()))) compHoraInicio = ErrorMessage.ERROR_INICIO+""+tempPos.get(0).getInstante().toString();
//            if(!horaFin.equals(horarioFin)) compHoraFin = ErrorMessage.ERROR_FIN+""+parser.format(horarioFin);
//            if(!horaFinB.equals(processorUtils.convertirATime(tempPos.get(tempPos.size()-1).getInstante().toString()))) compHoraFinB = ErrorMessage.ERROR_FIN+""+tempPos.get(tempPos.size()-1).getInstante().toString();
//            if(horarioInicioB!=null){
//                if(!horaInicioB.equals(horarioInicioB)) compHoraInicioB = ErrorMessage.ERROR_INICIO+""+parser.format(horarioInicioB);
//            }else {
//                compHoraInicioB = ErrorMessage.ERROR_INICIO+""+parser.format(horaInicioB);
//            }
//        }
//
//        limites.add(compHoraInicio);
//        limites.add(compHoraFin);
//        limites.add(compHoraInicioB);
//        limites.add(compHoraFinB);
//
//        return limites;
//
//    }



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




    private List<String> validarHorario(List<PreDatos> expedicionesTemporals, List<Integer> horaInicio, List<Integer> horaInicioB, List<Integer> horaFin, List<Integer> horaFinB, int distancia) {
        System.out.println("Entre a validar horario");
        List<String> comparaciones = new ArrayList<>();
        comparaciones.add("OK"); //Ini
        comparaciones.add("OK"); //Fin
        comparaciones.add("OK"); //IniB
        comparaciones.add("OK"); //FinB
        comparaciones.add("OK"); //Distancia;
        for(PreDatos datos: expedicionesTemporals){
            Double km = datos.getDistancia()*1000;
            km = ProcessorUtils.round(km,0);
            comparaciones = validarLimitesHorarios(comparaciones,horaInicio,horaInicioB,horaFin,horaFinB,datos);


            if(km==(double)distancia){
                comparaciones.set(4,"OK");
            }else{
                comparaciones.set(4, km+"");
            }
        }

        comparaciones.add(comparaciones.get(0));
        comparaciones.add(comparaciones.get(1));
        comparaciones.add(comparaciones.get(2));
        comparaciones.add(comparaciones.get(3));
        comparaciones.add(comparaciones.get(4));

        List<String> resultadosLimites = validarLimites(expedicionesTemporals,horaInicio,horaInicioB,
                horaFin,horaFinB);

        if(comparaciones.get(0).equals("OK")) comparaciones.set(0, resultadosLimites.get(0));
        if(comparaciones.get(1).equals("OK")) comparaciones.set(1, resultadosLimites.get(1));
        if(comparaciones.get(2).equals("OK")) comparaciones.set(2, resultadosLimites.get(2));
        if(comparaciones.get(3).equals("OK")) comparaciones.set(3, resultadosLimites.get(3));

        return comparaciones;
    }


    public List<String> validarLimitesHorarios( List<String> comparaciones, List<Integer> horaInicio, List<Integer> horaInicioB, List<Integer> horaFin, List<Integer> horaFinB,PreDatos expInicio){
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if( horaInicioB== null && horaFinB == null){
            if(estaDespuesDeLaHoraInicio(horaInicio,expInicio)){
            }else{
                comparaciones.set(0, ErrorMessage.ERROR_LIMITE+""+expInicio.toString());
            }

            if(estaAntesDelaHoraFin(horaFin,expInicio)){
            }else{
                comparaciones.set(1, ErrorMessage.ERROR_LIMITE+""+expInicio.toString());
            }
        }else{
            if( estaDespuesDeLaHoraInicio(horaInicio,expInicio)
                    && estaAntesDelaHoraFin(horaFin,expInicio)){
                //Esta en el primer Horario
            }else if(estaDespuesDeLaHoraInicio(horaInicioB,expInicio)
                    && estaAntesDelaHoraFin(horaFinB,expInicio)){
                //Esta En el segundo Horario
            }else{
                List<Integer> horaFinExtendida = calcularExtensionHora(horaFin,1);
                List<Integer> horaInicioExtendida = calcularExtensionHora(horaInicio,-1);
                if( estaDespuesDeLaHoraInicio(horaInicioExtendida,expInicio)
                        && estaAntesDelaHoraFin(horaFinExtendida,expInicio)){
                    // Mas cercano al Primer Horario
                    if( estaDespuesDeLaHoraInicio(horaInicioExtendida,expInicio)
                            && estaAntesDelaHoraFin(horaInicio,expInicio)){
                        comparaciones.set(0, ErrorMessage.ERROR_LIMITE+""+expInicio.toString());
                    }else{
                        comparaciones.set(1, ErrorMessage.ERROR_LIMITE+""+expInicio.toString());
                    }

                } else{
                    if(estaDespuesDeLaHoraInicio(horaInicioB,expInicio)){

                    }else{
                        comparaciones.set(2, ErrorMessage.ERROR_LIMITE+""+expInicio.toString());
                    }

                    if(estaAntesDelaHoraFin(horaFinB,expInicio)){

                    }else{
                        comparaciones.set(3, ErrorMessage.ERROR_LIMITE+""+expInicio.toString());
                    }
                }

            }

        }
        return comparaciones;
    }

    private boolean estaAntesDelaHoraFin(List<Integer> horaFin, PreDatos expInicio) {
        int hora = horaFin.get(0);
        int minutos = horaFin.get(1);
        int segundos = horaFin.get(2);
        if( hora == expInicio.getHora() ){
            if( minutos == expInicio.getMinutos() ){
                if( segundos == expInicio.getSegundos() || segundos > expInicio.getSegundos() ){
                    return true;
                }
            }else if( minutos > expInicio.getMinutos() ){
                return true;
            }
        }else if (hora > expInicio.getHora() ){
            return true;
        }
        return false;
    }



    private boolean estaSoloDespuesDeLaHoraInicio(List<Integer> horaInicio, PreDatos expInicio) {
        int hora = horaInicio.get(0);
        int minutos = horaInicio.get(1);
        int segundos = horaInicio.get(2);
        if( hora == expInicio.getHora() ){
            if( minutos == expInicio.getMinutos() ){
                if( segundos < expInicio.getSegundos() ){
                    return true;
                }
            }else if(minutos < expInicio.getMinutos() ){
                return true;
            }
        }else if (hora< expInicio.getHora() ){
            return true;
        }
        return false;
    }


    private boolean estaDespuesDeLaHoraInicio(List<Integer> horaInicio, PreDatos expInicio) {
        int hora = horaInicio.get(0);
        int minutos = horaInicio.get(1);
        int segundos = horaInicio.get(2);
        if( hora == expInicio.getHora() ){
            if( minutos == expInicio.getMinutos() ){
                if( segundos == expInicio.getSegundos() || segundos < expInicio.getSegundos() ){
                    return true;
                }
            }else if(minutos < expInicio.getMinutos() ){
                return true;
            }
        }else if (hora< expInicio.getHora() ){
            return true;
        }
        return false;
    }

    private List<Integer> calcularExtensionHora(List<Integer> horaFin, int minutos) {
        List<Integer> horaNueva = new ArrayList<>();
        horaNueva.add(horaFin.get(0)+minutos);
        horaNueva.add(horaFin.get(0)+minutos);
        horaNueva.add(horaFin.get(0)+minutos);
       return horaNueva;
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

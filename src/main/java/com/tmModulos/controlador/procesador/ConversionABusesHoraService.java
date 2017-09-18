package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.ConversorTablaHorario;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service("ConversionBusesHora")
public class ConversionABusesHoraService {


    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private ConversorTablaHorario conversorTablaHorario;

    @Autowired
    private NodoService nodoService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private IntervalosProcessor intervalosProcessor;

    private List<LogDatos> logDatos;
    private String destination;

    public ConversionABusesHoraService() {
    }


    public List<TipoDia> getTiposDiasDisponibles() {
        return conversorTablaHorario.getTipoDiaAll();
    }

    public void convertirABusesHora(String fileName, InputStream inputstream, String tipoDia) throws Exception {
        logDatos = new ArrayList<>();
        destination= PathFiles.PATH_FOR_FILES + "\\";
        processorUtils.copyFile(fileName,inputstream,destination);
        destination=PathFiles.PATH_FOR_FILES+"\\"+fileName;

        conversorTablaHorario.deleteTablaHorario();
        conversorTablaHorario.addTablaHorarioFromFile(destination);
           //compareDataExcel(fileForTipoDia(tipoDia),tipoValidacion);
            createExcelBusesHora(tipoDia);
        conversorTablaHorario.deleteTablaHorario();

    }

    private void createExcelBusesHora(String tipoDia) throws Exception {
        try {
            File file = new File(PathFiles.PATH_FOR_FILES+"\\Migracion\\"+PathFiles.BUSES_HORA_FILE);
            file.createNewFile();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("Buses Hora");
//            cellStyle = workbook.createCellStyle();
//            generalStyle = workbook.createCellStyle();
//            addGeneralStyle(cellStyle);
//            addGeneralStyle(generalStyle);
//            font = workbook.createFont();
            crearRowsIniciales(worksheet);
            crearRowsFranjaHorario(worksheet,workbook);
            crearRowsContenido(worksheet,workbook,tipoDia);

            FileOutputStream outFile =new FileOutputStream(PathFiles.PATH_FOR_FILES+"\\Migracion\\"+PathFiles.BUSES_HORA_FILE);
            workbook.write(outFile);
            outFile.close();
            System.out.println("Fin");
        } catch (FileNotFoundException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            e.fillInStackTrace();
            throw new Exception("No existe un archivo de Verificacion para ese Tipo Dia");
        } catch (IOException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void crearRowsContenido(HSSFSheet worksheet, HSSFWorkbook workbook, String tipoDia) {
        TipoDia dia = conversorTablaHorario.getTipoDia(tipoDia);
        List<ServicioTipoDia> servicioTipoDiaList = servicioService.getServiciosByTipoDia(dia);
        int cellActual = 2;
        for (int x = 0; x<servicioTipoDiaList.size();x++){
            ServicioTipoDia servicioTipoDia = servicioTipoDiaList.get(x);
            Map<IntervalosProgramacion, Double> cuartos = intervalosProcessor.convertirTablaHorarioABusesHora(servicioTipoDia);
            crearRowsServicio(servicioTipoDia.getServicio(),workbook,worksheet,cellActual);
            crearRowsIntervalos(cuartos,workbook,worksheet,cellActual);
            cellActual++;
        }

    }

    private void crearRowsIntervalos(Map<IntervalosProgramacion, Double> cuartos, HSSFWorkbook workbook, HSSFSheet worksheet, int cell) {

        Iterator it = cuartos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            IntervalosProgramacion intervalo = (IntervalosProgramacion) pair.getKey();
            double valorIntervalo = (double) pair.getValue();
            createCellNumberResultados(worksheet.getRow(intervalo.getOrden()),valorIntervalo,cell);
        }
    }

    private void crearRowsServicio(Servicio servicio, HSSFWorkbook workbook, HSSFSheet worksheet, int cell) {
        Nodo nodoInicio = obtenerNodoInicio(servicio.getPunto());
        Nodo nodoFin = obtenerNodoInicio(servicio.getPuntoFin());
        String nombreZona = obtenerNombreZona(nodoInicio);
        createCellResultados(worksheet.getRow(BusesHoraDef.SERVICIO),servicio.getNombreEspecial(),cell);
        createCellResultados(worksheet.getRow(BusesHoraDef.PUNTO_INICIO),obtenerNombreNodo(nodoInicio),cell);
        createCellResultados(worksheet.getRow(BusesHoraDef.PUNTO_FIN),obtenerNombreNodo(nodoFin),cell);
        createCellResultados(worksheet.getRow(BusesHoraDef.ZONA),nombreZona,cell);
    }

    private String obtenerNombreZona(Nodo nodoInicio) {
        if(nodoInicio==null){
            return "N/A";
        }
        return nodoInicio.getVagon().getEstacion().getZonaProgramacion().getNombre();
    }

    private String obtenerNombreNodo(Nodo nodoInicio) {
        if(nodoInicio==null){
            return "N/A";
        }
        return nodoInicio.getNombre();
    }

    private Nodo obtenerNodoInicio(int punto) {
        return nodoService.getNodoByCodigo(""+punto);
    }

    private void crearRowsFranjaHorario(HSSFSheet worksheet,HSSFWorkbook workbook) {
        intervalosProcessor.precalcularIntervalosProgramacion();
        crearRowsPorIntervalosCuartos(intervalosProcessor.getIntervalosFranjaInicio(), FranjaDef.INICIO,worksheet,workbook.createCellStyle());
        crearRowsPorIntervalosCuartos(intervalosProcessor.getIntervalosFranjaPicoAM(), FranjaDef.PICO_AM,worksheet,workbook.createCellStyle());
        crearRowsPorIntervalosCuartos(intervalosProcessor.getIntervalosFranjaValle(), FranjaDef.VALLE,worksheet,workbook.createCellStyle());
        crearRowsPorIntervalosCuartos(intervalosProcessor.getIntervalosFranjaPicoPM(), FranjaDef.PICO_PM,worksheet,workbook.createCellStyle());
        crearRowsPorIntervalosCuartos(intervalosProcessor.getIntervalosFranjaCierre(), FranjaDef.CIERRE,worksheet,workbook.createCellStyle());

    }

    private void crearRowsPorIntervalosCuartos(List<IntervalosProgramacion> intervalosFranja, String nombreFranja,HSSFSheet worksheet,CellStyle cellStyle) {
        int cellActual = 0;
        cellStyle = generarEstiloPorFranja(nombreFranja,cellStyle);
        for(IntervalosProgramacion intervalos: intervalosFranja){
            Row row = worksheet.createRow(intervalos.getOrden());
            createCellResultados(row,intervalos.getInicio().toString(),cellActual,cellStyle);
            createCellResultados(row,intervalos.getFin().toString(),cellActual+1,cellStyle);
        }
    }

    private CellStyle generarEstiloPorFranja(String nombreFranja, CellStyle cellStyle) {
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        if(nombreFranja.equals(FranjaDef.INICIO) || nombreFranja.equals(FranjaDef.VALLE) || nombreFranja.equals(FranjaDef.CIERRE)){
            cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        }else{
            cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        }
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }


    private void crearRowsIniciales(HSSFSheet worksheet) {
        createCellResultados(worksheet.createRow(BusesHoraDef.TITULO),"BUSES HORA",BusesHoraDef.TITULO);
        createCellResultados(worksheet.createRow(BusesHoraDef.SERVICIO),"SERVICIO",0);
        createCellResultados(worksheet.createRow(BusesHoraDef.PUNTO_INICIO),"Punto Inicio",0);
        createCellResultados(worksheet.createRow(BusesHoraDef.PUNTO_FIN),"Punto Final",0);
        createCellResultados(worksheet.createRow(BusesHoraDef.ZONA),"ZONA",0);
    }

    private void createCellResultados(Row row, String valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
    }

    private void createCellNumberResultados(Row row, double valor,int num) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_NUMERIC);
        resultadoHoraIni.setCellValue(valor);
    }


    private void createCellResultados(Row row, String valor,int num,CellStyle cellStyle) {
        Cell resultadoHoraIni= row.createCell(num);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellType(Cell.CELL_TYPE_STRING);
        resultadoHoraIni.setCellValue(valor);
        resultadoHoraIni.setCellStyle(cellStyle);
    }

    public ProcessorUtils getProcessorUtils() {
        return processorUtils;
    }

    public void setProcessorUtils(ProcessorUtils processorUtils) {
        this.processorUtils = processorUtils;
    }

    public ConversorTablaHorario getConversorTablaHorario() {
        return conversorTablaHorario;
    }

    public void setConversorTablaHorario(ConversorTablaHorario conversorTablaHorario) {
        this.conversorTablaHorario = conversorTablaHorario;
    }

    public IntervalosProcessor getIntervalosProcessor() {
        return intervalosProcessor;
    }

    public void setIntervalosProcessor(IntervalosProcessor intervalosProcessor) {
        this.intervalosProcessor = intervalosProcessor;
    }

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }
}

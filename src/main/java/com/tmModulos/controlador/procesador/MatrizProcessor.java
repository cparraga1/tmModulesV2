package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.DistanciaNodosService;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.utils.*;
import com.tmModulos.modelo.dao.saeBogota.GroupedHorario;
import com.tmModulos.modelo.entity.tmData.*;
import com.tmModulos.modelo.entity.saeBogota.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("MatrizProcessor")
public class MatrizProcessor {

    @Autowired
    private MatrizDistanciaService matrizDistanciaService;

    @Autowired
    private DistanciaNodosService distanciaNodosService;

    @Autowired
    private NodoService nodoService;

    @Autowired
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ExcelExtract excelExtract;

    private List<LogDatos> logDatos;
    private static Logger log = Logger.getLogger(MatrizProcessor.class);
    private String destination = PathFiles.PATH_FOR_FILES;
    private boolean exitoso;


    private List<GroupedHorario> macroLineaEnHorario(int macro, int linea, List<GroupedHorario> horarioByTipoDia) {
        List<GroupedHorario> horarios = new ArrayList<>();
        for(GroupedHorario horario:horarioByTipoDia){
            if(macro == horario.getMacro() && linea == horario.getLinea() ){
                horarios.add(horario);
            }
        }
        return horarios;
    }

    private ServicioDistancia crearOBuscarServicioDistancia(int macro, int linea, int seccion, String nombreMatriz,int nodoCodigo,Row row) {
        String identificador = macro+"-"+linea+"-"+seccion+"-"+nodoCodigo;
        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        if(servicioDistancia==null){
//            String nombreLinea = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.NOMBRE_LINEA);
//            int config = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.CONFIG);
//            int idSentido = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.ID_SENTIDO);
//            String sentido = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.SENTIDO);
//            String etiquetaLinea = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.ETIQUETA_LINEA);
//            String tipoServicio = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.TIPO_SERVICIO);
            servicioDistancia = new ServicioDistancia(nombreMatriz,macro,linea,seccion);
            servicioDistancia.setIdentificador(identificador);
            matrizDistanciaService.addServicioDistancia(servicioDistancia);
        }
        return servicioDistancia;
    }


    public List<LogDatos> processDataFromFile(String fileName, InputStream in, Date fechaProgramacion,String numeracion, Date fechaHabil, Date fechaSabado, Date fechaFestivo,String desc,String modo){
        long tiempoIncial = System.currentTimeMillis();
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Matriz Distancias con Archivo>>", TipoLog.INFO));
        log.info("<<Inicio Calculo Matriz Distancias con Archivo>>");
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fechaHabil,numeracion, fechaSabado,fechaFestivo,desc,modo);
        if(matrizDistancia!=null){
            try {
                readExcelAndSaveData(destination,matrizDistancia);
                exitoso =true;
            } catch (IOException e) {
                log.error( e.getMessage());
                logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
                exitoso =false;
            }
        }else{
            logDatos.add(new LogDatos("Ya existe una Matriz de Distancia con ese identificador", TipoLog.ERROR));
            exitoso =false;
        }

        logDatos.add(new LogDatos("<<Fin Calculo Matriz Distancias con Archivo>>", TipoLog.INFO));
        log.info("<<Fin Calculo Matriz Distancias con Archivo>>");
        tiempoIncial = System.currentTimeMillis() - tiempoIncial;
        log.info("Tiempo de procesamiento: "+ProcessorUtils.convertLongToTime(tiempoIncial));
        return logDatos;
    }

    public void readExcelAndSaveData(String destination, MatrizDistancia matrizDistancia)throws IOException {
        try {

            FileInputStream fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if( row.getCell(0) != null ){
                    int linea = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.LINEA);
                    int sublinea = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.SUBLINEA);

                    //Verificacion de la ruta para formato Antiguo y Nuevo

                    String[] rutaText= excelExtract.getStringCellValue(row, MatrizDistanciaDefinicion.RUTA).split("] ");

                    int ruta = 0;
                    String nombreRuta = "";
                    int numNodo = MatrizDistanciaDefinicion.NODO;
                    int numNomNodo = MatrizDistanciaDefinicion.NOMBRE_NODO;
                    int numPosicion = MatrizDistanciaDefinicion.POSICION;

                    if(rutaText.length > 1){
                        ruta = Integer.parseInt(rutaText[0].substring(1, rutaText[0].length()));
                        nombreRuta = rutaText[1];
                        numNodo = 3;
                        numNomNodo = 4;
                        numPosicion = 5;
                    } else {
                        ruta = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.RUTA);
                        nombreRuta = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.NOMBRE_RUTA);
                    }

                    int codigoNodo = excelExtract.getNumericCellValue(row, numNodo);
                    String nodoNombre= row.getCell(numNomNodo).getStringCellValue();
                    Double posX = excelExtract.getDoubleCellValue(row,numPosicion);

                    //int ruta = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.RUTA);
                  //  String operador = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.OPERADOR);
                    // String idParada = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.ID_PARADA);
//                    String labelNodo = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.LABEL);
//                    String atributos = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.ATRIBUTOS);
//                    Double posY = excelExtract.getDoubleCellValue(row,MatrizDistanciaDefinicion.POS_Y);
                    //String nombreRuta = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.NOMBRE_RUTA);
                    ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(linea,sublinea,ruta,nombreRuta,codigoNodo,row);
                    guardarDistanciaNodos(matrizDistancia, excelExtract.getNumericCellValue(row,numPosicion), servicioDistancia,nodoNombre,codigoNodo+"",posX);
                    log.info(linea + "-" + sublinea + "-" + ruta + "-" + nombreRuta + "-" + nodoNombre + "["+ row.getRowNum() + "]");
                }else{
                    break;
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
           log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        } catch (IOException e) {
            log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        }
    }

    private int convertirAInt(Row row,int numberCell) {
        Cell cell = row.getCell(numberCell);
        if(cell.getCellType()==0){
            return (int) cell.getNumericCellValue();
        }

        return Integer.parseInt(cell.getStringCellValue());
    }

    public List<Vigencias> encontrarVigencias(Date fecha){
        List<Vigencias> vigenciasDaoByDate = distanciaNodosService.getVigenciasDaoByDate(fecha);
        return vigenciasDaoByDate;
    }

    public List<Lineas> encontrarLineas(int macro,int linea){
        return distanciaNodosService.getLineasByMacroAndLinea( macro,linea );
    }

    public List<NodosSeccion> encontrarNodosSeccion(int macro,int linea, int seccion,int config,int tipoNodo){
        return distanciaNodosService.getNodosSeccionesByMacroLineaAndConfig(macro,linea, seccion,config,tipoNodo);
    }

    public String encontrarNombreMatriz(int macro,int linea,int config,int seccion){
        List<Secciones> seccionesByMacroLineaAndConfig = distanciaNodosService.getSeccionesByMacroLineaAndConfig(macro, linea, config, seccion);
        if(seccionesByMacroLineaAndConfig.size()>0){
            return seccionesByMacroLineaAndConfig.get(0).getNombre();
        }
        return "Sin nombre";
    }




    private MatrizDistancia guardarMatrizDistancia(Date fecha,String numeracion,Date fechaFestivos, Date fechaSabado, String desc,String modo){
        if(!existeMatrizDistancia(modo,numeracion)){
            MatrizDistancia matrizDistancia= new MatrizDistancia(new Date(),fecha,fechaSabado,fechaFestivos,numeracion,desc,modo);
            matrizDistanciaService.addMatrizDistancia(matrizDistancia);
            return matrizDistancia;
        }
       return null;
    }

    private boolean existeMatrizDistancia(String modo, String numeracion) {
        return matrizDistanciaService.existeMatrizDistancia(modo,numeracion);
    }

    private DistanciaNodos guardarDistanciaNodos(MatrizDistancia matrizDistancia, int distancia,ServicioDistancia servicioDistancia,String nodoNombre,
                                                 String nodoCodigo,
                                                 Double posX){
        DistanciaNodos distanciaNodosByServicioAndPunto = matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia, matrizDistancia,nodoCodigo);
        if(distanciaNodosByServicioAndPunto==null){
            DistanciaNodos distanciaNodos=new DistanciaNodos(distancia,matrizDistancia,servicioDistancia);
            distanciaNodos.setNodoNombre(nodoNombre);
            distanciaNodos.setNodoCodigo(nodoCodigo);
//            distanciaNodos.setOperador(operador);
//            distanciaNodos.setIdParada(idParada);
//            distanciaNodos.setLabelNodo(label);
//            distanciaNodos.setAtributos(atributos);
            distanciaNodos.setPosX(posX);
//            distanciaNodos.setPosY(posY);
            matrizDistanciaService.addDistanciaNodos(distanciaNodos);
            return distanciaNodos;
        }

        return distanciaNodosByServicioAndPunto;
    }

    public Nodos encontrarNodo(int id,int tipo){
        Nodos nodosByTipoandCode = distanciaNodosService.getNodosByTipoandCode(id, tipo);
        return nodosByTipoandCode;
    }

    public MatrizDistanciaService getMatrizDistanciaService() {
        return matrizDistanciaService;
    }

    public void setMatrizDistanciaService(MatrizDistanciaService matrizDistanciaService) {
        this.matrizDistanciaService = matrizDistanciaService;
    }

    public DistanciaNodosService getDistanciaNodosService() {
        return distanciaNodosService;
    }

    public void setDistanciaNodosService(DistanciaNodosService distanciaNodosService) {
        this.distanciaNodosService = distanciaNodosService;
    }

    public NodoService getNodoService() {
        return nodoService;
    }

    public void setNodoService(NodoService nodoService) {
        this.nodoService = nodoService;
    }

    public ProcessorUtils getProcessorUtils() {
        return processorUtils;
    }

    public void setProcessorUtils(ProcessorUtils processorUtils) {
        this.processorUtils = processorUtils;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
}

package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.DistanciaNodosService;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.MatrizDistanciaDefinicion;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.dao.saeBogota.GroupedHorario;
import com.tmModulos.modelo.dao.saeBogota.NodosDao;
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
import java.util.concurrent.TimeUnit;

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

    private List<LogDatos> logDatos;
    private static Logger log = Logger.getLogger(MatrizProcessor.class);
    private String destination="C:\\temp\\";


    public List<LogDatos> calcularMatrizDistancia(Date fechaHabil,String numeracion,Date fechaFestivos, Date fechaSabado,String desc){
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Matriz Distancias>>", TipoLog.INFO));
        log.info("<<Inicio Calculo Matriz Distancias>>");
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fechaHabil,numeracion,fechaFestivos,fechaSabado,desc);
        //List<DistanciaNodos> distanciaNodos= calcularMatrizPorFecha(fechaHabil, matrizDistancia);
         NodosHilo nodoHiloHabil = new NodosHilo(fechaHabil,matrizDistancia);
        taskExecutor.execute(nodoHiloHabil);

      //  List<DistanciaNodos> distanciaNodosSabado= calcularMatrizPorFecha(fechaSabado, matrizDistancia);
        taskExecutor.execute(new NodosHilo(fechaSabado,matrizDistancia));
       // List<DistanciaNodos> distanciaNodosFestivo= calcularMatrizPorFecha(fechaFestivos, matrizDistancia);
       taskExecutor.execute(new NodosHilo(fechaFestivos,matrizDistancia));
       while(taskExecutor.getActiveCount()>0){

       }
        
        logDatos.add(new LogDatos("<<Fin Calculo Matriz Distancias>>", TipoLog.INFO));
        log.info("<<Fin Calculo Matriz Distancias>>");
        return logDatos;
    }


    private List<GroupedHorario> macroLineaEnHorario(int macro, int linea, List<GroupedHorario> horarioByTipoDia) {
        List<GroupedHorario> horarios = new ArrayList<>();
        for(GroupedHorario horario:horarioByTipoDia){
            if(macro == horario.getMacro() && linea == horario.getLinea() ){
                horarios.add(horario);
            }
        }
        return horarios;
    }

    private ServicioDistancia crearOBuscarServicioDistancia(int macro, int linea, int seccion, String nombreMatriz,String nodoCodigo) {
        String identificador = macro+"-"+linea+"-"+seccion+"-"+nodoCodigo;
        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        if(servicioDistancia==null){
            servicioDistancia = new ServicioDistancia(nombreMatriz,macro,linea,seccion);
            servicioDistancia.setIdentificador(identificador);
            matrizDistanciaService.addServicioDistancia(servicioDistancia);
        }
        return servicioDistancia;
    }


    public List<LogDatos> processDataFromFile(String fileName, InputStream in, Date fechaProgramacion,String numeracion, Date fechaHabil, Date fechaSabado, Date fechaFestivo,String desc){
       logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Matriz Distancias con Archivo>>", TipoLog.INFO));
        log.info("<<Inicio Calculo Matriz Distancias con Archivo>>");
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fechaHabil,numeracion, fechaSabado,fechaFestivo,desc);
        try {
            readExcelAndSaveData(destination,matrizDistancia);

        } catch (IOException e) {
           log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        }
        logDatos.add(new LogDatos("<<Fin Calculo Matriz Distancias con Archivo>>", TipoLog.INFO));
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
                    int codigoNodo=convertirAInt(row, MatrizDistanciaDefinicion.NODO_CODIGO);


                    String nodoNombre= row.getCell(MatrizDistanciaDefinicion.NOMBRE_NODO).getStringCellValue();
                    String nodoCodigo;

                    if(row.getCell(MatrizDistanciaDefinicion.NODO_CODIGO).getCellType()==0 ){
                        nodoCodigo= (int) row.getCell(MatrizDistanciaDefinicion.NODO_CODIGO).getNumericCellValue()+"";
                    }else{
                        nodoCodigo= row.getCell(MatrizDistanciaDefinicion.NODO_CODIGO).getStringCellValue();
                    }

                    ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(convertirAInt(row,MatrizDistanciaDefinicion.MACRO)
                            , convertirAInt(row,MatrizDistanciaDefinicion.LINEA)
                            , convertirAInt(row,MatrizDistanciaDefinicion.SECCION)
                            ,row.getCell(MatrizDistanciaDefinicion.RUTA).getStringCellValue(),
                            nodoCodigo);
                    guardarDistanciaNodos(matrizDistancia,
                            convertirAInt(row,MatrizDistanciaDefinicion.ABSICSA),
                            servicioDistancia,nodoNombre,nodoCodigo);
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


    private Nodo findOrSaveNodo(int nodoCodigo,String nodoNombre) {
       Nodo nodos = nodoService.getNodo( nodoNombre );
        if( nodos != null){
            Zona zona = nodoService.getZonaByName("SIN ASIGNAR","P");
            Nodo nodo = new Nodo(nodoNombre,nodoCodigo);
         //   nodo.setZonaProgramacion(zona);
         //   nodo.setZonaUsuario(zona);
            nodoService.addNodo( nodo );
            return nodo;
        }
//        else if (nodos.get(0).getCodigo()==null){
//                nodos.get(0).setCodigo(nodoCodigo);
//                nodoService.updateNodo(nodos.get(0));
//        }
        return  nodos;
    }



    private MatrizDistancia guardarMatrizDistancia(Date fecha,String numeracion,Date fechaFestivos, Date fechaSabado, String desc){
        MatrizDistancia matrizDistancia= new MatrizDistancia(new Date(),fecha,fechaSabado,fechaFestivos,numeracion,desc);
        matrizDistanciaService.addMatrizDistancia(matrizDistancia);
        return matrizDistancia;
    }

    private DistanciaNodos guardarDistanciaNodos(MatrizDistancia matrizDistancia, int distancia,ServicioDistancia servicioDistancia,String nodoNombre, String nodoCodigo){
        DistanciaNodos distanciaNodosByServicioAndPunto = matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia, matrizDistancia,nodoCodigo);
        if(distanciaNodosByServicioAndPunto==null){
            DistanciaNodos distanciaNodos=new DistanciaNodos(distancia,matrizDistancia,servicioDistancia);
            distanciaNodos.setNodoNombre(nodoNombre);
            distanciaNodos.setNodoCodigo(nodoCodigo);
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
}

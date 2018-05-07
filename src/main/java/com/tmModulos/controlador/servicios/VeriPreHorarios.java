package com.tmModulos.controlador.servicios;

import com.tmModulos.controlador.utils.*;
import com.tmModulos.modelo.dao.tmData.EquivalenciasDao;
import com.tmModulos.modelo.dao.tmData.ExpedicionesTemporalDao;
import com.tmModulos.modelo.dao.tmData.TempHorarioDao;
import com.tmModulos.modelo.dao.tmData.TempPosDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.Iterator;
import java.util.List;

@Service("VeriPreHorarios")
public class VeriPreHorarios {


    @Autowired
    public ExpedicionesTemporalDao expedicionesTemporalDao;

    @Autowired
    public EquivalenciasDao equivalenciasDao;

    @Autowired
    public TempPosDao tempHorarioDao;

    @Autowired
    public ExpedicionesTempConv expedicionesTempConv;


    @Autowired
    private ExcelExtract excelExtract;


    public void addExpTemporal(ExpedicionesTemporal temporal) {
        expedicionesTemporalDao.addExpTemporal(temporal);
    }

    public void deleteExpTemporal(ExpedicionesTemporal temporal) {
        expedicionesTemporalDao.deleteExpTemporal(temporal);
    }


    public void updateExpTemporal(ExpedicionesTemporal temporal) {
       expedicionesTemporalDao.updateExpTemporal(temporal);
    }


    public List<ExpedicionesTemporal> getExpTemporalAll() {
        return expedicionesTemporalDao.getExpTemporalAll();
    }

    public List<ExpedicionesTemporal> getExpedicionesTemporalsData(String id){
        return expedicionesTemporalDao.getExpedicionesTemporalsData(id);
    }

    public List<String> getExpedicionesNoReferenciadas(List<String> serviciosEncontrados){
        return expedicionesTemporalDao.getExpedicionesNoReferenciadas(serviciosEncontrados);
    }


    public List<Equivalencias> getEquivalenciasByData(int linea, int sublinea, int puntoI, int puntoF){
     return equivalenciasDao.getEquivalenciasByData(linea,sublinea,puntoI,puntoF);
    }

    public void addEquivalenciasFromFile(String filename) throws Exception {
        try{
            equivalenciasDao.addEquivalenciasFromFile(filename);
        }catch (Exception e){
            throw new Exception("Archivo con Informacion no esperada");
        }

    }

    public void deleteEquivalencias(){
        equivalenciasDao.deleteEquivalencias();
    }

    public void addTablaHorarioFromFile(String filename) throws Exception {
        try{
            tempHorarioDao.addTablaHorarioFromFile(filename,";");
        }catch (Exception e){
           addTablaHorarioFormatoComa(filename);

        }
    }

    private void addTablaHorarioFormatoComa(String filename) throws Exception {
        try {
            tempHorarioDao.addTablaHorarioFromFile(filename,",");
        }catch (Exception e){
            throw new Exception("Archivo con Informacion no esperada ");
        }
    }

    public void addTablaHorarioTMFromFile(String filename){
        tempHorarioDao.addTablaHorarioTMFromFile(filename);
    }

    public void deleteTablaHorario(){
        tempHorarioDao.deleteTablaHorario();
    }

    public List<TempPos> getTablaHorarioByData(int linea, int sublinea, int ruta, int punto){
        return tempHorarioDao.getTablaHorarioByData(linea,sublinea,ruta,punto);
    }

    public List<String> getTempPosNoReferenciadas(List<String> serviciosEncontrados){
        return tempHorarioDao.getTempPosNoReferenciadas(serviciosEncontrados);
    }

    public Time getSumInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        return tempHorarioDao.getSumInstanteByFranjaHora(idServicio,inicio,fin);
    }

    public String findDelimiterFromFile (String filename){
//        filename.split(".")
        return ";";
    }

    public void addExpedicionesConversion(String destination,char delimiter) throws Exception {
        try{
            equivalenciasDao.addExpedicionesConversion(destination,delimiter);
        }catch (Exception e){
            throw new Exception("Archivo con Informacion no esperada");
        }

    }

    public void deleteExpedicionesConversion() {
        equivalenciasDao.deleteExpedicionesConversion();
    }

    public String getNewExpediciones() {
        String filename = PathFiles.PATH_FOR_FILES_CONVERSION+"ejemploRes.xls";
        return equivalenciasDao.getNewExpediciones(filename);

    }

    public void addDatosExpediciones(String destination) {
        try {

            FileInputStream fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if( row.getCell(0) != null ){

                    ExpedicionesTempConv exp = new ExpedicionesTempConv();
                    exp.setEvento(excelExtract.getStringCellValue(row,ExpDEF.EVENTO));
                    exp.setTipo(excelExtract.getStringCellValue(row,ExpDEF.TIPO));
                    exp.setHoraInicio(excelExtract.getStringCellValue(row,ExpDEF.INICIO));
                    exp.setPuntoInicio(excelExtract.getStringCellValue(row,ExpDEF.DE));
                    exp.setHoraFin(excelExtract.getStringCellValue(row,ExpDEF.FIN));
                    exp.setPuntoFin(excelExtract.getStringCellValue(row,ExpDEF.A));
                    exp.setDur(excelExtract.getStringCellValue(row,ExpDEF.DUR));
                    exp.setBus(excelExtract.getStringCellValue(row,ExpDEF.BUS));
                    exp.setLinea(excelExtract.getStringCellValue(row,ExpDEF.LINEA));
                    exp.setKm(excelExtract.getStringCellValue(row,ExpDEF.KM));
                    exp.setIdentificador(excelExtract.getStringCellValue(row,ExpDEF.V_INFERIDO));
                    exp.setInferido(excelExtract.getStringCellValue(row,ExpDEF.V_INFERIDO));
                    exp.setInferido(excelExtract.getStringCellValue(row,ExpDEF.V_INFERIDO));

                    int codigoNodo = excelExtract.getNumericCellValue(row, MatrizDistanciaDefinicion.NODO);
                    String nodoNombre= row.getCell(MatrizDistanciaDefinicion.NOMBRE_NODO).getStringCellValue();
                    int linea = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.LINEA);
                    int sublinea = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.SUBLINEA);
                    int ruta = excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.RUTA);
                    String operador = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.OPERADOR);
                    String idParada = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.ID_PARADA);
                    String labelNodo = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.LABEL);
                    String atributos = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.ATRIBUTOS);
                    Double posX = excelExtract.getDoubleCellValue(row,MatrizDistanciaDefinicion.POS_X);
                    Double posY = excelExtract.getDoubleCellValue(row,MatrizDistanciaDefinicion.POS_Y);
                    String nombreRuta = excelExtract.getStringCellValue(row,MatrizDistanciaDefinicion.NOMBRE_RUTA);
                    ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(
                            linea,sublinea,ruta,nombreRuta,codigoNodo,row);
                    guardarDistanciaNodos(matrizDistancia,
                            excelExtract.getNumericCellValue(row,MatrizDistanciaDefinicion.POSICION),
                            servicioDistancia,nodoNombre,codigoNodo+"",operador,idParada,labelNodo,atributos,posX,posY);
                }else{
                    break;
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        } catch (IOException e) {
            log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        }
    }
}



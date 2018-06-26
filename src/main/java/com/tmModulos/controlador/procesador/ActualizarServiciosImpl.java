package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.controlador.utils.*;
import com.tmModulos.modelo.entity.tmData.Servicio;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("ActualizarServiciosImpl")
public class ActualizarServiciosImpl {

    private List<LogDatos> logDatos;
    private String destination;

    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private ExcelExtract excelExtract;

    @Autowired
    private ServicioService servicioService;



    public List<LogDatos> processDataFromFile(String fileName, InputStream in) {
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("Incio Actualización de servicios", TipoLog.INFO));
        destination= PathFiles.PATH_FOR_FILES_CONVERSION;
        processorUtils.copyFile(fileName,in,destination);
        destination=PathFiles.PATH_FOR_FILES_CONVERSION+fileName;
        try {
            readExcelAndSaveData(destination);
            logDatos.add(new LogDatos("Fin de Actualización de servicios", TipoLog.INFO));
        } catch (IOException e) {
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
        }

        return logDatos;
    }

    private void readExcelAndSaveData(String destination) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(destination);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        HSSFSheet worksheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = worksheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();
            if (row.getCell(0) != null) {
                    //Buscar Servicio
                    String id_unico = excelExtract.getStringCellValue(row,ServiciosDEF.ID_UNICO);
                    Servicio servicio = servicioService.getServicioByIdUnico(id_unico);
                    if(servicio!=null){
                        updateServicio(servicio,row);
                    }else{
                        logDatos.add(new LogDatos("No se encontro el servicio con Id único "+id_unico, TipoLog.ERROR));
                    }
                //
            }
        }
    }

    private void updateServicio(Servicio servicio, Row row) {
        int linea = excelExtract.getNumericCellValue(row,ServiciosDEF.LINEA);
        int sublinea = excelExtract.getNumericCellValue(row,ServiciosDEF.SUBLINEA);
        int ruta = excelExtract.getNumericCellValue(row,ServiciosDEF.RUTA);
        int punto = excelExtract.getNumericCellValue(row,ServiciosDEF.PUNTO);
        int puntoFin = excelExtract.getNumericCellValue(row,ServiciosDEF.PUNTO_FIN);
        int cuartoFranja = excelExtract.getNumericCellValue(row,ServiciosDEF.CUARTO_FRANJA);
        int sentido = excelExtract.getNumericCellValue(row,ServiciosDEF.SENTIDO);
        int distancia = excelExtract.getNumericCellValue(row,ServiciosDEF.DISTANCIA);
        String nombreGeneral = excelExtract.getStringCellValue(row,ServiciosDEF.NOMBRE_G);
        String nombreEspecial = excelExtract.getStringCellValue(row,ServiciosDEF.NOMBRE_E);
        String tipoServicio = excelExtract.getNumericCellValue(row,ServiciosDEF.TIPO_SERVICIO)+"";

        servicio.setMacro(linea);
        servicio.setLinea(sublinea);
        servicio.setSeccion(ruta);
        servicio.setPunto(punto);
        servicio.setPuntoFin(puntoFin);
        servicio.setCuartoFranja(cuartoFranja);
        servicio.setSentido(sentido);
        servicio.setDistanciaBase(distancia);
        servicio.setNombreGeneral(nombreGeneral);
        servicio.setNombreEspecial(nombreEspecial);
        servicio.setTipoServicio(tipoServicio);
        servicio.setIdentificador(linea+"-"+sublinea+"-"+ruta+"-"+punto);
        servicio.setIdentificadorGIS(linea+"-"+sentido+"-"+sublinea);

        servicioService.updateServicio(servicio);
    }
}

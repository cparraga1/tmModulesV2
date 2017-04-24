package com.tmModulos.controlador.servicios;

import com.tmModulos.controlador.utils.GisCargaDefinition;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.dao.tmData.ServicioTipoDiaDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("OrdenServiciosService")
@Transactional(readOnly = true)
public class OrdenServiciosService {

    @Autowired
    ServicioService servicioService;

    @Autowired
    private ProcessorUtils processorUtils;

    private String destination="C:\\temp\\";
    private boolean actualizar;

    public List<ServicioTipoDia>  processDataFromFile(String fileName, InputStream in, TipoDia tipoDia, List<ServicioTipoDia> servicios) {
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
       return leerYProcesarDatosExcel(destination, servicios, tipoDia);

    }

    private  List<ServicioTipoDia>  leerYProcesarDatosExcel(String destination, List<ServicioTipoDia> servicios, TipoDia tipoDia) {
        List<ServicioTipoDia> serviciosActualizar = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if( row.getCell(0) != null ){
                    ServicioTipoDia servicioActual = obtenerServicio(servicios, row.getCell(0).getStringCellValue());
                    if(servicioActual!=null){
                        int nuevoOrden = (int) row.getCell(1).getNumericCellValue();
                        if(servicioActual.getOrden()!=nuevoOrden){
                            servicioActual.setOrden(nuevoOrden);
                            serviciosActualizar.add(servicioActual);
                        }
                    }else{
                        System.out.println("No se encontro el servicio: "+row.getCell(0).getStringCellValue());
                        actualizar =false;
                        serviciosActualizar= new ArrayList<>();
                        break;
                    }

                }else{
                    break;
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serviciosActualizar;
    }

    private ServicioTipoDia obtenerServicio(List<ServicioTipoDia> servicios, String stringCellValue) {

        for(ServicioTipoDia servicioTipoDia:servicios){
            if(servicioTipoDia.getIdentificador().equals(stringCellValue)){
                return servicioTipoDia;
            }
        }


        return null;
    }


}

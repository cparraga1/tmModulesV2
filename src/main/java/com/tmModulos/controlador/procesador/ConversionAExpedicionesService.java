package com.tmModulos.controlador.procesador;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.FormatoArchivo;
import com.tmModulos.controlador.utils.PathFiles;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.dao.tmData.NodoDao;
import com.tmModulos.modelo.entity.tmData.Nodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

@Service("ConversionExpediciones")
public class ConversionAExpedicionesService {


    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private NodoDao nodoDao;

    @Autowired
    private VeriPreHorarios veriPreHorarios;

    public String convertirAExpediciones(String fileName, InputStream inputstream,String tipoArchivo) {
        String destination= PathFiles.PATH_FOR_FILES_CONVERSION;
        processorUtils.copyFileUTF8(fileName,inputstream,destination);
        destination=PathFiles.PATH_FOR_FILES_CONVERSION+fileName;


        try {

        if(tipoArchivo.equals(FormatoArchivo.CSV_COMMA)){
                veriPreHorarios.addExpedicionesConversion(destination,',');
        }else{
            veriPreHorarios.addExpedicionesConversion(destination,';');
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
//    //      veriPreHorarios.addDatosExpediciones(destination);
       destination = veriPreHorarios.getNewExpediciones();

       veriPreHorarios.deleteExpedicionesConversion();
        return destination;
    }


    private String incluirFilasArchivo(String nombre, char separador) {
        String csvFile = PathFiles.PATH_FOR_FILES_CONVERSION+nombre;
        String csvFileOut = PathFiles.PATH_FOR_FILES_CONVERSION+"out_"+nombre;
        CSVWriter writer = null;
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile),separador);
            writer = new CSVWriter(new FileWriter(csvFileOut), ',');
            String[] entries = null;
            while ((entries = reader.readNext()) != null) {
                ArrayList<String> list = new ArrayList(Arrays.asList(entries));
                //remplazar texto por puntos numericos
                String puntoInicio = list.get(ExpedicionesIndex.PUNTO_INICIO);
                String puntoFin = list.get(ExpedicionesIndex.PUNTO_FIN);
                Nodo nodoInicio = nodoDao.getNodoByNombreCorto(puntoInicio);
                Nodo nodoFin = nodoDao.getNodoByNombreCorto(puntoFin);

                if(nodoInicio!=null){
                    list.set(ExpedicionesIndex.PUNTO_INICIO,nodoInicio.getCodigo());
                }else{
                    list.set(ExpedicionesIndex.PUNTO_INICIO,"-1");
                }

                if(nodoFin!=null){
                    list.set(ExpedicionesIndex.PUNTO_FIN,nodoFin.getCodigo());
                }else{
                    list.set(ExpedicionesIndex.PUNTO_FIN,"-1");
                }
                entries =  list.toArray(new String[list.size()]);
                writer.writeNext(entries);
            }
            writer.close();
        } catch (IOException e) {
        }
        return csvFileOut;
    }


}

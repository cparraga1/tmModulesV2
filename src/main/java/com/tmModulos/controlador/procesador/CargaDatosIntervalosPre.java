package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.utils.ExcelExtract;
import com.tmModulos.controlador.utils.PreDatos;
import com.tmModulos.controlador.utils.PreDatosDEF;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component
public class CargaDatosIntervalosPre {


    @Autowired
    private ExcelExtract excelExtract;

    public static String tipoAceptado = " Exp";


    public HashMap<String, List<PreDatos>> getExpediciones(String destination) {
        HashMap<String, List<PreDatos>> expediciones = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if (row.getCell(0) != null) {

                    String tipo = excelExtract.getStringCellValue(row,PreDatosDEF.TIPO);
                    if(tipo.equals(tipoAceptado)){
                            String identificador = calcularIdentificador(row);
                            if(expediciones.containsKey(identificador)){
                                List<PreDatos> datos = expediciones.get(identificador);
                                datos.add(nuevoPreDatos(row));
                            }else{

                            }
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private PreDatos nuevoPreDatos(Row row) {
        PreDatos preDatos = new PreDatos();
        String horaInicio = excelExtract.getStringCellValue(row,PreDatosDEF.INICIO);
        return preDatos;
    }

    private String calcularIdentificador(Row row) {
        String linea = excelExtract.getStringCellValue(row,PreDatosDEF.LINEA);
        Integer punto = Integer.parseInt(excelExtract.getStringCellValue(row,PreDatosDEF.PUNTO));
        punto = punto - 1000000;
        linea = linea+"-"+punto;
        return linea;
    }
}

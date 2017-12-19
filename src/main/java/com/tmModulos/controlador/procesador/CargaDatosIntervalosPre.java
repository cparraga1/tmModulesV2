package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.utils.ExcelExtract;
import com.tmModulos.controlador.utils.PosDatosDEF;
import com.tmModulos.controlador.utils.PreDatos;
import com.tmModulos.controlador.utils.PreDatosDEF;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if (row.getCell(0) != null) {

                    String tipo = excelExtract.getStringCellValue(row,PreDatosDEF.TIPO);
                    if(tipo.equals(tipoAceptado)){
                            String identificador = calcularIdentificador(row);
                            if(expediciones.containsKey(identificador)){
                                expediciones.get(identificador).add(nuevoPreDatos(row));
                            }else{
                                List<PreDatos> datos = new ArrayList<>();
                                datos.add(nuevoPreDatos(row));
                                expediciones.put(identificador,datos);
                            }
                    }

                }
            }
            ordenarLista(expediciones);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expediciones;
    }

    private void ordenarLista(HashMap<String, List<PreDatos>> expediciones) {
        for (Map.Entry<String,List<PreDatos>> entry : expediciones.entrySet()) {
            List<PreDatos> lista = entry.getValue();

            Collections.sort(lista, new Comparator<PreDatos>() {
                @Override
                public int compare(PreDatos lhs, PreDatos rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    if( lhs.getHora() == rhs.getHora()){
                        if(lhs.getMinutos() == rhs.getMinutos()){
                            return lhs.getSegundos() < rhs.getSegundos() ? -1 : (lhs.getSegundos() > rhs.getSegundos()) ? 1 : 0;
                        }else{
                            return lhs.getMinutos() < rhs.getMinutos() ? -1 : (lhs.getMinutos() > rhs.getMinutos()) ? 1 : 0;
                        }
                    }
                    return lhs.getHora() < rhs.getHora() ? -1 : (lhs.getHora() > rhs.getHora()) ? 1 : 0;
                }
            });
        }

    }

    private PreDatos nuevoPreDatos(Row row) {
        PreDatos preDatos = new PreDatos();
        String horaInicio = excelExtract.getStringCellDateValue(row,PreDatosDEF.INICIO);
        String[] horaSplit = horaInicio.split(":");
        if(horaSplit[0].equals("00")){
            horaSplit[0] = "24";
        }else if (horaSplit[0].equals("01")) {
            horaSplit[0] = "25";
        }
        preDatos.setHora(Integer.parseInt(horaSplit[0]));
        preDatos.setMinutos(Integer.parseInt(horaSplit[1]));
        preDatos.setSegundos(Integer.parseInt(horaSplit[2]));
        preDatos.setDistancia(excelExtract.getDoubleCellValue(row,PreDatosDEF.DISTANCIA));
        return preDatos;
    }

    private PreDatos nuevoPosDatos(Row row) {
        PreDatos preDatos = new PreDatos();
        String horaInicio = excelExtract.getStringCellDateValue(row,PosDatosDEF.INICIO);
        String[] horaSplit = horaInicio.split(":");
        if(horaSplit[0].equals("00")){
            horaSplit[0] = "24";
        }else if (horaSplit[0].equals("01")) {
            horaSplit[0] = "25";
        }
        preDatos.setHora(Integer.parseInt(horaSplit[0]));
        preDatos.setMinutos(Integer.parseInt(horaSplit[1]));
        preDatos.setSegundos(Integer.parseInt(horaSplit[2]));
        return preDatos;
    }

    private String calcularIdentificador(Row row) {
        String linea = excelExtract.getStringCellValue(row,PreDatosDEF.LINEA);
        Integer punto = excelExtract.getNumericCellValue(row,PreDatosDEF.PUNTO);
        punto = punto - 1000000;
        linea = linea+"-"+punto;
        return linea;
    }

    private String calcularIdentificadorPos(Row row) {
        int linea = excelExtract.getNumericCellValue(row,PosDatosDEF.LINEA);
        int sublinea = excelExtract.getNumericCellValue(row,PosDatosDEF.SUBLINEA);
        int ruta = excelExtract.getNumericCellValue(row,PosDatosDEF.RUTA);
        int punto = excelExtract.getNumericCellValue(row,PosDatosDEF.PUNTO);
        return linea+"-"+sublinea+"-"+ruta+"-"+punto;
    }

    public HashMap<String, List<PreDatos>> getExpedicionesPos(String destination) {
        HashMap<String, List<PreDatos>> expediciones = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(destination);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if (row.getCell(0) != null) {

                    int tipo = excelExtract.getNumericCellValue(row, PosDatosDEF.EVENTO);
                    if(tipo == 3 || tipo == 11){
                        String identificador = calcularIdentificadorPos(row);
                        if(expediciones.containsKey(identificador)){
                            expediciones.get(identificador).add(nuevoPosDatos(row));
                        }else{
                            List<PreDatos> datos = new ArrayList<>();
                            datos.add(nuevoPosDatos(row));
                            expediciones.put(identificador,datos);
                        }
                    }

                }
            }
            ordenarLista(expediciones);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expediciones;
    }
}

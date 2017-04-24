package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.IntervalosConversionServicio;
import com.tmModulos.modelo.dao.tmData.GisIntervalosDao;
import com.tmModulos.modelo.dao.tmData.IntervalosProgramacionDao;
import com.tmModulos.modelo.dao.tmData.ServicioTipoDiaDao;
import com.tmModulos.modelo.dao.tmData.TiempoIntervalosDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.FacesContext;
import java.io.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("ConversionIntervalosServicio")
@Transactional(readOnly = true)
public class ConversionIntervalosServicio {

   @Autowired
   private IntervalosConversionServicio intervalosServicio;

    private String destination="C:\\temp\\";

    private List<IntervalosProgramacion> intervalosProgramacions;
    private List<ServicioTipoDia> servicios;
    private String cuadro ;

    public ConversionIntervalosServicio() {
    }

    public StreamedContent crearArchivoDeIntervalos(GisIntervalos gisIntervalos){

        TipoDia tipoDia = intervalosServicio.getTipoDia(gisIntervalos.getTipoDia().getNombre());
        intervalosProgramacions = intervalosServicio.getAllIntervalos();
        servicios = intervalosServicio.getServiciosByTipoDia(tipoDia);
        cuadro=gisIntervalos.getCuadro();
        crearArchivo();
        try {
            FileInputStream fileInputStream = new FileInputStream(destination+"poi-test.xls");
            StreamedContent file = new DefaultStreamedContent(fileInputStream, "application/vnd.ms-excel", "intervalos_gis.xls");
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void crearArchivo(){
        try {
            FileOutputStream fileOut = new FileOutputStream(destination+"poi-test.xls");
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Intervalos");
            //Crear  primera linea
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("HORARIO");
            //Agregar servicios
            agregarServicios(row);
            agregarTiempos(sheet);
            sheet.autoSizeColumn(0);

            //Guardar libro
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void agregarTiempos(HSSFSheet sheet) {
        for(int i=0;i<intervalosProgramacions.size();i++){
            Row row = sheet.createRow(i+1);
            Cell cell = row.createCell(0);
            cell.setCellValue(intervalosProgramacions.get(i).getInicio()+"-"+intervalosProgramacions.get(i).getFin());
            for(int j=0;j<servicios.size();j++){
                List<TiempoIntervalos> tiempoIntervalos = intervalosServicio.getTiempoIntervalosByHoraServicioCuadro(intervalosProgramacions.get(i),
                        servicios.get(j),cuadro);
                String promedioIntervalos = calcularPromedio(tiempoIntervalos);
                if(!promedioIntervalos.equals("0")){
                    Cell cellInterna = row.createCell(j+1);
                    cellInterna.setCellValue(promedioIntervalos);
                }
            }
//            cell.setCellValue(convertirTiempo(intervalosProgramacions.get(i).getInicio())+"-"+convertirTiempo(intervalosProgramacions.get(i).getFin()));
        }

    }

    private String calcularPromedio(List<TiempoIntervalos> tiempoIntervalos) {
        double tiempoTotal=0;
        int i=0;
        for (TiempoIntervalos tiempoInt:tiempoIntervalos ) {
            tiempoTotal= tiempoTotal+ tiempoInt.getInstante();
            i++;
        }
        if(tiempoTotal!=0){
            tiempoTotal= tiempoTotal/i;
            return getTime((int) tiempoTotal).toString();
        }

        return "0";
    }

    private Time getTime(int instante) {
        int hor = instante / 3600;
        int min=(instante-(3600*hor))/60;
        int seg=instante-((hor*3600)+(min*60));
        Time time= new Time(hor,min,seg);
        return time;
    }

    public String convertirTiempo(Time tiempo){
        SimpleDateFormat format = new SimpleDateFormat("HH:MM");
        String date = format.format(new Date(tiempo.getTime()));
        return date;
    }

    private void agregarServicios(Row row) {
        for(int i=0;i<servicios.size();i++){
            Cell cell = row.createCell(i+1);
            cell.setCellValue(servicios.get(i).getIdentificador());
        }
    }

    public IntervalosConversionServicio getIntervalosServicio() {
        return intervalosServicio;
    }

    public void setIntervalosServicio(IntervalosConversionServicio intervalosServicio) {
        this.intervalosServicio = intervalosServicio;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<IntervalosProgramacion> getIntervalosProgramacions() {
        return intervalosProgramacions;
    }

    public void setIntervalosProgramacions(List<IntervalosProgramacion> intervalosProgramacions) {
        this.intervalosProgramacions = intervalosProgramacions;
    }
}

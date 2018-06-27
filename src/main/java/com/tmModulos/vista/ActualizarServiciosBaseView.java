package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.ActualizarServiciosImpl;
import com.tmModulos.controlador.procesador.DataProcesorImpl;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.PathFiles;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="actualizarServicios")
@ViewScoped
public class ActualizarServiciosBaseView implements Serializable {

    private UploadedFile serviciosFile;
    private String formatoArchivo;


    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @ManagedProperty("#{ActualizarServiciosImpl}")
    private ActualizarServiciosImpl actualizarServicios;

    private List<LogDatos> logDatos;
    private boolean resultadosVisibles;

    @PostConstruct
    public void init(){
        formatoArchivo = "Nota: Recuerde que el archivo debe estar en Formato XLS (Excel 97-2003)";
        resultadosVisibles = false;
        logDatos = new ArrayList<>();
    }

    public ActualizarServiciosBaseView() {
    }

    public void actualizar (){
        if(serviciosFile!=null){
            try {
                logDatos= actualizarServicios.processDataFromFile(serviciosFile.getFileName(),serviciosFile.getInputstream());
                resultadosVisibles=true;
                messagesView.info(Messages.MENSAJE_CARGA_EXITOSA,"Verifique los resultados");
            } catch (IOException e) {
                messagesView.error(Messages.MENSAJE_FALLO_ARCHIVO,Messages.ACCION_FALLO_ARCHIVO);
            }
        }else{
            messagesView.error(Messages.MENSAJE_CALCULO_FALLA,"Cargue el archivo correspondiente");
        }
    }

    public void  download ()throws IOException {


        String filename = "resultado.xls";

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        File file = new File("../webapp/resources/docs/Base_Actualizacion _Servicios.xls");
        file.createNewFile();
        FileInputStream fileIn = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=Base_Actualizacion _Servicios.xls");
        byte[] outputByte = new byte[4096];
        while(fileIn.read(outputByte, 0, 4096) != -1)
        {
            out.write(outputByte, 0, 4096);
        }
        fileIn.close();
        out.flush();
        out.close();
        out.flush();
        try {
            if (out != null) {
                out.close();
            }
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {

        }

    }

    public UploadedFile getServiciosFile() {
        return serviciosFile;
    }

    public void setServiciosFile(UploadedFile serviciosFile) {
        this.serviciosFile = serviciosFile;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public String getFormatoArchivo() {
        return formatoArchivo;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivo = formatoArchivo;
    }

    public List<LogDatos> getLogDatos() {
        return logDatos;
    }

    public void setLogDatos(List<LogDatos> logDatos) {
        this.logDatos = logDatos;
    }

    public boolean isResultadosVisibles() {
        return resultadosVisibles;
    }

    public void setResultadosVisibles(boolean resultadosVisibles) {
        this.resultadosVisibles = resultadosVisibles;
    }

    public ActualizarServiciosImpl getActualizarServicios() {
        return actualizarServicios;
    }

    public void setActualizarServicios(ActualizarServiciosImpl actualizarServicios) {
        this.actualizarServicios = actualizarServicios;
    }

}

package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.DataProcesorImpl;
import com.tmModulos.controlador.procesador.VerificacionHorarios;
import com.tmModulos.controlador.utils.PathFiles;
import com.tmModulos.modelo.entity.tmData.VerificacionTipoDia;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

@ManagedBean(name="VerHorario")
@ViewScoped
public class VerificaHorarioView implements Serializable {


    @ManagedProperty("#{VerificacionHorario}")
    private VerificacionHorarios verificacionHorarios;

    private UploadedFile file;
    private List<VerificacionTipoDia> tipoDiaRecords;
    private String tipoDia;
    private String tipoVerificacion;

    private boolean visibleDescarga;

    private String boxIntervaloMin;
    private String boxIntervaloMinRef;
    private String boxIntervaloMaxRef;
    private String boxIntervaloMax;
    private String formatoArchivo;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @PostConstruct
    public void init(){
        visibleDescarga = false;
        boxIntervaloMin = "00:01:00";
        boxIntervaloMax = "00:07:00";
        boxIntervaloMinRef = "00:01:00";
        boxIntervaloMaxRef = "00:07:00";
        tipoDiaRecords = verificacionHorarios.getTiposDiasDisponibles();
        formatoArchivo = "Nota: Recuerde que el archivo debe estar en Formato csv";

    }


    public void upload() {
            if(file!=null){
                try {
                  /*  verificacionHorarios.compararExpediciones(file.getFileName(),file.getInputstream(),tipoVerificacion,tipoDia,boxIntervaloMin,boxIntervaloMax,
                            boxIntervaloMinRef,boxIntervaloMaxRef);*/
                    verificacionHorarios.verificarHorarios(file.getFileName(),file.getInputstream(),tipoVerificacion,tipoDia,boxIntervaloMin,boxIntervaloMax,
                            boxIntervaloMinRef,boxIntervaloMaxRef);
                    messagesView.info("Verificación Terminada","");
                    visibleDescarga = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    messagesView.error(e.getMessage(),"");
                } catch (Exception e) {
                    e.printStackTrace();
                    messagesView.error(e.getMessage(),"");
                }
            }
    }

    public void  download ()throws IOException {


        String filename = "resultado.xls";

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();



        File file = new File(PathFiles.PATH_FOR_FILES_VERIFICACION+"update.xls");
        file.createNewFile();
        FileInputStream fileIn = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename="+tipoDia+".xls");


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

    public void updateFormato(){

    }


    public boolean isVisibleDescarga() {
        return visibleDescarga;
    }

    public void setVisibleDescarga(boolean visibleDescarga) {
        this.visibleDescarga = visibleDescarga;
    }

    public VerificacionHorarios getVerificacionHorarios() {
        return verificacionHorarios;
    }

    public void setVerificacionHorarios(VerificacionHorarios verificacionHorarios) {
        this.verificacionHorarios = verificacionHorarios;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getTipoVerificacion() {
        return tipoVerificacion;
    }

    public void setTipoVerificacion(String tipoVerificacion) {
        this.tipoVerificacion = tipoVerificacion;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public String getBoxIntervaloMin() {
        return boxIntervaloMin;
    }

    public void setBoxIntervaloMin(String boxIntervaloMin) {
        this.boxIntervaloMin = boxIntervaloMin;
    }

    public String getBoxIntervaloMax() {
        return boxIntervaloMax;
    }

    public void setBoxIntervaloMax(String boxIntervaloMax) {
        this.boxIntervaloMax = boxIntervaloMax;
    }

    public List<VerificacionTipoDia> getTipoDiaRecords() {
        return tipoDiaRecords;
    }

    public void setTipoDiaRecords(List<VerificacionTipoDia> tipoDiaRecords) {
        this.tipoDiaRecords = tipoDiaRecords;
    }

    public String getFormatoArchivo() {
        return formatoArchivo;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivo = formatoArchivo;
    }

    public String getBoxIntervaloMinRef() {
        return boxIntervaloMinRef;
    }

    public void setBoxIntervaloMinRef(String boxIntervaloMinRef) {
        this.boxIntervaloMinRef = boxIntervaloMinRef;
    }

    public String getBoxIntervaloMaxRef() {
        return boxIntervaloMaxRef;
    }

    public void setBoxIntervaloMaxRef(String boxIntervaloMaxRef) {
        this.boxIntervaloMaxRef = boxIntervaloMaxRef;
    }
}



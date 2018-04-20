package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.ConversionAExpedicionesService;
import com.tmModulos.controlador.servicios.Util;
import com.tmModulos.controlador.utils.PathFiles;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@ManagedBean(name="ConvExpediciones")
@ViewScoped
public class ConversorExpedicionesView {

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;
    private UploadedFile file;
    private boolean visibleDescarga;
    private List<String> tipoArchivos;
    private String tipoArchivo;
    private String archivoResultado;

    @ManagedProperty("#{ConversionExpediciones}")
    private ConversionAExpedicionesService conversionAExpedicionesService;

    public ConversorExpedicionesView() {
    }

    @PostConstruct
    public void init() {
        visibleDescarga = false;
        tipoArchivos = Util.listaFormatosCSV();
    }

    public void upload(){
        if(file!=null){
            try {
                archivoResultado =  conversionAExpedicionesService.convertirAExpediciones(file.getFileName(),file.getInputstream(),tipoArchivo);
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

    public void download() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        File file = new File(PathFiles.PATH_FOR_FILES_CONVERSION+""+archivoResultado);
        file.createNewFile();
        FileInputStream fileIn = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=conversion.csv");
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

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isVisibleDescarga() {
        return visibleDescarga;
    }

    public void setVisibleDescarga(boolean visibleDescarga) {
        this.visibleDescarga = visibleDescarga;
    }

    public ConversionAExpedicionesService getConversionAExpedicionesService() {
        return conversionAExpedicionesService;
    }

    public void setConversionAExpedicionesService(ConversionAExpedicionesService conversionAExpedicionesService) {
        this.conversionAExpedicionesService = conversionAExpedicionesService;
    }

    public List<String> getTipoArchivos() {
        return tipoArchivos;
    }

    public void setTipoArchivos(List<String> tipoArchivos) {
        this.tipoArchivos = tipoArchivos;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
}

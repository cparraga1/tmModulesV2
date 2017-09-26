package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.ConversionABusesHoraService;
import com.tmModulos.controlador.procesador.VerificacionHorarios;
import com.tmModulos.controlador.utils.PathFiles;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import com.tmModulos.modelo.entity.tmData.VerificacionTipoDia;
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
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="ConvBusesHora")
@ViewScoped
public class ConversionBusesHoraView {

    @ManagedProperty("#{ConversionBusesHora}")
    private ConversionABusesHoraService conversionABusesHoraService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;
    private UploadedFile file;
    private String tipoDia;
    private boolean visibleDescarga;

    private String boxIntervaloMin;
    private String boxIntervaloMax;

    private List<String> tipoDiaRecords;

    public ConversionBusesHoraView() {
    }

    @PostConstruct
    public void init(){
        visibleDescarga = false;
        tipoDiaRecords = new ArrayList<>();
        tipoDiaRecords.add("HABIL");
        tipoDiaRecords.add("SABADO");
        tipoDiaRecords.add("FESTIVO");
        boxIntervaloMin = "00:01:00";
        boxIntervaloMax = "00:07:00";
    }

    public void upload() {
        if(file!=null){
            try {
                conversionABusesHoraService.convertirABusesHora(file.getFileName(),file.getInputstream(),tipoDia, boxIntervaloMin,boxIntervaloMax);
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
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        File file = new File(PathFiles.PATH_FOR_FILES+"\\Migracion\\"+PathFiles.BUSES_HORA_FILE);
        file.createNewFile();
        FileInputStream fileIn = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=BusesHora"+tipoDia+".xls");
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


    public ConversionABusesHoraService getConversionABusesHoraService() {
        return conversionABusesHoraService;
    }

    public void setConversionABusesHoraService(ConversionABusesHoraService conversionABusesHoraService) {
        this.conversionABusesHoraService = conversionABusesHoraService;
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

    public List<String> getTipoDiaRecords() {
        return tipoDiaRecords;
    }

    public void setTipoDiaRecords(List<String> tipoDiaRecords) {
        this.tipoDiaRecords = tipoDiaRecords;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
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
}

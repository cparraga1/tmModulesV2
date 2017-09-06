package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class IntervalosVerificacionHorarios {



    public IntervalosVerificacionHorarios() {
    }

    public List<String> calcularIntervalos(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaInicioB,
                                           Date horaFin, Date horaFinB) {

        return calcularIntervalosUnHorario(expedicionesTemporals,horaInicio,horaFin,horaInicioB,horaFinB);



    }



    private List<String> calcularIntervalosUnHorario(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio,
                                                     Date horaFin,Date horaInicioB, Date horaFinB) {

        List<String> intervalosExpediciones = new ArrayList<>();

        if( expedicionesTemporals.size()>1){
            List<Long> diferenciaExpediciones  = new ArrayList<>();
            if( horaInicioB == null && horaFinB == null ){
                diferenciaExpediciones  = calcularTiempoDiferencia(expedicionesTemporals);
            }else{
                diferenciaExpediciones  = calcularTiempoDiferenciaDosHorarios(expedicionesTemporals,horaInicio,horaFin,horaInicioB,horaFinB);
            }

            intervalosExpediciones = calcularIntervalos(diferenciaExpediciones);
        }else{

        }

        return intervalosExpediciones;
    }

    private List<String> calcularIntervalos(List<Long> diferenciaExpediciones) {
        List<String> resultadosIntervalos = new ArrayList<>();
        long minimo = diferenciaExpediciones.get(0);
        long maximo = diferenciaExpediciones.get(0);
        long promedio = 0;
        long sumatoria = 0;
        for (Long tiempo:diferenciaExpediciones ) {
            sumatoria = sumatoria + tiempo;
            if( minimo > tiempo )  minimo = tiempo;
            if( maximo < tiempo )  maximo = tiempo;
        }
        promedio = sumatoria / diferenciaExpediciones.size();

        resultadosIntervalos.add(ProcessorUtils.convertLongToTime(promedio));
        resultadosIntervalos.add(ProcessorUtils.convertLongToTime(minimo));
        resultadosIntervalos.add(ProcessorUtils.convertLongToTime(maximo));

        return resultadosIntervalos;
    }




    private List<Long> calcularTiempoDiferencia(List<ExpedicionesTemporal> expedicionesTemporals) {
        List<Long> diferenciaExpediciones = new ArrayList<>();

        Date expedicionA = convertirATime(expedicionesTemporals.get(0).getHoraInicio());
        Date expedicionB;
        for(int i=1;i<expedicionesTemporals.size();i++){
            expedicionB = convertirATime(expedicionesTemporals.get(i).getHoraInicio());
            diferenciaExpediciones.add(expedicionB.getTime() - expedicionA.getTime());
            expedicionA = expedicionB;
        }

        return  diferenciaExpediciones;
    }

    private List<Long> calcularTiempoDiferenciaDosHorarios(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio,Date horaFin,Date horaInicioB,Date horaFinB) {
        List<Long> diferenciaExpediciones = new ArrayList<>();

        Date expedicionA = convertirATime(expedicionesTemporals.get(0).getHoraInicio());
        Date expedicionB;
        boolean band = true;
        for(int i=1;i<expedicionesTemporals.size();i++){
            expedicionB = convertirATime(expedicionesTemporals.get(i).getHoraInicio());
            if(expedicionB.after(horaInicio) && expedicionB.before(horaFin)){
                diferenciaExpediciones.add(expedicionB.getTime() - expedicionA.getTime());
                expedicionA = expedicionB;
            }else if (band){
                band = false;
                expedicionA = expedicionB;
            }else{
                diferenciaExpediciones.add(expedicionB.getTime() - expedicionA.getTime());
                expedicionA = expedicionB;
            }
        }

        return  diferenciaExpediciones;
    }

    public Date convertirATime(String stringCellValue) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if(!stringCellValue.equals("")){
            try {
                Date date = parser.parse(stringCellValue);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new Date();
    }



}

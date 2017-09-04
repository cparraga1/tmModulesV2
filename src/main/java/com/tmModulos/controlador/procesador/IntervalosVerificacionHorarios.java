package com.tmModulos.controlador.procesador;

import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class IntervalosVerificacionHorarios {

    public IntervalosVerificacionHorarios() {
    }

    public List<String> calcularIntervalos(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaInicioB,
                                           Date horaFin, Date horaFinB) {

        return calcularIntervalosUnHorario(expedicionesTemporals);

//        if( horaInicioB == null ){
//            return calcularIntervalosUnHorario(expedicionesTemporals);
//        }else{
//            return calcularIntervalosDosHorarios(expedicionesTemporals,horaInicio,horaFin,horaInicioB,horaFinB);
//
//        }

    }



    private List<String> calcularIntervalosUnHorario(List<ExpedicionesTemporal> expedicionesTemporals) {

        List<String> intervalosExpediciones = new ArrayList<>();

        if( expedicionesTemporals.size()>1){
            List<Long> diferenciaExpediciones  = calcularTiempoDiferencia(expedicionesTemporals);
            intervalosExpediciones = calcularIntervalos(diferenciaExpediciones);
        }else{

        }

        return intervalosExpediciones;
    }

    private List<String> calcularIntervalos(List<Long> diferenciaExpediciones) {
        List<String> resultadosIntervalos = new ArrayList<>();
        long minimo = diferenciaExpediciones.get(0);
        long maximo = diferenciaExpediciones.get(1);
        long promedio = 0;
        long sumatoria = 0;
        for (Long tiempo:diferenciaExpediciones ) {
            sumatoria = sumatoria + tiempo;
            if( minimo > tiempo )  minimo = tiempo;
            if( maximo < tiempo )  maximo = tiempo;
        }
        promedio = sumatoria / diferenciaExpediciones.size();

        resultadosIntervalos.add(convertLongToTime(promedio));
        resultadosIntervalos.add(convertLongToTime(minimo));
        resultadosIntervalos.add(convertLongToTime(maximo));

        return resultadosIntervalos;
    }

    private String convertLongToTime(long tiempo) {
       return  String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(tiempo),
                TimeUnit.MILLISECONDS.toMinutes(tiempo) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tiempo)),
                TimeUnit.MILLISECONDS.toSeconds(tiempo) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempo)));


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

    private List<String> calcularIntervalosDosHorarios(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaFin, Date horaInicioB, Date horaFinB) {
        return new ArrayList<>();
    }



    private Date convertirATime(String stringCellValue) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if(!stringCellValue.equals("")){
            try {
                Date date = parser.parse(stringCellValue);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

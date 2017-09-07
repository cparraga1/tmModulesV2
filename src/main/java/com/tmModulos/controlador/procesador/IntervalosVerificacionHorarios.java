package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.FranjaHorarioService;
import com.tmModulos.controlador.utils.DateMap;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class IntervalosVerificacionHorarios {

    @Autowired
    private FranjaHorarioService franjaHorarioService;

    private Map<String,DateMap> franjas;

    public IntervalosVerificacionHorarios() {

    }

    public void cargarFranjas(){
        List<TipoFranja> franjasRecords = franjaHorarioService.getAllTipoFranja();
        franjas = new HashMap<String,DateMap>();
        for(TipoFranja franja:franjasRecords){
            DateMap  dateMap = new DateMap();
            dateMap.setNombre(franja.getNombre());
            dateMap.setHoraInicio(convertirATime(franja.getHoraInicio()));
            dateMap.setHoraFin(convertirATime(franja.getHoraFin()));
            franjas.put(franja.getNombre(),dateMap);
        }
    }

    public List<String> calcularIntervalos(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio, Date horaInicioB,
                                           Date horaFin, Date horaFinB) {
        cargarFranjas();
        List<String> intervalosResultado = new ArrayList<>();
        List<Long> tiemposFranjaInicio = new ArrayList<>();
        List<Long> tiemposFranjaPicoAM = new ArrayList<>();
        List<Long> tiemposFranjaValle = new ArrayList<>();
        List<Long> tiemposFranjaPicoPM = new ArrayList<>();
        List<Long> tiemposFranjaCierre = new ArrayList<>();


        if(franjas.size()>0){
            for(ExpedicionesTemporal expediciones: expedicionesTemporals){
                Date exp =  convertirATime(expediciones.getHoraInicio());
                if(exp.after(franjas.get("Inicio").getHoraInicio()) &&
                        exp.before(franjas.get("Inicio").getHoraFin())){
                    tiemposFranjaInicio.add(exp.getTime());
                }else if(exp.after(franjas.get("Pico AM").getHoraInicio()) &&
                        exp.before(franjas.get("Pico AM").getHoraFin())){
                    tiemposFranjaPicoAM.add(exp.getTime());
                }else if(exp.after(franjas.get("Valle").getHoraInicio()) &&
                        exp.before(franjas.get("Valle").getHoraFin())){
                    tiemposFranjaValle.add(exp.getTime());
                }else if(exp.after(franjas.get("Pico PM").getHoraInicio()) &&
                        exp.before(franjas.get("Pico PM").getHoraFin())){
                    tiemposFranjaPicoPM.add(exp.getTime());
                }else{
                    tiemposFranjaCierre.add(exp.getTime());
                }
            }
        }

        intervalosResultado = calcularValorIntervalosPorFranja(intervalosResultado,tiemposFranjaInicio);
        intervalosResultado = calcularValorIntervalosPorFranja(intervalosResultado,tiemposFranjaPicoAM);
        intervalosResultado = calcularValorIntervalosPorFranja(intervalosResultado,tiemposFranjaValle);
        intervalosResultado = calcularValorIntervalosPorFranja(intervalosResultado,tiemposFranjaPicoPM);
        intervalosResultado = calcularValorIntervalosPorFranja(intervalosResultado,tiemposFranjaCierre);
       // return calcularIntervalosUnHorario(expedicionesTemporals,horaInicio,horaFin,horaInicioB,horaFinB);
        return intervalosResultado;

    }

    private List<String> calcularValorIntervalosPorFranja(List<String> intervalosResultado, List<Long> tiemposFranjaInicio) {

        List<String> resultadosFranja = new ArrayList<>();
        if(tiemposFranjaInicio.size()>1){
            List<Long> diferenciaExpediciones  = calcularDiferenciaExpediciones(tiemposFranjaInicio);
            resultadosFranja = calcularIntervalos(diferenciaExpediciones);
            intervalosResultado.add(resultadosFranja.get(0));
            intervalosResultado.add(resultadosFranja.get(1));
            intervalosResultado.add(resultadosFranja.get(2));
        }else{
            intervalosResultado.add("N/A");
            intervalosResultado.add("N/A");
            intervalosResultado.add("N/A");
        }

        return intervalosResultado;
    }

    private List<Long> calcularDiferenciaExpediciones(List<Long> tiemposFranja) {
        List<Long> diferenciaExpediciones = new ArrayList<>();

        Long expedicionA = tiemposFranja.get(0);
        Long expedicionB;
        for(int i=1;i<tiemposFranja.size();i++){
            expedicionB = tiemposFranja.get(i);
            diferenciaExpediciones.add(expedicionB - expedicionA);
            expedicionA = expedicionB;
        }

        return  diferenciaExpediciones;
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

    public FranjaHorarioService getFranjaHorarioService() {
        return franjaHorarioService;
    }

    public void setFranjaHorarioService(FranjaHorarioService franjaHorarioService) {
        this.franjaHorarioService = franjaHorarioService;
    }
}

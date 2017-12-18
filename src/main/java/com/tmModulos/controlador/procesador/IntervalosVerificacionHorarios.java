package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.FranjaHorarioService;
import com.tmModulos.controlador.utils.DateMap;
import com.tmModulos.controlador.utils.PreDatos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.TempHorario;
import com.tmModulos.modelo.entity.tmData.TempPos;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class IntervalosVerificacionHorarios {

    @Autowired
    private FranjaHorarioService franjaHorarioService;

    @Autowired
    private ProcessorUtils processorUtils;

    private Map<String,DateMap> franjas;

    public IntervalosVerificacionHorarios() {

    }

    public void cargarFranjas(){
        List<TipoFranja> franjasRecords = franjaHorarioService.getAllTipoFranja();
        franjas = new HashMap<String,DateMap>();
        for(TipoFranja franja:franjasRecords){
            DateMap  dateMap = new DateMap();
            dateMap.setNombre(franja.getNombre());
            dateMap.setHoraInicio(processorUtils.convertirATime(franja.getHoraInicio()));
            dateMap.setHoraFin(processorUtils.convertirATime(franja.getHoraFin()));
            franjas.put(franja.getNombre(),dateMap);
        }
    }

    public List<String> calcularIntervalos(List<PreDatos> expedicionesTemporals, List<Integer> inicio, List<Integer> inicioB,
                                           List<Integer> fin, List<Integer> finB) {

//        Date horaInicio = processorUtils.convertirATime(inicio.get(0)+":"+inicio.get(1)+":"+inicio.get(2));
//        Date horaInicioB =  processorUtils.convertirATime(inicioB.get(0)+":"+inicioB.get(1)+":"+inicioB.get(2));
//        Date horaFin = processorUtils.convertirATime(fin.get(0)+":"+fin.get(1)+":"+fin.get(2));
//        Date horaFinB = processorUtils.convertirATime(finB.get(0)+":"+finB.get(1)+":"+finB.get(2));

        List<String> intervalosResultado = new ArrayList<>();
        List<Long> tiemposFranjaInicio = new ArrayList<>();
        List<Long> tiemposFranjaPicoAM = new ArrayList<>();
        List<Long> tiemposFranjaValle = new ArrayList<>();
        List<Long> tiemposFranjaPicoPM = new ArrayList<>();
        List<Long> tiemposFranjaCierre = new ArrayList<>();


        if(franjas.size()>0){
            for(PreDatos expediciones: expedicionesTemporals){
                Date exp =  processorUtils.convertirATime(expediciones.toString());
                if(estaEnelRango(exp,"Inicio")){
                    tiemposFranjaInicio.add(exp.getTime());
                }else if(estaEnelRango(exp,"Pico AM")){
                    tiemposFranjaPicoAM.add(exp.getTime());
                }else if(estaEnelRango(exp,"Valle")){
                    tiemposFranjaValle.add(exp.getTime());
                }else if(estaEnelRango(exp,"Pico PM")){
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
        return intervalosResultado;

    }

    public List<String> calcularIntervalosPos(List<TempPos> tempHorarios, Date horaInicio, Date horaInicioB,
                                           Date horaFin, Date horaFinB) {
        cargarFranjas();
        List<String> intervalosResultado = new ArrayList<>();
        List<Long> tiemposFranjaInicio = new ArrayList<>();
        List<Long> tiemposFranjaPicoAM = new ArrayList<>();
        List<Long> tiemposFranjaValle = new ArrayList<>();
        List<Long> tiemposFranjaPicoPM = new ArrayList<>();
        List<Long> tiemposFranjaCierre = new ArrayList<>();


        if(franjas.size()>0){
            for(TempPos horario: tempHorarios){

                Date exp =  processorUtils.convertirATime(horario.getInstante().toString());
                if(estaEnelRango(exp,"Inicio")){
                    tiemposFranjaInicio.add(exp.getTime());
                }else if(estaEnelRango(exp,"Pico AM")){
                    tiemposFranjaPicoAM.add(exp.getTime());
                }else if(estaEnelRango(exp,"Valle")){
                    tiemposFranjaValle.add(exp.getTime());
                }else if(estaEnelRango(exp,"Pico PM")){
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
        return intervalosResultado;

    }

    private boolean estaEnelRango(Date exp,String nombreFranja) {
        if(exp.after(franjas.get(nombreFranja).getHoraInicio()) || exp.equals(franjas.get(nombreFranja).getHoraInicio())){
            if(exp.before(franjas.get(nombreFranja).getHoraFin()) || exp.equals(franjas.get(nombreFranja).getHoraFin())){
                return  true;
            }
        }
        return false;
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
            intervalosResultado.add("");
            intervalosResultado.add("");
            intervalosResultado.add("");
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

        Date expedicionA = processorUtils.convertirATime(expedicionesTemporals.get(0).getHoraInicio());
        Date expedicionB;
        for(int i=1;i<expedicionesTemporals.size();i++){
            expedicionB = processorUtils.convertirATime(expedicionesTemporals.get(i).getHoraInicio());
            diferenciaExpediciones.add(expedicionB.getTime() - expedicionA.getTime());
            expedicionA = expedicionB;
        }

        return  diferenciaExpediciones;
    }

    private List<Long> calcularTiempoDiferenciaDosHorarios(List<ExpedicionesTemporal> expedicionesTemporals, Date horaInicio,Date horaFin,Date horaInicioB,Date horaFinB) {
        List<Long> diferenciaExpediciones = new ArrayList<>();

        Date expedicionA = processorUtils.convertirATime(expedicionesTemporals.get(0).getHoraInicio());
        Date expedicionB;
        boolean band = true;
        for(int i=1;i<expedicionesTemporals.size();i++){
            expedicionB = processorUtils.convertirATime(expedicionesTemporals.get(i).getHoraInicio());
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



    public FranjaHorarioService getFranjaHorarioService() {
        return franjaHorarioService;
    }

    public void setFranjaHorarioService(FranjaHorarioService franjaHorarioService) {
        this.franjaHorarioService = franjaHorarioService;
    }
}

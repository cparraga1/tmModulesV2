package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.FranjaCuartos;
import com.tmModulos.controlador.utils.FranjaDef;
import com.tmModulos.controlador.utils.IntervaloCuartos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.dao.tmData.HoraFranjaDao;
import com.tmModulos.modelo.dao.tmData.TempBusesHoraDao;
import com.tmModulos.modelo.dao.tmData.TempPosDao;
import com.tmModulos.modelo.entity.saeBogota.HorarioS;
import com.tmModulos.modelo.entity.saeBogota.Vigencias;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Tie;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@Service("IntervalosProcessor")
public class IntervalosProcessor {


    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    @Autowired
    public TempPosDao tempHorarioDao;

    @Autowired
    public TempBusesHoraDao tempBusesHoraDao;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    private TipoFranja franjaIncio ;
    private TipoFranja franjaPicoAM;
    private TipoFranja franjaValle;
    private TipoFranja franjaPicoPM;
    private TipoFranja franjaCierre;
    private List<IntervalosProgramacion> intervalosFranjaInicio;
    private List<IntervalosProgramacion> intervalosFranjaPicoAM;
    private List<IntervalosProgramacion> intervalosFranjaValle;
    private List<IntervalosProgramacion> intervalosFranjaPicoPM;
    private List<IntervalosProgramacion> intervalosFranjaCierre;
    Map<IntervalosProgramacion, Double> valoresFinalesInicio;
    Map<IntervalosProgramacion, Double> valoresFinalesPicoAM;
    Map<IntervalosProgramacion, Double> valoresFinalesValle;
    Map<IntervalosProgramacion, Double> valoresFinalesPicoPM;
    Map<IntervalosProgramacion, Double> valoresFinalesCierre;

    List<Double> intervalosInicio;
    List<Double> intervalosPicoAM;
    List<Double> intervalosValle;
    List<Double> intervalosPicoPM;
    List<Double> intervalosCierre;



    public GisIntervalos generarIntervalos(Date fechaVigencia, String descripcion,
                                           String tipoDia, TablaMaestra tablaMaestra,
                                           TipoDia servicio) {
        GisIntervalos gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,tipoDia,servicio,tablaMaestra);
        horariosProvisionalServicio.addGisIntervalo(gisIntervalos);
        return gisIntervalos;
    }

    public Map<IntervalosProgramacion, Double> convertirTablaHorarioABusesHora(ServicioTipoDia servicioTipoDia){
        List<IntervaloCuartos> tiemposServicio = encontrarTiemposPorServicioBusesHora(servicioTipoDia);
        calcularDiferenciaDeTiempo(tiemposServicio);
        Map<IntervalosProgramacion, Double> intervalosDeTiempo = calcularIntervalosBusesHora(tiemposServicio);
        return intervalosDeTiempo;
    }

    private Map<IntervalosProgramacion, Double> calcularIntervalosBusesHora(List<IntervaloCuartos> tiemposServicio) {
        Map<IntervalosProgramacion, Double> cuartos = new HashMap<IntervalosProgramacion, Double>();
        Map<IntervalosProgramacion, List<Long>> franjaCuartos = distribuirTiemposEIntervalos(tiemposServicio);
        Iterator it = franjaCuartos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<Long> valores = (List<Long>) pair.getValue();
            double valorFInal = calcularPromedio(valores);
            IntervalosProgramacion intervalo = (IntervalosProgramacion) pair.getKey();
            cuartos.put(intervalo,valorFInal);
        }
        return cuartos;
    }


    public void precalcularIntervalosProgramacion(){
        franjaIncio = horariosProvisionalServicio.getTipoFranjaByNombre("Inicio");
        franjaPicoAM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico AM");
        franjaValle = horariosProvisionalServicio.getTipoFranjaByNombre("Valle");
        franjaPicoPM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico PM");
        franjaCierre = horariosProvisionalServicio.getTipoFranjaByNombre("Cierre");

        intervalosFranjaInicio= horariosProvisionalServicio.getIntervaloByFranja(franjaIncio);
        intervalosFranjaPicoAM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoAM);
        intervalosFranjaValle= horariosProvisionalServicio.getIntervaloByFranja(franjaValle);
        intervalosFranjaPicoPM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoPM);
        intervalosFranjaCierre= horariosProvisionalServicio.getIntervaloByFranja(franjaCierre);

    }


    public List<Intervalos> calcularIntervalos(TablaMaestraServicios tablaMaestraServicios,ServicioTipoDia servicio) {
        List<Intervalos> intervalosRecords = new ArrayList<Intervalos>();
        List<IntervaloCuartos> tiemposServicio = encontrarTiemposPorServicio(servicio);

        calcularDiferenciaDeTiempo(tiemposServicio);
        calcularValorPorIntervaloCuarto (tiemposServicio);
        intervalosRecords.add(intervaloPromedio(servicio,tablaMaestraServicios));
        intervalosRecords.add(intervaloMinimo(servicio,tablaMaestraServicios));
        intervalosRecords.add(intervaloMaximo(servicio,tablaMaestraServicios));
        return intervalosRecords;
    }

    private Intervalos intervaloMaximo(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios) {
        double valorInicio = maximo(intervalosInicio);
        double valorPicoAm = maximo(intervalosPicoAM);
        double valorValle = maximo(intervalosValle);
        double valorPicoPM = maximo(intervalosPicoPM);
        double valorCierre = maximo(intervalosCierre);
        Intervalos intervalos = incluirIntervalos(servicio, tablaMaestraServicios, valorInicio, valorPicoAm, valorValle, valorPicoPM, valorCierre, ProcessorUtils.CALCULO_MAXIMO);
        return intervalos;
    }

    private Intervalos incluirIntervalos(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, double valorInicio, double valorPicoAm, double valorValle, double valorPicoPM, double valorCierre, String calculoMaximo) {
        int busesInicio = calcularBuses(valorInicio);
        int busesAM = calcularBuses(valorPicoAm);
        int busesValle = calcularBuses(valorValle);
        int busesPm = calcularBuses(valorPicoPM);
        int busesCierre = calcularBuses(valorCierre);
        Intervalos intervalos = new Intervalos(calculoMaximo, valorInicio, valorPicoAm, valorValle, valorPicoPM, valorCierre, servicio, tablaMaestraServicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;
    }

    private Intervalos intervaloMinimo(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios) {
        double valorInicio = minimo(intervalosInicio);
        double valorPicoAm = minimo(intervalosPicoAM);
        double valorValle = minimo(intervalosValle);
        double valorPicoPM = minimo(intervalosPicoPM);
        double valorCierre = minimo(intervalosCierre);
        Intervalos intervalos = incluirIntervalos(servicio, tablaMaestraServicios, valorInicio, valorPicoAm, valorValle, valorPicoPM, valorCierre, ProcessorUtils.CALCULO_MINIMO);
        return intervalos;
    }

    private Intervalos intervaloPromedio(ServicioTipoDia servicio,TablaMaestraServicios tablaMaestraServicio) {
        double promedioInicio = promedio( intervalosInicio);
        double promedioPicoAm = promedio(intervalosPicoAM);
        double promedioValle = promedio(intervalosValle);
        double promedioPicoPM = promedio(intervalosPicoPM);
        double promedioCierre = promedio(intervalosCierre);
        Intervalos intervalos = incluirIntervalos(servicio, tablaMaestraServicio, promedioInicio, promedioPicoAm, promedioValle, promedioPicoPM, promedioCierre, ProcessorUtils.CALCULO_PROMEDIO);
        return intervalos;
    }



    private void calcularValorPorIntervaloCuarto(List<IntervaloCuartos> tiemposServicio) {

        Map<IntervalosProgramacion, List<Long>> franjaCuartos = distribuirTiemposEIntervalos(tiemposServicio);

        intervalosInicio = new ArrayList<Double>();
        intervalosPicoAM = new ArrayList<Double>();
        intervalosValle = new ArrayList<Double>();
        intervalosPicoPM = new ArrayList<Double>();
        intervalosCierre = new ArrayList<Double>();


        Iterator it = franjaCuartos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<Long> valores = (List<Long>) pair.getValue();
            double valorFInal = calcularPromedio(valores);

            IntervalosProgramacion intervalo = (IntervalosProgramacion) pair.getKey();
            if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.INICIO) ){
                intervalosInicio.add(valorFInal);
            }else if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.PICO_AM)  ){
                intervalosPicoAM.add(valorFInal);
            }else if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.VALLE)  ){
                intervalosValle.add(valorFInal);
            }else if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.PICO_PM)  ){
                intervalosPicoPM.add(valorFInal);
            }else{
                intervalosCierre.add(valorFInal);
            }

        }
    }

    private Map<IntervalosProgramacion, List<Long>> distribuirTiemposEIntervalos(List<IntervaloCuartos> tiemposServicio) {
        Map<IntervalosProgramacion, List<Long>> franjaCuartos = new HashMap<IntervalosProgramacion, List<Long>>();
        //Intervalos Franja Inicio
        for(IntervaloCuartos cuartos: tiemposServicio){
            IntervalosProgramacion intervaloActual = cuartos.getIntervalo();
            if(cuartos.getDiferencia()!= null){
                if(franjaCuartos.get(intervaloActual)!= null){
                    franjaCuartos.get(intervaloActual).add(cuartos.getDiferencia());
                }else{
                    List<Long> valores =new ArrayList<Long>();
                    valores.add(cuartos.getDiferencia());
                    franjaCuartos.put(intervaloActual,valores);
                }
            }

        }
        return franjaCuartos;
    }

    private double calcularPromedio(List<Long> valores) {
        long suma = 0;
        for(Long valor: valores){
            suma = suma + valor;
        }
        suma = suma / valores.size();
       return ProcessorUtils.transformarAFormatoTiempo(new Time(suma));
    }


    private void calcularDiferenciaDeTiempo(List<IntervaloCuartos> tiemposServicio) {
        for(int x = 0; x<tiemposServicio.size(); x++){
            IntervaloCuartos intervaloCuartos = tiemposServicio.get(x);
            try{
                Long tiempoB =tiemposServicio.get(x-1).getInstante().getTime();
                intervaloCuartos.setDiferencia(  intervaloCuartos.getInstante().getTime() - tiempoB  );
                tiemposServicio.set(x,intervaloCuartos);
            }catch (Exception e){
               // Cuando es el primer Registro
            }

        }
    }

    private List<IntervaloCuartos> encontrarTiemposPorServicioBusesHora(ServicioTipoDia servicio) {
        List<IntervaloCuartos> tiemposPorServicio = new ArrayList<>();
        List<TempBusesHora> tablaHorario = tempBusesHoraDao.getTablaHorarioPorServicio(servicio.getServicio());
        for(TempBusesHora horario:tablaHorario){
            IntervaloCuartos cuartos = new IntervaloCuartos();
            cuartos.setInstante(horario.getInstante());
            cuartos.setIntervalo(obtenerIntervaloProg(horario.getInstante()));
            tiemposPorServicio.add(cuartos);
        }
        return tiemposPorServicio;
    }

    private List<IntervaloCuartos> encontrarTiemposPorServicio(ServicioTipoDia servicio) {
        List<IntervaloCuartos> tiemposPorServicio = new ArrayList<>();
        List<TempHorario> tablaHorario = horariosProvisionalServicio.getTablaHorarioPorServicio(servicio.getServicio());
        for(TempHorario horario:tablaHorario){
            IntervaloCuartos cuartos = new IntervaloCuartos();
            cuartos.setInstante(horario.getInstante());
            cuartos.setIntervalo(obtenerIntervaloProg(horario.getInstante()));
            tiemposPorServicio.add(cuartos);
        }
        return tiemposPorServicio;
    }

    private IntervalosProgramacion obtenerIntervaloProg(Time instante) {
        IntervalosProgramacion prog = estaEnEstaFranja(instante,intervalosFranjaInicio);
        if (prog == null) {
            prog = estaEnEstaFranja(instante,intervalosFranjaPicoAM);
            if(prog == null){
                prog = estaEnEstaFranja(instante,intervalosFranjaValle);
                if(prog == null){
                    prog = estaEnEstaFranja(instante,intervalosFranjaPicoPM);
                    if(prog == null){
                        prog = estaEnEstaFranja(instante,intervalosFranjaCierre);
                    }
                }
            }
        }
        return prog;
    }

    private IntervalosProgramacion estaEnEstaFranja(Time instante,List<IntervalosProgramacion> franja) {
        for(IntervalosProgramacion prog: franja){
            if(instante.after(prog.getInicio()) || instante.equals(prog.getInicio())){
                if(instante.before(prog.getFin()) || instante.equals(prog.getFin())){
                    return  prog;
                }
            }
        }
        return null;
    }



    private double maximo(List<Double> tiemposFranjaInciio) {
        double mayor = 0.0;
        for(Double tiempo:tiemposFranjaInciio){
            if(mayor < tiempo){
                mayor = tiempo;
            }
        }
        return mayor;
    }


    private double minimo(List<Double> tiemposFranjaInciio) {
        double menor = 0;
        if(tiemposFranjaInciio.size()>0){
            menor = tiemposFranjaInciio.get(0);
            for(Double tiempo:tiemposFranjaInciio){
                if(menor >tiempo){
                    menor = tiempo;
                }
            }
        }
        return menor;
    }


    private int calcularBuses(double promedioInicio) {
        if(promedioInicio!=0){
            return (int) ((60/promedioInicio)+1);
        }
        return 0;
    }

    private double promedio(List<Double> tiempos) {
        double tiempoTotal=0;
        int i=0;
        for (Double tiempoInt:tiempos ) {
          tiempoTotal= tiempoTotal+ tiempoInt;
            i++;
        }
        if(tiempoTotal!=0){
            tiempoTotal= tiempoTotal/tiempos.size();
           return ProcessorUtils.round(tiempoTotal,2);
        }


        return ProcessorUtils.round(tiempoTotal,2);
    }


    public List<IntervalosProgramacion> getIntervalosFranjaInicio() {
        return intervalosFranjaInicio;
    }

    public void setIntervalosFranjaInicio(List<IntervalosProgramacion> intervalosFranjaInicio) {
        this.intervalosFranjaInicio = intervalosFranjaInicio;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaPicoAM() {
        return intervalosFranjaPicoAM;
    }

    public void setIntervalosFranjaPicoAM(List<IntervalosProgramacion> intervalosFranjaPicoAM) {
        this.intervalosFranjaPicoAM = intervalosFranjaPicoAM;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaValle() {
        return intervalosFranjaValle;
    }

    public void setIntervalosFranjaValle(List<IntervalosProgramacion> intervalosFranjaValle) {
        this.intervalosFranjaValle = intervalosFranjaValle;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaPicoPM() {
        return intervalosFranjaPicoPM;
    }

    public void setIntervalosFranjaPicoPM(List<IntervalosProgramacion> intervalosFranjaPicoPM) {
        this.intervalosFranjaPicoPM = intervalosFranjaPicoPM;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaCierre() {
        return intervalosFranjaCierre;
    }

    public void setIntervalosFranjaCierre(List<IntervalosProgramacion> intervalosFranjaCierre) {
        this.intervalosFranjaCierre = intervalosFranjaCierre;
    }
}

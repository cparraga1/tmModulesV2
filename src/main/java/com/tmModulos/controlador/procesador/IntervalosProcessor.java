package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.utils.ProcessorUtils;
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
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    @Autowired
    private TipoDiaService tipoDiaService;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    TipoFranja franjaIncio ;
    TipoFranja franjaPicoAM;
    TipoFranja franjaValle;
    TipoFranja franjaPicoPM;
    TipoFranja franjaCierre;
    List<IntervalosProgramacion> intervalosFranjaInicio;
    List<IntervalosProgramacion> intervalosFranjaPicoAM;
    List<IntervalosProgramacion> intervalosFranjaValle;
    List<IntervalosProgramacion> intervalosFranjaPicoPM;
    List<IntervalosProgramacion> intervalosFranjaCierre;



    public GisIntervalos generarIntervalos(Date fechaVigencia, String descripcion, String tipoDia, TablaMaestra tablaMaestra) {
        GisIntervalos gisIntervalos = null;
        // Traer valor cuadro por fecha de vigencia
        List<Vigencias> vigencias = tablaHorarioService.getVigenciasDaoByDate(fechaVigencia);
        if(vigencias.size()>0){

        // Traer servicios disponibles por tipo Dia
            TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
            List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);

            // Crear Gis intervalos
            gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,vigencias.get(0).getTipoDia(),dia,tablaMaestra);
            horariosProvisionalServicio.addGisIntervalo(gisIntervalos);


            long time=System.currentTimeMillis();
            List<HorarioS> tablaHorario = tablaHorarioService.getHorarioByDate(vigencias.get(0).getTipoDia());
            procesarInformacionTablaHorario(tablaHorario,gisIntervalos,serviciosTipoDia);

            System.out.println("Tiempo Total: "+(getTime((int) (System.currentTimeMillis()-time))));
        }
        return gisIntervalos;
    }

    private void procesarInformacionTablaHorario(List<HorarioS> tablaHorario,GisIntervalos gisIntervalos,List<ServicioTipoDia> servicioTipoDia) {

       extraerDiferenciaIntervalos(tablaHorario, servicioTipoDia, gisIntervalos);



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

    public void actualizarIntervalosTiempo(){

    }



    public List<Intervalos> calcularValorIntervaloPorFranja(TablaMaestraServicios tablaMaestraServicios,ServicioTipoDia servicio,GisIntervalos gisIntervalos) {
        precalcularIntervalosProgramacion();

               List<TiempoIntervalos> tiemposFranjaInciio = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaInicio,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaAM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoAM,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaValle = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaValle,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaPM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoPM,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaCierre = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaCierre,servicio,gisIntervalos);

        List<Intervalos> intervalosLista = new ArrayList<>();
       intervalosLista.add( calcularPromedio(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
       intervalosLista.add( calcularModa(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
        intervalosLista.add(calcularMinimo(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
       intervalosLista.add( calcularMaximo(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));

return intervalosLista;
    }

    private Intervalos calcularMaximo(ServicioTipoDia servicio, List<TiempoIntervalos> tiemposFranjaInciio, List<TiempoIntervalos> tiemposFranjaAM, List<TiempoIntervalos> tiemposFranjaValle, List<TiempoIntervalos> tiemposFranjaCierre, List<TiempoIntervalos> tiemposFranjaPM, TablaMaestraServicios tablaMaestraServicios) {
        double valorInicio = maximo(tiemposFranjaInciio);
        double valorPicoAm = maximo(tiemposFranjaAM);
        double valorValle = maximo(tiemposFranjaValle);
        double valorPicoPM = maximo(tiemposFranjaPM);
        double valorCierre = maximo(tiemposFranjaCierre);
        int busesInicio= calcularBuses(valorInicio);
        int busesAM= calcularBuses(valorPicoAm);
        int busesValle= calcularBuses(valorValle);
        int busesPm= calcularBuses(valorPicoPM);
        int busesCierre= calcularBuses(valorCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_MAXIMO,valorInicio,valorPicoAm,valorValle,valorPicoPM,valorCierre,servicio,tablaMaestraServicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;

    }

    private double maximo(List<TiempoIntervalos> tiemposFranjaInciio) {
        if(tiemposFranjaInciio.size()>0){
            return transformarAFormatoTiempo(tiemposFranjaInciio.get(tiemposFranjaInciio.size()-1).getInstante());
        }
        return 0;
    }

    private Intervalos calcularMinimo(ServicioTipoDia servicio, List<TiempoIntervalos> tiemposFranjaInciio, List<TiempoIntervalos> tiemposFranjaAM, List<TiempoIntervalos> tiemposFranjaValle, List<TiempoIntervalos> tiemposFranjaCierre, List<TiempoIntervalos> tiemposFranjaPM, TablaMaestraServicios tablaMaestraServicios) {
        double valorInicio = minimo(tiemposFranjaInciio);
        double valorPicoAm = minimo(tiemposFranjaAM);
        double valorValle = minimo(tiemposFranjaValle);
        double valorPicoPM = minimo(tiemposFranjaPM);
        double valorCierre = minimo(tiemposFranjaCierre);
        int busesInicio= calcularBuses(valorInicio);
        int busesAM= calcularBuses(valorPicoAm);
        int busesValle= calcularBuses(valorValle);
        int busesPm= calcularBuses(valorPicoPM);
        int busesCierre= calcularBuses(valorCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_MINIMO,valorInicio,valorPicoAm,valorValle,valorPicoPM,valorCierre,servicio,tablaMaestraServicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;

    }

    private double minimo(List<TiempoIntervalos> tiemposFranjaInciio) {
        if(tiemposFranjaInciio.size()>0){
            return transformarAFormatoTiempo(tiemposFranjaInciio.get(0).getInstante());
        }
        return 0;
    }

    private Intervalos calcularModa(ServicioTipoDia servicio, List<TiempoIntervalos> tiemposFranjaInciio, List<TiempoIntervalos> tiemposFranjaAM, List<TiempoIntervalos> tiemposFranjaValle, List<TiempoIntervalos> tiemposFranjaCierre, List<TiempoIntervalos> tiemposFranjaPM, TablaMaestraServicios tablaMaestraServicios) {
        double valorInicio = moda(tiemposFranjaInciio);
        double valorPicoAm = moda(tiemposFranjaAM);
        double valorValle = moda(tiemposFranjaValle);
        double valorPicoPM = moda(tiemposFranjaPM);
        double valorCierre = moda(tiemposFranjaCierre);
        int busesInicio= calcularBuses(valorInicio);
        int busesAM= calcularBuses(valorPicoAm);
        int busesValle= calcularBuses(valorValle);
        int busesPm= calcularBuses(valorPicoPM);
        int busesCierre= calcularBuses(valorCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_MODA,valorInicio,valorPicoAm,valorValle,valorPicoPM,valorCierre,servicio,tablaMaestraServicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;
    }

    private double moda(List<TiempoIntervalos> tiemposFranjaInciio) {
        Map<Integer,Integer> servicios = new HashMap<>();

        for (TiempoIntervalos tiempoInt:tiemposFranjaInciio ) {
            if(servicios.containsKey(tiempoInt.getInstante())){
                servicios.replace(tiempoInt.getInstante(),servicios.get(tiempoInt.getInstante())+1);
            }else{
                servicios.put(tiempoInt.getInstante(),1);
            }
        }
        int repetidoMasVeces=0;
        int instante=0;
        for (Map.Entry<Integer, Integer> entry : servicios.entrySet())
        {
            if(repetidoMasVeces< entry.getValue()){
                repetidoMasVeces = entry.getValue();
                instante=entry.getKey();
            }
        }

        return transformarAFormatoTiempo(instante);
    }


    private Intervalos calcularPromedio(ServicioTipoDia id, List<TiempoIntervalos> tiemposFranjaInciio, List<TiempoIntervalos> tiemposFranjaAM, List<TiempoIntervalos> tiemposFranjaValle, List<TiempoIntervalos> tiemposFranjaCierre, List<TiempoIntervalos> tiemposFranjaPM,TablaMaestraServicios tservicios) {
        double promedioInicio = promedio(tiemposFranjaInciio);
        double promedioPicoAm = promedio(tiemposFranjaAM);
        double promedioValle = promedio(tiemposFranjaValle);
        double promedioPicoPM = promedio(tiemposFranjaPM);
        double promedioCierre = promedio(tiemposFranjaCierre);
        int busesInicio= calcularBuses(promedioInicio);
        int busesAM= calcularBuses(promedioPicoAm);
        int busesValle= calcularBuses(promedioValle);
        int busesPm= calcularBuses(promedioPicoPM);
        int busesCierre= calcularBuses(promedioCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_PROMEDIO,promedioInicio,promedioPicoAm,promedioValle,promedioPicoPM,promedioCierre,id,tservicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;
    }

    private int calcularBuses(double promedioInicio) {
        if(promedioInicio!=0){
            return (int) (60/promedioInicio);
        }
        return 0;
    }

    private double promedio(List<TiempoIntervalos> tiempos) {
        double tiempoTotal=0;
        int i=0;
        for (TiempoIntervalos tiempoInt:tiempos ) {
          tiempoTotal= tiempoTotal+ tiempoInt.getInstante();
            i++;
        }
        if(tiempoTotal!=0){
            tiempoTotal= tiempoTotal/i;
           return transformarAFormatoTiempo(tiempoTotal);
        }


        return tiempoTotal;
    }

    private double transformarAFormatoTiempo(double tiempoTotal) {
        Time time = getTime((int) tiempoTotal);
        SimpleDateFormat format = new SimpleDateFormat("mm.ss");
        String date = format.format(new Date(time.getTime()));
        if (date.split("")[0].equals("0")) {
            String [] fecha = date.split("");
            date = fecha[1]+fecha[2]+fecha[3]+fecha[4];
        }
        return Double.parseDouble(date);
    }



    private List<TiempoIntervalos> extraerDiferenciaIntervalos(List<HorarioS> tablaHorario,List<ServicioTipoDia> servicioTipoDia,GisIntervalos gisIntervalos) {
        List<TiempoIntervalos> tiempoIntervalosLista = new ArrayList<>();
        String cuadro = tablaHorario.get(0).getCuadro();
        HorarioS horarioA= tablaHorario.get(0);
        HorarioS horarioB= null;
        int macroA= horarioA.getMacro();
        int lineaA=horarioA.getLinea();
        int seccionA=horarioA.getSeccion();
        int puntoA=horarioA.getPunto();
        Time horaA= getTime(horarioA.getInstante());
        IntervalosProgramacion intervaloA= calcularIntervaloInstante(horaA);
        int macroB,lineaB,seccionB,puntoB;
        Time horaB;
        IntervalosProgramacion intervaloB=null;
        int aux=0;
        for (int i=1;i<tablaHorario.size();i++) {
            horarioB = tablaHorario.get(i);
            macroB=horarioB.getMacro();
            lineaB=horarioB.getLinea();
            seccionB=horarioB.getSeccion();
            puntoB=horarioB.getPunto();
            horaB=getTime(horarioB.getInstante());
            intervaloB=calcularIntervaloInstante(horaB);

            if( macroA==macroB && lineaA==lineaB && seccionA==seccionB && puntoA==puntoB){
                if(intervaloA.getId() == intervaloB.getId() ){
                    ServicioTipoDia  servicio = getServicioById(servicioTipoDia,macroB+"-"+lineaB+"-"+seccionB+"-"+puntoB);
                    if(servicio!=null){
                        int diferencia = calcularDiferencia(horarioA.getInstante(),horarioB.getInstante());
                        TiempoIntervalos tiempoIntervalos = new TiempoIntervalos(getTime(diferencia),servicio,intervaloB,diferencia,gisIntervalos);
                       if(tiempoIntervalosLista.size()<200){
                           tiempoIntervalosLista.add(tiempoIntervalos);
                       }else{
                           taskExecutor.execute(new IntervalosHilo(tiempoIntervalosLista));
                           tiempoIntervalosLista = new ArrayList<>();
                       }


                    }
                    aux++;
                }else{
                    horarioA=horarioB;
                    macroA=macroB;
                    lineaA=lineaB;
                    seccionA=seccionB;
                    puntoA=puntoB;
                    horaA=horaB;
                    intervaloA=intervaloB;
                    aux=0;
                }
            }else{
                horarioA=horarioB;
                macroA=macroB;
                lineaA=lineaB;
                seccionA=seccionB;
                puntoA=puntoB;
                horaA=horaB;
                intervaloB=intervaloA;
                aux=0;
            }
        }
        return tiempoIntervalosLista;
    }

    private ServicioTipoDia getServicioById(List<ServicioTipoDia> servicioTipoDia, String id) {
        for (ServicioTipoDia servicio: servicioTipoDia ) {
            if(servicio.getIdentificador().equals(id)){
                return servicio;
            }
        }
        return null;
    }


    private Time getTime(int instante) {
        int hor = instante / 3600;
        int min=(instante-(3600*hor))/60;
        int seg=instante-((hor*3600)+(min*60));
        Time time= new Time(hor,min,seg);
        return time;
    }

    private int calcularDiferencia(int horaA, int horaB) {
       int diferencia= horaB-horaA;
        return diferencia;
    }

    private IntervalosProgramacion calcularIntervaloInstante(Time time){
        IntervalosProgramacion intervalosProgramacion = horariosProvisionalServicio.getIntervaloForDate(time);
        return intervalosProgramacion;
    }





    public TablaHorarioService getTablaHorarioService() {
        return tablaHorarioService;
    }

    public void setTablaHorarioService(TablaHorarioService tablaHorarioService) {
        this.tablaHorarioService = tablaHorarioService;
    }
}

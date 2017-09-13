package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.servicios.VeriPreHorarios;
import com.tmModulos.controlador.utils.FranjaCuartos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.dao.tmData.HoraFranjaDao;
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
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    @Autowired
    private VeriPreHorarios veriPreHorarios;

    @Autowired
    private TipoDiaService tipoDiaService;

    @Autowired
    public TempPosDao tempHorarioDao;


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
    List<HoraFranja> horaFranjaInicio;
    List<HoraFranja> horaFranjaPicoAM;
    List<HoraFranja> horaFranjaValle;
    List<HoraFranja> horaFranjaPicoPM;
    List<HoraFranja> horaFranjaCierre;



    public GisIntervalos generarIntervalos(Date fechaVigencia, String descripcion,
                                           String tipoDia, TablaMaestra tablaMaestra,
                                           TipoDia servicio) {
        GisIntervalos gisIntervalos = null;
        // Traer valor cuadro por fecha de vigencia
//        List<Vigencias> vigencias = tablaHorarioService.getVigenciasDaoByDate(fechaVigencia);
//        if(vigencias.size()>0){
//
//        // Traer servicios disponibles por tipo Dia
//            TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
//            List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);
//
//            // Crear Gis intervalos
//            gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,vigencias.get(0).getTipoDia(),dia,tablaMaestra);
//            horariosProvisionalServicio.addGisIntervalo(gisIntervalos);
//
//
//            long time=System.currentTimeMillis();
//            List<HorarioS> tablaHorario = tablaHorarioService.getHorarioByDate(vigencias.get(0).getTipoDia());
//            procesarInformacionTablaHorario(tablaHorario,gisIntervalos,serviciosTipoDia);
//
//            System.out.println("Tiempo Total: "+(getTime((int) (System.currentTimeMillis()-time))));
//        }


        gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,"Habil",servicio,tablaMaestra);
        horariosProvisionalServicio.addGisIntervalo(gisIntervalos);
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

//        horaFranjaInicio = horariosProvisionalServicio.getHoraByFranja(franjaIncio);
//        horaFranjaPicoAM = horariosProvisionalServicio.getHoraByFranja(franjaPicoAM);
//        horaFranjaValle = horariosProvisionalServicio.getHoraByFranja(franjaValle);
//        horaFranjaPicoPM = horariosProvisionalServicio.getHoraByFranja(franjaPicoPM);
//        horaFranjaCierre = horariosProvisionalServicio.getHoraByFranja(franjaCierre);

    }

    public HoraFranja encontrarHoraFranjaMenor(List<HoraFranja> horaFranja,String idServicio){

        Map<HoraFranja,Double> intervalosPorHora = new HashMap<HoraFranja,Double>();
        for( HoraFranja horaF : horaFranja ){
            List<TempHorario> horarios= horariosProvisionalServicio.getListHorario(idServicio, horaF.getHoraInicio(), horaF.getHoraFin());
            Double sumaDiff = 0.0;
            if(horarios.size()>1){
                Time horarioA = horarios.get(0).getInstante();
                Time horarioB = null;
                for(int i=1;i<horarios.size();i++){
                    horarioB = horarios.get(i).getInstante();
                    sumaDiff = sumaDiff + getDiferencia(horarioA,horarioB);
                    horarioA = horarioB;
                }
                if(sumaDiff==0){
                    System.out.println("entre");
                }
                intervalosPorHora.put(horaF,sumaDiff);
            }

        }
        //Ordenar Mapa
//        Map<HoraFranja, Time> treeMap = new TreeMap<HoraFranja, Time>(intervalosPorHora);

//        Time min = Collections.min(intervalosPorHora.values());
        if(intervalosPorHora.size()>0){
            Double menor = intervalosPorHora.get(intervalosPorHora.keySet().iterator().next());
            HoraFranja key = null;
            for(Map.Entry<HoraFranja,Double> entry : intervalosPorHora.entrySet()) {
                key = entry.getKey();
                Double value = entry.getValue();
                if(value<menor){
                    menor = value;
                }

            }
            return key;
        }


        return null;

    }

    private double getDiferencia(Time horarioA, Time horarioB) {
        long c = horarioA.getTime() - horarioB.getTime();
        return c/1000;
    }


    public List<Intervalos> calcularValorIntervaloPorFranja(TablaMaestraServicios tablaMaestraServicios,ServicioTipoDia servicio,GisIntervalos gisIntervalos) {


        //Buscar Menor hora de la franja
//        HoraFranja horaFranjaInicio = encontrarHoraFranjaMenor(this.horaFranjaInicio, servicio.getServicio().getIdentificador());
//        HoraFranja horaFranjaPicoAM = encontrarHoraFranjaMenor(this.horaFranjaPicoAM, servicio.getServicio().getIdentificador());
//        HoraFranja horaFranjaValle = encontrarHoraFranjaMenor(this.horaFranjaValle, servicio.getServicio().getIdentificador());
//        HoraFranja horaFranjaPicoPM = encontrarHoraFranjaMenor(this.horaFranjaPicoPM, servicio.getServicio().getIdentificador());
//        HoraFranja horaFranjaCierre = encontrarHoraFranjaMenor(this.horaFranjaCierre, servicio.getServicio().getIdentificador());

        List<TempHorario> tiemposFranjaInciio = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaInicio,servicio);
        List<TempHorario> tiemposFranjaAM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoAM,servicio);
        List<TempHorario> tiemposFranjaValle = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaValle,servicio);
        List<TempHorario> tiemposFranjaPM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoPM,servicio);
        List<TempHorario> tiemposFranjaCierre = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaCierre,servicio);

        List<Intervalos> intervalosLista = new ArrayList<>();
//        intervalosLista.add(getMenorInstante(servicio.getServicio().getIdentificador(),horaFranjaInicio,horaFranjaPicoAM,
//                horaFranjaValle,horaFranjaPicoPM,horaFranjaCierre,servicio,tablaMaestraServicios));

        List<Double> diffFranjaInicio = extraerValorTiempoPorCuartos(tiemposFranjaInciio,intervalosFranjaInicio);
        List<Double> diffFranjaAM = extraerValorTiempoPorCuartos(tiemposFranjaAM,intervalosFranjaPicoAM);
        List<Double> diffFranjaIValle = extraerValorTiempoPorCuartos(tiemposFranjaValle,intervalosFranjaValle);
        List<Double> diffFranjaPM = extraerValorTiempoPorCuartos(tiemposFranjaPM,intervalosFranjaPicoPM);
        List<Double> diffFranjaCierre = extraerDiferenciaTiempos(tiemposFranjaCierre);

       intervalosLista.add( calcularPromedio(servicio,diffFranjaInicio,diffFranjaAM,diffFranjaIValle,diffFranjaCierre,diffFranjaPM,tablaMaestraServicios));
//       intervalosLista.add( calcularModa(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
        intervalosLista.add(calcularMinimo(servicio,diffFranjaInicio,diffFranjaAM,diffFranjaIValle,diffFranjaCierre,diffFranjaPM,tablaMaestraServicios));
       intervalosLista.add( calcularMaximo(servicio,diffFranjaInicio,diffFranjaAM,diffFranjaIValle,diffFranjaCierre,diffFranjaPM,tablaMaestraServicios));

return intervalosLista;
    }




    public Intervalos getMenorInstante(String idServicio,HoraFranja horaFranjaInicio,HoraFranja hFPicoAM, HoraFranja hFValle, HoraFranja hFPicoPM, HoraFranja hFCierre,ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios){
        double valorInicio = transformarAFormatoTiempo(horariosProvisionalServicio.getMinInstanteByFranjaHora(idServicio, horaFranjaInicio, horaFranjaInicio));
        double valorPicoAM = transformarAFormatoTiempo(horariosProvisionalServicio.getMinInstanteByFranjaHora(idServicio, hFPicoAM, hFPicoAM));
        double valorValle = transformarAFormatoTiempo(horariosProvisionalServicio.getMinInstanteByFranjaHora(idServicio, hFValle, hFValle));
        double valorPicoPM = transformarAFormatoTiempo(horariosProvisionalServicio.getMinInstanteByFranjaHora(idServicio, hFPicoPM, hFPicoPM));
        double valorCierre = transformarAFormatoTiempo(horariosProvisionalServicio.getMinInstanteByFranjaHora(idServicio, hFCierre, hFCierre));
        int busesInicio= calcularBuses(valorInicio);
        int busesAM= calcularBuses(valorPicoAM);
        int busesValle= calcularBuses(valorValle);
        int busesPm= calcularBuses(valorPicoPM);
        int busesCierre= calcularBuses(valorCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_MINIMO,valorInicio,valorPicoAM,valorValle,valorPicoPM,valorCierre,servicio,tablaMaestraServicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;
    }

    public Intervalos getMayorInstante(String idServicio,HoraFranja horaFranjaInicio,HoraFranja hFPicoAM, HoraFranja hFValle, HoraFranja hFPicoPM, HoraFranja hFCierre,ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios){
        double valorInicio = transformarAFormatoTiempo(horariosProvisionalServicio.getMaxInstanteByFranjaHora(idServicio, horaFranjaInicio.getHoraInicio(), horaFranjaInicio.getHoraFin()));
        double valorPicoAM = transformarAFormatoTiempo(horariosProvisionalServicio.getMaxInstanteByFranjaHora(idServicio, hFPicoAM.getHoraInicio(), hFPicoAM.getHoraFin()));
        double valorValle = transformarAFormatoTiempo(horariosProvisionalServicio.getMaxInstanteByFranjaHora(idServicio, hFValle.getHoraInicio(), hFValle.getHoraFin()));
        double valorPicoPM = transformarAFormatoTiempo(horariosProvisionalServicio.getMaxInstanteByFranjaHora(idServicio, hFPicoPM.getHoraInicio(), hFPicoPM.getHoraFin()));
        double valorCierre = transformarAFormatoTiempo(horariosProvisionalServicio.getMaxInstanteByFranjaHora(idServicio, hFCierre.getHoraInicio(), hFCierre.getHoraFin()));
        int busesInicio= calcularBuses(valorInicio);
        int busesAM= calcularBuses(valorPicoAM);
        int busesValle= calcularBuses(valorValle);
        int busesPm= calcularBuses(valorPicoPM);
        int busesCierre= calcularBuses(valorCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_MAXIMO,valorInicio,valorPicoAM,valorValle,valorPicoPM,valorCierre,servicio,tablaMaestraServicios);
        intervalos.setBusesInicio(busesInicio);
        intervalos.setBusesAM(busesAM);
        intervalos.setBusesValle(busesValle);
        intervalos.setBusesPM(busesPm);
        intervalos.setBusesCierre(busesCierre);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;
    }

    private Intervalos calcularMaximo(ServicioTipoDia servicio, List<Double> tiemposFranjaInciio, List<Double> tiemposFranjaAM, List<Double> tiemposFranjaValle, List<Double> tiemposFranjaCierre, List<Double> tiemposFranjaPM, TablaMaestraServicios tablaMaestraServicios) {
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

    private double maximo(List<Double> tiemposFranjaInciio) {
        double mayor = 0.0;
        for(Double tiempo:tiemposFranjaInciio){
            if(mayor < tiempo){
                mayor = tiempo;
            }
        }
        return mayor;
    }

    private Intervalos calcularMinimo(ServicioTipoDia servicio, List<Double> tiemposFranjaInciio, List<Double> tiemposFranjaAM, List<Double> tiemposFranjaValle, List<Double> tiemposFranjaCierre, List<Double> tiemposFranjaPM, TablaMaestraServicios tablaMaestraServicios) {
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

//    private Intervalos calcularModa(ServicioTipoDia servicio, List<TempHorario> tiemposFranjaInciio, List<TempHorario> tiemposFranjaAM, List<TempHorario> tiemposFranjaValle, List<TempHorario> tiemposFranjaCierre, List<TempHorario> tiemposFranjaPM, TablaMaestraServicios tablaMaestraServicios) {
//        double valorInicio = moda(tiemposFranjaInciio);
//        double valorPicoAm = moda(tiemposFranjaAM);
//        double valorValle = moda(tiemposFranjaValle);
//        double valorPicoPM = moda(tiemposFranjaPM);
//        double valorCierre = moda(tiemposFranjaCierre);
//        int busesInicio= calcularBuses(valorInicio);
//        int busesAM= calcularBuses(valorPicoAm);
//        int busesValle= calcularBuses(valorValle);
//        int busesPm= calcularBuses(valorPicoPM);
//        int busesCierre= calcularBuses(valorCierre);
//        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_MODA,valorInicio,valorPicoAm,valorValle,valorPicoPM,valorCierre,servicio,tablaMaestraServicios);
//        intervalos.setBusesInicio(busesInicio);
//        intervalos.setBusesAM(busesAM);
//        intervalos.setBusesValle(busesValle);
//        intervalos.setBusesPM(busesPm);
//        intervalos.setBusesCierre(busesCierre);
//        horariosProvisionalServicio.addIntervalos(intervalos);
//        return intervalos;
//    }

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


    private Intervalos calcularPromedio(ServicioTipoDia id, List<Double> tiemposFranjaInciio, List<Double> tiemposFranjaAM, List<Double> tiemposFranjaValle, List<Double> tiemposFranjaCierre, List<Double> tiemposFranjaPM,TablaMaestraServicios tservicios) {
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

    private double promedio(List<Double> tiempos) {
        double tiempoTotal=0;
        int i=0;
        for (Double tiempoInt:tiempos ) {
          tiempoTotal= tiempoTotal+ tiempoInt;
            i++;
        }
        if(tiempoTotal!=0){
            tiempoTotal= tiempoTotal/tiempos.size();
           return tiempoTotal;
        }


        return tiempoTotal;
    }

    public List<Double> extraerDiferenciaTiempos(List<TempHorario> tiempos){
        List<Double> diferenciaTiempos = new ArrayList<>();
        double diff=0.0;
        if(tiempos.size()>0){
            TempHorario tempA= tiempos.get(0);
            for (int i=1; i< tiempos.size(); i++ ) {
                TempHorario tempB= tiempos.get(i);
                diff = getDiferencia(tempA.getInstante(),tempB.getInstante())/60;
                if(diff!=0){
                    diferenciaTiempos.add(diff);
                }
                tempA = tempB;
            }
        }

        return diferenciaTiempos;
    }

    // CLAVE PARA BUSES HORA
    public List<Double> extraerValorTiempoPorCuartos(List<TempHorario> tiempos, List<IntervalosProgramacion> intervalos){
        List<FranjaCuartos> franjaCuartos = new ArrayList<>();
        List<Double> diferenciaTiempos = new ArrayList<>();
        for (int i=0; i< intervalos.size(); i++ ) {
                FranjaCuartos franja = new FranjaCuartos();
                franja.setIntervalosProgramacion(intervalos.get(i));
                franja.setTablaHorario(encontrarValoresFranja(intervalos.get(i),tiempos));
                if(!franja.getTablaHorario().isEmpty()){
                    franja.setDiffTiempos(extraerDiferenciaTiempos(franja.getTablaHorario()));
                    franjaCuartos.add(franja);
                }

        }

        // AQUI PROMEDIO O NUMERO MENOR
        for( FranjaCuartos franjaCua : franjaCuartos){
            diferenciaTiempos.add(minimo(franjaCua.getDiffTiempos()));
        }

        return diferenciaTiempos;
    }

    private List<TempHorario> encontrarValoresFranja(IntervalosProgramacion intervalosProgramacion, List<TempHorario> tiempos) {
        List<TempHorario> horarioPorFranja = new ArrayList<>();
        for (int i=0; i< tiempos.size(); i++ ) {
            if(tiempos.get(i).getInstante().after(intervalosProgramacion.getInicio()) &&
                    tiempos.get(i).getInstante().before(intervalosProgramacion.getFin())){
                    horarioPorFranja.add(tiempos.get(i));
            }
        }
        return horarioPorFranja;
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

    private double transformarAFormatoTiempo(Time time) {
        if(time!=null){
            SimpleDateFormat format = new SimpleDateFormat("mm.ss");
            String date = format.format(new Date(time.getTime()));
            if (date.split("")[0].equals("0")) {
                String [] fecha = date.split("");
                date = fecha[1]+fecha[2]+fecha[3]+fecha[4];
            }
            return Double.parseDouble(date);
        }
        return 0.0;
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

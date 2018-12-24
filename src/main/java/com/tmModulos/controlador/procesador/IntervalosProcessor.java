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


    /*Se modifican para más franjas creadas*/

    private TipoFranja franjaPrimera ;
    private TipoFranja franjaSegunda;
    private TipoFranja franjaTercera;
    private TipoFranja franjaCuarta;
    private TipoFranja franjaQuinta;
    private TipoFranja franjaSexta ;
    private TipoFranja franjaSeptima;
    private TipoFranja franjaOctava;
    private TipoFranja franjaNovena;
    private TipoFranja franjaDecima;

    private List<IntervalosProgramacion> intervalosFranjaPrimera;
    private List<IntervalosProgramacion> intervalosFranjaSegunda;
    private List<IntervalosProgramacion> intervalosFranjaTercera;
    private List<IntervalosProgramacion> intervalosFranjaCuarta;
    private List<IntervalosProgramacion> intervalosFranjaQuinta;
    private List<IntervalosProgramacion> intervalosFranjaSexta;
    private List<IntervalosProgramacion> intervalosFranjaSeptima;
    private List<IntervalosProgramacion> intervalosFranjaOctava;
    private List<IntervalosProgramacion> intervalosFranjaNovena;
    private List<IntervalosProgramacion> intervalosFranjaDecima;

    Map<IntervalosProgramacion, Double> valoresFinalesPrimea;
    Map<IntervalosProgramacion, Double> valoresFinalesSegunda;
    Map<IntervalosProgramacion, Double> valoresFinalesTercera;
    Map<IntervalosProgramacion, Double> valoresFinalesCuarta;
    Map<IntervalosProgramacion, Double> valoresFinalesQuinta;
    Map<IntervalosProgramacion, Double> valoresFinalesSexta;
    Map<IntervalosProgramacion, Double> valoresFinalesSeptima;
    Map<IntervalosProgramacion, Double> valoresFinalesOctava;
    Map<IntervalosProgramacion, Double> valoresFinalesNovena;
    Map<IntervalosProgramacion, Double> valoresFinalesDecima;

    List<Double> intervalosPrimera;
    List<Double> intervalosSegunda;
    List<Double> intervalosTercera;
    List<Double> intervalosCuarta;
    List<Double> intervalosQuinta;
    List<Double> intervalosSexta;
    List<Double> intervalosSeptima;
    List<Double> intervalosOctava;
    List<Double> intervalosNovena;
    List<Double> intervalosDecima;

    /*private TipoFranja franjaIncio ;
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
    List<Double> intervalosCierre;*/



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

        franjaPrimera = horariosProvisionalServicio.getTipoFranjaByNombre("Primera");
        franjaSegunda = horariosProvisionalServicio.getTipoFranjaByNombre("Segunda");
        franjaTercera = horariosProvisionalServicio.getTipoFranjaByNombre("Tercera");
        franjaCuarta = horariosProvisionalServicio.getTipoFranjaByNombre("Cuarta");
        franjaQuinta = horariosProvisionalServicio.getTipoFranjaByNombre("Quinta");
        franjaSexta = horariosProvisionalServicio.getTipoFranjaByNombre("Sexta");
        franjaSeptima = horariosProvisionalServicio.getTipoFranjaByNombre("Septima");
        franjaOctava = horariosProvisionalServicio.getTipoFranjaByNombre("Octava");
        franjaNovena = horariosProvisionalServicio.getTipoFranjaByNombre("Novena");
        franjaDecima = horariosProvisionalServicio.getTipoFranjaByNombre("Decima");

        intervalosFranjaPrimera = horariosProvisionalServicio.getIntervaloByFranja(franjaPrimera);
        intervalosFranjaSegunda = horariosProvisionalServicio.getIntervaloByFranja(franjaSegunda);
        intervalosFranjaTercera = horariosProvisionalServicio.getIntervaloByFranja(franjaTercera);
        intervalosFranjaCuarta = horariosProvisionalServicio.getIntervaloByFranja(franjaCuarta);
        intervalosFranjaQuinta= horariosProvisionalServicio.getIntervaloByFranja(franjaQuinta);
        intervalosFranjaSexta = horariosProvisionalServicio.getIntervaloByFranja(franjaSexta);
        intervalosFranjaSeptima = horariosProvisionalServicio.getIntervaloByFranja(franjaSeptima);
        intervalosFranjaOctava = horariosProvisionalServicio.getIntervaloByFranja(franjaOctava);
        intervalosFranjaNovena = horariosProvisionalServicio.getIntervaloByFranja(franjaNovena);
        intervalosFranjaDecima = horariosProvisionalServicio.getIntervaloByFranja(franjaDecima);

        /*franjaIncio = horariosProvisionalServicio.getTipoFranjaByNombre("Inicio");
        franjaPicoAM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico AM");
        franjaValle = horariosProvisionalServicio.getTipoFranjaByNombre("Valle");
        franjaPicoPM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico PM");
        franjaCierre = horariosProvisionalServicio.getTipoFranjaByNombre("Cierre");

        intervalosFranjaInicio= horariosProvisionalServicio.getIntervaloByFranja(franjaIncio);
        intervalosFranjaPicoAM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoAM);
        intervalosFranjaValle= horariosProvisionalServicio.getIntervaloByFranja(franjaValle);
        intervalosFranjaPicoPM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoPM);
        intervalosFranjaCierre= horariosProvisionalServicio.getIntervaloByFranja(franjaCierre);*/

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


        double[] valores = new double[10];
        valores[0] = maximo(intervalosPrimera);
        valores[1] = maximo(intervalosSegunda);
        valores[2] = maximo(intervalosTercera);
        valores[3] = maximo(intervalosCuarta);
        valores[4] = maximo(intervalosQuinta);
        valores[5] = maximo(intervalosSexta);
        valores[6] = maximo(intervalosSeptima);
        valores[7] = maximo(intervalosOctava);
        valores[8] = maximo(intervalosNovena);
        valores[9] = maximo(intervalosDecima);

        /*double valorInicio = maximo(intervalosInicio);
        double valorPicoAm = maximo(intervalosPicoAM);
        double valorValle = maximo(intervalosValle);
        double valorPicoPM = maximo(intervalosPicoPM);
        double valorCierre = maximo(intervalosCierre);*/
        Intervalos intervalos = incluirIntervalos(servicio, tablaMaestraServicios, valores, ProcessorUtils.CALCULO_MAXIMO);
        return intervalos;
    }

    private Intervalos incluirIntervalos(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, double[] valores, String calculoMaximo) {

        int[] buses = new int[10];

        for(int i=0; i<buses.length; i++){
            buses[i] = calcularBuses(valores[i]);
        }

        /*int busesInicio = calcularBuses(valorInicio);
        int busesAM = calcularBuses(valorPicoAm);
        int busesValle = calcularBuses(valorValle);
        int busesPm = calcularBuses(valorPicoPM);
        int busesCierre = calcularBuses(valorCierre);*/

        Intervalos intervalos = new Intervalos(calculoMaximo, valores[0], valores[1], valores[2], valores[3], valores[4], valores[5], valores[6],valores[7],valores[8],valores[9], servicio, tablaMaestraServicios);

        intervalos.setBusesPrimera(buses[0]);
        intervalos.setBusesSegunda(buses[1]);
        intervalos.setBusesTercera(buses[2]);
        intervalos.setBusesCuarta(buses[3]);
        intervalos.setBusesQuinta(buses[4]);
        intervalos.setBusesSexta(buses[5]);
        intervalos.setBusesSeptima(buses[6]);
        intervalos.setBusesOctava(buses[7]);
        intervalos.setBusesNovena(buses[8]);
        intervalos.setBusesDecima(buses[9]);
        horariosProvisionalServicio.addIntervalos(intervalos);
        return intervalos;
    }

    private Intervalos intervaloMinimo(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios) {

        double[] valores = new double[10];
        valores[0] = maximo(intervalosPrimera);
        valores[1] = maximo(intervalosSegunda);
        valores[2] = maximo(intervalosTercera);
        valores[3] = maximo(intervalosCuarta);
        valores[4] = maximo(intervalosQuinta);
        valores[5] = maximo(intervalosSexta);
        valores[6] = maximo(intervalosSeptima);
        valores[7] = maximo(intervalosOctava);
        valores[8] = maximo(intervalosNovena);
        valores[9] = maximo(intervalosDecima);

        /*double valorInicio = minimo(intervalosPrimera);
        double valorPicoAm = minimo(intervalosSegunda);
        double valorValle = minimo(intervalosTercera);
        double valorPicoPM = minimo(intervalosCuarta);
        double valorCierre = minimo(intervalosQuinta);*/
        Intervalos intervalos = incluirIntervalos(servicio, tablaMaestraServicios, valores, ProcessorUtils.CALCULO_MINIMO);
        return intervalos;
    }

    private Intervalos intervaloPromedio(ServicioTipoDia servicio,TablaMaestraServicios tablaMaestraServicio) {

        double[] valores = new double[10];
        valores[0] = maximo(intervalosPrimera);
        valores[1] = maximo(intervalosSegunda);
        valores[2] = maximo(intervalosTercera);
        valores[3] = maximo(intervalosCuarta);
        valores[4] = maximo(intervalosQuinta);
        valores[5] = maximo(intervalosSexta);
        valores[6] = maximo(intervalosSeptima);
        valores[7] = maximo(intervalosOctava);
        valores[8] = maximo(intervalosNovena);
        valores[9] = maximo(intervalosDecima);

        /*double promedioInicio = promedio( intervalosInicio);
        double promedioPicoAm = promedio(intervalosPicoAM);
        double promedioValle = promedio(intervalosValle);
        double promedioPicoPM = promedio(intervalosPicoPM);
        double promedioCierre = promedio(intervalosCierre);*/

        Intervalos intervalos = incluirIntervalos(servicio, tablaMaestraServicio, valores, ProcessorUtils.CALCULO_PROMEDIO);
        return intervalos;
    }



    private void calcularValorPorIntervaloCuarto(List<IntervaloCuartos> tiemposServicio) {

        Map<IntervalosProgramacion, List<Long>> franjaCuartos = distribuirTiemposEIntervalos(tiemposServicio);

        intervalosPrimera = new ArrayList<Double>();
        intervalosSegunda = new ArrayList<Double>();
        intervalosTercera = new ArrayList<Double>();
        intervalosCuarta = new ArrayList<Double>();
        intervalosQuinta = new ArrayList<Double>();
        intervalosSexta = new ArrayList<Double>();
        intervalosSeptima = new ArrayList<Double>();
        intervalosOctava = new ArrayList<Double>();
        intervalosNovena = new ArrayList<Double>();
        intervalosDecima = new ArrayList<Double>();

        /*intervalosInicio = new ArrayList<Double>();
        intervalosPicoAM = new ArrayList<Double>();
        intervalosValle = new ArrayList<Double>();
        intervalosPicoPM = new ArrayList<Double>();
        intervalosCierre = new ArrayList<Double>();*/


        Iterator it = franjaCuartos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<Long> valores = (List<Long>) pair.getValue();
            double valorFInal = calcularPromedio(valores);

            IntervalosProgramacion intervalo = (IntervalosProgramacion) pair.getKey();
            if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.PRIMERA) ){
                intervalosPrimera.add(valorFInal);
            }else if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.SEGUNDA)  ){
                intervalosSegunda.add(valorFInal);
            }else if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.TERCERA)  ){
                intervalosTercera.add(valorFInal);
            }else if( intervalo.getTipoFranja().getNombre().equals(FranjaDef.CUARTA)  ){
                intervalosCuarta.add(valorFInal);
            }else{
                intervalosQuinta.add(valorFInal);
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
        IntervalosProgramacion prog = estaEnEstaFranja(instante,intervalosFranjaPrimera);
        if (prog == null) {
            prog = estaEnEstaFranja(instante,intervalosFranjaSegunda);
            if(prog == null){
                prog = estaEnEstaFranja(instante,intervalosFranjaTercera);
                if(prog == null){
                    prog = estaEnEstaFranja(instante,intervalosFranjaCuarta);
                    if(prog == null){
                        prog = estaEnEstaFranja(instante,intervalosFranjaQuinta);
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

    public List<IntervalosProgramacion> getIntervalosFranjaPrimera() {
        return intervalosFranjaPrimera;
    }

    public void setIntervalosFranjaPrimera(List<IntervalosProgramacion> intervalosFranjaPrimera) {
        this.intervalosFranjaPrimera = intervalosFranjaPrimera;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaSegunda() {
        return intervalosFranjaSegunda;
    }

    public void setIntervalosFranjaSegunda(List<IntervalosProgramacion> intervalosFranjaSegunda) {
        this.intervalosFranjaSegunda = intervalosFranjaSegunda;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaTercera() {
        return intervalosFranjaTercera;
    }

    public void setIntervalosFranjaTercera(List<IntervalosProgramacion> intervalosFranjaTercera) {
        this.intervalosFranjaTercera = intervalosFranjaTercera;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaCuarta() {
        return intervalosFranjaCuarta;
    }

    public void setIntervalosFranjaCuarta(List<IntervalosProgramacion> intervalosFranjaCuarta) {
        this.intervalosFranjaCuarta = intervalosFranjaCuarta;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaQuinta() {
        return intervalosFranjaQuinta;
    }

    public void setIntervalosFranjaQuinta(List<IntervalosProgramacion> intervalosFranjaQuinta) {
        this.intervalosFranjaQuinta = intervalosFranjaQuinta;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaSexta() {
        return intervalosFranjaSexta;
    }

    public void setIntervalosFranjaSexta(List<IntervalosProgramacion> intervalosFranjaSexta) {
        this.intervalosFranjaSexta = intervalosFranjaSexta;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaSeptima() {
        return intervalosFranjaSeptima;
    }

    public void setIntervalosFranjaSeptima(List<IntervalosProgramacion> intervalosFranjaSeptima) {
        this.intervalosFranjaSeptima = intervalosFranjaSeptima;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaOctava() {
        return intervalosFranjaOctava;
    }

    public void setIntervalosFranjaOctava(List<IntervalosProgramacion> intervalosFranjaOctava) {
        this.intervalosFranjaOctava = intervalosFranjaOctava;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaNovena() {
        return intervalosFranjaNovena;
    }

    public void setIntervalosFranjaNovena(List<IntervalosProgramacion> intervalosFranjaNovena) {
        this.intervalosFranjaNovena = intervalosFranjaNovena;
    }

    public List<IntervalosProgramacion> getIntervalosFranjaDecima() {
        return intervalosFranjaDecima;
    }

    public void setIntervalosFranjaDecima(List<IntervalosProgramacion> intervalosFranjaDecima) {
        this.intervalosFranjaDecima = intervalosFranjaDecima;
    }
}

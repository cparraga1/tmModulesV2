package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.*;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.PathFiles;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.dao.tmData.GisCargaDao;
import com.tmModulos.modelo.dao.tmData.IntervalosDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.faces.bean.ManagedProperty;
import javax.xml.bind.SchemaOutputResolver;
import java.io.InputStream;
import java.util.*;

@Service("TablaMaestraProcessor")
public class TablaMaestraProcessor {

    @Autowired
    GisCargaService gisCargaService;

    @Autowired
    MatrizDistanciaService matrizDistanciaService;

    @Autowired
    TablaMaestraService tablaMaestraService;

    @Autowired
    private IntervalosProcessor intervalosProcessor;

    @Autowired
    private TipoDiaService tipoDiaService;

    @Autowired
    private NodoService nodoService;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ProcessorUtils processorUtils;

    @Autowired
    private VeriPreHorarios veriPreHorarios;



    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    private List<LogDatos> logDatos;
    private static Logger log = Logger.getLogger(TablaMaestraServicios.class);
    private String destination= PathFiles.PATH_FOR_FILES+"\\Migracion\\";


    private Map serviciosIncluidos;


    public List<GisCarga> getGisCargaList(String modo) {
        return gisCargaService.getGisCargaByModo(modo);
    }

    public List<MatrizDistancia> getMatrizDistanciaAll(String modo) {
        return matrizDistanciaService.getMatrizDistanciaByModo(modo);
    }

    public GisCargaService getGisCargaService() {
        return gisCargaService;
    }

    public void setGisCargaService(GisCargaService gisCargaService) {
        this.gisCargaService = gisCargaService;
    }

    public MatrizDistanciaService getMatrizDistanciaService() {
        return matrizDistanciaService;
    }

    public void setMatrizDistanciaService(MatrizDistanciaService matrizDistanciaService) {
        this.matrizDistanciaService = matrizDistanciaService;
    }

    public List<LogDatos> calcularTablaMaestra(Date fechaDeProgramacion, String descripcion, String gisCarga, String matrizDistancia,
                                               Date fechaIntervalos,String tipoDia, String filename, InputStream in,String modo) {
        long tiempoIncial = System.currentTimeMillis();
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Tabla Maestra>>", TipoLog.INFO));
        log.info("<<Inicio Calculo Tabla Maestra>>");
        processorUtils.copyFile(filename,in,destination);
        destination=destination+filename;
        // Copiar informacion intervalos
        horariosProvisionalServicio.deleteTablaHorarioFromFile();
        horariosProvisionalServicio.addTablaHorarioFromFile(destination);


        //Encontrar parametros para la generacion de la tabla maestra
        GisCarga gis= gisCargaService.getGisCargaById(gisCarga);
        MatrizDistancia matriz= matrizDistanciaService.getMatrizDistanciaById(matrizDistancia);

        //Crear tabla maestra con los parametros encontrados
        TablaMaestra tablaMaestra = crearTablaMaestra(fechaDeProgramacion,new Date(),descripcion,gis,matriz,modo);
        tablaMaestra.setTipoDia(tipoDia);
        tablaMaestraService.addCustomer(tablaMaestra);

        // Traer servicios disponibles por tipo Dia
        TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
        if(dia!=null){
            List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);

            serviciosTipoDia = cleanServiciosTipoDia(serviciosTipoDia);

            //Calcular intervalos
            GisIntervalos gisIntervalos= generarIntervalosDeTiempo(fechaDeProgramacion,descripcion,tipoDia,tablaMaestra,dia);
            // intervalosProcessor.precalcularIntervalosProgramacion();

            for (ServicioTipoDia servicio: serviciosTipoDia) {


                //Encontrar nodo Inicio del servicio por el codigo
                // Nodo nodo = nodoService.getNodoByCodigo(servicio.getServicio().getPunto());
                GisServicio gisServicio = obtenerGisServicio(servicio, servicio.getServicio().getIdentificadorGIS());

                TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();
                tablaMaestraServicios= copiarInfoBasicaServicio(servicio, tablaMaestraServicios,tablaMaestra);


                if(gisServicio!=null){

                    //Obtener información de los arco tiempo del GIS de carga
                    List<ArcoTiempo> arcoTiempoRecords = gisCargaService.getArcoTiempoByGisCargaAndServicio(gis,gisServicio);
                    if(arcoTiempoRecords.size()>0){

                        ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);


                        //Obtener Nodo Inicio basado en la informacion del GIS de carga
                        Nodo nodoInicio = getNodoInicio(arcoTiempoBase.getServicio().getNodoIncial());
                        if(nodoInicio!=null){
                            tablaMaestraServicios = agregarInfoNodo(servicio.getServicio(), tablaMaestraServicios, nodoInicio);

                            Nodo nodoFinal = getNodoInicio(arcoTiempoBase.getServicio().getNodoFinal());
                            if(nodoFinal!=null){
                                tablaMaestraServicios =agregarInfoNodoFin(servicio.getServicio(),tablaMaestraServicios,nodoFinal);

                                //Calcular datos basicos matriz
                                tablaMaestraServicios.setTipoDia(arcoTiempoBase.getTipoDiaByArco().getTipoDia().getNombre());
                                tablaMaestraServicios.setSecuencia(arcoTiempoBase.getSecuencia());
                                tablaMaestraServicios= calcularDistancia(tablaMaestraServicios,nodoInicio,nodoFinal,matriz);

                                //Calcular ciclos
                                CicloServicio cicloServicio = calcularCiclos(tablaMaestraServicios,arcoTiempoRecords);
                                tablaMaestraServicios.setCicloServicio(cicloServicio);
                                VelocidadProgramada  velocidadProgramada = calcularVelocidadProgramada(cicloServicio,tablaMaestraServicios.getDistancia());
                                tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

                                HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio());
                                tablaMaestraServicios.setHorariosServicio(horariosServicio);

                                tablaMaestraService.addTServicios(tablaMaestraServicios);

                                //Calcular Intervalos de tiempo
                                intervalosProcessor.precalcularIntervalosProgramacion();
                                List<Intervalos> intervaloses = intervalosProcessor.calcularIntervalos(tablaMaestraServicios, servicio);
//                                 List<Intervalos> intervaloses = intervalosProcessor.calcularValorIntervaloPorFranja(tablaMaestraServicios, servicio,gisIntervalos);


                            }else{
                                log.warn("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoFinal()+" No encontrado");
                                logDatos.add(new LogDatos("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoFinal()+" No encontrado", TipoLog.WARN));
                                tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                                HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio());
                                tablaMaestraServicios.setHorariosServicio(horariosServicio);
                                tablaMaestraServicios.setIdentificadorb("0");
                                tablaMaestraServicios.setIdentificadorc("0");
                                tablaMaestraServicios.setSentido(1);
                                tablaMaestraService.addTServicios(tablaMaestraServicios);
                            }

                        }else{
                            log.warn("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoIncial()+" No encontrado");
                            logDatos.add(new LogDatos("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoIncial()+" No encontrado", TipoLog.WARN));
                            tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                            HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio());
                            tablaMaestraServicios.setHorariosServicio(horariosServicio);
                            tablaMaestraServicios.setIdentificadorb("0");
                            tablaMaestraServicios.setIdentificadorc("0");
                            tablaMaestraServicios.setSentido(1);
                            tablaMaestraService.addTServicios(tablaMaestraServicios);
                        }


                    }else{
                        servicioNoExisteEnGISCarga(servicio);

                    }
                }else{
                    tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);

                    HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio());
                    tablaMaestraServicios.setHorariosServicio(horariosServicio);
                    tablaMaestraServicios.setIdentificadorb("0");
                    tablaMaestraServicios.setIdentificadorc("0");
                    tablaMaestraServicios.setSentido(1);
                    tablaMaestraService.addTServicios(tablaMaestraServicios);
                    servicioNoExisteEnGISCarga(servicio);
                }
            }
        }


        veriPreHorarios.deleteTablaHorario();
        horariosProvisionalServicio.deleteTablaHorarioFromFile();
        logDatos.add(new LogDatos("<<Fin Calculo Tabla Maestra>>", TipoLog.INFO));
        log.info("<<Fin Calculo Tabla Maestra>>");
        tiempoIncial = System.currentTimeMillis() - tiempoIncial;
        log.info("Tiempo de procesamiento: "+ProcessorUtils.convertLongToTime(tiempoIncial));
        return logDatos;
    }

    private List<ServicioTipoDia> cleanServiciosTipoDia(List<ServicioTipoDia> serviciosTipoDia) {
        List<ServicioTipoDia> updateServicios= new ArrayList<>();
        for(ServicioTipoDia servicio: serviciosTipoDia){
            if(servicio.getServicio().isEstado()){
             updateServicios.add(servicio);
            }
        }

        return updateServicios;
    }

    public boolean crearServicioTablaMaestraDefinitiva(TablaMaestraServicios tablaMaestraServicios){
        tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
        Tipologia tipologiaByNombre = tablaMaestraService.getTipologiaByNombre(tablaMaestraServicios.getTipologiaAux());
        tablaMaestraServicios.setTipologia(tipologiaByNombre);
        tablaMaestraServicios.setHorariosServicio(definirHorarioServiciosVacio());
        tablaMaestraService.addTServicios(tablaMaestraServicios);
        return true;
    }


    public HorariosServicio definirHorarioServiciosVacio(){
        HorariosServicio horario= new HorariosServicio();
        tablaMaestraService.addHorariosServicios(horario);
        return horario;
    }

    public HorariosServicio calcularHorarioServicios(Servicio servicio) {

        List<Horario> horariosByServicio = tablaMaestraService.getHorariosByServicio(servicio);
        HorariosServicio horario = getHorariosServicio(horariosByServicio);
        tablaMaestraService.addHorariosServicios(horario);
        return horario;
    }

    private HorariosServicio getHorariosServicio(List<Horario> horariosByServicio) {
        HorariosServicio horario= new HorariosServicio();
        if( horariosByServicio.size()>0){
            for(Horario hr: horariosByServicio){
                if(hr.getTipoHorario().equals("P")){
                    if(hr.getConfig()==1){
                        horario.setHoraInicioProgA(hr.getHoraInicio());
                        horario.setHoraFinProgA(hr.getHoraFin());
                    }else if(hr.getConfig() ==2){
                        horario.setHoraInicioProgB(hr.getHoraInicio());
                        horario.setHoraFinProgB(hr.getHoraFin());
                    }
                }else {
                    if(hr.getConfig()==1){
                        horario.setHoraInicioUsuarioA(hr.getHoraInicio());
                        horario.setHoraFinUsuarioA(hr.getHoraFin());
                    }else if(hr.getConfig() ==2){
                        horario.setHoraInicioUsuarioB(hr.getHoraInicio());
                        horario.setHoraFinUsuarioB(hr.getHoraFin());
                    }
                }
            }
        }
        return horario;
    }

    public void actualizarHorarioServicios(Servicio servicio, TablaMaestraServicios tablaMaestraServicios){
        List<Horario> horariosByServicio = tablaMaestraService.getHorariosByServicio(servicio);
        HorariosServicio horario = getHorariosServicio(horariosByServicio);
        HorariosServicio oldHorario =  tablaMaestraServicios.getHorariosServicio();
        oldHorario.setHoraInicioProgA(horario.getHoraInicioProgA());
        oldHorario.setHoraFinProgA(horario.getHoraFinProgA());
        oldHorario.setHoraInicioProgB(horario.getHoraInicioProgB());
        oldHorario.setHoraFinProgB(horario.getHoraFinProgB());
        oldHorario.setHoraInicioUsuarioA(horario.getHoraInicioUsuarioA());
        oldHorario.setHoraFinUsuarioA(horario.getHoraFinUsuarioA());
        oldHorario.setHoraInicioUsuarioB(horario.getHoraInicioUsuarioB());
        oldHorario.setHoraFinUsuarioB(horario.getHoraFinUsuarioB());
        tablaMaestraService.updateHorariosServicios(oldHorario);

    }

    private VelocidadProgramada calcularVelocidadProgramada(CicloServicio cicloServicio, Integer distancia) {
        VelocidadProgramada velocidadProgramada = getVelocidadProgramada(cicloServicio, distancia);
        tablaMaestraService.addVelocidadProgramada(velocidadProgramada);
        return velocidadProgramada;
    }

    public void actualizarVelocidadProgramada(TablaMaestraServicios tablaMaestraServicios, CicloServicio cicloServicio, Integer distancia){
        VelocidadProgramada velocidadProgramada = getVelocidadProgramada(cicloServicio, distancia);
        VelocidadProgramada oldVelocidad = tablaMaestraServicios.getVelocidadProgramada();
        oldVelocidad.setOptimoAM(velocidadProgramada.getOptimoAM());
        oldVelocidad.setOptimoPM(velocidadProgramada.getOptimoPM());
        oldVelocidad.setOptimoValle(velocidadProgramada.getOptimoValle());
        oldVelocidad.setMinimoAM(velocidadProgramada.getMinimoAM());
        oldVelocidad.setMinimoPM(velocidadProgramada.getMinimoPM());
        oldVelocidad.setMinimoValle(velocidadProgramada.getMinimoValle());
        oldVelocidad.setMaximoAM(velocidadProgramada.getMaximoAM());
        oldVelocidad.setMaximoPM(velocidadProgramada.getMaximoPM());
        oldVelocidad.setMaximoValle(velocidadProgramada.getMaximoValle());
        tablaMaestraServicios.setVelocidadProgramada(oldVelocidad);

    }

    private VelocidadProgramada getVelocidadProgramada(CicloServicio cicloServicio, Integer distancia) {
        VelocidadProgramada  velocidadProgramada = new VelocidadProgramada();
        Integer distanciaKM;
        if(distancia!=-1){
            distanciaKM = distancia/1000;
            velocidadProgramada.setOptimoAM(calcularVelocidad(distanciaKM,cicloServicio.getOptimoAM()));
            velocidadProgramada.setOptimoPM(calcularVelocidad(distanciaKM,cicloServicio.getOptimoPM()));
            velocidadProgramada.setOptimoValle(calcularVelocidad(distanciaKM,cicloServicio.getOptimoValle()));
            velocidadProgramada.setMaximoAM(calcularVelocidad(distanciaKM,cicloServicio.getMaximoAM()));
            velocidadProgramada.setMaximoPM(calcularVelocidad(distanciaKM,cicloServicio.getMaximoPM()));
            velocidadProgramada.setMaximoValle(calcularVelocidad(distanciaKM,cicloServicio.getMaximoValle()));
            velocidadProgramada.setMinimoAM(calcularVelocidad(distanciaKM,cicloServicio.getMinimoAM()));
            velocidadProgramada.setMinimoPM(calcularVelocidad(distanciaKM,cicloServicio.getMinimoPM()));
            velocidadProgramada.setMinimoValle(calcularVelocidad(distanciaKM,cicloServicio.getMinimoValle()));
        }
        return velocidadProgramada;
    }

    private double calcularVelocidad(Integer distanciaKM, String optimoAM) {
        if(optimoAM!=null){
            String[] datos = optimoAM.split(":");
            double horas= Integer.parseInt(datos[0]);
            double minutos= Integer.parseInt(datos[1]);
            double segundos= Integer.parseInt(datos[2]);
            horas=horas + (minutos/60) + ( segundos/3600 );

            if(horas!=0){
                double velocidad =distanciaKM/horas;
             //   String velocidadFormateada = String.format("%.2f",velocidad);
                try{
                    return ProcessorUtils.round(velocidad,2);
                //    return Double.parseDouble(velocidadFormateada);
                }catch (Exception e){
                    logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
                    log.error(e.getMessage());
                }
                return 0;
            }

        }
        return 0;
    }

    private TablaMaestraServicios addCicloServicio(TablaMaestraServicios tablaMaestraServicios) {
        CicloServicio cicloServicio = new CicloServicio();
        tablaMaestraService.addCicloServicio(cicloServicio);
        tablaMaestraServicios.setCicloServicio(cicloServicio);

        VelocidadProgramada velocidadProgramada = new VelocidadProgramada();
        tablaMaestraService.addVelocidadProgramada(velocidadProgramada);
        tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

        return tablaMaestraServicios;
    }

    private TablaMaestraServicios copiarInfoBasicaServicio(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, TablaMaestra tablaMaestra) {
        //Copiar informacion del servicio
        tablaMaestraServicios.setTrayecto(servicio.getServicio().getTrayecto()+"");
        tablaMaestraServicios.setMacro(servicio.getServicio().getMacro());
        tablaMaestraServicios.setLinea(servicio.getServicio().getLinea());
        tablaMaestraServicios.setSeccion(servicio.getServicio().getSeccion());
        tablaMaestraServicios.setSentido(1);
        tablaMaestraServicios.setTipoServicio(servicio.getServicio().getTipoServicio());
        tablaMaestraServicios.setNombreEspecial(servicio.getServicio().getNombreEspecial());
        tablaMaestraServicios.setNombreGeneral(servicio.getServicio().getNombreGeneral());
        tablaMaestraServicios.setEstado(servicio.getServicio().isEstado());
        tablaMaestraServicios.setIdentificador(servicio.getServicio().getIdentificador());

        tablaMaestraServicios.setTablaMeestra(tablaMaestra);
        tablaMaestraServicios.setTipologia(servicio.getServicio().getTipologia());
        tablaMaestraServicios.setDistancia(-1);
        tablaMaestraServicios.setSecuencia(0);
        return tablaMaestraServicios;
    }

    private String calcularSegundoServicio(String identificador,int sentido,String nombreP) {

        String[] split = identificador.split("-");
        String nuevoId= split[0]+"-"+split[1]+"-"+sentido+"-"+nombreP;
        return nuevoId;
    }

    private String calcularTercerServicio(int linea, int sentido,int ruta, int cod) {
        String nuevoId= linea+"-"+sentido+"-"+ruta+"-"+cod;
        return nuevoId;
    }

    public TablaMaestraServicios agregarInfoNodo(Servicio servicio, TablaMaestraServicios tablaMaestraServicios, Nodo nodoInicio) {
        tablaMaestraServicios.setCodigoInicio(nodoInicio.getCodigo());
        tablaMaestraServicios.setNombreInicio(nodoInicio.getNombre());
        tablaMaestraServicios.setZonaTInicio(nodoInicio.getVagon().getEstacion().getZonaUsuario().getNombre());
        tablaMaestraServicios.setZonaPInicio(nodoInicio.getVagon().getEstacion().getZonaProgramacion().getNombre());
        tablaMaestraServicios.setIdInicio(calcularId(servicio,nodoInicio.getCodigo()));
        tablaMaestraServicios.setIdentificadorb(calcularSegundoServicio(servicio.getIdentificador(),servicio.getSeccion(),nodoInicio.getNombre()));
        tablaMaestraServicios.setIdentificadorc("111");

        return  tablaMaestraServicios;
    }

    public TablaMaestraServicios agregarInfoNodoFin(Servicio servicio, TablaMaestraServicios tablaMaestraServicios, Nodo nodoFinal) {
        tablaMaestraServicios.setCodigoFin(nodoFinal.getCodigo());
        tablaMaestraServicios.setNombreIFin(nodoFinal.getNombre());
        tablaMaestraServicios.setZonaTFin(nodoFinal.getVagon().getEstacion().getZonaUsuario().getNombre());
        tablaMaestraServicios.setZonaPFin(nodoFinal.getVagon().getEstacion().getZonaProgramacion().getNombre());
        tablaMaestraServicios.setIdFin(calcularId(servicio,nodoFinal.getCodigo()));
        return  tablaMaestraServicios;
    }

    private void servicioNoExisteEnGISCarga(ServicioTipoDia servicio) {
        log.warn("Servicio no encontrado en el GIS de carga ");
        log.warn("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto());
        logDatos.add(new LogDatos("Servicio no encontrado en el GIS de carga ", TipoLog.WARN));
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto(), TipoLog.WARN));
    }

    // Obtener el servicio asociado en el GIS de Carga a partir de la linea, trayecto y nodo
    private GisServicio obtenerGisServicio(ServicioTipoDia servicio, String identificador) {
        GisServicio gisServicio=null;
        if(identificador!=null){
            gisServicio=gisCargaService.getGisServicioByTrayectoLinea(identificador);
        }else{
            log.error("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador());
            logDatos.add(new LogDatos("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador(), TipoLog.ERROR));
        }
        return gisServicio;
    }

    public Nodo getNodoInicio(String nodoIncial) {
       Nodo nodo = nodoService.getNodo(nodoIncial);
       return nodo;
    }

    private GisIntervalos generarIntervalosDeTiempo(Date fechaIntervalos,String descripcion, String tipoDia, TablaMaestra tablaMaestra, TipoDia servicio) {
       return intervalosProcessor.generarIntervalos(fechaIntervalos,descripcion,tipoDia,tablaMaestra,servicio);
    }

    //Calcular ciclos de tiempos de recorrido - en base al GIS de carga
    private CicloServicio calcularCiclos(TablaMaestraServicios tablaMaestraServicios, List<ArcoTiempo> arcoTiempoRecords) {
        CicloServicio cicloServicio = getCicloServicio(arcoTiempoRecords);
        tablaMaestraService.addCicloServicio(cicloServicio);
        return cicloServicio;
    }

    public void actualizarCicloServicio (TablaMaestraServicios tablaMaestraServicios, List<ArcoTiempo> arcoTiempoRecords){
        CicloServicio cicloServicio = getCicloServicio(arcoTiempoRecords);
        CicloServicio oldCicloServicio = tablaMaestraServicios.getCicloServicio();
        oldCicloServicio.setMinimoAM(cicloServicio.getMinimoAM());
        oldCicloServicio.setMaximoInicio(cicloServicio.getMinimoInicio());
        oldCicloServicio.setMinimoValle(cicloServicio.getMinimoValle());
        oldCicloServicio.setMinimoPM(cicloServicio.getMinimoPM());
        oldCicloServicio.setMinimoCierre(cicloServicio.getMinimoCierre());
        oldCicloServicio.setMaximoInicio(cicloServicio.getMaximoInicio());
        oldCicloServicio.setMaximoAM(cicloServicio.getMaximoAM());
        oldCicloServicio.setMaximoValle(cicloServicio.getMaximoValle());
        oldCicloServicio.setMaximoPM(cicloServicio.getMaximoPM());
        oldCicloServicio.setMaximoCierre(cicloServicio.getMaximoCierre());
        oldCicloServicio.setOptimoInicio(cicloServicio.getOptimoInicio());
        oldCicloServicio.setOptimoAM(cicloServicio.getOptimoAM());
        oldCicloServicio.setOptimoValle(cicloServicio.getOptimoValle());
        oldCicloServicio.setOptimoPM(cicloServicio.getOptimoPM());
        oldCicloServicio.setOptimoCierre(cicloServicio.getOptimoCierre());
        tablaMaestraService.updateCicloServicio(oldCicloServicio);

    }

    private CicloServicio getCicloServicio(List<ArcoTiempo> arcoTiempoRecords) {
        CicloServicio cicloServicio = new CicloServicio();
        for (ArcoTiempo arcoTiempo: arcoTiempoRecords){
            String horaInicio = arcoTiempo.getHoraDesde();
            String horaFin = arcoTiempo.getHoraHasta();
            horaFin = validarHoraFin(horaFin);
            TipoFranja tipoFranja = tablaMaestraService.getTipoFranjaByHorario(horaInicio,horaFin);
            if(tipoFranja!=null){
                if(tipoFranja.getNombre().equals("Inicio")){
                    cicloServicio.setMinimoInicio(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoInicio(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoInicio(arcoTiempo.getTiempoOptimo());
                }else if(tipoFranja.getNombre().equals("Pico AM")){
                    cicloServicio.setMinimoAM(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoAM(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoAM(arcoTiempo.getTiempoOptimo());
                }else if(tipoFranja.getNombre().equals("Pico PM")){
                    cicloServicio.setMinimoPM(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoPM(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoPM(arcoTiempo.getTiempoOptimo());
                }else if(tipoFranja.getNombre().equals("Valle")){
                    cicloServicio.setMinimoValle(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoValle(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoValle(arcoTiempo.getTiempoOptimo());
                }else {
                    cicloServicio.setMinimoCierre(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoCierre(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoCierre(arcoTiempo.getTiempoOptimo());
                }

            }else{
                System.out.println("Tipo de franja no existente");
            }

        }
        return cicloServicio;
    }

    private String validarHoraFin(String horaFin) {

        if(horaFin.equals("24:00:00") || horaFin.equals("25:00:00") || horaFin.equals("26:00:00")
                || horaFin.equals("27:00:00") || horaFin.equals("28:00:00") || horaFin.equals("29:00:00")
                || horaFin.equals("30:00:00")){
            return "00:00:00";
        }
        return horaFin;
    }

    public TablaMaestraServicios calcularDistancia(TablaMaestraServicios tablaMaestraServicios, Nodo nodoIni,Nodo nodoFin, MatrizDistancia matrizDistancia) {

        int macro = tablaMaestraServicios.getMacro();
        int linea = tablaMaestraServicios.getLinea();
        int seccion = tablaMaestraServicios.getSeccion();
        int seccionAux = 4;
        boolean calculoEspecialInicio=false;
        boolean calculoEspecialFin=false;

        // Obtener servicio asociado a la matriz de Distancia
        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        ServicioDistancia servicioDistanciaAux=null ;

        if (servicioDistancia!=null){
            DistanciaNodos distanciaNodosA= matrizDistanciaService.getDistanciaNodosByServicioAndNodo(servicioDistancia,matrizDistancia,nodoIni.getCodigo()+"");
            DistanciaNodos distanciaNodosB= matrizDistanciaService.getDistanciaNodosByServicioAndNodo(servicioDistancia,matrizDistancia,nodoFin.getCodigo()+"");

            if( distanciaNodosA==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosA= matrizDistanciaService.getDistanciaNodosByServicioAndNodo(servicioDistanciaAux,matrizDistancia,nodoIni.getCodigo()+"");
                    calculoEspecialInicio =true;
                }else{
                    serviciosNoEncontradosMatriz(macro, linea, seccion);
                }

            }
            if(distanciaNodosB==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux  = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosB= matrizDistanciaService.getDistanciaNodosByServicioAndNodo(servicioDistanciaAux,matrizDistancia,nodoFin.getCodigo()+"");
                    calculoEspecialFin =true;
                }else{
                    serviciosNoEncontradosMatriz(macro, linea, seccion);
                }

            }
            if( distanciaNodosA!=null ){
                if(distanciaNodosB!=null){
                    int disFinal=distanciaNodosB.getDistancia();
                    if(calculoEspecialInicio){
                        disFinal= calcularDistanciaEspecial(seccion,distanciaNodosA,distanciaNodosB,servicioDistancia,servicioDistanciaAux,matrizDistancia);
                    }else if(calculoEspecialFin){
                        disFinal= calcularDistanciaEspecial(seccion,distanciaNodosA,distanciaNodosB,servicioDistancia,servicioDistanciaAux,matrizDistancia);
                    }
                    tablaMaestraServicios.setDistancia(disFinal-distanciaNodosA.getDistancia());
                    tablaMaestraServicios.setMatrizNombre(servicioDistancia.getRuta());
                }else{
                    noHayValoresenMatriz(nodoFin, servicioDistancia);
                    tablaMaestraServicios.setDistancia(-1);
                    return tablaMaestraServicios;
                }

            }else{
                noHayValoresenMatriz(nodoIni, servicioDistancia);
                tablaMaestraServicios.setDistancia(-1);
                return tablaMaestraServicios;
            }

        }else{
            serviciosNoEncontradosMatriz(macro, linea, seccion);
            tablaMaestraServicios.setDistancia(-1);
            return tablaMaestraServicios;
        }

        return tablaMaestraServicios;
    }

    private void noHayValoresenMatriz(Nodo nodoFin, ServicioDistancia servicioDistancia) {
        log.warn("Error en la busqueda de la matriz de distancia");
        log.warn("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
        +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre());
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
                +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre(), TipoLog.WARN));
    }

    private void serviciosNoEncontradosMatriz(int macro, int linea, int seccion) {
        log.warn("Servicio no encontrado en la Matriz de Distancias con Macro "+macro+" Linea "+linea+" Seccion "+seccion);
        logDatos.add(new LogDatos("Servicio no encontrado en en la Matriz de Distancias con Macro "+macro+" Linea "+linea+" Seccion "+seccion, TipoLog.WARN));
    }

    private int getSeccionAux(int seccion) {
        int seccionAux;
        if(seccion==1){
            seccionAux=3;
        }else{
            seccionAux=4;
        }
        return seccionAux;
    }

    private int calcularDistanciaEspecial(int seccion, DistanciaNodos distanciaNodosA, DistanciaNodos distanciaNodosB,ServicioDistancia servicioDistancia, ServicioDistancia servicioDistanciaAux,MatrizDistancia matrizDistancia ) {
        DistanciaNodos ultimoNodoSeccion = matrizDistanciaService.getUltimoDistanciaNodosByServicioAndPunto(servicioDistancia,matrizDistancia);
     return   distanciaNodosB.getDistancia()+ultimoNodoSeccion.getDistancia();
    }

    private String calcularId(Servicio servicio, String codigo) {
        return servicio.getMacro()+"-"+servicio.getLinea()+"-"+servicio.getSeccion()+"-"+codigo;
    }

    private TablaMaestra crearTablaMaestra(Date fechaDeProgramacion, Date fechaCreacion, String descripcion, GisCarga gisCarga,
                                           MatrizDistancia matrizDistancia,String modo) {
       TablaMaestra tablaMaestra = new TablaMaestra(fechaCreacion,fechaDeProgramacion,descripcion,matrizDistancia,gisCarga,modo);
        tablaMaestra.setEsDefinitiva(true);
        return tablaMaestra;
    }

    public List<LogDatos> getLogDatos() {
        return logDatos;
    }

    public void setLogDatos(List<LogDatos> logDatos) {
        this.logDatos = logDatos;
    }

    public List<LogDatos> copiarUltimaTablaMaestra(Date fechaDeProgramacion, String descripcion,String tipoDia,Date fechaCreacion,String modo) {
        TablaMaestra ultimaTablaMaestra = obtenerUltimaTablaMaestra(tipoDia,fechaCreacion,modo);
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Tabla Maestra>>", TipoLog.INFO));
        if(ultimaTablaMaestra!=null){
            copiarTabla(fechaDeProgramacion,descripcion,tipoDia,ultimaTablaMaestra);
        }else{
            logDatos.add(new LogDatos("<<No existe una tabla maestra previa>>", TipoLog.ERROR));
        }
        logDatos.add(new LogDatos("<<Fin Calculo Tabla Maestra>>", TipoLog.INFO));
        return logDatos;
    }

    private void copiarTabla(Date fechaDeProgramacion, String descripcion, String tipoDia, TablaMaestra ultimaTablaMaestra) {
        TablaMaestra nuevaTablaMaestra = new TablaMaestra();
        nuevaTablaMaestra.setFechaCreacion(new Date());
        nuevaTablaMaestra.setFechaVigencia(fechaDeProgramacion);
        nuevaTablaMaestra.setEsDefinitiva(false);
        nuevaTablaMaestra.setDescripcion(descripcion);
        nuevaTablaMaestra.setTipoDia(tipoDia);
        nuevaTablaMaestra.setGisCarga(ultimaTablaMaestra.getGisCarga());
        nuevaTablaMaestra.setMatrizDistancia(ultimaTablaMaestra.getMatrizDistancia());
        nuevaTablaMaestra.setModo(ultimaTablaMaestra.getModo());

        tablaMaestraService.addCustomer(nuevaTablaMaestra);

        List<TablaMaestraServicios> serviciosByTabla = tablaMaestraService.getServiciosByTabla(ultimaTablaMaestra);
        for(TablaMaestraServicios tablaMaestraServicios: serviciosByTabla){
            copiarInformacionTablaMaestraServicios(nuevaTablaMaestra, tablaMaestraServicios);

        }

    }

    private void copiarInformacionTablaMaestraServicios(TablaMaestra nuevaTablaMaestra, TablaMaestraServicios tablaMaestraServicios) {
        TablaMaestraServicios nuevaTablaMaestraServicios = new TablaMaestraServicios();
        nuevaTablaMaestraServicios.setDistancia(tablaMaestraServicios.getDistancia());
        nuevaTablaMaestraServicios.setSecuencia(tablaMaestraServicios.getSecuencia());
        nuevaTablaMaestraServicios.setIdentificador(tablaMaestraServicios.getIdentificador());
        nuevaTablaMaestraServicios.setIdInicio(tablaMaestraServicios.getIdInicio());
        nuevaTablaMaestraServicios.setIdFin(tablaMaestraServicios.getIdFin());
        nuevaTablaMaestraServicios.setTipoDia(tablaMaestraServicios.getTipoDia());
        nuevaTablaMaestraServicios.setMatrizNombre(tablaMaestraServicios.getMatrizNombre());
        nuevaTablaMaestraServicios.setTrayecto(tablaMaestraServicios.getTrayecto());
        nuevaTablaMaestraServicios.setMacro(tablaMaestraServicios.getMacro());
        nuevaTablaMaestraServicios.setLinea(tablaMaestraServicios.getLinea());
        nuevaTablaMaestraServicios.setSeccion(tablaMaestraServicios.getSeccion());
        nuevaTablaMaestraServicios.setTipoServicio(tablaMaestraServicios.getTipoServicio());
        nuevaTablaMaestraServicios.setNombreGeneral(tablaMaestraServicios.getNombreGeneral());
        nuevaTablaMaestraServicios.setNombreEspecial(tablaMaestraServicios.getNombreEspecial());
        nuevaTablaMaestraServicios.setEstado(tablaMaestraServicios.isEstado());
        nuevaTablaMaestraServicios.setCodigoInicio(tablaMaestraServicios.getCodigoInicio());
        nuevaTablaMaestraServicios.setCodigoFin(tablaMaestraServicios.getCodigoFin());
        nuevaTablaMaestraServicios.setZonaTInicio(tablaMaestraServicios.getZonaTInicio());
        nuevaTablaMaestraServicios.setZonaTFin(tablaMaestraServicios.getZonaTFin());
        nuevaTablaMaestraServicios.setZonaPInicio(tablaMaestraServicios.getZonaPInicio());
        nuevaTablaMaestraServicios.setZonaPFin(tablaMaestraServicios.getZonaPFin());
        nuevaTablaMaestraServicios.setNombreInicio(tablaMaestraServicios.getNombreInicio());
        nuevaTablaMaestraServicios.setNombreIFin(tablaMaestraServicios.getNombreIFin());
        nuevaTablaMaestraServicios.setTipologia(tablaMaestraServicios.getTipologia());
        nuevaTablaMaestraServicios.setIdentificadorb(tablaMaestraServicios.getIdentificadorb());
        nuevaTablaMaestraServicios.setIdentificadorc(tablaMaestraServicios.getIdentificadorc());
        nuevaTablaMaestraServicios.setSentido(tablaMaestraServicios.getSentido());

        CicloServicio cicloServicio = new CicloServicio();
        cicloServicio.setOptimoInicio(tablaMaestraServicios.getCicloServicio().getOptimoInicio());
        cicloServicio.setOptimoAM(tablaMaestraServicios.getCicloServicio().getOptimoAM());
        cicloServicio.setOptimoValle(tablaMaestraServicios.getCicloServicio().getOptimoValle());
        cicloServicio.setOptimoPM(tablaMaestraServicios.getCicloServicio().getOptimoPM());
        cicloServicio.setOptimoCierre(tablaMaestraServicios.getCicloServicio().getOptimoCierre());
        cicloServicio.setMaximoInicio(tablaMaestraServicios.getCicloServicio().getMaximoInicio());
        cicloServicio.setMaximoAM(tablaMaestraServicios.getCicloServicio().getMaximoAM());
        cicloServicio.setMaximoValle(tablaMaestraServicios.getCicloServicio().getMaximoValle());
        cicloServicio.setMaximoPM(tablaMaestraServicios.getCicloServicio().getMaximoPM());
        cicloServicio.setMaximoCierre(tablaMaestraServicios.getCicloServicio().getMaximoCierre());
        cicloServicio.setMinimoInicio(tablaMaestraServicios.getCicloServicio().getMinimoInicio());
        cicloServicio.setMinimoAM(tablaMaestraServicios.getCicloServicio().getMinimoAM());
        cicloServicio.setMinimoValle(tablaMaestraServicios.getCicloServicio().getMinimoValle());
        cicloServicio.setMinimoPM(tablaMaestraServicios.getCicloServicio().getMinimoPM());
        cicloServicio.setMinimoCierre(tablaMaestraServicios.getCicloServicio().getMinimoCierre());
        tablaMaestraService.addCicloServicio(cicloServicio);
        nuevaTablaMaestraServicios.setCicloServicio(cicloServicio);

        VelocidadProgramada velocidadProgramada = new VelocidadProgramada();
        velocidadProgramada.setMaximoAM(tablaMaestraServicios.getVelocidadProgramada().getMaximoAM());
        velocidadProgramada.setMaximoValle(tablaMaestraServicios.getVelocidadProgramada().getMaximoValle());
        velocidadProgramada.setMaximoPM(tablaMaestraServicios.getVelocidadProgramada().getMaximoPM());
        velocidadProgramada.setMinimoAM(tablaMaestraServicios.getVelocidadProgramada().getMinimoAM());
        velocidadProgramada.setMinimoPM(tablaMaestraServicios.getVelocidadProgramada().getMinimoPM());
        velocidadProgramada.setMinimoValle(tablaMaestraServicios.getVelocidadProgramada().getMinimoValle());
        velocidadProgramada.setOptimoAM(tablaMaestraServicios.getVelocidadProgramada().getOptimoAM());
        velocidadProgramada.setOptimoValle(tablaMaestraServicios.getVelocidadProgramada().getOptimoValle());
        velocidadProgramada.setOptimoPM(tablaMaestraServicios.getVelocidadProgramada().getOptimoPM());
        tablaMaestraService.addVelocidadProgramada(velocidadProgramada);
        nuevaTablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

        HorariosServicio horariosServicio = new HorariosServicio();
        horariosServicio.setHoraInicioProgA(tablaMaestraServicios.getHorariosServicio().getHoraInicioProgA());
        horariosServicio.setHoraFinProgA(tablaMaestraServicios.getHorariosServicio().getHoraFinProgA());
        horariosServicio.setHoraInicioProgB(tablaMaestraServicios.getHorariosServicio().getHoraInicioProgB());
        horariosServicio.setHoraFinProgB(tablaMaestraServicios.getHorariosServicio().getHoraFinProgB());
        horariosServicio.setHoraInicioUsuarioA(tablaMaestraServicios.getHorariosServicio().getHoraInicioUsuarioA());
        horariosServicio.setHoraFinUsuarioA(tablaMaestraServicios.getHorariosServicio().getHoraFinUsuarioA());
        horariosServicio.setHoraInicioUsuarioB(tablaMaestraServicios.getHorariosServicio().getHoraInicioUsuarioB());
        horariosServicio.setHoraFinUsuarioB(tablaMaestraServicios.getHorariosServicio().getHoraFinUsuarioB());
        tablaMaestraService.addHorariosServicios(horariosServicio);
        nuevaTablaMaestraServicios.setHorariosServicio(horariosServicio);

        nuevaTablaMaestraServicios.setTablaMeestra(nuevaTablaMaestra);
        tablaMaestraService.addTServicios(nuevaTablaMaestraServicios);

        List<Intervalos> intervalos = tablaMaestraServicios.getServiciosRecords();
        for( Intervalos intervalo:intervalos ){
            Intervalos nuevoInter = new Intervalos(intervalo.getTipoCalculo(),intervalo.getValorInicio(),intervalo.getValorAM(),
                    intervalo.getValorValle(),intervalo.getValorPM(),intervalo.getValorCierre(),intervalo.getBusesInicio(),
                    intervalo.getBusesAM(),intervalo.getBusesValle(),intervalo.getBusesPM(),intervalo.getBusesCierre(),
                    intervalo.getIdServicio(), nuevaTablaMaestraServicios);
            tablaMaestraService.addIntervalos(nuevoInter);

        }


    }

    private TablaMaestra obtenerUltimaTablaMaestra(String tipoDia,Date fechaCreacion,String modo) {
        return tablaMaestraService.getUltimaTablaMaestraByaTipoDia(tipoDia,fechaCreacion,modo);
    }

    public ProcessorUtils getProcessorUtils() {
        return processorUtils;
    }

    public void setProcessorUtils(ProcessorUtils processorUtils) {
        this.processorUtils = processorUtils;
    }
}

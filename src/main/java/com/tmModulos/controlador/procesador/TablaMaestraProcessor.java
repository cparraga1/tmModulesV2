package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.*;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.PathFiles;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    //private String destination = PathFiles.PATH_FOR_FILES+"\\Migracion\\/";
    private String destination = PathFiles.PATH_FOR_FILES_CONVERSION;


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
        processorUtils.copyFileUTF8(filename,in,destination);
        //processorUtils.copyFile(filename,in,destination);
        destination=destination+filename;

        // Copiar informacion intervalos
        horariosProvisionalServicio.deleteTablaHorarioFromFile();

        //LINEA PARA VALIDACION
        //Carga el archivo dentro de la base de datos
        horariosProvisionalServicio.addTablaHorarioFromFile(destination);

        //Encontrar parametros para la generacion de la tabla maestra (GIS de carga y Matriz de Distancia)
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

                if (servicio.getServicio().getIdentificador().equals("263-640-855-11423") || servicio.getServicio().getIdentificador().equals("263-640-855-11423") || servicio.getServicio().getIdentificador().equals("56-863-1046-1966") || servicio.getServicio().getIdentificador().equals("56-863-1046-1304") || servicio.getServicio().getIdentificador().equals("56-863-1179-1908") || servicio.getServicio().getIdentificador().equals("56-863-1179-1312")) {

                    System.out.println(servicio.getServicio().getIdentificador());

                    //Encontrar nodo Inicio del servicio por el codigo
                    // Nodo nodo = nodoService.getNodoByCodigo(servicio.getServicio().getPunto());
                    List<GisServicio> gisServicio = obtenerGisServicio(servicio, servicio.getServicio().getIdentificadorGIS());

                    TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();
                    tablaMaestraServicios = copiarInfoBasicaServicio(servicio, tablaMaestraServicios, tablaMaestra);


                    if (gisServicio.size() > 0) {

                        //Obtener información de los arco tiempo del GIS de carga
                        List<ArcoTiempo> arcoTiempoRecords = gisCargaService.getArcoTiempoByGisCargaAndServicio(gis, gisServicio);
                        if (arcoTiempoRecords.size() > 0) {

                            ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);


                            //Obtener Nodo Inicio basado en la informacion del GIS de carga
                            Nodo nodoInicio = getNodoInicio(servicio.getServicio().getPunto() + "");
                            if (nodoInicio != null) {
                                tablaMaestraServicios = agregarInfoNodo(servicio.getServicio(), tablaMaestraServicios, nodoInicio);

                                Nodo nodoFinal = getNodoInicio(servicio.getServicio().getPuntoFin() + "");
                                if (nodoFinal != null) {
                                    tablaMaestraServicios = agregarInfoNodoFin(servicio.getServicio(), tablaMaestraServicios, nodoFinal);

                                    //Calcular datos basicos matriz
                                    tablaMaestraServicios.setTipoDia(tipoDia);
                                    tablaMaestraServicios.setSecuencia(arcoTiempoBase.getSecuencia());
                                    tablaMaestraServicios = calcularDistancia(tablaMaestraServicios, nodoInicio, nodoFinal, matriz);

                                    //Calcular ciclos
                                    CicloServicio cicloServicio = calcularCiclos(tablaMaestraServicios, arcoTiempoRecords);
                                    tablaMaestraServicios.setCicloServicio(cicloServicio);
                                    //Calcular Velocidad Programada
                                    VelocidadProgramada velocidadProgramada = calcularVelocidadProgramada(cicloServicio, tablaMaestraServicios.getDistancia());
                                    tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

                                    HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio(), dia);
                                    tablaMaestraServicios.setHorariosServicio(horariosServicio);

                                    tablaMaestraService.addTServicios(tablaMaestraServicios);

                                    //Calcular Intervalos de tiempo
                                    intervalosProcessor.precalcularIntervalosProgramacion();
                                    List<Intervalos> intervaloses = intervalosProcessor.calcularIntervalos(tablaMaestraServicios, servicio);
//                                 List<Intervalos> intervaloses = intervalosProcessor.calcularValorIntervaloPorFranja(tablaMaestraServicios, servicio,gisIntervalos);


                                } else {
                                    log.warn("Nodo con nombre " + arcoTiempoBase.getServicio().getNodoFinal() + " No encontrado");
                                    logDatos.add(new LogDatos("Nodo con nombre " + arcoTiempoBase.getServicio().getNodoFinal() + " No encontrado", TipoLog.WARN));
                                    tablaMaestraServicios = addCicloServicio(tablaMaestraServicios);
                                    HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio(), dia);
                                    tablaMaestraServicios.setHorariosServicio(horariosServicio);
                                    tablaMaestraServicios.setIdentificadorb("0");
                                    tablaMaestraServicios.setIdentificadorc("0");
                                    tablaMaestraServicios.setSentido(1);
                                    tablaMaestraService.addTServicios(tablaMaestraServicios);
                                }

                            } else {
                                log.warn("Nodo con nombre " + arcoTiempoBase.getServicio().getNodoIncial() + " No encontrado");
                                logDatos.add(new LogDatos("Nodo con nombre " + arcoTiempoBase.getServicio().getNodoIncial() + " No encontrado", TipoLog.WARN));
                                tablaMaestraServicios = addCicloServicio(tablaMaestraServicios);
                                HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio(), dia);
                                tablaMaestraServicios.setHorariosServicio(horariosServicio);
                                tablaMaestraServicios.setIdentificadorb("0");
                                tablaMaestraServicios.setIdentificadorc("0");
                                tablaMaestraServicios.setSentido(1);
                                tablaMaestraService.addTServicios(tablaMaestraServicios);
                            }


                        } else {
                            servicioNoExisteEnGISCarga(servicio);

                        }
                    } else {
                        tablaMaestraServicios = addCicloServicio(tablaMaestraServicios);

                        HorariosServicio horariosServicio = calcularHorarioServicios(servicio.getServicio(), dia);
                        tablaMaestraServicios.setHorariosServicio(horariosServicio);
                        tablaMaestraServicios.setIdentificadorb("0");
                        tablaMaestraServicios.setIdentificadorc("0");
                        tablaMaestraServicios.setSentido(1);
                        tablaMaestraService.addTServicios(tablaMaestraServicios);
                        servicioNoExisteEnGISCarga(servicio);
                    }
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
        tablaMaestraServicios.setEstado(true);
        tablaMaestraServicios.setSecuencia(1);
        tablaMaestraService.addTServicios(tablaMaestraServicios);
        return true;
    }


    public HorariosServicio definirHorarioServiciosVacio(){
        HorariosServicio horario= new HorariosServicio();
        tablaMaestraService.addHorariosServicios(horario);
        return horario;
    }

    public HorariosServicio calcularHorarioServicios(Servicio servicio, TipoDia dia) {

        List<Horario> horariosByServicio = tablaMaestraService.getHorariosByServicioAndTipoDia(servicio,dia);
        HorariosServicio horario = getHorariosServicio(horariosByServicio);
        tablaMaestraService.addHorariosServicios(horario);
        return horario;
    }

    private HorariosServicio getHorariosServicio(List<Horario> horariosByServicio) {
        HorariosServicio horario= new HorariosServicio();

        int ext = 0;

        if( horariosByServicio.size()>0){
            for(Horario hr: horariosByServicio){
                ext = ext + 1;
                if(hr.getTipoHorario().equals("P")){
                    //if(hr.getConfig()==1){
                    if(ext <= 2){
                        horario.setHoraInicioProgA(hr.getHoraInicio());
                        horario.setHoraFinProgA(hr.getHoraFin());
                    } else /*if(hr.getConfig() ==2)*/{
                        horario.setHoraInicioProgB(hr.getHoraInicio());
                        horario.setHoraFinProgB(hr.getHoraFin());
                    }
                }
                else {
                    //if(hr.getConfig()==1){
                    if(ext <= 2){
                        horario.setHoraInicioUsuarioA(hr.getHoraInicio());
                        horario.setHoraFinUsuarioA(hr.getHoraFin());
                    }else /*if(hr.getConfig() ==2)*/{
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

        oldVelocidad.setOptimoPrimera(velocidadProgramada.getOptimoPrimera());
        oldVelocidad.setMinimoPrimera(velocidadProgramada.getMinimoPrimera());
        oldVelocidad.setMaximoPrimera(velocidadProgramada.getMaximoPrimera());
        oldVelocidad.setOptimoSegunda(velocidadProgramada.getOptimoSegunda());
        oldVelocidad.setMinimoSegunda(velocidadProgramada.getMinimoSegunda());
        oldVelocidad.setMaximoSegunda(velocidadProgramada.getMaximoSegunda());
        oldVelocidad.setOptimoTercera(velocidadProgramada.getOptimoTercera());
        oldVelocidad.setMinimoTercera(velocidadProgramada.getMinimoTercera());
        oldVelocidad.setMaximoTercera(velocidadProgramada.getMaximoTercera());
        oldVelocidad.setOptimoCuarta(velocidadProgramada.getOptimoCuarta());
        oldVelocidad.setMinimoCuarta(velocidadProgramada.getMinimoCuarta());
        oldVelocidad.setMaximoCuarta(velocidadProgramada.getMaximoCuarta());
        oldVelocidad.setOptimoQuinta(velocidadProgramada.getOptimoQuinta());
        oldVelocidad.setMinimoQuinta(velocidadProgramada.getMinimoQuinta());
        oldVelocidad.setMaximoQuinta(velocidadProgramada.getMaximoQuinta());
        oldVelocidad.setOptimoSexta(velocidadProgramada.getOptimoSexta());
        oldVelocidad.setMinimoSexta(velocidadProgramada.getMinimoSexta());
        oldVelocidad.setMaximoSexta(velocidadProgramada.getMaximoSexta());
        oldVelocidad.setOptimoSeptima(velocidadProgramada.getOptimoSeptima());
        oldVelocidad.setMinimoSeptima(velocidadProgramada.getMinimoSeptima());
        oldVelocidad.setMaximoSeptima(velocidadProgramada.getMaximoSeptima());
        oldVelocidad.setOptimoOctava(velocidadProgramada.getOptimoOctava());
        oldVelocidad.setMinimoOctava(velocidadProgramada.getMinimoOctava());
        oldVelocidad.setMaximoOctava(velocidadProgramada.getMaximoOctava());
        oldVelocidad.setOptimoNovena(velocidadProgramada.getOptimoNovena());
        oldVelocidad.setMinimoNovena(velocidadProgramada.getMinimoNovena());
        oldVelocidad.setMaximoNovena(velocidadProgramada.getMaximoNovena());
        oldVelocidad.setOptimoDecima(velocidadProgramada.getOptimoDecima());
        oldVelocidad.setMinimoDecima(velocidadProgramada.getMinimoDecima());
        oldVelocidad.setMaximoDecima(velocidadProgramada.getMaximoDecima());

        tablaMaestraServicios.setVelocidadProgramada(oldVelocidad);

    }

    private VelocidadProgramada getVelocidadProgramada(CicloServicio cicloServicio, Integer distancia) {
        VelocidadProgramada  velocidadProgramada = new VelocidadProgramada();
        Integer distanciaKM;
        if(distancia!=-1){
            distanciaKM = distancia/1000;

            velocidadProgramada.setOptimoPrimera(calcularVelocidad(distanciaKM,cicloServicio.getOptimoPrimera()));
            velocidadProgramada.setMaximoPrimera(calcularVelocidad(distanciaKM,cicloServicio.getMaximoPrimera()));
            velocidadProgramada.setMinimoPrimera(calcularVelocidad(distanciaKM,cicloServicio.getMinimoPrimera()));
            velocidadProgramada.setOptimoSegunda(calcularVelocidad(distanciaKM,cicloServicio.getOptimoSegunda()));
            velocidadProgramada.setMaximoSegunda(calcularVelocidad(distanciaKM,cicloServicio.getMaximoSegunda()));
            velocidadProgramada.setMinimoSegunda(calcularVelocidad(distanciaKM,cicloServicio.getMinimoSegunda()));
            velocidadProgramada.setOptimoTercera(calcularVelocidad(distanciaKM,cicloServicio.getOptimoTercera()));
            velocidadProgramada.setMaximoTercera(calcularVelocidad(distanciaKM,cicloServicio.getMaximoTercera()));
            velocidadProgramada.setMinimoTercera(calcularVelocidad(distanciaKM,cicloServicio.getMinimoTercera()));
            velocidadProgramada.setOptimoCuarta(calcularVelocidad(distanciaKM,cicloServicio.getOptimoCuarta()));
            velocidadProgramada.setMaximoCuarta(calcularVelocidad(distanciaKM,cicloServicio.getMaximoCuarta()));
            velocidadProgramada.setMinimoCuarta(calcularVelocidad(distanciaKM,cicloServicio.getMinimoCuarta()));
            velocidadProgramada.setOptimoQuinta(calcularVelocidad(distanciaKM,cicloServicio.getOptimoQuinta()));
            velocidadProgramada.setMaximoQuinta(calcularVelocidad(distanciaKM,cicloServicio.getMaximoQuinta()));
            velocidadProgramada.setMinimoQuinta(calcularVelocidad(distanciaKM,cicloServicio.getMinimoQuinta()));
            velocidadProgramada.setOptimoSexta(calcularVelocidad(distanciaKM,cicloServicio.getOptimoSexta()));
            velocidadProgramada.setMaximoSexta(calcularVelocidad(distanciaKM,cicloServicio.getMaximoSexta()));
            velocidadProgramada.setMinimoSexta(calcularVelocidad(distanciaKM,cicloServicio.getMinimoSexta()));
            velocidadProgramada.setOptimoSeptima(calcularVelocidad(distanciaKM,cicloServicio.getOptimoSeptima()));
            velocidadProgramada.setMaximoSeptima(calcularVelocidad(distanciaKM,cicloServicio.getMaximoSeptima()));
            velocidadProgramada.setMinimoSeptima(calcularVelocidad(distanciaKM,cicloServicio.getMinimoSeptima()));
            velocidadProgramada.setOptimoOctava(calcularVelocidad(distanciaKM,cicloServicio.getOptimoOctava()));
            velocidadProgramada.setMaximoOctava(calcularVelocidad(distanciaKM,cicloServicio.getMaximoOctava()));
            velocidadProgramada.setMinimoOctava(calcularVelocidad(distanciaKM,cicloServicio.getMinimoOctava()));
            velocidadProgramada.setOptimoNovena(calcularVelocidad(distanciaKM,cicloServicio.getOptimoNovena()));
            velocidadProgramada.setMaximoNovena(calcularVelocidad(distanciaKM,cicloServicio.getMaximoNovena()));
            velocidadProgramada.setMinimoNovena(calcularVelocidad(distanciaKM,cicloServicio.getMinimoNovena()));
            velocidadProgramada.setOptimoDecima(calcularVelocidad(distanciaKM,cicloServicio.getOptimoDecima()));
            velocidadProgramada.setMaximoDecima(calcularVelocidad(distanciaKM,cicloServicio.getMaximoDecima()));
            velocidadProgramada.setMinimoDecima(calcularVelocidad(distanciaKM,cicloServicio.getMinimoDecima()));
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
        tablaMaestraServicios.setFranjaCuartos(servicio.getServicio().getCuartoFranja());
        tablaMaestraServicios.setSentido(servicio.getServicio().getSentido());
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
                +servicio.getServicio().getSentido()+"-"+servicio.getServicio().getLinea()+" nodo: "+servicio.getServicio().getPunto());
        logDatos.add(new LogDatos("Servicio no encontrado en el GIS de carga ", TipoLog.WARN));
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getSentido()+"-"+servicio.getServicio().getLinea()+" nodo: "+servicio.getServicio().getPunto(), TipoLog.WARN));
    }

    // Obtener el servicio asociado en el GIS de Carga a partir de la linea, trayecto y nodo
    private List<GisServicio> obtenerGisServicio(ServicioTipoDia servicio, String identificador) {

        List<GisServicio> gisServicio= new ArrayList<>();
        if(identificador!=null){
            gisServicio=gisCargaService.getGisServicioByTrayectoLinea(identificador);
            gisServicio = validarPuntosServicio(gisServicio,servicio.getServicio().getPunto()+"");
            gisServicio = validarPuntosServicioFin(gisServicio,servicio.getServicio().getPuntoFin()+"");
        }else{
            log.error("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador());
            logDatos.add(new LogDatos("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador(), TipoLog.ERROR));
        }
        return gisServicio;
    }

    private List<GisServicio> validarPuntosServicio(List<GisServicio> gisServicio, String punto) {
        List<GisServicio> remover = new ArrayList<>();
        for (int x=0;x<gisServicio.size();x++){
            if(!gisServicio.get(x).getPuntoInicial().equals(punto)){
                remover.add(gisServicio.get(x));
            } else {
                break;
            }
        }

        gisServicio=removerDatos(gisServicio,remover);

        return gisServicio;
    }

    private List<GisServicio> removerDatos(List<GisServicio> gisServicio, List<GisServicio> remover) {
        for (GisServicio num:remover){
            gisServicio.remove(num);
        }
        return gisServicio;
    }

    private List<GisServicio> validarPuntosServicioFin(List<GisServicio> gisServicio, String punto) {

        List<GisServicio> remover = new ArrayList<>();
        for (int x=gisServicio.size()-1;x>0;x--){
            if(!gisServicio.get(x).getPuntoFinal().equals(punto)){
                remover.add(gisServicio.get(x));
            } else {
                break;
            }
        }
        gisServicio=removerDatos(gisServicio,remover);

        return gisServicio;
    }

    public Nodo getNodoInicio(String nodoIncial) {
        Nodo nodo = nodoService.getNodoByCodigo(nodoIncial);
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

        oldCicloServicio.setMinimoPrimera(cicloServicio.getMinimoPrimera());
        oldCicloServicio.setMaximoPrimera(cicloServicio.getMaximoPrimera());
        oldCicloServicio.setOptimoPrimera(cicloServicio.getOptimoPrimera());
        oldCicloServicio.setMinimoSegunda(cicloServicio.getMinimoSegunda());
        oldCicloServicio.setMaximoSegunda(cicloServicio.getMaximoSegunda());
        oldCicloServicio.setOptimoSegunda(cicloServicio.getOptimoSegunda());
        oldCicloServicio.setMinimoTercera(cicloServicio.getMinimoTercera());
        oldCicloServicio.setMaximoTercera(cicloServicio.getMaximoTercera());
        oldCicloServicio.setOptimoTercera(cicloServicio.getOptimoTercera());
        oldCicloServicio.setMinimoCuarta(cicloServicio.getMinimoCuarta());
        oldCicloServicio.setMaximoCuarta(cicloServicio.getMaximoCuarta());
        oldCicloServicio.setOptimoCuarta(cicloServicio.getOptimoCuarta());
        oldCicloServicio.setMinimoQuinta(cicloServicio.getMinimoQuinta());
        oldCicloServicio.setMaximoQuinta(cicloServicio.getMaximoQuinta());
        oldCicloServicio.setOptimoQuinta(cicloServicio.getOptimoQuinta());
        oldCicloServicio.setMinimoSexta(cicloServicio.getMinimoSexta());
        oldCicloServicio.setMaximoSexta(cicloServicio.getMaximoSexta());
        oldCicloServicio.setOptimoSexta(cicloServicio.getOptimoSexta());
        oldCicloServicio.setMinimoSeptima(cicloServicio.getMinimoSeptima());
        oldCicloServicio.setMaximoSeptima(cicloServicio.getMaximoSeptima());
        oldCicloServicio.setOptimoSeptima(cicloServicio.getOptimoSeptima());
        oldCicloServicio.setMinimoOctava(cicloServicio.getMinimoOctava());
        oldCicloServicio.setMaximoOctava(cicloServicio.getMaximoOctava());
        oldCicloServicio.setOptimoOctava(cicloServicio.getOptimoOctava());
        oldCicloServicio.setMinimoNovena(cicloServicio.getMinimoNovena());
        oldCicloServicio.setMaximoNovena(cicloServicio.getMaximoNovena());
        oldCicloServicio.setOptimoNovena(cicloServicio.getOptimoNovena());
        oldCicloServicio.setMinimoDecima(cicloServicio.getMinimoDecima());
        oldCicloServicio.setMaximoDecima(cicloServicio.getMaximoDecima());
        oldCicloServicio.setOptimoDecima(cicloServicio.getOptimoDecima());

        tablaMaestraService.updateCicloServicio(oldCicloServicio);

    }

    //Calcula los Ciclos del Servicio
    private CicloServicio getCicloServicio(List<ArcoTiempo> arcoTiempoRecords) {
        CicloServicio cicloServicio = new CicloServicio();
        for (ArcoTiempo arcoTiempo: arcoTiempoRecords) {
            String horaInicio = arcoTiempo.getHoraDesde();
            String horaFin = arcoTiempo.getHoraHasta();
            //horaFin = validarHoraFin(horaFin);
            List<TipoFranja> tipoFranjaList = tablaMaestraService.getTipoFranjaByHorario(horaInicio, horaFin);

            //Para saber si la franja indicada en el GIS contiene una o varias tipo_franjas reales
            for(TipoFranja tipoFranja:tipoFranjaList){
            if (tipoFranja != null) {
                if (tipoFranja.getNombre().equals("Primera")) {
                    cicloServicio.setMinimoPrimera(sumaValores(cicloServicio.getMinimoPrimera(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoPrimera(sumaValores(cicloServicio.getMaximoPrimera(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoPrimera(sumaValores(cicloServicio.getOptimoPrimera(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Segunda")) {
                    cicloServicio.setMinimoSegunda(sumaValores(cicloServicio.getMinimoSegunda(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoSegunda(sumaValores(cicloServicio.getMaximoSegunda(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoSegunda(sumaValores(cicloServicio.getOptimoSegunda(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Tercera")) {
                    cicloServicio.setMinimoTercera(sumaValores(cicloServicio.getMinimoTercera(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoTercera(sumaValores(cicloServicio.getMaximoTercera(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoTercera(sumaValores(cicloServicio.getOptimoTercera(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Cuarta")) {
                    cicloServicio.setMinimoCuarta(sumaValores(cicloServicio.getMinimoCuarta(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoCuarta(sumaValores(cicloServicio.getMaximoCuarta(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoCuarta(sumaValores(cicloServicio.getOptimoCuarta(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Quinta")) {
                    cicloServicio.setMinimoQuinta(sumaValores(cicloServicio.getMinimoQuinta(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoQuinta(sumaValores(cicloServicio.getMaximoQuinta(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoQuinta(sumaValores(cicloServicio.getOptimoQuinta(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Sexta")) {
                    cicloServicio.setMinimoSexta(sumaValores(cicloServicio.getMinimoSexta(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoSexta(sumaValores(cicloServicio.getMaximoSexta(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoSexta(sumaValores(cicloServicio.getOptimoSexta(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Septima")) {
                    cicloServicio.setMinimoSeptima(sumaValores(cicloServicio.getMinimoSeptima(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoSeptima(sumaValores(cicloServicio.getMaximoSeptima(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoSeptima(sumaValores(cicloServicio.getOptimoSeptima(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Octava")) {
                    cicloServicio.setMinimoOctava(sumaValores(cicloServicio.getMinimoOctava(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoOctava(sumaValores(cicloServicio.getMaximoOctava(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoOctava(sumaValores(cicloServicio.getOptimoOctava(), arcoTiempo.getTiempoOptimo()));
                } else if (tipoFranja.getNombre().equals("Novena")) {
                    cicloServicio.setMinimoNovena(sumaValores(cicloServicio.getMinimoNovena(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoNovena(sumaValores(cicloServicio.getMaximoNovena(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoNovena(sumaValores(cicloServicio.getOptimoNovena(), arcoTiempo.getTiempoOptimo()));
                } else {
                    cicloServicio.setMinimoDecima(sumaValores(cicloServicio.getMinimoDecima(), arcoTiempo.getTiempoMinimo()));
                    cicloServicio.setMaximoDecima(sumaValores(cicloServicio.getMaximoDecima(), arcoTiempo.getTiempoMaximo()));
                    cicloServicio.setOptimoDecima(sumaValores(cicloServicio.getOptimoDecima(), arcoTiempo.getTiempoOptimo()));
                }
            } else {
                System.out.println("Tipo de franja no existente: " + horaInicio + "-" + horaFin);
            }
        }

        }
        return cicloServicio;
    }

    private String sumaValores(String anterior, String nuevo) {
        if(anterior!=null){
            try {
                String pt = "1970-01-01-";
                final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                final Calendar sum = Calendar.getInstance();
                sum.setTimeInMillis(0);

                long tm0 = 0;
                tm0 = new SimpleDateFormat("yyyy-MM-dd").parse(pt).getTime();

                Date x = dt.parse(pt + anterior);
                // System.out.println(x.getTime());
                sum.add(Calendar.MILLISECOND, (int)x.getTime());
                sum.add(Calendar.MILLISECOND, (int)-tm0);

                x = dt.parse(pt + nuevo);
                // System.out.println(x.getTime());
                sum.add(Calendar.MILLISECOND, (int)x.getTime());
                sum.add(Calendar.MILLISECOND, (int)-tm0);

                long tm = sum.getTime().getTime();

                tm = tm / 1000;

                long hh = tm / 3600;
                tm %= 3600;
                long mm = tm / 60;
                tm %= 60;
                long ss = tm;
                nuevo = format(hh) + ":" + format(mm) + ":" + format(ss);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return nuevo;
    }
    private static String format(long s){
        if (s < 10) return "0" + s;
        else return "" + s;
    }


    private String convertirAFormatoFecha(Double horaNueva) {

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        long myLong = System.currentTimeMillis() + ((long) (horaNueva * 1000));
        Date itemDate = new Date(myLong);
        return parser.format(itemDate);
    }

    private Double getHoraArray(String nuevo) {
        String[] datos = nuevo.split(":");
        double horasNueva= Integer.parseInt(datos[0]);
        double minutosNueva= Integer.parseInt(datos[1]);
        double segundosNueva= Integer.parseInt(datos[2]);
        horasNueva=horasNueva + (minutosNueva/60) + ( segundosNueva/3600 );
        return horasNueva;
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
        log.warn("Servicio no encontrado en la Matriz de Distancias con Línea "+macro+" Sublinea "+linea+" Ruta "+seccion);
        logDatos.add(new LogDatos("Servicio no encontrado en en la Matriz de Distancias con Línea "+macro+" Sublinea "+linea+" Ruta "+seccion, TipoLog.WARN));
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
        nuevaTablaMaestraServicios.setFranjaCuartos(tablaMaestraServicios.getFranjaCuartos());
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

        cicloServicio.setOptimoPrimera(tablaMaestraServicios.getCicloServicio().getOptimoPrimera());
        cicloServicio.setMaximoPrimera(tablaMaestraServicios.getCicloServicio().getMaximoPrimera());
        cicloServicio.setMinimoPrimera(tablaMaestraServicios.getCicloServicio().getMinimoPrimera());
        cicloServicio.setOptimoSegunda(tablaMaestraServicios.getCicloServicio().getOptimoSegunda());
        cicloServicio.setMaximoSegunda(tablaMaestraServicios.getCicloServicio().getMaximoSegunda());
        cicloServicio.setMinimoSegunda(tablaMaestraServicios.getCicloServicio().getMinimoSegunda());
        cicloServicio.setOptimoTercera(tablaMaestraServicios.getCicloServicio().getOptimoTercera());
        cicloServicio.setMaximoTercera(tablaMaestraServicios.getCicloServicio().getMaximoTercera());
        cicloServicio.setMinimoTercera(tablaMaestraServicios.getCicloServicio().getMinimoTercera());
        cicloServicio.setOptimoCuarta(tablaMaestraServicios.getCicloServicio().getOptimoCuarta());
        cicloServicio.setMaximoCuarta(tablaMaestraServicios.getCicloServicio().getMaximoCuarta());
        cicloServicio.setMinimoCuarta(tablaMaestraServicios.getCicloServicio().getMinimoCuarta());
        cicloServicio.setOptimoQuinta(tablaMaestraServicios.getCicloServicio().getOptimoQuinta());
        cicloServicio.setMaximoQuinta(tablaMaestraServicios.getCicloServicio().getMaximoQuinta());
        cicloServicio.setMinimoQuinta(tablaMaestraServicios.getCicloServicio().getMinimoQuinta());
        cicloServicio.setOptimoSexta(tablaMaestraServicios.getCicloServicio().getOptimoSexta());
        cicloServicio.setMaximoSexta(tablaMaestraServicios.getCicloServicio().getMaximoSexta());
        cicloServicio.setMinimoSexta(tablaMaestraServicios.getCicloServicio().getMinimoSexta());
        cicloServicio.setOptimoSeptima(tablaMaestraServicios.getCicloServicio().getOptimoSeptima());
        cicloServicio.setMaximoSeptima(tablaMaestraServicios.getCicloServicio().getMaximoSeptima());
        cicloServicio.setMinimoSeptima(tablaMaestraServicios.getCicloServicio().getMinimoSeptima());
        cicloServicio.setOptimoOctava(tablaMaestraServicios.getCicloServicio().getOptimoOctava());
        cicloServicio.setMaximoOctava(tablaMaestraServicios.getCicloServicio().getMaximoOctava());
        cicloServicio.setMinimoOctava(tablaMaestraServicios.getCicloServicio().getMinimoOctava());
        cicloServicio.setOptimoNovena(tablaMaestraServicios.getCicloServicio().getOptimoNovena());
        cicloServicio.setMaximoNovena(tablaMaestraServicios.getCicloServicio().getMaximoNovena());
        cicloServicio.setMinimoNovena(tablaMaestraServicios.getCicloServicio().getMinimoNovena());
        cicloServicio.setOptimoDecima(tablaMaestraServicios.getCicloServicio().getOptimoDecima());
        cicloServicio.setMaximoDecima(tablaMaestraServicios.getCicloServicio().getMaximoDecima());
        cicloServicio.setMinimoDecima(tablaMaestraServicios.getCicloServicio().getMinimoDecima());

        tablaMaestraService.addCicloServicio(cicloServicio);
        nuevaTablaMaestraServicios.setCicloServicio(cicloServicio);

        VelocidadProgramada velocidadProgramada = new VelocidadProgramada();

        velocidadProgramada.setMaximoPrimera(tablaMaestraServicios.getVelocidadProgramada().getMaximoPrimera());
        velocidadProgramada.setMinimoPrimera(tablaMaestraServicios.getVelocidadProgramada().getMinimoPrimera());
        velocidadProgramada.setOptimoPrimera(tablaMaestraServicios.getVelocidadProgramada().getOptimoPrimera());
        velocidadProgramada.setMaximoSegunda(tablaMaestraServicios.getVelocidadProgramada().getMaximoSegunda());
        velocidadProgramada.setMinimoSegunda(tablaMaestraServicios.getVelocidadProgramada().getMinimoSegunda());
        velocidadProgramada.setOptimoSegunda(tablaMaestraServicios.getVelocidadProgramada().getOptimoSegunda());
        velocidadProgramada.setMaximoTercera(tablaMaestraServicios.getVelocidadProgramada().getMaximoTercera());
        velocidadProgramada.setMinimoTercera(tablaMaestraServicios.getVelocidadProgramada().getMinimoTercera());
        velocidadProgramada.setOptimoTercera(tablaMaestraServicios.getVelocidadProgramada().getOptimoTercera());
        velocidadProgramada.setMaximoCuarta(tablaMaestraServicios.getVelocidadProgramada().getMaximoCuarta());
        velocidadProgramada.setMinimoCuarta(tablaMaestraServicios.getVelocidadProgramada().getMinimoCuarta());
        velocidadProgramada.setOptimoCuarta(tablaMaestraServicios.getVelocidadProgramada().getOptimoCuarta());
        velocidadProgramada.setMaximoQuinta(tablaMaestraServicios.getVelocidadProgramada().getMaximoQuinta());
        velocidadProgramada.setMinimoQuinta(tablaMaestraServicios.getVelocidadProgramada().getMinimoQuinta());
        velocidadProgramada.setOptimoQuinta(tablaMaestraServicios.getVelocidadProgramada().getOptimoQuinta());
        velocidadProgramada.setMaximoSexta(tablaMaestraServicios.getVelocidadProgramada().getMaximoSexta());
        velocidadProgramada.setMinimoSexta(tablaMaestraServicios.getVelocidadProgramada().getMinimoSexta());
        velocidadProgramada.setOptimoSexta(tablaMaestraServicios.getVelocidadProgramada().getOptimoSexta());
        velocidadProgramada.setMaximoSeptima(tablaMaestraServicios.getVelocidadProgramada().getMaximoSeptima());
        velocidadProgramada.setMinimoSeptima(tablaMaestraServicios.getVelocidadProgramada().getMinimoSeptima());
        velocidadProgramada.setOptimoSeptima(tablaMaestraServicios.getVelocidadProgramada().getOptimoSeptima());
        velocidadProgramada.setMaximoOctava(tablaMaestraServicios.getVelocidadProgramada().getMaximoOctava());
        velocidadProgramada.setMinimoOctava(tablaMaestraServicios.getVelocidadProgramada().getMinimoOctava());
        velocidadProgramada.setOptimoOctava(tablaMaestraServicios.getVelocidadProgramada().getOptimoOctava());
        velocidadProgramada.setMaximoNovena(tablaMaestraServicios.getVelocidadProgramada().getMaximoNovena());
        velocidadProgramada.setMinimoNovena(tablaMaestraServicios.getVelocidadProgramada().getMinimoNovena());
        velocidadProgramada.setOptimoNovena(tablaMaestraServicios.getVelocidadProgramada().getOptimoNovena());
        velocidadProgramada.setMaximoDecima(tablaMaestraServicios.getVelocidadProgramada().getMaximoDecima());
        velocidadProgramada.setMinimoDecima(tablaMaestraServicios.getVelocidadProgramada().getMinimoDecima());
        velocidadProgramada.setOptimoDecima(tablaMaestraServicios.getVelocidadProgramada().getOptimoDecima());

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
            Intervalos nuevoInter = new Intervalos(intervalo.getTipoCalculo(),intervalo.getValorPrimera(),intervalo.getValorSegunda(),
                    intervalo.getValorTercera(),intervalo.getValorCuarta(),intervalo.getValorQuinta(), intervalo.getValorSexta(), intervalo.getValorSeptima(), intervalo.getValorOctava(), intervalo.getValorNovena(), intervalo.getValorDecima(), intervalo.getBusesPrimera(),
                    intervalo.getBusesSegunda(),intervalo.getBusesTercera(),intervalo.getBusesCuarta(),intervalo.getBusesQuinta(), intervalo.getBusesSexta(), intervalo.getBusesSeptima(), intervalo.getBusesOctava(), intervalo.getBusesNovena(), intervalo.getBusesDecima(),
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

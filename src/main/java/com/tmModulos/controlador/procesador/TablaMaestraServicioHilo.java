package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.dao.tmData.TablaMaestraAuxiliarDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.log4j.Logger;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

public class TablaMaestraServicioHilo implements Runnable{

    private ServicioTipoDia servicioTipoDia;
    private GisIntervalos gisIntervalos;
    private TablaMaestraAuxiliarDao auxiliarDao;
    private GisCarga gis;
    private  TablaMaestra tablaMaestra;
    private MatrizDistancia matriz;
    private List<LogDatos> logDatos;
    private static Logger log = Logger.getLogger(TablaMaestraServicioHilo.class);


    public TablaMaestraServicioHilo(ServicioTipoDia servicioTipoDia, GisIntervalos gisIntervalos, GisCarga gis, TablaMaestra tablaMaestra, MatrizDistancia matriz, List<LogDatos> logDatos) {
        this.servicioTipoDia = servicioTipoDia;
        this.gisIntervalos = gisIntervalos;
        this.gis = gis;
        this.tablaMaestra = tablaMaestra;
        this.matriz = matriz;
        this.logDatos = logDatos;
    }

    @Override
    public void run() {
        auxiliarDao = new TablaMaestraAuxiliarDao();

        //Encontrar nodo Inicio del servicio por el codigo
        // Nodo nodo = nodoService.getNodoByCodigo(servicio.getServicio().getPunto());
        GisServicio gisServicio = obtenerGisServicio(servicioTipoDia, servicioTipoDia.getServicio().getIdentificador());

        TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();
        tablaMaestraServicios= copiarInfoBasicaServicio(servicioTipoDia, tablaMaestraServicios,tablaMaestra);

        if(gisServicio!=null){

            //Obtener información de los arco tiempo del GIS de carga
            List<ArcoTiempo> arcoTiempoRecords = auxiliarDao.getArcoTiempoByGisCargaAndServicio(gis,gisServicio);
            if(arcoTiempoRecords.size()>0){
                ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);

                //Obtener Nodo Inicio basado en la informacion del GIS de carga
                Nodo nodoInicio = getNodoInicio(arcoTiempoBase.getServicio().getNodoIncial());
                if(nodoInicio!=null){

                    tablaMaestraServicios = agregarInfoNodo(servicioTipoDia, tablaMaestraServicios, nodoInicio);
                    Nodo nodoFinal = getNodoInicio(arcoTiempoBase.getServicio().getNodoFinal());
                    if(nodoFinal!=null){
                        tablaMaestraServicios =agregarInfoNodoFin(servicioTipoDia,tablaMaestraServicios,nodoFinal);

                        //Calcular datos basicos matriz
                        tablaMaestraServicios.setTipoDia(arcoTiempoBase.getTipoDiaByArco().getTipoDia().getNombre());
                        tablaMaestraServicios.setSecuencia(arcoTiempoBase.getSecuencia());
                        tablaMaestraServicios= calcularDistancia(tablaMaestraServicios,nodoInicio,nodoFinal,matriz);

                        //Calcular ciclos
                        CicloServicio cicloServicio = calcularCiclos(tablaMaestraServicios,arcoTiempoRecords);
                        tablaMaestraServicios.setCicloServicio(cicloServicio);
                        VelocidadProgramada  velocidadProgramada = calcularVelocidadProgramada(cicloServicio,tablaMaestraServicios.getDistancia());
                        tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

                        HorariosServicio horariosServicio = calcularHorarioServicios(servicioTipoDia.getServicio());
                        tablaMaestraServicios.setHorariosServicio(horariosServicio);

                        auxiliarDao.addTServicios(tablaMaestraServicios);

                        //Calcular Intervalos de tiempo
                        List<Intervalos> intervaloses = calcularValorIntervaloPorFranja(tablaMaestraServicios, servicioTipoDia, gisIntervalos);




                    }else{
                        log.warn("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoFinal()+" No encontrado");
                        logDatos.add(new LogDatos("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoFinal()+" No encontrado", TipoLog.WARN));
                        tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                        HorariosServicio horariosServicio = calcularHorarioServicios(servicioTipoDia.getServicio());
                        tablaMaestraServicios.setHorariosServicio(horariosServicio);
                        auxiliarDao.addTServicios(tablaMaestraServicios);
                    }

                }else{
                    log.warn("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoIncial()+" No encontrado");
                    logDatos.add(new LogDatos("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoIncial()+" No encontrado", TipoLog.WARN));
                    tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                    HorariosServicio horariosServicio = calcularHorarioServicios(servicioTipoDia.getServicio());
                    tablaMaestraServicios.setHorariosServicio(horariosServicio);
                    auxiliarDao.addTServicios(tablaMaestraServicios);
                }


            }else{
                servicioNoExisteEnGISCarga(servicioTipoDia);
            }


        }else{
            tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
            HorariosServicio horariosServicio = calcularHorarioServicios(servicioTipoDia.getServicio());
            tablaMaestraServicios.setHorariosServicio(horariosServicio);
            auxiliarDao.addTServicios(tablaMaestraServicios);
            servicioNoExisteEnGISCarga(servicioTipoDia);
        }

    }



    private GisServicio obtenerGisServicio(ServicioTipoDia servicio, String identificador) {
        GisServicio gisServicio=null;
        if(identificador!=null){
            gisServicio=auxiliarDao.getGisServicioByTrayectoLinea(identificador);
        }else{
            log.error("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador());
            logDatos.add(new LogDatos("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador(), TipoLog.ERROR));
        }
        return gisServicio;
    }


    private TablaMaestraServicios copiarInfoBasicaServicio(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, TablaMaestra tablaMaestra) {
        //Copiar informacion del servicio
        tablaMaestraServicios.setTrayecto(servicio.getServicio().getTrayecto()+"");
        tablaMaestraServicios.setMacro(servicio.getServicio().getMacro());
        tablaMaestraServicios.setLinea(servicio.getServicio().getLinea());
        tablaMaestraServicios.setSeccion(servicio.getServicio().getSeccion());
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


    private TablaMaestraServicios addCicloServicio(TablaMaestraServicios tablaMaestraServicios) {
        CicloServicio cicloServicio = new CicloServicio();
        auxiliarDao.addCicloServicio(cicloServicio);
        tablaMaestraServicios.setCicloServicio(cicloServicio);

        VelocidadProgramada velocidadProgramada = new VelocidadProgramada();
        auxiliarDao.addVelocidadProgramada(velocidadProgramada);
        tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

        return tablaMaestraServicios;
    }

    private HorariosServicio calcularHorarioServicios(Servicio servicio) {

        List<Horario> horariosByServicio = auxiliarDao.getHorariosByServicio(servicio);
        HorariosServicio horario= new HorariosServicio();
        if( horariosByServicio.size()>0){
            for(Horario hr: horariosByServicio){
                if(hr.getTipoHorario().equals("P")){
                    if(hr.getConfig()==1){
                        horario.setHoraInicioA(hr.getHoraInicio());
                        horario.setHoraFinA(hr.getHoraFin());
                    }else if(hr.getConfig() ==2){
                        horario.setHoraInicioB(hr.getHoraInicio());
                        horario.setHoraFinB(hr.getHoraFin());
                    }
                }else {
                    if(hr.getConfig()==1){
                        horario.setHoraInicioC(hr.getHoraInicio());
                        horario.setHoraFinC(hr.getHoraFin());
                    }else if(hr.getConfig() ==2){
                        horario.setHoraInicioD(hr.getHoraInicio());
                        horario.setHoraFinD(hr.getHoraFin());
                    }
                }
            }
        }
        auxiliarDao.addHorariosServicios(horario);
        return horario;
    }

    private void servicioNoExisteEnGISCarga(ServicioTipoDia servicio) {
        log.warn("Servicio no encontrado en el GIS de carga ");
        log.warn("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto());
        logDatos.add(new LogDatos("Servicio no encontrado en el GIS de carga ", TipoLog.WARN));
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto(), TipoLog.WARN));
    }

    private Nodo getNodoInicio(String nodoIncial) {
        Nodo nodo = auxiliarDao.getNodo(nodoIncial);
        return nodo;
    }

    private TablaMaestraServicios agregarInfoNodo(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, Nodo nodoInicio) {
        tablaMaestraServicios.setCodigoInicio(nodoInicio.getCodigo());
        tablaMaestraServicios.setNombreInicio(nodoInicio.getNombre());
        tablaMaestraServicios.setZonaTInicio(nodoInicio.getVagon().getEstacion().getZonaUsuario().getNombre());
        tablaMaestraServicios.setZonaPInicio(nodoInicio.getVagon().getEstacion().getZonaProgramacion().getNombre());
        tablaMaestraServicios.setIdInicio(calcularId(servicio.getServicio(),nodoInicio.getCodigo()));
        return  tablaMaestraServicios;
    }

    private String calcularId(Servicio servicio, Integer codigo) {
        return servicio.getMacro()+"-"+servicio.getLinea()+"-"+servicio.getSeccion()+"-"+codigo;
    }

    private TablaMaestraServicios agregarInfoNodoFin(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, Nodo nodoFinal) {
        tablaMaestraServicios.setCodigoFin(nodoFinal.getCodigo());
        tablaMaestraServicios.setNombreIFin(nodoFinal.getNombre());
        tablaMaestraServicios.setZonaTFin(nodoFinal.getVagon().getEstacion().getZonaUsuario().getNombre());
        tablaMaestraServicios.setZonaPFin(nodoFinal.getVagon().getEstacion().getZonaProgramacion().getNombre());
        tablaMaestraServicios.setIdFin(calcularId(servicio.getServicio(),nodoFinal.getCodigo()));
        return  tablaMaestraServicios;
    }

    private TablaMaestraServicios calcularDistancia(TablaMaestraServicios tablaMaestraServicios, Nodo nodoIni,Nodo nodoFin, MatrizDistancia matrizDistancia) {

        int macro = tablaMaestraServicios.getMacro();
        int linea = tablaMaestraServicios.getLinea();
        int seccion = tablaMaestraServicios.getSeccion();
        int seccionAux = 4;
        boolean calculoEspecialInicio=false;
        boolean calculoEspecialFin=false;

        // Obtener servicio asociado a la matriz de Distancia
        ServicioDistancia servicioDistancia = auxiliarDao.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        ServicioDistancia servicioDistanciaAux=null ;

        if (servicioDistancia!=null){
            DistanciaNodos distanciaNodosA= auxiliarDao.getDistanciaNodosByServicioAndNodo(servicioDistancia,matrizDistancia,nodoIni.getCodigo()+"");
            DistanciaNodos distanciaNodosB= auxiliarDao.getDistanciaNodosByServicioAndNodo(servicioDistancia,matrizDistancia,nodoFin.getCodigo()+"");

            if( distanciaNodosA==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux = auxiliarDao.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosA= auxiliarDao.getDistanciaNodosByServicioAndNodo(servicioDistanciaAux,matrizDistancia,nodoIni.getCodigo()+"");
                    calculoEspecialInicio =true;
                }else{
                    serviciosNoEncontradosMatriz(macro, linea, seccion);
                }

            }
            if(distanciaNodosB==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux  = auxiliarDao.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosB= auxiliarDao.getDistanciaNodosByServicioAndNodo(servicioDistanciaAux,matrizDistancia,nodoFin.getCodigo()+"");
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

    private int getSeccionAux(int seccion) {
        int seccionAux;
        if(seccion==1){
            seccionAux=3;
        }else{
            seccionAux=4;
        }
        return seccionAux;
    }


    private void serviciosNoEncontradosMatriz(int macro, int linea, int seccion) {
        log.warn("Servicio no encontrado en la Matriz de Distancias con Macro "+macro+" Linea "+linea+" Seccion "+seccion);
        logDatos.add(new LogDatos("Servicio no encontrado en en la Matriz de Distancias con Macro "+macro+" Linea "+linea+" Seccion "+seccion, TipoLog.WARN));
    }

    private int calcularDistanciaEspecial(int seccion, DistanciaNodos distanciaNodosA, DistanciaNodos distanciaNodosB,ServicioDistancia servicioDistancia, ServicioDistancia servicioDistanciaAux,MatrizDistancia matrizDistancia ) {
        DistanciaNodos ultimoNodoSeccion = auxiliarDao.getUltimoDistanciaNodosByServicioAndPunto(servicioDistancia,matrizDistancia);
        return   distanciaNodosB.getDistancia()+ultimoNodoSeccion.getDistancia();
    }

    private void noHayValoresenMatriz(Nodo nodoFin, ServicioDistancia servicioDistancia) {
        log.warn("Error en la busqueda de la matriz de distancia");
        log.warn("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
                +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre());
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
                +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre(), TipoLog.WARN));
    }

    //Calcular ciclos de tiempos de recorrido - en base al GIS de carga
    private CicloServicio calcularCiclos(TablaMaestraServicios tablaMaestraServicios, List<ArcoTiempo> arcoTiempoRecords) {
        CicloServicio cicloServicio = new CicloServicio();
        for (ArcoTiempo arcoTiempo: arcoTiempoRecords){
            TipoFranja tipoFranja = auxiliarDao.getTipoFranjaByHorario(arcoTiempo.getHoraDesde(),arcoTiempo.getHoraHasta());
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

        auxiliarDao.addCicloServicio(cicloServicio);
        return cicloServicio;
    }

    private VelocidadProgramada calcularVelocidadProgramada(CicloServicio cicloServicio, Integer distancia) {
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
        auxiliarDao.addVelocidadProgramada(velocidadProgramada);
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
                String velocidadFormateada = String.format("%.2f",velocidad);
                return Double.parseDouble(velocidadFormateada);
            }

        }
        return 0;
    }

    public List<Intervalos> calcularValorIntervaloPorFranja(TablaMaestraServicios tablaMaestraServicios,ServicioTipoDia servicio,GisIntervalos gisIntervalos) {

        TipoFranja franjaIncio = auxiliarDao.getTipoFranjaByNombre("Inicio");
        TipoFranja franjaPicoAM = auxiliarDao.getTipoFranjaByNombre("Pico AM");
        TipoFranja franjaValle = auxiliarDao.getTipoFranjaByNombre("Valle");
        TipoFranja franjaPicoPM = auxiliarDao.getTipoFranjaByNombre("Pico PM");
        TipoFranja franjaCierre = auxiliarDao.getTipoFranjaByNombre("Cierre");

        List<IntervalosProgramacion> intervalosFranjaInicio= auxiliarDao.getIntervaloByFranja(franjaIncio);
        List<IntervalosProgramacion> intervalosFranjaPicoAM= auxiliarDao.getIntervaloByFranja(franjaPicoAM);
        List<IntervalosProgramacion> intervalosFranjaValle= auxiliarDao.getIntervaloByFranja(franjaValle);
        List<IntervalosProgramacion> intervalosFranjaPicoPM= auxiliarDao.getIntervaloByFranja(franjaPicoPM);
        List<IntervalosProgramacion> intervalosFranjaCierre= auxiliarDao.getIntervaloByFranja(franjaCierre);

        List<TiempoIntervalos> tiemposFranjaInciio = auxiliarDao.getTiempoIntervalosByServicio(intervalosFranjaInicio,servicio,gisIntervalos);
        List<TiempoIntervalos> tiemposFranjaAM = auxiliarDao.getTiempoIntervalosByServicio(intervalosFranjaPicoAM,servicio,gisIntervalos);
        List<TiempoIntervalos> tiemposFranjaValle = auxiliarDao.getTiempoIntervalosByServicio(intervalosFranjaValle,servicio,gisIntervalos);
        List<TiempoIntervalos> tiemposFranjaPM = auxiliarDao.getTiempoIntervalosByServicio(intervalosFranjaPicoPM,servicio,gisIntervalos);
        List<TiempoIntervalos> tiemposFranjaCierre = auxiliarDao.getTiempoIntervalosByServicio(intervalosFranjaCierre,servicio,gisIntervalos);

        List<Intervalos> intervalosLista = new ArrayList<>();
        intervalosLista.add( calcularPromedio(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
        intervalosLista.add( calcularModa(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
        intervalosLista.add(calcularMinimo(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));
        intervalosLista.add( calcularMaximo(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios));

        return intervalosLista;
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
        auxiliarDao.addIntervalos(intervalos);
        return intervalos;
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

    private Time getTime(int instante) {
        int hor = instante / 3600;
        int min=(instante-(3600*hor))/60;
        int seg=instante-((hor*3600)+(min*60));
        Time time= new Time(hor,min,seg);
        return time;
    }

    private int calcularBuses(double promedioInicio) {
        if(promedioInicio!=0){
            return (int) (60/promedioInicio);
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
        auxiliarDao.addIntervalos(intervalos);
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
        auxiliarDao.addIntervalos(intervalos);
        return intervalos;

    }

    private double minimo(List<TiempoIntervalos> tiemposFranjaInciio) {
        if(tiemposFranjaInciio.size()>0){
            return transformarAFormatoTiempo(tiemposFranjaInciio.get(0).getInstante());
        }
        return 0;
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
        auxiliarDao.addIntervalos(intervalos);
        return intervalos;

    }

    private double maximo(List<TiempoIntervalos> tiemposFranjaInciio) {
        if(tiemposFranjaInciio.size()>0){
            return transformarAFormatoTiempo(tiemposFranjaInciio.get(tiemposFranjaInciio.size()-1).getInstante());
        }
        return 0;
    }



}

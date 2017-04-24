package com.tmModulos.controlador.procesador;


import com.tmModulos.modelo.dao.saeBogota.GroupedHorario;
import com.tmModulos.modelo.dao.tmData.NodosDistanciaAuxiliarDao;
import com.tmModulos.modelo.entity.saeBogota.*;
import com.tmModulos.modelo.entity.tmData.DistanciaNodos;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.ServicioDistancia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NodosHilo implements Runnable{

    private List<NodosSeccion> nodosSeccions;
    private List<DistanciaNodos> distanciaNodoses;
    private MatrizDistancia matrizDistancia;
    private Date fechaCalculo;
    private NodosDistanciaAuxiliarDao auxiliarDao;


    public NodosHilo(Date fechaCalculo, MatrizDistancia matrizDistancia) {
        this.fechaCalculo = fechaCalculo;
        this.matrizDistancia = matrizDistancia;
    }

    public NodosHilo(List<NodosSeccion> nodosSeccions, MatrizDistancia matrizDistancia) {
        this.nodosSeccions = nodosSeccions;
        this.matrizDistancia = matrizDistancia;
    }

    public NodosHilo(List<DistanciaNodos> distanciaNodoses) {
        this.distanciaNodoses = distanciaNodoses;
    }

    @Override
    public void run() {
        System.out.println( " is running");
        auxiliarDao = new NodosDistanciaAuxiliarDao();
        List<Vigencias> vigenciasDaoByDate = encontrarVigencias(fechaCalculo);
        if(vigenciasDaoByDate.size()>0){
            List<GroupedHorario> horarioByTipoDia = auxiliarDao.getHorarioByTipoDia(vigenciasDaoByDate.get(0).getTipoDia());
            for (Vigencias vigencia:vigenciasDaoByDate ) {
                int macro = vigencia.getMacro();
                int linea = vigencia.getLinea();
                int config=0;
                int seccion=0;
                String nombreMatriz= "";
                int distancia=0;
                Nodos nodos=null;
                List<GroupedHorario> horarioActual =macroLineaEnHorario(macro,linea,horarioByTipoDia);
                if(horarioActual.size()>0){
                    for( GroupedHorario tablaHorario: horarioActual ){
                        List<Lineas> lineasObj = encontrarLineas(macro, linea);
                        if(lineasObj.size()>0){
                            config= lineasObj.get(0).getConfig();
                        }
                        List<NodosSeccion> nodosSeccions = encontrarNodosSeccion(macro, linea, tablaHorario.getSeccion(), config,1);
                        List<NodosSeccion> nodosProceso= new ArrayList<>();

                        if(nodosSeccions.size()>0){
                            //taskExecutor.execute(new NodosHilo(nodosSeccions,matrizDistancia));
                            for (NodosSeccion nodoSec:nodosSeccions) {
//                        if (nodosProceso.size()<7){
//                            nodosProceso.add(nodoSec);
//                        }else{
//                            taskExecutor.execute(new NodosHilo(nodosProceso,matrizDistancia));
//                            nodosProceso = new ArrayList<>();
//                        }
//                        i++;
                                seccion= nodoSec.getSeccion();
                                nombreMatriz= encontrarNombreMatriz(macro,linea,config,seccion);
                                distancia=nodoSec.getDistancia();
                                nodos= encontrarNodo(nodoSec.getNodo(),nodoSec.getTipo());
                                //Nodo nodo= findOrSaveNodo(nodos.getId(),nodos.getNombre());
                                ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(macro,linea,seccion,nombreMatriz,nodos.getId());
                                guardarDistanciaNodos(matrizDistancia,distancia,servicioDistancia,nodos.getNombre(),nodos.getId());


                            }

                        }
                    }
                }
            }
        }
//        for(NodosSeccion nodosSeccion: nodosSeccions){
//            int seccion= nodosSeccion.getSeccion();
//            String nombreMatriz = encontrarNombreMatriz(nodosSeccion.getMacro(),nodosSeccion.getLinea(),nodosSeccion.getConfigLinea(),
//                    seccion);
//            int distancia=nodosSeccion.getDistancia();
//            Nodos nodos= encontrarNodo(nodosSeccion.getNodo(),nodosSeccion.getTipo());
//            Nodo nodo= findOrSaveNodo(nodos.getId(),nodos.getNombre());
//            ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(nodosSeccion.getMacro(),nodosSeccion.getLinea(),seccion,nombreMatriz);
//            guardarDistanciaNodos(matrizDistancia,nodo,distancia,servicioDistancia);
//        }
//        for(DistanciaNodos nodo:distanciaNodoses){
//            auxiliarDao.addDistanciaNodos(nodo);
//        }
        return;

    }



    public List<NodosSeccion> encontrarNodosSeccion(int macro,int linea, int seccion,int config,int tipoNodo){
        return auxiliarDao.getNodosSeccionesByMacroLineaAndConfig(macro,linea, seccion,config,tipoNodo);
    }

    public List<Lineas> encontrarLineas(int macro,int linea){
        return auxiliarDao.getLineasByMacroAndLinea( macro,linea );
    }

    private List<GroupedHorario> macroLineaEnHorario(int macro, int linea, List<GroupedHorario> horarioByTipoDia) {
        List<GroupedHorario> horarios = new ArrayList<>();
        for(GroupedHorario horario:horarioByTipoDia){
            if(macro == horario.getMacro() && linea == horario.getLinea() ){
                horarios.add(horario);
            }
        }
        return horarios;
    }

    private void guardarDistanciaNodos(MatrizDistancia matrizDistancia, int distancia, ServicioDistancia servicioDistancia,String nodoNombre, int nodoCodigo){
        DistanciaNodos distanciaNodosByServicioAndPunto = auxiliarDao.getDistanciaNodosByServicioAndPunto(servicioDistancia, matrizDistancia,nodoCodigo+"");
        if(distanciaNodosByServicioAndPunto==null){
            DistanciaNodos distanciaNodos=new DistanciaNodos(distancia,matrizDistancia,servicioDistancia);
            distanciaNodos.setNodoNombre(nodoNombre);
            distanciaNodos.setNodoCodigo(nodoCodigo+"");
            auxiliarDao.addDistanciaNodos(distanciaNodos);
        }
    }

    public String encontrarNombreMatriz(int macro,int linea,int config,int seccion){
        Secciones seccionObj = auxiliarDao.getSeccionesByMacroLineaAndConfig(macro,linea,config,seccion);
        if(seccionObj!=null){
            return seccionObj.getNombre();
        }
        return "Sin Nombre";
    }

    public Nodos encontrarNodo(int id, int tipo){
        Nodos nodosByTipoandCode = auxiliarDao.getNodosByTipoandCode(id, tipo);
        return nodosByTipoandCode;
    }

    private Nodo findOrSaveNodo(int nodoCodigo, String nodoNombre) {
        List<Nodo> nodos = auxiliarDao.getNodo( nodoNombre );
        if( nodos.size() == 0 ){
            Nodo nodo = new Nodo(nodoNombre,nodoCodigo);
            auxiliarDao.addNodo( nodo );
            return nodo;
        }else if (nodos.get(0).getCodigo()==null){
            nodos.get(0).setCodigo(nodoCodigo);
            auxiliarDao.updateNodo(nodos.get(0));
        }
        return  nodos.get(0);
    }

    private ServicioDistancia crearOBuscarServicioDistancia(int macro, int linea, int seccion, String nombreMatriz, int nodoCodigo) {
        if(seccion==4 || seccion==3){
            System.out.println("ad");
        }
        String identificador =macro+"-"+linea+"-"+seccion+"-"+nodoCodigo;
        ServicioDistancia servicioDistancia = auxiliarDao.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        if(servicioDistancia==null){
            servicioDistancia = new ServicioDistancia(nombreMatriz,macro,linea,seccion);
            servicioDistancia.setIdentificador(identificador);
            auxiliarDao.addServicioDistancia(servicioDistancia);
        }
        return servicioDistancia;
    }

    public List<Vigencias> encontrarVigencias(Date fecha){
        List<Vigencias> vigenciasDaoByDate = auxiliarDao.getVigenciasDaoByDate(fecha);
        return vigenciasDaoByDate;
    }
}

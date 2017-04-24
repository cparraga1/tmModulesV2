package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="distancia_nodos")
public class DistanciaNodos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="DistanciaGenerator")
    @SequenceGenerator(name="DistanciaGenerator", sequenceName = "matriz_distancia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "distancia")
    private int distancia;

    @Column(name = "nodo_nombre")
    private String nodoNombre;

    @Column(name = "nodo_codigo")
    private String nodoCodigo;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_distancia", nullable = false)
    private MatrizDistancia matrizDistancia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio_distancia", nullable = false)
    private ServicioDistancia servicioDistancia;

    public DistanciaNodos() {
    }

    public DistanciaNodos(int distancia,  MatrizDistancia matrizDistancia, ServicioDistancia servicioDistancia) {
        this.distancia = distancia;
        this.matrizDistancia = matrizDistancia;
        this.servicioDistancia = servicioDistancia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public MatrizDistancia getMatrizDistancia() {
        return matrizDistancia;
    }

    public void setMatrizDistancia(MatrizDistancia matrizDistancia) {
        this.matrizDistancia = matrizDistancia;
    }

    public ServicioDistancia getServicioDistancia() {
        return servicioDistancia;
    }

    public void setServicioDistancia(ServicioDistancia servicioDistancia) {
        this.servicioDistancia = servicioDistancia;
    }

    public String getNodoNombre() {
        return nodoNombre;
    }

    public void setNodoNombre(String nodoNombre) {
        this.nodoNombre = nodoNombre;
    }

    public String getNodoCodigo() {
        return nodoCodigo;
    }

    public void setNodoCodigo(String nodoCodigo) {
        this.nodoCodigo = nodoCodigo;
    }
}

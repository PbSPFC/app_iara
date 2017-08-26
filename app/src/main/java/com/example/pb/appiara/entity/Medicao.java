package com.example.pb.appiara.entity;

/**
 * Created by PB on 19/08/2017.
 */

public class Medicao {

    public Medicao(){
    }

    public Medicao(Long leitura, Float turbidez, Float temperatura, Long luminosidade, Float latitude, Float longitude, String data, String cronometro) {
        this.leitura = leitura;
        this.turbidez = turbidez;
        this.temperatura = temperatura;
        this.luminosidade = luminosidade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
        this.cronometro = cronometro;
    }

    private Long leitura;
    private Float turbidez;
    private Float temperatura;
    private Long luminosidade;
    private Float latitude;
    private Float longitude;
    private String data;
    private String cronometro;

    public Long getLeitura() {
        return leitura;
    }

    public void setLeitura(Long leitura) {
        this.leitura = leitura;
    }

    public Float getTurbidez() {
        return turbidez;
    }

    public void setTurbidez(Float turbidez) {
        this.turbidez = turbidez;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Long getLuminosidade() {
        return luminosidade;
    }

    public void setLuminosidade(Long luminosidade) {
        this.luminosidade = luminosidade;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCronometro() {
        return cronometro;
    }

    public void setCronometro(String cronometro) {
        this.cronometro = cronometro;
    }
}

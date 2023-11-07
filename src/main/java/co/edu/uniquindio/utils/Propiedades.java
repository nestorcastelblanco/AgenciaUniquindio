package co.edu.uniquindio.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Locale;

public class Propiedades {

    @Getter
    private ResourceBundle resourceBundle;
    private static Propiedades instancia;
    private final List<CambioIdiomaListener> listeners;
    private Propiedades(){
        this.resourceBundle = ResourceBundle.getBundle("textos");
        this.listeners = new ArrayList<>();
    }
    public static Propiedades getInstance(){
        if(instancia == null){
            instancia = new Propiedades();
        }
        return instancia;
    }
    public void addCambioIdiomaListener(CambioIdiomaListener listener) {
        listeners.add(listener);
    }
    public void removeCambioIdiomaListener(CambioIdiomaListener listener) {
        listeners.remove(listener);
    }
    public void setLocale(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("textos", locale);
        // Notificar a los escuchadores sobre el cambio de idioma
        CambioIdiomaEvent evento = new CambioIdiomaEvent(this, locale);
        for (CambioIdiomaListener listener : listeners) {
            listener.onCambioIdioma(evento);
        }
    }
}
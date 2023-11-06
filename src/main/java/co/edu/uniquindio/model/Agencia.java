package co.edu.uniquindio.model;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.utils.ArchivoUtils;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.java.Log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.PasswordAuthentication;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@Getter
public class Agencia {
    private final String RUTA_CLIENTES = "src/main/resources/textos/clientes.ser";
    private final String RUTA_GUIAS = "src/main/resources/textos/guias.ser";
    private final String RUTA_DESTINOS = "src/main/resources/textos/destinos.ser";
    private final String RUTA_PAQUETES = "src/main/resources/textos/paquetes.ser";
    private final String RUTA_RESERVAS = "src/main/resources/textos/reservas.ser";
    private Reservas RESERVA_CALIFICACION = new Reservas();
    private Reservas RESERVA_CANCELACION = new Reservas();
    private Reservas RESERVA_EDICION = new Reservas();
    private Paquetes PAQUETE_SELECCIONADO = new Paquetes();
    private Paquetes PAQUETE_EDICION = new Paquetes();
    private Paquetes PAQUETE_RESERVA = new Paquetes();
    private Paquetes PAQUETE_CANCELACION = new Paquetes();
    private Destinos DESTINO_CANCELACION = new Destinos();
    private Destinos DESTINO_EDICION = new Destinos();
    private Clientes CLIENTE_SESION = new Clientes();
    private Guias GUIA_EDICION = new Guias() ;
    private ArrayList<Destinos> destinos = new ArrayList<>();
    private ArrayList<Paquetes> paquetes = new ArrayList<>();
    private ArrayList<Clientes> clientes = new ArrayList<>();
    private ArrayList<Reservas> reservas = new ArrayList<>();
    private ArrayList<Guias> guias = new ArrayList<>();
    private final Logger LOGGER=Logger.getLogger(Agencia.class.getName());
    private static Agencia agencia;
    public void inicializarDatos(){
        leerClientes();
        leerGuias();
        leerDestinos();
        leerPaquetes();
        leerReservas();
    }
    private Agencia()
    {
        try {
            LOGGER.addHandler(new FileHandler("logs.xml", true));
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    public static Agencia getInstance()
    {
        if(agencia== null)
        {
            agencia = new Agencia();
        }
        return agencia;
    }
    private  void leerReservas() {
        reservas =  new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_RESERVAS))) {
            ArrayList<Reservas> paquetes1 = (ArrayList<Reservas>) entrada.readObject();
            System.out.println("Reservas deserializados correctamente.");
            for (Reservas paquete : paquetes1) {
                reservas.add(paquete);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public  void leerGuias()
    {
        guias = new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_GUIAS))) {
            ArrayList<Guias> guias1 = (ArrayList<Guias>) entrada.readObject();
            System.out.println("Guias deserializados correctamente.");
            for (Guias guia : guias1) {
                guias.add(guia);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void leerDestinos()
    {
        destinos = new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_DESTINOS))) {
            ArrayList<Destinos> destinos1 = (ArrayList<Destinos>) entrada.readObject();
            System.out.println("Destinos deserializados correctamente.");
            destinos.addAll(destinos1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void leerPaquetes()
    {
        paquetes = new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_PAQUETES))) {
            ArrayList<Paquetes> paquetes1 = (ArrayList<Paquetes>) entrada.readObject();
            System.out.println("Paquetes deserializados correctamente.");
            for (Paquetes paquete : paquetes1) {
                paquetes.add(paquete);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void leerClientes() {
        clientes = new ArrayList<>();
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_CLIENTES))) {
            ArrayList<Clientes> cliente = (ArrayList<Clientes>) entrada.readObject();
            System.out.println("Clientes deserializados correctamente.");
            for (Clientes paquete : cliente) {
                clientes.add(paquete);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registrarCliente(String nombre, String correo, String direccion,String ciudad, String telefono, String usuario, String contrasena, String id) throws CampoVacioException, CampoObligatorioException, CampoRepetido
    {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (correo == null || correo.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el correo");
        }
        if (!agencia.esCorreoValido(correo)) {
            throw new CampoVacioException("Es necesario ingresar un correo valido");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la direccion.");
        }
        if (agencia.obtenerCliente(id) != null) {
            throw new CampoRepetido("Ya se encuentra un usuario registrado con la identificacion");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la ciudad de residencia");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar su numero de telefono");
        }
        if (agencia.verificarCredenciales(usuario,contrasena)) {
            throw new CampoRepetido("Las credenciales proporcionadas no estan disponibles");
        }

        Clientes cliente = Clientes.builder().
                nombreCompleto(nombre).
                correo(correo).
                direccion(direccion).
                identificacion(id).
                ciudad(ciudad).
                telefono(telefono).
                usuario(usuario).
                contrasena(contrasena).
                busquedas(new ArrayList<>())
                .build();
        clientes.add(cliente);
        ArchivoUtils.serializarArraylistClientes(RUTA_CLIENTES,clientes);
        LOGGER.log(Level.INFO, "Se registro un nuevo cliente");
    }
    public void registrarGuia(String nombre, String exp, String id, String idiomas, Paquetes paqueteGuia) throws CampoRepetido,CampoObligatorioException,CampoVacioException {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (agencia.verificarIdentificacion(id)) {
            throw new CampoRepetido("Ya se encuentra un guia registrado con esta identificacion");
        }
        if (exp == null || exp.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (idiomas == null || idiomas.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if(paqueteGuia == null )
        {
            throw new CampoObligatorioException("Es necesario ingresar el paquete");
        }
        Guias guia = Guias.builder()
                .nombre(nombre)
                .identificacion(id)
                .exp(exp)
                .contViajes(0)
                .promedioCalificacion(0)
                .paquete(paqueteGuia)
                .calificaciones(new ArrayList<>())
                .build();
        guia.setLenguajes(llenarArrayIdioma(idiomas));
        guias.add(guia);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS,guias);
        LOGGER.log(Level.INFO, "Se registro un nuevo guia");
    }
    public void registrarDestino(String nombre, String ciudad, String descripcion, ArrayList<String> imagenes, String clima) throws CampoRepetido,CampoObligatorioException,CampoVacioException{
        if (ciudad == null || ciudad.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoRepetido("Ya se encuentra un Destino registrado con este nombre");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar la descripcion");
        }
        if (imagenes == null || imagenes.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar imagenes del Destino");
        }
        if (clima == null || clima.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el clima del Destino");
        }
        Random random = new Random();
        Destinos destino = Destinos.builder()
                .nombre(nombre)
                .ciudad(ciudad)
                .clima(clima)
                .descripcion(descripcion)
                .contBusquedas(0)
                .contBusquedas(0)
                .numero(random.nextInt(10000))
                .calificaciones(new ArrayList<>())
                .build();
        destino.setImagenes(imagenes);
        destinos.add(destino);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS,destinos);
        LOGGER.log(Level.INFO, "Se registro un nuevo Destino");
    }
    public void registrarPaquete(String nombre, ArrayList<Destinos> destinos, LocalDate inicio, LocalDate fin, String servicios,String personas, String valor) throws CampoRepetido,CampoVacioException,CampoObligatorioException
    {
        if (nombre== null || nombre.isEmpty() || !agencia.verificarNombrePaquete(nombre)) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre o el nombre es invalido");
        }
        if (destinos == null || destinos.isEmpty()) {
            throw new CampoRepetido("No se añadieron destinos al paquete");
        }
        if(inicio == null){
            throw new CampoObligatorioException("La fecha de inicio ingresada es erronea");
        }
        if(fin == null){
            throw new CampoObligatorioException("La fecha de finalizacion ingresada es erronea");
        }
        if (agencia.verificarFechas(inicio, fin) == false) {
            throw new CampoObligatorioException("Las fechas ingresadas son erroneas");
        }
        if (personas == null || personas.isEmpty() || Float.valueOf(personas) <=0 || !verificarNumero(personas)) {
            throw new CampoObligatorioException("Es necesario ingresar el numero valido de personas");
        }
        if (servicios == null || servicios.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar los servicios del paquete");
        }
        if (valor.isEmpty() || valor == null || Float.valueOf(valor)<= 0 || valor.isEmpty() || !verificarNumero(valor)) {
            throw new CampoObligatorioException("Se crearon valores en el precio erroneos");
        }
        Paquetes paquete = Paquetes.builder().
                nombre(nombre)
                .destinos(destinos)
                .precio(Float.parseFloat(valor))
                .inicio(inicio)
                .fin(fin)
                .duracion(inicio.until(fin, ChronoUnit.DAYS)+"")
                .servicios(servicios)
                .cantReservas(0)
                .numeroPersonas(Integer.parseInt(personas))
                .build();
        paquetes.add(paquete);
        for (int i = 0 ; i<paquetes.size();i++)
        {
            for (int j=0 ; j<paquetes.get(i).getDestinos().size();j++)
            {
                System.out.println("Nombre: " + paquetes.get(i).getNombre() + " Destinos: " + paquetes.get(i).getDestinos().get(j));
            }
        }
        borrarDatosSerializados(RUTA_PAQUETES);
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
        leerPaquetes();
        LOGGER.log(Level.INFO, "Se registro un nuevo Paquete");
    }
    public void realizarReserva(Paquetes paquete, Clientes cliente, LocalDate inicio, LocalDate fin, String personas, Guias selectedItem, String pendiente) throws CampoRepetido,CampoObligatorioException,CampoVacioException {
        Reservas reserva = new Reservas();
        if (paquete == null) {
            throw new CampoObligatorioException("No se cargo el paquete");
        }
        if (cliente == null ) {
            throw new CampoRepetido("No se añadieron destinos al paquete");
        }
        if (!agencia.verificarFechasReserva(inicio, fin, paquete)) {
            throw new CampoObligatorioException("Las fechas ingresadas son erroneas");
        }
        if (personas == null || personas.isEmpty() || Float.valueOf(personas) <=0 || !verificarNumero(personas)) {
            throw new CampoObligatorioException("El numero de personas sobrepasa el cupo");
        }
        if (!agencia.verificarPersonasPaquete(paquete,personas)) {
            throw new CampoObligatorioException("El numero de personas no es valido");
        }
        System.out.println("guia seleccionado" + selectedItem);
        if(selectedItem == null)
        {
            System.out.println("ENTRO");
            Guias guia = Guias.builder()
                    .nombre("SIN GUIA")
                    .build();
            reserva.setGuia(guia);
        }else{
            reserva.setGuia(selectedItem);
        }
        int numeroPersonas = Integer.parseInt(personas);
        new Thread(new Runnable() {
            @Override
            public void run() {
                agencia.enviarCorreo(cliente,paquete,inicio,fin,personas, selectedItem,pendiente);
            }
        }){
        }.start();
        reserva.setPaquete(paquete);
        reserva.setCliente(cliente);
        reserva.setFechaSolicitud(inicio);
        reserva.setFechaPlanificada(fin);
        reserva.setNumeroPersonas(numeroPersonas);
        reserva.setEstado(pendiente);
        reserva.setCalificacion(false);
        Random random = new Random();
        reserva.setCodigo(random.nextInt(10000));
        reservas.add(reserva);
        ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
        /*for (int i = 0 ; i< paquetes.size() ; i++)
        {
            if (paqueteSeleccion().equals(paquetes.get(i)))
            {
                paquetes.get(i).setNumeroPersonas(paquetes.get(i).getNumeroPersonas()-numeroPersonas);
                paquetes.get(i).setCantReservas(paquetes.get(i).getCantReservas()+1);
                paquetes.set(i,paquetes.get(i));
                for(int x = 0 ; x<paqueteSeleccion().getDestinos().size();x++)
                {
                    for(int j = 0; j<destinos.size();j++)
                    {
                        if(destinos.get(j).getNombre().equals(paqueteSeleccion().getDestinos().get(x).getNombre()))
                        {
                            System.out.println("Se actualiza ");
                            destinos.get(j).setContReservas(destinos.get(j).getContReservas()+1);
                            destinos.set(j, destinos.get(j));
                        }
                    }
                }
            }
        }
        LOGGER.log(Level.INFO, "Se realizo la reserva de un Paquete");
        borrarDatosSerializados(RUTA_PAQUETES);
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
         */
        actualizarPaquetesRecursivo(0,0,numeroPersonas);
        for(int i = 0 ; i<reservas.size();i++)
        {
            System.out.println(reservas.get(i).getCliente().getNombreCompleto() + " Codigo: " + reservas.get(i).getCodigo());
        }
    }
    public void actualizarPaquetesRecursivo(int i, int x, int numeroPersonas) {
        if (i < paquetes.size()) {
            if (paqueteSeleccion().getNombre().equals(paquetes.get(i).getNombre())) {
                paquetes.get(i).setNumeroPersonas(paquetes.get(i).getNumeroPersonas() - numeroPersonas);
                paquetes.get(i).setCantReservas(paquetes.get(i).getCantReservas() + 1);
                paquetes.set(i, paquetes.get(i));
                actualizarDestinosRecursivo(x);
            }
            actualizarPaquetesRecursivo(i + 1, 0, numeroPersonas); // Llamada recursiva con i incrementado y x y j reiniciados a 0
        } else {
            LOGGER.log(Level.INFO, "Se realizó la reserva de un Paquete");
            borrarDatosSerializados(RUTA_PAQUETES);
            ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
        }
    }

    private void actualizarDestinosRecursivo(int x) {
        if (x < paqueteSeleccion().getDestinos().size()) {
            String nombreDestinoPaquete = paqueteSeleccion().getDestinos().get(x).getNombre();
            for (int j = 0; j < destinos.size(); j++) {
                if (destinos.get(j).getNombre().equals(nombreDestinoPaquete)) {
                    System.out.println("Se actualiza ");
                    destinos.get(j).setContReservas(destinos.get(j).getContReservas() + 1);
                }
            }
            actualizarDestinosRecursivo(x + 1);
        } else {
            borrarDatosSerializados(RUTA_DESTINOS);
            ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS, destinos);
        }
    }
    public void realizarEdicion(String nombre, String correo, String direccion, String id, String ciudad, String telefono, String usuario, String contrasena) throws CampoRepetido,CampoObligatorioException,CampoVacioException {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (correo == null || correo.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el correo");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la direccion.");
        }
        if (id == null || id.isEmpty()) {
            throw new CampoRepetido("Ya se encuentra un usuario registrado con la identificacion");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la ciudad de residencia");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar su numero de telefono");
        }
        if (!CLIENTE_SESION.getUsuario().equals(usuario) || !CLIENTE_SESION.getContrasena().equals(contrasena))
        {
            if (agencia.verificarCredenciales(usuario,contrasena)) {
                throw new CampoRepetido("Las credenciales proporcionadas no estan disponibles");
            }
        }
        /*
        for (int i = 0 ; i< clientes.size() ; i++)
        {
            if (CLIENTE_SESION.equals(clientes.get(i))) {
                clientes.get(i).setNombreCompleto(nombre);
                clientes.get(i).setCorreo(correo);
                clientes.get(i).setDireccion(direccion);
                clientes.get(i).setCiudad(ciudad);
                clientes.get(i).setIdentificacion(id);
                clientes.get(i).setTelefono(telefono);
                clientes.get(i).setUsuario(usuario);
                clientes.get(i).setContrasena(contrasena);
                clientes.set(i,clientes.get(i));
                CLIENTE_SESION = clientes.get(i);
            }
        }
         */
        actualizarClientesRecursivo(0,nombre,correo,direccion,ciudad,id,telefono,usuario,contrasena);
        borrarDatosSerializados(RUTA_CLIENTES);
        ArchivoUtils.serializarArraylistClientes(RUTA_CLIENTES, clientes);
        for(int i = 0; i<clientes.size();i++)
        {
            System.out.println(clientes.get(i).getNombreCompleto() + " " + clientes.get(i).getCorreo() + " " );
            System.out.println(clientes.get(i).getDireccion() + " " + clientes.get(i).getCiudad() + " " );
            System.out.println(clientes.get(i).getIdentificacion() + " " + clientes.get(i).getTelefono() + " " );
            System.out.println(clientes.get(i).getUsuario() + " " + clientes.get(i).getContrasena() + " " );
        }
    }
    public void actualizarClientesRecursivo(int i, String nombre, String correo, String direccion, String ciudad, String id, String telefono, String usuario, String contrasena) {
        if (i < clientes.size()) {
            if (CLIENTE_SESION.equals(clientes.get(i))) {
                clientes.get(i).setNombreCompleto(nombre);
                clientes.get(i).setCorreo(correo);
                clientes.get(i).setDireccion(direccion);
                clientes.get(i).setCiudad(ciudad);
                clientes.get(i).setIdentificacion(id);
                clientes.get(i).setTelefono(telefono);
                clientes.get(i).setUsuario(usuario);
                clientes.get(i).setContrasena(contrasena);
                clientes.set(i, clientes.get(i));
                CLIENTE_SESION = clientes.get(i);
            }
            actualizarClientesRecursivo(i + 1, nombre, correo, direccion, ciudad, id, telefono, usuario, contrasena);
        }
    }
    public void editarGuia(String nombre, String exp, String id, String idiomas, Paquetes paqueteGuia) throws CampoRepetido,CampoObligatorioException,CampoVacioException {
        if(!GUIA_EDICION.getNombre().equals(nombre))
        {
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioException("Es necesario ingresar el nombre");
            }
        }
        if(!GUIA_EDICION.getIdentificacion().equals(id)) {
            if (agencia.verificarIdentificacion(id)) {
                throw new CampoRepetido("Ya se encuentra un guia registrado con esta identificacion");
            }
        }
        if(!GUIA_EDICION.getExp().equals(exp)) {
            if (exp == null || exp.isEmpty()) {
                throw new CampoObligatorioException("Es necesario ingresar el nombre");
            }
        }
        if(!GUIA_EDICION.getLenguajes().equals(idiomas)) {
            if (idiomas == null || idiomas.isEmpty()) {
                throw new CampoObligatorioException("Es necesario ingresar el nombre");
            }
        }
        if(paqueteGuia == null )
        {
            throw new CampoObligatorioException("Es necesario ingresar el paquete");
        }
        /*
        for (int i = 0; i<guias.size();i++)
        {
            if(guias.get(i).getIdentificacion().equals(GUIA_EDICION.getIdentificacion()))
            {
                Guias guia = Guias.builder()
                        .nombre(nombre)
                        .identificacion(id)
                        .exp(exp)
                        .contViajes(GUIA_EDICION.getContViajes())
                        .promedioCalificacion(GUIA_EDICION.getPromedioCalificacion())
                        .paquete(paqueteGuia)
                        .calificaciones(GUIA_EDICION.getCalificaciones())
                        .build();
                guia.setLenguajes(llenarArrayIdioma(idiomas));
                guias.set(i,guia);
            }
        }
        */
        actualizarGuiasRecursivo(0,nombre,id,exp,paqueteGuia,idiomas);
        borrarDatosSerializados(RUTA_GUIAS);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS,guias);
        LOGGER.log(Level.INFO, "Se edito un guia");
        for(int i = 0; i<guias.size();i++)
        {
            System.out.println(guias.get(i).getNombre() + " " + guias.get(i).getIdentificacion() + " " );
            System.out.println(guias.get(i).getExp() + " " + guias.get(i).getPaquete().getNombre() + " " );
            System.out.println(obtenerIdiomas(guias.get(i).getLenguajes()));
        }
    }
    public void actualizarGuiasRecursivo(int i, String nombre, String id, String exp, Paquetes paqueteGuia, String idiomas) {
        if (i < guias.size()) {
            if (guias.get(i).getIdentificacion().equals(GUIA_EDICION.getIdentificacion())) {
                Guias guia = Guias.builder()
                        .nombre(nombre)
                        .identificacion(id)
                        .exp(exp)
                        .contViajes(GUIA_EDICION.getContViajes())
                        .promedioCalificacion(GUIA_EDICION.getPromedioCalificacion())
                        .paquete(paqueteGuia)
                        .calificaciones(GUIA_EDICION.getCalificaciones())
                        .build();
                guia.setLenguajes(llenarArrayIdioma(idiomas));
                guias.set(i, guia);
            }
            actualizarGuiasRecursivo(i + 1, nombre, id, exp, paqueteGuia, idiomas);
        }
    }
    public void editarDestino(String nombre, String ciudad, String descripcion, ArrayList<String> imagenes, String clima) throws CampoRepetido,CampoObligatorioException,CampoVacioException{
        if (ciudad == null || ciudad.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoRepetido("Ya se encuentra un Destino registrado con este nombre");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar la descripcion");
        }
        if (imagenes == null || imagenes.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar imagenes del Destino");
        }
        if (clima == null || clima.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el clima del Destino");
        }
        actualizarDestinoRecursivo(0,nombre,ciudad,descripcion,imagenes,clima);
        borrarDatosSerializados(RUTA_DESTINOS);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS,destinos);
        LOGGER.log(Level.INFO, "Se edito un Destino");
    }
    public void actualizarDestinoRecursivo(int i,String nombre, String ciudad, String descripcion, ArrayList<String> imagenes, String clima) {
        if (i < destinos.size()) {
            if (destinos.get(i).getNumero() == DESTINO_EDICION.getNumero()) {
                Destinos destino = Destinos.builder()
                        .nombre(nombre)
                        .ciudad(ciudad)
                        .descripcion(descripcion)
                        .imagenes(imagenes)
                        .clima(clima)
                        .numero(DESTINO_EDICION.getNumero())
                        .calificaciones(DESTINO_EDICION.getCalificaciones())
                        .contBusquedas(DESTINO_EDICION.getContBusquedas())
                        .build();
                destinos.set(i,destino);
            }
            actualizarDestinoRecursivo(i + 1, nombre, ciudad, descripcion,imagenes,clima);
        }
    }
    public void editarPaquetes(Paquetes paqueteE, String nombre, ArrayList<Destinos> destinos, LocalDate inicio, LocalDate fin, String servicios,String personas, String valor) throws CampoRepetido,CampoVacioException,CampoObligatorioException {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (destinos == null || destinos.isEmpty()) {
            throw new CampoRepetido("No se añadieron destinos al paquete");
        }
        if (!agencia.verificarFechas(inicio, fin)) {
            throw new CampoObligatorioException("Las fechas ingresadas son erroneas");
        }
        if (personas == null || personas.isEmpty() || Float.valueOf(personas) < 0 || !verificarNumero(personas)) {
            throw new CampoObligatorioException("Es necesario ingresar el numero valido de personas");
        }
        if (servicios == null || servicios.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar los servicios del paquete");
        }
        if (valor == null || Float.valueOf(valor) <= 0 || valor.isEmpty() || !verificarNumero(valor)) {
            throw new CampoObligatorioException("Se crearon valores en el precio erroneos");
        }
        for (Paquetes paquete : paquetes) {
            if (paqueteE.getNombre().equals(paquete.getNombre())) {
                paquete.setNombre(nombre);
                paquete.setDestinos(destinos);
                paquete.setDuracion(inicio.until(fin, ChronoUnit.DAYS) + "");
                paquete.setServicios(servicios);
                paquete.setPrecio(Float.parseFloat(valor));
                paquete.setNumeroPersonas(paquete.getNumeroPersonas() + Integer.parseInt(personas));
                paquete.setInicio(inicio);
                paquete.setFin(fin);
            }
        }
        borrarDatosSerializados(RUTA_PAQUETES);
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
        LOGGER.info("Se ha actualizado al paquete con nombre: " + nombre);
    }
    public void actualizarPaquetesRecursivo(int i,String nombre,ArrayList<Destinos> destinos, LocalDate inicio, LocalDate fin, String servicios, String valor, String personas) {
        if (i<paquetes.size()) {
            if (PAQUETE_EDICION.getNombre().equals(paquetes.get(i).getNombre())) {
                Paquetes paquete = new Paquetes();
                paquete.setNombre(nombre);
                paquete.setDestinos(destinos);
                paquete.setDuracion(inicio.until(fin, ChronoUnit.DAYS) + "");
                paquete.setServicios(servicios);
                paquete.setPrecio(Float.parseFloat(valor));
                paquete.setNumeroPersonas(paquetes.get(i).getNumeroPersonas() + Integer.parseInt(personas));
                paquete.setInicio(inicio);
                paquete.setFin(fin);
                paquetes.set(i,paquete);
                borrarDatosSerializados(RUTA_PAQUETES);
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
                LOGGER.info("Se ha actualizado al paquete con nombre: " + nombre);
            }
            actualizarPaquetesRecursivo(i+1, nombre, destinos, inicio, fin, servicios, valor, personas);
        }
    }
    public void editarReserva(Paquetes paquete, LocalDate inicio, LocalDate fin, String agregarPersonas, String quitarPersonas, Guias selectedItem, String pendiente) throws CampoRepetido,CampoObligatorioException,CampoVacioException {
        if (paquete == null) {
            throw new CampoObligatorioException("No se cargo el paquete");
        }
        if (!inicio.isEqual(RESERVA_EDICION.getFechaSolicitud()) || !fin.isEqual(RESERVA_EDICION.getFechaPlanificada())) {
            if (!agencia.verificarFechasReserva(inicio, fin, paquete)) {
                throw new CampoObligatorioException("Las fechas ingresadas son erroneas");
            }
        }
        if (agregarPersonas == null || agregarPersonas.isEmpty() || Integer.parseInt(agregarPersonas) < 0 || !verificarNumero(agregarPersonas)) {
            throw new CampoObligatorioException("El numero de personas sobrepasa el cupo");
        }
        if (!agencia.verificarPersonasPaquete(paquete, agregarPersonas)) {
            throw new CampoObligatorioException("El numero de personas no es valido");
        }
        if (quitarPersonas == null || quitarPersonas.isEmpty() || Integer.parseInt(quitarPersonas) < 0 || !verificarNumero(quitarPersonas)) {
            throw new CampoObligatorioException("El numero de personas sobrepasa el cupo");
        }
        if ((RESERVA_EDICION.getNumeroPersonas() - Integer.parseInt(quitarPersonas)) <= 0) {
            throw new CampoObligatorioException("Se trato de dejar la cantidad de personas menor o igual a 0");
        }
        if(selectedItem == null || selectedItem.getNombre().equals("SIN GUIA") || selectedItem.getIdentificacion() == null)
        {
            selectedItem = new Guias();
            selectedItem.setNombre("SIN GUIA");
            selectedItem.setIdentificacion(null);
        }
        int numeroPersonas = Integer.parseInt(agregarPersonas);
        int numeroPersonas1 = Integer.parseInt(quitarPersonas);
        /*
        for (Reservas reserva : reservas) {
            if (RESERVA_EDICION.getCodigo() == reserva.getCodigo()) {
                reserva.setPaquete(paquete);
                reserva.setCliente(RESERVA_EDICION.getCliente());
                reserva.setFechaSolicitud(inicio);
                reserva.setFechaPlanificada(fin);
                reserva.setNumeroPersonas(RESERVA_EDICION.getNumeroPersonas() + numeroPersonas - numeroPersonas1);
                reserva.setEstado(pendiente);
                reserva.setGuia(selectedItem);
                reserva.setCalificacion(RESERVA_EDICION.calificacion);
            }
        }
         */
        actualizarReservaRecursivo(0,paquete, inicio,fin,numeroPersonas,numeroPersonas1,selectedItem, pendiente);
        LOGGER.log(Level.INFO, "Se realizo la edicion de una Reserva");
        borrarDatosSerializados(RUTA_RESERVAS);
        ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
        actualizarPaquetesRecursivoReserva(0,paquete,numeroPersonas1,numeroPersonas);
        LOGGER.log(Level.INFO, "Se realizo la edicion de un Paquete con base a la edicion de la reserva");
        borrarDatosSerializados(RUTA_PAQUETES);
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
        /*
        for (int i = 0 ; i< paquetes.size() ; i++)
        {
            System.out.print("Paquete seleccionado :" + paquete.getNombre() + " "+ paquete.getDestinos());
            System.out.print("Paquete array :" + paquetes.get(i).getNombre() + " "+ paquetes.get(i).getDestinos());
            if (paquete.getNombre().equals(paquetes.get(i).getNombre())) {
                System.out.print("Paquete encontrado :" + paquetes.get(i).getNombre() + " "+ paquetes.get(i).getDestinos());
                paquetes.get(i).setNombre(paquete.getNombre());
                paquetes.get(i).setDestinos(paquete.getDestinos());
                paquetes.get(i).setPrecio(Float.valueOf(paquete.getPrecio()));
                paquetes.get(i).setInicio(paquete.getInicio());
                paquetes.get(i).setFin(paquete.getFin());
                paquetes.get(i).setServicios(paquete.getServicios());
                paquetes.get(i).setNumeroPersonas(paquete.getNumeroPersonas() + numeroPersonas1 - numeroPersonas);
                paquetes.get(i).setDuracion(paquete.getDuracion());
                paquetes.set(i,paquetes.get(i));;
                LOGGER.log(Level.INFO, "Se realizo la edicion de un Paquete con base a la edicion de la reserva");
                borrarDatosSerializados(RUTA_PAQUETES);
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
            }
        }
         */
    }
    public void actualizarReservaRecursivo(int i ,Paquetes paquete, LocalDate inicio, LocalDate fin, int numeroPersonas, int numeroPersonas1, Guias selectedItem, String pendiente) {
        if (i<reservas.size()) {
            if (RESERVA_EDICION.getCodigo() == reservas.get(i).getCodigo()) {
                reservas.get(i).setPaquete(paquete);
                reservas.get(i).setCliente(RESERVA_EDICION.getCliente());
                reservas.get(i).setFechaSolicitud(inicio);
                reservas.get(i).setFechaPlanificada(fin);
                reservas.get(i).setNumeroPersonas(RESERVA_EDICION.getNumeroPersonas() + numeroPersonas - numeroPersonas1);
                reservas.get(i).setEstado(pendiente);
                reservas.get(i).setGuia(selectedItem);
                reservas.get(i).setCalificacion(RESERVA_EDICION.calificacion);
            }
            actualizarReservaRecursivo(i+1,paquete,inicio,fin,numeroPersonas,numeroPersonas1,selectedItem,pendiente);
        }
    }
    public void actualizarPaquetesRecursivoReserva(int i,Paquetes paquete,int numeroPersonas1, int numeroPersonas) {
        if (i<paquetes.size()) {
            if (paquete.getNombre().equals(paquetes.get(i).getNombre())) {
                System.out.print("Paquete encontrado :" + paquetes.get(i).getNombre() + " "+ paquetes.get(i).getDestinos());
                paquetes.get(i).setNumeroPersonas(paquete.getNumeroPersonas() + numeroPersonas1 - numeroPersonas);
                paquetes.set(i,paquetes.get(i));;
                LOGGER.log(Level.INFO, "Se realizo la edicion de un Paquete con base a la edicion de la reserva");
                borrarDatosSerializados(RUTA_PAQUETES);
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
            }
            actualizarPaquetesRecursivoReserva(i+1, paquete,numeroPersonas1,numeroPersonas);
        }
    }
    public Clientes obtenerCliente(String id)
    {
        return obtenerClienteRecursivo(id,0);
        /*
        Clientes cliente = null;
        for ( int i = 0 ; i<clientes.size();i++)
        {
            if(clientes.get(i).getIdentificacion().equals(id))
            {
                cliente =  clientes.get(i);
            }
        }
        return cliente;
         */
    }
    public Clientes obtenerClienteRecursivo(String id, int index) {
        if (index == clientes.size()) {
            return null;
        }
        if (clientes.get(index).getIdentificacion().equals(id)) {
            return clientes.get(index);
        } else {
            return obtenerClienteRecursivo(id, index + 1);
        }
    }
    public Clientes clienteSesion() {
        return CLIENTE_SESION;
    }
    public Reservas getReservaCalificacion() {
        return RESERVA_CALIFICACION;
    }
    public ArrayList<Destinos> enviarDestinos ()
    {
        return destinos;
    }
    public ArrayList<Paquetes> enviarPaquetes ()
    {
        return paquetes;
    }
    public ArrayList<Reservas> enviarReservas()
    {
        return reservas;
    }
    public ArrayList<Guias> enviarGuias() {
        return guias;
    }
    public void enviarGuiaEdicion(Guias selectedItem) {
        GUIA_EDICION = selectedItem;
    }
    public Paquetes enviarPaqueteEdicion() {
        return PAQUETE_EDICION;
    }
    public Reservas enviarReservaEdicion() {
        return RESERVA_EDICION;
    }
    public Paquetes paqueteSeleccion() {
        return PAQUETE_SELECCIONADO;
    }
    public ArrayList<Guias> enviarGuiasPaquete(Paquetes paquete) {
        /*
        for (int i = 0 ; i<guias.size();i++){
            if(guias.get(i).getPaquete().getNombre().equals(paquete.getNombre()))
            {
                guiasCorrespondientes.add(guias.get(i));
            }
        }

         */
        return enviarGuiasPaqueteRecursivo(paquete,0);
    }
    public ArrayList<Guias> enviarGuiasPaqueteRecursivo(Paquetes paquete, int index) {
        if (index == guias.size()) {
            return new ArrayList<>();
        }
        ArrayList<Guias> guiasCorrespondientes = new ArrayList<>();
        if (guias.get(index).getPaquete().getNombre().equals(paquete.getNombre())) {
            guiasCorrespondientes.add(guias.get(index));
        }
        guiasCorrespondientes.addAll(enviarGuiasPaqueteRecursivo( paquete, index + 1));
        return guiasCorrespondientes;
    }
    public ArrayList<Reservas> enviarReservasCliente() {
        ArrayList<Reservas> reservasCliente = new ArrayList<>();
        reservas.removeAll(reservas);
        leerReservas();
        /*
        for (int i = 0 ; i<reservas.size();i++)
        {
            if (reservas.get(i).getCliente().getIdentificacion().equals(CLIENTE_SESION.getIdentificacion()))
            {
                reservasCliente.add(reservas.get(i));
            }
        }
         */
        return enviarReservasClienteRecursivo(0,reservasCliente);
    }
    public ArrayList<Reservas> enviarReservasClienteRecursivo(int index, ArrayList<Reservas> reservasCliente) {
        if (index == reservas.size()) {
            return reservasCliente;
        }
        if (reservas.get(index).getCliente().getIdentificacion().equals(CLIENTE_SESION.getIdentificacion())) {
            // Si es igual, agrega la reserva actual a la lista de reservas del cliente
            reservasCliente.add(reservas.get(index));
        }
        return enviarReservasClienteRecursivo(index + 1, reservasCliente);
    }
    public void recibirPaqueteEdicion(Paquetes selectedItem) {
        PAQUETE_EDICION= selectedItem;
    }
    public Guias recibirGuiaEdicion() {
        return GUIA_EDICION;
    }
    public Destinos recibirDestinoEdicion(Destinos destinos)
    {
        return DESTINO_EDICION = destinos;
    }
    public void recibirReservaEdicion(Reservas selectedItem) {
        RESERVA_EDICION = selectedItem;
    }
    public void recibirPaqueteCancelacion(Paquetes selectedItem) {
        PAQUETE_CANCELACION= selectedItem;
        /*for (int i = 0 ; i<paquetes.size();i++)
        {
            if (paquetes.get(i).getNombre().equals(PAQUETE_CANCELACION.getNombre()))
            {
                paquetes.remove(i);
            }
        }
         */
        cancelarPaqueteRecursivo(0);
        borrarDatosSerializados(RUTA_PAQUETES);
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
    }
    public void cancelarPaqueteRecursivo(int index) {
        if (index == paquetes.size()) {
            return;
        }
        if (paquetes.get(index).getNombre().equals(PAQUETE_CANCELACION.getNombre())) {
            paquetes.remove(index);
            cancelarPaqueteRecursivo(index);
        } else {
            cancelarPaqueteRecursivo(index + 1);
        }
    }
    public void recibirDestinoCancelacion(Destinos selectedItem) {
        DESTINO_CANCELACION = selectedItem;
        /*for (int i = 0 ; i<paquetes.size();i++)
        {
            if (paquetes.get(i).getNombre().equals(PAQUETE_CANCELACION.getNombre()))
            {
                paquetes.remove(i);
            }
        }
         */
        cancelarDestinoRecursivo(0);
        borrarDatosSerializados(RUTA_DESTINOS);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS,destinos);
    }
    public void cancelarDestinoRecursivo(int index) {
        if (index == destinos.size()) {
            return;
        }
        if (destinos.get(index).getNombre().equals(DESTINO_CANCELACION.getNombre())) {
            destinos.remove(index);
            cancelarDestinoRecursivo(index);
        } else {
            cancelarDestinoRecursivo(index + 1);
        }
    }
    public void recibirReservaCancelacion(Reservas selectedItem) {
        RESERVA_CANCELACION = selectedItem;
        cancelarReservaRecursivo(0,0);
        /*for (int i = 0; i < reservas.size(); i++) {

            if (reservas.get(i).getCodigo() == RESERVA_CANCELACION.getCodigo()) {

                for (int j = 0; j < paquetes.size(); j++) {

                    if (paquetes.get(j).getNombre().equals(RESERVA_CANCELACION.getPaquete().getNombre())) {

                        paquetes.get(j).setNumeroPersonas(paquetes.get(j).getNumeroPersonas() + RESERVA_CANCELACION.getNumeroPersonas());
                        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
                    }
                }
                reservas.remove(i);
                ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS, reservas);
                leerReservas();
            }
        }
         */
    }
    public void cancelarReservaRecursivo(int reservaIndex, int paqueteIndex) {
        if (reservaIndex == reservas.size()) {
            return;
        }
        if (reservas.get(reservaIndex).getCodigo() == RESERVA_CANCELACION.getCodigo()) {
            while (paqueteIndex < paquetes.size() &&
                    !paquetes.get(paqueteIndex).getNombre().equals(RESERVA_CANCELACION.getPaquete().getNombre())) {
                paqueteIndex++;
            }
            if (paqueteIndex < paquetes.size()) {
                paquetes.get(paqueteIndex).setNumeroPersonas(paquetes.get(paqueteIndex).getNumeroPersonas() + RESERVA_CANCELACION.getNumeroPersonas());
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
            }
            reservas.remove(reservaIndex);
            ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS, reservas);
            leerReservas();
        } else {
            cancelarReservaRecursivo(reservaIndex + 1, paqueteIndex);
        }
    }
    public void recibirGuiaEliminado(Guias selectedItem) {
        /*
        for (int i = 0 ; i<guias.size();i++)
        {
            if (guias.get(i).getIdentificacion().equals(selectedItem.getIdentificacion()))
            {
                guias.remove(i);
            }
        }
         */
        eliminarGuiaRecursivo(selectedItem,0);
        borrarDatosSerializados(RUTA_GUIAS);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS,guias);
    }
    public void eliminarGuiaRecursivo(Guias guiaEliminar, int index) {
        if (index == guias.size()) {
            return;
        }
        if (guias.get(index).getIdentificacion().equals(guiaEliminar.getIdentificacion())) {
            guias.remove(index);
            eliminarGuiaRecursivo(guiaEliminar, index);
        } else {
            eliminarGuiaRecursivo(guiaEliminar, index + 1);
        }
    }
    public boolean recibirReservaCalificacion(Reservas selectedItem) {
        RESERVA_CALIFICACION = selectedItem;
        /*
        boolean state =false;
        for (int i = 0 ; i<reservas.size();i++)
        {
            if (reservas.get(i).getCodigo() == RESERVA_CALIFICACION.getCodigo())
            {
                if(reservas.get(i).getFechaPlanificada().isBefore(LocalDate.now()) && !reservas.get(i).isCalificacion())
                {
                    RESERVA_CALIFICACION= selectedItem;
                    state = true;
                    LOGGER.log(Level.INFO, "El paquete puede ser calificado");
                }else{
                    state = false;
                    LOGGER.log(Level.INFO, "El paquete no puede ser calificado");
                }
            }
        }
         */
        return verificarCalificacionRecursivo(selectedItem,0);
    }
    public boolean verificarCalificacionRecursivo(Reservas reservaCalificacion, int index) {
        if (index == reservas.size()) {
            return false;
        }
        if (reservas.get(index).getCodigo() == reservaCalificacion.getCodigo()) {
            if (reservas.get(index).getFechaPlanificada().isBefore(LocalDate.now()) && !reservas.get(index).isCalificacion()) {
                LOGGER.log(Level.INFO, "El paquete puede ser calificado");
                return true;
            } else {
                LOGGER.log(Level.INFO, "El paquete no puede ser calificado");
                return false;
            }
        }
        return verificarCalificacionRecursivo(reservaCalificacion, index + 1);
    }
    public void recibirPaqueteSeleccionado(Paquetes selectedItem) {
        PAQUETE_SELECCIONADO = selectedItem;
        borrarDatosSerializados(RUTA_CLIENTES);
        borrarDatosSerializados(RUTA_DESTINOS);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS, destinos);
        ArchivoUtils.serializarArraylistClientes(RUTA_CLIENTES, clientes);
    }
    public void ingresarCliente(String usuario, String contrasena) throws CampoRepetido
    {
        if (agencia.buscarCliente(usuario,contrasena) == false) {
            throw new CampoRepetido("Las credenciales proporcionadas son erroneas");
        }else{
            LOGGER.log(Level.INFO, "Se genero el ingreso de un cliente: " + CLIENTE_SESION.getNombreCompleto() );
        }
    }
    public void ingresarAdmin(String usuario, String contrasena) throws CampoRepetido
    {
        if (!agencia.buscarAdmin(usuario,contrasena)) {
            throw new CampoRepetido("Las credenciales proporcionadas son erroneas");
        }else{
            LOGGER.log(Level.INFO, "Se genero el ingreso de un cliente: " + CLIENTE_SESION.getNombreCompleto() );
        }
    }
    public boolean buscarCliente (String usuario, String contrasena)
    {
        /*
        boolean state = false;
        for (Clientes c : clientes)
        {
            if (c.getUsuario().equals(usuario) && c.getContrasena().equals(contrasena))
            {
                state = true;
                CLIENTE_SESION = c;
            }
        }
         */
        return buscarClienteRecursivo(usuario,contrasena,0);
    }
    public boolean buscarClienteRecursivo(String usuario, String contrasena, int index) {
        if (index == clientes.size()) {
            return false;
        }
        if (clientes.get(index).getUsuario().equals(usuario) && clientes.get(index).getContrasena().equals(contrasena)) {
            CLIENTE_SESION = clientes.get(index);
            System.out.println("Cliente encontrado " + CLIENTE_SESION.getNombreCompleto());
            return true;
        }
        return buscarClienteRecursivo(usuario, contrasena, index + 1);
    }

    public boolean buscarAdmin (String usuario, String contrasena) {
        boolean state = false;
        if (usuario.equals("admin") && contrasena.equals("admin")) {
            state = true;
        }
        return state;
    }
    public void buscarDestino(String destino, Clientes clienteSesion) {
        buscarDestinoRecursivo(destino,clienteSesion,0);
        /*for(int j = 0 ; j<destinos.size();j++)
        {
            System.out.println("Buscando");
            if(destinos.get(j).getNombre().equals(destino))
            {
                destinos.get(j).setContBusquedas(destinos.get(j).getContBusquedas()+1);
                for(int i = 0 ; i<clientes.size();i++ )
                {
                    if(clienteSesion.getIdentificacion().equals(clientes.get(i).getIdentificacion()))
                    {
                        clientes.get(i).añadirBusqueda(destino);
                        clientes.set(i,clientes.get(i));
                        System.out.println("Cliente Sesion: " + clienteSesion + " busqueda " + destino);
                    }
                }
                System.out.println("Contador de busquedas del destino: " + destinos.get(j).getNombre() +" es: " + destinos.get(j).getContBusquedas());
            }
        }
         */
    }
    public void buscarDestinoRecursivo(String destino, Clientes clienteSesion, int destinoIndex) {
        if (destinoIndex == destinos.size()) {
            return;
        }
        if (destinos.get(destinoIndex).getNombre().equals(destino)) {
            destinos.get(destinoIndex).setContBusquedas(destinos.get(destinoIndex).getContBusquedas() + 1);
            actualizarBusquedaClienteRecursivo(clienteSesion, destino,0);
            System.out.println("Contador de busquedas del destino " + destino + " es: " + destinos.get(destinoIndex).getContBusquedas());
        }
        buscarDestinoRecursivo(destino, clienteSesion, destinoIndex + 1);
        borrarDatosSerializados(RUTA_DESTINOS);
        borrarDatosSerializados(RUTA_CLIENTES);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS,destinos);
        ArchivoUtils.serializarArraylistClientes(RUTA_CLIENTES,clientes);
    }
    public void actualizarBusquedaClienteRecursivo(Clientes clienteSesion, String destino, int clienteIndex) {
        if (clienteIndex == clientes.size()) {
            return;
        }
        if (clienteSesion.getIdentificacion().equals(clientes.get(clienteIndex).getIdentificacion())) {
            clientes.get(clienteIndex).añadirBusqueda(destino);
            System.out.println("Cliente Sesion: " + clienteSesion + " busqueda " + destino);
        }
        actualizarBusquedaClienteRecursivo(clienteSesion, destino, clienteIndex + 1);
    }
    public String obtenerNombresDestinos(ArrayList<Destinos> destinos) {
        StringBuilder nombres = new StringBuilder();
        for (Destinos destino : destinos) {
            nombres.append(destino.getNombre()).append(", ");
        }
        if (nombres.length() > 2) {
            nombres.setLength(nombres.length() - 2);
        }
        return nombres.toString();
    }
    public String obtenerIdiomas(ArrayList<String> idiomas) {
        StringBuilder resultado = new StringBuilder();
        for (String idioma : idiomas) {
            resultado.append(idioma).append(", ");
        }
        // Eliminar la coma y el espacio final
        if (resultado.length() > 0) {
            resultado.setLength(resultado.length() - 2);
        }
        return resultado.toString();
    }
    public String obtenerNombresCiudades(ArrayList<Destinos> destinos) {
        StringBuilder nombres = new StringBuilder();
        for (Destinos destino : destinos) {
            nombres.append(destino.getCiudad()).append(", ");
        }
        if (nombres.length() > 2) {
            nombres.setLength(nombres.length() - 2);
        }
        return nombres.toString();
    }
    public boolean verificarCredenciales(String usuario, String contrasena)
    {
        /*
        boolean state = false;
        for (Clientes c : clientes)
        {
            if (c.getUsuario().equals(usuario) && c.getContrasena().equals(contrasena))
            {
                state = true;
            }
        }
         */
        return verificarCredencialesRecursivo(usuario,contrasena,0);
    }
    public boolean verificarCredencialesRecursivo(String usuario, String contrasena, int index) {
        if (index == clientes.size()) {
            return false;
        }
        if (clientes.get(index).getUsuario().equals(usuario) && clientes.get(index).getContrasena().equals(contrasena)) {
            return true;
        }
        return verificarCredencialesRecursivo(usuario, contrasena, index + 1);
    }
    public boolean verificarIdentificacion(String id)
    {
        /*
        boolean state = false;
        for (Guias c : guias)
        {
            if (c.getIdentificacion().equals(id))
            {
                state = true;
            }
        }
        */

        return verificarIdentificacionRecursivo(id,0);
    }
    public boolean verificarIdentificacionRecursivo(String id, int index) {
        if (index == guias.size()) {
            return false;
        }
        if (guias.get(index).getIdentificacion().equals(id)) {
            return true;
        }
        return verificarIdentificacionRecursivo( id, index + 1);
    }
    public boolean esCorreoValido(String correo) {
        String expresionRegular = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(gmail\\.com|hotmail\\.com|uqvirtual\\.edu\\.co|yahoo\\.es)$";
        Pattern pattern = Pattern.compile(expresionRegular);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }
    public void enviarCorreo(Clientes cliente, Paquetes paqueteSeleccionado, LocalDate inicio, LocalDate fin, String personas, Guias selectedItem, String pendiente) {
        String correoEmisor = "traveluniquindio@gmail.com"; // Cambia esto con tu dirección de correo electrónico
        String contraseñaEmisor = "sgfc sgay apnx qvxq"; // Cambia esto con tu contra/seña de correo electrónico
        String destinos = obtenerNombresDestinos(paqueteSeleccionado.getDestinos());
        String asunto = "RESERVACION DE PAQUETE TURISTICO EN TRAVEL UNIQUINDIO";
        String mensaje = "Buen dia " + cliente.getNombreCompleto() +" la reservacion de su paquete, " + paqueteSeleccionado.getNombre() + " fue realizado con exito, le recordamos las caracteristicas con las que cuenta este paquete: " + paqueteSeleccionado.getServicios() +", durante " + inicio.until(fin, ChronoUnit.DAYS) +" dias, Reservado para: " + personas + " personas, con los siguientes destinos: " + destinos + " con un valor de: " + (long) paqueteSeleccionado.getPrecio()+"COP por persona, es decir un total de: " + (long) paqueteSeleccionado.getPrecio() * Integer.parseInt(personas) +"COP con fecha de inicio el: " + inicio + " y fecha de finalizacion : " + fin + ", pronto se notificara el estado de su reserva";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Para Gmail
        props.put("mail.smtp.port", "587"); // Puerto para TLS

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(correoEmisor, contraseñaEmisor);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoEmisor));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cliente.getCorreo()));
            message.setSubject(asunto);
            message.setText(mensaje);

            Transport.send(message);

            System.out.println("Correo enviado satisfactoriamente a la direccion: " + cliente.getCorreo());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> llenarArrayIdioma(String idiomas) {
        String[] leng = idiomas.split(",");
        ArrayList<String> jpgs = new ArrayList<>();
        // Verificar si la longitud del array resultante es 1
        if (leng.length == 1 && !leng[0].isEmpty()) {
            // Si solo hay un elemento y no está vacío, agregarlo al ArrayList
            jpgs.add(leng[0]);
        } else {
            // Si hay más de un elemento o está vacío, agregarlos al ArrayList
            jpgs.addAll(Arrays.asList(leng));
        }
        return jpgs;
    }
    private ArrayList<String> llenarArrayImagenes(String imagenes) {
        String[] leng = imagenes.split(",");
        ArrayList<String> jpgs = new ArrayList<>();
        // Verificar si la longitud del array resultante es 1
        if (leng.length == 1 && !leng[0].isEmpty()) {
            // Si solo hay un elemento y no está vacío, agregarlo al ArrayList
            jpgs.add(leng[0]);
        } else {
            // Si hay más de un elemento o está vacío, agregarlos al ArrayList
            jpgs.addAll(Arrays.asList(leng));
        }
        return jpgs;
    }
    public  void borrarDatosSerializados(String archivo) {
        Path path = Paths.get(archivo);

        try {
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadStage(String url, Event event, String mensaje){
            try
            {
                ((Node)(event.getSource())).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(Objects.requireNonNull(Agencia.class.getResource(url)));
                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle("Travel Uniquindio");
                newStage.show();
            } catch (Exception ignored)
            {
                LOGGER.log(Level.INFO,mensaje);
            }
    }
    private boolean verificarNombrePaquete(String nombre) {
        boolean state = true;
        for (int i = 0; i<paquetes.size();i++)
        {
            if(nombre.equals(paquetes.get(i).getNombre()))
            {
                state = false;
            }
        }
        return state;
    }
    private boolean verificarFechas(LocalDate inicio, LocalDate fin) {
        boolean state = true;
        if (inicio.isAfter(fin))
        {
            state = false;
        }
        return state;
    }
    private boolean verificarPersonasPaquete(Paquetes paquete, String personas) {
        boolean state = true;
        int numPersonas = Integer.parseInt(personas);
        if(paquete.getNumeroPersonas()>=numPersonas){
            state= true;
        }else{
            state = false;
        }
        return state;
    }
    private boolean verificarFechasReserva(LocalDate inicio, LocalDate fin, Paquetes paquete) {
        /*boolean state = true;
        if (inicio.isAfter(fin))
        {
            state = false;
        }else {
            for (int i = 0 ; i<paquetes.size();i++)
            {
                if (inicio.isAfter(paquete.getInicio()) || inicio.isEqual(paquete.getInicio())) {
                    if (fin.isBefore(paquete.getFin()) || fin.isEqual(paquete.getFin())) {
                        System.out.println("Las fechas de reserva están dentro del rango del paquete.");
                        state = true;
                    } else {
                        System.out.println("La fecha de fin de reserva está fuera del rango del paquete.");
                        state = false;
                    }
                } else {
                    System.out.println("La fecha de inicio de reserva está fuera del rango del paquete.");
                    state = false;
                }
            }
        }
         */
        return verificarFechasReservaRecursivo(inicio,fin,paquete,0);
    }
    public boolean verificarFechasReservaRecursivo(LocalDate inicio, LocalDate fin, Paquetes paquete, int paqueteIndex) {
        if (paqueteIndex == paquetes.size()) {
            return false;
        }
        if ((inicio.isAfter(paquete.getInicio()) || inicio.isEqual(paquete.getInicio())) &&
                (fin.isBefore(paquete.getFin()) || fin.isEqual(paquete.getFin()))) {
            System.out.println("Las fechas de reserva están dentro del rango del paquete.");
            return true;
        }
        return verificarFechasReservaRecursivo(inicio, fin, paquete, paqueteIndex + 1);
    }

    private boolean verificarNumero(String numero)
    {
        boolean isNumeric = (numero != null && numero.matches("[0-9]+"));
        return isNumeric;
    }
    public void cargarCalificacionesCompleta(ArrayList<Destinos> destinosCombo, ArrayList<Integer> calificacionDestinos, int calificacionGuia, Guias guia) {
        for(int i = 0 ; i<destinos.size();i++){
            for( int x = 0 ; x<destinosCombo.size();x++)
            {
                if(destinos.get(i).getNombre().equals(destinosCombo.get(x).getNombre()))
                {
                    destinos.get(i).añadirCalificacion(calificacionDestinos.get(x));
                    destinos.set(i,destinos.get(i));
                    System.out.println(destinos.get(i).getNombre() + " Calificacion: " + calificacionDestinos.get(x));
                }
            }
        }
        for(int j=0; j<guias.size();j++)
        {
            if (guias.get(j).getIdentificacion().equals(guia.getIdentificacion()))
            {
                guias.get(j).addCalificacion(calificacionGuia);
                guias.set(j,guias.get(j));
                System.out.println(guias.get(j).getNombre() + " Calificacion: " + calificacionGuia);
            }
        }
        LOGGER.log(Level.INFO,"Se ha generado la calificacion correctamente");
        for (Reservas reserva : reservas) {
            if (RESERVA_CALIFICACION.getCodigo() == reserva.getCodigo()) {
                reserva.setPaquete(RESERVA_CALIFICACION.getPaquete());
                reserva.setCliente(RESERVA_CALIFICACION.getCliente());
                reserva.setFechaSolicitud(RESERVA_CALIFICACION.getFechaSolicitud());
                reserva.setFechaPlanificada(RESERVA_CALIFICACION.getFechaPlanificada());
                reserva.setNumeroPersonas(RESERVA_CALIFICACION.getNumeroPersonas());
                reserva.setEstado(RESERVA_CALIFICACION.getEstado());
                reserva.setGuia(RESERVA_CALIFICACION.getGuia());
                reserva.setCodigo(RESERVA_CALIFICACION.getCodigo());
                reserva.setCalificacion(true);
            }
        }
        borrarDatosSerializados(RUTA_RESERVAS);
        ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
        borrarDatosSerializados(RUTA_GUIAS);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS,guias);
    }

    //CALIFICACIONES DE RESERVAS QUE NO CUENTAN CON UN GUIA
    public void cargarCalificaciones(ArrayList<Destinos> destinosCombo, ArrayList<Integer> calificacionDestinos) {
        for(int i = 0 ; i<destinos.size();i++){
            for( int x = 0 ; x<destinosCombo.size();x++)
            {
                if(destinos.get(i).getNombre().equals(destinosCombo.get(x).getNombre()))
                {
                    destinos.get(i).añadirCalificacion(calificacionDestinos.get(x));
                    destinos.set(i,destinos.get(i));
                    System.out.println(destinos.get(i).getNombre() + " Calificacion: " + calificacionDestinos.get(x));
                }
            }
        }
        for (Reservas reserva : reservas) {
            if (RESERVA_CALIFICACION.getCodigo() == reserva.getCodigo()) {
                reserva.setPaquete(RESERVA_CALIFICACION.getPaquete());
                reserva.setCliente(RESERVA_CALIFICACION.getCliente());
                reserva.setFechaSolicitud(RESERVA_CALIFICACION.getFechaSolicitud());
                reserva.setFechaPlanificada(RESERVA_CALIFICACION.getFechaPlanificada());
                reserva.setNumeroPersonas(RESERVA_CALIFICACION.getNumeroPersonas());
                reserva.setEstado(RESERVA_CALIFICACION.getEstado());
                reserva.setGuia(RESERVA_CALIFICACION.getGuia());
                reserva.setCodigo(RESERVA_CALIFICACION.getCodigo());
                reserva.setCalificacion(true);
            }
        }
        for(int i = 0 ; i<reservas.size();i++) {
            System.out.println(reservas.get(i).getCliente().getNombreCompleto() + " Codigo: " + reservas.get(i).getCodigo() + " Estado Calificacion: " + reservas.get(i).isCalificacion());
        }
        LOGGER.log(Level.INFO,"Se ha generado la calificacion correctamente");
        LOGGER.log(Level.INFO, "Se realizo la edicion de una Reserva");
        borrarDatosSerializados(RUTA_RESERVAS);
        ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
        borrarDatosSerializados(RUTA_GUIAS);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS,guias);
    }
    public ArrayList<Destinos> ordenarPorRepeticiones() {
        HashSet<String> destinosBuscados = new HashSet<>(agencia.CLIENTE_SESION.getBusquedas());
        ArrayList<String> arrayBuscados = new ArrayList<>(destinosBuscados);
        ArrayList<Destinos> destinosBusquedas = new ArrayList<>();
        for (int i = 0 ; i<arrayBuscados.size() ; i++)
        {
            for (int j = 0 ; j< agencia.getDestinos().size() ; j++)
            {
                if(arrayBuscados.get(i).equals(agencia.getDestinos().get(j).getNombre()))
                {
                    destinosBusquedas.add(agencia.getDestinos().get(i));
                }
            }
        }
        return destinosBusquedas;
    }

    public Destinos enviarDestinoEdicion() {
        return DESTINO_EDICION;
    }
}
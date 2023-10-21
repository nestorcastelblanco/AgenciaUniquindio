package co.edu.uniquindio.model;
import co.edu.uniquindio.utils.ArchivoUtils;
import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
@Getter
public class Agencia {
    private static final String RUTA_CLIENTES = "src/main/resources/textos/clientes.txt";
    private static final String RUTA_GUIAS = "src/main/resources/textos/guias.ser";
    private static final String RUTA_DESTINOS = "src/main/resources/textos/destinos.ser";
    private static final String RUTA_PAQUETES = "src/main/resources/textos/paquetes.ser";
    private static final String RUTA_RESERVAS = "src/main/resources/textos/reservas.ser";
    private static Paquetes PAQUETE_EDICION = new Paquetes();
    private static Clientes CLIENTE_SESION = new Clientes();
    private static ArrayList<Destinos> destinos = new ArrayList<>();
    private static ArrayList<Paquetes> paquetes = new ArrayList<>();
    private static ArrayList<Clientes> clientes = new ArrayList<>();
    private static ArrayList<Guias> guias = new ArrayList<>();
    private static final Logger LOGGER=Logger.getLogger(Agencia.class.getName());
    private static Agencia agencia;
    public static void inicializarDatos()
    {
        leerClientes();
        leerGuias();
        leerDestinos();
        leerPaquetes();
        System.out.println("Clientes: ");
        for(int i = 0 ; i<clientes.size();i++)
        {
            System.out.println(clientes.get(i).getNombreCompleto() + " Usuario: " + clientes.get(i).getUsuario() + " Clave: " + clientes.get(i).getContrasena());
        }
        System.out.println("Guias: ");
        for(int i = 0 ; i<guias.size();i++)
        {
            System.out.println(guias.get(i).getNombre());
        }
        System.out.println("Destinos: ");
        for(int i = 0 ; i<destinos.size();i++)
        {
            System.out.println(destinos.get(i).getNombre());
        }
        System.out.println("Paquetes: ");
        for(int i = 0 ; i<paquetes.size();i++)
        {
            System.out.println(paquetes.get(i).getNombre());
        }
    }
    public ArrayList<Destinos> enviarDestinos ()
    {
        return destinos;
    }
    public ArrayList<Paquetes> enviarPaquetes ()
    {
        paquetes.removeAll(paquetes);
        leerPaquetes();
        return paquetes;
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
    public Clientes obtenerCliente(String id)
    {
        Clientes cliente = null;
        for ( int i = 0 ; i<clientes.size();i++)
        {
            if(clientes.get(i).getContrasena().equals(id))
            {
                cliente =  clientes.get(i);
            }
        }
        return cliente;
    }
    public boolean verificarCredenciales(String usuario, String contrasena)
    {
        boolean state = false;
        for (Clientes c : clientes)
        {
            if (c.getUsuario().equals(usuario) && c.getContrasena().equals(contrasena))
            {
                state = true;
            }
        }
        return state;
    }
    public boolean verificarIdentificacion(String id)
    {
        boolean state = false;
        for (Guias c : guias)
        {
            if (c.getIdentificacion().equals(id))
            {
                state = true;
            }
        }
        return state;
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
    public boolean buscarCliente (String usuario, String contrasena)
    {
        boolean state = false;
        for (Clientes c : clientes)
        {
            if (c.getUsuario().equals(usuario) && c.getContrasena().equals(contrasena))
            {
                state = true;
                CLIENTE_SESION = c;
            }
        }
        System.out.println("Cliente encontrado " + CLIENTE_SESION.getNombreCompleto());
        return state;
    }
    public void registrarCliente(String nombre, String correo, String direccion,String ciudad, String telefono, String usuario, String contrasena, String id) throws CampoVacioException, CampoObligatorioException, CampoRepetido
    {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (correo == null || correo.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el correo");
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
                contrasena(contrasena)
                .build();

        clientes.add(cliente);
        escribirCliente(cliente);
        LOGGER.log(Level.INFO, "Se registro un nuevo cliente");
    }
    public void registrarGuia(String nombre, String exp, String id, String idiomas) throws CampoRepetido,CampoObligatorioException,CampoVacioException {
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
        Guias guia = Guias.builder().
                nombre(nombre).
                identificacion(id).
                exp(exp)
                .build();
        guia = llenarArrayIdioma(idiomas,guia);
        guias.add(guia);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS,guias);
        LOGGER.log(Level.INFO, "Se registro un nuevo guia");
    }

    private Guias llenarArrayIdioma(String idiomas, Guias guia) {
        String[] leng = idiomas.split(",");
        for (int i  = 0; i<leng.length ; i++)
        {
           guia.addLenguajes(leng[i]);
        }
        System.out.println(guia.getLenguajes().toString());
        return guia;
    }
    private Destinos llenarArrayImagenes(String imagenes, Destinos destinos) {
        String[] leng = imagenes.split(",");
        for (int i  = 0; i<leng.length ; i++)
        {
            destinos.addImagenes(leng[i]);
        }
        System.out.println(destinos.getImagenes().toString());
        return destinos;
    }

    public void ingresarCliente(String usuario, String contrasena) throws CampoRepetido
    {
        if (agencia.buscarCliente(usuario,contrasena) == false) {
            throw new CampoRepetido("Las credenciales proporcionadas son erroneas");
        }else{
            LOGGER.log(Level.INFO, "Se genero el ingreso de un cliente: " + CLIENTE_SESION.getNombreCompleto() );
        }
    }
    private void escribirCliente(Clientes cliente) {
        try {
            String linea = cliente.getNombreCompleto()+";"+cliente.getCorreo()+";"+cliente.getDireccion()+";"+cliente.getIdentificacion()+";"+cliente.getCiudad()+";"+cliente.getTelefono()+";"+cliente.getUsuario()+";"+cliente.getContrasena();
            ArchivoUtils.escribirArchivoBufferedWriter(RUTA_CLIENTES, List.of(linea), true);
        }catch (IOException e){
            LOGGER.severe(e.getMessage());
        }
    }
    private void escribirClientes() {
        borrarDatosSerializados(RUTA_CLIENTES);
        try {
            for (int i = 0 ; i<clientes.size();i++)
            {
                String linea = clientes.get(i).getNombreCompleto()+";"+clientes.get(i).getCorreo()+";"+clientes.get(i).getDireccion()+";"+clientes.get(i).getIdentificacion()+";"+clientes.get(i).getCiudad()+";"+clientes.get(i).getTelefono()+";"+clientes.get(i).getUsuario()+";"+clientes.get(i).getContrasena();
                ArchivoUtils.escribirArchivoBufferedWriter(RUTA_CLIENTES, List.of(linea), true);
            }
        }catch (IOException e){
            LOGGER.severe(e.getMessage());
        }
    }
    public static void borrarDatosSerializados(String archivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            // No se escribe nada en el archivo, simplemente se cierra
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
                newStage.setTitle("Borrador");
                newStage.show();
            } catch (Exception ignored)
            {
                LOGGER.log(Level.INFO,mensaje);
            }
    }
    public static void leerGuias()
    {
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
    public static void leerDestinos()
    {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_DESTINOS))) {
            ArrayList<Destinos> destinos1 = (ArrayList<Destinos>) entrada.readObject();
            System.out.println("Destinos deserializados correctamente.");
            destinos.addAll(destinos1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void leerPaquetes()
    {
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
    private static void leerClientes() {
        try{
            ArrayList<String> lineas = ArchivoUtils.leerArchivoScanner(RUTA_CLIENTES);
            for(String linea : lineas){
                String[] valores = linea.split(";");
                clientes.add( Clientes.builder()
                                .nombreCompleto(valores[0])
                                .correo(valores[1])
                                .direccion(valores[2])
                                .identificacion(valores[3])
                                .ciudad(valores[4])
                                .telefono(valores[5])
                                .usuario(valores[6])
                                .contrasena(valores[7])
                        .build());
            }
        }catch (IOException e){
            LOGGER.severe(e.getMessage());
        }
    }
    public Clientes clienteSesion() {
        return CLIENTE_SESION;
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
        for (int i = 0 ; i< clientes.size() ; i++)
        {
            if (CLIENTE_SESION.equals(clientes.get(i)))
            {
                Clientes cliente = Clientes.builder()
                        .nombreCompleto(nombre)
                        .correo(correo)
                        .direccion(direccion)
                        .ciudad(ciudad)
                        .identificacion(id)
                        .telefono(telefono)
                        .usuario(usuario)
                        .contrasena(contrasena).build();
                clientes.set(i,cliente);
                CLIENTE_SESION = cliente;
                escribirClientes();
            }
        }
    }
    public void registrarDestino(String nombre, String ciudad, String descripcion, String imagenes, String clima) throws CampoRepetido,CampoObligatorioException,CampoVacioException{
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
        Destinos destino = Destinos.builder().
                nombre(nombre).
                ciudad(ciudad).
                clima(clima).
                descripcion(descripcion)
                .build();
        destino = llenarArrayImagenes(imagenes,destino);
        destinos.add(destino);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS,destinos);
        LOGGER.log(Level.INFO, "Se registro un nuevo Destino");
    }
    public void registrarPaquete(String nombre, ArrayList<Destinos> destinos, LocalDate inicio, LocalDate fin, String servicios,String personas, String valor) throws CampoRepetido,CampoVacioException,CampoObligatorioException
    {
        if (nombre== null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (destinos == null || destinos.isEmpty()) {
            throw new CampoRepetido("No se añadieron destinos al paquete");
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
        if (valor == null || Float.valueOf(valor)<= 0 || valor.isEmpty() || !verificarNumero(valor)) {
            throw new CampoObligatorioException("Se crearon valores en el precio erroneos");
        }
        Paquetes paquete = Paquetes.builder().
                nombre(nombre)
                .destinos(destinos)
                .precio(Float.valueOf(valor))
                .inicio(inicio)
                .fin(fin)
                .servicios(servicios)
                .numeroPersonas(Integer.parseInt(personas))
        .build();
        paquete.setDuracion(inicio.until(fin, ChronoUnit.DAYS)+"");
        paquetes.add(paquete);
        for (int i = 0 ; i<paquetes.size();i++)
        {
            for (int j=0 ; j<paquetes.get(i).getDestinos().size();j++)
            {
                System.out.println("Nombre: " + paquetes.get(i).getNombre() + " Destinos: " + paquetes.get(i).getDestinos().get(j));
            }
        }
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
        LOGGER.log(Level.INFO, "Se registro un nuevo Paquete");
    }
    public void editarPaquete(String nombre, ArrayList<Destinos> destinos, LocalDate inicio, LocalDate fin, String servicios,String personas, String valor) throws CampoRepetido,CampoVacioException,CampoObligatorioException
    {
        if (nombre== null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (destinos == null || destinos.isEmpty()) {
            throw new CampoRepetido("No se añadieron destinos al paquete");
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
        if (valor == null || Float.valueOf(valor)<= 0 || valor.isEmpty() || !verificarNumero(valor)) {
            throw new CampoObligatorioException("Se crearon valores en el precio erroneos");
        }
        for (int i = 0 ; i< paquetes.size() ; i++)
        {
            if (PAQUETE_EDICION.equals(paquetes.get(i)))
            {
                Paquetes paquete = Paquetes.builder().
                        nombre(nombre)
                        .destinos(destinos)
                        .precio(Float.valueOf(valor))
                        .inicio(inicio)
                        .fin(fin)
                        .servicios(servicios)
                        .numeroPersonas(Integer.parseInt(personas))
                        .build();
                paquete.setDuracion(inicio.until(fin, ChronoUnit.DAYS)+"");
                paquetes.set(i,paquete);;
                LOGGER.log(Level.INFO, "Se realizo la edicion de un Paquete");
                PAQUETE_EDICION = paquete;
                borrarDatosSerializados(RUTA_PAQUETES);
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
            }
        }
    }
    private boolean verificarFechas(LocalDate inicio, LocalDate fin) {
        boolean state = true;
        if (inicio.isAfter(fin) || inicio.isEqual(LocalDate.now()))
        {
            state = false;
        }
        return state;
    }
    private boolean verificarNumero(String numero)
    {
        boolean isNumeric = (numero != null && numero.matches("[0-9]+"));
        return isNumeric;
    }
    public void recibirPaqueteEdicion(Paquetes selectedItem) {
        PAQUETE_EDICION= selectedItem;
    }
    public Paquetes enviarPaqueteEdicion() {
        return PAQUETE_EDICION;
    }
}

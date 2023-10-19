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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
@Getter
public class Agencia {
    private static final String RUTA_CLIENTES = "src/main/resources/textos/clientes.txt";
    private static final String RUTA_GUIAS = "src/main/resources/textos/guias.ser";
    private static Clientes CLIENTE_SESION = new Clientes();
    private static ArrayList<Clientes> clientes = new ArrayList<>();
    private static ArrayList<Guias> guias = new ArrayList<>();
    private static final Logger LOGGER=Logger.getLogger(Agencia.class.getName());
    private static Agencia agencia;
    public static void inicializarDatos()
    {
        leerClientes();
        leerGuias();
        for(int i = 0 ; i<clientes.size();i++)
        {
            System.out.println(clientes.get(i).getNombreCompleto() + " Usuario: " + clientes.get(i).getUsuario() + " Clave: " + clientes.get(i).getContrasena());
        }
        for(int i = 0 ; i<guias.size();i++)
        {
            System.out.println(guias.get(i).getNombre());
        }
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
            ArrayList<Guias> vehiculo = (ArrayList<Guias>) entrada.readObject();
            System.out.println("Guias deserializados correctamente.");
            for (Guias guia : vehiculo) {
                guias.add(guia);
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
}

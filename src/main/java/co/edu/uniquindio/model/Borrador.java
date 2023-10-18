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
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
@Getter
public class Borrador {
    private static final String RUTA_CLIENTES = "src/main/resources/textos/clientes.txt";
    private static final String RUTA_GUIAS = "src/main/resources/textos/guias.ser";
    private static Clientes CLIENTE_SESION;
    private static ArrayList<Clientes> clientes = new ArrayList<>();
    private static ArrayList<Guias> guias = new ArrayList<>();
    private static final Logger LOGGER=Logger.getLogger(Borrador.class.getName());
    private static Borrador agencia;
    public static void inicializarDatos()
    {
        leerClientes();
        leerGuias();
    }
    private Borrador()
    {
        try {
            LOGGER.addHandler(new FileHandler("logs.xml", true));
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    public static Borrador getInstance()
    {
        if(agencia== null)
        {
            agencia = new Borrador();
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
    public Clientes buscarCliente (String usuario, String contrasena)
    {
        Clientes cliente = new Clientes();
        for (Clientes c : clientes)
        {
            if (c.getUsuario().equals(usuario) && c.getContrasena().equals(contrasena))
            {
                cliente = c;
            }
        }
        return cliente;
    }
    public void registrarCliente(String nombre, String correo, String direccion,String id, String ciudad, String telefono, String usuario, String contrasena) throws CampoVacioException, CampoObligatorioException, CampoRepetido
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
    public void ingresarCliente(String usuario, String contrasena) throws CampoRepetido
    {
        if (agencia.buscarCliente(usuario,contrasena) == null) {
            throw new CampoRepetido("Las credenciales proporcionadas son erroneas");
        }
        CLIENTE_SESION = agencia.buscarCliente(usuario,contrasena);
        LOGGER.log(Level.INFO, "Se genero el ingreso de un cliente: " + CLIENTE_SESION.toString() );
    }
    private void escribirCliente(Clientes cliente) {
        try {
            String linea = cliente.getNombreCompleto()+";"+cliente.getCorreo()+";"+cliente.getDireccion()+";"+cliente.getIdentificacion()+";"+cliente.getCiudad()+";"+cliente.getTelefono()+";"+cliente.getUsuario()+";"+cliente.getContrasena();
            ArchivoUtils.escribirArchivoBufferedWriter(RUTA_CLIENTES, List.of(linea), true);
        }catch (IOException e){
            LOGGER.severe(e.getMessage());
        }
    }
    public void loadStage(String url, Event event, String mensaje){
            try
            {
                ((Node)(event.getSource())).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(Objects.requireNonNull(Borrador.class.getResource(url)));
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
            System.out.println("Vehículos deserializados correctamente.");
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
                        .identificacion(valores[0])
                        .nombreCompleto(valores[1])
                        .telefono(valores[2])
                        .correo(valores[3])
                        .ciudad(valores[4])
                        .direccion(valores[5])
                        .usuario(valores[6])
                        .contrasena(valores[7])
                        .build());
            }
        }catch (IOException e){
            LOGGER.severe(e.getMessage());
        }
    }
}

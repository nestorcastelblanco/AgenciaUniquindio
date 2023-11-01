package co.edu.uniquindio.model;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.utils.ArchivoUtils;
import com.sun.javafx.scene.shape.ArcHelper;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.java.Log;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.PasswordAuthentication;
import javax.print.attribute.standard.RequestingUserName;
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
    private final String RUTA_CLIENTES = "src/main/resources/textos/clientes.txt";
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
    private Clientes CLIENTE_SESION = new Clientes();
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
            for(int j=0 ; j<destinos.get(i).getImagenes().size();j++)
            {
                System.out.println(destinos.get(i).getImagenes().get(j));
                System.out.print("entro");
            }
            System.out.print("ya");
        }
        System.out.println("Paquetes: ");
        for(int i = 0 ; i<paquetes.size();i++)
        {
            System.out.println(paquetes.get(i).getNombre());
        }
        for(int i = 0 ; i<reservas.size();i++)
        {
            System.out.println(reservas.get(i).getCliente().getNombreCompleto());
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
    private  void leerReservas() {
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
    public ArrayList<Reservas> enviarReservas()
    {
        reservas.removeAll(reservas);
        leerReservas();
        return reservas;
    }
    public ArrayList<Reservas> enviarReservasCliente() {
        ArrayList<Reservas> reservasCliente = new ArrayList<>();
        reservas.removeAll(reservas);
        leerReservas();
        for (int i = 0 ; i<reservas.size();i++)
        {
            if (reservas.get(i).getCliente().getIdentificacion().equals(CLIENTE_SESION.getIdentificacion()))
            {
                reservasCliente.add(reservas.get(i));
            }
        }
        return reservasCliente;
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
    public boolean esCorreoValido(String correo) {
        String expresionRegular = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(gmail\\.com|hotmail\\.com|uqvirtual\\.edu\\.co|yahoo\\.es)$";
        Pattern pattern = Pattern.compile(expresionRegular);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }
    public void enviarCorreo(Clientes cliente, Paquetes paqueteSeleccionado, LocalDate inicio, LocalDate fin, String personas, Guias selectedItem, String pendiente) {
        final String correoEmisor = "traveluniquindio@gmail.com"; // Cambia esto con tu dirección de correo electrónico
        final String contraseñaEmisor = "sgfc sgay apnx qvxq"; // Cambia esto con tu contra/seña de correo electrónico
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
    public ArrayList<Destinos> obtenerDestinos(ArrayList<Destinos> destinos) {
       return destinos;
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
    public boolean buscarAdmin (String usuario, String contrasena)
    {
        boolean state = false;
        if (usuario.equals("admin") && contrasena.equals("admin"))
        {
            state = true;
        }
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
                contrasena(contrasena)
                .build();

        clientes.add(cliente);
        escribirCliente(cliente);
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
    public  void borrarDatosSerializados(String archivo) {
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
                newStage.setTitle("Travel Uniquindio");
                newStage.show();
            } catch (Exception ignored)
            {
                LOGGER.log(Level.INFO,mensaje);
            }
    }
    public  void leerGuias()
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
    public void leerDestinos()
    {
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
        if (imagenes == null || imagenes.isEmpty() || !agencia.isImageValid(imagenes)) {
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
                .contBusquedas(0)
                .contBusquedas(0)
                .build();
        ArrayList<String> imagenes1 = llenarArrayImagenes(imagenes);
        for(int i = 0; i<imagenes1.size();i++)
        {
            System.out.print(imagenes1.get(i));
        }
        destino.setImagenes(imagenes1);
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
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
        LOGGER.log(Level.INFO, "Se registro un nuevo Paquete");
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

    public void editarPaquete(String nombre, ArrayList<Destinos> destinos, LocalDate inicio, LocalDate fin, String servicios,String personas, String valor) throws CampoRepetido,CampoVacioException,CampoObligatorioException
    {
        if (nombre== null || nombre.isEmpty()) {
            throw new CampoObligatorioException("Es necesario ingresar el nombre");
        }
        if (destinos == null || destinos.isEmpty()) {
            throw new CampoRepetido("No se añadieron destinos al paquete");
        }
        if (!agencia.verificarFechas(inicio, fin)) {
            throw new CampoObligatorioException("Las fechas ingresadas son erroneas");
        }
        if (personas == null || personas.isEmpty() || Float.valueOf(personas) <0 || !verificarNumero(personas)) {
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
                paquetes.get(i).setNombre(nombre);
                paquetes.get(i).setDestinos(destinos);
                paquetes.get(i).setPrecio(Float.valueOf(valor));
                paquetes.get(i).setInicio(inicio);
                paquetes.get(i).setFin(fin);
                paquetes.get(i).setServicios(servicios);
                paquetes.get(i).setNumeroPersonas(PAQUETE_EDICION.getNumeroPersonas() + Integer.parseInt(personas));
                paquetes.get(i).setDuracion(inicio.until(fin, ChronoUnit.DAYS)+"");
                paquetes.set(i,paquetes.get(i));
                LOGGER.log(Level.INFO, "Se realizo la edicion de un Paquete");
                PAQUETE_EDICION = paquetes.get(i);
                borrarDatosSerializados(RUTA_PAQUETES);
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
            }
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
        for (int i = 0 ; i< reservas.size() ; i++)
        {
            if (RESERVA_EDICION.equals(reservas.get(i)))
            {
                reservas.get(i).setPaquete(paquete);
                reservas.get(i).setCliente(RESERVA_EDICION.getCliente());
                reservas.get(i).setFechaSolicitud(inicio);
                reservas.get(i).setFechaPlanificada(fin);
                reservas.get(i).setNumeroPersonas(RESERVA_EDICION.getNumeroPersonas() + numeroPersonas - numeroPersonas1);
                reservas.get(i).setEstado(pendiente);
                reservas.get(i).setGuia(selectedItem);
                reservas.set(i,reservas.get(i));
                LOGGER.log(Level.INFO, "Se realizo la edicion de una Reserva");
                borrarDatosSerializados(RUTA_RESERVAS);
                ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
            }
        }
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
    }
    private boolean verificarFechas(LocalDate inicio, LocalDate fin) {
        boolean state = true;
        if (inicio.isAfter(fin))
        {
            state = false;
        }
        return state;
    }
    private boolean verificarFechasReserva(LocalDate inicio, LocalDate fin, Paquetes paquete) {
        boolean state = true;
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
    public void recibirReservaEdicion(Reservas selectedItem) {
        RESERVA_EDICION= selectedItem;
        /*for (int i = 0 ; i<paquetes.size();i++)
        {
            if (paquetes.get(i).equals(selectedItem.getPaquete()))
            {
                PAQUETE_RESERVA = selectedItem.getPaquete();
            }
        }
         */
    }
    public void recibirReservaCancelacion(Reservas selectedItem) {
        RESERVA_CANCELACION= selectedItem;
        for (int i = 0 ; i<reservas.size();i++)
        {
            if (reservas.get(i).equals(RESERVA_CANCELACION))
            {
                for (int j = 0; j<paquetes.size();j++)
                {
                    if(paquetes.get(j).getNombre().equals(RESERVA_CANCELACION.getPaquete().getNombre())) {
                        paquetes.get(j).setNumeroPersonas(paquetes.get(j).getNumeroPersonas() + RESERVA_CANCELACION.getNumeroPersonas());
                        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
                    }
                }
                reservas.remove(i);
                ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
                leerReservas();
            }
        }
    }
    public boolean recibirReservaCalificacion(Reservas selectedItem) {
        boolean state =false;
        for (int i = 0 ; i<reservas.size();i++)
        {
            if (reservas.get(i).equals(RESERVA_CALIFICACION))
            {
                if(reservas.get(i).getFechaPlanificada().isBefore(LocalDate.now()))
                {
                    RESERVA_CALIFICACION= selectedItem;
                    state = true;
                }else{
                    state = false;
                }
            }
        }
        return state;
    }
    public Paquetes enviarPaqueteEdicion() {
        return PAQUETE_EDICION;
    }
    public Reservas enviarReservaEdicion() {
        return RESERVA_EDICION;
    }

    public void recibirPaqueteSeleccionado(Paquetes selectedItem) {
        PAQUETE_SELECCIONADO = selectedItem;
    }

    public Paquetes paqueteSeleccion() {
        return PAQUETE_SELECCIONADO;
    }
    public boolean isImageValid(String imageUrl) {
        boolean state = false;
        String[] leng = imageUrl.split(",");
        for (String ruta : leng) {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                // La imagen en la ruta especificada no existe
                System.out.println("La imagen en la ruta '" + ruta + "' no existe.");
                return false;
            }
        }
        // Todas las imágenes existen
        System.out.println("Todas las imágenes existen.");
        return true;
    }
    public ArrayList<Guias> retornarGuias() {
        return guias;
    }

    public ArrayList<Guias> enviarGuiasPaquete(Paquetes paquete) {
        ArrayList<Guias> guiasCorrespondientes = new ArrayList<>();
        for (int i = 0 ; i<guias.size();i++){
            if(guias.get(i).getPaquete().getNombre().equals(paquete.getNombre()))
            {
                guiasCorrespondientes.add(guias.get(i));
            }
        }
        return guiasCorrespondientes;
    }
    public ArrayList<Guias> enviarGuias() {
        return guias;
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
        reservas.add(reserva);
        ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS,reservas);
        for (int i = 0 ; i< paquetes.size() ; i++)
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

                //paquetes.set(i,paqueteN);;
                LOGGER.log(Level.INFO, "Se realizo la reserva de un Paquete");
                borrarDatosSerializados(RUTA_PAQUETES);
                ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES,paquetes);
            }
        }
    }
/*
    private boolean verificarGuiaDisponible(Guias guia,LocalDate inicio, LocalDate fin) {
        boolean state = false;
        if (guia == null || guia.getIdentificacion() == null || guia.getIdentificacion().isEmpty()) {
            return true;
        }else {
            for (int i = 0 ; i<reservas.size();i++)
            {
                if(reservas.get(i).getGuia().getIdentificacion().equals(guia.getIdentificacion()))
                {
                    if (inicio.isAfter(reservas.get(i).getFechaSolicitud()) || inicio.isEqual(reservas.get(i).getFechaSolicitud())) {
                        if (fin.isBefore(reservas.get(i).getFechaPlanificada()) || fin.isEqual(reservas.get(i).getFechaPlanificada())) {
                            System.out.println("El guia se encuentra disponible en las fechas de seleccionadas ");
                            LOGGER.log(Level.INFO, "El guia se encuentra disponible en las fechas de seleccionadas ");
                            state = true;
                        } else {
                            System.out.println("El guia no se encuentra disponible en las fechas de seleccionadas ");
                            LOGGER.log(Level.INFO, "El guia no se encuentra disponible en las fechas de seleccionadas ");
                            state = false;
                        }
                    } else {
                        System.out.println("El guia no se encuentra disponible en las fechas de seleccionadas ");
                        LOGGER.log(Level.INFO, "El guia no se encuentra disponible en las fechas de seleccionadas ");
                        state = false;
                    }
                }
            }
        }
        return state;
    }
*/
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
    public void buscarDestino(String destino) {
        for(int j = 0 ; j<destinos.size();j++)
        {
            System.out.println("Buscando");
            if(destinos.get(j).getNombre().equals(destino))
            {
                destinos.get(j).setContBusquedas(destinos.get(j).getContBusquedas()+1);
                System.out.println("Contador de busquedas del destino: " + destinos.get(j).getNombre() +" es: " + destinos.get(j).getContBusquedas());
            }
        }
    }

    public void datos() {
        agencia.borrarDatosSerializados(RUTA_DESTINOS);
        agencia.borrarDatosSerializados(RUTA_PAQUETES);
        agencia.borrarDatosSerializados(RUTA_RESERVAS);
        agencia.borrarDatosSerializados(RUTA_GUIAS);
        agencia.borrarDatosSerializados(RUTA_CLIENTES);
        ArchivoUtils.serializarArraylistDestinos(RUTA_DESTINOS,destinos);
        ArchivoUtils.serializarArraylistPaquetes(RUTA_PAQUETES, paquetes);
        ArchivoUtils.serializarArraylistReservas(RUTA_RESERVAS, reservas);
        ArchivoUtils.serializarArraylist(RUTA_GUIAS, guias);
        agencia.escribirClientes();
        leerPaquetes();
        leerDestinos();
        leerReservas();
        leerGuias();
        leerClientes();
    }
}

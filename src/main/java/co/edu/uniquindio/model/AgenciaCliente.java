package co.edu.uniquindio.model;
import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

@Log
public class AgenciaCliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 1234;
    private static AgenciaCliente agenciaCliente;

    public static AgenciaCliente getInstance()
    {
        if(agenciaCliente== null)
        {
            agenciaCliente = new AgenciaCliente();
        }
        return agenciaCliente;
    }
    public String registrarCliente(String nombre, String correo, String direccion,String ciudad, String telefono, String usuario, String contrasena, String id) throws CampoVacioException, CampoObligatorioException, CampoRepetido
    {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

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
            out.writeObject( Mensaje.builder()
                    .tipo("agregarCliente")
                    .contenido(cliente).build() );
            String respuesta = (String) in.readObject();
            in.close();
            out.close();
            System.out.println("RESPUESTA DE REGISTRO: " + respuesta);
            return respuesta;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public ArrayList<Clientes> listarClientes() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("listarClientes").build());
            Object respuesta = in.readObject();
            ArrayList<Clientes> list = (ArrayList<Clientes>) respuesta;
            in.close();
            out.close();
            return list;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void loadStage(String url, Event event, String mensaje){
        try
        {
            ((Node)(event.getSource())).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(Objects.requireNonNull(AgenciaCliente.class.getResource(url)));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Travel Uniquindio");
            newStage.show();
        } catch (Exception ignored)
        {
            log.log(Level.INFO,mensaje);
        }
    }

    public ArrayList<Destinos> reservaCalificacion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("recibirReservaCalificacion").build());
            Object respuesta = in.readObject();
            ArrayList<Destinos> list = (ArrayList<Destinos>) respuesta;
            in.close();
            out.close();
            return list;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public Paquetes reservaCalificacionPaquete() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("paqueteCalificacion").build());
            Object respuesta = in.readObject();
            Paquetes list = (Paquetes) respuesta;
            in.close();
            out.close();
            return list;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public Guias guiaCalificacion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("recibirGuiaCalificacion").build());
            Object respuesta = in.readObject();
            Guias list = (Guias) respuesta;
            in.close();
            out.close();
            return list;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void cargarCalificaciones(ArrayList<Destinos> arrayListDestinos, ArrayList<Integer> calificacionDestinos, Paquetes paquetes) {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Calificaciones calificaciones = Calificaciones.builder()
                    .destinos(arrayListDestinos)
                    .calificacionDestinos(calificacionDestinos)
                    .paquete(paquetes)
                    .build();
            out.writeObject( Mensaje.builder()
                    .tipo("cargarCalificacion")
                    .contenido(calificaciones).build() );
            Object respuesta = in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void cargarCalificacionesCompleta(ArrayList<Destinos> arrayListDestinos, ArrayList<Integer> calificacionDestinos, int calificacionGuia, Guias guias, Paquetes paquetes) {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            CalificacionesCompletas calificaciones = CalificacionesCompletas.builder()
                    .destinos(arrayListDestinos)
                    .calificacionDestinos(calificacionDestinos)
                    .paquete(paquetes)
                    .calificacionGuia(calificacionGuia)
                    .guia(guias)
                    .build();
            out.writeObject( Mensaje.builder()
                    .tipo("cargarCalificacionCompleta")
                    .contenido(calificaciones).build() );
            Object respuesta = in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Guias> enviarGuias() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("recibirGuias").build());
            ArrayList<Guias> respuesta = (ArrayList<Guias>) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public String ingresarCliente(String usuario, String contraseña, String codigo)
    {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ClienteIngreso clienteIngreso = new ClienteIngreso();
            clienteIngreso.setUsuario(usuario);
            clienteIngreso.setContrasena(contraseña);
            clienteIngreso.setCodigo(codigo);
            out.writeObject( Mensaje.builder()
                    .tipo("ingresoCliente")
                    .contenido(clienteIngreso).build());
            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        }catch (Exception e){
            return e.toString();
        }
    }
    public String enviarCodigo(String usuario, String contraseña)
    {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ClienteIngreso clienteIngreso = new ClienteIngreso();
            clienteIngreso.setUsuario(usuario);
            clienteIngreso.setContrasena(contraseña);
            out.writeObject( Mensaje.builder()
                    .tipo("enviarCodigo")
                    .contenido(clienteIngreso).build());
            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void inicializarDatos() {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ClienteIngreso clienteIngreso = new ClienteIngreso();
            out.writeObject( Mensaje.builder()
                    .tipo("inicializarDatos")
                    .contenido(clienteIngreso).build());
            Object respuesta = in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String ingresarAdmin (String usuario, String contrasena) throws IOException
    {
        try (Socket socket = new Socket(HOST, PUERTO)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ClienteIngreso clienteIngreso = new ClienteIngreso();
            clienteIngreso.setUsuario(usuario);
            clienteIngreso.setContrasena(contrasena);
            out.writeObject( Mensaje.builder()
                    .tipo("verificarAdmin")
                    .contenido(clienteIngreso).build());
            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public ArrayList<Paquetes> enviarPaquetes() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("paquetes").build());
            Object respuesta = in.readObject();
            ArrayList<Paquetes> list = (ArrayList<Paquetes>) respuesta;
            in.close();
            out.close();
            return list;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarGuia(String text, String text1, String text2, String text3, Paquetes selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            GuiaRegistro guiaRegistro = GuiaRegistro.builder()
                    .nombre(text).exp(text1)
                    .identificacion(text2)
                    .lenguajes(text3)
                    .paquete(selectedItem)
                    .build();
            out.writeObject(Mensaje.builder()
                    .tipo("registrarGuia")
                    .contenido(guiaRegistro).build());
            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String registrarDestino(String text, String text1, String text2, ArrayList<String> imagePaths, String text3) throws CampoObligatorioException{
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Destinos destino = Destinos.builder()
                    .nombre(text)
                    .ciudad(text1)
                    .descripcion(text2)
                    .imagenes(imagePaths)
                    .clima(text3).build();
            out.writeObject(Mensaje.builder()
                    .tipo("registrarDestino")
                    .contenido(destino).build());
            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public ArrayList<Destinos> enviarDestinos() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(Mensaje.builder()
                    .tipo("destinos").build());
            ArrayList<Destinos> respuesta = (ArrayList<Destinos>) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarPaqueteCupon(String text, ArrayList<Destinos> destinosSeleccionados, LocalDate value, LocalDate value1, String text1, String text2,
                                      String text3, String text4, String text5, LocalDate value2, LocalDate value3) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            PaquetesCupon paquetes = PaquetesCupon.builder()
                    .nombre(text)
                    .destinos(destinosSeleccionados)
                    .inicio(value)
                    .fin(value1)
                    .servicios(text1)
                    .numeroPersonas(text2)
                    .precio(text3)
                    .cupon(text4)
                    .valorCupon(text5)
                    .inicioCupon(value2).finCupon(value3).build();

            out.writeObject(Mensaje.builder()
                    .tipo("registrarPaqueteCupon")
                    .contenido(paquetes).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String registrarPaquete(String text, ArrayList<Destinos> destinosSeleccionados, LocalDate value, LocalDate value1, String text1, String text2, String text3) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            PaquetesCupon paquetes = PaquetesCupon.builder()
                    .nombre(text)
                    .destinos(destinosSeleccionados)
                    .inicio(value)
                    .fin(value1)
                    .servicios(text1)
                    .numeroPersonas(text2)
                    .precio(text3).build();

            out.writeObject(Mensaje.builder()
                    .tipo("registrarPaquete")
                    .contenido(paquetes).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void recibirDestinoCancelacion(Destinos selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("destinoCancelacion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirDestinoEdicion(Destinos selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("destinoEdicion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String obtenerNombresCiudades(ArrayList<Destinos> destinos) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerNombreCiudades")
                    .contenido(destinos).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirPaqueteEdicion(Paquetes selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("paqueteEdicion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirPaqueteCancelacion(Paquetes selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("paqueteCancelacion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Reservas> enviarReservas() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("enviarReservas").build());

            ArrayList<Reservas> respuesta = (ArrayList<Reservas>) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirReservaEdicion(Reservas selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("reservaEdicion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirReservaCancelacion(Reservas selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("reservaCancelacion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String obtenerNombresDestinos(ArrayList<Destinos> destinos) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerNombresDestino")
                    .contenido(destinos).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void datos() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("datos").build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirPaqueteSeleccionado(Paquetes selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("paqueteSeleccionado")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Clientes getCLIENTE_SESION() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerClienteSesion").build());

            Object cliente = in.readObject();
            System.out.println(cliente);
            in.close();
            out.close();
            return (Clientes) cliente;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void buscarDestino(String destino, Clientes clienteSesion) {
        try (Socket socket = new Socket(HOST, PUERTO)) {

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ClienteBuscarDestino clienteBuscarDestino = ClienteBuscarDestino.builder()
                    .cliente(clienteSesion)
                    .destino(destino).build();

            out.writeObject(Mensaje.builder()
                    .tipo("buscarDestino")
                    .contenido(clienteBuscarDestino).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Paquetes paqueteSeleccion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerPaqueteSeleccion").build());

            Paquetes respuesta = (Paquetes) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Clientes clienteSesion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerClienteSesiones").build());

            Clientes respuesta = (Clientes) in.readObject();
            System.out.println(respuesta.getNombreCompleto());
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Guias> enviarGuiasPaquete(Paquetes paquete) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerGuiasPaquetes")
                    .contenido(paquete).build());

            ArrayList<Guias> respuesta = (ArrayList<Guias>) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String realizarReserva(Paquetes paquete, Clientes cliente, LocalDate value, LocalDate value1, String text, Guias selectedItem, String pendiente, double valorDescuento) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ReservaCreada reservaCreada = ReservaCreada.builder()
                    .paquete(paquete)
                    .cliente(cliente)
                    .fechaSolicitud(value)
                    .fechaPlanificada(value1)
                    .numeroPersonas(text)
                    .guia(selectedItem)
                    .estado(pendiente)
                    .descuento(valorDescuento).build();

            out.writeObject(Mensaje.builder()
                    .tipo("realizarReserva")
                    .contenido(reservaCreada).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public float verificarCupon(Paquetes paquete, LocalDate value, LocalDate value1, String text, String text1) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Cupon cupon = Cupon.builder()
                            .paquete(paquete)
                            .inicio(value)
                            .fin(value1)
                            .cupon(text)
                            .personas(text1).build();

            out.writeObject(Mensaje.builder()
                    .tipo("verificarCupon")
                    .contenido(cupon).build());

            float respuesta = (float) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> ordenarPorRepeticiones() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("ordenarPorRepeticiones").build());

            ArrayList<String> respuesta = (ArrayList<String>) in.readObject();
            System.out.println("Destinos recomendados: " + respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void recibirGuiaEliminado(Guias selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("guiaEliminacion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void enviarGuiaEdicion(Guias selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("guiaEdicion")
                    .contenido(selectedItem).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String obtenerIdiomas(ArrayList<String> lenguajes) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Guias guias = Guias.builder()
                    .lenguajes(lenguajes).build();

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerIdiomas")
                    .contenido(guias).build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public float promedioPaquetes(Paquetes paqueteSeleccionado) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("obtenerPromedioPaquetes")
                    .contenido(paqueteSeleccionado).build());

            Float respuesta = (Float) in.readObject();
            System.out.println("Promedio del paquete: " + respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Reservas> enviarReservasCliente() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("enviarReservasClientes")
                    .build());

            ArrayList<Reservas> respuesta = (ArrayList<Reservas>) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Destinos enviarDestinoEdicion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("enviarDestinoEdicion")
                    .build());

            Destinos respuesta = (Destinos) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String editarDestino(String text, String text1, String text2, ArrayList<String> imagePaths, String text3) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Destinos destinos = Destinos.builder()
                    .nombre(text)
                    .ciudad(text1)
                    .descripcion(text2)
                    .imagenes(imagePaths)
                    .clima(text3).build();

            out.writeObject(Mensaje.builder()
                    .tipo("editarDestino")
                    .contenido(destinos)
                    .build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Guias recibirGuiaEdicion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("recibirGuiaEdicion")
                    .build());

            Guias respuesta = (Guias) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String editarGuia(String text, String text1, String text2, String text3, Paquetes selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            GuiaRegistro guias = GuiaRegistro.builder()
                    .nombre(text)
                    .exp(text1)
                    .identificacion(text2)
                    .lenguajes(text3)
                    .paquete(selectedItem).build();

            out.writeObject(Mensaje.builder()
                    .tipo("editarGuia")
                    .contenido(guias)
                    .build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public ArrayList<Destinos> getDestinos() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("enviarDestinos")
                    .build());

            ArrayList<Destinos> respuesta = (ArrayList<Destinos>) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Paquetes enviarPaqueteEdicion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("enviarPaqueteEdicion")
                    .build());

            Paquetes respuesta = (Paquetes) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Paquetes getPAQUETE_EDICION() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("recibirPaqueteEdicion")
                    .build());

            Paquetes respuesta = (Paquetes) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String editarPaquetes(Paquetes paqueteEdicion, String text, ArrayList<Destinos> destinosActuales, LocalDate value, LocalDate value1, String text1, String text2, String text3) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            PaqueteEdicion paquete = PaqueteEdicion.builder()
                    .paquete(paqueteEdicion)
                    .nombre(text)
                    .destinos(destinosActuales)
                    .inicio(value)
                    .fin(value1)
                    .servicios(text1)
                    .cupos(text2)
                    .valor(text3)
                    .build();

            out.writeObject(Mensaje.builder()
                    .tipo("editarPaquete")
                    .contenido(paquete)
                    .build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String realizarEdicion(String text, String text1, String text2, String text3, String text4, String text5, String text6, String text7) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Clientes clientes = Clientes.builder()
                    .nombreCompleto(text)
                    .correo(text1)
                    .direccion(text2)
                    .identificacion(text3)
                    .ciudad(text4)
                    .telefono(text5)
                    .usuario(text6)
                    .contrasena(text7)
                    .build();

            out.writeObject(Mensaje.builder()
                    .tipo("editarClientes")
                    .contenido(clientes)
                    .build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Reservas enviarReservaEdicion() {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("enviarReservaEdicion")
                    .build());

            Reservas respuesta = (Reservas) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String editarReserva(Paquetes selectedItem, LocalDate value, LocalDate value1, String text, String text1, Guias selectedItem1, String selectedItem2) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ReservaEdicion reservaEdicion = ReservaEdicion.builder()
                    .paquetes(selectedItem)
                    .fechaInicio(value)
                    .fechaFin(value1)
                    .agregarPersonas(text)
                    .quitarPersonas(text1)
                    .guias(selectedItem1)
                    .estado(selectedItem2)
                    .build();

            out.writeObject(Mensaje.builder()
                    .tipo("editarReserva")
                    .contenido(reservaEdicion)
                    .build());

            String respuesta = (String) in.readObject();
            System.out.println(respuesta);
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public float promedioGuias(Guias guias) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("promedioGias")
                    .contenido(guias).build());

            float respuesta = (float) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean recibirReservaCalificacion(Reservas selectedItem) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("recibirReservaCalificar")
                    .contenido(selectedItem).build());

            boolean respuesta = (boolean) in.readObject();
            in.close();
            out.close();
            return respuesta;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

package co.edu.uniquindio.model;

import java.io.*;
import java.net.*;

public class ManejadorCliente implements Runnable {
    private Socket clientSocket;
    public ManejadorCliente(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try (ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream salida = new ObjectOutputStream(clientSocket.getOutputStream())) {
            // Lógica para leer/escribir datos del cliente y gestionar la persistencia.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package co.edu.uniquindio.model;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Servidor {
    private static final int PUERTO = 12345;
    private static final int NUM_HILOS = 10;
    private static ExecutorService pool = Executors.newFixedThreadPool(NUM_HILOS);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ManejadorCliente(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // localhost
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to chat server");

            new ReadThread(socket).start();
            new WriteThread(socket).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ReadThread extends Thread {
        private BufferedReader in;

        public ReadThread(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }
    }

    static class WriteThread extends Thread {
        private PrintWriter out;
        private Scanner sc;

        public WriteThread(Socket socket) {
            sc = new Scanner(System.in);
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            out.println(name + " joined the chat");

            while (true) {
                String message = sc.nextLine();
                out.println(name + ": " + message);
            }
        }
    }
}

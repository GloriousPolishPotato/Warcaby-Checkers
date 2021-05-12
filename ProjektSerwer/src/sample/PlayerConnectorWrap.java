package sample;

import java.net.ServerSocket;

public class PlayerConnectorWrap extends Thread {

    public ServerSocket serverSocket;
    public int Port = 29090;

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(Port);

            System.out.println("Serwer otwarty.");

            try {
                while (true) {
                    // ServerSocket.accept() - Listens for a connection to be made to this socket and accepts it.
                    new PlayerConnector(serverSocket.accept()).start();
                }
            }
            finally {
                serverSocket.close();
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }

    }

}

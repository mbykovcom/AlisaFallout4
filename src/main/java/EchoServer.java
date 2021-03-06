import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by smirnov-n on 06.05.2016.
 */
public class EchoServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        args = Arrays.asList("4322").toArray(new String[0]);

        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            //System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        ExecutorService executorService = Executors.newCachedThreadPool();
        Answer answer = new Answer("src/main/resources/associations.xml");

        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equals("close")) {
                            out.println("closed");
                            break;
                        }
                        out.println(answer.getWeightedAnswer(inputLine));
                    }
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                    System.out.println(e.getMessage());
                }
            });
        }

        executorService.awaitTermination(5, TimeUnit.MINUTES);
        serverSocket.close();
    }
}

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server 
{
    public static void main(String[] args)
    {
        //processing command line arg and setting up
        int port = Integer.parseInt(args[0]);
        Map<String,String> table = new HashMap<>();
        String response = new String();

        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
           
            while (true)
            {   
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String request = in.readLine();
                String[] requestElems = request.split(" ");
                
                if (requestElems[0].equals("REGISTER"))
                {
                    String dnsName = requestElems[1];
                    String ipAddress = requestElems[2];
                    String existingEntry = table.get(dnsName);
                    if (existingEntry == null)
                    {
                        table.put(dnsName, ipAddress);
                        response = String.valueOf(table.size());
                    }
                    else response = "-1";
                }
                else if (requestElems[0].equals("LOOKUP"))
                {
                    String dnsName = requestElems[1];
                    String existingEntry = table.get(dnsName);
                    if (existingEntry == null) response = "NOT_FOUND";
                    else response = dnsName + " " + existingEntry;
                }

                out.println(response);

                //print log
                String log = new String("Server:");
                for (String elem : requestElems)
                {
                    log += " " + elem;
                }
                System.out.println(log);
             }

        }
        catch (SocketException e)
        {
            // e.printStackTrace();
            System.out.println("Failed to open socket!");
        }
        catch (SocketTimeoutException e)
        {
            System.out.println("Waited for too long!");
        }
        catch (IOException e)
        {
            System.out.println("Failed to read/write to/from socket!");
        }

    }    
}

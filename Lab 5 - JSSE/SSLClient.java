import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient
{
    public static void main(String[] args) throws IOException
    {
        // System.setProperty("javax.net.ssl.keyStore", "server.keys");
        // System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.keyStore", "client.keys");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.trustStore", "server.keys");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        // System.setProperty("javax.net.ssl.trustStore", "truststore");
        // System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        String response = new String();
        String message = new String();
        try
        {
            //process command line args
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String operation = args[2];
            List<String> operands = new ArrayList<>();

            InetAddress address = InetAddress.getByName(host);
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(address, port);
        
            if (operation.equals("REGISTER"))
            {
                operands.add(args[3]);
                operands.add(args[4]);

                if (args.length > 5)
                {
                    String[] cipherSuites = new String[args.length - 5];
                    for (int i = 5; i < args.length; i++) {
                        cipherSuites[i - 5] = args[i];
                    }
                    socket.setEnabledCipherSuites(cipherSuites);
                }
            }
            else
            {
                operands.add(args[3]);
                if (args.length > 4)
                {
                    String[] cipherSuites = new String[args.length - 4];
                    for (int i = 4; i < args.length; i++) {
                        cipherSuites[i - 4] = args[i];
                    }
                    socket.setEnabledCipherSuites(cipherSuites);
                }
            } 

            //build message
            message = operation;
            for (String operand : operands)
            {
                message += " " + operand;
            }

            
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //send message
            out.println(message);

            //process response
            response = in.readLine();
            //close socket
            socket.shutdownOutput();
            socket.close();
        }
        catch (SocketException e)
        {
             e.printStackTrace();
            System.out.println("Couldn't setup socket!");
            response = "ERROR";
        }
        catch (UnknownHostException e)
        {
            // e.printStackTrace();
            System.out.println("Couldn't get host!");
            response = "ERROR";
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
            System.out.println("Couldn't read/write to/from socket!");
            response = "ERROR";
        }
        finally
        {
            //print log
            System.out.println("SSLClient: " + message + " : " + response);
        }

    }    
}
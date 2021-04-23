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

public class Client
{
    public static void main(String[] args) throws IOException
    {
        String response = new String();
        String message = new String();
        try
        {
            //process command line args
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String operation = args[2];
            List<String> operands = new ArrayList<>();
            if (operation.equals("REGISTER"))
            {
                operands.add(args[3]);
                operands.add(args[4]);
            }
            else operands.add(args[3]);

            //build message
            message = operation;
            for (String operand : operands)
            {
                message += " " + operand;
            }

            //setup connection and packet
            InetAddress address = InetAddress.getByName(host);
            Socket socket = new Socket(address, port);
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
            // e.printStackTrace();
            System.out.println("Couldn't read/write to/from socket!");
            response = "ERROR";
        }
        finally
        {
            //print log
            System.out.println("Client: " + message + " : " + response);
        }

    }    
}
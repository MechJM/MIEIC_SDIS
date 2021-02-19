import java.io.IOException;
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
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);
            byte[] buf = new byte[256];
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

            //send packet
            socket.send(packet);

            //receive packet
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            //process response
            response = new String(packet.getData(), 0 , packet.getLength());
            //close socket
            socket.close();
        }
        catch (SocketException e)
        {
            // e.printStackTrace();
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
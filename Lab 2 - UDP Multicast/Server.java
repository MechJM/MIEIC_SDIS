import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Server 
{
    public static void main(String[] args)
    {
        //processing command line arg and setting up
        int port = Integer.parseInt(args[0]);

        int mcastPort = Integer.parseInt(args[2]);
        Map<String,String> table = new HashMap<>();
        String response = new String();

        try
        {
            //setting up mcast
            InetAddress mcastAddress = InetAddress.getByName(args[1]);
            DatagramSocket mcastSocket = new DatagramSocket(mcastPort);
            AddressAdvert advertTask = new AddressAdvert(mcastSocket, mcastAddress, port);
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(advertTask, 0, 1000);

            //setting up service
            DatagramSocket socket = new DatagramSocket(port);
            socket.setSoTimeout(0);

            while (true)
            {   
                //receive packet
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                //process message
                buf = packet.getData();
                String request = new String(packet.getData(), 0, packet.getLength());
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

                //send reply
                buf = response.getBytes();
                InetAddress address = packet.getAddress();
                packet = new DatagramPacket(buf, buf.length, address, packet.getPort());
                socket.send(packet);

                //print log
                String log = new String();
                for (String elem : requestElems)
                {
                    log += " " + elem;
                }
                log += " :: " + response;
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

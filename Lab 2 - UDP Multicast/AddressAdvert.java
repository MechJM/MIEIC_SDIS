import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimerTask;

public class AddressAdvert extends TimerTask
{
    private DatagramSocket socket;
    private byte[] buf;
    private InetAddress mcastAddress;
    private int servicePort;
    private int mcastPort;


    AddressAdvert(DatagramSocket socket, InetAddress mcastAddress, int servicePort, int mcastPort)
    {
        this.socket = socket;
        this.servicePort = servicePort;
        String stringAddress = "";
        try
        {
           stringAddress = InetAddress.getLocalHost().toString(); 
        }
        catch (UnknownHostException e)
        {
            System.out.println("Couldn't find host!");
        }
        
        buf = (stringAddress.split("/")[1] + " " + servicePort).getBytes();
        this.mcastAddress = mcastAddress;
        this.mcastPort = mcastPort;
    }

    @Override
    public void run() 
    {
        DatagramPacket packet = new DatagramPacket(buf, buf.length, mcastAddress, this.mcastPort);
        try
        {
            socket.send(packet);
            System.out.println("multicast: " + mcastAddress.toString().split("/")[1] + " " + mcastPort + ": " + InetAddress.getLocalHost().toString().split("/")[1] + " " + servicePort);
        }
        catch (IOException e)
        {
            System.out.println("Couldn't send packet through socket!");;
        }
    }
}

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server implements RMIInterface {
    private Map<String, String> table;

    public Server() {table = new HashMap<>();}
    public static void main(String[] args) {

        try
        {
            String remoteName = args[0];

            Server server = new Server();
            RMIInterface manager = (RMIInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(remoteName, manager);
        }
        catch (RemoteException e)
        {
            System.out.println("Couldn't interact with remote!");
            System.out.println(e.toString());
            System.exit(1);
        }
        
    }

    @Override
    public String lookup(String hostname) throws RemoteException {
        String response;
        String existingEntry = table.get(hostname);
        if (existingEntry == null)
            response = "NOT_FOUND";
        else
            response = hostname + " " + existingEntry;

        System.out.println("LOOKUP " + hostname + " :: " + response);

        return response;
    }

    @Override
    public String register(String address, String hostname) throws RemoteException {
        String response;
        String existingEntry = table.get(hostname);
        if (existingEntry == null) {
            table.put(hostname, address);
            response = String.valueOf(table.size());
        } else
            response = "-1";

        System.out.println("REGISTER " + hostname + " " + address + " :: " + response);

        return response;
    }
}

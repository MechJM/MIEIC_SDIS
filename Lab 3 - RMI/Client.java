import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Client
{
    public static void main(String[] args)
    {
        String hostname = args[0];
        String remoteName = args[1];

        String operation = args[2];
        List<String> operands = new ArrayList<>();
        operands.add(args[3]);
        if (operation.equals("REGISTER")) operands.add(args[4]);

        try
        {
            Registry registry = LocateRegistry.getRegistry(hostname);
            RMIInterface manager = (RMIInterface) registry.lookup(remoteName);
            
            String response = "";
            if (operation.equals("REGISTER")) response = manager.register(operands.get(1), operands.get(0));
            else response = manager.lookup(operands.get(0));

            String log = operation;
            for (String operand : operands)
            {
                log += " " + operand;
            }
            log += " :: " + response;
            System.out.println(log);
        }
        catch (RemoteException e)
        {
            System.out.println("Couldn't communicate with remote!");
            return;
        }
        catch (NotBoundException e)
        {
            System.out.println("Remote object name isn't bound to anything!");
            return;
        }
    }    
}
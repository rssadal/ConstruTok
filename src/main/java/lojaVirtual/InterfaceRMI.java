package lojaVirtual;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
    
    public String repoePratileira(String json) throws RemoteException;
}

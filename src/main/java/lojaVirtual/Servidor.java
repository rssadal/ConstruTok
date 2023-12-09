package lojaVirtual;

//Imports de WebService
import javax.xml.ws.Endpoint;

//Imports de RMI
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import javax.jws.WebService;

@WebService(endpointInterface = "lojaVirtual.WebServiceVendas")
public class Servidor implements InterfaceRMI, WebServiceVendas {

    private final ArrayList<Produto> produtos = new ArrayList<Produto>();
    private final ReentrantLock tranca = new ReentrantLock();

    
    //Iniciando a Pratileira com 5 Produtos de Cada
    public Servidor() {
        for (int i = 0; i < 5; i++) {
            this.produtos.add(new Produto("Brita (Saco)", 85.50f, "Fundação"));
            this.produtos.add(new Produto("Cimento (Saco)", 25.99f, "Concretagem"));
            this.produtos.add(new Produto("Areia (Metro Cubico)", 166.91f, "Fundação"));
            this.produtos.add(new Produto("Ceramica (Metro Quadrado)", 31.00f, "Revestimento"));
        }
    }

    // Método de compra do WebService
    @Override
    public String compra(String nome, double dinheiro, String usuario) {
    	
        tranca.lock();
        try {
            if (usuario.equals("espera")) {
                Thread.sleep(10000);
            }
            for (Produto produto : produtos) {
                
                if (produto.getNome().equals(nome)) {
                    if (produto.getPreco() <= dinheiro) {
                        produtos.remove(produto);
                        return produto.ProdutoToJson();
                    } else {
                        return "{'result':'insuficiente'}";
                    }
                }
            }
            return "{'result':'semProduto'}";
        } catch (InterruptedException ex) {
            return "{'result':'erroEspera'}";
        } finally {
            tranca.unlock();
        }
    }

    //MÉTODO IMPLEMENTADO DE RMI
    @Override
    public String repoePratileira(String json) throws RemoteException {
        tranca.lock();
        try {
        	//Adiciona mais 5 produtos a pratileira
        	for(int i = 0; i < 5; i++) {
        		this.produtos.add(new Produto(json));
        	}
            return "Produtos Adicionados a Pratileira";
        } catch (Exception ex) {
            return "Falha ao Repor";
        } finally {
            tranca.unlock();
        }
    }

    public static void main(String[] args) {
        try {
            // Iniciando Servidor
            Servidor servidor = new Servidor();

            String address = "http://localhost:8080/lojaVirtual";
            Endpoint.publish(address, servidor);

            // RMI
            InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(servidor, 0);

            // Ligar o stub com RMI
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("InterfaceRMI", stub);

            System.out.println("Servidor online");
        } catch (Exception e) {
            System.out.println("Erro ao iniciar servidor: " + e.getMessage());
        }
    }

}

package lojaVirtual;

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
            this.produtos.add(new Produto("Caibro Eucalipto (6cmx6cmx3m)", 41.90f, "Suporte"));
            this.produtos.add(new Produto("Aluminio Liso (1mm X 1000mm)", 2368.54f, "Revestimento"));
            this.produtos.add(new Produto("Forro Fibra M.(13mm C/ 12pçs)", 569.25f, "Revestimento"));
        }
    }

    // Método de compra do WebService
    @Override
    public String compra(String nome, double dinheiro, String cliente) {
    		  	
    	try {
    		//Timeout antes de pegar a tranca para o recurso compartilhado
    		Thread.sleep(5000);
    		
    		//Tranca para o cliente executar uma compra
            tranca.lock();
            for (Produto produto : produtos) {
                
                if (produto.getNome().equals(nome)) {
                    if (produto.getPreco() <= dinheiro) {
                        produtos.remove(produto);
                        return produto.ProdutoToJson();
                    } else {
                        return "{'result':'Insuficiente'}";
                    }
                }
            }
            return "{'result':'Sem Estoque'}";
        } catch (InterruptedException ex) {
            return "{'result':'ErroTimerOut'}";
        } finally {
            tranca.unlock();
        }
    }

    //Método do RMI
    @Override
    public String repoePratileira(String mensagem) throws RemoteException {
    	//Tranca para fazer a reposição
        tranca.lock();
        try {
        	//Adiciona mais 5 produtos a pratileira
        	for(int i = 0; i < 5; i++) {
        		this.produtos.add(new Produto(mensagem));
        	}
            return "Produtos Adicionados a Pratileira";
        } catch (Exception ex) {
            return "Falha ao Repor";
        } finally {
        	//Destranca depois de fazer tudo
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

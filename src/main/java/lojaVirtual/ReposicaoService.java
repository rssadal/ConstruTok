package lojaVirtual;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ReposicaoService {

    private ReposicaoService() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            InterfaceRMI stub = (InterfaceRMI) registry.lookup("InterfaceRMI");
            JFrame display = new JFrame("Reposição");
         
            ArrayList<Produto> produtos = new ArrayList<Produto>();
            
            produtos.add(new Produto("Brita (Saco)", 85.50f, "Fundação"));
            produtos.add(new Produto("Cimento (Saco)", 25.99f, "Concretagem"));
            produtos.add(new Produto("Areia (Metro Cubico)", 166.91f, "Fundação"));
            produtos.add(new Produto("Ceramica (Metro Quadrado)", 31.00f, "Revestimento"));
            produtos.add(new Produto("Caibro Eucalipto (6cmx6cmx3m)", 41.90f, "Suporte"));
            produtos.add(new Produto("Aluminio Liso (1mm X 1000mm)", 2368.54f, "Revestimento"));
            produtos.add(new Produto("Forro Fibra M.(13mm C/ 12pçs)", 569.25f, "Revestimento"));
            
            int posicao = 50;

            JLabel header2 = new JLabel("Adicionar o produto:");
            header2.setBounds(10, 10, 200, 30);
            display.add(header2);

            for (int i = 0; i < produtos.size(); i++) {
                String json = "{'nome':'" + produtos.get(i).getNome()
                		+ "','preco':'" + Double.toString(produtos.get(i).getPreco())
                		+ "','setor':'" + produtos.get(i).getSetor()  +"'}";
                
                BotaoProduto botao = new BotaoProduto(produtos.get(i).getNome(), json);
                botao.setBounds(10, posicao, 290, 30);
                
                posicao += 50;

                botao.addActionListener((ActionEvent e) -> {
                    BotaoProduto clickedButton = (BotaoProduto) e.getSource();
                    
                    try {
                        String envio = stub.repoePratileira(clickedButton.getKey());
                        JOptionPane.showMessageDialog(null,envio);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, exception.getMessage());
                    }

                });
                display.add(botao);
            }

            display.setSize(330, 450);
            display.setLayout(null);

            display.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            display.setVisible(true);

        } catch (NotBoundException | RemoteException e) {
            System.out.println("Client exception: " + e.toString());
        }
    }
}

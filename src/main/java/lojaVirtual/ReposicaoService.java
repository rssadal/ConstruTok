package lojaVirtual;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JButton;
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

            String[] options = {"Brita (Saco)", "Cimento (Saco)", "Areia (Metro Cubico)", "Ceramica (Metro Quadrado)"};
            double[] precos = {85.5f, 29.99f, 166.91f, 31.00f};
            String[] setores = {"Fundação", "Concretagem", "Fundação", "Revestimento"};
            int posicao = 50;

            JLabel header2 = new JLabel("Adicionar o produto:");
            header2.setBounds(10, 10, 200, 30);
            display.add(header2);

            for (int i = 0; i < options.length; i++) {
                String json = "{'nome':'" + options[i]
                		+ "','preco':'" + Double.toString(precos[i])
                		+ "','setor':'" + setores[i]+"'}";
                
                MeuBotao botao = new MeuBotao(options[i], json);
                botao.setBounds(10, posicao, 290, 30);
                
                posicao += 50;

                botao.addActionListener((ActionEvent e) -> {
                    MeuBotao clickedButton = (MeuBotao) e.getSource();
                    
                    try {
                        String envio = stub.repoePratileira(clickedButton.getKey());
                        JOptionPane.showMessageDialog(null,envio);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, exception.getMessage());
                    }

                });
                display.add(botao);
            }

            display.setSize(330, 300);
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

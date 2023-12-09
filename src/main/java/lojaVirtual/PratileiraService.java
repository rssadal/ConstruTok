package lojaVirtual;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import javax.swing.JOptionPane;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PratileiraService {

    public static void main(String[] args) {

        try {
        	
            //Inicia e conecta ao Web Service Local
            URL url = new URL("http://localhost:8080/lojaVirtual?wsdl");
            QName qnameService = new QName("http://lojaVirtual/", "ServidorService");
            QName qnamePort = new QName("http://lojaVirtual/", "ServidorPort");
            Service service = Service.create(url, qnameService);
            WebServiceVendas pratileira = service.getPort(qnamePort, WebServiceVendas.class);
            String usuario = JOptionPane.showInputDialog(null, "Qual o seu nome?");
            JFrame janela = new JFrame("Cliente: " + usuario);
            
            // Preços e Nomes
            String[] options = {"Brita (Saco)", "Cimento (Saco)", "Areia (Metro Cubico)", "Ceramica (Metro Quadrado)"};
            String[] precos = {"R$85,50", "R$29,99", "R$166,91", "R$31,00"};
            
            //Posição Inicial
            int pos = 40;
            
            //Hearders
            JLabel header1 = new JLabel("Preço");
            JLabel header2 = new JLabel("Produto");
            
            header1.setBounds(10, 10, 50, 30);
            header2.setBounds(180, 10, 50, 30);
            
            janela.add(header1);
            janela.add(header2);

            for (int i = 0; i < options.length; i++) {
                JButton botoes = new JButton(options[i]);
                JLabel labels = new JLabel(precos[i]);
                labels.setBounds(10, pos, 80, 30);
                botoes.setBounds(100, pos, 200, 30);
                pos += 40;

                botoes.addActionListener((ActionEvent e) -> {
                    JButton clickedButton = (JButton) e.getSource();
                    String buttonLabel = clickedButton.getText();
                    double valor = Double.parseDouble(JOptionPane.showInputDialog("Pagamento: R$"));
                    try {
                    	
                        String json = pratileira.compra(buttonLabel, valor, usuario);
                        Produto meuProduto = new Produto(json);
                        
                       JOptionPane.showMessageDialog(null, "Compra Realizada!!!");
                                               
                        if (meuProduto.getPreco() < valor) {
                            JOptionPane.showMessageDialog(null, "Troco: R$" + Double.toString(valor - meuProduto.getPreco()));
                        }
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, exception.getMessage());
                    }

                });
                janela.add(botoes);
                janela.add(labels);
            }

            janela.setSize(330, 300);
            janela.setLayout(null);

            janela.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            janela.setVisible(true);

        } catch (HeadlessException | MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}

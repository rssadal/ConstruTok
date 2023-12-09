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
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PratileiraService {

    public static void main(String[] args) {

        try {
        	
            //Inicia e conecta ao Web Service Local
            URL url = new URL("http://localhost:8080/lojaVirtual?wsdl");
            QName serviceVendas = new QName("http://lojaVirtual/", "ServidorService");
            QName portaServidorVendas = new QName("http://lojaVirtual/", "ServidorPort");
            Service service = Service.create(url, serviceVendas);
            WebServiceVendas pratileira = service.getPort(portaServidorVendas, WebServiceVendas.class);
            
            String cliente = JOptionPane.showInputDialog(null, "Qual o seu nome?");
            
            if(cliente != null && !cliente.trim().isEmpty()) {
            	JFrame display = new JFrame("Cliente: " + cliente);
                
                // Preços e Nomes
                ArrayList<Produto> produtos = new ArrayList<Produto>();
                
                produtos.add(new Produto("Brita (Saco)", 85.50f, "Fundação"));
                produtos.add(new Produto("Cimento (Saco)", 25.99f, "Concretagem"));
                produtos.add(new Produto("Areia (Metro Cubico)", 166.91f, "Fundação"));
                produtos.add(new Produto("Ceramica (Metro Quadrado)", 31.00f, "Revestimento"));
                produtos.add(new Produto("Caibro Eucalipto (6cmx6cmx3m)", 41.90f, "Suporte"));
                produtos.add(new Produto("Aluminio Liso (1mm X 1000mm)", 2368.54f, "Revestimento"));
                produtos.add(new Produto("Forro Fibra M.(13mm C/ 12pçs)", 569.25f, "Revestimento"));
                
                //Posição Inicial
                int pos = 40;
                
                //Hearders
                JLabel header1 = new JLabel("Preço");
                JLabel header2 = new JLabel("Produto");
                
                header1.setBounds(10, 10, 50, 30);
                header2.setBounds(180, 10, 50, 30);
                
                display.add(header1);
                display.add(header2);

                for (int i = 0; i < produtos.size(); i++) {
                	//Definição dos Botões
                    JButton botoes = new JButton(produtos.get(i).getNome());
                    DecimalFormat formato = new DecimalFormat("0.00");
                    String precoFormatado = formato.format(produtos.get(i).getPreco());
                    JLabel labels = new JLabel("R$: " + precoFormatado);
                    
                    labels.setBounds(10, pos, 80, 30);
                    botoes.setBounds(100, pos, 200, 30);
                    pos += 40;

                    botoes.addActionListener((ActionEvent e) -> {
                        JButton clickedButton = (JButton) e.getSource();
                        String buttonLabel = clickedButton.getText();
                        double valor = Double.parseDouble(JOptionPane.showInputDialog("Pagamento: R$"));
                        try {
                        	
                            String json = pratileira.compra(buttonLabel, valor, cliente);
                            Produto meuProduto = new Produto(json);
                            
                           JOptionPane.showMessageDialog(null, "Compra Realizada!!!");
                                                   
                            if (meuProduto.getPreco() < valor) {
                                JOptionPane.showMessageDialog(null, "Troco: R$" + Double.toString(valor - meuProduto.getPreco()));
                            }
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, exception.getMessage());
                        }

                    });
                    display.add(botoes);
                    display.add(labels);
                }

                display.setSize(330, 400);
                display.setLayout(null);

                display.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

                display.setVisible(true);
            }
            else {
            	JOptionPane.showMessageDialog(null, "Tenta novamente com um nome Valido");
            }
            
        } catch (HeadlessException | MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}

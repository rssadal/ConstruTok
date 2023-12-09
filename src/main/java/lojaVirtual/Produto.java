package lojaVirtual;

import javax.swing.JOptionPane;

public class Produto {
    
    private String nome;
    private String setor;
    private double preco;

    
    public Produto(){}
    
    public Produto(String json)throws Exception{
    	
        json = json.substring(1, json.length()-1);
        String[] parametros = json.split(",");
        for(String valores : parametros){
            String[] temp = valores.split(":");
            String type = temp[0].substring(1, temp[0].length()-1);
            String parametro = temp[1].substring(1, temp[1].length()-1);
            switch(type){
                case "result":
                    System.out.println(parametro);
                    if(parametro.equals("insuficiente")){
                        throw new Exception("Dinheiro Insuficiente");
                    }else{
                        throw new Exception("Produto Insuficiente");
                    }
                case "nome":
                    this.nome = parametro;
                    break;
                case "preco":
                    this.preco = Double.parseDouble(parametro);
                    break;
                case "setor":
                    this.setor = parametro;
                    break;
            }
        }
    }
    
    public Produto(String nome, double preco, String setor){
        this.nome = nome;
        this.preco = preco;
        this.setor = setor;
    }
    
    public String ProdutoToJson(){
    	
        String json = "{'nome':'"+ this.nome 
        		+ "','preco':'" + Double.toString(preco) 
        		+ "','setor':'" + this.setor+ "'}";
        
        return json;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public double getPreco() {
        return this.preco;
    }
    
    public String getSetor(){
        return this.setor;
    }
    
}

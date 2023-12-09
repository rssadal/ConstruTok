
package lojaVirtual;

import javax.swing.JButton;

public class BotaoProduto extends JButton {
    
    private String key;
    
    public BotaoProduto(String texto, String key){
        super(texto);
        this.key = key;
    }
    
    public String getKey(){
        return this.key;
    }
    
}

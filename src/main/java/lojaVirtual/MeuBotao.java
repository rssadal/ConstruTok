
package lojaVirtual;

import javax.swing.JButton;

public class MeuBotao extends JButton {
    
    private String key;
    
    public MeuBotao(String texto, String key){
        super(texto);
        this.key = key;
    }
    
    public String getKey(){
        return this.key;
    }
    
}

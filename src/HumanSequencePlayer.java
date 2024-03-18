package proyecto;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class HumanSequencePlayer extends ASequencePlayer {
	
	private HashMap<Integer, JButton> handMap;
        public String username;
	
	public HumanSequencePlayer(int number, String username) {
		
		super(number);
		this.username = username;
		handMap = new HashMap<Integer, JButton>();
		
	}
	
	HashMap<Integer, JButton> getHandMap() {
		return handMap;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
        
        
	/*
        habilita todos los botones de la mano del jugador. 
        Itera sobre las entradas del HashMap y habilita cada botón asociado a una carta.
        */
	void enableAllHandCards() {
		for (HashMap.Entry<Integer, JButton> mapElement : handMap.entrySet()) {
                    mapElement.getValue().setEnabled(true);
		}
                JOptionPane.showMessageDialog(null, "ENABLED");
	}

}

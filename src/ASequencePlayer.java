package proyecto;

import java.util.LinkedList;

public class ASequencePlayer {
	
	protected int playerNumber;
	char playerColor;
	String playerName;
	LinkedList<ASequenceCard> hand = new LinkedList<ASequenceCard>();

	public ASequencePlayer(int number) {
		
		playerNumber = number;
		
	}

    public String getPlayerName() {
        return playerName;
    }

    public char getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(char playerColor) {
        this.playerColor = playerColor;
    }
        
	/*
        Un método que imprime en la consola los nombres de las 
        cartas en la mano del jugador, seguido por un salto de línea.
        */
	void printHand() {
		for(ASequenceCard i : hand)
			System.out.print(i.getCardName() + " ");
		System.out.println("\n");
	}
	/*
        devuelve una cadena que representa los nombres de 
        las cartas en la mano del jugador, separados por comas.
        */
	String getHand() {
		String s = "";
		for(ASequenceCard i : hand)
			s+=i.getCardName() + ", ";
		return s;
	}
	
}

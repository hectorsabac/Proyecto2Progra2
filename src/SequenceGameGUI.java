package proyecto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;

public class SequenceGameGUI extends JFrame {
	
	JPanel boardPanel, handPanel, deckPanel, playerPanel;
	//matrix de los botones de las tarjetas en el orden del tablero
	SButton[][] cardButtons = new SButton[10][10];
	//matrix de botones de ficha 
	SButton[][] tokenButtons = new SButton[10][10];
	//Botón para mazo de cartas
	JButton deckButton;
	//controlado por SequenceGame, que se establece cuando se juega una jack en la mano 
	boolean twoEyedJackIsPlayed;
	int jackNumber;
	//controlado por SequenceGame, que se establece cuando se recicla una carta
	boolean isRecycled;
	ASequenceCard recycledCard;
	//controlado por SequenceGame, notificar qué carta quitar de la mano
	int removedHandCardIndex;
	
	ImageIcon redToken = new ImageIcon(getClass().getResource("res/tokens/redToken.png"));
	ImageIcon blueToken = new ImageIcon(getClass().getResource("res/tokens/blueToken.png"));
	ImageIcon greenToken = new ImageIcon(getClass().getResource("res/tokens/greenToken.png"));
			
		/* 
		   **YA NO SE USA**, nombre original del botón en orden
		{
			{new JButton("TLCorner"), new JButton("c1"), new JButton("ck"), new JButton("cq"), new JButton("c10"), new JButton("c9"), new JButton("c8"), new JButton("c7"), new JButton("c6"), new JButton("TRCorner")},
			{new JButton("d1"), new JButton("s7"), new JButton("s8"), new JButton("s9"), new JButton("s10"), new JButton("sq"), new JButton("sk"), new JButton("s1"), new JButton("c5"), new JButton("s2")},
			{new JButton("dk"), new JButton("s6"), new JButton("C10"), new JButton("C9"), new JButton("C8"), new JButton("C7"), new JButton("C6"), new JButton("d2"), new JButton("c4"), new JButton("s3")}, 
			{new JButton("dq"), new JButton("s5"), new JButton("Cq"), new JButton("h8"), new JButton("h7"), new JButton("h6"), new JButton("C5"), new JButton("d3"), new JButton("c3"), new JButton("s4")},
			{new JButton("d10"), new JButton("S4"), new JButton("Ck"), new JButton("h9"), new JButton("h2"), new JButton("h5"), new JButton("C4"), new JButton("d4"), new JButton("c2"), new JButton("S5")},
			{new JButton("d9"), new JButton("S3"), new JButton("C1"), new JButton("h10"), new JButton("h3"), new JButton("h4"), new JButton("C3"), new JButton("d5"), new JButton("h1"), new JButton("S6")},
			{new JButton("d8"), new JButton("S2"), new JButton("D1"), new JButton("hq"), new JButton("hk"), new JButton("H1"), new JButton("C2"), new JButton("d6"), new JButton("Hk"), new JButton("S7")},
			{new JButton("d7"), new JButton("H2"), new JButton("Dk"), new JButton("Dq"), new JButton("D10"), new JButton("D9"), new JButton("D8"), new JButton("D7"), new JButton("Hq"), new JButton("S8")},
			{new JButton("D6"), new JButton("H3"), new JButton("H4"), new JButton("H5"), new JButton("H6"), new JButton("H7"), new JButton("H8"), new JButton("H9"), new JButton("H10"), new JButton("S9")},
			{new JButton("BLCorner"), new JButton("D5"), new JButton("D4"), new JButton("D3"), new JButton("D2"), new JButton("S1"), new JButton("Sk"), new JButton("Sq"), new JButton("S10"), new JButton("BRCorner")}
			};
			*/
	
	ImageIcon[][] normalCardImages = {
			{new ImageIcon(getClass().getResource("res/normalCards/corner.png")), new ImageIcon(getClass().getResource("res/normalCards/AC.png")), new ImageIcon(getClass().getResource("res/normalCards/KC.png")), new ImageIcon(getClass().getResource("res/normalCards/QC.png")), new ImageIcon(getClass().getResource("res/normalCards/10C.png")), new ImageIcon(getClass().getResource("res/normalCards/9C.png")), new ImageIcon(getClass().getResource("res/normalCards/8C.png")), new ImageIcon(getClass().getResource("res/normalCards/7C.png")), new ImageIcon(getClass().getResource("res/normalCards/6C.png")), new ImageIcon(getClass().getResource("res/normalCards/corner.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/AD.png")), new ImageIcon(getClass().getResource("res/normalCards/7S.png")), new ImageIcon(getClass().getResource("res/normalCards/8S.png")), new ImageIcon(getClass().getResource("res/normalCards/9S.png")), new ImageIcon(getClass().getResource("res/normalCards/10S.png")), new ImageIcon(getClass().getResource("res/normalCards/QS.png")), new ImageIcon(getClass().getResource("res/normalCards/KS.png")), new ImageIcon(getClass().getResource("res/normalCards/AS.png")), new ImageIcon(getClass().getResource("res/normalCards/5C.png")), new ImageIcon(getClass().getResource("res/normalCards/2S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/KD.png")), new ImageIcon(getClass().getResource("res/normalCards/6S.png")), new ImageIcon(getClass().getResource("res/normalCards/10C.png")), new ImageIcon(getClass().getResource("res/normalCards/9C.png")), new ImageIcon(getClass().getResource("res/normalCards/8C.png")), new ImageIcon(getClass().getResource("res/normalCards/7C.png")), new ImageIcon(getClass().getResource("res/normalCards/6C.png")), new ImageIcon(getClass().getResource("res/normalCards/2D.png")), new ImageIcon(getClass().getResource("res/normalCards/4C.png")), new ImageIcon(getClass().getResource("res/normalCards/3S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/QD.png")), new ImageIcon(getClass().getResource("res/normalCards/5S.png")), new ImageIcon(getClass().getResource("res/normalCards/QC.png")), new ImageIcon(getClass().getResource("res/normalCards/8H.png")), new ImageIcon(getClass().getResource("res/normalCards/7H.png")), new ImageIcon(getClass().getResource("res/normalCards/6H.png")), new ImageIcon(getClass().getResource("res/normalCards/5C.png")), new ImageIcon(getClass().getResource("res/normalCards/3D.png")), new ImageIcon(getClass().getResource("res/normalCards/3C.png")), new ImageIcon(getClass().getResource("res/normalCards/4S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/10D.png")), new ImageIcon(getClass().getResource("res/normalCards/4S.png")), new ImageIcon(getClass().getResource("res/normalCards/KC.png")), new ImageIcon(getClass().getResource("res/normalCards/9H.png")), new ImageIcon(getClass().getResource("res/normalCards/2H.png")), new ImageIcon(getClass().getResource("res/normalCards/5H.png")), new ImageIcon(getClass().getResource("res/normalCards/4C.png")), new ImageIcon(getClass().getResource("res/normalCards/4D.png")), new ImageIcon(getClass().getResource("res/normalCards/2C.png")), new ImageIcon(getClass().getResource("res/normalCards/5S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/9D.png")), new ImageIcon(getClass().getResource("res/normalCards/3S.png")), new ImageIcon(getClass().getResource("res/normalCards/AC.png")), new ImageIcon(getClass().getResource("res/normalCards/10H.png")), new ImageIcon(getClass().getResource("res/normalCards/3H.png")), new ImageIcon(getClass().getResource("res/normalCards/4H.png")), new ImageIcon(getClass().getResource("res/normalCards/3C.png")), new ImageIcon(getClass().getResource("res/normalCards/5D.png")), new ImageIcon(getClass().getResource("res/normalCards/AH.png")), new ImageIcon(getClass().getResource("res/normalCards/6S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/8D.png")), new ImageIcon(getClass().getResource("res/normalCards/2S.png")), new ImageIcon(getClass().getResource("res/normalCards/AD.png")), new ImageIcon(getClass().getResource("res/normalCards/QH.png")), new ImageIcon(getClass().getResource("res/normalCards/KH.png")), new ImageIcon(getClass().getResource("res/normalCards/AH.png")), new ImageIcon(getClass().getResource("res/normalCards/2C.png")), new ImageIcon(getClass().getResource("res/normalCards/6D.png")), new ImageIcon(getClass().getResource("res/normalCards/KH.png")), new ImageIcon(getClass().getResource("res/normalCards/7S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/7D.png")), new ImageIcon(getClass().getResource("res/normalCards/2H.png")), new ImageIcon(getClass().getResource("res/normalCards/KD.png")), new ImageIcon(getClass().getResource("res/normalCards/QD.png")), new ImageIcon(getClass().getResource("res/normalCards/10D.png")), new ImageIcon(getClass().getResource("res/normalCards/9D.png")), new ImageIcon(getClass().getResource("res/normalCards/8D.png")), new ImageIcon(getClass().getResource("res/normalCards/7D.png")), new ImageIcon(getClass().getResource("res/normalCards/QH.png")), new ImageIcon(getClass().getResource("res/normalCards/8S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/6D.png")), new ImageIcon(getClass().getResource("res/normalCards/3H.png")), new ImageIcon(getClass().getResource("res/normalCards/4H.png")), new ImageIcon(getClass().getResource("res/normalCards/5H.png")), new ImageIcon(getClass().getResource("res/normalCards/6H.png")), new ImageIcon(getClass().getResource("res/normalCards/7H.png")), new ImageIcon(getClass().getResource("res/normalCards/8H.png")), new ImageIcon(getClass().getResource("res/normalCards/9H.png")), new ImageIcon(getClass().getResource("res/normalCards/10H.png")), new ImageIcon(getClass().getResource("res/normalCards/9S.png"))},
			{new ImageIcon(getClass().getResource("res/normalCards/corner.png")), new ImageIcon(getClass().getResource("res/normalCards/5D.png")), new ImageIcon(getClass().getResource("res/normalCards/4D.png")), new ImageIcon(getClass().getResource("res/normalCards/3D.png")), new ImageIcon(getClass().getResource("res/normalCards/2D.png")), new ImageIcon(getClass().getResource("res/normalCards/AS.png")), new ImageIcon(getClass().getResource("res/normalCards/KS.png")), new ImageIcon(getClass().getResource("res/normalCards/QS.png")), new ImageIcon(getClass().getResource("res/normalCards/10S.png")), new ImageIcon(getClass().getResource("res/normalCards/corner.png"))}

			};
	
	ImageIcon[][] greyCardImages = {
			{new ImageIcon(getClass().getResource("res/greyCards/corner.png")), new ImageIcon(getClass().getResource("res/greyCards/AC.png")), new ImageIcon(getClass().getResource("res/greyCards/KC.png")), new ImageIcon(getClass().getResource("res/greyCards/QC.png")), new ImageIcon(getClass().getResource("res/greyCards/10C.png")), new ImageIcon(getClass().getResource("res/greyCards/9C.png")), new ImageIcon(getClass().getResource("res/greyCards/8C.png")), new ImageIcon(getClass().getResource("res/greyCards/7C.png")), new ImageIcon(getClass().getResource("res/greyCards/6C.png")), new ImageIcon(getClass().getResource("res/greyCards/corner.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/AD.png")), new ImageIcon(getClass().getResource("res/greyCards/7S.png")), new ImageIcon(getClass().getResource("res/greyCards/8S.png")), new ImageIcon(getClass().getResource("res/greyCards/9S.png")), new ImageIcon(getClass().getResource("res/greyCards/10S.png")), new ImageIcon(getClass().getResource("res/greyCards/QS.png")), new ImageIcon(getClass().getResource("res/greyCards/KS.png")), new ImageIcon(getClass().getResource("res/greyCards/AS.png")), new ImageIcon(getClass().getResource("res/greyCards/5C.png")), new ImageIcon(getClass().getResource("res/greyCards/2S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/KD.png")), new ImageIcon(getClass().getResource("res/greyCards/6S.png")), new ImageIcon(getClass().getResource("res/greyCards/10C.png")), new ImageIcon(getClass().getResource("res/greyCards/9C.png")), new ImageIcon(getClass().getResource("res/greyCards/8C.png")), new ImageIcon(getClass().getResource("res/greyCards/7C.png")), new ImageIcon(getClass().getResource("res/greyCards/6C.png")), new ImageIcon(getClass().getResource("res/greyCards/2D.png")), new ImageIcon(getClass().getResource("res/greyCards/4C.png")), new ImageIcon(getClass().getResource("res/greyCards/3S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/QD.png")), new ImageIcon(getClass().getResource("res/greyCards/5S.png")), new ImageIcon(getClass().getResource("res/greyCards/QC.png")), new ImageIcon(getClass().getResource("res/greyCards/8H.png")), new ImageIcon(getClass().getResource("res/greyCards/7H.png")), new ImageIcon(getClass().getResource("res/greyCards/6H.png")), new ImageIcon(getClass().getResource("res/greyCards/5C.png")), new ImageIcon(getClass().getResource("res/greyCards/3D.png")), new ImageIcon(getClass().getResource("res/greyCards/3C.png")), new ImageIcon(getClass().getResource("res/greyCards/4S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/10D.png")), new ImageIcon(getClass().getResource("res/greyCards/4S.png")), new ImageIcon(getClass().getResource("res/greyCards/KC.png")), new ImageIcon(getClass().getResource("res/greyCards/9H.png")), new ImageIcon(getClass().getResource("res/greyCards/2H.png")), new ImageIcon(getClass().getResource("res/greyCards/5H.png")), new ImageIcon(getClass().getResource("res/greyCards/4C.png")), new ImageIcon(getClass().getResource("res/greyCards/4D.png")), new ImageIcon(getClass().getResource("res/greyCards/2C.png")), new ImageIcon(getClass().getResource("res/greyCards/5S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/9D.png")), new ImageIcon(getClass().getResource("res/greyCards/3S.png")), new ImageIcon(getClass().getResource("res/greyCards/AC.png")), new ImageIcon(getClass().getResource("res/greyCards/10H.png")), new ImageIcon(getClass().getResource("res/greyCards/3H.png")), new ImageIcon(getClass().getResource("res/greyCards/4H.png")), new ImageIcon(getClass().getResource("res/greyCards/3C.png")), new ImageIcon(getClass().getResource("res/greyCards/5D.png")), new ImageIcon(getClass().getResource("res/greyCards/AH.png")), new ImageIcon(getClass().getResource("res/greyCards/6S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/8D.png")), new ImageIcon(getClass().getResource("res/greyCards/2S.png")), new ImageIcon(getClass().getResource("res/greyCards/AD.png")), new ImageIcon(getClass().getResource("res/greyCards/QH.png")), new ImageIcon(getClass().getResource("res/greyCards/KH.png")), new ImageIcon(getClass().getResource("res/greyCards/AH.png")), new ImageIcon(getClass().getResource("res/greyCards/2C.png")), new ImageIcon(getClass().getResource("res/greyCards/6D.png")), new ImageIcon(getClass().getResource("res/greyCards/KH.png")), new ImageIcon(getClass().getResource("res/greyCards/7S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/7D.png")), new ImageIcon(getClass().getResource("res/greyCards/2H.png")), new ImageIcon(getClass().getResource("res/greyCards/KD.png")), new ImageIcon(getClass().getResource("res/greyCards/QD.png")), new ImageIcon(getClass().getResource("res/greyCards/10D.png")), new ImageIcon(getClass().getResource("res/greyCards/9D.png")), new ImageIcon(getClass().getResource("res/greyCards/8D.png")), new ImageIcon(getClass().getResource("res/greyCards/7D.png")), new ImageIcon(getClass().getResource("res/greyCards/QH.png")), new ImageIcon(getClass().getResource("res/greyCards/8S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/6D.png")), new ImageIcon(getClass().getResource("res/greyCards/3H.png")), new ImageIcon(getClass().getResource("res/greyCards/4H.png")), new ImageIcon(getClass().getResource("res/greyCards/5H.png")), new ImageIcon(getClass().getResource("res/greyCards/6H.png")), new ImageIcon(getClass().getResource("res/greyCards/7H.png")), new ImageIcon(getClass().getResource("res/greyCards/8H.png")), new ImageIcon(getClass().getResource("res/greyCards/9H.png")), new ImageIcon(getClass().getResource("res/greyCards/10H.png")), new ImageIcon(getClass().getResource("res/greyCards/9S.png"))},
			{new ImageIcon(getClass().getResource("res/greyCards/corner.png")), new ImageIcon(getClass().getResource("res/greyCards/5D.png")), new ImageIcon(getClass().getResource("res/greyCards/4D.png")), new ImageIcon(getClass().getResource("res/greyCards/3D.png")), new ImageIcon(getClass().getResource("res/greyCards/2D.png")), new ImageIcon(getClass().getResource("res/greyCards/AS.png")), new ImageIcon(getClass().getResource("res/greyCards/KS.png")), new ImageIcon(getClass().getResource("res/greyCards/QS.png")), new ImageIcon(getClass().getResource("res/greyCards/10S.png")), new ImageIcon(getClass().getResource("res/greyCards/corner.png"))}


};

	/*
        Se colocan cuatro jacks en las cuatro esquinas, ya que
	Las esquinas no son cartas reales
        */
	ImageIcon[][] handCardImages = {
			{new ImageIcon(getClass().getResource("res/handCards/JS.png")), new ImageIcon(getClass().getResource("res/handCards/AC.png")), new ImageIcon(getClass().getResource("res/handCards/KC.png")), new ImageIcon(getClass().getResource("res/handCards/QC.png")), new ImageIcon(getClass().getResource("res/handCards/10C.png")), new ImageIcon(getClass().getResource("res/handCards/9C.png")), new ImageIcon(getClass().getResource("res/handCards/8C.png")), new ImageIcon(getClass().getResource("res/handCards/7C.png")), new ImageIcon(getClass().getResource("res/handCards/6C.png")), new ImageIcon(getClass().getResource("res/handCards/JH.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/AD.png")), new ImageIcon(getClass().getResource("res/handCards/7S.png")), new ImageIcon(getClass().getResource("res/handCards/8S.png")), new ImageIcon(getClass().getResource("res/handCards/9S.png")), new ImageIcon(getClass().getResource("res/handCards/10S.png")), new ImageIcon(getClass().getResource("res/handCards/QS.png")), new ImageIcon(getClass().getResource("res/handCards/KS.png")), new ImageIcon(getClass().getResource("res/handCards/AS.png")), new ImageIcon(getClass().getResource("res/handCards/5C.png")), new ImageIcon(getClass().getResource("res/handCards/2S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/KD.png")), new ImageIcon(getClass().getResource("res/handCards/6S.png")), new ImageIcon(getClass().getResource("res/handCards/10C.png")), new ImageIcon(getClass().getResource("res/handCards/9C.png")), new ImageIcon(getClass().getResource("res/handCards/8C.png")), new ImageIcon(getClass().getResource("res/handCards/7C.png")), new ImageIcon(getClass().getResource("res/handCards/6C.png")), new ImageIcon(getClass().getResource("res/handCards/2D.png")), new ImageIcon(getClass().getResource("res/handCards/4C.png")), new ImageIcon(getClass().getResource("res/handCards/3S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/QD.png")), new ImageIcon(getClass().getResource("res/handCards/5S.png")), new ImageIcon(getClass().getResource("res/handCards/QC.png")), new ImageIcon(getClass().getResource("res/handCards/8H.png")), new ImageIcon(getClass().getResource("res/handCards/7H.png")), new ImageIcon(getClass().getResource("res/handCards/6H.png")), new ImageIcon(getClass().getResource("res/handCards/5C.png")), new ImageIcon(getClass().getResource("res/handCards/3D.png")), new ImageIcon(getClass().getResource("res/handCards/3C.png")), new ImageIcon(getClass().getResource("res/handCards/4S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/10D.png")), new ImageIcon(getClass().getResource("res/handCards/4S.png")), new ImageIcon(getClass().getResource("res/handCards/KC.png")), new ImageIcon(getClass().getResource("res/handCards/9H.png")), new ImageIcon(getClass().getResource("res/handCards/2H.png")), new ImageIcon(getClass().getResource("res/handCards/5H.png")), new ImageIcon(getClass().getResource("res/handCards/4C.png")), new ImageIcon(getClass().getResource("res/handCards/4D.png")), new ImageIcon(getClass().getResource("res/handCards/2C.png")), new ImageIcon(getClass().getResource("res/handCards/5S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/9D.png")), new ImageIcon(getClass().getResource("res/handCards/3S.png")), new ImageIcon(getClass().getResource("res/handCards/AC.png")), new ImageIcon(getClass().getResource("res/handCards/10H.png")), new ImageIcon(getClass().getResource("res/handCards/3H.png")), new ImageIcon(getClass().getResource("res/handCards/4H.png")), new ImageIcon(getClass().getResource("res/handCards/3C.png")), new ImageIcon(getClass().getResource("res/handCards/5D.png")), new ImageIcon(getClass().getResource("res/handCards/AH.png")), new ImageIcon(getClass().getResource("res/handCards/6S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/8D.png")), new ImageIcon(getClass().getResource("res/handCards/2S.png")), new ImageIcon(getClass().getResource("res/handCards/AD.png")), new ImageIcon(getClass().getResource("res/handCards/QH.png")), new ImageIcon(getClass().getResource("res/handCards/KH.png")), new ImageIcon(getClass().getResource("res/handCards/AH.png")), new ImageIcon(getClass().getResource("res/handCards/2C.png")), new ImageIcon(getClass().getResource("res/handCards/6D.png")), new ImageIcon(getClass().getResource("res/handCards/KH.png")), new ImageIcon(getClass().getResource("res/handCards/7S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/7D.png")), new ImageIcon(getClass().getResource("res/handCards/2H.png")), new ImageIcon(getClass().getResource("res/handCards/KD.png")), new ImageIcon(getClass().getResource("res/handCards/QD.png")), new ImageIcon(getClass().getResource("res/handCards/10D.png")), new ImageIcon(getClass().getResource("res/handCards/9D.png")), new ImageIcon(getClass().getResource("res/handCards/8D.png")), new ImageIcon(getClass().getResource("res/handCards/7D.png")), new ImageIcon(getClass().getResource("res/handCards/QH.png")), new ImageIcon(getClass().getResource("res/handCards/8S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/6D.png")), new ImageIcon(getClass().getResource("res/handCards/3H.png")), new ImageIcon(getClass().getResource("res/handCards/4H.png")), new ImageIcon(getClass().getResource("res/handCards/5H.png")), new ImageIcon(getClass().getResource("res/handCards/6H.png")), new ImageIcon(getClass().getResource("res/handCards/7H.png")), new ImageIcon(getClass().getResource("res/handCards/8H.png")), new ImageIcon(getClass().getResource("res/handCards/9H.png")), new ImageIcon(getClass().getResource("res/handCards/10H.png")), new ImageIcon(getClass().getResource("res/handCards/9S.png"))},
			{new ImageIcon(getClass().getResource("res/handCards/JD.png")), new ImageIcon(getClass().getResource("res/handCards/5D.png")), new ImageIcon(getClass().getResource("res/handCards/4D.png")), new ImageIcon(getClass().getResource("res/handCards/3D.png")), new ImageIcon(getClass().getResource("res/handCards/2D.png")), new ImageIcon(getClass().getResource("res/handCards/AS.png")), new ImageIcon(getClass().getResource("res/handCards/KS.png")), new ImageIcon(getClass().getResource("res/handCards/QS.png")), new ImageIcon(getClass().getResource("res/handCards/10S.png")), new ImageIcon(getClass().getResource("res/handCards/JC.png"))}

	};
	
	public SequenceGameGUI(SequenceGame game) {
		
		Container contentPane = getContentPane();
        contentPane.setLayout(null);
        
        //Obtener el tamaño de la ventana para que este en pantalla completa 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        
        //Crear un estilo de borde
        Border blackLineBorder = BorderFactory.createLineBorder(Color.BLACK);
        
        
        //set panels
        int x1 = (int)(screenWidth*0.05), y1 = (int)(screenHeight*0.01), w1 = (int)(screenWidth*0.7), h1 = (int)(screenHeight*0.8);
        int y2 = (int)(screenHeight*0.03) + (int)(screenHeight*0.8), h2 = (int)(screenHeight*0.15);
        
        boardPanel = new JPanel(new GridLayout(10, 10));
        boardPanel.setBounds(x1, y1, w1, h1);
        boardPanel.setBorder(blackLineBorder);
        contentPane.add(boardPanel);
        
        handPanel = new JPanel(new FlowLayout());
        handPanel.setBounds(x1, y2, w1, h2);
        handPanel.setBorder(blackLineBorder);
        contentPane.add(handPanel);
        
        playerPanel = new JPanel();
        playerPanel.setBounds((int)(screenWidth*0.07) + (int)(screenWidth*0.70), (int)(screenHeight*0.01), (int)(screenWidth*0.15), (int)(screenHeight*0.8));
        playerPanel.setBorder(blackLineBorder);
        contentPane.add(playerPanel);
        
        deckPanel = new JPanel();
        deckPanel.setBounds((int)(screenWidth*0.07) + (int)(screenWidth*0.70), (int)(screenHeight*0.03) + (int)(screenHeight*0.8), (int)(screenWidth*0.15), (int)(screenHeight*0.15));
        deckPanel.setBorder(blackLineBorder);
        deckPanel.setLayout(new OverlayLayout(deckPanel));
        contentPane.add(deckPanel);
        
        //Calcular la dimensión de cada tarjeta y token
        int bw = boardPanel.getWidth()/10;
        int bh = boardPanel.getHeight()/10;
        int tbw = (int)(bw/1.2);
        int tbh = (int)(bh/1.2);
        
        //Establecer el tamaño de los tokens
        Image Rimg = redToken.getImage();
		Image RnewImg = Rimg.getScaledInstance(tbw, tbh, java.awt.Image.SCALE_SMOOTH);
		redToken = new ImageIcon(RnewImg);

        Image Bimg = blueToken.getImage();
		Image BnewImg = Bimg.getScaledInstance(tbw, tbh, java.awt.Image.SCALE_SMOOTH);
		blueToken = new ImageIcon(BnewImg);
		
        Image Gimg = greenToken.getImage();
		Image GnewImg = Gimg.getScaledInstance(tbw, tbh, java.awt.Image.SCALE_SMOOTH);
		greenToken = new ImageIcon(GnewImg);
        
		//añade las cartas al tablero
        for(int i=0; i<10; i++)
        	for(int j=0; j<10; j++) {
        		//Crear un panel tanto para la tarjeta como para el token (para ser añadida a boardPanel)
        		JPanel grid = new JPanel();
        		grid.setLayout(new OverlayLayout(grid));
        		
        		//Crea un botón para cada carta del tablero
        		SButton b = cardButtons[i][j] = new SButton();
        		
        		//Crea un botón para cada ficha en el tablero
        		SButton t = tokenButtons[i][j] = new SButton();
        		
        		
        		//***TOKEN BUTTON
        		//Asigne el valor único I y J (posición del tablero) a cada botón
        		t.i = i; t.j = j;
        		//set action para el boton de TOKEN
        		t.addActionListener(
        				new ActionListener() {
        					public void actionPerformed(ActionEvent e) {
        						//si ONE-eyed jack se juega
        						//0(a). Desactivar todas las cartas de la mano
        						for (HashMap.Entry<Integer, JButton> mapElement : ((HumanSequencePlayer) (game.currentPlayer)).getHandMap().entrySet()) { 
        							mapElement.getValue().setEnabled(false);
        						}
        						//0(b). Habilitar mazo de cartas para robar una nueva carta
        						deckButton.setEnabled(true);
        						//1. Restablecer todos los iconos de tarjetas desactivados
        						makeAllDisabledCardsNormal();
        						//2. establezca el icono del token en null Y actualice el board[][]
        						t.setIcon(null);
        						game.board[t.i][t.j] = ' ';
        						//3. Deshabilitar todos los botones de token
								for(int i=0; i<10; i++)
									for(int j=0; j<10; j++)
											tokenButtons[i][j].setEnabled(false);
								//4. quita one eyed jack de la mano
        						ASequenceCard card = new ASequenceCard(jackNumber);
        						game.currentPlayer.hand.remove(removedHandCardIndex);
        						if(game.currentPlayer instanceof HumanSequencePlayer) {
	        						handPanel.remove(
	        								((HumanSequencePlayer) game
	        										.currentPlayer)
	        											.getHandMap().get(card.getCardNumber()));
	        						handPanel.repaint();
        						}//end of inner if
        						//5. actualizar SequenceGame
        						game.lastPlayedX = t.i;
        						game.lastPlayedY = t.j;
        						game.lastPlayedCard = card;
        						game.resume();
        					}
        				});
        		t.setEnabled(false);
        		
        		
        		//***BOTÓN DE TARJETA
        		//Asigne el valor único I y J (posición del tablero) a cada botón
        		b.i = i; b.j = j;
        		
        		//retrieve the ImageIcon from array y crear new Image basada en resoluciones
        		Image img = normalCardImages[i][j].getImage();
        		Image newImg = img.getScaledInstance(bw, bh, java.awt.Image.SCALE_SMOOTH);
        		ImageIcon newIcon = new ImageIcon(newImg);
        		b.setIcon(newIcon);
        		b.setDisabledIcon(newIcon);
        		
        		//set action para el botón CARD
        		b.addActionListener(
        				new ActionListener() {
        					public void actionPerformed(ActionEvent e) {
        						//cuando se juega una carta elegible,
        						//0(a). Desactivar todas las cartas de la mano
        						for (HashMap.Entry<Integer, JButton> mapElement : ((HumanSequencePlayer) (game.currentPlayer)).getHandMap().entrySet()) { 
        							mapElement.getValue().setEnabled(false);
        						}
        						//0(b). Habilitar mazo de cartas para robar una nueva carta
        						deckButton.setEnabled(true);
        						//1. actualizar board[][]
        						char color = game.currentPlayerColor; 
        						game.board[b.i][b.j] = color;
        						//2. Mostrar token de jugador
        						switch(color) {
        							case 'r': t.setIcon(redToken); t.setDisabledIcon(redToken); break;
        							case 'b': t.setIcon(blueToken); t.setDisabledIcon(blueToken); break;
        							case 'g': t.setIcon(greenToken); t.setDisabledIcon(greenToken); break;
        						}
        						//3. Restablecer todos los iconos desactivados
        						makeAllDisabledCardsNormal();
        						//n. hacer cosas diferentes en función de si se juega o no al Wild Jack
        						if(twoEyedJackIsPlayed) {
        							//4. deshabiliat todas las cartas
        							disableAllCards();
        							//5. quitar wild jack de la mano
            						ASequenceCard card = new ASequenceCard(jackNumber);
            						game.currentPlayer.hand.remove(removedHandCardIndex);
            						if(game.currentPlayer instanceof HumanSequencePlayer) {
    	        						handPanel.remove(
    	        								((HumanSequencePlayer) game
    	        										.currentPlayer)
    	        											.getHandMap().get(card.getCardNumber()));
    	        						handPanel.repaint();
            						}//end of inner if
            						//6. Restablecer campos de conector
            						twoEyedJackIsPlayed = false;
            						//7. actualizar lastPlayedCard
            						game.lastPlayedCard = card;
        						}
        						else {
        							// non-jack card se juega
        							//4. Deshabilite las dos tarjetas de opción
            						cardButtons[b.x1][b.y1].setEnabled(false);
            						cardButtons[b.x2][b.y2].setEnabled(false);
            						//5. Retira la tarjeta de la mano
            						ASequenceCard card = new ASequenceCard(b.cardNumber);
            						game.currentPlayer.hand.remove(removedHandCardIndex);
            						if(game.currentPlayer instanceof HumanSequencePlayer) {
    	        						handPanel.remove(
    	        								((HumanSequencePlayer) game
    	        										.currentPlayer)
    	        											.getHandMap().get(card.getCardNumber()));
    	        						handPanel.repaint();
            						}//fin de la declaración del inner if 
            						//6. actualizar lastPlayedCard
            						game.lastPlayedCard = card;
        						}//end de else
        						
        						game.lastPlayedX = b.i;
        						game.lastPlayedY = b.j;
        						game.resume();
        					}//end del actionPerformed
        				});
        		b.setEnabled(false);
        		
        		//Actualice la matriz con nuevas imágenes de tarjeta para esta resolución
        		normalCardImages[i][j] = newIcon;
        		

        		//Agregar este botón de token al panel de cuadrícula
        		t.setBorder(null);
        		t.setContentAreaFilled(false);
        		t.setAlignmentX(CENTER_ALIGNMENT);
        		t.setAlignmentY(CENTER_ALIGNMENT);
        		grid.add(t);
        		
        		//Agregue este botón de tarjeta al panel específico de esta cuadrícula
        		b.setOpaque(false);
        		b.setAlignmentX(CENTER_ALIGNMENT);
        		b.setAlignmentY(CENTER_ALIGNMENT);
        		grid.add(b);
        		
        		//Agregue este panel de cuadrícula al panel Tablero
        		boardPanel.add(grid);
        		
        		//Cambiar el tamaño de las imágenes de la tarjeta gris para usarlas en el futuro
        		Image greyImg = greyCardImages[i][j].getImage();
        		Image newGreyImg = greyImg.getScaledInstance(bw, bh, java.awt.Image.SCALE_SMOOTH);
        		ImageIcon newGreyIcon = new ImageIcon(newGreyImg);
        		greyCardImages[i][j]= newGreyIcon;

        		//Cambiar el tamaño de las imágenes de las tarjetas de mano para usarlas en el futuro
        		Image handImg = handCardImages[i][j].getImage();
        		Image newHandImg = handImg.getScaledInstance(bh, bw, java.awt.Image.SCALE_SMOOTH);
        		ImageIcon newHandIcon = new ImageIcon(newHandImg);
        		handCardImages[i][j]= newHandIcon;
        	}
        
        //deshabilitar corners
      		cardButtons[0][0].setEnabled(false);
      		cardButtons[0][9].setEnabled(false);
      		cardButtons[9][0].setEnabled(false);
      		cardButtons[9][9].setEnabled(false);
        
        //set buttons
      	//rendirse (ESTE BOTON TIENE QUE SER ELIMINADO YA QUE EN CLASE SE ESPECIFICO QUE NO SE PUEDE RENDIR)
        JButton quit = new JButton("Quit");
        quit.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				System.exit(0);
                                        
                                        try {
                                            RandomAccessFile registro = new RandomAccessFile (login.logged.getUsername() + ".rep", "rw");
                                        
                                            registro.seek(registro.length());
                                            Calendar now = Calendar.getInstance();

                                            registro.writeLong(now.getTimeInMillis());
                                        } catch (IOException f){
                                            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado");
                                        }
                                        
        			}
        		});
        quit.setSize(100, 100);
        playerPanel.add(quit);
        
        //deck button
        //resize icon
        ImageIcon deckIcon = new ImageIcon(getClass().getResource("res/cardBack.png"));
		Image deckImg = deckIcon.getImage();
		Image newDeckImg = deckImg.getScaledInstance(bw, bh, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newDeckIcon = new ImageIcon(newDeckImg);
		deckButton = new JButton(newDeckIcon);
		deckButton.setBorder(BorderFactory.createEmptyBorder());
		deckButton.setContentAreaFilled(false);
		deckButton.setAlignmentX(CENTER_ALIGNMENT);
		deckButton.setAlignmentY(CENTER_ALIGNMENT);
        deckButton.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				//disable to avoid drawing again
        				deckButton.setEnabled(false);
        				
        				ASequenceCard c;
        				
        				if(isRecycled) {
        					//NO termine el turno
            				//Roba una nueva carta para añadirla a la mano del jugador (LinkedList)
            				c = game.Chris.dealCard(game.currentPlayer);
            				//Mostrar la nueva tarjeta
            				game.displayNewHandCard(c, (HumanSequencePlayer)game.currentPlayer);
            				//Quitar la tarjeta de reciclaje
        					game.currentPlayer.hand.remove(recycledCard);
        					handPanel.remove(
    								((HumanSequencePlayer) game
    										.currentPlayer)
    											.getHandMap().get(recycledCard.getCardNumber()));
        					((HumanSequencePlayer) game
        									.currentPlayer)
        										.getHandMap()
        											.get(c.getCardNumber())
        												.setEnabled(true);
    						handPanel.repaint();
    						
    						//isRecycled = false;
        				}
        				else {
	        				//end del turno
            				
            				//Roba una nueva carta para añadirla a la mano del jugador (LinkedList)
            				c = game.Chris.dealCard(game.currentPlayer);
            				//Mostrar la nueva tarjeta
            				game.displayNewHandCard(c, (HumanSequencePlayer)game.currentPlayer);
            				
							game.resume();
        				}
        			}//end of actionPerformed
        		});
        deckButton.setEnabled(false);
        deckPanel.add(deckButton);
        
        
        	
        //set size and name
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setUndecorated(true);
        setTitle("Sequence Game");        
        
	}
	
	
	void makeAllDisabledCardsNormal() {
		//Hacer que el icono de todos los botones de la tarjeta sea normal
		for(int i=0; i<10; i++)
			for(int j=0; j<10; j++)
				cardButtons[i][j].setDisabledIcon(normalCardImages[i][j]);
	}
	
	void makeAllDisabledCardsGrey(int option) {
		switch(option) {
			case 0:
				//hacer que todos los botones de la tarjeta sean grises
				for(int i=0; i<10; i++)
					for(int j=0; j<10; j++) {
						cardButtons[i][j].setDisabledIcon(greyCardImages[i][j]);
					}
				break;
			case 1:
				//Deshabilite todas las tarjetas y haga que el icono de todos los botones de la tarjeta sea gris
				for(int i=0; i<10; i++)
					for(int j=0; j<10; j++) {
						cardButtons[i][j].setDisabledIcon(greyCardImages[i][j]);
						cardButtons[i][j].setEnabled(false);
					}
				break;
		}
	}
	
	void enableAllCards(SequenceGame game, int option) {
		switch(option) {
			case 0:
				//Habilitar todas las cartas en el tablero
				for(int i=0; i<10; i++)
					for(int j=0; j<10; j++) {
						cardButtons[i][j].setEnabled(true);
					}
				break;
			case 1:
				//habilitar todas las tarjetas DISPONIBLES en el tablero
				for(int i=0; i<10; i++)
					for(int j=0; j<10; j++) {
						if(game.board[i][j]==' ')
							cardButtons[i][j].setEnabled(true);
						else
							cardButtons[i][j].setDisabledIcon(greyCardImages[i][j]);
					}
				break;
		}

		//deshabilitar corners
		cardButtons[0][0].setEnabled(false);
		cardButtons[0][9].setEnabled(false);
		cardButtons[9][0].setEnabled(false);
		cardButtons[9][9].setEnabled(false);
	}
	
	void disableAllCards() {
		//Desactiva todas las cartas del tablero
				for(int i=0; i<10; i++)
					for(int j=0; j<10; j++) {
						cardButtons[i][j].setEnabled(false);
					}
	}



}

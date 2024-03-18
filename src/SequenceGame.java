package proyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class SequenceGame {
	
	protected char[][] board;
	char currentPlayerColor; 
	char gameResult = ' ';
	ASequencePlayer[] playerList;
	ASequencePlayer currentPlayer;
	String winner;
	CardDealer Chris;
	final char BLANK = ' ', CORNER = 'C';
	SequenceGameGUI gui;
	SequenceGame thisGame = this;
	Border empty = BorderFactory.createEmptyBorder();
	boolean oneEyedJackIsPlayed = false;
	int isStartingNewGame = JOptionPane.YES_OPTION;
	// set by SequenceGameGUI:
		//Coordenadas x e y de la carta jugada más recientemente
		int lastPlayedX = -1, lastPlayedY = -1;
		ASequenceCard lastPlayedCard;
	//para testing 
		String startUpTime, winBy;
		int turn = 0;
		SequenceLog log;
        
                
        public ASequencePlayer [] getPlayerList(){
            return playerList;
        }
        
        public void setPlayerList(){
            for (int i = 0; i < menuPrincipal.jugadores.size(); i++) {
                playerList[i] = menuPrincipal.jugadores.get(i);
            }
        }
	
	public SequenceGame(int numberOfPlayers) throws InterruptedException {
	
                
		long startTime = System.nanoTime();
		
		setBoard();
		createPlayers(numberOfPlayers);
		gui = new SequenceGameGUI(this);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dealCards(numberOfPlayers);
		
		gui.setVisible(true);

        long endTime = System.nanoTime();
        startUpTime = String.valueOf(endTime - startTime);
        System.out.println(startUpTime);
        
        startGame(numberOfPlayers);
        isStartingNewGame = JOptionPane.showConfirmDialog(gui, "Diviertete en este juego :D", 
        		"Jugar Otra vez?", JOptionPane.YES_NO_OPTION);
        gui.dispose();
	}


	void setBoard() {
		
		board = new char[10][10];
		
		//Hace que cada ranura del tablero esté en blanco
		for(int r=0; r<10; r++)
			for(int c=0; c<10; c++)
				board[r][c] = BLANK;
		
		//fill de los four corners
		board[0][0] = CORNER; board[0][9] = CORNER;
		board[9][0] = CORNER; board[9][0] = CORNER;
	}
	
	void createPlayers(int n) {
		
		if(n<1)
			System.out.println("Necesitas al menos un jugador para jugar");
		else if(n==1) {
			//modo de un solo jugador, hacer un reproductor de CPU
			playerList = new ASequencePlayer[2];
			
			playerList[0] = new HumanSequencePlayer(0, "Default");
			playerList[1] = new CpuSequencePlayer(1);
		}
		else {
			//modo multijugador, no se necesita reproductor de CPU
			playerList = new ASequencePlayer[n];
                        setPlayerList();
			
//			for(int i=0; i<n; i++) {
//				playerList[i] = new HumanSequencePlayer(i);
//			}
				
		}
		
	}
	
	void dealCards(int numberOfPlayers) {
		//NECESITA TRABAJO, ESPECIFIQUE CASOS PARA 1,2,3 JUGADORES
		Chris = new CardDealer();
		Chris.shuffleCards();//Barajar cartas
		Chris.shuffle(playerList);//Barajar el orden de los jugadores
		
		switch(numberOfPlayers) {
			case 1:
				for(int i=0; i<6; i++)
					//un jugador + una CPU, ¡todos reciben SEIS cartas!
					for(int j=0; j<2; j++) {
						//Reparte una carta a cada jugador
						ASequenceCard c = Chris.dealCard(playerList[j]);

						//agregar cartas de mano a la GUI para el jugador (el único jugador)
						if(playerList[j] instanceof HumanSequencePlayer) 
							displayNewHandCard(c, (HumanSequencePlayer)playerList[j]);
					}
				break;
			case 2:
				//NECESITA TRABAJO, AÚN NO ES COMPATIBLE CON EL MODO MULTIJUGADOR
				for(int i=0; i<7; i++)
					//reparte seis cartas si son DOS jugadores
					for(int j=0; j<numberOfPlayers; j++){
                                            ASequenceCard c = Chris.dealCard(playerList[j]);
                                            displayNewHandCard(c, (HumanSequencePlayer)playerList[j]);
                                        }
						
				break;
			case 3:
				//NEED WORK, DOES NOT SUPPORT MULTI PLAYER YET
				for(int i=0; i<7; i++)
					//reparten siete cartas si son TRES jugadores
					for(int j=0; j<numberOfPlayers; j++)
						Chris.dealCard(playerList[j]);
				break;
			default: 
				System.out.println("El juego solo admite hasta tres jugadores.");//pero aqui debe entra lo de equipos
		}			
		
	}//Cartas de fin de reparto
	
	boolean isGameOver(int x, int y) {
		/*
		 * Introduzca la coordenada x, y del token que se acaba de reproducir
		 * Determinar si algún jugador ha ganado
		 */
		
		//el juego acaba de comenzar, el juego NO debe terminar
		if(lastPlayedX == -1)
			return false;
		
		//si one-eyed jack es jugada, el juego NO debe terminar
		if(oneEyedJackIsPlayed) {
			oneEyedJackIsPlayed = false;
			return false;
		}
		
		//chequaer corners
		//Top-left
		if(board[0][1]!=' ' && board[0][1]==board[0][2] && board[0][2]==board[0][3] && board[0][3]==board[0][4]) {
			gameResult = board[0][1];
			winner = currentPlayer.playerName;
			winBy = "Top-Left Corner (0)";
			return true;
		}
		if(board[1][0]!=' ' && board[1][0]==board[2][0] && board[2][0]==board[3][0] && board[3][0]==board[4][0]) {
			gameResult = board[1][0];
			winner = currentPlayer.playerName;
			winBy = "Top-Left Corner (1)";
			return true;
		}
		if(board[1][1]!=' ' && board[1][1]==board[2][2] && board[2][2]==board[3][3] && board[3][3]==board[4][4]) {
			gameResult = board[1][1];
			winner = currentPlayer.playerName;
			winBy = "Top-Left Corner (2)";
			return true;
		}
		
		//Top-right
		if(board[0][5]!=' ' && board[0][5]==board[0][6] && board[0][6]==board[0][7] && board[0][7]==board[0][8]) {
			gameResult = board[0][5];
			winner = currentPlayer.playerName;
			winBy = "Top-Right Corner (0)";
			return true;
		}
		if(board[1][9]!=' ' && board[1][9]==board[2][9] && board[2][9]==board[3][9] && board[3][9]==board[4][9]) {
			gameResult = board[1][1];
			winner = currentPlayer.playerName;
			winBy = "Top-Right Corner (1)";
			return true;
		}
		if(board[1][8]!=' ' && board[1][8]==board[2][7] && board[2][7]==board[3][6] && board[3][6]==board[4][5]) {
			gameResult = board[1][8];
			winner = currentPlayer.playerName;
			winBy = "Top-Right Corner (2)";
			return true;
		}
		
		//Bottom-left
		if(board[0][8]!=' ' && board[0][8]==board[0][7] && board[0][7]==board[0][6] && board[0][6]==board[0][5]) {
			gameResult = board[0][8];
			winner = currentPlayer.playerName;
			winBy = "Bottom-Left Corner (0)";
			return true;
		}
		if(board[9][1]!=' ' && board[9][1]==board[9][2] && board[9][2]==board[9][3] && board[9][3]==board[9][4]) {
			gameResult = board[9][1];
			winner = currentPlayer.playerName;
			winBy = "Bottom-Left Corner (1)";
			return true;
		}
		if(board[8][1]!=' ' && board[8][1]==board[7][2] && board[7][2]==board[6][3] && board[6][3]==board[5][4]) {
			gameResult = board[8][1];
			winner = currentPlayer.playerName;
			winBy = "Bottom-Left Corner (2)";
			return true;
		}
		
		//Bottom-right
		if(board[9][8]!=' ' && board[9][8]==board[9][7] && board[9][7]==board[9][6] && board[9][6]==board[9][5]) {
			gameResult = board[9][8];
			winner = currentPlayer.playerName;
			winBy = "Bottom-Right Corner (0)";
			return true;
		}
		if(board[8][9]!=' ' && board[8][9]==board[7][9] && board[7][9]==board[6][9] && board[6][9]==board[5][9]) {
			gameResult = board[8][9];
			winner = currentPlayer.playerName;
			winBy = "Bottom-Right Corner (1)";
			return true;
		}
		if(board[8][8]!=' ' && board[8][8]==board[7][7] && board[7][7]==board[6][6] && board[6][6]==board[5][5]) {
			gameResult = board[8][8];
			winner = currentPlayer.playerName;
			winBy = "Bottom-Right Corner (2)";
			return true;
		}
		
		
		
		/*Para cada ficha colocada, marque 8 formas
		 * if:
		 *  left + right >= 4 o
		 *  up + down >= 4 o
		 *  LeftUp + RightDown >= 4 o
		 *  LeftDown + RightUp >= 4 
		 * entonces return true
		 */

		//sum = Número total de fichas consecutivas y del mismo color
		int sum = 0;

		//left+right
		//left
		for(int i=y; i>0; i--) {
			if(board[x][i]==board[x][i-1])
				sum++;
			else
				break;
		}
		//right
		for(int i=y; i<9; i++) {
			if(board[x][i]==board[x][i+1])
				sum++;
			else
				break;
		}
		if(sum>=4) {
			gameResult = board[x][y];
			winner = currentPlayer.playerName;
			winBy = "Left-Right Raya: " + sum;
			return true;
		}
		else
			sum = 0;
		
		//up+down
		//up
		for(int i=x; i>0; i--) {
			if(board[i][y]==board[i-1][y])
				sum++;
			else
				break;
		}
		//down
		for(int i=x; i<9; i++) {
			if(board[i][y]==board[i+1][y])
				sum++;
			else
				break;
		}
		if(sum>=4) {
			gameResult = board[x][y];
			winner = currentPlayer.playerName;
			winBy = "Up-Down Raya: " + sum;
			return true;
		}
		else
			sum = 0;
		
		//LeftUp + RightDown
		//LeftUp
		for(int i=x, j=y; i>0&&j>0; i--, j--) {
			if(board[i][j]==board[i-1][j-1]) {
				System.out.println("Left-up: " +i + ", " +j);
				sum++;
			}
			else
				break;
		}
		//RightDown
		for(int i=x, j=y; i<9&&j<9; i++, j++) {
			if(board[i][j]==board[i+1][j+1]) {
				System.out.println("Left-up: " +i + ", " +j);
				sum++;
			}
			else
				break;
		}
		if(sum>=4) {
			gameResult = board[x][y];
			winner = currentPlayer.playerName;
			winBy = "Izquierda Arriba + Derecha Abajo Diagonal: " + sum;
			return true;
		}
		else
			sum = 0;
		
		//LeftDown + RightUp
		//LeftDown
		for(int i=x, j=y; i>0&&j<9; i--, j++) {
			if(board[i][j]==board[i-1][j+1])
				sum++;
			else
				break;
		}
		//RightUp
		for(int i=x, j=y; i<9&&j>0; i++, j--) {
			if(board[i][j]==board[i+1][j-1])
				sum++;
			else
				break;
		}
		if(sum>=4) {
			gameResult = board[x][y];
			winner = currentPlayer.playerName;
			winBy = "IzquierdaAbajo + DerechaArriba Diagonal: " + sum;
			return true;
		}

		
		//Si la mesa está llena, pero nadie ha ganado, entonces es un empate
		boolean hasEmpty = false;
		outerloop:
		for(int i=0; i<10; i++)
			for(int j=0; j<10; j++) 
				if(board[i][j]==' ') {
					hasEmpty = true;
					break outerloop;
				}
		
		if(!hasEmpty) {
			//El juego termina en empate
			gameResult = 'T';
			winner = "EMPATE";
			winBy = "Empate: tablero completo";
			return true;
		}
		
		//Si ninguna de las opciones anteriores devuelve true, devuelve false
		return false;
	}//fin del isGameOver

	
	
	void displayNewHandCard(ASequenceCard c, HumanSequencePlayer player) {
		JButton b = new JButton();
		//Desactiva inmediatamente este botón
		b.setEnabled(false);
		

		int x1 = c.getX1(), y1 = c.getY1();
		int x2 = c.getX2(), y2 = c.getY2();
		int cardNumber = c.getCardNumber();

		//añadir el botón de esta carta a la mano del handMap
		player.getHandMap().put(new Integer(cardNumber), b);
		
		//Establecer acción cuando se selecciona una carta de mano
		b.setBorder(empty);
		b.setIcon(gui.handCardImages[x1][y1]);
		b.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Restablecer los campos jack de la GUI
						gui.twoEyedJackIsPlayed = false;
						oneEyedJackIsPlayed = false;
						
						//Restablecer campos de reciclaje
						gui.isRecycled = false;
						gui.deckButton.setEnabled(false);
						
						//Establece el índice de esta carta en la mano si se quita
						gui.removedHandCardIndex = currentPlayer.hand.indexOf(c);
						
						if(!c.getIsTwoEyedJack()) {
							//non-jack or one-eyed jack
							gui.makeAllDisabledCardsGrey(1);
							
							if(!c.getIsOneEyedJack()) {
								/*
                                                            Una tarjeta que no es jack
								Deshabilite todos los botones de token disponibles
                                                            */
								for(int i=0; i<10; i++)
									for(int j=0; j<10; j++)
											gui.tokenButtons[i][j].setEnabled(false);
								
								SButton b1 = gui.cardButtons[x1][y1];
								SButton b2 = gui.cardButtons[x2][y2];
								//store the two positions of the same card
								b1.x1 = x1; b1.y1 = y1; b2.x1 = x1; b2.y1 = y1;
								b1.x2 = x2; b2.y2 = y2; b2.x2 = x2; b2.y2 = y2;
								//store the card number
								b1.cardNumber = cardNumber;
								b2.cardNumber = cardNumber;
								//resalte las tarjetas en el tablero Y habilite los botones
								if(board[x1][y1]==' ') {
									b1.setIcon(gui.normalCardImages[x1][y1]);
									b1.setEnabled(true);
								}
								if(board[x2][y2]==' ') {
									b2.setIcon(gui.normalCardImages[x2][y2]);
									b2.setEnabled(true);
								}
								if(board[x1][y1]!=' ' && board[x2][y2]!=' ') {
									//Se toman los dos lugares, se recicla
									gui.isRecycled = true;
									gui.recycledCard = c;
									gui.removedHandCardIndex = currentPlayer.hand.indexOf(c);
									gui.deckButton.setEnabled(true);
								}
							}
							else {
								//an one eyed jack
								//Deshabilite todos los botones de token disponibles
								for(int i=0; i<10; i++)
									for(int j=0; j<10; j++)
											gui.tokenButtons[i][j].setEnabled(false);
								
								oneEyedJackIsPlayed = true;
								gui.jackNumber = cardNumber;
								//Habilite todos los botones de token disponibles
								for(int i=0; i<10; i++)
									for(int j=0; j<10; j++)
										if(board[i][j] != ' ')
											gui.tokenButtons[i][j].setEnabled(true);
							}//inner else												
						}
						else {
							//a two eyed jack
							gui.twoEyedJackIsPlayed = true;
							gui.jackNumber = cardNumber;
							gui.enableAllCards(thisGame, 1);
						}
					}
					
				});

		gui.handPanel.add(b);
		gui.handPanel.revalidate();
	}

	void startGame(int numberOfPlayers) throws InterruptedException {
		
		log = new SequenceLog();
		log.updateLog("Tiempo de puesta en marcha: " + startUpTime + "\n");
		
		switch(numberOfPlayers) {
		case 1:
			//Modo para un jugador
			ASequencePlayer p0 = playerList[0];
			HumanSequencePlayer human;
			CpuSequencePlayer cpu;
                        
			if(p0 instanceof HumanSequencePlayer) {
				human = (HumanSequencePlayer) p0;
				cpu = (CpuSequencePlayer) playerList[1];
			}
			else {
				cpu = (CpuSequencePlayer) p0;
				human = (HumanSequencePlayer) playerList[1];
			}
			
			//assignar el nombre
			String name = JOptionPane.showInputDialog(gui, "Please enter your name: ", "Sugar Rainbow");

			//Comprueba si el nombre es todo espacio en blanco, pero al iniciar sesion no sera necesario
                        //pues ya deberia de extraer el nombre, asi que deje esto de prueba
			boolean isBlank = true;
			if(name!=null) {
				int len = name.length();
				for(int i=0; i<len; i++)
					if(name.charAt(i)!= ' ') {
						isBlank = false;
						break;
					}
			}
					
			if(name=="" || isBlank) {
				//set default name
				human.playerName = "Sugar Rainbow";
				//notifica al user
				JOptionPane.showMessageDialog(gui, "Su nombre predeterminado es: Sugar Rainbow", "¿Has renunciado a la oportunidad de nombrarte?",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				human.playerName = name;
			}
			
			//assign player color
			JList<ImageIcon> list = new JList<ImageIcon>(new ImageIcon[] {gui.redToken, gui.blueToken, gui.greenToken});
			JOptionPane.showMessageDialog(
			  gui, list, "Por favor, elija su color", JOptionPane.PLAIN_MESSAGE);
			
			//player si no elije color
			while(list.getSelectedIndex()==-1) {
				JOptionPane.showMessageDialog(gui, "No, DEBE seleccionar un color", "Podemos hacer esto todo el día", JOptionPane.ERROR_MESSAGE);
				
				JOptionPane.showMessageDialog(
						  gui, list, "Por favor, elija su color", JOptionPane.PLAIN_MESSAGE);
			}
                      
                        
			
			switch(list.getSelectedIndex()) {
				case 0: 
					human.playerColor = 'r';
					cpu.selectColor(0);
				break;
				case 1: human.playerColor = 'b'; 
					cpu.selectColor(1);
				break;
				case 2: human.playerColor = 'g'; 
					cpu.selectColor(2);
				break;
			}
			//info del jugador
			String playerInfo = "User's name: " + human.playerName + "(" + human.playerColor + ")\n"
					+ "CPU chose color: " + cpu.playerColor + "\n";
			log.updateLog(playerInfo);
                        
			
			//deja iniciar la partida

                        
			if(p0 instanceof HumanSequencePlayer) {
				//empezamos primero
				while(true) {
					if(isGameOver(lastPlayedX, lastPlayedY)) {
						//game is over
						endGame();
						break;
					}
					else {
						currentPlayer = human;
						currentPlayerColor = human.playerColor;
						human.enableAllHandCards();
						
						//nuestra mano
						log.updateLog("Nuestro turno " + turn + ":"
								+ "\n\tMano Actual: " + currentPlayer.getHand());
						
						//espera a que hagamos el movimiento
						synchronized(this) {
							wait();
						} 
						//nuestro movimiento
						log.updateLog("\tMovimiento: " + lastPlayedCard.getCardName() +
								" en " + "(" + lastPlayedX + ", " + lastPlayedY + ")");
						
						//chequea si el movimiento gana
						if(isGameOver(lastPlayedX, lastPlayedY)) {
							//game is over
							endGame();
							break;
						}
						synchronized(this) {
							wait();
						} 
					}

					//turno de la compu
					currentPlayer = cpu;
					currentPlayerColor = cpu.playerColor;

					//cpu's mano
					log.updateLog("CPU turn " + turn + ":"
							+ "\n\tCurrent hand: " + currentPlayer.getHand());
					
					//computer hace el movimiento
					cpu.makeAMove(thisGame);
					//cpu's move
					log.updateLog("\tMovimiento: " + lastPlayedCard.getCardName() +
							" en " + "(" + lastPlayedX + ", " + lastPlayedY + ")");
					
					//actualizar turno
					turn++;
					
				}//while externo
			}//fin del if
			else {
				//cpu empieza primero
				while(true) {
					//turno de la compu
					currentPlayer = cpu;
					currentPlayerColor = cpu.playerColor;
					//log cpu's hand
					log.updateLog("CPU turno " + turn + ":"
							+ "\n\tMano Actual: " + currentPlayer.getHand());
                                     
					
					//movimiento de la compu
					cpu.makeAMove(thisGame);
					//log cpu's move
					log.updateLog("\tMovimiento: " + lastPlayedCard.getCardName() +
							" en " + "(" + lastPlayedX + ", " + lastPlayedY + ")");

					if(isGameOver(lastPlayedX, lastPlayedY)) {
                                            JOptionPane.showMessageDialog(null, "entra");
						//game is over
						endGame();
						break;
					}
					else {
						currentPlayer = human;
						currentPlayerColor = human.playerColor;
						
						//movimiento de mano
						log.updateLog("Turno de nosotros " + turn + ":"
								+ "\n\tMano actual: " + currentPlayer.getHand());

						human.enableAllHandCards();
                                                
                                                
						//espera que hagamos nuestro movimiento
						synchronized(this) {
							wait();
						}
						//nuestro movimiento
						log.updateLog("\tMovimiento: " + lastPlayedCard.getCardName() +
								" en " + "(" + lastPlayedX + ", " + lastPlayedY + ")");
                                                JOptionPane.showMessageDialog(null, "REGISTERED");
						
						if(isGameOver(lastPlayedX, lastPlayedY)) {
							//game is over
							endGame();
							break;
						}
						synchronized(this) {
							wait();
						}
					}
					
					//actualizar turno
					turn++;
					
				}//while externo
			}//fin del else	
		case 2:
			break;
		case 3:
		}//switch
	}
	
	void resume() {
		synchronized(this) {
			notify();
		}
	}
	
	void endGame() {
		//game is over
		JOptionPane.showMessageDialog(null,"Ganador es: " + winner,
				"GAME OVER",JOptionPane.ERROR_MESSAGE);
		//log endgame
		log.updateLog("\tMovimiento: " + lastPlayedCard.getCardName() +
				" at " + "(" + lastPlayedX + ", " + lastPlayedY + ")");
		log.updateLog("\nEl juego a terminado al turno " + turn + "\nGanador es: " + winner
				+ ", por " + winBy);
	}
	
	public static void main(String[] args) throws InterruptedException {

		while(true) {
			SequenceGame g = new SequenceGame(1);

	        //Dar aviso al comenzar un nuevo juego
			if(g.isStartingNewGame == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null,"Cargando nueva partida...",
						"Just a second",JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null,"Bye :(",
						"Play again!",JOptionPane.INFORMATION_MESSAGE);
				
				break;
			}

			/*
			ASequencePlayer[] l = g.playerList;

			System.out.println("Players:");
			for(int i=0; i<l.length;i++) {
				System.out.print("\tPlayer " + l[i].playerNumber + String.valueOf(l[i].getClass()) + ":\n\t\tHand: ");
				l[i].printHand();
			}
				*/
		}

	}

}

package proyecto;

import java.awt.FlowLayout;


import javax.swing.JButton;
import javax.swing.JLabel;

class SButton extends JButton{
	
	//hace todo lo que hace JButton, pero también almacena otros 
	//información relacionada con Sequence
	
	//La posición única de este botón en el tablero
	int i, j;
	//las DOS posiciones de la misma carta en el tablero
	int x1, y1, x2, y2;
	//El número de tarjeta/carta
	int cardNumber;

}

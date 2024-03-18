/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.Color;
import java.io.File;

/**
 *
 * @author hecto
 */
public class Ficha {
    File puestaSobre;
    Color color;

    public Ficha(File puestaSobre, Color color) {
        this.puestaSobre = puestaSobre;
        this.color = color;
    }

    public File getPuestaSobre() {
        return puestaSobre;
    }

    public void setPuestaSobre(File puestaSobre) {
        this.puestaSobre = puestaSobre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

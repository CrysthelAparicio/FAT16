/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofat16;

import java.nio.ByteBuffer;

/**
 *
 * @author Crys
 */
public class data {

    private char primer_char;
    private char[] nombre_archiv = new char[10];
    private char atrib;
    private long fecha_creacion;
    private short dir_cluster;
    private int tamano;
    private byte[] reserv = new byte[6];

    public data(byte[] data) {

        byte[] fecha_array = new byte[8];
        byte[] tamano_array = new byte[4];
        byte[] dir_cluster_array = new byte[2];

        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                this.primer_char = (char) data[i];
            } else if (i > 0 && i < 11) {
                this.nombre_archiv[i - 1] = (char) data[i];
            } else if (i == 11) {
                this.atrib = (char) data[i];
            } else if (i > 11 && i < 20) {
                fecha_array[i - 12] = data[i];
            } else if (i > 19 && i < 22) {
                dir_cluster_array[i - 20] = data[i];
            } else if (i > 21 && i < 26) {
                tamano_array[i - 22] = data[i];
            } else if (i > 25) {
                this.reserv[i - 26] = data[i];
            }
        }

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(fecha_array);
        buffer.flip();//need flip 
        this.fecha_creacion = buffer.getLong();

        buffer = ByteBuffer.allocate(4);
        buffer.put(tamano_array);
        buffer.flip();//need flip 
        this.tamano = buffer.getInt();

        buffer = ByteBuffer.allocate(2);
        buffer.put(dir_cluster_array);
        buffer.flip();//need flip 
        this.dir_cluster = buffer.getShort();

    }

    public char getPrimer_char() {
        return primer_char;
    }

    public char[] getNombre_archiv() {
        return nombre_archiv;
    }

    public String getNombre_archivString() {
        String nombre_archivString = "";
        for (int i = 0; i < this.nombre_archiv.length; i++) {
            if (this.nombre_archiv[i] != (char) ((byte) 0)) {
                nombre_archivString += this.nombre_archiv[i];
            }
        }
        return nombre_archivString;
    }

    public char getAtrib() {
        return atrib;
    }

    public long getFecha_creacion() {
        return fecha_creacion;
    }

    public short getDir_cluster() {
        return dir_cluster;
    }

    public int getTamano() {
        return tamano;
    }

    public byte[] getReserv() {
        return reserv;
    }

    public void setPrimer_char(char primer_char) {
        this.primer_char = primer_char;
    }

    public void setNombre_archiv(char[] nombre_archiv) {
        this.nombre_archiv = nombre_archiv;
    }

    public void setAtrib(char atrib) {
        this.atrib = atrib;
    }

    public void setFecha_creacion(long fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public void setDir_cluster(short dir_cluster) {
        this.dir_cluster = dir_cluster;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public void setReserv(byte[] reserv) {
        this.reserv = reserv;
    }

    @Override
    public String toString() {
        String nombre_archivo = "";
        for (int i = 0; i < this.nombre_archiv.length; i++) {
            nombre_archivo += this.nombre_archiv[i];
        }
        return "metadata{" + "primer_char=" + primer_char + ", nombre_archivo=" + nombre_archivo + ", atrib=" + atrib + ", fecha_creacion=" + fecha_creacion + ", dir_cluster=" + dir_cluster + ", tamano=" + tamano + ", reserv=" + reserv + '}';
    }

}

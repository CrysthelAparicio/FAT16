/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Crys
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class main {

    // Tam en bytes
    final static int TAM_CLUSTER = 4096;
    final static int TAM_RAIZ = 16384;
    final static int TAM_FAT = 131072;
    final static int TAM_DIRENTRY = 32;
    final static int TAM_FATENTRY = 2;

    //cantidad de entradas posibles
    final static int ENTRADASMAX_RAIZ = TAM_RAIZ / TAM_DIRENTRY;
    final static int CURRENTDIR_ENTRADASMAX = TAM_CLUSTER / TAM_DIRENTRY;

    //otros
    static Scanner read = new Scanner(System.in);
    static String archiv_bin = "./proyect.bin";
    static String current_dir = "mi_shell/";
    static long pos_current_dir = 0;
    static byte[] contenido_current_dir = new byte[TAM_CLUSTER];
    final static short endOfFile = (short) 65535;

    final static byte DIR = (byte) 16;
    final static byte ARCHIV = (byte) 32;
    final static byte SOLO_LECTURA = (byte) 1;
    final static byte ESCONDIDO = (byte) 2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        boolean shell = true;
        while (shell) {
            System.out.print(current_dir + " : ");
            String entrada = read.nextLine();
            //Otra carpeta 
            String[] comando = entrada.split(" ");

        }

    }
}

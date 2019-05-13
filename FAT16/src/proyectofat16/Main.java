package proyectofat16;

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

public class Main {

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
            entrada = OPFAT16.quitar_espacios(entrada).toLowerCase();
            String[] comando = entrada.split(" ");
            if (comando.length == 1) {
                //switch cuando solo viene el comando sin mas papadas
                switch (comando[0]) {
                    case "pwd": {
                        System.out.println(current_dir);
                        break;
                    }
                    case "q": {
                        shell = false;
                        break;
                    }
                    case "pfat": {
                        for (int i = 0; i < TAM_FAT / TAM_FATENTRY; i++) {
                            byte[] read_fat_registry = OPFAT16.leer(i * TAM_FATENTRY + TAM_RAIZ, TAM_FATENTRY);
                            System.out.println("INDICE DE FAT: " + i * TAM_FATENTRY + "\tPUNTERO: " + OPFAT16.bytes_a_short(read_fat_registry));
                        }
                        break;
                    }
                    default: {
                        System.out.println("Comando no encontrado");
                        break;
                    }
                }
            } else {
                // switch de comandos de shell mkdir, cd, ls -l, rmdir, cat, rm, chmod
                switch (comando[0]) {
                    case "mkdir": {
                        MKDIR.mkdir(comando[1]);
                        break;
                    }
                    case "cd": {
                        //CD.cd(comando[1]);
                        break;
                    }
                    case "ls": {
                        if ("-l".equals(comando[1])) {
                            //LS.ls(true);
                        } else {
                            System.out.println("comando not found");
                        }
                        break;
                    }
                    case "rmdir": {
                        //RMDIR.rmdir(comando[1]);
                        break;
                    }
                    case "cat": {
                        //CAT.cat(comando);
                        break;
                    }
                    case "rm": {
                        //RM.rm(comando[1]);
                        break;
                    }
                    case "chmod": {
                        //CHMOD.chmod(comando);
                        break;
                    }
                }
            }
        }
    }
}

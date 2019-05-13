package proyectofat16;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import static proyectofat16.Main.archiv_bin;
import static proyectofat16.Main.TAM_DIRENTRY;
import static proyectofat16.Main.TAM_FAT;
import static proyectofat16.Main.TAM_FATENTRY;
import static proyectofat16.Main.TAM_RAIZ;
import static proyectofat16.Main.DIR;
import static proyectofat16.Main.ARCHIV;

/**
 *
 * @author Crys
 */
public class OPFAT16 {
    public static byte[] leer(long posicion, int num_bytes) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[num_bytes];
        RandomAccessFile leer_file = new RandomAccessFile(new File(archiv_bin), "r");
        leer_file.seek(posicion);
        leer_file.read(buffer);
        leer_file.close();
        return buffer;
    }
    
    public static void escribir(long posicion, byte[] data) throws FileNotFoundException, IOException {
        RandomAccessFile escribir_file = new RandomAccessFile(new File(archiv_bin), "rw");
        escribir_file.seek(posicion);
        escribir_file.write(data);
        escribir_file.close();
    }
    
    public static byte[] crear_metadata(String nombre_file, byte atrib, int tam_file) throws IOException {
        byte[] metadata = new byte[TAM_DIRENTRY];
        metadata[0] = (byte) nombre_file.charAt(0);
        for (int i = 1; i <= 10; i++) {
            if ((i - 1) < nombre_file.length()) {
                metadata[i] = (byte) (nombre_file.toCharArray())[i - 1];
            } else {
                metadata[i] = (byte) 0;
            }
        }

        if (atrib == ARCHIV) {
            metadata[11] = (byte) 32;
        } else if(atrib == DIR){
            metadata[11] = (byte) 16;
        }
        
        long fecha = new java.util.Date().getTime();
        byte[] byteArray_temp = ByteBuffer.allocate(8).putLong(fecha).array();
        for (int i = 0; i < byteArray_temp.length; i++) {
            metadata[i + 12] = byteArray_temp[i];
        }

        //necesario para encontrar el primer cluster
        short primer_cluster = encontrar_espacio_libre_fat();
        byte[] posicion_cluster = ByteBuffer.allocate(2).putShort(primer_cluster).array();
        for (int i = 0; i < posicion_cluster.length; i++) {
            metadata[i + 20] = posicion_cluster[i];
        }

        byte[] tam_archivo = ByteBuffer.allocate(4).putInt(tam_file).array();
        for (int i = 0; i < tam_archivo.length; i++) {
            metadata[i + 22] = tam_archivo[i];
        }

        for (int i = 26; i <= 31; i++) {
            metadata[i] = (byte) 0;
        }
        return metadata;

    }
    
    public static byte[] crear_metadata(String nombre_file, byte atrib, int tam_file, Long fecha, short primer_cluster) throws IOException {
        byte[] metadata = new byte[TAM_DIRENTRY];
        metadata[0] = (byte) nombre_file.charAt(0);
        for (int i = 1; i <= 10; i++) {
            if ((i - 1) < nombre_file.length()) {
                metadata[i] = (byte) (nombre_file.toCharArray())[i - 1];
            } else {
                metadata[i] = (byte) 0;
            }
        }
        metadata[11] = atrib;
        
        long date =fecha;
        byte[] byteArray_temp = ByteBuffer.allocate(8).putLong(date).array();
        for (int i = 0; i < byteArray_temp.length; i++) {
            metadata[i + 12] = byteArray_temp[i];
        }

        //necesario para encontrar primero el cluster
        byte[] posicion_cluster = ByteBuffer.allocate(2).putShort(primer_cluster).array();
        for (int i = 0; i < posicion_cluster.length; i++) {
            metadata[i + 20] = posicion_cluster[i];
        }

        byte[] tam_archivo = ByteBuffer.allocate(4).putInt(tam_file).array();
        for (int i = 0; i < tam_archivo.length; i++) {
            metadata[i + 22] = tam_archivo[i];
        }

        for (int i = 26; i <= 31; i++) {
            metadata[i] = (byte) 0;
        }
        return metadata;

    }
    
    public static short encontrar_espacio_libre_fat() throws IOException {
        short indice = -1;
        for (short i = 0; i < TAM_FAT; i+=2) {
            byte[] buffer = leer(i + TAM_RAIZ, TAM_FATENTRY);           
            if (byte_array_vacio(buffer)) {
                indice = i;
                break;
            }
        }
        return indice;
    }
    
    public static byte[] chequear_si_hay_espacio_disponible_en_fat(int cantidad_espacio) throws IOException {//always find 0
        int espacios_encontrados = 0;
        byte[] espacios_libres_encontrados = new byte[cantidad_espacio*TAM_FATENTRY];
         for (int i = 0; i < TAM_FAT; i++) {
            byte[] buffer = leer(i*TAM_FATENTRY + TAM_RAIZ, TAM_FATENTRY);
            if (byte_array_vacio(buffer)) {
                for (int j = 0; j < TAM_FATENTRY; j++) {             
                    espacios_libres_encontrados[espacios_encontrados*TAM_FATENTRY+j] = ByteBuffer.allocate(2).putShort((short)(i*TAM_FATENTRY)).array()[j];                   
                }
                espacios_encontrados++;
                if(espacios_encontrados == cantidad_espacio)
                    return espacios_libres_encontrados;
            }
        }
        return null;
    }
    
    public static boolean byte_array_vacio(byte[] array){
        int tmp_bytes_vacios = 0;
        for (int j = 0; j < array.length; j++) {
            if (array[j] == (byte) 0) {
                tmp_bytes_vacios++;
            }
        }
        if(tmp_bytes_vacios == array.length)
            return true;
        return false;
    }
    
    public static String quitar_espacios(String entrada) {
        boolean hay_espacio = false;
        String entrada_tmp = "";
        for (int i = 0; i < entrada.length(); i++) {
            if (entrada.charAt(i) != ' ') {
                hay_espacio = true;
            }
            if (hay_espacio) {
                entrada_tmp += entrada.charAt(i);
            }
        }
        return entrada_tmp;
    }

    public static short bytes_a_short(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(bytes);
        buffer.flip();//necesario para el flip 
        return buffer.getShort();
    }

    public static byte[]entero_a_bytes(int numero){
        return ByteBuffer.allocate(4).putInt(numero).array();
    }
    

}

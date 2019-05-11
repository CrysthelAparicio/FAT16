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
    
}

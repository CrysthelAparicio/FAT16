/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofat16;

import java.io.IOException;
import java.nio.ByteBuffer;
import static proyectofat16.Main.CURRENTDIR_ENTRADASMAX;
import static proyectofat16.Main.current_dir;
import static proyectofat16.Main.ENTRADASMAX_RAIZ;
import static proyectofat16.Main.pos_current_dir;
import static proyectofat16.Main.TAM_DIRENTRY;
import static proyectofat16.Main.TAM_CLUSTER;
import static proyectofat16.Main.TAM_FAT;
import static proyectofat16.Main.TAM_FATENTRY;
import static proyectofat16.Main.TAM_RAIZ;
import static proyectofat16.Main.DIR;
import static proyectofat16.Main.contenido_current_dir;
import static proyectofat16.Main.endOfFile;

/**
 *
 * @author Crys
 */
public class MKDIR {

    public static boolean mkdir(String nombre_dir) throws IOException {
        int entradas_max = CURRENTDIR_ENTRADASMAX;

        if ("mi_shell/".equals(current_dir)) {
            entradas_max = ENTRADASMAX_RAIZ;
        }

        for (long i = 0; i < entradas_max; i++) {
            byte[] leer_entrada_dir = OPFAT16.leer(i * TAM_DIRENTRY + pos_current_dir, TAM_DIRENTRY);
            if (leer_entrada_dir[0] == (byte) 0) {
                byte[] metadata = OPFAT16.crear_metadata(nombre_dir, DIR, 0);
                data meta_data = new data(metadata);
                byte[] indice_fat = ByteBuffer.allocate(2).putShort(endOfFile).array();
                byte[] limpiar_cluster_dir = new byte[TAM_CLUSTER];
                OPFAT16.escribir(i * TAM_DIRENTRY + pos_current_dir, metadata);// se escribe la metadata
                OPFAT16.escribir(meta_data.getDir_cluster() + TAM_RAIZ, indice_fat);// se escribe el eof en la posicion de la fat
                OPFAT16.escribir(meta_data.getDir_cluster() + TAM_RAIZ + TAM_FAT, indice_fat);// se escribe el eof en la posicion de la fat copia
                OPFAT16.escribir(meta_data.getDir_cluster() * TAM_CLUSTER / TAM_FATENTRY + TAM_RAIZ + TAM_FAT * 2, limpiar_cluster_dir);
                //nota: se debe borrar el cluster que le fue asignado ya que si un archivo o directorio poseyo esa direccion
                //previamente, la data sigue alli, la cual podria ser interpretada como directorios o archivos que "no existen"
                //debido a que cuando hacemos rm de un archivo solo se borran los punteros de este en la fat y su metadata.
                contenido_current_dir = OPFAT16.leer(pos_current_dir, entradas_max * TAM_DIRENTRY);
                return true;
            }
        }
        return false;
    }
}

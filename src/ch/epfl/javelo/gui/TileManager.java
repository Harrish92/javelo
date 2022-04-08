package ch.epfl.javelo.gui;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public final class TileManager {

    /**
     *
     * @param path le chemin d'accès au répertoire contenant le cache disque, de type Path
     * @param serverName le nom du serveur de tuile (p.ex. tile.openstreetmap.org)
     */
    public TileManager(Path path, String serverName){


    }

    private record TileId(double zoom, double x, double y){

        /**
         *
         * @return vrai ssi les arguments zoom, x et y constituent une identité de tuile valide.
         */
        public static boolean isValid(){
            return true;
        }
    }
}

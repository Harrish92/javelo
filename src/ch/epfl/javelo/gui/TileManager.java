package ch.epfl.javelo.gui;

import ch.epfl.javelo.Preconditions;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedHashMap;

/**
 *
 * TileManager représente un gestionnaire de tuiles OSM
 *
 * @author Harrishan Raveendran (345291)
 *
 */

public final class TileManager {
    private final LinkedHashMap<TileId, Image> lhm =
            new LinkedHashMap<>(0, 0.75f, true);
    private final Path path;
    private final String serverName;

    /**
     *
     * @param path le chemin d'accès au répertoire contenant le cache disque, de type Path
     * @param serverName le nom du serveur de tuile (p.ex. tile.openstreetmap.org)
     *
     * constructeur de TileManager
     */
    public TileManager(Path path, String serverName){
        this.path = path;
        this.serverName = serverName;


    }

    /**
     * @param zoom niveau de zoom de la tuile
     * @param x l'index x de la tuile
     * @param y l'index y de la tuile
     *
     * enregistement imbriqué qui représente l'identité d'une tuile
     */
    public record TileId(int zoom, int x, int y){

        /**
         * constructeur qui vérifie si les paramètres données sont valides
         */
        public TileId{
            Preconditions.checkArgument(isValid(zoom, x, y));
        }
        /**
         *
         * @return vrai ssi les arguments zoom, x et y constituent une identité de tuile valide.
         */
        public static boolean isValid(int zoom, double x, double y){

            double max = Math.pow(2, zoom) - 1;
            return (zoom >= 0 && zoom <= 19 &&  x >= 0 && x <= max && y >= 0 && y <= max);
        }
    }

    /**
     *
     * @param tileid identité d'une tuile
     * @return l'image de l'identité d'une tuile
     * @throws IOException exception lancée s'il y'a une erreur d'entrée/sortie
     */
    public Image imageForTileAt(TileId tileid) throws IOException {
        if(lhm.containsKey(tileid)){
            return lhm.get(tileid);
        }

         Path filePath = Path.of(path.toString() + "/" + tileid.zoom + "/"+ tileid.x + "/" + tileid.y + ".png");
        if(Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)){
            try(InputStream input = new FileInputStream(filePath.toString())){
                Image img = new Image(input);
                if(lhm.size() >= 100){
                    lhm.remove(lhm.entrySet().iterator().next().getKey());
                }
                lhm.put(tileid, img);
                return imageForTileAt(tileid);
            }
        }

        Files.createDirectories(Path.of(path + "/" + tileid.zoom + "/"+ tileid.x));

        URL u = new URL(serverName + "/" + tileid.zoom + "/" + tileid.x + "/" + tileid.y + ".png");
        URLConnection c = u.openConnection();
        c.setRequestProperty("User-Agent", "JaVelo");
        try(InputStream i = c.getInputStream()) {
            FileOutputStream output = new FileOutputStream(filePath.toString());
            i.transferTo(output);
            return imageForTileAt(tileid);
        }
    }
}

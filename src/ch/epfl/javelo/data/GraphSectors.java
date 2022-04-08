package ch.epfl.javelo.data;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import ch.epfl.javelo.projection.SwissBounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Yoan Giovannini (303934)
 *
 * Représente la totalité des secteurs.
 *
 * @param buffer la mémoire tampon contenant la valeur
 *              des attributs de la totalité des secteurs.
 */
public record GraphSectors(ByteBuffer buffer) {
    //Longueur d'un secteur en octets.
    private static final int SECTOR_LENGTH = Integer.BYTES + Short.BYTES;
    //Décalage pour obtenir le nombre de noeuds d'un secteur.
    private static final int OFFSET_NBN = Integer.BYTES;

    /**
     * Représente un secteur
     * @param startNodeId index du premier noeud du secteur.
     * @param endNodeId index du noeud juste après le dernier du secteur.
     */
    public record Sector(int startNodeId, int endNodeId) {}


    /**
     * Retourne la liste de tous les secteurs ayant une intersection
     * avec le carré centré au point donné.
     * @param center centre du carré.
     * @param distance demi-côté du carré.
     * @return une liste de secteurs.
     */
    public List<Sector> sectorsInArea(PointCh center, double distance) {
        Preconditions.checkArgument(distance >= 0);
        ArrayList<Sector> listeSecteurs = new ArrayList<>();
        double deltaSE = SwissBounds.WIDTH/128;
        double deltaSN = SwissBounds.HEIGHT/128;
        int secteur0X = (int) Math.floor((center.e() - distance - SwissBounds.MIN_E)/deltaSE);
        int secteur0Y = (int) Math.floor((center.n() - distance - SwissBounds.MIN_N)/deltaSN);
        int secteur1X = (int) Math.floor((center.e() + distance - SwissBounds.MIN_E)/deltaSE);
        int secteur1Y = (int) Math.floor((center.n() + distance - SwissBounds.MIN_N)/deltaSN);
        secteur0X = Math2.clamp(0,secteur0X,127);
        secteur0Y = Math2.clamp(0, secteur0Y, 127);
        secteur1X = Math2.clamp(0, secteur1X, 127);
        secteur1Y = Math2.clamp(0, secteur1Y, 127);
        for(int y = secteur0Y; y <= secteur1Y; y++){
            for(int x = secteur0X; x <= secteur1X; x++){
                int secteur = y * 128 + x;
                int indexN = buffer.getInt(secteur * SECTOR_LENGTH);
                int nbN = Short.toUnsignedInt(buffer.getShort(secteur * SECTOR_LENGTH + OFFSET_NBN));
                listeSecteurs.add(new Sector(indexN, indexN + nbN));
            }
        }
        return listeSecteurs;
    }

}

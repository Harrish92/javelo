package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import ch.epfl.javelo.projection.SwissBounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * Représente la totalité des secteurs.
 * @param buffer la mémoire tampon contenant la valeur
 *              des attributs de la totalité des secteurs.
 * @author Yoan Giovannini (303934)
 */
public record GraphSectors(ByteBuffer buffer) {


    /**
     * Représente un secteur
     * @param startNodeId index du premier noeud du secteur.
     * @param endNodeId index du noeud juste après le dernier du secteur.
     */
    private record Sector(int startNodeId, int endNodeId) {


    }


    /**
     * Retourne la liste de tous les secteurs ayant une intersection
     * avec le carré centré au point donné.
     * @param center centre du carré.
     * @param distance demi-côté du carré.
     * @return une liste de secteurs.
     */
    public List<Sector> sectorsInArea(PointCh center, double distance) {
        ArrayList<Sector> listeSecteurs = new ArrayList<>();
        double deltaSE = SwissBounds.WIDTH/128;
        double deltaSN = SwissBounds.HEIGHT/128;
        int secteur0X = (int) Math.floor((center.e() - distance - SwissBounds.MIN_E)/deltaSE);
        int secteur0Y = (int) Math.floor((center.n() - distance - SwissBounds.MIN_N)/deltaSN);
        int secteur1X = (int) Math.floor((center.e() + distance - SwissBounds.MIN_E)/deltaSE);
        int secteur1Y = (int) Math.floor((center.n() + distance - SwissBounds.MIN_N)/deltaSN);
        for(int y = secteur0Y; y < secteur1Y; y++){
            for(int x = secteur0X; x < secteur1X; x++){
                int secteur = y * 128 + x;
                short indexN = buffer.getShort(secteur);
                int nbN = buffer.getInt(secteur);

                //listeSecteurs.add(new Sector(indexN, ));
            }
        }
        return listeSecteurs;
    }

}

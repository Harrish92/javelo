package ch.epfl.javelo.data;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {
    private static final int OFFSET_EDGESBUFFER_INVERTED = 0;
    private static final int OFFSET_EDGESBUFFER_LENGTH = OFFSET_EDGESBUFFER_INVERTED + 1;
    private static final int OFFSET_EDGESBUFFER_ELEVATIONGAIN = OFFSET_EDGESBUFFER_LENGTH + 1;
    private static final int OFFSET_EDGESBUFFER_ATTRIBUTESID = OFFSET_EDGESBUFFER_ELEVATIONGAIN + 1;
    private static final int EDGESBUFFER_INTS = OFFSET_EDGESBUFFER_ATTRIBUTESID + 1;


    /**
     *
     * @param edgeId arrête identité
     * @return vrai ssi edgeId va dans le sens inverse de la voie OSM.
     */
    public boolean isInverted(int edgeId){
        int nb = edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED);
        return nb < 0;

    }

    /**
     *
     * @param edgeId arrête identité
     * @return l'identité du noeud de destination de edgeId.
     */
    public int targetNodeId(int edgeId){
        return edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED); // pas sûr
    }

    /**
     *
     * @param edgeId arrête identité
     * @return la longueur en mètre de edgeId.
     */
    public double length(int edgeId){
        return edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
    }

    /**
     *
     * @param edgeId arrête identité
     * @return le dénivelé positif en mètre de edgeId.
     */

    public double elevationGain(int edgeId){
        return edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ELEVATIONGAIN);

    }

    /**
     *
     * @param edgeId arrête identité
     * @return vrai ssi edgeId possède un profil.
     */
    public boolean hasProfile(int edgeId){
        return false;
    }

    /**
     *
     * @param edgeId arrête identité
     * @return le tableau des échantillons du profil de edgeId.
     * Retourne un tableau vide si edgeId ne possède pas de profil
     */
    public float[] profileSamples(int edgeId){
        return null;
    }

    /**
     *
     * @param edgeId arrête identité
     * @return l'identité de l'ensemble des attributs attachés à l'arrête portant l'identité edgeId.
     */
    public int attributesIndex(int edgeId){
        return 0;
    }


}


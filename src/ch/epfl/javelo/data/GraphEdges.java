package ch.epfl.javelo.data;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Q28_4;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {
    private static final int OFFSET_EDGESBUFFER_INVERTED = 0;
    private static final int OFFSET_EDGESBUFFER_LENGTH = OFFSET_EDGESBUFFER_INVERTED + 1;
    private static final int OFFSET_EDGESBUFFER_ELEVATIONGAIN = OFFSET_EDGESBUFFER_LENGTH + 1;
    private static final int OFFSET_EDGESBUFFER_ATTRIBUTESID = OFFSET_EDGESBUFFER_ELEVATIONGAIN + 1;
    private static final int EDGESBUFFER_INTS = OFFSET_EDGESBUFFER_ATTRIBUTESID + 1;

    private static final int OFFSET_PROFILESIDS_TYPE = 0;
    private static final int PROFILEIDS_INTS = OFFSET_PROFILESIDS_TYPE + 1;


    /**
     *
     * @param edgeId arrête identité
     * @return vrai ssi edgeId va dans le sens inverse de la voie OSM.
     */
    public boolean isInverted(int edgeId){
        int nb = edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED);
        return (nb < 0);

    }

    /**
     *
     * @param edgeId arrête identité
     * @return l'identité du noeud de destination de edgeId.
     */
    public int targetNodeId(int edgeId){
        int nb = edgesBuffer.get(edgeId);
        if(nb >= 0){
            return nb;
        }else{
            return ~nb;
        }
    }

    /**
     *
     * @param edgeId arrête identité
     * @return la longueur en mètre de edgeId.
     */
    public double length(int edgeId){
        int nb = edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        return (nb >>> 4);
    }

    /**
     *
     * @param edgeId arrête identité
     * @return le dénivelé positif en mètre de edgeId.
     */

    public double elevationGain(int edgeId){
        int nb = edgesBuffer.get(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ELEVATIONGAIN);
        return (nb >>> 4);

    }

    /**
     *
     * @param edgeId arrête identité
     * @return vrai ssi edgeId possède un profil.
     */
    public boolean hasProfile(int edgeId){
        int profile = profileIds.get(edgeId*PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE);
        int profile_type = profile >>> 30;
        return (profile_type != 0);
    }

    /**
     *
     * @param edgeId arrête identité
     * @return le tableau des échantillons du profil de edgeId.
     * Retourne un tableau vide si edgeId ne possède pas de profil
     */
    public float[] profileSamples(int edgeId) {
        int l = (int) length(edgeId); //cast int nécessaire?
        int q28_4of2 = Q28_4.ofInt(2);
        int pts = 1 + Math2.ceilDiv(l, q28_4of2);
        float[] tab = new float[pts];
        if (!hasProfile(edgeId))
            return tab;
        int profile = profileIds.get(edgeId * PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE);
        int profile_type = profile >>> 30;
        switch (profile_type) {
            case 1:
                break;
            case 2:
                break;

            case 3:
                break;

            case 4:
                break;
        }
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


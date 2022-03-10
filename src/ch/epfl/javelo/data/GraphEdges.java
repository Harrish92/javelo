package ch.epfl.javelo.data;

import ch.epfl.javelo.Bits;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Q28_4;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {
    private static final int OFFSET_EDGESBUFFER_INVERTED = 0;
    private static final int OFFSET_EDGESBUFFER_LENGTH = OFFSET_EDGESBUFFER_INVERTED + 4;
    private static final int OFFSET_EDGESBUFFER_ELEVATIONGAIN = OFFSET_EDGESBUFFER_LENGTH + 2;
    private static final int OFFSET_EDGESBUFFER_ATTRIBUTESID = OFFSET_EDGESBUFFER_ELEVATIONGAIN + 2;
    private static final int EDGESBUFFER_INTS = OFFSET_EDGESBUFFER_ATTRIBUTESID + 1;

    private static final int OFFSET_PROFILESIDS_TYPE = 0;
    private static final int PROFILEIDS_INTS = OFFSET_PROFILESIDS_TYPE + 1;


    /**
     *
     * @param edgeId arrête identité
     * @return vrai ssi edgeId va dans le sens inverse de la voie OSM.
     */
    public boolean isInverted(int edgeId){
        int nb = edgesBuffer.getInt(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED);
        return (nb < 0);

    }

    /**
     *
     * @param edgeId arrête identité
     * @return l'identité du noeud de destination de edgeId.
     */
    public int targetNodeId(int edgeId){
        int nb = edgesBuffer.getInt(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED);
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
        int nb = edgesBuffer.getShort(edgeId*EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        return Q28_4.asDouble(nb);
    }

    /**
     *
     * @param edgeId arrête identité
     * @return le dénivelé positif en mètre de edgeId.
     */

    public double elevationGain(int edgeId){
        int nb = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ELEVATIONGAIN);
        return Q28_4.asDouble(nb);

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
        int l = edgesBuffer.getShort(edgeId*EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        int q28_4of2 = Q28_4.ofInt(2);
        int pts = 1 + Math2.ceilDiv(l, q28_4of2);
        float[] tab = new float[pts];

        if (!hasProfile(edgeId))
            return tab;
        int profile = profileIds.get(edgeId * PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE);
        int profile_type = profile >>> 30;
        switch (profile_type) {
            case 1:
                return profileType1(pts, tab, edgeId);
            case 2:
                return profileType2(pts, tab, edgeId);

            case 3:
                return profileType3(pts, tab, edgeId);

        }
        return null;
    }


    private float [] profileType1(int pts, float[] tab, int edgeId){
        int c = 0;
        int counting = pts;
        for(int i = 1; i <= counting; i++){
            int nb = elevations.get(i);

            if(isInverted(edgeId)){
                tab[pts-1] = nb;
                --pts;
            }else{
                tab[c]  = nb;
                ++c;
            }
        }

        return tab;

    }

    private float[] profileType2(int pts, float[] tab, int edgeId){
        int nb1 = elevations.get(1);
        float f1 = Q28_4.asFloat(nb1);
        double e = (double) (pts-1) / (double) 2;
        int c = 0;
        int numberOfElements = (int) Math.ceil(e) + 2;
        if(isInverted(edgeId)){
            tab[pts-1] = f1;
            --pts;
        }else{
            tab[c]  = f1;
            ++c;
        }
        System.out.println(numberOfElements);
        for(int i = 2; i < numberOfElements; i++){
            int nb2 = elevations.get(i);
            for(int j = 1; j >= 0; j--){
                int extract2 = Bits.extractSigned(nb2, j*8, 8);
                float f2 = Q28_4.asFloat(extract2);
                f1 += f2;
                if(f2 != 0){
                    System.out.println(f1);

                    if(isInverted(edgeId)){
                        tab[pts-1] = f1;
                        --pts;
                    }else{
                        tab[c]  = f2;
                        ++c;
                    }

                }



            }
        }
        return tab;

    }

    private float[] profileType3(int pts,float [] tab, int edgeId){
        int nb1 = elevations.get(1);
        float f1 = Q28_4.asFloat(nb1);
        double e = (double) (pts-1) / (double) 4;
        int c = 0;
        int numberOfElements = (int) Math.ceil(e) + 2;
        if(isInverted(edgeId)){
            tab[pts-1] = f1;
            --pts;
        }else{
            tab[c]  = f1;
            ++c;
        }
        for(int i = 2; i < numberOfElements; i++){
            int nb2 = elevations.get(i);
            for(int j = 3; j >= 0; j--){
                int extract2 = Bits.extractSigned(nb2, j*4, 4);
                float f2 = Q28_4.asFloat(extract2);
                f1 += f2;
                if(f2 != 0){
                    if(isInverted(edgeId)){
                        tab[pts-1] = f1;
                        --pts;
                    }else{
                        tab[c]  = f2;
                        ++c;
                    }

                }



            }
        }
        return tab;
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


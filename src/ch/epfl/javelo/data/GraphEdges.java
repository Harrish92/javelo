package ch.epfl.javelo.data;

import ch.epfl.javelo.Bits;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Q28_4;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author Harrishan Raveendran (345291)
 */
public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {
    private static final int OFFSET_EDGESBUFFER_INVERTED = 0;
    private static final int OFFSET_EDGESBUFFER_LENGTH = OFFSET_EDGESBUFFER_INVERTED + 4;
    private static final int OFFSET_EDGESBUFFER_ELEVATIONGAIN = OFFSET_EDGESBUFFER_LENGTH + 2;
    private static final int OFFSET_EDGESBUFFER_ATTRIBUTESID = OFFSET_EDGESBUFFER_ELEVATIONGAIN + 2;
    private static final int EDGESBUFFER_INTS = OFFSET_EDGESBUFFER_ATTRIBUTESID + 2;

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
        short nb = edgesBuffer.getShort(edgeId*EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        int mon_integer = Short.toUnsignedInt(nb);
        return Q28_4.asDouble(mon_integer);
    }

    /**
     *
     * @param edgeId arrête identité
     * @return le dénivelé positif en mètre de edgeId.
     */

    public double elevationGain(int edgeId){
        short nb = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ELEVATIONGAIN);
        int mon_integer = Short.toUnsignedInt(nb);
        return Q28_4.asDouble(mon_integer);

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
        int l = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        int q28_4of2 = Q28_4.ofInt(2);
        int pts = 1 + Math2.ceilDiv(l, q28_4of2);
        float[] tab = new float[pts];

        if (!hasProfile(edgeId)){
            float[] aux = new float[0];
            return aux;
        }
        int profile = profileIds.get(edgeId * PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE);
        int profile_type = profile >>> 30;
        int index_element = Bits.extractUnsigned(profile, 0, 30);
        switch (profile_type) {
            case 1:
                return profileType1(pts, tab, edgeId, index_element);
            case 2:
                return profileType2(pts, tab, edgeId, index_element);

            case 3:
                return profileType3(pts, tab, edgeId, index_element);

        }
        return null;
    }

    /**
     *
     * @param pts nombre d'échantillons
     * @param tab tableau de type float
     * @param edgeId arrête identité
     * @return le tableau avec les échantillons du profil de type 1 pour ProfileSamples.
     */
    private float [] profileType1(int pts, float[] tab, int edgeId, int firstSampleIndex){
        for(int i = firstSampleIndex; i < firstSampleIndex + pts; i++){
            short nb = elevations.get(i);
            float echantillon = Q28_4.asFloat(Short.toUnsignedInt(nb));
            System.out.println(echantillon);
            if(isInverted(edgeId)){
                tab[pts-(i - firstSampleIndex + 1)] = echantillon;
            }else{
                tab[i - firstSampleIndex]  = echantillon;
            }
        }

        return tab;

    }

    /**
     *
     * @param pts nombre d'échantillons
     * @param tab tableau de type float
     * @param edgeId arrête identité
     * @param index_element identité du premier échantillon du profil
     * @return le tableau avec les échantillons du profil de type 2 pour ProfileSamples.
     */
    private float[] profileType2(int pts, float[] tab, int edgeId, int index_element){
        int nb1 = elevations.get(index_element);
        float f1 = Q28_4.asFloat(nb1);
        int counting = 0;
        if(isInverted(edgeId)){
            tab[pts-1] = f1;
        }else{
            tab[counting]  = f1;
        }
        ++ counting;
        for(int i = index_element+1; i < pts + index_element; i += 2){
            int nb2 = elevations.get(i);
            for(int j = 1; j >= 0; j--){
                int extract2 = Bits.extractSigned(nb2, j*8, 8);
                float f2 = Q28_4.asFloat(extract2);
                f1 += f2;
                if(f2 != 0){

                    if(isInverted(edgeId)){
                        tab[pts-(counting + 1)] = f1;
                    }else{
                        tab[counting]  = f1;
                    }
                    ++counting;
                }



            }
        }
        for(int i = 0; i < tab.length; i++){
            System.out.println(tab[i]);
        }
        return tab;



/*
        int nb1 = elevations.get(index_element);
        float f1 = Q28_4.asFloat(nb1);
        if(isInverted(edgeId)){
            tab[pts-1] = f1;
        }else{
            tab[0]  = f1;

        }
        for(int i = index_element+1; i < pts + index_element; i++){

            int nb2 = elevations.get(i);
            for(int j = 1; j >= 0; j--){
                int extract2 = Bits.extractSigned(nb2, j*8, 8);
                float f2 = Q28_4.asFloat(extract2);
                f1 += f2;
                if(f2 != 0){
                    if(isInverted(edgeId)){
                        tab[pts - (i - index_element + 1 - j)] = f1;

                    }else{
                        tab[i - (index_element) + 1 - j]  = f1;

                        System.out.print(f1);
                        System.out.print(" ");
                        System.out.println(i - (index_element) + 1 - j);



                    }

                }



            }
        }
        return tab;

 */

    }

    /**
     *
     * @param pts nombre d'échantillons
     * @param tab tableau de type float
     * @param edgeId arrête identité
     * @param index_element identité du premier échantillon du profil
     * @return le tableau avec les échantillons du profil de type 3 pour ProfileSamples.
     */
    private float[] profileType3(int pts,float [] tab, int edgeId, int index_element){
        int nb1 = elevations.get(index_element);
        float f1 = Q28_4.asFloat(nb1);
        double e = (double) (pts-1) / (double) 4;
        int c = 0;
        int numberOfElements = (int) Math.ceil(e) + index_element;
        if(isInverted(edgeId)){
            tab[pts-1] = f1;
            --pts;
        }else{
            tab[c]  = f1;
            ++c;
        }
        for(int i = index_element+1; i <= numberOfElements; i++){
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
                        tab[c]  = f1;
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
        short nb = edgesBuffer.getShort(edgeId*EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ATTRIBUTESID);
        return Short.toUnsignedInt(nb);
    }


}


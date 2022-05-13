package ch.epfl.javelo.data;

import ch.epfl.javelo.Bits;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Q28_4;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * GraphEdges représente le tableau de toutes les arêtes du graphe
 *
 * @param edgesBuffer mémoire tampon contenant les attributs des edges.
 * @param profileIds  mémoire tampon contenant les attributs des profils.
 * @param elevations  mémoire tampon contenant tous les échantillons des profils,
 *                    qu'ils soient compressés ou non.
 * @author Harrishan Raveendran (345291)
 */
public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {

    // décalages qui permet de retrouver l'attribut souhaité dans le buffer
    private static final int OFFSET_EDGESBUFFER_INVERTED = 0;
    private static final int OFFSET_EDGESBUFFER_LENGTH = OFFSET_EDGESBUFFER_INVERTED + 4;
    private static final int OFFSET_EDGESBUFFER_ELEVATIONGAIN = OFFSET_EDGESBUFFER_LENGTH + 2;
    private static final int OFFSET_EDGESBUFFER_ATTRIBUTESID = OFFSET_EDGESBUFFER_ELEVATIONGAIN + 2;
    private static final int EDGESBUFFER_INTS = OFFSET_EDGESBUFFER_ATTRIBUTESID + 2;

    private static final int OFFSET_PROFILESIDS_TYPE = 0;
    private static final int PROFILEIDS_INTS = OFFSET_PROFILESIDS_TYPE + 1;


    /**
     * @param edgeId arrête identité
     * @return vrai ssi edgeId va dans le sens inverse de la voie OSM.
     */
    public boolean isInverted(int edgeId) {
        int nb = edgesBuffer.getInt(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED);
        return (nb < 0);

    }

    /**
     * @param edgeId arrête identité
     * @return l'identité du noeud de destination de edgeId.
     */
    public int targetNodeId(int edgeId) {
        int nb = edgesBuffer.getInt(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_INVERTED);
        if (nb >= 0) {
            return nb;
        }
        return ~nb;
    }

    /**
     * @param edgeId arrête identité
     * @return la longueur en mètre de edgeId.
     */
    public double length(int edgeId) {
        short nb = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        int my_integer = Short.toUnsignedInt(nb);
        return Q28_4.asDouble(my_integer);
    }

    /**
     * @param edgeId arrête identité
     * @return le dénivelé positif en mètre de edgeId.
     */

    public double elevationGain(int edgeId) {
        short nb = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ELEVATIONGAIN);
        int mon_integer = Short.toUnsignedInt(nb);
        return Q28_4.asDouble(mon_integer);

    }

    /**
     * @param edgeId arrête identité
     * @return vrai ssi edgeId possède un profil.
     */
    public boolean hasProfile(int edgeId) {
        int profile = profileIds.get(edgeId * PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE);
        int profileType = profile >>> 30;
        return (profileType != 0);
    }

    /**
     * @param edgeId arrête identité
     * @return le tableau des échantillons du profil de edgeId.
     * Retourne un tableau vide si edgeId ne possède pas de profil
     */
    public float[] profileSamples(int edgeId) {

        // si l'arrête n'a pas de profil, un tableau vide est retourné

        if (!hasProfile(edgeId)) {
            return new float[0];
        }

        // partie du code qui détermine le type du profil

        int l = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_LENGTH);
        int q28_4of2 = Q28_4.ofInt(2);
        int pts = 1 + Math2.ceilDiv(l, q28_4of2);
        int profile_type = profileIds.get(edgeId * PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE) >>> 30;
        int index_element = Bits.extractUnsigned(profileIds.get(edgeId * PROFILEIDS_INTS + OFFSET_PROFILESIDS_TYPE),
                0, 30);
        float[] tab = new float[pts];

        // switch qui appelle une méthode selon le type du profil

        return switch (profile_type) {
            case 2 -> profileType(pts, tab, edgeId, index_element, 2); // profil de type 2
            case 3 -> profileType(pts, tab, edgeId, index_element, 3); // profil de type 3
            default -> profileType1(pts, tab, edgeId, index_element); // profil de type 1

        };

    }

    /**
     * @param pts nombre d'échantillons
     * @param tab tableau de type float
     * @param edgeId arrête identité
     * @return le tableau avec les échantillons du profil de type 1 pour ProfileSamples.
     */
    private float[] profileType1(int pts, float[] tab, int edgeId, int firstSampleIndex) {
        for (int i = firstSampleIndex; i < firstSampleIndex + pts; i++) {
            float echantillon = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(i)));

            // vérifie si les échantillons du profil sont toujours ordonnés dans le sens de la voie OSM
            if (isInverted(edgeId)) {
                tab[pts - (i - firstSampleIndex + 1)] = echantillon;
            } else {
                tab[i - firstSampleIndex] = echantillon;
            }
        }

        return tab;

    }

    /**
     * @param pts nombre d'échantillons
     * @param tab tableau de type float
     * @param edgeId arrête identité
     * @param index_element identité du premier échantillon du profil
     * @return le tableau avec les échantillons du profil de type 2 et 3 pour ProfileSamples.
     */
    private float[] profileType(int pts, float[] tab, int edgeId, int index_element, int profileType) {
        float echantillon = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(index_element)));

        // vérifie si les échantillons du profil sont toujours ordonnés dans le sens de la voie OSM

        if (isInverted(edgeId)) {
            tab[pts - 1] = echantillon;
        } else {
            tab[0] = echantillon;
        }
        int incrementation = 0;
        int numberOfBits = 0;
        switch (profileType) {
            case 2 -> {
                incrementation = 2;
                numberOfBits = 8;
            }
            case 3 -> {
                incrementation = 4;
                numberOfBits = 4;
            }
        }

        int counting = 1, offset = 1;
        for (int i = index_element + 1; i < pts + index_element; i += incrementation) {
            for (int j = incrementation-1; j >= 0; j--) {
                echantillon += Q28_4.asFloat(Bits.extractSigned(elevations.get(index_element + offset),
                        j * numberOfBits, numberOfBits));

                if (counting < pts) {

                    if (isInverted(edgeId)) {
                        tab[pts - (counting + 1)] = echantillon;
                    } else {
                        tab[counting] = echantillon;
                    }
                }
                ++counting;
            }
            ++offset;


        }
        return tab;

    }


    /**
     * @param edgeId arrête identité
     * @return l'identité de l'ensemble des attributs attachés à l'arrête portant l'identité edgeId.
     */
    public int attributesIndex(int edgeId) {
        short nb = edgesBuffer.getShort(edgeId * EDGESBUFFER_INTS + OFFSET_EDGESBUFFER_ATTRIBUTESID);
        return Short.toUnsignedInt(nb);
    }


}


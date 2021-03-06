package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Preconditions;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.function.DoubleUnaryOperator;

/**
 * Représente le profil d'une arête.
 *
 * @author Yoan Giovannini (303934)
 */
public final class ElevationProfile {

    private final double length;
    private final float[] elevationSamples;
    private final DoubleUnaryOperator profile;
    private final double minElevation;
    private final double maxElevation;
    private final double totalAscend;
    private final double totalDescend;

    /**
     * Constructeur de la classe.
     *
     * @param length           la longueur du profil.
     * @param elevationSamples les points du profil.
     * @throws IllegalArgumentException si la longueur est négative ou nulle
     * ou qu'il n'y a pas assez d'elevationSamples.
     */
    public ElevationProfile(double length, float[] elevationSamples) {
        Preconditions.checkArgument(length > 0 && elevationSamples.length >= 2);
        this.length = length;
        this.elevationSamples = Arrays.copyOf(elevationSamples, elevationSamples.length);
        profile = Functions.sampled(elevationSamples, length);
        minElevation = statistics().getMin();
        maxElevation = statistics().getMax();
        totalAscend = denivele(true);
        totalDescend = denivele(false);
    }

    /**
     * Calcule la longueur du profil.
     *
     * @return la longueur en mètre.
     */
    public double length() {
        return length;
    }

    /**
     * Calcule l'altitude du point le plus bas du profil.
     *
     * @return une altitude.
     */
    public double minElevation() {
        return minElevation;
    }

    /**
     * Calcule l'altitude du point le plus haut du profil.
     *
     * @return une altitude.
     */
    public double maxElevation() {
        return maxElevation;
    }

    /**
     * Calcule statistiques du profil.
     *
     * @return les statistiques du profil.
     */
    private DoubleSummaryStatistics statistics() {
        DoubleSummaryStatistics s = new DoubleSummaryStatistics();
        for (double point : elevationSamples) {
            s.accept(point);
        }
        return s;
    }

    /**
     * Calcule le dénivelé positif ou négatif du tableau de points elevationSamples.
     *
     * @param ascent détermine si l'on veut le dénivelé positif (true) ou négatif (false).
     * @return le dénivelé en mètres.
     */
    private double denivele(boolean ascent) {
        double sum = 0;
        for (int i = 1; i < elevationSamples.length; i++) {
            double diff = elevationSamples[i] - elevationSamples[i - 1];
            if (diff > 0 && ascent) {
                sum += diff;
            } else if (diff < 0 && !ascent) {
                sum -= diff;
            }
        }
        return sum;
    }

    /**
     * Calcule le dénivelé positif des points elevationSamples.
     *
     * @return le dénivelé positif en mètres.
     */
    public double totalAscent() {return totalAscend;}

    /**
     * Calcule le dénivelé négatif des points elevationSamples.
     *
     * @return le dénivelé négatif en mètres.
     */
    public double totalDescent() {
        return totalDescend;
    }

    /**
     * Calcule l'altitude du profil à la position donnée en paramètre.
     *
     * @param position la position.
     * @return l'altitude en mètres.
     */
    public double elevationAt(double position) {
        return profile.applyAsDouble(position);
    }
}

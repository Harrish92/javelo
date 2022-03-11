package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Preconditions;

import java.util.DoubleSummaryStatistics;
import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author Yoan Giovannini (303934)
 */
public final class ElevationProfile {

    private double length;
    private float[] elevationSamples;
    private DoubleUnaryOperator profile;

    public ElevationProfile(double length, float[] elevationSamples) {
        Preconditions.checkArgument(length > 0 && elevationSamples.length >= 2);
        this.length = length;
        this.elevationSamples = elevationSamples;
        profile = Functions.sampled(elevationSamples, length);
    }

    public double length() {
        return length;
    }

    public double minElevation() {
        return statistics().getMin();
    }

    public double maxElevation() {
        return statistics().getMax();
    }

    private DoubleSummaryStatistics statistics() {
        DoubleSummaryStatistics s = new DoubleSummaryStatistics();
        for(double point : elevationSamples) {
            s.accept(point);
        }
        return s;
    }

    /**
     * Calcule le dénivelé positif ou négatif du tableau de points elevationSamples.
     * @param ascent détermine si l'on veut le dénivelé positif (true) ou négatif (false).
     * @return le dénivelé en mètres.
     */
    private double denivele(boolean ascent){
        double sum = 0;
        for(int i = 1; i < elevationSamples.length; i++) {
            double diff = elevationSamples[i] - elevationSamples[i - 1];
            if(diff > 0 && ascent){
                sum += diff;
            }
            else if (diff < 0 && !ascent){
                sum -= diff;
            }
        }
        return sum;
    }

    /**
     * Calcule le dénivelé positif des points elevationSamples.
     * @return le dénivelé positif en mètres.
     */
    public double totalAscent() {
        return denivele(true);
    }

    /**
     * Calcule le dénivelé négatif des points elevationSamples.
     * @return le dénivelé négatif en mètres.
     */
    public double totalDescent() {
        return denivele(false);
    }

    /**
     * Calcule l'altitude du profil à la position donnée en paramètre.
     * @param position la position.
     * @return l'altitude en mètres.
     */
    public double elevationAt(double position) {
        return profile.applyAsDouble(position);
    }
}

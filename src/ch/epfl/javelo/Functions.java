package ch.epfl.javelo;

import java.util.function.DoubleUnaryOperator;

/** Représente des fonctions
 *
 * @author Yoan Giovannini (303934)
 */
public final class Functions {

    //classe Functions non instantiable
    private Functions(){}

    /**
     * créé une fonction constante
     * @param y la constante de la fonction.
     * @return une fonction constante.
     */
    public static DoubleUnaryOperator constant(double y) {
        return new Constant(y);
    }

    /**
     * créé une fonction d'interpolation linéaire.
     * @param samples les points de la fonction.
     * @param xMax l'abscisse maximale de la fonction.
     * @return une fonction d'interpolation linéaire.
     */
    public static DoubleUnaryOperator sampled(float[] samples, double xMax) {
        return new Sampled(samples, xMax);
    }

    /**
     * Fonction constante
     */
    private static final class Constant implements DoubleUnaryOperator {
        private final double constante;

        /**
         * Constructeur de la fonction constante.
         * @param c la constante de la fonction
         */
        public Constant(double c) {
            constante = c;
        }

        @Override
        public double applyAsDouble(double operand) {
            return constante;
        }
    }

    /**
     * Fonction d'interpolation linéaire
     */
    private static final class Sampled implements DoubleUnaryOperator {
        private float[] samples;
        private double xMax;

        /**
         * Constructeur de la fonction d'interpolation linéaire.
         * @param samples les points de la fonction.
         * @param xMax l'abscisse maximale de la fonction.
         */
        public Sampled(float[] samples, double xMax){
            if(samples.length < 2 || xMax <= 0){
                throw new IllegalArgumentException("pas assez d'échantillons ou maximum <= 0");
            }
            else{
                this.samples = samples;
                this.xMax = xMax;
            }
        }

        @Override
        public double applyAsDouble(double operand) {
            if(operand >= xMax){
                return samples[samples.length - 1];
            }
            if(operand <= 0){
                return samples[0];
            }
            double delta = xMax/(samples.length - 1);
            int i = (int) Math.floor(operand/delta);
            return Math2.interpolate( samples[i], samples[i+1], (operand - i*delta)/delta);
        }
    }
}

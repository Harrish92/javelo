package ch.epfl.javelo;

/**
 *
 *
 * @author Harrishan Raveendran (345291)
 */
public final class Math2{

    private Math2(){

    }

    /**
     * @param x un entier
     * @param y un entier
     * @return retourne un entier qui est la valeur supérieure de la division entre x et y.
     */
    public static int ceilDiv(int x, int y){
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y > 0);
        return (x+y-1)/y;
    }

    /**
     *
     * @param y0 valeur de y en x0
     * @param y1 valeur de y en x
     * @param x nombre à virgule
     * @return retourne une interpolation sous la forme de y = a*x+y0
     */
    public static double interpolate(double y0, double y1, double x){
        double a;
        if(x != 0){
            a = (y1 - y0);
        }else{
            a = 0;
        }

        return Math.fma(a, x, y0);

    }

    /**
     *
     * @param min minimum de l'intervalle
     * @param v un entier
     * @param max maximum de l'intervalle
     * @return retourne la valeur de max, si v est supérieur au bord supérieur de l'intervalle.
     *         retourn la valeur de min si v est inférieur au bord inférieur de l'intervalle.
     *         Sinon retourne la valeur de v.
     */
    public static int clamp(int min, int v, int max){
        Preconditions.checkArgument(max > min);
        if(v < min){
            return min;
        }else{
            if(v > max){
                return max;
            }else{
                return v;
            }
        }

    }

    /**
     * méthode similaire à celui au-dessus, mais les paramètres et le type de retour sont des nombre à virgule.
     */
    public static double clamp(double  min, double v, double max){
        Preconditions.checkArgument(max > min);
        if(v < min){
            return min;
        }else{
            if(v > max){
                return max;
            }else{
                return v;
            }
        }

    }

    /**
     *
     * @param x un nombre à virgule
     * @return retourne y = asinh(x) en fonction de la valeur de x.
     */
    public static double asinh(double x){
        return Math.log(x+Math.sqrt(1+Math.pow(x,2)));
    }

    /**
     *
     * @param uX coordonnée X du point u
     * @param uY coordonnée Y du point u
     * @param vX coordonnée X du point v
     * @param vY coordonnée Y du point v
     * @return retourne le produit scalaire entre deux vecteurs.
     */
    public static double dotProduct(double uX, double uY, double vX, double vY){
        return Math.fma(uX, vX, uY*vY);
    }

    /**
     *
     * @param uX coordonnée X du point u
     * @param uY coordonnée Y du point u
     * @return retourne le carré de la norme d'un vecteur.
     */
    public static double squaredNorm(double uX, double uY){
        return Math.pow(uX, 2) + Math.pow(uY, 2);
    }

    /**
     *
     * @param uX coordonnée X du point u
     * @param uY coordonnée Y du point u
     * @return retourne la norme d'un vecteur.
     */
    public static double norm(double uX, double uY){
        return Math.sqrt(squaredNorm(uX,uY));
    }

    /**
     *
     * @param aX coordonnée X du point A
     * @param aY coordonnée Y du point A
     * @param bX coordonnée X du point B
     * @param bY coordonnée Y du point B
     * @param pX coordonnée X du point P
     * @param pY coordonnée Y du point P
     * @return retourne la longeur de la projection du vecteur AP sur le vecteur AB.
     */
    public static double projectionLength(double aX, double aY, double bX, double bY, double pX, double pY){
        double uX = Math.abs(aX - pX);
        double uY = Math.abs(aY - pY);
        double vX = Math.abs(aX-bX);
        double vY = Math.abs(aY-bY);
        return (dotProduct(uX, uY, vX, vY) / norm(vX, vY));
    }


}

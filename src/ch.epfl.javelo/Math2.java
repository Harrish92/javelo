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
    static int ceilDiv(int x, int y){
        Preconditions.checkArgument(x < 0 || y <= 0);
        return (x+y-1)/y;
    }

    /**
     *
     * @param y0 valeur de y en x0
     * @param y1 valeur de y en x
     * @param x
     * @return retourne une interpolation de la forme de y = a*x+y0
     */
    static double interpolate(double y0, double y1, double x){
        return Math.fma(y1,x,y0);
    }

    static int clamp(int min, int v, int max){
        Preconditions.checkArgument(min > max);
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

    static double clamp(double  min, double v, double max){
        Preconditions.checkArgument(min > max);
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

    static double asinh(double x){
        return Math.log(x+Math.sqrt(1+Math.pow(x,2)));
    }

    static double dotProduct(double uX, double uY, double vX, double vY){
        return Math.fma(uX, vX, uX*uY);
    }

    static double squareNorm(double uX, double uY){
        return Math.pow(uX, 2) + Math.pow(uY, 2);
    }

    static double norm(double uX, double uY){
        return Math.sqrt(squareNorm(uX,uY));
    }

    static double projectionLength(double aX, double aY, double bX, double bY, double pX, double pY){
        double uX = Math.abs(aX - pX);
        double uY = Math.abs(aY - pY);
        double vX = Math.abs(aX-bX);
        double vY = Math.abs(aY-bY);
        return (Math.abs(aX - pX) / norm(vX, vY));
    }


}

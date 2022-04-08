package ch.epfl.javelo.routing;

/**
 * Interface représentant le coût d'une fonction.
 *
 * @author Yoan Giovannini (303934)
 */
public interface CostFunction {

    /**
     * Calcule le facteur par lequel la longueur de l'arête d'identité edgeId,
     * partant du nœud d'identité nodeId, doit être multipliée.
     *
     * @param nodeId identité du noeud.
     * @param edgeId identité de l'arête.
     * @return un facteur supérieur ou égal à 1.
     */
    double costFactor(int nodeId, int edgeId);
}

package ch.epfl.javelo.routing;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;

import java.util.*;

/**
 * Représente un planificateur d'itinéraire.
 * @author Yoan Giovannini (303934)
 */
public final class RouteComputer {
    private final Graph graph;
    private final CostFunction costFct;

    record WeightedNode(int nodeId, float distance)
            implements Comparable<WeightedNode> {
        @Override
        public int compareTo(WeightedNode that) {
            return Float.compare(this.distance, that.distance);
        }
    }

    /**
     * Construit un planificateur d'itinéraire pour le graphe et
     * la fonction de coût donnés.
     * @param graph le graphe.
     * @param costFunction la fonction de coût.
     */
    public RouteComputer(Graph graph, CostFunction costFunction) {
        this.graph = graph;
        this.costFct = costFunction;
    }

    /**
     * Calcule l'itinéraire de coût total minimal allant du noeud d'identité
     * startNodeId au noeud d'identité endNodeId dans le graphe passé au constructeur.
     * @throws IllegalArgumentException si le noeud de départ et d'arrivée sont identiques.
     * @param startNodeId le noeud de départ.
     * @param endNodeId le noeud d'arrivée.
     * @return un itinéraire ou null s'il en existe aucun.
     */
    public Route bestRouteBetween(int startNodeId, int endNodeId) {
        if(startNodeId == endNodeId) throw  new IllegalArgumentException();
        //Initialisation des tableaux
        int nbN = graph.nodeCount();
        double[] distance = new double[nbN];
        int[] predecesseur = new int[nbN];
        int[] predecesseurEdgeId = new int[nbN];
        //Remplissage des tableaux
        for(int nID = 0; nID < nbN; nID++){
            distance[nID] = (int) Double.POSITIVE_INFINITY;
        }
        //Premier noeud
        distance[startNodeId] = 0;
        predecesseur[startNodeId] = startNodeId;
        PriorityQueue<WeightedNode> enExploration = new PriorityQueue<>();
        enExploration.add(new WeightedNode(startNodeId, 0));
        //Itère sur les noeuds en exploration
        while(!enExploration.isEmpty()) {
            int N;
            do {
                N = enExploration.remove().nodeId;
            }
            while (distance[N] == Double.NEGATIVE_INFINITY);
            if(N == endNodeId) {
                //Termine le programme, car le plus court chemin a été trouvé
                ArrayList<Edge> edgesList = new ArrayList<>();
                while(predecesseur[N] != N) {
                    Edge edge = Edge.of(graph, predecesseurEdgeId[N], predecesseur[N], N);
                    edgesList.add(edge);
                    N = predecesseur[N];
                }
                Collections.reverse(edgesList);
                return new SingleRoute(edgesList);
            }
            //Explore les arêtes du noeud N
            for(int i = 0; i < graph.nodeOutDegree(N); i++) {
                int edgeId = graph.nodeOutEdgeId(N, i);
                int Np = graph.edgeTargetNodeId(edgeId);
                double d = distance[N]
                        + graph.edgeLength(edgeId)*costFct.costFactor(N, edgeId);
                if(d < distance[Np]){
                    distance[Np] = d;
                    predecesseur[Np] = N;
                    predecesseurEdgeId[Np] = edgeId;
                    d += graph.nodePoint(endNodeId).distanceTo(graph.nodePoint(Np));
                    enExploration.add(new WeightedNode(Np, (float) d));
                }
            }
            distance[N] = Double.NEGATIVE_INFINITY;
        }
        return null;
    }
}
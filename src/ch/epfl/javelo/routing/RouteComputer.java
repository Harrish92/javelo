package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Représente un planificateur d'itinéraire.
 * @author Yoan Giovannini (303934)
 */
public class RouteComputer {
    Graph graph;
    CostFunction costFct;

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
    /*public Route bestRouteBetween(int startNodeId, int endNodeId) {
        int nbN = graph.nodeCount();
        double[] distance = new double[nbN];
        WeightedNode[] predecesseur = new WeightedNode[nbN];
        int[] predecesseurEdgeId = new int[nbN];
        for(int nID = 0; nID < nbN; nID++){
            distance[nID] = (int) Double.POSITIVE_INFINITY;
            predecesseur[nID] = 0;
        }
        distance[startNodeId] = 0;
        PriorityQueue<WeightedNode> enExploration = new PriorityQueue<>();
        enExploration.add(new WeightedNode(startNodeId, 0));
        while(!enExploration.isEmpty()) {
            WeightedNode N = enExploration.remove();
            if(N.nodeId == endNodeId) {
                //TERMINER
                ArrayList<Edge> edgesList = new ArrayList<>();
                while(predecesseur[N.nodeId] != 0) {//TODO: verifier
                    Edge edge = Edge.of(graph, predecesseurEdgeId[predecesseur[N.nodeId]], N, predecesseur[N.nodeId]);
                    edgesList.add(edge);
                    N = predecesseur[N.nodeId];
                }
                SingleRoute route = new SingleRoute(edgesList);
            }
            for(int i = 0; i < graph.nodeOutDegree(N); i++) {//TODO: costfunction ?
                int edgeId = graph.nodeOutEdgeId(N, i);
                int Np = graph.edgeTargetNodeId(edgeId);
                double d = distance[N] + graph.edgeLength(edgeId);
                if(d < distance[Np]){
                    distance[Np] = d;
                    predecesseur[Np] = N;
                    predecesseurEdgeId[Np] = edgeId;
                    enExploration.add(Np);
                }
            }
        }
        return null;
    }*/
    /* //pas weightednode
    public Route bestRouteBetween(int startNodeId, int endNodeId) {
        int nbN = graph.nodeCount();
        double[] distance = new double[nbN];
        int[] predecesseur = new int[nbN];
        int[] predecesseurEdgeId = new int[nbN];
        for(int nID = 0; nID < nbN; nID++){
            distance[nID] = (int) Double.POSITIVE_INFINITY;
            predecesseur[nID] = 0;
        }
        distance[startNodeId] = 0;
        PriorityQueue<Integer> enExploration = new PriorityQueue<>();
        enExploration.add(startNodeId);
        while(!enExploration.isEmpty()) {
            int N = enExploration.remove();
            if(N == endNodeId) {
                //TERMINER
                ArrayList<Edge> edgesList = new ArrayList<>();
                while(predecesseur[N] != 0) {//TODO: verifier
                    Edge edge = Edge.of(graph, predecesseurEdgeId[predecesseur[N]], N, predecesseur[N]);
                    edgesList.add(edge);
                    N = predecesseur[N];
                }
                SingleRoute route = new SingleRoute(edgesList);
            }
            for(int i = 0; i < graph.nodeOutDegree(N); i++) {//TODO: costfunction ?
                int edgeId = graph.nodeOutEdgeId(N, i);
                int Np = graph.edgeTargetNodeId(edgeId);
                double d = distance[N] + graph.edgeLength(edgeId);
                if(d < distance[Np]){
                    distance[Np] = d;
                    predecesseur[Np] = N;
                    predecesseurEdgeId[Np] = edgeId;
                    enExploration.add(Np);
                }
            }
        }
        return null;
    }
    */
}

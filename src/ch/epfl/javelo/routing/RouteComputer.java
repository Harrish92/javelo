package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
        predecesseur[startNodeId] = startNodeId;
        PriorityQueue<WeightedNode> enExploration = new PriorityQueue<>();
        enExploration.add(new WeightedNode(startNodeId, 0));
        while(!enExploration.isEmpty()) {
            int N = enExploration.remove().nodeId;
            while (N == Double.NEGATIVE_INFINITY){
                N = enExploration.remove().nodeId;
            }
            System.out.println(enExploration.size());
            if(N == endNodeId) {
                //TERMINER
                ArrayList<Edge> edgesList = new ArrayList<>();
                while(predecesseur[N] != N) {
                    Edge edge = Edge.of(graph, predecesseurEdgeId[N], predecesseur[N], N);
                    edgesList.add(edge);
                    N = predecesseur[N];
                }
                Collections.reverse(edgesList);
                SingleRoute route = new SingleRoute(edgesList);
                return route;
            }
            for(int i = 0; i < graph.nodeOutDegree(N); i++) {
                int edgeId = graph.nodeOutEdgeId(N, i);
                int Np = graph.edgeTargetNodeId(edgeId);
                CostFunction costFunction = new CityBikeCF(graph);
                //double d = distance[N] * costFunction.costFactor(N, edgeId) + graph.edgeLength(edgeId);
                double d = distance[N] + graph.edgeLength(edgeId)*costFunction.costFactor(N, edgeId);
                if(d < distance[Np]){
                    distance[Np] = d;
                    predecesseur[Np] = N;
                    predecesseurEdgeId[Np] = edgeId;
                    enExploration.add(new WeightedNode(Np, (float) d));
                    //distance[N] = Double.NEGATIVE_INFINITY; ca marche pas
                }
            }
        }
        return null;
    }
    //pas weightednode
    /*public Route bestRouteBetween(int startNodeId, int endNodeId) {
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
    }*/

}

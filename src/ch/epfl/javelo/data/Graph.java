package ch.epfl.javelo.data;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;

import java.io.IOException;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * La classe Graph représente le graphe de javelo.
 * @author Yoan Giovannini (303934)
 */
public final class Graph {

    private GraphNodes nodes;
    private GraphSectors sectors;
    private GraphEdges edges;
    private List<AttributeSet> attributeSets;
    /**
     * Constructeur de la classe Graph.
     * @param nodes les noeuds.
     * @param sectors les secteurs.
     * @param edges les arêtes.
     * @param attributeSets les attributs.
     */
    public Graph(GraphNodes nodes, GraphSectors sectors, GraphEdges edges,
                 List<AttributeSet> attributeSets) {
        this.nodes = nodes;
        this.sectors = sectors;
        this.edges = edges;
        this.attributeSets = List.copyOf(attributeSets);
    }

    /**
     * Charge le graphe depuis un répertoire.
     * @param basePath le chemin vers le répertoire.
     * @return le graphe.
     */
    public static Graph loadFrom(Path basePath) throws IOException {
        /*String paths[] = {"nodes.bin", "sectors.bin", "edges.bin", "profile_ids.bin",
                "elevations.bin", "attributes.bin"};//nodesIds: pour les test
        MappedByteBuffer[] buffers = new MappedByteBuffer[paths.length];
        for(int i = 0; i < paths.length; i++){
            try (FileChannel channel = FileChannel.open(basePath.resolve(paths[i]))) {
                buffers[i] = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            }
        }*/
        GraphNodes nodes = new GraphNodes(bufferFromPath(basePath, "nodes.bin").asIntBuffer());
        GraphSectors sectors = new GraphSectors(bufferFromPath(basePath, "sectors.bin"));
        GraphEdges edges = new GraphEdges(bufferFromPath(basePath, "edges.bin"),
                bufferFromPath(basePath, "profile_ids.bin").asIntBuffer(),
                bufferFromPath(basePath, "elevations.bin").asShortBuffer());

        ArrayList<AttributeSet> attributeSetList = new ArrayList<>();
        LongBuffer attBuffer = bufferFromPath(basePath, "attributes.bin").asLongBuffer();
        for(int i = 0; i < attBuffer.capacity(); i++){
            long bits = attBuffer.get(i);
            attributeSetList.add(new AttributeSet(bits));
        }
        return new Graph(nodes, sectors, edges, attributeSetList);
    }

    /**
     * Créée un buffer à partir d'un fichier.
     * @param basePath le chemin vers le répertoire.
     * @param path le nom du fichier.
     * @return un buffer d'octets.
     * @throws IOException si le channel ne peut pas être ouvert.
     */
    private static MappedByteBuffer bufferFromPath(Path basePath, String path) throws IOException {
        try (FileChannel channel = FileChannel.open(basePath.resolve(path))) {
            return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        }
    }

    /**
     * Compte les noeuds.
     * @return le nombre de noeuds.
     */
    public int nodeCount() {
        return nodes.count();
    }

    /**
     * Calcule la position du noeud d'identité donnée en argument.
     * @param nodeId l'identité d'un noeud.
     * @return la position du noeud.
     */
    public PointCh nodePoint(int nodeId){
        return new PointCh(nodes.nodeE(nodeId), nodes.nodeN(nodeId));
    }

    /**
     * Compte le nombre d'arêtes sortant du noeud donné en argument.
     * @param nodeId l'identité d'un noeud.
     * @return le nombre d'arêtes sortant du noeud.
     */
    public int nodeOutDegree(int nodeId) {
        return nodes.outDegree(nodeId);
    }

    /**
     * Calcule l'identité de la edgeIndex-ième arête sortant du nœud d'identité nodeId.
     * @param nodeId l'identitée du noeud.
     * @param edgeIndex l'indexe de l'arête.
     * @return l'identité de la edgeIndex-ième arête sortant du nœud d'identité nodeId.
     */
    public int nodeOutEdgeId(int nodeId, int edgeIndex) {
        return nodes.edgeId(nodeId, edgeIndex);
    }

    /**
     * Trouve le noeud le plus proche du point donné en paramètre dans un rayon donné.
     * @param point un point en coordonnées suisses.
     * @param searchDistance le rayon de recherche.
     * @return l'identité du noeud le plus proche.
     */
    public int nodeClosestTo(PointCh point, double searchDistance) {
        int nID = -1;
        Preconditions.checkArgument(searchDistance >= 0);
        double d = Math.pow(searchDistance, 2);
        ArrayList<GraphSectors.Sector> sectorList = (ArrayList<GraphSectors.Sector>) sectors.sectorsInArea(point, searchDistance);
        for(GraphSectors.Sector sector : sectorList){
            for(int i = sector.startNodeId(); i <= sector.endNodeId(); i++) {
                PointCh pt = new PointCh(nodes.nodeE(i), nodes.nodeN(i));
                if(point.squaredDistanceTo(pt) <= d){
                    nID = i;
                    d = point.squaredDistanceTo(pt);
                }
            }
        }
        return nID;
    }

    /**
     * Cherche l'identité du noeud destination de l'arête d'identité donnée en paramètre.
     * @param edgeId l'identité de l'arête.
     * @return l'identité du noeuds destination.
     */
    public int edgeTargetNodeId(int edgeId) {
        return edges.targetNodeId(edgeId);
    }

    /**
     * Renvoie vrai si et seulement si l'arête d'identité donnée en argument
     * va dans le sens contraire de la voie OSM dont elle provient.
     * @param edgeId l'identité de l'arête.
     * @return vrai si et seulement si l'arête va dans le sens contraire.
     */
    public boolean edgeIsInverted(int edgeId) {
        return edges.isInverted(edgeId);
    }

    /**
     * Retourne l'ensemble des attributs OSM attachés à l'arête d'identité donnée en argument.
     * @param edgeId l'identité de l'arête.
     * @return un ensemble d'attributs.
     */
    public AttributeSet edgeAttributes(int edgeId) {
        return  attributeSets.get(edges.attributesIndex(edgeId));
    }

    /**
     * Calcule la longueur de l'arête d'identité donnée en argument.
     * @param edgeId l'identité de l'arête.
     * @return une longueur en mètre.
     */
    public double edgeLength(int edgeId) {
        return edges.length(edgeId);
    }

    /**
     * Calcule le dénivelé positif total de l'arête d'identité donnée en argument.
     * @param edgeId l'identité de l'arête.
     * @return le dénivelé positif total de l'arête en mètre.
     */
    public double edgeElevationGain(int edgeId) {
        return edges.elevationGain(edgeId);
    }

    /**
     * Créé une fonction qui représente le profil de l'arête d'identité
     * donnée en argument.
     * @param edgeId l'identité de l'arête.
     * @return une fonction.
     */
    public DoubleUnaryOperator edgeProfile(int edgeId) {
        return Functions.sampled(edges.profileSamples(edgeId), edgeLength(edgeId));

    }
}

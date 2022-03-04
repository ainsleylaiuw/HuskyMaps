package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        this.edgeTo = new HashMap<>();
        this.distTo = new HashMap<>();

        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        Set<V> visited = new HashSet<>();
        List<V> result = new ArrayList<>();
        if (!visited.contains(start)) {
            dfsPostOrder(graph, start, visited, result);
        }
        // Reverse the DFS postorder.
        Collections.reverse(result);
        for (V node: result) {
            for (Edge<V> edge : graph.neighbors(node)) {
                V to = edge.to;
                // oldDist is the weight of the best-known path not using this edge.
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                // newDist is the weight of the shortest path using this edge.
                double newDist = distTo.get(node) + edge.weight;
                // Check that we haven't added the vertex to the SPT already...
                // AND the path using this edge is better than the best-known path.
                // if () cond. can't have "!visited.contains(to) && "
                if (newDist < oldDist) {
                    edgeTo.put(to, edge);
                    // Store the weight of the path using this edge.
                    distTo.put(to, newDist);
                    //dfsPostOrder(graph, start, visited, result);
                }
                // This if block is called "relaxing" an edge.
            }
        }


    }

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    private void dfsPostOrder(Graph<V> graph, V start, Set<V> visited, List<V> result) {
        visited.add(start);
        for (Edge<V> neighbor : graph.neighbors(start)) {
            if (!visited.contains(neighbor.to)) {
                dfsPostOrder(graph, neighbor.to, visited, result);
            }
        }
        // Postorder: Add start after visiting all the neighbors.
        result.add(start);
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}

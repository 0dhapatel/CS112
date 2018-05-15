package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		
		PartialTreeList list = new PartialTreeList();
		for (int i = 0; i < graph.vertices.length; i++) {
			Vertex ver = graph.vertices[i];
			PartialTree tree = new PartialTree(ver);
			MinHeap<PartialTree.Arc> point = tree.getArcs();
			for (structures.Vertex.Neighbor neigh = ver.neighbors; neigh != null; neigh = neigh.next) {
				point.insert(new PartialTree.Arc(ver, neigh.vertex, neigh.weight));
			}
			list.append(tree);
		}
		return list;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		
		/* COMPLETE THIS METHOD */

		ArrayList<PartialTree.Arc> list = new ArrayList<PartialTree.Arc>();
		while (ptlist.size() > 1) {
			PartialTree tree = ptlist.remove();
			MinHeap<PartialTree.Arc> treeHeap = tree.getArcs();
			if (treeHeap.isEmpty())
				continue;
			PartialTree.Arc treearc = treeHeap.deleteMin();
			while (treearc.v2.getRoot() == tree.getRoot()) {
				treearc = treeHeap.deleteMin();
			}
			list.add(treearc);
			PartialTree pty = ptlist.removeTreeContaining(treearc.v2);
			tree.merge(pty);
			ptlist.append(tree);
		}
		return list;
	}
}

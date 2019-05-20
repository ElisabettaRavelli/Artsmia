package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Graph<ArtObject,DefaultWeightedEdge> graph; //grafo pesato, semplice e non orientato
	private Map<Integer, ArtObject> idMap;//creo una volta solo l'oggetto e quando mi serve vado a recuperarlo quindi 
										  //è più conveniente utilizzare una mappa per inserirci i dati presi dal db
	
	public Model() {
		idMap=new HashMap<Integer, ArtObject>();
		graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	public void creaGraph() {
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(graph, idMap.values());
		//aggiungo gli archi (uso il terzo metodo che fa lavorare di più il DB ma riducendo il numero di query)
		List<Adiacenza> adj = dao.listAdiacenze();
		
		for(Adiacenza a:adj) {
			ArtObject source = idMap.get(a.getO1());//possiamo recuperare l'oggetto dalla mappa con un certo id
			ArtObject dest = idMap.get(a.getO2());
			Graphs.addEdge(graph, source, dest, a.getPeso());
			
		}
		
		System.out.println("Grafo creato: " + graph.vertexSet().size()+" vertici e "+graph.edgeSet().size()+" archi");
	}
	
	public int getVertexSize() {
		return graph.vertexSet().size();
	}
	
	public int getEdgeSize() {
		return graph.edgeSet().size();
	}
}

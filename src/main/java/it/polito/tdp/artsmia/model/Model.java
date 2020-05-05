package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	public Model() {
		idMap= new HashMap<>();
	}
	public void creaGrafo() {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//Aggiungere vertici
		ArtsmiaDAO dao= new ArtsmiaDAO();
		dao.listObjects(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		// Aggiungere archi
		
		/*
		 * Modo 2! oggetti adiacenti al primo MOLTO LUNGO
		 *  SELECT e2.object_id, COUNT(*) FROM exhibition_objects AS e1 , exhibition_objects AS e2 
		    WHERE e1.exhibition_id=e2.exhibition_id 
		   AND e1.object_id= 8485 AND e2.object_id != e1.object_id  GROUP BY e2.object_id
		
		 */
		
		//MODO 3, SEMPRE IL MIGLIORE
		for(Adiacenza a: dao.getAdiacenze()) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getObj1()), idMap.get(a.getObj2()), a.getPeso());
			}
		}
		
		System.out.format("Grafo con %d vertici e %d archi",this.grafo.vertexSet().size(),this.grafo.edgeSet().size());
	}
}

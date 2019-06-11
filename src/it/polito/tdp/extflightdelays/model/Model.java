package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;


public class Model {

	/*
	 * Decido di lavorare su JAVA
	 */
	
	private Map<Integer, Airport> idAirportMap;
	private Graph<Airport, DefaultWeightedEdge> graph;
	private ExtFlightDelaysDAO dao;
	private List<Flight> allFlights;
	private Set<AirportDuration> ottimo;
	private double best;
	private int max;

	public Model() {
	
		this.idAirportMap = new HashMap<Integer, Airport>();
		this.dao = new ExtFlightDelaysDAO();
		dao.loadAllAirports(idAirportMap);
		this.allFlights = dao.loadAllFlights(idAirportMap); 
	}
	
	public void creaGrafo(int numMin) {
		
		
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//devo aggiungere solo i vertici aventi abbastanza voli
		
		for(Airport air : idAirportMap.values()) {
			
			int numeroVoli = 0;
			
			for(Flight fli : allFlights) {
				
				if(air.getId() == fli.getOriginAirportId() || air.getId() == fli.getDestinationAirportId())
					numeroVoli++;
				
			}
			
			if(numeroVoli>=numMin)
				graph.addVertex(air);
			
		}
		
		//per gli archi è un po' piuù complesso
		
		/*
		 * Per ogni aereporto lo confronto
		 * con un altro diverso e vedo che voli hanno in comune
		 */
		
		List<Airport> listaVertici = this.listaVertici();
		
		for(int i=0; i<listaVertici.size()-1; i++) {
			
			Airport primo = listaVertici.get(i);
			
			for(int j=i+1; j<listaVertici.size(); j++) {
				
				Airport secondo = listaVertici.get(j);
				
				double durataVoli = 0.0; 
				int numeroVoli = 0;
				
				for(Flight fli : allFlights) {
				
				if((fli.getOriginAirportId() == primo.getId() && fli.getDestinationAirportId() == secondo.getId())
					 ||	(fli.getOriginAirportId() == secondo.getId() && fli.getDestinationAirportId() == primo.getId())) {
					
					durataVoli+=fli.getElapsedTime();
					numeroVoli++;
				    }
		
				}
				
				if(numeroVoli>0) 
					Graphs.addEdge(graph, primo, secondo, (double)(durataVoli/numeroVoli));
			
			}
		}
		
		System.out.println("Grafo creato --> Verici: "+graph.vertexSet().size() 
		        +"  Archi:  "+graph.edgeSet().size());
	}
	
	
	public List<Airport> listaVertici(){
		if(graph!=null) {
			List<Airport> vertici =  new ArrayList<Airport>(graph.vertexSet());
			Collections.sort(vertici);
			return vertici;
		}
		return null;
	}
	
	public List<AirportDuration> listaVicini(Airport fisso){
		
		if(graph!=null) {
			List<AirportDuration> vicini = new LinkedList<>();
			for(Airport air : Graphs.neighborListOf(graph, fisso))
				vicini.add(new AirportDuration(fisso, air, graph.getEdgeWeight(graph.getEdge(fisso, air))));
			Collections.sort(vicini);
			return vicini;
		}
		return null;	
		
	}

	public Set<AirportDuration> camminoOttimo(int max, Airport partenza) {
	
		this.ottimo = new HashSet<>();
		this.max = max;
		this.best = 0.0;
		
		Set<AirportDuration> parziale = new HashSet<AirportDuration>();
		List<AirportDuration> disponibili = this.listaVicini(partenza);
		cerca(parziale, 0, disponibili);
		
		return ottimo;
		
	}

	private void cerca(Set<AirportDuration> parziale, int L, List<AirportDuration> disponibili) {
		
		//non ho più aereoporti da visitare
		if(L==disponibili.size())
			return;
		//non aggiungo l'aereoporto
		cerca(parziale,L+1,disponibili);
		//aggiungo l'aereoporto
		
		//metodo stupido
		if(sfora(parziale, disponibili.get(L))) {
			//se non sfora continuo
			if(calcolaPeso(parziale)>best) {
				best = calcolaPeso(parziale);
				ottimo = new HashSet<>(parziale);
				return;
			}return;
		}
		else {
		parziale.add(disponibili.get(L));
		cerca(parziale,L+1,disponibili);
		parziale.remove(disponibili.get(L));
		}
	}

	private boolean sfora(Set<AirportDuration> parziale, AirportDuration airportDuration) {
		
		Set<AirportDuration> ris = new HashSet<AirportDuration>(parziale);
		ris.add(airportDuration);
		 if(calcolaPeso(ris)>max)
			 return true;
		 else
	       	return false;
	}

	public double calcolaPeso(Set<AirportDuration> ris) {
	
		double peso = 0.0;
		
		for(AirportDuration ad : ris)
			peso+=2*(ad.getAvgDurata());
		
		return peso;
	}
	
}
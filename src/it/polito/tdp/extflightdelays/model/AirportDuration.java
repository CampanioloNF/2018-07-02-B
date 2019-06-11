package it.polito.tdp.extflightdelays.model;

public class AirportDuration implements Comparable <AirportDuration>{

	private Airport fisso;
	private Airport vicino;
	private double avgDurata;
	
	public AirportDuration(Airport fisso, Airport vicino, double avgDurata) {
		super();
		this.fisso = fisso;
		this.vicino = vicino;
		this.avgDurata = avgDurata;
	}
	public Airport getFisso() {
		return fisso;
	}
	public Airport getVicino() {
		return vicino;
	}
	public double getAvgDurata() {
		return avgDurata;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fisso == null) ? 0 : fisso.hashCode());
		result = prime * result + ((vicino == null) ? 0 : vicino.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AirportDuration other = (AirportDuration) obj;
		if (fisso == null) {
			if (other.fisso != null)
				return false;
		} else if (!fisso.equals(other.fisso))
			return false;
		if (vicino == null) {
			if (other.vicino != null)
				return false;
		} else if (!vicino.equals(other.vicino))
			return false;
		return true;
	}
	@Override
	public int compareTo(AirportDuration o) {
		// TODO Auto-generated method stub
		return (int) (this.avgDurata-o.avgDurata);
	}
	
	
	
	
}

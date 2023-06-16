package modelo;

import java.util.ArrayList;
public class Grupo extends Participante {
	private ArrayList<Individual> individuos;
	
	public Grupo(String nome){
		super(nome);
		individuos = new ArrayList<>();
	}
	
	public ArrayList<Individual> getIndividuos(){
		return individuos;
	}
	
	public void adicionar(Individual ind) {
		individuos.add(ind);
		ind.adicionar(this);
	}
	
	public void remover(Individual ind) {
		individuos.remove(ind);
	}
	
	public Individual localizarIndividuo(String nome) {
		for(Individual ind: individuos) {
			if(nome.equals(ind.getNome())) {
				return ind;
			}
		}
		return null;
	}
	
	public boolean isMembro(String nome) {
	    return localizarIndividuo(nome) != null;
	}
	
	public String toString() {
		return "Nome do grupo: " + this.getNome() ;
	}
}

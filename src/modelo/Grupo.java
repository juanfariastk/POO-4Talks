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
	}
}

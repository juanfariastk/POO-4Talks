package modelo;

import java.util.ArrayList;

public class Individual extends Participante {
	private String senha;
	private boolean administrador;
	private ArrayList<Grupo> grupos;
	
	public Individual(String nome,String senha, boolean admin ) {
		super(nome);
		this.senha = senha;
		administrador = admin;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public boolean getAdministrador() {
		return administrador;
	}
	
	public ArrayList<Grupo> getGrupos(){
		return grupos;
	}
	
	public void adicionar(Grupo grup) {
		grupos.add(grup);
		grup.adicionar(this);
	}
	
	public void remover(Grupo grup) {
		grupos.remove(grup);
		grup.remover(this);
	}
	
	public Grupo localizarGrupo(String nome) {
		for(Grupo grup : grupos) {
			if(nome.equals(grup)) {
				return grup;
			}
		}
		return null;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}

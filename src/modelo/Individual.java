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
		grupos = new ArrayList<>();
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
	}
	
	public void remover(Grupo grup) {
		grupos.remove(grup);
		grup.remover(this);
	}
	
	public Grupo localizarGrupo(String nome) {
		for(Grupo grup : grupos) {
			if(nome.equals(grup.getNome())) {
				return grup;
			}
		}
		return null;
	}
	
	public boolean verificarGrupo(String nome) {
		for (Grupo g : grupos) {
			if(g.getNome().equals(nome))
				return true;
		}
		return false;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	@Override
	public String toString() {
		String s = super.toString() + "\n Grupos do participante: ";
		
		if(grupos.isEmpty())
			s += "sem grupo";
		
		else {
			for(Grupo g : grupos) {
				s += "\n  --  " + g.getNome();
			}
		}
		return s;
	}

	
}

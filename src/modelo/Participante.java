package modelo;

import java.util.ArrayList;
public class Participante {
	private String nome;
	private ArrayList<Mensagem> recebidas;
	private ArrayList<Mensagem> enviadas;
	
	public Participante(String nome) {
		this.nome = nome;
		recebidas = new ArrayList<>();
		enviadas = new ArrayList<>();
	}
	
	public String getNome() {
		return nome;
	}
	
	public ArrayList<Mensagem> getMensagensRecebidas(){
		return recebidas;
	}
	
	public ArrayList<Mensagem> getMensagensEnviadas(){
		return enviadas;
	}
	
	public Mensagem localizarEnviada(int ID) {
		for (Mensagem m : enviadas) {
			if(m.getId() == ID)
				return m;
		}
		return null;
	}
	
	public Mensagem localizarRecebida(int ID) {
		for (Mensagem m : recebidas) {
			if(m.getId() == ID)
				return m;
		}
		return null;
	}
	
	public void removerEnviada(int ID) {
		enviadas.remove(ID);
	}
	
	public void removerRecebida(int ID) {
		recebidas.remove(ID);
	}
	
	public void adicionarEnviada(Mensagem mensag) {
		enviadas.add(mensag);
	}
	
	public void adicionarRecebida(Mensagem mensag) {
		recebidas.add(mensag);
	}
	
	public void removerEnviada(Mensagem mensag) {
		enviadas.remove(mensag);
	}
	
	public void removerRecebida(Mensagem mensag) {
		recebidas.remove(mensag);
	}
	
	@Override
	public String toString() {
		String s = " -- Nome = " + nome + "\n Mensagens enviadas: ";
		
		if(enviadas.isEmpty())
			s += " Sem mensagens enviadas \n";
		
		else {
			for(Mensagem m : enviadas) {
				s += "\n  -> " + m;
			}
			s += "\n ";
		}
		
		s += " Mensagens recebidas: ";
		
		if(recebidas.isEmpty())
			s += "Sem mensagens recebidas";
		
		else {
			for(Mensagem m : recebidas) {
				s += "\n  -> " + m;
			}
		}
		return s;
	}
}

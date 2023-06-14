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
	
	public String toString() {
		return "Nome do participante: " + nome + " | Mensagens recebidas: " + recebidas + " | Mensagens enviadas: " + enviadas; 
	}
}

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
		return enviadas.get(ID);
	}
	
	public Mensagem localizarRecebida(int ID) {
		return recebidas.get(ID);
	}
	
	public void removerEnviada(int ID) {
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
	
	public String toString() {
		return "Nome do participante: " + nome + " | Mensagens recebidas: " + recebidas + " | Mensagens enviadas: " + enviadas; 
	}
}

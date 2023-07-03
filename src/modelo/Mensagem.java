package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Mensagem {
	private int id;
	private String texto;
	private Participante emitente;
	private Participante destinatario;
	private LocalDateTime datahora;
	
	
	public Mensagem(int id,  Participante emissor, Participante destinatario,String msg) {
		this.id = id;
		texto = msg;
		emitente = emissor;
		this.destinatario = destinatario;
		datahora = LocalDateTime.now();
	}
	
	public int getId() {
		return id;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public Participante getEmitente() {
		return emitente;
	}
	
	public Participante getDestinatario() {
		return destinatario;
	}
	
	public LocalDateTime getDataHora() {
		return datahora;
	}
	
	public void setRemetente(Participante part) {
		emitente = part;
	}
	
	public void setDestinatario(Participante part) {
		destinatario = part;
	}
	
	public String toString() {
		return "ID : " + id + ", Emitente: " + this.emitente.getNome() + ", Destinatario =  " + this.destinatario.getNome() + " Data Hora: " + datahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " Texto : " + this.texto; 
	}
}

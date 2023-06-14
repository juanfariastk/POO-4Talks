package modelo;

import java.time.LocalDateTime;
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
	
	public void setParticipante(Participante part) {
		emitente = part;
	}
	
}

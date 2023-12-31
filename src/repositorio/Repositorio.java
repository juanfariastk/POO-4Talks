package repositorio;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import modelo.Grupo;
import modelo.Individual;
import modelo.Mensagem;
import modelo.Participante;

public class Repositorio {
	
	private TreeMap<String, Participante> participantes = new TreeMap<String, Participante>();
	private ArrayList<Mensagem> mensagens = new ArrayList<>();
	
	public Repositorio() {
		carregarObjetos();
	}
	
	public Mensagem criarMensagem(Participante emitente, Participante destinatario, String texto) {
		Mensagem mensagem;
		mensagem = new Mensagem(this.gerarId(), emitente, destinatario, texto, LocalDateTime.now());
		this.adicionar(mensagem);
		return mensagem;
	}
	
	public Mensagem criarMensagem(int id, Participante emitente, Participante destinatario, String texto) {
		Mensagem mensagem;
		mensagem = new Mensagem(id, emitente, destinatario, texto, LocalDateTime.now());
		this.adicionar(mensagem);
		return mensagem;
	}
	
	public void adicionar(Participante participante) {
		participantes.put(participante.getNome(), participante);
	}
	
	public void adicionar(Mensagem mensagem) {
		mensagens.add(mensagem);
	}
	
	public void remover(Participante participante) {
		participantes.remove(participante.getNome());
	}
	
	public void remover(Mensagem mensagem) {
		for (Mensagem m : mensagens) {
			if (m.getId() == mensagem.getId()) {
				mensagens.remove(mensagem);
				return;
			}
		}
	}
	
	public Participante localizarParticipante(String nome){
		return participantes.get(nome);
	}
	
	public Mensagem localizarMensagem(int id){
		for (Mensagem m: mensagens) {
			if (m.getId() == id) {
				return m;
			}
		} return null;
	}
	
	public Individual localizarIndividual(String nome){
		Participante participante = participantes.get(nome);
		
		if (participante instanceof Individual)
			return (Individual) participante;
		
		return null;
	}
	
	public Grupo localizarGrupo(String nome) {
		Participante participante = participantes.get(nome);
		
		if (participante instanceof Grupo)
			return (Grupo) participante;

		return null;
	}
	
	public int gerarId() {
		if (mensagens.isEmpty())
			return 1;
		int ultimoId = mensagens.get(mensagens.size() - 1).getId();
		return ultimoId + 1;
	}
	
	public TreeMap<String, Participante> getParticipantes() {
		return participantes;
	}
	
	public ArrayList<Individual> getIndividuos() {
		ArrayList<Individual> individuos = new ArrayList<>();
		
		for (Participante part : participantes.values())
			if (part instanceof Individual)
				individuos.add((Individual) part);

		return individuos;
	}
	
	public ArrayList<Grupo> getGrupos() {
		ArrayList<Grupo> grupos = new ArrayList<>();
		
		for (Participante part : participantes.values())
			if (part instanceof Grupo)
				grupos.add((Grupo) part);
	
		return grupos;
	}

	public void setParticipantesRepositorio(TreeMap<String, Participante> participantes) {
		this.participantes = participantes;
	}

	public ArrayList<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagensRepositorio(ArrayList<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public void carregarObjetos() {
		try {
			File grupos = new File(new File(".\\grupos.csv").getCanonicalPath());
			File mensagens = new File(new File(".\\mensagens.csv").getCanonicalPath());
			File individuos = new File(new File(".\\individuos.csv").getCanonicalPath());
			
			if (!grupos.exists() || !mensagens.exists() || !individuos.exists()) {
				
				FileWriter arquivoGrupos = new FileWriter(grupos);
				arquivoGrupos.close();
				
				FileWriter arquivoMensagens = new FileWriter(mensagens);
				arquivoMensagens.close();
				
				FileWriter arquivoIndividuos = new FileWriter(individuos);
				arquivoIndividuos.close();
				
				return;
			}
			
		} catch (Exception ex) {
			throw new RuntimeException("Criacao dos arquivos vazios: " + ex.getMessage());
		}
		
		String linha;
		String[] colunasDoCsv;

		try {
			String nome, senha, administrador;
			File caminhoArquivoIndividuos = new File(new File(".\\individuos.csv").getCanonicalPath());
			
			Scanner arquivoIndividuos = new Scanner(caminhoArquivoIndividuos);
			
			while (arquivoIndividuos.hasNextLine()) {
				linha = arquivoIndividuos.nextLine().trim();
				colunasDoCsv = linha.split(";");
			
				nome = colunasDoCsv[0];
				senha = colunasDoCsv[1];
				administrador = colunasDoCsv[2];
				
				Individual individuo = new Individual(nome, senha, Boolean.parseBoolean(administrador));
				
				this.adicionar(individuo);
			}
			arquivoIndividuos.close();
			
		} catch (Exception ex) {
			throw new RuntimeException("leitura arquivo de individuos:" + ex.getMessage());
		}
		
		try	{
			String nome;
			Grupo grupo;
			Individual individuo;
			File caminhoArquivoGrupos = new File( new File(".\\grupos.csv").getCanonicalPath())  ;
			
			Scanner arquivoGrupos = new Scanner(caminhoArquivoGrupos); 
			
			while (arquivoGrupos.hasNextLine()) {
				linha = arquivoGrupos.nextLine().trim();	
				colunasDoCsv = linha.split(";");
				nome = colunasDoCsv[0];
				grupo = new Grupo(nome);
				if (colunasDoCsv.length > 1)
					for (int i = 1; i < colunasDoCsv.length; i++) {
						individuo = this.localizarIndividual(colunasDoCsv[i]);
						grupo.adicionar(individuo);
					}
				this.adicionar(grupo);
			}
			arquivoGrupos.close();

		} catch(Exception ex) {
			throw new RuntimeException("leitura arquivo de grupos:" + ex.getMessage());
		}
		
		try	{
			String nomeEmitente, nomeDestinatario, texto;
			int id;
			LocalDateTime dataHora;
			Mensagem mensagem;
			Participante emitente, destinatario;
			File caminhoArquivoMensagens = new File( new File(".\\mensagens.csv").getCanonicalPath() )  ;
			
			Scanner arquivoMensagens = new Scanner(caminhoArquivoMensagens); 

			while (arquivoMensagens.hasNextLine()) {
				linha = arquivoMensagens.nextLine().trim();		
				colunasDoCsv = linha.split(";");
				id = Integer.parseInt(colunasDoCsv[0]);
				texto = colunasDoCsv[1];
				nomeEmitente = colunasDoCsv[2];
				nomeDestinatario = colunasDoCsv[3];
				
				DateTimeFormatterBuilder dateBuilder = new DateTimeFormatterBuilder();
				dateBuilder.parseCaseInsensitive();
				dateBuilder.append(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
				
				dataHora = LocalDateTime.parse(colunasDoCsv[4], dateBuilder.toFormatter());
				
				emitente = this.localizarParticipante(nomeEmitente);
				destinatario = this.localizarParticipante(nomeDestinatario);
				mensagem = new Mensagem(id, emitente, destinatario, texto, dataHora);
				
				this.adicionar(mensagem);
				emitente.adicionarMensagem(mensagem);
				destinatario.adicionar(mensagem);
			} 
			arquivoMensagens.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de mensagens:"+ex.getMessage());
		}
	}
	
	public void salvarObjetos() {
		
		try	{
			File caminhoArquivoMensagens = new File( new File(".\\mensagens.csv").getCanonicalPath())  ;
			FileWriter arquivoMensagens = new FileWriter(caminhoArquivoMensagens); 
			
			for (Mensagem msg : mensagens) {
				arquivoMensagens.write(
					msg.getId()+";"+
					msg.getTexto()+";"+
					msg.getEmitente().getNome()+";"+
					msg.getDestinatario().getNome()+";"+
					msg.getData()+"\n"
				);
			} 
			arquivoMensagens.close();
	
		} catch (Exception e){
			throw new RuntimeException("problema na criação do arquivo mensagens " + e.getMessage());
		}
	
		try	{
			File caminhoArquivoIndividuos = new File( new File(".\\individuos.csv").getCanonicalPath());
			FileWriter arquivoIndividuos = new FileWriter(caminhoArquivoIndividuos);
			
			for (Individual ind : this.getIndividuos())
				arquivoIndividuos.write(ind.getNome() +";"+ ind.getSenha() +";"+ ind.getAdministrador() +"\n");	
			 
			arquivoIndividuos.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na criação do arquivo  individuos "+e.getMessage());
		}
	
		try	{
			File caminhoArquivoGrupos = new File( new File(".\\grupos.csv").getCanonicalPath())  ;
			FileWriter arquivoGrupos = new FileWriter(caminhoArquivoGrupos);
			
			for (Grupo g : this.getGrupos()) {
				String individuos = "";
				
				for (Individual ind : g.getIndividuos())
					individuos += ";" + ind.getNome();
				arquivoGrupos.write(g.getNome() + individuos + "\n");	
			} 
			arquivoGrupos.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na criação do arquivo  grupos "+e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return String.format("Repositorio%nparticipantes = %s%nmensagens = %s%n", 
				this.participantes, 
				this.mensagens);
	}

}

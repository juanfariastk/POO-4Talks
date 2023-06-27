package regras_negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.Comparator;
import modelo.*;
import repositorio.Repositorio;

public class Fachada {
	private Fachada() {}

	private static Repositorio repositorio = new Repositorio();


	public static void criarIndividuo(String nome, String senha) throws  Exception{
		if(nome.isEmpty()) 
			throw new Exception("criar individual - nome vazio:");
		if(senha.isEmpty()) 
			throw new Exception("criar individual - senha vazia:");
		
		Participante p = repositorio.localizarParticipante(nome);	
		if(p != null) 
			throw new Exception("criar individual - nome ja existe:" + nome);
		

		Individual individuo = new Individual(nome,senha, false);
		repositorio.adicionar(individuo);		
	}
	
	public static boolean validarIndividuo(String nome) throws Exception {
		if(nome.isEmpty())
			throw new Exception("validar individuo - nome vazio");
		if(repositorio.localizarParticipante(nome)!= null)
			return true;
		return false;
	}
	
	public static void criarAdministrador(String nome, String senha) throws  Exception{
		if(nome.isEmpty())
			throw new Exception("criar administrador - nome vazio");
		if(senha.isEmpty())
			throw new Exception("criar administrador - senha vazia");
		if(validarIndividuo(nome))
			throw new Exception("criar administrador - usuário já existe");
		
		Individual ind = new Individual(nome, senha, true);
		repositorio.adicionar(ind);
		
	}
	
	public static void criarGrupo(String nome) throws  Exception{
		if(nome.isEmpty())
			throw new Exception("criar grupos - nome vazio");
		if(repositorio.localizarGrupo(nome)!=null) {
			throw new Exception("criar grupos - este grupo já existe!");
		}
		
		Grupo grup = new Grupo(nome);
		repositorio.adicionar(grup);
	}
	
	public static void inserirGrupo(String nomeInd, String nomeGrup) throws Exception{
				Individual ind = repositorio.localizarIndividual(nomeInd);
				if(ind == null) 
					throw new Exception("inserir Grupo - individuo não existe:" + nomeInd);
				
				Grupo g = repositorio.localizarGrupo(nomeGrup);
				if(g == null) 
					throw new Exception("inserir Grupo - grupo não existe:" + nomeGrup);
				

				ArrayList<Individual> individuos = g.getIndividuos();
				
				for(Individual ind2 : individuos){
					if((ind2.getNome()).equals(nomeInd))
						throw new Exception("inserir Grupo - individuo já está no grupo:");
				}
				
				g.adicionar(ind);
				
				ind.adicionar(g);
	}
	
	public static void removerGrupo(String nomeInd, String nomeGrup) throws Exception{
				//localizar nomeindividuo no repositorio
				//localizar nomegrupo no repositorio
				//verificar se individuo ja esta no grupo	
				//remover individuo com o grupo e vice-versa
		if(nomeInd.isEmpty())
			throw new Exception("inserir grupo - nome do individuo vazio");
		if(nomeGrup.isEmpty())
			throw new Exception("inserir grupo - nome do grupo vazio");
		
		if(validarIndividuo(nomeInd) && (repositorio.localizarGrupo(nomeGrup)!=null)) {
			Individual ind = repositorio.localizarIndividual(nomeInd);
			if(!(repositorio.localizarGrupo(nomeGrup).isMembro(nomeGrup)))
				throw new Exception("remover grupo - individuo não pertence a esse grupo");
			ind.remover((Grupo) repositorio.localizarGrupo(nomeGrup));
		}
	}
	
	public static void criarMensagem(String nomeemitente, String nomedestinatario, String texto) throws Exception{
		if(texto.isEmpty()) 
			throw new Exception("criar mensagem - texto vazio:");

		Individual emitente = repositorio.localizarIndividual(nomeemitente);	
		if(emitente == null) 
			throw new Exception("criar mensagem - emitente nao existe:" + nomeemitente);

		Participante destinatario = repositorio.localizarParticipante(nomedestinatario);	
		if(destinatario == null) 
			throw new Exception("criar mensagem - destinatario nao existe:" + nomeemitente);

		if(destinatario instanceof Grupo g && !emitente.verificarGrupo(g.getNome()))
			throw new Exception("criar mensagem - emitente não está inserido no grupo:" + nomedestinatario);

		int contmen = 0;
		do{
			contmen++;
		}while (verificarMensagemID(contmen));
		
		Mensagem m = new Mensagem(contmen, emitente, destinatario, texto);
		
		emitente.adicionarEnviada(m);
		destinatario.adicionarRecebida(m);
		
		repositorio.adicionarMensagem(m, destinatario, destinatario);
		
		if(destinatario instanceof Grupo) {
			Grupo g = (Grupo) destinatario;
			for(Individual ind : g.getIndividuos()) {
				if(!ind.equals(emitente)) {
					Mensagem copia = new Mensagem(contmen, emitente, destinatario, texto);
					g.adicionarEnviada(copia);
					ind.adicionarRecebida(copia);
				}
			}
		}	
		
	}
	
	public static ArrayList<Mensagem> obterConversa(String nomeemitente, String nomedestinatario) throws Exception{
				Individual emitente = repositorio.localizarIndividual(nomeemitente);	
				if(emitente == null) 
					throw new Exception("obter conversa - emitente nao existe:" + nomeemitente);
				
				Participante destinatario = repositorio.localizarParticipante(nomedestinatario);	
				if(destinatario == null) 
					throw new Exception("obter conversa - destinatario nao existe:" + nomeemitente);
				
				ArrayList<Mensagem> enviadas = emitente.getMensagensEnviadas();
				
				ArrayList<Mensagem> recebidas = emitente.getMensagensRecebidas();
				
				ArrayList<Mensagem> conversa = new ArrayList<>();
				
				for (Mensagem m : enviadas) {
					if(m.getDestinatario().equals(destinatario))
						conversa.add(m);
				}
				
				for (Mensagem m : recebidas) {
					if(m.getEmitente().equals(destinatario))
						conversa.add(m);
				}
				
				conversa.sort(new Comparator<Mensagem>() {
					@Override
					public int compare(Mensagem m1, Mensagem m2) {
						return Integer.compare(m1.getId(), m2.getId());
					}
				});
				
				return conversa;
	}

	public static void apagarMensagem(String nomeindividuo, int id) throws  Exception{
		Individual emitente = repositorio.localizarIndividual(nomeindividuo);	
		if(emitente == null) 
			throw new Exception("apagar mensagem - nome nao existe:" + nomeindividuo);

		Mensagem m = emitente.localizarEnviada(id);
		if(m == null)
			throw new Exception("apagar mensagem - mensagem nao pertence a este individuo:" + id);

		emitente.removerEnviada(m);
		Participante destinatario = m.getDestinatario();
		destinatario.removerRecebida(m);
		repositorio.remover(m);	

		if(destinatario instanceof Grupo g) {
			ArrayList<Mensagem> lista = destinatario.getMensagensEnviadas();
			lista.removeIf(new Predicate<Mensagem>() {
				@Override
				public boolean test(Mensagem t) {
					if(t.getId() == m.getId()) {
						t.getDestinatario().removerRecebida(t);	
						return true;	
					}
					else
						return false;
				}

			});

		}
	}
	
	public static ArrayList<Individual> listarIndividuos() {
		return repositorio.getIndividuos();	
	}
	public static ArrayList<Grupo> listarGrupos() {
		return repositorio.getGrupos();
	}
	public static ArrayList<Mensagem> listarMensagens() {
		return repositorio.getMensagens();
	}

	public static ArrayList<Mensagem> listarMensagensEnviadas(String nome) throws Exception{
		Individual ind = repositorio.localizarIndividual(nome);	
		if(ind == null) 
			throw new Exception("listar  mensagens enviadas - nome nao existe:" + nome);
		if(ind.getMensagensEnviadas().size() == 0)
			throw new Exception("Este individuo não enviou mensagens : " + ind.getNome());
		return ind.getMensagensEnviadas();	
	}

	public static ArrayList<Mensagem> listarMensagensRecebidas(String nome) throws Exception{
		Individual ind = repositorio.localizarIndividual(nome);
		if(ind==null)
			throw new Exception("listar mensagens recebidas - nome não existe: " + nome);
		return ind.getMensagensRecebidas();
	}

	
	private static boolean verificarMensagemID(int id) {
		return repositorio.getMensagens().contains(repositorio.localizarMensagem(id));
	}

	public static ArrayList<Mensagem> espionarMensagens(String nomeadministrador, String termo) throws Exception{
				Individual admin = repositorio.localizarIndividual(nomeadministrador);
				if(admin == null) 
					throw new Exception("espionar mensagem - individuo não existe:" + nomeadministrador);
				
				if(!admin.getAdministrador())
					throw new Exception("espionar mensagem - individuo não é administrador:" + nomeadministrador);
				
				if(termo.isEmpty())
					return repositorio.getMensagens();
				
				ArrayList<Mensagem> mensagensEspionadas = new ArrayList<>();
				for(Mensagem m : repositorio.getMensagens()) {
					if(m.getTexto().contains(termo))
						mensagensEspionadas.add(m);
				}
				return mensagensEspionadas;
	}

	public static ArrayList<String> ausentes(String nomeadministrador) throws Exception{
		Individual admin = repositorio.localizarIndividual(nomeadministrador);
		if(admin == null) 
			throw new Exception("ausentes - individuo não existe:" + nomeadministrador);
		
		if(!admin.getAdministrador())
			throw new Exception("ausentes - individuo não é administrador:" + nomeadministrador);
		
		ArrayList<String> ausentes = new ArrayList<>();
		for (Participante p : repositorio.getIndividuos()) {
			if(p.getMensagensEnviadas().isEmpty())
				ausentes.add(p.getNome());
		}
		return ausentes;
	}

}

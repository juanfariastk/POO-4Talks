package regras_negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

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
		//localizar nomeindividuo no repositorio
				//localizar nomegrupo no repositorio
				//verificar se individuo nao esta no grupo	
				//adicionar individuo com o grupo e vice-versa
		if(nomeInd.isEmpty())
			throw new Exception("inserir grupo - nome do individuo vazio");
		if(nomeGrup.isEmpty())
			throw new Exception("inserir grupo - nome do grupo vazio");
		
		if(validarIndividuo(nomeInd) && (repositorio.localizarGrupo(nomeGrup)!=null)) {
			Individual ind = repositorio.localizarIndividual(nomeInd);
			if(ind.getGrupos().contains(repositorio.localizarGrupo(nomeGrup)))
				throw new Exception("inserir grupo - individuo já pertence ao grupo");
			ind.adicionar((Grupo) repositorio.localizarGrupo(nomeGrup));
		}
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
			if(!(ind.getGrupos().contains(repositorio.localizarGrupo(nomeGrup))))
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

		if(destinatario instanceof Grupo g && emitente.localizarGrupo(g.getNome())==null)
			throw new Exception("criar mensagem - grupo nao permitido:" + nomedestinatario);
		
		int contmen = 0;
		do{
			contmen++;
		}while (verificarMensagemID(contmen));
		
		Mensagem mensagem = new Mensagem(contmen,emitente, destinatario, texto);
		if (destinatario instanceof Grupo) {
			Grupo grupoDestinatario = (Grupo) destinatario;
			for (Individual ind : grupoDestinatario.getIndividuos()) {
				Mensagem copiaMensagem = new Mensagem(contmen, grupoDestinatario, ind, texto);
				repositorio.adicionar(copiaMensagem, grupoDestinatario, ind);
			}
		}
		
		repositorio.adicionar(mensagem, emitente, destinatario);
		//cont.
		//gerar id no repositorio para a mensagem
		//criar mensagem
		//adicionar mensagem ao emitente e destinatario
		//adicionar mensagem ao repositorio
		//
		//caso destinatario seja tipo Grupo então criar copias da mensagem, tendo o grupo como emitente e cada membro do grupo como destinatario, 
		//  usando mesmo id e texto, e adicionar essas copias no repositorio
		
	}
	
	public static ArrayList<Mensagem> obterConversa(String nomeindividuo, String nomedestinatario) throws Exception{
		//localizar emitente no repositorio
		//localizar destinatario no repositorio
		//obter do emitente a lista  enviadas
		//obter do emitente a lista  recebidas
		
		//criar a lista conversa
		//Adicionar na conversa as mensagens da lista enviadas cujo destinatario é igual ao parametro destinatario
		//Adicionar na conversa as mensagens da lista recebidas cujo emitente é igual ao parametro destinatario
		//ordenar a lista conversa pelo id das mensagens
		//retornar a lista conversa
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
						repositorio.remover(t);	
						return true;		//apaga mensagem da lista
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
		//localizar individuo no repositorio
		//verificar se individuo é administrador
		//listar as mensagens que contem o termo no texto
	}

	public static ArrayList<String> ausentes(String nomeadministrador) throws Exception{
		//localizar individuo no repositorio
		//verificar se individuo é administrador
		//listar os nomes dos participante que nao enviaram mensagens
	}

}
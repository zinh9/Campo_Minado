package br.com.spadetto.cm.modelo;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.spadetto.cm.excecao.ExplosaoException;

// Classe que dará forma para a estrutura de cada campo e executar os métodos de cada campo
public class Tabuleiro {
	// Atributos necessarios para a classe Tabuleiro
	private int linhas; // Atributo que define o tanto de linhas
	private int colunas; //  Atributo que define o tanto de colunas
	private int minas; // Atributo que define o tanto de campos minados
	
	private final List<Campo> campos = new ArrayList<>(); // Lista de campos que vai receber cada valor

	// Construtor que recebe o tanto de linhas, colunas e minas
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos(); // Executa o método de gerar os campos
		associarVizinhos(); // Executa o método de associar cada campo com vizinhaça
		sortearMinas(); // Executa o método que vai gerar o tanto de minas aleatorias
	}
	
	// Método que vai abrir o tabuleiro
	public void abrir(int linha, int coluna) {
		try { // Abre um try para disparar uma exceção caso o comando retorne ...
			campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrir()); // Executa o método de abertura de um campo
		} catch(ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true)); // Se um campo aberto estiver minado, é lançado uma exceção de explosão e mostra todos os campos que estavam minados
			throw e;
		}
	}
	
	// Método para desmarcar e marcar um campo
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.alternarMarcacao()); // Executa o método de alternar marcação
	}

	// Método que vai gerar os campos
	private void gerarCampos() {
		for(int linha = 0; linha < linhas; linha++) {
	        for(int coluna = 0; coluna < colunas; coluna++) {
	            campos.add(new Campo(linha, coluna)); // É adicionado cada linha e coluna associados a cada campo
	        }
	    }
	}
	
	// Método que vai associar cada campo vizinho
	private void associarVizinhos() {
		for(Campo campo1 : campos) {
			for(Campo campo2 : campos) {
				campo1.adicionarVizinho(campo2);
			}
		}
		
	}
	
	// Método que vai gerar cada mina no campo
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado(); // Predicate para verificar se o campo esta minado
		
		do { // Abre um bloco do while para gerar um campo minado aleatorio
			int aleatorio = (int) (Math.random() * campos.size()); // Utiliza o Math.random para gerar um numero aleatorio em um intevalo do tamanho de campos
			campos.get(aleatorio).minar(); // Associa esse numero ao método para minar um campo
			minasArmadas = campos.stream().filter(minado).count(); // Pega todos os campos minados
		} while(minasArmadas < minas); // Enquanto o tamanho de minasArmadas for menor que o tanto de minas, ele executa o do while até gerar todas as minas
	}
	
	// Método que verifica se o objetivo do tabuleiro foi alcançado
	public boolean objetivoAlcancado() {
    	return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }
	
	// Método para reiniciar o jogo e sorteando novamente cada campo minado
	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	// Método que vai gerar todo o tabuleiro
	public String toString() {
		StringBuilder sb = new StringBuilder(); // Utiliza o StringBuilder para construir a representação do tabuleiro como String
		
		sb.append("  ");
		for(int coluna = 0; coluna < colunas; coluna++) { // Um loop para enumarar cada coluna para facilitar a visão do jogo
			sb.append(" ");
			sb.append(coluna);
			sb.append(" ");
		}
		
		sb.append("\n");
	        
	    int i = 0;
	    for(int linha = 0; linha < linhas; linha++) { // Loop para gerar cada linha dando espaçamento, e facilitar a visão de cada linha
	    	sb.append(linha);
	    	sb.append(" ");
	    	for(int coluna = 0; coluna < colunas; coluna++) { // Loop para gerar cada coluna dando espaçamento
	    		sb.append(" ");
	            sb.append(campos.get(i));
	            sb.append(" ");
	            i++;
	        }
	        sb.append("\n");
	    }
	        
	    return sb.toString(); // Retorna toda a estrutura do tabuleiro
	}
}

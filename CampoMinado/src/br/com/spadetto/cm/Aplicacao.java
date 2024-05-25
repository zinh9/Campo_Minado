package br.com.spadetto.cm;

import br.com.spadetto.cm.modelo.Tabuleiro;
import br.com.spadetto.cm.visual.TabuleiroConsole;

// Classe mãe onde vai ser executar o tabuleiro
public class Aplicacao {
	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro(7, 6, 4); // Instancia um objeto passando os valores de linha, caoluna e minas
		
		new TabuleiroConsole(tabuleiro); // Passa como parametro o objeto tabuleiro para a classe TabuleiroConsole, onde é possivel gerar a estrutura e jogar
	}
}

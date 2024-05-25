package br.com.spadetto.cm.visual;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.spadetto.cm.excecao.ExplosaoException;
import br.com.spadetto.cm.excecao.SairException;
import br.com.spadetto.cm.modelo.Tabuleiro;

// Classe que vai gerar o tabuleiro no console
public class TabuleiroConsole {
	// Atributos necessarios
	private Tabuleiro tabuleiro; // Atributo instanciado de Tabuleiro
	private Scanner in = new Scanner(System.in); // Atributo instanciado de Scanner para pegar os valores digitados no console pelo jogador
	
	// Contrutor que recebe como parametro um objeto de Tabuleiro
	public TabuleiroConsole(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		
		executarJogo(); // Executa o método para execuatar o jogo
	}

	// Método que vai e executar o jogo
	private void executarJogo() {
		try { // ...
			boolean continuar = true; // Variavel booleana para verificar se o jogador vai continuar jogando ou não
			
			while(continuar) { // Loop while que fica executando até a variavel for falsa
				cicloJogo(); // Execuat o método para dar ciclo ao jogo
				
				System.out.println("Deseja jogar outra partida ? (S/n)"); // Ao fina de uma partida, pergunta se o jogador quer continuar jogando ou não
				String resposta = in.nextLine();
				
				if("n".equalsIgnoreCase(resposta)) { // Se a resposta for igual a "n" (não), a variavel recebe false para encerrar o loop
					continuar = false;
				} else { // Caso contrario, o jogo é reiniciado chamando o método reiniciarJogo
					tabuleiro.reiniciarJogo();
				}
			}
		} catch(SairException e) { // Caso o jogador não queira mais jogar, é disparado uma exceção que vai parar o jogo e exibir uma mensagem
			while(true) {
				int mensagem = (int) (Math.random() * 4) + 1; // Variavel que recebe um valor de um intervalo entre 1 e 4 para exibir uma das mensagens
				
				if(mensagem == 1) {
					System.out.println("Vai arregar ?");
					break;
				} else if(mensagem == 2) {
					System.out.println("Obrigado :)");
					break;
				} else if(mensagem == 3) {
					System.out.println("Até a proxima");
					break;
				} else if(mensagem == 4) {
					System.out.println("Fica só mais um pouquinho");
					break;
				} 
			}
		} finally { // Fecha o objeto de Scanner
			in.close();
		}
	}
	
	// Método que gerar o ciclo do jogo
	private void cicloJogo() {
		try {
			while(!tabuleiro.objetivoAlcancado()) { // Equanto o objetivo do jogo não for alcançado, o jogo continua
				System.out.println(tabuleiro);
				
				String local = pegarValorDigitado("Digite o local (linha, coluna): "); // É chamado um método que pega um valor 
				//digitado pelo jogador, questionando qual linha e coluna ele deseja abrir ou des/marcar, e armazena essa resposta em uma variavel
				
				Iterator<Integer> xy = Arrays.stream(local.split(",")).map(e -> Integer.parseInt(e.trim())).iterator(); // Separa o valor digitado pelo usuario informando a localização do campo desejado (linha, coluna),
				// e converte essa resposta de String para tipo inteiro, os dois valores
				
				local = pegarValorDigitado("Digite 1 para abrir ou  2 para (Des)Marcar: "); // Chama novamente o método de pegar valor digitado e vai ser armazenado na mesma variavel
				
				if("1".equals(local)) { // Se o valor digitado for 1, o local do campo é aberto
					tabuleiro.abrir(xy.next(), xy.next());
				} else if("2".equals(local)) { // Se o valor digitado for 2, o local do campo é marcado
					tabuleiro.alternarMarcacao(xy.next(), xy.next());
				}
			}
			System.out.println("Você ganhou!"); // Quando o objetivo do jogo for alcançado, é exibido uma mensagem informando que o jogador ganhou
		} catch(ExplosaoException e) { // Caso o jogador abra um campo que esteja minado, é disparado uma exeção de explosão, e encerra o jogo
			System.out.println(tabuleiro); // Mostra todos os locais que tinham campos minados
			System.out.println("Você perdeu!"); // Exibe uma mensagem informando que o jogador perdeu
		}
		
	}
	
	// Método que pega o valor digitado pelo jogador
	private String pegarValorDigitado(String texto) {
		System.out.println(texto); // Mostra a mensagem no console e pega a resposta do jogador
		String resposta = in.nextLine(); 
		
		if("sair".equalsIgnoreCase(resposta)) { // Se o jogador digitar "sair", é disparado uma exceção que vai encerrar o jogo
			throw new SairException();
		}
		
		return resposta; // Caso contrario, é retornado uma das resposta das opções do jogador
	}
}

package br.com.spadetto.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.spadetto.cm.excecao.ExplosaoException;

// Classe que define cada campo e suas propriedades
public class Campo {
	// Atributos necessarios para a classe Campo
	private List<Campo> vizinhos = new ArrayList<Campo>(); // Lista de objetos vizinhos
    
    private boolean marcado = false; // Atributo booleano para saber se um campo esta marcado ou não
    private boolean aberto = false; // Atrubuto booleano para saber se um campo esta aberto ou não
    private boolean minado = false; // Atributo booleano para saber se o campo está minado
    
    private final int linha; // Atributo constante que define o tanto de linhas
    private final int coluna; //  Atributo constante que define o tanto de colunas
    
    // Construtor para receber o tamanho de linhas e colunas
    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    // Método para saber se um campo é vizinho
    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha; // O valor de um campo deve ser diferente de um campo vizinho para ser vizinho
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente; // O valor de um vizinho diagonal deve ser diferente dos dois vizinhos (de linha e coluna)
        
        int deltaLinha = Math.abs(linha - vizinho.linha); // O valor de linha e coluna deve ter uma diferença de 1
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int geral = deltaLinha + deltaColuna; // A soma desses valores deve ser 1 ou 2
        
        if(geral == 1 && !diagonal) { // Se a soma dos valores for igual a 1 e diagonal for false, é adicionado um vizinho
            vizinhos.add(vizinho);
            return true;
        } else if(geral == 2 && diagonal) { // Se a soma dos valores for igual a 2 e diagonal for true, é adicionado um vizinho
            vizinhos.add(vizinho);
            return true;
        } else { //  Caso contrario o retorno sera false
            return false;
        }
    }
    
    // Método para desmarcar ou marcar um campo
    void alternarMarcacao() {
    	if(!aberto) {
    		marcado = !marcado;
    	}
    }
    
    // Método para abrir um campo
    boolean abrir() {
    	if(!aberto && !marcado) { // Se os valores não forem verdadeiros, o campo pode ser aberto
    		aberto = true;
    		
    		if(minado) { //  Se um campo que foi aberto tiver uma mina, é disparada uma exceção
    			throw new ExplosaoException();
    		}
    		
    		if(vizinhacaSegura()) { // Se for uma vizinhaça não minada, os demais vizinhos que não estão minados podem ser abertos
    			vizinhos.forEach(v -> v.abrir());
    		}
    		
    		return true;
    	} else { // Caso contrario é retornado false
    		return false;
    	}
    }
    
    // Método que verifica se um campo vizinho contem ou não um campo minado
    public boolean vizinhacaSegura() {
    	return vizinhos.stream().noneMatch(v -> v.minado); // Retorna cada campo dentro de vizinhos que não der match com duas vizinhaças
    }
    
    // Método que ao inicializar o jogo vai declarar os campos minados
    void minar() {
    	minado = true; 
    }
    
    // Método que diz se um campo está marcado
    public boolean isMarcacao() {
    	return marcado;
    }
    
    // Método que diz se um campo está aberto
    public boolean isAberto() {
    	return aberto;
    }
    
    // Método que recebe um valor booleano que vai declarar se um campo vai está aberto ou fechado
    void setAberto(boolean aberto) {
    	this.aberto = aberto;
    }
    
    // Método que diz se um campo está fechado
    public boolean isFechado() {
    	return !aberto;
    }
    
    // Método que diz se um campo está minado
    public boolean isMinado() {
    	return minado;
    }
    
    // Método que retorna o valor de linhas
	public int getLinha() {
		return linha;
	}
	
	// Método que retorna o valor de colunas
	public int getColuna() {
		return coluna;
	}
    
	// Método ...
    public boolean objetivoAlcancado() {
    	boolean desvendado = !minado && aberto; // Verifica se o campo não esta minado e se esta aberto 
    	boolean protegido = minado && marcado; // Verifica se o campo esta minado e marcado
    	return desvendado || protegido; // Retorna true e o campo foi desvendado ou protegido
    }
    
    // Método que enumera a quantidade de minas por vizinhaça
    public long minasNaVizinhaca() {
    	return vizinhos.stream().filter(v -> v.minado).count(); // Retorna todas os campos que contem campos minados
    }
    
    // Método que reinicia o jogo para jogar novamente
    public void reiniciar() {
    	marcado = false;
        aberto = false;
        minado = false;
    }
    
    // Método que da a forma para cada campo
    public String toString() {
    	if(marcado) { // Se o campo estiver marcado, vai ser um "X", para simbolizar um campo marcado
    		return "X";
    	} else if(minado && aberto) { //  Se um campo for minado e for aberto, será um "*" para simbolizar uma bomba
    		return "*";
    	} else if (aberto && minasNaVizinhaca() > 0) { // Se um campo estiver aberto e cada campo que contem vizinho minado, vai mostrar o tanto de campos minados por vizinhaça
    		return Long.toString(minasNaVizinhaca());
    	} else if (aberto) { // Se um campo aberto não tiver campos vizinhos, retorna um " " para simbolizar que esta vazio
    		return " ";
    	} else { // Caso contrario cada campo é simbolizado com "#" campo estado inicial
    		return "#";
    	}
    }
}

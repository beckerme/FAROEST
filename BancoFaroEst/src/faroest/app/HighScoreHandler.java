package faroest.app;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Classe responsável por tratar das pontuações máximas. É ela que mantém 
 * as pontuações, as lê e grava no disco.
 */
public class HighScoreHandler {
	/** número máximo de pontuações que são armazenadas */
	public static final int MAX_SCORES = 20;       
	/** número máximo de caracteres de um nome */
	public static final int MAX_CHARS_NOME = 20; 
	/** número máximo de digitos da pontuação (guardados no ficheiro) */
	public static final int MAX_DIGITOS_PONT = 7; 

	/** Classe interna que representa uma pontuação na lista de records.
	 * Colocou-se esta interna para estarem as duas classes juntas já que esta só
	 * é usada em conjunção com a HighScoreHandler
	 */
	public static class Score {
		private String nome;
		private int pontuacao;
		
		/** Cria um Score, com o nome do jogador e respetiva pontuação
		 * @param nome nome do jogador (tem de ter menos de 20 caracteres)
		 * @param pontuacao pontuação obtida pelo jogador (tem de ser positiva)
		 */
		public Score(String nome, int pontuacao) {
			if( pontuacao < 0 )
				throw new IllegalArgumentException("pontuacao tem de ser positiva: " + pontuacao );
			this.nome = Objects.requireNonNull(nome);
			if( nome.length() > 20 )
				throw new IllegalArgumentException("nome comprido demais " + nome );
			this.pontuacao = pontuacao;
		}

		/** retorna o nome do jogador que obteve a pontuação 
		 * @return o nome do jogador que obteve a pontuação
		 */
		public String getNome() {
			return nome;
		}

		/** retorna a pontuação obtida
		 * @return  a pontuação obtida
		 */
		public int getPontuacao() {
			return pontuacao;
		}
	}

	/** o ficheiro onde se armazenam as pontuações */
	private File file;
	
	/** a lista de pontuações */
	private ArrayList<Score> pontuacoes = new ArrayList<Score>( MAX_SCORES );

	/** Cria um HishScoreHandler indicando onde este deve ler e armazenar as pontuações.
	 * Começa por ler e carregar as pontuações do ficheiro
	 * @param nomeFich nome do ficheiro onde ler e armazenar as pontuações
	 * @throws IOException se houver algum erro a ler o ficheiro
	 */
	public HighScoreHandler( String nomeFich ) throws IOException {
		file = new File( nomeFich );
		loadScores( );
	}

	/** carrega as pontuações máximas do ficheiro
	 * @throws IOException se houver algum erro a ler o ficheiro
	 */
	public void loadScores() throws IOException {
		// TODO implementar este método
		// este código deve desaparecer, está cá só para não dar erros
		addScore( "Sergio Barbosa",10000 );
		addScore( "Lucky Luke", 6000 );
		addScore( "Buffalo Bill", 5000 );
		addScore( "Wyatt Earp", 4000 );
		addScore( "Jesse James", 3000 );
		addScore( "Billy the Kid", 2000 );
		addScore( "David Crockett", 1800 );
		addScore( "Sitting Bull", 1600 );
		addScore( "Calamity Jane", 1400 );
		addScore( "Crazy Horse", 1200 );
		addScore( "Butch Cassidy", 1000 );
		addScore( "Wild Bill Hickok", 900 );
		addScore( "Annie Oakley", 800 );
		addScore( "John Wayne", 700 );
		addScore( "Blueberry", 600 );
		addScore( "Clint Eastwood", 500 );
		addScore( "Geronimo", 400 );
		addScore( "Doc Hollyday", 300 );
		addScore( "Joe Dalton", 200 );
		addScore( "Pocahontas", 100 );
	}
	
	/** Classifica uma pontuação indicando em que posição da
	 * lista ela deve ser colocada, ou -1 se não foi suficiente para atingir
	 * um lugar na mesma
	 * @param pontuacao a pontuação a classificar
	 * @return a posição onde ficou, ou -1 se não foi boa o suficiente
	 */
	public int classificarScore( int pontuacao ) {
		for( int i=0; i < pontuacoes.size(); i++ )
			if( pontuacao > pontuacoes.get(i).getPontuacao() )
				return i;
		return pontuacoes.size() >= MAX_SCORES? -1: pontuacoes.size();
	}

	/** Adiciona a pontuação à lista. A pontuação só é adicionada se 
	 * for suficientemente boa.
	 * @param nome nome do jogador que obteve a pontuação
	 * @param pontuacao pontuação obtida
	 */
	public void addScore( String nome, int pontuacao ) {
		int pos = classificarScore( pontuacao );
		if( pos == -1 )
			return;
		pontuacoes.add( pos, new Score(nome,pontuacao) );
		while( pontuacoes.size() > MAX_SCORES )
			pontuacoes.remove( pontuacoes.size()-1 );
	}
	
	/** Guarda a pontuação para o ficheiro de pontuações
	 * @throws IOException se alguma coisa correr mal na gravação do ficheiro
	 */
	public void saveScores() throws IOException {
		// TODO implementar este método
	}

	/** retorna a lista das pontuações
	 * @return a lista das pontuações
	 */
	public List<HighScoreHandler.Score> getScores() {
		return Collections.unmodifiableList( pontuacoes );
	}
}

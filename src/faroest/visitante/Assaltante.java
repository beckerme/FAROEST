package faroest.visitante;

import java.awt.Graphics2D;
import java.awt.Point;

import faroest.mundo.Porta;
import faroest.util.GeradorAleatorio;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

/** Representa um assaltante. Um assaltante irá disparar sobre
 * o jogador após algum tempo. Pode aparecer com as armas empunhadas
 * ou com as armas por sacar. Só pode ser morto quando as armas estão empunhadas.
 */
public class Assaltante extends VisitanteDefault {
	/** constantes para os estados possíveis deste visitante */
	private static final int ESPERA = 10;
	private static final int SACANDO = 11;
	private static final int SACOU = 12;
	private static final int DISPARO = 13;
	private static final int MORTO = 14;

	private int minSacar;   // tempo mínimo que demora a sacar
	private int maxSacar;   // tempo máximo que demora a sacar
	private int minDisparo; // tempo mínimo que demora a disparar (após sacar)
	private int maxDisparo; // tempo máximo que demora a disparar (após sacar)
	private long proxAto;   // quando vai fazer a próxima ação

	private long tempoSaque; // armazena o tempo em que sacou das armas

	private ComponenteAnimado imgSaida; // imagem de saida do visitante 
	// A imagem de saída é um "efeito especial",
	// que pode ser o dinheiro ou outra (num futuro)
    

	/** Cria um visitante Assaltante
	 * @param nome nome do visitante (usado para as imagens)
	 * @param pontos pontos que vale
	 * @param minSacar tempo mínimo que demora a sacar 
	 * @param maxSacar tempo máximo que demora a sacar (se 0, as armas já vêm empunhadas)
	 * @param minDisparo tempo mínimo que demora a disparar (após sacar)
	 * @param maxDisparo tempo máximo que demora a disparar (após sacar)
	 */
	public Assaltante( String nome, int pontos, int minSacar, int maxSacar, int minDisparo, 
			int maxDisparo) {
		super(nome, pontos);

		// ver se vem com as armas empunhadas ou não
		if( maxSacar > 0){  // por sacar
			setStatus( ESPERA );
			setImagem( nome + "_espera" );			
		}
		else {  // já sacadas
			setStatus( SACOU );
			setImagem( nome + "_sacada" );			
		}
		this.minSacar = minSacar;
		this.maxSacar = maxSacar;
		this.minDisparo = minDisparo;
		this.maxDisparo = maxDisparo;
	}

	/** método que diz ao visitante que a porta fechou
	 * @return a pontuação por esta ação
	 */
	public int fecharPorta() {
		return 0;
	}

	/** informa o visitante que a porta abriu
	 */
	public void portaAberta() {
		if( getStatus() == ESPERA )
			proxAto = GeradorAleatorio.proxTempo(minSacar, maxSacar);
		else {
			proxAto = GeradorAleatorio.proxTempo(minDisparo, maxDisparo);
			tempoSaque = System.currentTimeMillis();
		}
	}

	/** indica se o visitante permite que a porta feche
	 * @return true se a porta pode fechar
	 */
	public boolean podeFechar() {
		return getStatus() == MORTO && getImagem().numCiclosFeitos() > 0;
	}

	/** o visiatnte foi baleado 
	 * @return a pontuação obtida
	 */
	public int baleado() {
		if( getStatus() == MORTO )
			return 0;

		float pontuacao = 0;
		if( getStatus() < SACOU ){
			setImagem( getNome() + "_morte1" );
			fezAsneira("oops");
		} 
		else {
			setImagem( getNome() + "_morte2" );
			long tempo = (System.currentTimeMillis() - tempoSaque);
			if( tempo < 100 )
				pontuacao = getPontos();
			else if( tempo < 200 )
				pontuacao = getPontos() * 0.95f;
			else if( tempo < 400 )
				pontuacao = getPontos() * 0.8f;
			else if( tempo < 600 )
				pontuacao = getPontos() * 0.7f;
			else if( tempo < 800 )
				pontuacao = getPontos() * 0.6f;
			else if( tempo < 1200 )
				pontuacao = getPontos() * 0.5f;
			else if( tempo < 1500 )
				pontuacao = getPontos() * 0.4f;
			else
				pontuacao = getPontos() * 0.2f;
		}
		setStatus( MORTO );
		// retorna a pontuação arredondada
		System.out.println(getPontos());
		System.out.println(pontuacao);
		return (int)(pontuacao+0.5f);
	}

	/** efetua um ciclo do jogo
	 */
	public void atualizar() {
		if( getStatus() == ESPERA && fimEspera()){
			setStatus( SACANDO );
			setImagem( getNome() + "_saca");
		}
		else if( getStatus() == SACANDO && getImagem().numCiclosFeitos() > 0 ){
			setStatus( SACOU );
			setImagem ( getNome() + "_sacada" );
			tempoSaque = System.currentTimeMillis();
			proxAto = GeradorAleatorio.proxTempo(minDisparo, maxDisparo);
		}
		else if( getStatus() == SACOU && fimEspera() ){
			setStatus( DISPARO );
			fezAsneira("bang");
		}
	}

	/**
	 * desenha o visitante
	 * @param g ambiente gráfico onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		getImagem().desenhar(g);
		if( imgSaida != null )
			imgSaida.desenhar( g );
	}

	private boolean fimEspera(){
		return System.currentTimeMillis() >= proxAto;
	}

	/** Alguem fez asneira (matou o visitante ou deixou o visitante fazer asneira)
	 * @param nomeImg imagem do tipo de asneira
	 */
	private void fezAsneira( String nomeImg ){
		getPorta().getMundo().perdeNivel( nomeImg );	
	}

	/** cria um clone do visitante
	 * @return um visitante igual ao original
	 */
	public Assaltante clone() {
		return (Assaltante) super.clone();
	}

}

package faroest.visitante;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import faroest.mundo.Porta;
import faroest.util.GeradorAleatorio;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

public class Aleatorio extends VisitanteDefault {
	
	private static final int ENTRAR = 10;
	private static final int ESPERA = 11;
	private static final int SAIR = 12;
	
	

	private ComponenteAnimado extras[];
	private int minAberto, maxAberto;
	private double numAleatorio = Math.random();
	private ComponenteAnimado imgSaida; // imagem de saida do visitante 
	private long proxFecho; // quando vai fechar
	private ComponenteVisual extraSai;  // imagem de um extra a sair
	private boolean eAssaltante;
	
	public Aleatorio(String nome, int pontos, int numExtra, int minAberto, int maxAberto) {
		
		super(nome, pontos, numExtra);

		extras = new ComponenteAnimado[ numExtra];
		for( int i = 0; i < numExtra; i++){
			extras[i] = (ComponenteAnimado)ComponenteVisualLoader.getCompVisual( nome + "_extra" + i );
		}
		
		this.minAberto = minAberto;
		this.maxAberto = maxAberto;
		
		if(numAleatorio > 0.5) {
			eAssaltante = true;
			setStatus(ENTRAR);
			setImagem(getNome() + "_mata");
		} else {
			eAssaltante = false;
			setStatus(ENTRAR);
			setImagem(getNome() + "_deposita");
		}

	}
	

	public void portaAberta() {
		proxFecho = GeradorAleatorio.proxTempo(minAberto, maxAberto);
	}
	
	/** indica se o visitante permite que a porta feche
	 * @return true se a porta pode fechar
	 */
	@Override
	public boolean podeFechar() {
		return getStatus() == SAIR && getImagem().numCiclosFeitos() > 1;
	}
	
	/** método que diz ao visitante que a porta fechou
	 * @return a pontuação por esta ação
	 */
	@Override
	public int fecharPorta() {
		getPorta().setRecebeu( true );
		return getPontos();
	}
	
	
	public void desenhar( Graphics2D g ){
		getImagem().desenhar(g);
		if( imgSaida != null )
			imgSaida.desenhar( g );
		for( int i = 0; i < getnExtras(); i++){
			extras[i].desenhar( g );
		}
		if( extraSai != null ){
			extraSai.desenhar( g );
			if( extraSai.numCiclosFeitos() > 0 )
				extraSai = null;
		}
	}
	
	private boolean fimEspera(){
		return System.currentTimeMillis() >= proxFecho;
	}
	
	/** efetua um ciclo do jogo
	 */
	@Override
	public void atualizar() {
		if( getStatus() == ENTRAR && getImagem().numCiclosFeitos() > 0 ){
			setStatus( ESPERA );
		}
		else if( getStatus() == ESPERA && fimEspera() ){
			setStatus( SAIR );
		}
	}
	
	/** o visiatnte foi baleado 
	 * @return a pontuação obtida
	 */
	@Override
	public int baleado() {
		if( getStatus() == SAIR )
			return 0 ;
		
		if( temExtras() ){
			reduzExtra();
			return getPontos();
		}
		
		if(eAssaltante) {
			fezAsneira("boom");
			setStatus(SAIR);
			
			return 0;
		} else {
			setStatus(SAIR);
			setImagemSaida( "dinheiro" );
			
			return 0;
		}
	}
	
	
	/** Alguem fez asneira (matou o visitante ou deixou o visitante fazer asneira)
	 * @param nomeImg imagem do tipo de asneira
	 */
	private void fezAsneira( String nomeImg ){
		getPorta().getMundo().perdeNivel( nomeImg );	
	}
	
	/** define qual a imagem de saída. A imagem de saída é um "efeito especial",
	 * que pode ser o dinheiro ou outra (num futuro) 
	 * @param nomeImg o nome da imagem de saida
	 */
	private void setImagemSaida( String nomeImg ){
		imgSaida = (ComponenteAnimado)ComponenteVisualLoader.getCompVisual( nomeImg );
		Rectangle r = getImagem().getBounds();
		imgSaida.setPosicao( new Point( r.x+(r.width - imgSaida.getComprimento())/2 , r.y) );
	}
	
	/** retorna a imagem de saída
	 * @return  a imagem de saída
	 */
	protected ComponenteAnimado getImagemSaida(){
		return imgSaida;
	}
	
	/**
	 * Define a posição do visitante no jogo. A posição é dada em pixeis.
	 * @param point a posição do visitante
	 */
	@Override
	public void setPosicao(Point posicao) {
		getImagem().setPosicao( (Point)posicao.clone() );
		if( imgSaida != null )
			imgSaida.setPosicao( (Point)posicao.clone() );
		for( int i = 0; i < getnExtras(); i++){
			extras[i].setPosicao( (Point)posicao.clone() );
		}
	}

	/** cria um clone do visitante
	 * @return um visitante igual ao original
	 */
	@Override
	public Aleatorio clone() {
		
		Aleatorio v = (Aleatorio) super.clone();

		v.extras = new ComponenteAnimado[ extras.length ];
		for( int i=0; i < extras.length; i++ ){
			v.extras[i] = extras[i].clone();
		}
		
		return v;
	}
}

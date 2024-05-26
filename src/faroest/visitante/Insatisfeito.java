package faroest.visitante;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import faroest.util.GeradorAleatorio;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

public class Insatisfeito extends VisitanteDefault{
	
	private static final int ENTRAR = 10;
	private static final int ESPERA_ANTES = 11;
	private static final int PACIFICANDO = 12;
	private static final int ESPERA_APOS = 13;
	private static final int SAIR = 14;
	private static final int MORTO = 15;
	
	private int minAberto;  // mínimo de tempo que mantém a porta aberta
	private int maxAberto;  // máximo de tempo que mantém a porta aberta 
	private long proxFecho; // squando vai fechar  

	
	private ComponenteVisual extraSai;  // imagem de um extra a sair
	private ComponenteAnimado imgSaida; // imagem de saida do visitante 
	// A imagem de saída é um "efeito especial",
	// que pode ser o dinheiro ou outra (num futuro)
	
	private ComponenteAnimado extras[]; // imagens dos extras
	
	public Insatisfeito(String nome, int pontos, int nExtras, int minAberto, int maxAberto) {
		
		super(nome, pontos, nExtras);
		
		extras = new ComponenteAnimado[ nExtras ];
		for( int i = 0; i < nExtras; i++){
			extras[i] = (ComponenteAnimado)ComponenteVisualLoader.getCompVisual( nome + "_extra" + i );
		}
		
		this.minAberto= minAberto;
		this.maxAberto = maxAberto;
		
		setStatus( ENTRAR );
		setImagem( nome + "_zangado" );
		
	}
	
	public void portaAberta() {
		proxFecho = GeradorAleatorio.proxTempo(minAberto, maxAberto);
	}
	
	public int baleado() {
		if(getStatus() == MORTO) {
			return 0;
		}
		if( temExtras() ){
			reduzExtra();
			return getPontos();
		}
		
		setImagem( getNome() + "_pacificando" );
		setStatus(PACIFICANDO);
		return 0;
	}
	
	private boolean fimEspera(){
		return System.currentTimeMillis() >= proxFecho;
	}
	
	public void atualizar() {
		if( getStatus() == ENTRAR && getImagem().numCiclosFeitos() > 0 ){
			setStatus( ESPERA_ANTES );
		}
		
		else if( getStatus() == PACIFICANDO && getImagem().numCiclosFeitos() > 0 ){
			setStatus(ESPERA_APOS);
			setImagem ( getNome() + "_espera" );
			setImagemSaida( "dinheiro" );
		}
		
		else if( getStatus() == ESPERA_ANTES && fimEspera() ){
			setStatus( MORTO );
			fezAsneira("boom");
		}
		
		else if(getStatus() == ESPERA_APOS && fimEspera()) {
			setStatus(SAIR);
			setImagem(getNome() + "_adeus");
		}
		
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

	private void fezAsneira( String nomeImg ){
		getPorta().getMundo().perdeNivel( nomeImg );	
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
	
	public Insatisfeito clone() {
		
		Insatisfeito v = (Insatisfeito) super.clone();
		
		v.extras = new ComponenteAnimado[ extras.length ];
		for( int i=0; i < extras.length; i++ ){
			v.extras[i] = extras[i].clone();
		}
		return v;
	}
	
}

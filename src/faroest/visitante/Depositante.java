package faroest.visitante;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import faroest.mundo.Porta;
import faroest.util.GeradorAleatorio;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

/** Representa um depositante. Um depositante irá depositar o dinheiro
 * quando fechar a porta. Pode ter vários extras que podem ser removidos.
 * Os extras não precisam de ser removidos para ele depositar mas valem pontos. 
 */
public class Depositante extends VisitanteDefault {
	/** constantes para os estados possíveis deste visitante */
	private static final int OLA = 10;
	private static final int ESPERA = 11;
	private static final int ADEUS = 12;
	private static final int MORTO = 13;
	
	private int minAberto;  // mínimo de tempo que mantém a porta aberta
	private int maxAberto;  // máximo de tempo que mantém a porta aberta 
	private long proxFecho; // squando vai fechar
	
	private ComponenteVisual extraSai;  // imagem de um extra a sair
	private ComponenteAnimado imgSaida; // imagem de saida do visitante 
	// A imagem de saída é um "efeito especial",
	// que pode ser o dinheiro ou outra (num futuro)
	
	private ComponenteAnimado extras[]; // imagens dos extras
	private int nExtras;                // número de extras que ainda tem     

	/** Cria um visitante Depositante
	 * @param nome nome do visitante (usado para as imagens)
	 * @param pontos pontos que vale
	 * @param nExtras quantos extras tem
	 * @param minAberto mínimo de tempo que mantém a porta aberta
	 * @param maxAberto máximo de tempo que mantém a porta aberta
	 */
	public Depositante( String nome, int pontos, int nExtras, int minAberto, int maxAberto ) {
		super(nome, pontos, nExtras);
		this.nExtras = nExtras;
		extras = new ComponenteAnimado[ this.nExtras ];
		for( int i = 0; i < this.nExtras; i++){
			extras[i] = (ComponenteAnimado)ComponenteVisualLoader.getCompVisual( nome + "_extra" + i );
		}
		
		setStatus( OLA );
		setImagem( nome + "_ola" );
		this.minAberto= minAberto;
		this.maxAberto = maxAberto;
	}
	
	
	/** informa o visitante que a porta abriu
	 */
	@Override
	public void portaAberta() {
		proxFecho = GeradorAleatorio.proxTempo(minAberto, maxAberto);
	}
	
	/** indica se o visitante permite que a porta feche
	 * @return true se a porta pode fechar
	 */
	@Override
	public boolean podeFechar() {
		return getStatus() == ADEUS && getImagem().numCiclosFeitos() > 1;
	}
	
	/** método que diz ao visitante que a porta fechou
	 * @return a pontuação por esta ação
	 */
	@Override
	public int fecharPorta() {
		getPorta().setRecebeu( true );
		return getPontos();
	}
	
	/** Indica se ainda tem extras
	 * @return true, se ainda tem extras
	 */
	public boolean temExtras(){
		return nExtras > 0;
	}
	
	/** o visiatnte foi baleado 
	 * @return a pontuação obtida
	 */
	@Override
	public int baleado() {
		if( getStatus() == MORTO )
			return 0 ;
		
		if( temExtras() ){
			reduzExtra();
			return getPontos();
		}
		
		setImagem( getNome() + "_morte" );
		fezAsneira("oops");
		setStatus(MORTO);
		return 0;
	}
	
	/** efetua um ciclo do jogo
	 */
	@Override
	public void atualizar() {
		if( getStatus() == OLA && getImagem().numCiclosFeitos() > 0 ){
			setStatus( ESPERA );
			System.out.println(getNome());
			setImagem( getNome() + "_espera");
		}
		else if( getStatus() == ESPERA && fimEspera() ){
			setStatus( ADEUS );
			setImagem ( getNome() + "_adeus" );
			setImagemSaida( "dinheiro" );
		}
	}
	
	public void reduzExtra(){
		nExtras--;
		extraSai = ComponenteVisualLoader.getCompVisual( getNome() + "_extra"+nExtras+"_sai"); 
		extraSai.setPosicao( (Point)getImagem().getPosicao().clone() );
	}

	
	private boolean fimEspera(){
		return System.currentTimeMillis() >= proxFecho;
	}
	
	/**
	 * desenha o visitante
	 * @param g ambiente gráfico onde desenhar
	 */
	@Override
	public void desenhar( Graphics2D g ){
		getImagem().desenhar(g);
		if( imgSaida != null )
			imgSaida.desenhar( g );
		for( int i = 0; i < nExtras; i++){
			extras[i].desenhar( g );
		}
		if( extraSai != null ){
			extraSai.desenhar( g );
			if( extraSai.numCiclosFeitos() > 0 )
				extraSai = null;
		}
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
		for( int i = 0; i < nExtras; i++){
			extras[i].setPosicao( (Point)posicao.clone() );
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
	
	/** retorna a imagem de saída
	 * @return  a imagem de saída
	 */
	protected ComponenteAnimado getImagemSaida(){
		return imgSaida;
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
	@Override
	public Depositante clone() {
		
		Depositante v = (Depositante) super.clone();

		v.extras = new ComponenteAnimado[ extras.length ];
		for( int i=0; i < extras.length; i++ ){
			v.extras[i] = extras[i].clone();
		}
		
		return v;
	}
}

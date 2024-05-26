package faroest.mundo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import faroest.util.GeradorAleatorio;
import faroest.visitante.*;
import prof.jogos2D.image.ComponenteMultiAnimado;

/**
 * Representa uma porta no jogo.
 */
public class Porta {
	// estados possíveis para a porta
	public static final int FECHADA = 0;
	public static final int ABRINDO = 1;
	public static final int ABERTA = 2;
	public static final int FECHANDO = 3;
	public static final int BLOQUEADA = 4;

	private int minFechada;          // mínimo de tempo que está fechada
	private int maxFechada;          // máximo de tempo que está fechada
	private boolean recebeu = false; // indica se já recebeu dinheiro 
	private int estado = FECHADA;    // estado atual da porta
	private long proxAbertura;       // tempo programado para a próxima abertura
	private ComponenteMultiAnimado img; // imagem da porta
	private Rectangle soleira;       // representa a soleira da porta de modo
	// a centrar as imagens dos visitantes 
	private Mundo mundo;             // mundo a que a porta está associada

	// TODO ZFEITO ter uma variável para cada tipo de visitante NÃO é uma boa ideia
	private Visitante visita = null; // quem é o depositante que está na porta 
	//private Assaltante visitaAss = null; // quem é o assaltante que está na porta

	/**
	 * Construtor da porta
	 * @param banco banco associado
	 * @param img imagem com as várias animações da porta 
	 * @param soleira retângulo que contém as dimensões da soleira da porta
	 * @param minFechada mínimo de tempo entre aberturas da porta
	 * @param maxFechada máximo de tempo entre aberturas da porta
	 */
	public Porta(Mundo banco, ComponenteMultiAnimado img, Rectangle soleira, int minFechada, int maxFechada) {
		this.mundo = banco;
		this.img = img;
		img.setPosicao( new Point() );
		this.minFechada = minFechada;
		this.maxFechada = maxFechada;
		this.soleira = soleira;
		programarAbertura();
	}

	/**
	 * atualiza este elemento
	 * @return a pontuação obtida neste ciclo
	 */
	public int atualizar(){
		// inicializa pontuação a 0
		int pts = 0;

		if( estaBloqueada() )
			return pts;

		switch( estado ){
		case FECHADA:
			// se está fechada, mas já passa da hora de abertura, abre
			if( System.currentTimeMillis() > proxAbertura ){
				estado = ABRINDO;
				img.setAnim( ABRINDO );
				img.setFrameNum( 0 );
				mundo.portaAbrindo( this );
			}
			break;
		case ABRINDO:
			// se está abrindo e já completou a animação da abertura
			if( img.numCiclosFeitos() > 0 ){
				estado = ABERTA;
				img.setAnim( ABERTA );
				img.setFrameNum( 0 );
				// TODO ZFEITO Este é um problema de ter duas variáveis (como será com 5??)
				if( visita != null )
					visita.portaAberta();
			}
			break;
		case ABERTA:
			// se está aberta tem de mandar atualizar também o visitante
			// TODO ZEITO Este é um problema de ter duas variáveis (será que vamos ter mais que duas?)
			if( visita != null )
				visita.atualizar();
			// se está aberta ver se pode fechar
			// TODO ZFEITO Este é um problema de ter duas variáveis (será que vamos ter mais que duas?)
			boolean pode = visita.podeFechar();  
			if( pode ){
				estado = FECHANDO;
				img.setAnim( FECHANDO );
				img.setFrameNum( 0 );
				// como se vai fechar a porta isso pode dar pontos
				// TODO ZFEITO Este é um problema de ter duas variáveis (será que vamos ter mais que duas?)
				pts += visita.fecharPorta();
			}
			break;
		case FECHANDO:
			// se está a fechar e a imagem já completou a animação
			if( img.numCiclosFeitos() > 0 ){
				estado = FECHADA;
				img.setAnim( FECHADA );
				img.setFrameNum( 0 );
				// TODO ZFEITO Este é um problema de ter duas variáveis (e quando forem 5?)
				visita = null;
				programarAbertura();
			}
			break;
		}
		return pts;
	}

	/**
	 * processa um disparo na porta
	 * @return os pontos obtidos com o disparo
	 */
	public int disparo() {
		// TODO ZFEITO Este é um problema de ter duas variáveis (será que dá para usar só uma?)
		if( estado == ABERTA && visita != null )
			return  visita.baleado();
		return 0;
	}

	/**
	 * desenha a porta no ambiente gráfico especificado 
	 * @param g ambiente gráfico onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		// se tiver uma visita, desenhá-la também
		// TODO ZFEITO Este é um problema de ter duas variáveis (e já vimos outros e ainda há mais!!)
		if( visita != null )
			visita.desenhar(g);
		else if( visita != null )
			visita.desenhar(g);
		// desenhar a imagem da porta
		img.desenhar(g);
	}

	/**
	 * define a posiçao da porta
	 * @param p posição
	 */
	public void setPosicao( Point p ){
		img.setPosicao( p );
		//se tem visita é preciso também alterar a posição desta
		// TODO ZFEITO Este é um problema de ter duas variáveis (como será com 5??)
		if( visita != null ){
			Point pv = (Point)img.getPosicao().clone();
			pv.translate( soleira.x, soleira.y);
			if( visita != null )
				visita.setPosicao( pv );
			else
				visita.setPosicao( pv );
		}
	}

	/**
	 * indica se a porta está aberta
	 * @return true se a porta está aberta
	 */
	public boolean estaAberta(){
		return estado != FECHADA && estado != BLOQUEADA;
	}

	/**
	 * indica se a porta está bloqueada
	 * @return true se a porta está bloqueada
	 */
	public boolean estaBloqueada() {
		return estado == BLOQUEADA;
	}


	/**
	 * bloqueia/desbloqueia a porta
	 * @param b estado do bloqueio da porta
	 */
	public void setBloqueada( boolean b ){
		estado = b? BLOQUEADA: FECHADA;
		img.setAnim( FECHADA );
		img.setFrameNum( 0 );
		programarAbertura();
	}

	/**
	 * Atribui um depositante à porta
	 * @param visitante nova visita
	 */
	public void setVisitante(Visitante visitante) {
		// TODO ZFEITO este método e o seguinte são muito semelhantes!

		this.visita = visitante;

		//centrar a visita na soleira da porta
		Point pv = (Point)img.getPosicao().clone();
		pv.translate( soleira.x + (soleira.width - visitante.getImagem().getComprimento())/2, soleira.y + soleira.height - visitante.getImagem().getAltura());
		visita.setPosicao( pv );

		// indicar à visita em que porta está
		visita.setPorta( this );
	}

	/**
	 * indica em que posição está a porta
	 * @return a posição da porta
	 */
	public Point getPosicao() {
		return img.getPosicao();
	}

	/**
	 * indica se a porta já recebeu dinheiro
	 * @return true se a porta já recebeu dinheiro
	 */
	public boolean jaRecebeu() {
		return recebeu;
	}

	/**
	 * define o estado de receber dinheiro
	 * @param b o estado de receber dinheiro
	 */
	public void setRecebeu(boolean b) {
		recebeu = b;
	}

	/**
	 * devolve o mundo associado à porta
	 * @return o mundo associado à porta
	 */
	public Mundo getMundo() {
		return mundo;
	}

	/** calcula quando vai ser a próxima abertura
	 */
	private void programarAbertura() {
		proxAbertura = System.currentTimeMillis() + GeradorAleatorio.nextInt( minFechada, maxFechada );
	}
}

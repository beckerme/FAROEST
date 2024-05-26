package faroest.mundo;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import faroest.visitante.*;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

/**
 * Informações sobre todo o mundo: portas, fundo, etc
 * Como o mundo vai ser circular, vai ser dividido em partes,
 * em que, no máximo, duas partes estão visíveis
 */
public class Mundo {

	/** características do mapa */
	private int comprimento, altura;      // comprimento e altura, em pixeis, do mapa

	// as partes que constituem o mundo
	private ParteMundo partes[];
	private int parteDim; // a dimensão de cada parte
	
	// as partes visiveis
	private ParteMundo pVisivel1, pVisivel2;
	
	// qual a coordenada do mundo colocada no lado esquerdo do écran
	private float ecraX = 0;

	// variáveis para rodar o mundo
	private int direcao;
	private float deslocamento;
	private boolean rodando;
	private int numIntervalos;
	private int portaVisivelIndex;

	/** número de  de portas visíveis */
	private static final int NUM_PORTAS_VISIVEIS = 3;
	/** número de deslocamentos para completar um ciclo de rotação */
	private static final int DESLOCAMENTOS_RODAR = 9;

	/** as portas deste nível */
	private Porta portas[];
	/** as portas visiveis num dado momento */
	private Porta portasVisiveis[] = new Porta[ NUM_PORTAS_VISIVEIS ];
	
	/** as possíveis visitas neste nível */
	// TODO ZFEITO Será que esta é uma boa maneira de resolver o problema? Não me parece...
	private ArrayList<Visitante> possiveisVisitantes = new ArrayList<Visitante>();
//	private ArrayList<Assaltante> possiveisAssaltantes = new ArrayList<Assaltante>();

	/** a pontuação por completar o nível */
	private int pontos;
	
	/** a animação final quando perde o nível */
	private ComponenteVisual animacaoFinal;
	
	/** quanto tempo deve a animação final ficar visível */
	private long delayFinal;
	
	/** indica se o nível já terminou */
	private boolean terminouRound = false;

	/** criar um mundo com determinadas características
	 * @param fundo   elemento visual para o fundo
	 * @param imgPorta imagem da porta
	 * @param nPortas número de portas que o mundo tem (tem de ser múltiplo de 3)
	 * @param pontos a pontuação por passar o nível
	 */
	public Mundo(BufferedImage fundo, BufferedImage imgPorta, int nPortas, int pontos) {
		Objects.requireNonNull( fundo );
		if( nPortas <= 0 || nPortas % 3 != 0 )
			throw new IllegalArgumentException( "nPortas  (" + nPortas +  ") tem de ser múltiplo de 3" );
		if( pontos < 0 )
			throw new IllegalArgumentException( "pontos  (" + pontos +  ") tem de ser positivo" );
		
		this.pontos = pontos;
		int nPartes = nPortas/3;
		comprimento = fundo.getWidth();
		altura = fundo.getHeight();
		parteDim = comprimento / nPartes;
		partes = new ParteMundo[ nPartes ];
		
		for( int i = 0; i < nPartes; i++ ){
			BufferedImage img = fundo.getSubimage( parteDim * i, 0, parteDim, altura);
			ComponenteSimples comp = new ComponenteSimples( img );
			comp.setPosicao( new Point(0, 0) );
			partes[i] = new ParteMundo( comp, parteDim * i );
		}
		
		int distEntrePortas = parteDim / NUM_PORTAS_VISIVEIS;	
		portas = new Porta[ nPortas ];
		for( int i=0; i < portas.length; i++ ){
			ComponenteMultiAnimado compPorta = new ComponenteMultiAnimado(null, imgPorta, 4, 5, 4);
			portas[i] = new Porta( this, compPorta, new Rectangle(100, 20, 200, 368), 1000, 5000 );
			Point pos = new Point( distEntrePortas * i, 85 );
			addPorta( pos, portas[i] );
			portas[i].setBloqueada( true );
		}
		
		deslocamento = distEntrePortas / DESLOCAMENTOS_RODAR; 
		portaVisivelIndex = 0;
		determinarPartesVisiveis();
		atualizarPortasVisiveis();
		desbloquearPortasVisiveis();
	}
	
	/**
	 * determina as partes visiveis
	 */
	private void determinarPartesVisiveis() {
		int p1 = (int)ecraX / parteDim;
		int p2 = p1 + 1 < partes.length? p1 + 1: 0;
		pVisivel1 = partes[p1];
		pVisivel2 = partes[p2];		
	}

	/**
	 * Desenhar todo o mundo
	 * @param g ambiente onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		// desenhar a primeira parte, mas primeiro transladar
		// o desenho para a posição correta
		g.translate( -ecraX + pVisivel1.getXref(), 0);
		pVisivel1.desenhar( g );
		
		// desenhar a segunda parte
		g.translate( parteDim, 0);
		pVisivel2.desenhar( g );

		// voltar a por tudo a zero
		g.translate( ecraX - pVisivel1.getXref() - parteDim, 0);
		
		// desenhar a animação final, se houver
		if( animacaoFinal != null ){
			animacaoFinal.desenhar( g );
			if( animacaoFinal.numCiclosFeitos() > 0 && delayFinal <= System.currentTimeMillis() ){
				terminouRound = true;
			}
		}

	}
	
	/** indica se o nível já terminou
	 * @return true, se o nível já terminou
	 */
	public boolean terminouRound() {
		return terminouRound;
	}
	
	/**
	 * faz o reset, isto é, coloca o mundo na posição 0
	 */
	public void reset(){
		deslocar( ecraX );
		terminouRound = false;
		portaVisivelIndex = 0;
		animacaoFinal = null;
		atualizarPortasVisiveis();
	}
	
	/**
	 * adicionar uma porta num dado ponto
	 * @param p ponto, pixel, onde adicionar a porta
	 * @param porta a porta a adicionar
	 */
	public void addPorta( Point p, Porta porta ){
		int parte = p.x / parteDim;
		porta.setPosicao( new Point(p.x-parte*parteDim, p.y) );
		partes[ parte ].addPorta( porta );
	}
	
	/**
	 * atualizar o mundo e todos os seus constituintes
	 * @return a pontuação obtida neste ciclo
	 */
	public int atualizar() {
		// se já tem animação final, não atualizar nada
		if( animacaoFinal != null )
			return 0;
		
		if( rodando )
			rodar();
		// assumindo que os elementos não se podem deslocar de uma parte para outra
		// basta atualizar as partes visiveis
		int pts = pVisivel1.atualizar();
		pts += pVisivel2.atualizar();
		return pts;
	}

	/** inicia um ciclo de rotação para a direita 
	 */
	public void rodarDir() {
		comecarRodar( 1 );
	}
	
	/** inicia um ciclo de rotação para a esquerda 
	 */
	public void rodarEsq() {
		comecarRodar( -1 );
	}

	/** Inicia um movimento de rotação na direção indicada 
	 * @param direcao 1 para rodar para a direita e
	 *                -1 para rodar para a esquerda
	 */
	private void comecarRodar( int direcao ) {
		if( rodando || !portasFechadas() ) return;
		rodando = true;
		this.direcao = direcao;
		numIntervalos = DESLOCAMENTOS_RODAR;
		bloquearPortasVisiveis();
		rodar();
	}
	
	/** roda o mundo na direção estabelecida
	 */
	private void rodar() {
		ecraX += direcao * deslocamento;
		if( ecraX >= comprimento )
			ecraX -= comprimento;
		else if( ecraX < 0 )
			ecraX += comprimento;
		
		numIntervalos--;
		if( numIntervalos <= 0 ) {
			pararRodar();
		}
		
		determinarPartesVisiveis();
	}

	/** parar de rodar o mundo
	 */
	private void pararRodar() {
		rodando = false;
		if( direcao < 0 ) {
			portaVisivelIndex--;
			if( portaVisivelIndex < 0 )
				portaVisivelIndex = portas.length-1;
		}
		else {
			portaVisivelIndex++;
			if( portaVisivelIndex >= portas.length )
				portaVisivelIndex = 0;
		}		
		atualizarPortasVisiveis();
		desbloquearPortasVisiveis();
	}
	
	/** atualiza as portas visíveis */
	private void atualizarPortasVisiveis() {
		for( int i=0; i < NUM_PORTAS_VISIVEIS; i++ )
			portasVisiveis[i] = portas[ idxPortaVisivel(i) ];		
	}

	/** retorna o índice correto da iª porta visível
	 * @param i qual o índice da porta visível
	 * @return o índice no array de portas global
	 */
	private int idxPortaVisivel(int i) {
		return (portaVisivelIndex + i)%portas.length;
	}
	
	/**
	 * deslocar o mundo em alguns pixeis
	 * @param dx deslocamento horizontal a aplicar
	 */
	public void deslocar( float dx ){
		ecraX -= dx;
		if( ecraX >= comprimento )
			ecraX -= comprimento;
		else if( ecraX < 0 )
			ecraX += comprimento;
		determinarPartesVisiveis();
	} 

	/** método chamado quando uma porta está a abrir
	 * @param porta a porta que se está a abrir
	 */
	public void portaAbrindo(Porta porta) {
		// para saber qual o próximo visitante somar tudo
		int idx = ThreadLocalRandom.current().nextInt(possiveisVisitantes.size());
		// TODO ZFEITO arranjar uma maneira mais simples (como será com 5 visitantes?)
		// para decidir se è assaltante ou depositante, ver o valor gerado
		if( idx < possiveisVisitantes.size() )
			porta.setVisitante( possiveisVisitantes.get(idx ).clone() );
	}

	/** bloqueia as portas visívies, isto é,
	 * estas já não se poderão abrir
	 */
	public void bloquearPortasVisiveis() {
		for( Porta p : portasVisiveis )
			p.setBloqueada( true );
	}
	
	/** desbloqueia as portas visíveis, isto é,
	 * estas já se poderão abrir
	 */
	public void desbloquearPortasVisiveis() {
		for( Porta p : portasVisiveis )
			p.setBloqueada( false );
	}

	/** indica se todas as portas visíveis estão fechadas
	 * @return true, se estiverem todas fechadas
	 */
	private boolean portasFechadas(){
		for( Porta p : portasVisiveis )
			if( p.estaAberta() )
				return false;
		return true;
	}

	/** retorna as portas desde nível 
	 * @return  as portas desde nível
	 */
	public Porta[] getPortas() {
		return portas.clone();
	}
	
	/** retona as portas visíveis neste momento 
	 * @returnas portas visíveis neste momento
	 */
	public Porta[] getPortasVisiveis() {
		return portasVisiveis.clone();
	}

	/** define qual a animação final que deve ser apresentada antes de terminar o nível
	 * @param anim a animação de fim de nível
	 * @param delayFinal quanto tempo deve a animação ser apresentada
	 */
	public void setAnimacaoFinal(ComponenteVisual anim, int delayFinal) {
		animacaoFinal = anim;
		this.delayFinal = System.currentTimeMillis() + delayFinal;
	}

	/** adiciona um possível assaltante
	 * @param aquele o visitante que pode aparecer numa das portas
	 */
	public void addPossivelVisitante( Visitante aquele ) {
		// TODO ZFEITO ter estes métodos semelhantes repetidos NÃO é uma boa solução
		possiveisVisitantes.add( aquele );
	}


	/** Fizeram asneira, agora perdem o nível
	 * @param nomeImg nome da imagem para indicar a asneira que se fez
	 */
	public void perdeNivel(String nomeImg) {
		ComponenteVisual imgFim = ComponenteVisualLoader.getCompVisual( nomeImg );
		imgFim.setPosicao( new Point( (parteDim - imgFim.getComprimento())/2 ,
				                      (altura - imgFim.getAltura())/2 ) );
		setAnimacaoFinal( imgFim, 2000 );	
	}
	
	/** retorna a pontuação por terminar o nível
	 * @return a pontuação por terminar o nível
	 */
	public int getPontos() {
		return pontos;
	}
}

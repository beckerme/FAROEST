package faroest.visitante;

import java.awt.Graphics2D;
import java.awt.Point;

import faroest.mundo.Porta;
import faroest.util.GeradorAleatorio;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

public class VisitanteDefault implements Visitante, Cloneable {

	private int minAberto;  // mínimo de tempo que mantém a porta aberta
	private int maxAberto;  // máximo de tempo que mantém a porta aberta 
	private long proxFecho; // quando vai fechar

	/** imagem representativa do visitante, num dado momento */
	private ComponenteVisual img;   
	private String nome; // o nome do visitante (usado nas imagens)
	private int status;  // o estado atual do visitante
	private int pontos;  // quanto pontos vale

	private ComponenteVisual extraSai;  // imagem de um extra a sair
	private ComponenteAnimado imgSaida; // imagem de saida do visitante 
	// A imagem de saída é um "efeito especial",
	// que pode ser o dinheiro ou outra (num futuro)

	private ComponenteAnimado extras[]; // imagens dos extras

	private int nExtras;  // número de extras que ainda tem

	private Porta porta;  // a porta onde está  

	// Visitante com extras
	public VisitanteDefault(String nome, int pontos) {
		this.nome = nome;
		this.pontos = pontos;
	}
	
	public VisitanteDefault(String nome, int pontos, int nExtras) {
		this.nome = nome;
		this.pontos = pontos;
		this.nExtras = nExtras;
	
	}

	@Override
	public void portaAberta() {
		proxFecho = GeradorAleatorio.proxTempo(minAberto, maxAberto);
	}

	@Override
	public boolean podeFechar() {
		return false;
	}
	
	@Override
	public int fecharPorta() {
		porta.setRecebeu( true );
		return getPontos();
	}

	@Override
	public int baleado() {
		return 0;
	}

	@Override
	public void atualizar() {

	}

	@Override
	public void desenhar(Graphics2D g) {

	}

	/** retorna a imagem associada
	 * @return a imagem associada
	 */
	public ComponenteVisual getImagem() {
		return img;
	}

	/** define a imagem que representa o visitante
	 * @param nome o nome da imagem
	 */
	public void setImagem(String nome) {
		Point p = img != null? img.getPosicao() : null;
		img = ComponenteVisualLoader.getCompVisual( nome );
		img.setPosicao( p );
	}

	/**
	 * Define a posição do visitante no jogo. A posição é dada em pixeis.
	 * @param point a posição do visitante
	 */
	public void setPosicao(Point posicao) {
		img.setPosicao( (Point)posicao.clone() );
		if( imgSaida != null )
			imgSaida.setPosicao( (Point)posicao.clone() );
	}

	/** retona o nome do visitante
	 * @return o nome do visitante
	 */
	@Override
	public String getNome() {
		return nome;
	}
	
	/** Define o nome do visitante
	 * @param nome novo nome
	 */
	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	/** Coloca o visitante numa porta
	 * @param p a porta onde o visitante aparece
	 */
	@Override
	public void setPorta(Porta p) {
		porta = p;
	}
	
	/** retorna a porta onde o visitante está 
	 * @return a porta onde o visitante está
	 */
	@Override
	public Porta getPorta() {
		return porta;
	}
	
	/** Retorna o número de pontos que vale
	 * @return o número de pontos que vale
	 */
	@Override
	public int getPontos() {
		return pontos;
	}
	
	public void setPontos(int pontos) {
		this.pontos = pontos;
	}

	/** retorna o status atual
	 * @return  o status atual
	 */
	public int getStatus() {
		return status;
	}

	/** muda o status do visitante
	 * @param status o novo status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public ComponenteAnimado[] getExtras() {
		return extras;
	}

	public void setExtras(int i, ComponenteAnimado c) {
		this.extras[i] = c;
	}
	
	public void setNextras(int nExtras) {
		this.nExtras = nExtras;
	}

	/** retorna o número de extras que ainda possui
	 * @return o número de extras que ainda possui
	 */
	public int getnExtras() {
		return nExtras;
	}

	/** remove um extra
	 */
	public void reduzExtra(){
		nExtras--;
		extraSai = ComponenteVisualLoader.getCompVisual( nome + "_extra"+nExtras+"_sai"); 
		extraSai.setPosicao( (Point)getImagem().getPosicao().clone() );
	}

	/** Indica se ainda tem extras
	 * @return true, se ainda tem extras
	 */
	public boolean temExtras(){
		return nExtras > 0;
	}

	public VisitanteDefault clone() {
		try {
			VisitanteDefault v = (VisitanteDefault) super.clone();
			if( img != null )
				v.img = img.clone();
			return v;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}




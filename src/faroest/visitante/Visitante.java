package faroest.visitante;

import java.awt.Graphics2D;
import java.awt.Point;

import faroest.mundo.Porta;
import prof.jogos2D.image.ComponenteVisual;

public interface Visitante {

	/** informa o visitante que a porta abriu
	 */
	void portaAberta();

	/** indica se o visitante permite que a porta feche
	 * @return true se a porta pode fechar
	 */
	boolean podeFechar();

	/** método que diz ao visitante que a porta fechou
	 * @return a pontuação por esta ação
	 */
	int fecharPorta();

	/** o visiatnte foi baleado 
	 * @return a pontuação obtida
	 */
	int baleado();

	/** efetua um ciclo do jogo
	 */
	void atualizar();

	/**
	 * desenha o visitante
	 * @param g ambiente gráfico onde desenhar
	 */
	void desenhar(Graphics2D g);

	/** retorna a imagem associada
	 * @return a imagem associada
	 */
	ComponenteVisual getImagem();

	/** define a imagem que representa o visitante
	 * @param nome o nome da imagem
	 */
	void setImagem(String nome);

	/**
	 * Define a posição do visitante no jogo. A posição é dada em pixeis.
	 * @param point a posição do visitante
	 */
	void setPosicao(Point posicao);

	/** retona o nome do visitante
	 * @return o nome do visitante
	 */
	String getNome();

	/** Define o nome do visitante
	 * @param nome novo nome
	 */
	void setNome(String nome);

	/** Coloca o visitante numa porta
	 * @param p a porta onde o visitante aparece
	 */
	void setPorta(Porta p);

	/** retorna a porta onde o visitante está 
	 * @return a porta onde o visitante está
	 */
	Porta getPorta();

	/** Retorna o número de pontos que vale
	 * @return o número de pontos que vale
	 */
	int getPontos();

	/** cria um clone do visitante
	 * @return um visitante igual ao original
	 */
	Visitante clone();

}
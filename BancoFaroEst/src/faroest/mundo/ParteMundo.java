package faroest.mundo;

import java.awt.Graphics2D;
import java.util.ArrayList;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Representa uma parte do mundo
 */
public class ParteMundo {
	/** imagem para o fundo */
	private ComponenteVisual fundo;
	
	/** coordenada x que serve de referência para a posição da parte */
	private int xref;
	
	/** lista com as portas presentes em cada parte */
	private ArrayList<Porta> portas = new ArrayList<Porta>();
	
	/**
	 * Cria uma parte do mundo
	 * @param fundo imagem para o fundo
	 * @param xref coredenada do início da parte
	 */
	public ParteMundo(ComponenteVisual fundo, int xref){
		this.fundo = fundo;
		this.xref = xref;
	}

	/**
	 * devolve a coordenada de referência desta parte
	 * @return a coordenada de referência desta parte
	 */
	public int getXref() {
		return xref;
	}
	
	/**
	 * Desenhar toda a parte
	 * @param g ambiente onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		// desenhar a imagem de fundo
		fundo.desenhar( g );
		
		for( Porta p : portas )
			p.desenhar(g);
	}

	/**
	 * adiciona uma porta à parte
	 * @param p porta a adicionar
	 * @return se adicionou a porta
	 */
	public boolean addPorta(Porta p) {
		return portas.add(p);
	}

	/**
	 * atualiza os elementos presentes nesta parte
	 * @return a pontuação obtida neste processamento
	 */
	public int atualizar() {
		int pts = 0;
		for( Porta p : portas )
			pts += p.atualizar();
		return pts;
	}
}

package faroest.app;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

/** Classe responsável pela leitura do ficheiro de relação de imagens
 */
public class ImagensReader {
	
	/**
	 * método para ler o ficheiro de nível
	 * @param level número do nivel
	 * @return o Nivel criado
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static String[] lerImagens( String file ) throws FileNotFoundException, IOException{
		BufferedReader in  = new BufferedReader( new FileReader( file ) );
		// saber quantas personagens são suportadas
		int numPersonagens = Integer.parseInt( in.readLine() );
		String personagens[] = new String[numPersonagens];
		for( int i = 0; i < numPersonagens; i++ ) {
			personagens[i] = in.readLine();
		}
		
		// ler as imagens
		String linha;
		while( (linha = in.readLine()) != null ){
			if( linha.isBlank() ) continue;
			String info[] = linha.split("=");
			String nome = info[0].trim();
			String infoImg[] = info[1].trim().split("\t");
			ComponenteVisual cv = lerComponenteVisual(infoImg, 0);
			ComponenteVisualLoader.store( nome, cv );
			//System.out.println( nome + "= " + cv);
		}
		in.close();
		return personagens;
	}
	
	/** leitura de um componente visual
	 * @param info informação da linha
	 * @param idx índice a partir do qual está presente a info do componente
	 * @return o componente visual criado
	 * @throws IOException
	 */
	private static ComponenteVisual lerComponenteVisual(String[] info, int idx ) throws IOException {
		switch( info[idx] ) {
		case "CS":  return criarComponenteSimples( info, idx+1 );
		case "CA":  return criarComponenteAnimado( info, idx+1 );
		case "CMA": return criarComponenteMultiAnimado( info, idx+1 );
		}
		return null;
	}

	/** lê a info e cria um componente simples
	 * Na linha a info é <br>
	 * CS pos x + pos y + nome da imagem
	 */
	private static ComponenteSimples criarComponenteSimples(String[] info, int idx) throws IOException {
		Point p = null; //lerPosicao( info[idx], info[idx+1] );
		return new ComponenteSimples(p, "art/"+info[idx+1] );
	}
	
	/** le a info e cria um componente animado
	 * Na linha a info é <br>
	 * CA pos x + pos y + nome da imagem + número de frames + delay na animação
	 */
	private static ComponenteAnimado criarComponenteAnimado(String[] info, int idx ) throws IOException {
		Point p = null; // lerPosicao( info[idx], info[idx+1] );
		String nomeImg =  "art/" + info[idx+0];
		int nFrames = Integer.parseInt( info[idx+1] );
		int delay = Integer.parseInt( info[idx+2] );
		ComponenteAnimado anim = new ComponenteAnimado( p, nomeImg, nFrames, delay );
		if( info.length-idx > 3 && info[idx+3].equals("unico") )
			anim.setCiclico( false );
		return anim;		
	}

	/** le a info e cria um componente multianimado
	 * Na linha a info é <br>
	 * CMA	pos x + pos y + nome da imagem + número de animações + número de frames + delay
	 */
	private static ComponenteMultiAnimado criarComponenteMultiAnimado(String[] info, int idx ) throws IOException {
		Point p = null; // lerPosicao( info[idx], info[idx+1] );
		String nomeImg = "art/" + info[idx+0];
		int nAnims =  Integer.parseInt( info[idx+1] );
		int nFrames = Integer.parseInt( info[idx+2] );
		int delay = Integer.parseInt( info[idx+3] );
		return new ComponenteMultiAnimado( p, nomeImg, nAnims, nFrames, delay );
	}
}

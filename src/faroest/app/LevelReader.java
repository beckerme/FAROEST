package faroest.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import faroest.mundo.Mundo;
import faroest.visitante.*;
import prof.jogos2D.util.ESTProperties;

/** Classe responsável pela leitura dos ficheiros de nível
 */
public class LevelReader {
	
	private static ESTProperties props;    // as propriedades a ler
	
	/**
	 * método para ler o ficheiro de nível
	 * @param level número do nivel
	 * @return o Nivel criado
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static Mundo lerNivel( int level ) throws FileNotFoundException, IOException{
		String file = "niveis/nivel" + level + ".bfe";

		// ler o ficheiro como uma sequência de propriedades
		props = new ESTProperties( new FileReader( file ) );
		
		// ler imagem do fundo e da porta
		BufferedImage imgFundo = ImageIO.read( new File( "art/" + props.getConfig("fundo") ));
		BufferedImage imgPorta = ImageIO.read( new File( "art/" + props.getConfig("porta") ));
		
		// ler as restantes infos do mundo
		int numPortas = props.getConfigAsInt("numPortas");
		int numVisitantes = props.getConfigAsInt("numVisitantes");
		int pontos = props.getConfigAsInt("pontos"); 
		
		// criar o mundo com as infos lidas
		Mundo mundo = new Mundo( imgFundo, imgPorta, numPortas, pontos );
		
		// ler os visitantes possíveis
		try {
			for( int i=1; i <= numVisitantes; i++ ) {
				String p = "visitante_" + (i<10? "0": "") + i;
				String info[] = props.getConfig( p ).split(",");
					switch( info[0] ) {
					// TODO ZFEITO-Tomás implementar os metodos de criação dos visitantes
					case "depositante": mundo.addPossivelVisitante( criarDepositante(info) ); break;
					case "assaltante": mundo.addPossivelVisitante( criarAssaltante(info) ); break;
					//(NAO IMPLEMENTADO CORRETAMENTE)	
//	   				case "troca": mundo.addPossivelVisitante( criarTroca(info) ); break;
					case "aleatorio": mundo.addPossivelVisitante( criarAleatorio(info) ); break;
					case "insatisfeito": mundo.addPossivelVisitante( criarInsatisfeito(info) ); break;
					// TODO ZFEITO ler os restantes tipos de visitantes
					default: throw new IOException();
					}
			}
		} catch( Exception e ) {
			throw new IOException( e );
		}
		
		// criar e retornar o mundo
		return mundo;
	}

	/** Cria um depositante
	 * @param info as informações sobre o depositante
	 * @return o depositante criado
	 */
	private static Depositante criarDepositante( String info[] ) {
		String nome = info[1];	
		int pontos = Integer.parseInt( info[2] );
		int extras = Integer.parseInt( info[3] );
		int minAberto = Integer.parseInt( info[4] );
		int maxAberto = Integer.parseInt( info[5] );
		return new Depositante(nome, pontos, extras, minAberto, maxAberto );
	}
	
	/** Cria um assaltante
	 * @param info as informações sobre o assaltante
	 * @return o assaltante criado
	 */
	private static Assaltante criarAssaltante( String info[] ) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int minSacar = Integer.parseInt( info[3] );
		int maxSacar = Integer.parseInt( info[4] );
		int minDisparo = Integer.parseInt( info[5] );
		int maxDisparo = Integer.parseInt( info[6] );

		return new Assaltante(nome, pontos, minSacar, maxSacar, minDisparo, maxDisparo);
	}
	
	/** Cria um depositante
	 * @param info as informações sobre o depositante
	 * @return o depositante criado
	 */
	private static Aleatorio criarAleatorio(String info[]) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int numExtra = Integer.parseInt( info[3] );
		int minAberto = Integer.parseInt( info[4] );
		int maxAberto = Integer.parseInt( info[5] );
		return new Aleatorio(nome, pontos, numExtra, minAberto, maxAberto);
	}
	
	/** Cria um insatisfeito
	 * @param info as informações sobre o depositante
	 * @return o depositante criado
	 */
	private static Insatisfeito criarInsatisfeito(String info[]) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int nExtras = Integer.parseInt( info[3] );
		int minAberto = Integer.parseInt( info[4] );
		int maxAberto = Integer.parseInt( info[5] );
		// TODO ZFEITO-Ewerton implementar construtor da classe insatisfeito
		return new Insatisfeito(nome,pontos,nExtras ,minAberto,maxAberto);
	}
	
	/** Cria um troca
	 * @param info as informações sobre o troca
	 * @return o troca criado
	 */
	
// nao implementado corretamente.
//	private static Troca criarTroca(String info[]) {
//	    String nome = info[1];
//	    
//	    // Assuming the constructors for Depositante and Assaltante are correctly defined, 
//	    // and we have a way to create these objects from the given information.
//	    // This assumes info[2] and info[3] are the necessary parameters to find or create these objects.
//	    Depositante depositante = criarDepositante(info[2].split(";"));
//	    Assaltante assaltante = criarAssaltante(info[3].split(";"));
//	    
//	    int pontos = Integer.parseInt(info[4]);
//	    int nExtras = Integer.parseInt(info[5]);
//	    int tempoMinTroca = Integer.parseInt(info[6]);
//	    int tempoMaxTroca = Integer.parseInt(info[7]);
//	    int tempoMinDisp = Integer.parseInt(info[8]);
//	    int tempoMaxDisp = Integer.parseInt(info[9]);
//
//	    return new Troca(nome, depositante, assaltante, pontos, nExtras, tempoMinTroca, tempoMaxTroca, tempoMinDisp, tempoMaxDisp);
//	}

}

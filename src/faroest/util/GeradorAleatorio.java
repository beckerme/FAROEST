package faroest.util;

import java.util.Random;

/**
 * classe utilitária para dar a todos os elementos do
 * jogo o mesmo gerador de números aleatórios, bem como
 * métodos para determinar tempos
 * @author F. Sérgio Barbosa
 */
public class GeradorAleatorio {

	private static Random rand = new Random();
	
	/**
	 * devolve um número aleatório inteiro que esteja dentro do intervalo especificado 
	 * @param min limite inferior 
	 * @param max limite superior
	 * @return um número aleatório entre min (inclusive) e max (exclusive)
	 */
	public static int nextInt( int min, int max){
		return min + rand.nextInt( max - min );
	}
	
	/**
	 * devolve um número aleatório inteiro entre 0 e um máximo (não incluído)
	 * @param max limite superior
	 * @return um número aleatório interiro entre 0 e max (exclusive)
	 */
	public static int nextInt( int max){
		return rand.nextInt( max );
	}
	
	/**
	 * retorna um tempo que é calculado desde o tempo atual somado
	 * de um intervalo de tempo aleatório dentro de uma gama de valores (em milisegundos) atual 
	 * @param min tempo minimo a somar ao tempo atual (em milisegundos) 
	 * @param max tempo máximo a somar ao tempo atual (em milisegundos)
	 * @return um tempo aleatório desde o tempo atual somado de um valor entre min e max
	 */
	public static long proxTempo( int min, int max){
		return System.currentTimeMillis() + nextInt( min, max);
	}
	
	/**
	 * devolve o gerador de números aleatório
	 * @return o gerador de números aleatório
	 */
	public static Random getRandom(){
		return rand;
	}
}

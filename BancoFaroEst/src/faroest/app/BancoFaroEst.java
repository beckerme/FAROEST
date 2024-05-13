package faroest.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import faroest.mundo.Mundo;
import faroest.mundo.Porta;
import prof.jogos2D.util.SKeyboard;

/**
 * Esta classe representa o jogo em si.
 */
@SuppressWarnings("serial")
public class BancoFaroEst extends JFrame {
	// constantes a usar no jogo
	private static final int COMPRIMENTO = 999;
	private static final int ALTURA = 600;
	private static final int ULTIMO_NIVEl = 10;
	private static JWindow splash;
	
	// variáveis para os vários elementos visuais do jogo
	private JPanel jContentPane = null;
	private JPanel zonaJogo = null;
	
	// imagem usada para melhorar as animações
	private Image ecran;
	
	// as cores e as fontes a usar 
	private Color pontColor1 = new Color( 0, 0, 100, 255 );
	private Color pontColor2 = new Color( 50, 50, 200, 255 );
	private Font pontFont;   
	private Font textFont;
	private Font scoreFont;

	// para processar as teclas
	private SKeyboard teclado;
	private boolean estaDisparar;

	// Para lidar com as as pontuações máximas
	private ImageIcon scoreImg = new ImageIcon( "art/scores.png" );
	private HighScoreHandler score;
	
	// informações sobre o jogo
	private Mundo mundo;           // as informaçoes sobre o nível atual
	private int nivel;             // número do nível em que se está a jogar
	private int vidas;             // quantas vidas faltam
	private boolean terminouRound; // se acabou este nível
	private int pontuacao;         // a pontuação atual 
	
	/**
	 * construtor da aplicação
	 */
	public BancoFaroEst() {
		super();
		showSplash();
		initialize();
		teclado = new SKeyboard( );
		try {
			ImagensReader.lerImagens( "config/imagens.bfe" );
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ficheiro niveis/imagens.bfe não encontrado");
			System.exit(1);
		}
		try {
			score  = new HighScoreHandler( "config/highscores.bfe" );
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ficheiro niveis/highscores.bfe não encontrado");
			System.exit(1);
		}		
	}
	
	/**
	 * método para arrancar com o jogo
	 */
	public void comecar(){
		closeSplash();
		nivel = 1;      // alterar este valor se quiserem testar algum nível em particular
		vidas = 3;      // alterar este valor para testes (nada de batotas!)
		pontuacao = 0;  // começar em 0 (nada de batota para bater a melhor pontuação) 
		jogarNivel();
	}

	/** ler e começar a jogar o nível atual
	 */
	private void jogarNivel(){
		try {
			mundo = LevelReader.lerNivel( nivel );
		} catch (IOException e) {
			JOptionPane.showMessageDialog( this, "Erro na leitura do nível " + nivel );
			e.printStackTrace();
			System.exit(0);
		}
		mundo.desbloquearPortasVisiveis();
		terminouRound = false;
		Atualizador actualiza = new Atualizador();
		actualiza.start();
	}

	/** voltar a jogar o mesmo nível 
	 */
	private void resetNivel() {
		mundo.bloquearPortasVisiveis();
		terminouRound = false;
		mundo.reset();
		mundo.desbloquearPortasVisiveis();
	}

	/** 
	 * método chamado sempre que é necessário atualizar qualquer coisa na aplicação.
	 * Atenção! Este método NÃO desenha nada. Usar o método desenharJogo para isso.
	 */
	private void atualizarJogo() {	
		pontuacao += mundo.atualizar();
		
		// ver se roda para um lado ou para o outro
		if( teclado.estaPremida( KeyEvent.VK_RIGHT ) )
			mundo.rodarDir();
		else if( teclado.estaPremida( KeyEvent.VK_LEFT ) )
			mundo.rodarEsq();

		if( teclado.estaPremida( KeyEvent.VK_1 ) && !estaDisparar ){
			pontuacao += mundo.getPortasVisiveis()[0].disparo();
			estaDisparar = true;
		}

		if( teclado.estaPremida( KeyEvent.VK_2 ) && !estaDisparar ){
			pontuacao += mundo.getPortasVisiveis()[1].disparo();
			estaDisparar = true;
		}

		if( teclado.estaPremida( KeyEvent.VK_3 ) && !estaDisparar ){
			pontuacao += mundo.getPortasVisiveis()[2].disparo();
			estaDisparar = true;
		}
		if( !teclado.estaPremida( KeyEvent.VK_1 ) && !teclado.estaPremida( KeyEvent.VK_2 ) && !teclado.estaPremida( KeyEvent.VK_3 ) )
			estaDisparar = false;
		
		// terminou este round se ganhou ou morreu
		terminouRound = passouNivel() || mundo.terminouRound();
		if( mundo.terminouRound() ) {
			vidas--;
		}
	}	
	
	/**
	 * método chamado quando se pretende redesenhar o jogo
	 * QUALQUER DESENHO DEVE SER FEITO AQUI
	 * @param g elemento onde se vai desenhar
	 */
	private void desenharJogo( Graphics2D g ){
		// passar para graphics2D pois este é mais avançado
		Graphics2D ge = (Graphics2D )ecran.getGraphics();
		
		AffineTransform tf = ge.getTransform();
		mundo.desenhar(ge);
		ge.setTransform( tf );
		
		// desenhar as quantidades
		ge.setColor( pontColor1 );
		ge.setFont( textFont );
		ge.drawString( "Nivel " , 20, 580 );
		ge.drawString( "Vidas " , 850, 580 );

		ge.setColor( pontColor2 );
		ge.drawString( ""+nivel, 120, 580 );
		ge.drawString( ""+vidas, 950, 580 );

		// fazer a sombra para a pontuação e portas
		ge.setFont( pontFont );
		Porta portas[] = mundo.getPortas();
		ge.setColor( Color.black );
		for( int i = 0; i < portas.length; i++){
			ge.drawString( "" + (i+1), 232 + 40 * i, 582);		
		}
		ge.drawString("" + pontuacao, 622, 582 );

		// desenhar a pontuação e info das portas
		for( int i = 0; i < portas.length; i++){
			ge.setColor( portas[i].jaRecebeu()? Color.green: Color.RED );
			ge.drawString( "" + (i+1), 230 + 40 * i, 580);		
		}
		
		ge.setColor( Color.YELLOW );
		ge.drawString("" + pontuacao, 620, 580 );;
		
		// agora que está tudo desenhado na imagem auxiliar, desenhar no ecrá
		g.drawImage( ecran, 0, 0, null );		
	}

	/** Método chamado quando se termina um nível
	 */
	private void terminouNivel() {
		if( perdeuTodasVidas() ){
			verPontuacao();
			String escolhas[] = {"Voltar a jogar", "Terminar Jogo" };
			int resposta = JOptionPane.showOptionDialog(  null, "Foi despedido pelo banco! Que deseja fazer?", "DERROTA", JOptionPane.YES_NO_OPTION,
					                                       JOptionPane.PLAIN_MESSAGE, null, escolhas, escolhas[0]  );
			switch( resposta ){
			case 0:
				comecar();
				break;
			case 1:
				System.exit( 0 );
			}
		}
		else if( passouNivel() ) {
			pontuacao += mundo.getPontos();
			if( nivel < ULTIMO_NIVEl ){
				JOptionPane.showMessageDialog(BancoFaroEst.this, "Nível " + nivel + " completo. Recompensa: " + mundo.getPontos(), "Nível Completo", JOptionPane.PLAIN_MESSAGE);
				nivel++;
				jogarNivel();
			}
			else {
				verPontuacao();
				// se ganhou vai mostrar a mensagem de vitória e perguntar o que deseja fazer
				String escolhas[] = {"Voltar a Jogar", "Terminar Jogo" };
				int resposta = JOptionPane.showOptionDialog(  null, "Parabéns, completou o Jogo", "Jogo Completo", JOptionPane.YES_NO_OPTION,
						                                      JOptionPane.PLAIN_MESSAGE, null, escolhas, escolhas[0]  );
				switch( resposta ){
				case 0:
					comecar();
					break;
				case 1:
					System.exit( 0 );
				}
			}
		}
	}

	/** Verifica se obteve uma pontuação digna de figurar na tabela de records.
	 * Pede o nome do jogador, caso atinja esse objetivo. 
	 */
	private void verPontuacao() {
		int pos = score.classificarScore(pontuacao);
		if( pos != -1 ) {
			String nome = null;
			do {
				try {
					nome = JOptionPane.showInputDialog( (pos+1) + "º Lugar! Parabéns!\nIntroduza o seu nome." );
					score.addScore(nome, pontuacao);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, "Nome tem de ter 20 caracteres no máximo");
					nome = null;
				}
			} while( nome == null );
		}	
		showScores( pos );
		try {
			score.saveScores();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro a gravar pontuações");
			System.exit( 1 );
		}
	}

	/** Apresenta as pontuações máximas, destacando a pontuação
	 * obtida pelo jogador, se for caso disso  
	 * @param destaqueIdx o índice da pontuação a destacar (0 a 19),
	 *        ou -1 se não houver pontuação a destacar
	 */
	private void showScores( int destaqueIdx ) {
		JPanel scorePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				scoreImg.paintIcon(null, g, 0, 0);
				java.util.List<HighScoreHandler.Score> pontuacoes = score.getScores();
				g.setFont(scoreFont); 
				for( int i=0; i < pontuacoes.size(); i++ ){
					g.setColor( i == destaqueIdx? Color.RED: Color.BLACK );
					HighScoreHandler.Score s =  pontuacoes.get( i );
					int y = 145 + i*28;
					g.drawString(s.getNome(), 30, y );
					String pontStr = "" + s.getPontuacao();
					int comp = g.getFontMetrics().stringWidth(pontStr);
					g.drawString(pontStr, 570-comp, y );
				}
			}
		};
		scorePanel.setPreferredSize( new Dimension(scoreImg.getIconWidth(), scoreImg.getIconHeight()));
		JOptionPane.showMessageDialog( this, scorePanel, "HighScores", JOptionPane.PLAIN_MESSAGE, null );
	}

	/** testa se passou o nível, isto é, se já tem todas as portas 
	 * com dinheiro recebido e fechadas
	 * 
	 * @return true se passou o nível
	 */
	private boolean passouNivel() {
		for( Porta p : mundo.getPortas() )
			if( !p.jaRecebeu() || p.estaAberta() )
				return false;
		return true;
	}

	/** testa se perdeu o jogo, isto é, já não tem vidas
	 * @return true, se perdeu o jogo
	 */
	private boolean perdeuTodasVidas() {
		return vidas <= 0;
	}
	
	/**
	 * Classe responsável pela criação da thread que vai actualizar o mundo de x em x tempo
	 * @author F. Sergio Barbosa
	 */
	class Atualizador extends Thread {
		public void run(){
			long mili = System.currentTimeMillis();
			long target = mili + 33;
			do {
				do {
					atualizarJogo();		
					zonaJogo.repaint();
					// esperar 33 milisegundos o que dá umas 30 frames por segundo
					while( mili < target )
						mili = System.currentTimeMillis();
					target = mili + 33;
				} while( !terminouRound );
				resetNivel();
			} while( !passouNivel() && !perdeuTodasVidas() );
			terminouNivel();
		}
	};	

	/** apresenta o splash screen do jogo */
	private static void showSplash() {
		splash = new JWindow();
		ImageIcon icon = new ImageIcon("art/faroest.png");
		splash.getContentPane().add( new JLabel("", icon, SwingConstants.CENTER));
		splash.setBounds( 0, 0, icon.getIconWidth(), icon.getIconHeight() );
		splash.setLocationRelativeTo( null );
		splash.setVisible(true);
	}
	
	/** fecha o splash screen do jogo */
	private static void closeSplash( ) {
		splash.setVisible(false);
	}

	/**
	 *  vai inicializar a aplicação
	 */
	private void initialize() {
		// características da janela
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setContentPane( getJContentPane() );
		setTitle("Banco no FaroEST");
		pack();
		setResizable(false);		
		setLocationRelativeTo( null );
		
		try {
			// criar as fontes para os textos e pontuações e registá-las no sistema
			File fontFile = new File("font/Saddlebag.ttf");
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font novaFonte =  Font.createFont(Font.TRUETYPE_FONT, fontFile );
            ge.registerFont( novaFonte );

            // definir que se quer esta nova fonte, mas com o tamanho 44
            pontFont = novaFonte.deriveFont( 44.0f );
           
			fontFile = new File("font/RioGrande.ttf");
            novaFonte =  Font.createFont(Font.TRUETYPE_FONT, fontFile );
            ge.registerFont( novaFonte );
            textFont = novaFonte.deriveFont( 33.0f );
            // definir que se quer esta nova fonte, mas com o tamanho 35
            scoreFont = novaFonte.deriveFont( 35.0f );
 		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		// criar a imagem para melhorar as animações e configurá-la para isso mesmo
		ecran = new BufferedImage( COMPRIMENTO, ALTURA, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ge = (Graphics2D )ecran.getGraphics();		
		ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	    ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);	
	}

	/**
	 * método auxiliar para configurar a janela
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getZonaJogo(),BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * Este método inicializa a zonaJogo, AQUI NÃO DEVEM ALTERAR NADA 	
	 */
	private JPanel getZonaJogo() {
		if (zonaJogo == null) {
			zonaJogo = new JPanel(){
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					desenharJogo( (Graphics2D)g );
				}
			};
			Dimension d = new Dimension(COMPRIMENTO, ALTURA);
			zonaJogo.setPreferredSize( d );
			zonaJogo.setSize( d );
			zonaJogo.setMinimumSize( d );
			zonaJogo.setBackground(Color.pink);
		}
		return zonaJogo;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BancoFaroEst jogo = new BancoFaroEst();
		jogo.comecar();
		jogo.setVisible( true );
	}
}

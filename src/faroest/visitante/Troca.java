package faroest.visitante;

public class Troca extends VisitanteDefault {
	private static final int ENTRAR = 10;
	private static final int ANTESTROCA = 11;
	private static final int TROCAR = 12;
	private static final int APOSTROCAR = 13;
	private static final int MORTE = 14;
	private Assaltante assaltante;
	private Depositante depositante;

	public Troca(String nome, Depositante depositante,  Assaltante assaltante, int status, int pontos) {
		super(nome, status, pontos);

		this.depositante = depositante;
		this.assaltante = assaltante;


		depositante.setImagem(depositante.getNome()+"_ola");
	}

	public void trocar() {
		//depositante aparece
		depositante.atualizar();

		if(depositante.baleado() == 0){

			depositante.baleado();
		}

		//assaltante troca com depositante
		depositante.setImagem(depositante.getNome()+"_troca");
		assaltante.setImagem(assaltante.getNome()+"_troca");

		// depois da troca
//		assaltante.setStatus(Assaltante.SACOU);1
		assaltante.setImagem(assaltante.getNome()+"_sacada");

		if(assaltante.baleado()==0) {

			assaltante.baleado();			
		} else
			assaltante.atualizar();

	}
}

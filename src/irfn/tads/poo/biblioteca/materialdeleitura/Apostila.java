package irfn.tads.poo.biblioteca.materialdeleitura;



public class Apostila extends ItemAcervo{

	
	public Apostila(String titulo, String autor, int cdg, double custo) {
		this.titulo = titulo;
		this.autor = autor;		
		this.custo = custo;
		this.codigoItem = cdg;
	}	
}

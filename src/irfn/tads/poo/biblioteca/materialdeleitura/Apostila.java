package irfn.tads.poo.biblioteca.materialdeleitura;



public class Apostila extends ItemAcervo{

	
	public Apostila(String titulo, String autor, int cdg, double custo, double multa) {
		this.titulo = titulo;
		this.autor = autor;		
		this.custo = custo;
		this.codigoItem = cdg;
		this.multa = multa;
	}	
}

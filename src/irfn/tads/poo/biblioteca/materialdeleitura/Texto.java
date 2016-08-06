package irfn.tads.poo.biblioteca.materialdeleitura;

public class Texto extends ItemAcervo{	
	public Texto (String titulo, String autor, double custo, int cdg){
		this.titulo = titulo;
		this.autor = autor;
		this.custo = custo;
		this.codigoItem = cdg;
	}
}

package irfn.tads.poo.biblioteca.materialdeleitura;



public class Apostila extends ItemAcervo{
int quantidade;
	
	public Apostila(String titulo, String autor, int quantidade) {
		this.titulo = titulo;
		this.autor = autor;
		this.quantidade = quantidade;
	}	
	public int getQtd(){
		return this.quantidade;
	}
}

package irfn.tads.poo.biblioteca.materialdeleitura;
import java.lang.Integer;


public class Livro extends ItemAcervo{
String isbn;
	
	Integer edicao; 
	public Livro(String titulo, String autor, String isbn, Integer edicao, int cdg, double custo, double multa){
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.edicao = edicao;
		this.codigoItem = cdg;
		this.custo = custo;
		this.multa = multa;
	}
	public String getISBN(){
		return this.isbn;
	}
	public String getTitulo(){
		return this.titulo;
	}
	public String getAutor(){
		return this.autor;
	}
	public Integer getEdi(){
		return this.edicao;
	}
	public int getCdg(){
		return this.codigoItem;
	}
}

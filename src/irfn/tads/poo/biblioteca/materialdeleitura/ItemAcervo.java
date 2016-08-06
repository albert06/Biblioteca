package irfn.tads.poo.biblioteca.materialdeleitura;
import java.util.Calendar;
import java.util.Date;


public abstract class ItemAcervo {
	double custo;
	boolean pago;
	Date dataAluguel,dataDevolucao;
	Calendar cal = Calendar.getInstance();
	int codigoItem;
	String autor, titulo;
	public void setCusto(double custo){
		this.custo = custo;
	}
	public double getCusto(){
		return this.custo;
	}
	public boolean getPago(){
		return this.pago;
	}
	public int getCodigoItem(){
		return this.codigoItem;
	}
	public void setDataAluguel(Date data){
		this.dataAluguel = data;
	}
	public void setDataDevolucao(Date data){
		this.dataDevolucao = data;
	}
	public Date getDataAluguel(){
		return this.dataAluguel;
	}
	public Date getDataDevolucao(){
		return this.dataDevolucao;		
	}
	public String getTitulo(){
		return this.titulo;
	}
	public String getAutor(){
		return this.autor;

	}
	
	public void setPago(boolean ent){
		this.pago = ent;
	}
}

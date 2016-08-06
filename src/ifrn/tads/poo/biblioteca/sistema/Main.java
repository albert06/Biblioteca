package ifrn.tads.poo.biblioteca.sistema;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ifrn.tads.poo.biblioteca.usuarios.Administrador;
import ifrn.tads.poo.biblioteca.usuarios.Usuario;
import irfn.tads.poo.biblioteca.materialdeleitura.*;


public class Main {
	public static void addAdm(Biblioteca biblioteca,int cdgAdm, String nomeA, String endeA, String cpfA){
		Administrador adm = new Administrador(cdgAdm, nomeA, endeA, cpfA);
		if(biblioteca.veriAdm(adm) == 1){
			System.out.println("Adminstrador ja cadastrado!");
		}else if(biblioteca.veriAdm(adm) == 2){
			System.out.println("CPF não disponivel para cadastro!");
		}
		biblioteca.addAdm(adm);
	}

	public static void devolver(int codUsu,Biblioteca biblioteca, Scanner sc){
		Usuario usu = null;
		int dev = 0,i;
		double multa = 0;
		usu = biblioteca.buscaUsuario(codUsu);
		ItemAcervo[] aluPeUsu = new ItemAcervo[usu.qtdAlugados()];
		aluPeUsu = usu.listaAlugados();

	    

		System.out.println("Escolha o item a ser devolvido: ");
		for(i = 0; i < aluPeUsu.length; i++){
			System.out.printf("Cod: %d, Titulo: %s",aluPeUsu[i].getCodigoItem(),aluPeUsu[i].getTitulo());
		}
		if(sc.hasNextInt())
			dev = sc.nextInt() - 1;
		System.out.println("Digite uma data nesse formato dd/mm/yyyy: ");
		String dataRecebida = sc.nextLine();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
		Date dt = null;
		try {
			dt = df.parse(dataRecebida);
		} catch (ParseException e) {			
			System.out.println("Formato de data inválido!");
		}
		Calendar diaEntrega = Calendar.getInstance();
		Calendar diaLim = Calendar.getInstance();
		diaLim.setTime(aluPeUsu[dev].getDataDevolucao());
		diaEntrega.setTime(dt);	
		if (aluPeUsu[dev].getPago()){
			if (biblioteca.CalcDias(diaEntrega,diaLim) > 0 ){
				if(aluPeUsu[dev] instanceof Livro){
					multa  = 2.0;
					System.out.printf("O valor a ser pago é: %f. "
							+ "Valor da multa por %d dias de atraso:"
							+ "%f",(multa*biblioteca.CalcDias(diaEntrega,diaLim)), aluPeUsu[dev].getCusto());
				}else if(aluPeUsu[dev] instanceof Apostila){
					multa  = 1.5;
					System.out.printf("O valor a ser pago é: %f. "
							+ "Valor da multa por %d dias de atraso:"
							+ "%f",(multa*biblioteca.CalcDias(diaEntrega,diaLim)), aluPeUsu[dev].getCusto());
				}else if(aluPeUsu[dev] instanceof Texto){
					multa  = 2.0;
					System.out.printf("O valor a ser pago é: %f. "
							+ "Valor da multa por %d dias de atraso:"
							+ "%f",(multa*biblioteca.CalcDias(diaEntrega,diaLim)), aluPeUsu[dev].getCusto());
				}
			}
		}else {
			if (biblioteca.CalcDias(diaEntrega,diaLim) > 0 ){
				if(aluPeUsu[dev] instanceof Livro){
					multa  = 2.0;
					System.out.printf("O valor a ser pago é: %f. "
							+ "Valor do livro: %f + Valor da multa por %d dias de atraso:"
							+ "%f",(aluPeUsu[dev].getCusto() + (multa*biblioteca.CalcDias(diaEntrega,diaLim))), aluPeUsu[dev].getCusto(),biblioteca.CalcDias(diaEntrega,diaLim));
				}else if(aluPeUsu[dev] instanceof Apostila){
					multa  = 1.5;
					System.out.printf("O valor a ser pago é: %f. "
							+ "Valor da apostila: %f + Valor da multa por %d dias de atraso:"
							+ "%f",(aluPeUsu[dev].getCusto() + (multa*biblioteca.CalcDias(diaEntrega,diaLim))), aluPeUsu[dev].getCusto(),biblioteca.CalcDias(diaEntrega,diaLim));
				}else if(aluPeUsu[dev] instanceof Texto){
					multa  = 2.0;
					System.out.printf("O valor a ser pago é: %f. "
							+ "Valor do Texto: %f + Valor da multa por %d dias de atraso:"
							+ "%f",(aluPeUsu[dev].getCusto() + (multa*biblioteca.CalcDias(diaEntrega,diaLim))), aluPeUsu[dev].getCusto(),biblioteca.CalcDias(diaEntrega,diaLim));
				}
				
			}else{
				System.out.printf("O valor a ser pago é: %f. ",aluPeUsu[dev].getCusto());
			}
		}
		if(aluPeUsu[dev] instanceof Livro){
			biblioteca.adicionarLivro((Livro) aluPeUsu[dev], new Integer(1));
		}else if(aluPeUsu[dev] instanceof Apostila){
			biblioteca.adicionarApostila((Apostila) aluPeUsu[dev], new Integer(1));
			
		}else if(aluPeUsu[dev] instanceof Texto){
			biblioteca.adicionarTexto((Texto) aluPeUsu[dev], new Integer(1));
		}			
			usu.devolver(aluPeUsu[dev]);		
	}
	public static void reservar(int codUsu, Biblioteca biblioteca, int tipo, Scanner sc){
		int i,escolha = 0;
		Usuario usu = null;
		usu = biblioteca.buscaUsuario(codUsu);
		switch(tipo){
			case 1:
				Livro lL[] = new Livro[biblioteca.tamLivros()];
				lL = biblioteca.listLivros();
				System.out.println("Escolha o livro que deseja reservar: ");
				for(i = 0; i < lL.length; i++){
					System.out.printf("Cod: %d, Titulo: %s",lL[i].getCdg(),lL[i].getTitulo());
				}
				if(sc.hasNextInt())
					escolha = sc.nextInt();
				if(biblioteca.qtdDoLivro(lL[escolha - 1]) > 0)					
					biblioteca.reservar(usu, lL[escolha - 1]);
				else
					System.out.println("Livro não disponivel para reserva!");
				break;
			case 2:
				Apostila aL[] = new Apostila[biblioteca.qtdApo()];
				aL = biblioteca.listApostila();
				System.out.println("Escolha a apostila que deseja reservar: ");
				for(i = 0; i < aL.length ; i++){
					System.out.printf("Cod: %d, Titulo: %s",aL[i].getCodigoItem(),aL[i].getTitulo());
				}
				if(sc.hasNextInt())
					escolha = sc.nextInt();
				if(biblioteca.qtdUmaApo(aL[escolha - 1]) > 0)
					biblioteca.reservar(usu, aL[escolha - 1]);
				else
					System.out.println("Apostila não está disponivel para reserva!");
				break;
			case 3:
				Texto tL[] = new Texto[biblioteca.qtdTxt()];
				tL = biblioteca.listTxt();
				System.out.println("Escolha o texto que deseja reservar: ");
				for(i = 0; i < tL.length ; i++){
					System.out.printf("Cod: %d, Titulo: %s",tL[i].getCodigoItem(),tL[i].getTitulo());
				}
				if(sc.hasNextInt())
					escolha = sc.nextInt();
				if(biblioteca.qtdUmTxt(tL[escolha - 1]) > 0)
					biblioteca.reservar(usu, tL[escolha]);
				else
					System.out.println("O Texto não está disponivel para reserva!");
				break;
			default:
				break;
		}
	}
	public static void alugar(int tipo, int codUsu, Biblioteca biblioteca, Scanner sc){
		SimpleDateFormat format = new SimpleDateFormat();
		int escolha = 0, i;
		Date data = new Date();
		switch(tipo){
			case 1:		

				Livro listaL[] = new Livro[biblioteca.tamLivros()];
				listaL = biblioteca.listLivros();									
				System.out.println("Livros disponiveis\n");
				for(i = 0 ; i < listaL.length ; i++){
					System.out.printf("Cod: %d, Titulo: %s, Autor: %s, ISBN: %s, Edição: %d, "
							+ "Quantidade Disponivel: %d, Valor do Aluguel: %f\n"
							+ "",listaL[i].getCdg(),listaL[i].getTitulo(),listaL[i].getAutor(),listaL[i].getISBN(),listaL[i].getEdi(),biblioteca.qtdDoLivro(listaL[i]), listaL[i].getCusto());
				}
				System.out.println("Digite o código do livro que deseja alugar: ");
				if(sc.hasNextInt())
					escolha = sc.nextInt();
				if(biblioteca.qtdDoLivro(listaL[escolha-1]) > 0){
					biblioteca.alugar(listaL[escolha - 1], data,7, codUsu);
					System.out.printf("O livro deve ser entregue até o dia: ");
					System.out.println(format.format(listaL[escolha].getDataDevolucao()));
				}else
					System.out.println("O livro não esta disponivel para alugar!");
				
				
				break;
			case 2:
				Texto listaT[] = new Texto[biblioteca.qtdTxt()];
				listaT = biblioteca.listTxt();
				System.out.println("Textos disponiveis: ");
				for (i = 0; i < listaT.length; i++) {
					System.out.printf("Cod: %d, Titulo: %s, Autor: %s"
							+ "", listaT[i].getCodigoItem(), listaT[i].getTitulo(), listaT[i].getAutor());
				}
				System.out.println("Digite o código do texto que deseja alugar: ");
				if(sc.hasNextInt())
					escolha = sc.nextInt();
				if(biblioteca.qtdUmTxt(listaT[escolha-1]) > 0){
					biblioteca.alugar(listaT[escolha - 1], data, 15, codUsu);
					System.out.printf("O texto deve ser entregue até o dia: ");
					System.out.println(format.format(listaT[escolha].getDataDevolucao()));
				}else
					System.out.println("O texto não está disponivel para alugar!");
				
				break;
			case 3:
				Apostila listaA[] = new Apostila[biblioteca.qtdApo()];
				listaA = biblioteca.listApostila();
				System.out.println("Textos disponiveis: ");
				for (i = 0; i < listaA.length; i++) {
					System.out.printf("Cod: %d, Titulo: %s, Autor: %s"
							+ "", listaA[i].getCodigoItem(), listaA[i].getTitulo(), listaA[i].getAutor());
				}
				System.out.println("Digite o código da apostila que deseja alugar: ");
				if(sc.hasNextInt())
					escolha = sc.nextInt();
				if(biblioteca.qtdUmaApo(listaA[escolha-1]) > 0){
					biblioteca.alugar(listaA[escolha - 1], data, 15, codUsu);
					System.out.printf("A apostila deve ser entregue até o dia: ");
					System.out.println(format.format(listaA[escolha].getDataDevolucao()));
				}else
					System.out.println("A apostila não está disponivel para alugar!");
				break;
		}
		
	}
	
	
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int opcao = -1,i = 0, cdgLivro = 1, cdgUsu = 1, cdgAdm = 1;
		String nome;
		Biblioteca biblioteca;
		
		
		System.out.println("Bem Vindo ao asistente de configuração do seu sistema de biblioteca!\n"
				+ "Para começar digite o nome da sua biblioteca: ");
		nome = sc.nextLine();
		biblioteca = new Biblioteca(nome);
		System.out.println("1 Adicionar Administrador\n2 Adicionar livro\n3 Alugar\n4 listar\n5 Teste CPF");
		opcao = sc.nextInt();
		while(opcao != 0){
			
			switch(opcao){
				case 1:
					String cpfA = null,nomeA = null,endeA = null;
					
					System.out.print("Digite o nome: ");
					if(sc.hasNext())
						nomeA = sc.next();
					System.out.print("Digite o endereço: ");
					if(sc.hasNext())
						endeA = sc.next();
					System.out.print("Digite o cpf: ");
					if(sc.hasNext())
						cpfA = sc.next();					
					if(biblioteca.verificarCPF(cpfA) == 1){
						System.out.println("Tamanho inválido!");						
					}else{
						addAdm(biblioteca, cdgAdm++,nomeA, endeA, cpfA);
					}
					break;
				case 2:
					String tit = null, autor, isbn;
					double valor = 0;
					int qtd = 0,edi = 0;					
					System.out.println("Titulo: ");
					if(sc.hasNext()){
						tit = sc.next();
					}
					System.out.println("Autor: ");
					autor = sc.next();
					System.out.println("ISBN: ");
					isbn = sc.next();					
					System.out.println("Edição: ");
					edi = sc.nextInt();
					System.out.println("Quantidade: ");
					qtd = sc.nextInt();
					System.out.println("Insira o valor do aluguel do livro: ");
					if(sc.hasNextDouble())
						valor = sc.nextDouble();
					if(biblioteca.verificarISBN(isbn)){
						Livro novo = new Livro(tit,autor,isbn,new Integer(edi), cdgLivro++,valor);
						biblioteca.adicionarLivro(novo, qtd);
					}else{
						System.out.println("Número do ISBN invalido ou repetido!");
					}
					
					break;
				case 3:
					int escolha = -1 ,cod = -1;
					System.out.print("Caso queira alugar um livro digite 1 para textos 2 e apostilas 3: ");
					if(sc.hasNextInt())
						escolha = sc.nextInt();
					System.out.print("Digite o código do Usuario: ");
					if(sc.hasNextInt())
						cod = sc.nextInt();
					try{
						alugar(escolha,cod,biblioteca,sc);
					}catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Código do livro ou do usuário inválido! Tente novamente.");
					}
					break;
				case 4:
					Livro listar[] = new Livro[biblioteca.tamLivros()];
					listar = biblioteca.listLivros();
					for(i = 0 ; i < listar.length ; i++){
						System.out.printf("Cod: %d, Titulo: %s, Autor: %s, ISBN: %s, Edição: %d, "
								+ "Quantidade Disponivel: %d\n"
								+ "",listar[i].getCdg(),listar[i].getTitulo(),listar[i].getAutor(),listar[i].getISBN(),listar[i].getEdi(),biblioteca.qtdDoLivro(listar[i]));
					}
					break;
				case 5:
					String cpfU = null,nomeU = null, enderecoU = null;;
					Usuario usu;
					System.out.print("Digite o nome: ");
					if(sc.hasNext())
						nomeU = sc.next();
					System.out.print("Digite o endereço: ");
					if(sc.hasNext())
						enderecoU = sc.next();
					System.out.print("Digite o cpf: ");
					if(sc.hasNext())
						cpfU = sc.next();					
					if(biblioteca.verificarCPF(cpfU) == 1){
						System.out.println("Tamanho inválido!");						
					}else{
						usu = new Usuario(cdgUsu, nomeU, enderecoU, cpfU);
						if(biblioteca.veriUsu(usu) == 1){
							System.out.println("Usuario ja cadastrado!");
						}else if(biblioteca.veriUsu(usu) == 2){
							System.out.println("CPF não disponivel para cadastro!");
						}
						biblioteca.addUsuario(usu);
					}					
					break;
				case 6:
					
					break;
				default:
					break;
			}
			System.out.println("1 Adicionar Administrador\n2 Adicionar livro\n3 Alugar\n4 listar\n5 Teste CPF");
			if(sc.hasNextInt())
				opcao = sc.nextInt();
		}
		
	}
}

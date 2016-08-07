package ifrn.tads.poo.biblioteca.sistema;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ifrn.tads.poo.biblioteca.usuarios.Administrador;
import ifrn.tads.poo.biblioteca.usuarios.FalhaNoCadastroException;
import ifrn.tads.poo.biblioteca.usuarios.Usuario;
import irfn.tads.poo.biblioteca.materialdeleitura.*;


public class Main {
	public static void addAdm(Biblioteca biblioteca,int cdgAdm, String nomeA, String endeA, String cpfA, String senha){
		Administrador adm = new Administrador(cdgAdm, nomeA, endeA, cpfA,senha);
		if(biblioteca.veriAdm(adm) == 1){
			System.out.println("Adminstrador ja cadastrado!");
		}else if(biblioteca.veriAdm(adm) == 2){
			System.out.println("CPF não disponivel para cadastro!");
		}
		try {
			biblioteca.addAdm(adm);
		} catch (FalhaNoCadastroException e) {			
			System.out.println("Falha no cadastro!");
		}
	}
	public static void addUsu(Biblioteca biblioteca, int cdgUsu, String nomeUsu, String endU,String cpfU){
		Usuario usu = new Usuario(cdgUsu, nomeUsu, endU, cpfU);
		if(biblioteca.veriUsu(usu) == 1)
			System.out.println("Usuário já cadastrado!");
		else if(biblioteca.veriUsu(usu) == 2)
			System.out.println("Cpf ja cadastrado!");
		
		try{
			biblioteca.addUsuario(usu);
			System.out.println("foi");
		}catch(FalhaNoCadastroException e){
			System.out.println("Falha no cadastro!");
		}
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
			System.out.printf("Número: %d, Titulo: %s",i,aluPeUsu[i].getTitulo());
		}
		if(sc.hasNextInt())
			dev = sc.nextInt() - 1;
		System.out.println("Digite uma data nesse formato dd/mm/yyyy: ");
		String dataRecebida = sc.nextLine();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
		Date dt = null;
		try {
			dt = df.parse(dataRecebida);
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
		} catch (ParseException e) {			
			System.out.println("Formato de data inválido!");
		}
		
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
	public static void listarItens(Biblioteca biblioteca, int opt){
		int i;
		switch(opt){
			case 1:
				Livro listar[] = new Livro[biblioteca.tamLivros()];
				listar = biblioteca.listLivros();
				if(listar.length > 0){
					for(i = 0 ; i < listar.length ; i++){
						System.out.printf("Cod: %d, Titulo: %s, Autor: %s, ISBN: %s, Edição: %d, "
								+ "Quantidade Disponivel: %d\n"
								+ "",listar[i].getCdg(),listar[i].getTitulo(),listar[i].getAutor(),listar[i].getISBN(),listar[i].getEdi(),biblioteca.qtdDoLivro(listar[i]));
					}
				}else
					System.out.println("A biblioteca não possui livros cadastrados!");
				
				break;
			case 2:
				Texto lt[] = new Texto[biblioteca.qtdTxt()];
				lt = biblioteca.listTxt();
				if(lt.length > 0){
					for(i = 0 ; i < lt.length ; i++){
						System.out.printf("Titulo: %s, Autor: %s\n",lt[i].getTitulo(),lt[i].getAutor());
					}
				}else
					System.out.println("A biblioteca não possui textos cadastrados!");
				break;
			case 3:
				Apostila la[] = new Apostila[biblioteca.qtdApo()];
				la = biblioteca.listApostila();
				if(la.length > 0){
					for(i = 0 ; i < la.length ; i++){
						System.out.printf("Titulo: %s, Autor: %s\n",la[i].getTitulo(),la[i].getAutor());
					}
				}else
					System.out.println("A biblioteca não possui apostilas cadastradas!");
				break;
			default:
				break;
			}
		
	}
	public static void listarUsuario(Biblioteca biblioteca){
		Usuario usu [] = new Usuario[biblioteca.qtdUsu()];
		usu = biblioteca.listUsu();
		for(int i = 0 ; i < usu.length ; i++){
			
			System.out.printf("Nome: %s, Endereço: %s, CPF: %s, Código: %d\n",usu[i].getNome(),usu[i].getEnd(),usu[i].getCPF(),usu[i].getCod());
		}
	}
	public static void listarAlugados(int codUsu,Biblioteca bibli){
		Usuario usu ;
		usu = bibli.buscaUsuario(codUsu);
		ItemAcervo itens [] = new ItemAcervo[usu.qtdAlugados()];
		itens = usu.listaAlugados();
		for(int i = 0 ; i < itens.length ; i++){
			System.out.printf("Titulo: %s, Autor: %s, Data de Devolução: ",itens[i].getTitulo(),itens[i].getAutor());
			System.out.println(itens[i].getDataDevolucao());
		}
	}
	public static void listarReservados(int cdgUsu, Biblioteca bibli){
		Usuario usu;
		usu = bibli.buscaUsuario(cdgUsu);
		ItemAcervo itens [] = new ItemAcervo[usu.qtdAlugados()];
		itens = usu.listaReservados();
		for(int i = 0 ; i < itens.length ; i++){
			System.out.printf("Titulo: %s, Autor: %s \n",itens[i].getTitulo(),itens[i].getAutor());
		}
	}
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int opcao = -1, cdgLivro = 1, cdgUsu = 1, cdgAdm = 1, cdgTexto = 1, cdgApostila = 1;
		String nome;
		Biblioteca biblioteca;
		
		
		System.out.println("Bem Vindo ao asistente de configuração do seu sistema de biblioteca!\n"
				+ "Para começar digite o nome da sua biblioteca: ");
		nome = sc.nextLine();
		biblioteca = new Biblioteca(nome);
		System.out.println("Cadastre um Administrador");
		String cpfA = null,nomeA = null,endeA = null,senha = null;
		
		System.out.print("Digite o nome: ");
		if(sc.hasNext())
			nomeA = sc.next();
		System.out.print("Digite o endereço: ");
		if(sc.hasNext())
			endeA = sc.next();
		System.out.print("Digite o cpf: ");
		if(sc.hasNext())
			cpfA = sc.next();		
		System.out.println("Senha: ");
		if(sc.hasNext())
			senha = sc.next();
		addAdm(biblioteca, cdgAdm++,nomeA, endeA, cpfA, senha);
		System.out.println("0 Para sair\n1 Menu Administrador\n2 Menu Usuarios\n");
		opcao = sc.nextInt();
		while(opcao != 0){
			switch(opcao){				
				case 1:
					String veri = null;
					int opA = -1, esA;
					System.out.println("Digite sua senha");
					if(sc.hasNext())
						veri = sc.next();
					if(biblioteca.loguinAdm(veri)){
						System.out.println("Digite 0 para sair\n1 para adicionar livros\n2 Para adicionar textos\n"
								+ "3 Para adicionar apostilas\n4 Para listar Usuarios\n5 Para ver os itens alugados"
								+ "\n6 Para ver os itens reservados\n7 Para Alugar\n8 Para devolver");
						if(sc.hasNextInt())
							opA = sc.nextInt();
						while(opA != 0){
							switch(opA){
								case 1:
									String tit = null, autor = null, isbn = null;
									double valor = 0;
									int qtd = 0,edi = 0;					
									System.out.println("Titulo: ");
									if(sc.hasNext()){
										tit = sc.next();
									}
									System.out.println("Autor: ");
									if(sc.hasNext())
										autor = sc.next();
									System.out.println("ISBN: ");
									if(sc.hasNext())
										isbn = sc.next();
									System.out.println("Edição: ");
									if(sc.hasNextInt())
										edi = sc.nextInt();
									System.out.println("Quantidade: ");
									if(sc.hasNextInt())
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
								case 2:
									String tiT = null, autorT = null;
									double valorT = -1;
									int qtdT = 0;
									System.out.println("Titulo: ");
									if(sc.hasNext()){
										tiT = sc.next();
									}
									System.out.println("Autor: ");
									if(sc.hasNext())
										autorT = sc.next();
									System.out.println("Valor da Multa: ");
									if(sc.hasNextDouble())
										valorT = sc.nextDouble();
									System.out.println("Quantidade: ");
									if(sc.hasNextInt())
										qtdT = sc.nextInt();
									Texto txt = new Texto(tiT, autorT, valorT, cdgTexto++);
									biblioteca.adicionarTexto(txt, new Integer(qtdT));
									break;
								case 3:
									String tiA = null, autoA = null;
									double valorA = -1;
									int qtdA = 0;
									System.out.println("Titulo: ");
									if(sc.hasNext()){
										tiA = sc.next();
									}
									System.out.println("Autor: ");
									if(sc.hasNext())
										autoA = sc.next();
									System.out.println("Valor da Multa: ");
									if(sc.hasNextDouble())
										valorA = sc.nextDouble();
									System.out.println("Quantidade: ");
									if(sc.hasNextInt())
										qtdA = sc.nextInt();
									Apostila apo = new Apostila(tiA, autoA, cdgApostila++, valorA);
									biblioteca.adicionarApostila(apo, new Integer(qtdA));;
									break;
								case 4:
									listarUsuario(biblioteca);
									break;
								case 5:
									int cdgDigi = -1;
									System.out.println("Digite o código do usuário na qual deseje ver os itens alugados: ");
									listarUsuario(biblioteca);
									if(sc.hasNextInt())
										cdgDigi = sc.nextInt();									
									try{
										listarAlugados(cdgDigi, biblioteca);
									}catch(IndexOutOfBoundsException e){
										System.out.println("Código inválido do usuário!");
									}
									break;
								case 6:
									int cdgDig = -1;
									System.out.println("Digite o código do usuário na qual deseje ver os itens reservados: ");
									listarUsuario(biblioteca);
									if(sc.hasNextInt())
										cdgDig = sc.nextInt();
									try{
										listarAlugados(cdgDig, biblioteca);
									}catch(IndexOutOfBoundsException e){
										System.out.println("Código inválido do usuário!");
									}
									break;
								case 7:
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
								case 8:
									int cdgDi = -1;
									System.out.println("Digite o código do usuário na qual deseje ver os itens reservados: ");
									listarUsuario(biblioteca);
									if(sc.hasNextInt())
										cdgDi = sc.nextInt();
									try{
										devolver(cdgDi, biblioteca, sc);
									}catch(IndexOutOfBoundsException e){
										System.out.println("Código inválido do usuário!");
									}
									
									break;
								case 9:
									int cadastro = 0;
									System.out.println("Cadastro de Admnistrador digite 1\n2 Usuários");
									if(sc.hasNextInt())
										cadastro = sc.nextInt();
									while(cadastro != 0){
										switch(cadastro){
											case 1:
												cpfA = null;
												nomeA = null;
												endeA = null;
												senha = null;
												
												System.out.print("Digite o nome: ");
												if(sc.hasNext())
													nomeA = sc.next();
												System.out.print("Digite o endereço: ");
												if(sc.hasNext())
													endeA = sc.next();
												System.out.print("Digite o cpf: ");
												if(sc.hasNext())
													cpfA = sc.next();		
												System.out.println("Senha: ");
												if(sc.hasNext())
													senha = sc.next();
												addAdm(biblioteca, cdgAdm++,nomeA, endeA, cpfA, senha);
												
												break;
											case 2:
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
												usu = new Usuario(cdgUsu, nomeU, enderecoU, cpfU);
												if(biblioteca.veriUsu(usu) == 1){
													System.out.println("Usuario ja cadastrado!");
												}else if(biblioteca.veriUsu(usu) == 2){
													System.out.println("CPF não disponivel para cadastro!");
													}
												try {
													biblioteca.addUsuario(usu);
													cdgUsu++;
												} catch (FalhaNoCadastroException e) {					
													e.printStackTrace();
												}
												break;
											default:
												break;
										}
										System.out.println("Cadastro de Admnistrador digite 1\n2 Usuários");
										if(sc.hasNextInt())
											cadastro = sc.nextInt();
									}	
									break;
								default:
									break;
							}
							System.out.println("Digite 0 para sair\n1 para adicionar livros\n2 Para adicionar textos\n"
									+ "3 Para adicionar apostilas\n4 Para listar Usuarios\n5 Para ver os itens alugados"
									+ "\n6 Para ver os itens reservados\n7 Para Alugar\n8 Para devolver\n9 Cadastro");
							if(sc.hasNextInt())
								opA = sc.nextInt();
						}
						
					}
					break;
				case 2:
					int opU = -1;
					System.out.println("Digite 0 para sair\n1 Para ver os itens disponiveis\n2 Itens alugados\n3 Reservar");
					if(sc.hasNextInt())
						opU = sc.nextInt();
					while(opU != 0){
						switch(opU){
							case 1:
								int opt = 0;
								System.out.println("Digite 1 para ver os livros, 2 para textos e 3 para apostilas");
								if(sc.hasNextInt())
									opt = sc.nextInt();
								listarItens(biblioteca, opt);
							break;
							case 2:
								int codigo = -1;
								System.out.println("Digite seu código: ");
								if(sc.hasNextInt())
									codigo = sc.nextInt();
								listarAlugados(codigo, biblioteca);
								break;
							case 3:
								int rese = -1, cdgRe = 0;
								System.out.println("Para reservar um Livro digite 1\nApostila 2\nTexto 3");
								if(sc.hasNextInt())
									rese = sc.nextInt();
								System.out.println("Digite seu código: ");
								if(sc.hasNextInt())
									cdgRe = sc.nextInt();
								try{
									reservar(cdgRe, biblioteca, rese, sc);
								}catch(IndexOutOfBoundsException e){
									System.out.println("Código do usuário inválido!");
								}
								break;
							default:
								break;
						}
						System.out.println("Digite 0 para sair\n1 Para ver os itens disponiveis\n2 Itens alugados\n3 Reservar");
						if(sc.hasNextInt())
							opU = sc.nextInt();
					}
					break;
				default:
					break;
			}
			
			System.out.println("0 Para sair\n1 Menu Administrador\n2 Menu Usuarios\n");
			if(sc.hasNextInt())
				opcao = sc.nextInt();
		}
		
	}
}

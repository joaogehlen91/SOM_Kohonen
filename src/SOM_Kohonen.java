/*
 * Nomes: Elias Fank, João Gehlen, Ricardo Zanuzzo
 * Disciplina: Inteligencia Artificial
 * 
 * 2017/1
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SOM_Kohonen {
	
	public static int DIM_MATRIZ_NEURONIOS = 15;
	public static int MAX_EPOCAS = 10;
	public static double TAXA_APRENDIZAGEM = 0.1;
	public static double RAIO = 0.5;
	public static double TEST_SIZE = 0.3;
	
	public static void main(String[] args) throws IOException {
		
		if(args.length == 5){
			DIM_MATRIZ_NEURONIOS = Integer.parseInt(args[0]);
			MAX_EPOCAS = Integer.parseInt(args[1]);
			TAXA_APRENDIZAGEM = Double.parseDouble(args[2]);
			RAIO = Double.parseDouble(args[3]);
			TEST_SIZE = Double.parseDouble(args[4]);
		}

		
		ArrayList<ArrayList<Integer>> input_data = new ArrayList<ArrayList<Integer>>();
		ArrayList<String> input_labels = new ArrayList<String>();
		String fileName = "arquivos/training";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String linha;
		String numero = "";
		
		br.readLine();
		
		while((linha = br.readLine()) != null){
			if(linha.length() == 32){
				numero += linha;
			}
			if(linha.length() != 32 && numero != ""){
				input_labels.add(linha.replace(" ",""));
				ArrayList<Integer> num = new ArrayList<Integer>();
				for (String s : numero.split("")) {
					num.add(Integer.parseInt(s));
				}
				input_data.add(num);
				numero = "";
			}
		}
		
		br.close();

		ArrayList<ArrayList<Integer>> sort_input_data = new ArrayList<ArrayList<Integer>>();
		ArrayList<String> sort_input_labels = new ArrayList<String>();
		ArrayList<Integer> sortIndex = new ArrayList<Integer>();
		for (int i=0; i<input_data.size();i++) sortIndex.add(i);
		Collections.shuffle(sortIndex);
		for(int i=0; i<input_data.size(); i++){
			sort_input_data.add(input_data.get(sortIndex.get(i)));
			sort_input_labels.add(input_labels.get(sortIndex.get(i)));
		}


/*		for(int i=0; i<input_data.size(); i++){
			for(int j=0; j<1024; j++){
				if(j%32==0) System.out.println();
				System.out.print(sort_input_data.get(i).get(j));
			}
			System.out.println(sort_input_labels.get(i));
		}*/


		
		
		int i = 0;
		int[][] input_num = new int[sort_input_data.size()][sort_input_data.get(0).size()];
		for (ArrayList<Integer> integers : sort_input_data) {
			int j = 0;
			for (Integer integer : integers) {
				input_num[i][j] = integer;
				//System.out.println(input_num[i][j]);
				j++;
			}
			i++;
		}
		
		int limit = (int) (input_num.length*TEST_SIZE);
		System.out.println();
		System.out.println(input_num.length-limit+" exemplos para treinamento");
		System.out.println(limit+" exemplos para teste");
		int[][] input_train = new int[input_num.length-limit][1024];
		String[] input_train_labels = new String[input_num.length-limit];
		int[][] input_test = new int[limit][1024];
		String[] input_test_labels = new String[limit];
		
		for(int x=0; x<limit; x++)
			for(int y=0; y<input_num[0].length; y++){
				input_test[x][y] = input_num[x][y]; 
				input_test_labels[x] = sort_input_labels.get(x);

			}
		for(int x=limit; x<input_num.length; x++)
			for(int y=0; y<input_num[0].length; y++){
				input_train[x-limit][y] = input_num[x][y];
				input_train_labels[x-limit] = sort_input_labels.get(x);
			}

		

		SOM_Class som = new SOM_Class(input_train, DIM_MATRIZ_NEURONIOS, MAX_EPOCAS, RAIO, TAXA_APRENDIZAGEM);
		som.treinamento(input_train_labels);
		som.teste(input_test, input_test_labels);
		//som.imprimeNeuronios();
		som.escreveDesenhoNeurArquivo("arquivos/Rede.txt");
		System.out.println("\n\nO Mapa de neurônios com seus respectivos desenhos foi escrito no arquivo arquivos/Rede.txt");
		som.escreveMapaArquivo("arquivos/Mapa.txt");
		System.out.println("O Mapa de neurônios, a acurácia da rede e a acurácia da rede por cluster foi salvo no arquivo arquivos/Mapa.txt\n");
		//som.imprimeMapaRotuladoTrain();

	}

}

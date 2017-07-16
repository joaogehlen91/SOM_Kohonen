import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SOM_Kohonen {
	
	public static final int DIM_MATRIZ_NEURONIOS = 10;
	public static final int MAX_EPOCAS = 100;
	public static final double TEST_SIZE = 0.3;
	public static final double TAXA_APRENDIZAGEM = 0.1;
	public static final double RAIO = 0.5;
	
	public static void main(String[] args) throws IOException {
		
		ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
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
				input.add(num);
				numero = "";
			}
		}
		
		br.close();
		
		
		int i = 0;
		int[][] input_num = new int[input.size()][input.get(0).size()];
		for (ArrayList<Integer> integers : input) {
			int j = 0;
			for (Integer integer : integers) {
				input_num[i][j] = integer;
				//System.out.println(input_num[i][j]);
				j++;
			}
			i++;
		}
		
		int limit = (int) (input_num.length*TEST_SIZE);
		System.out.println(input_num.length-limit+" exemplos para treinamento");
		System.out.println(limit+" exemplos para teste");
		int[][] input_train = new int[input_num.length-limit][1024];
		String[] input_train_labels = new String[input_num.length-limit];
		int[][] input_test = new int[limit][1024];
		String[] input_test_labels = new String[limit];
		
		for(int x=0; x<limit; x++)
			for(int y=0; y<input_num[0].length; y++){
				input_test[x][y] = input_num[x][y]; 
				input_test_labels[x] = input_labels.get(x);

			}
		for(int x=limit; x<input_num.length; x++)
			for(int y=0; y<input_num[0].length; y++){
				input_train[x-limit][y] = input_num[x][y];
				input_train_labels[x-limit] = input_labels.get(x);
			}

		

		SOM_Class som = new SOM_Class(input_train, DIM_MATRIZ_NEURONIOS, MAX_EPOCAS, RAIO, TAXA_APRENDIZAGEM);
		som.treinamento(input_train_labels);
		som.teste(input_test, input_test_labels);
		//som.imprimeNeuronios();
		som.escreveDesenhoNeurArquivo("arquivos/Rede.txt");
		som.escreveMapaArquivo("arquivos/Mapa.txt");
		som.imprimeMapaRotuladoTrain();
		som.imprimeAcuracia();

	}

}

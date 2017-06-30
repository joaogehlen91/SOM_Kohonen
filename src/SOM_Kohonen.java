import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SOM_Kohonen {
	
	public static final int MAX_CLUSTERS = 100;
	public static final double TEST_SIZE = 0.5;  //porcentagem do arquivo de entrada utilizada para testes
	
	public static void main(String[] args) throws IOException {
		
		ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
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
		
//		int limit = (int) (input_num.length*TEST_SIZE);
//		System.out.println(limit);
//		System.out.println(input_num.length-limit);
//		int[][] input_train = new int[input_num.length-limit][VEC_LEN];
//		int[][] input_test = new int[limit][VEC_LEN];
//		
//		for(int x=0; x<limit; x++)
//			for(int y=0; y<input_num[0].length; y++)
//				input_test[x][y] = input_num[x][y]; 
//		for(int x=limit; x<input_num.length; x++)
//			for(int y=0; y<input_num[0].length; y++)
//				input_train[x-limit][y] = input_num[x][y]; 

		
		
/*		for(int x=0; x<input_train.length; x++) {
			for(int y=0; y<input_train[0].length; y++) {
				System.out.print(input_train[x][y]);
			}
			System.out.println();
		}*/
		

		SOM_Class som = new SOM_Class(MAX_CLUSTERS, input_num);
		som.train();
		som.printResults();
		/*som.Train(input_train, input_test);
		som.Test(input_train, input_test);*/
		
		

	}

}

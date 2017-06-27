import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class SOM_Kohonen {
	
	public static final int MAX_CLUSTERS = 10;
	public static final int VEC_LEN = 1024;
	public static final double DECAY_RATE = 0.96;
	public static final double MIN_ALPHA = 0.01;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		ArrayList<ArrayList<Integer>> cepo = new ArrayList<ArrayList<Integer>>();
		Integer[][] cepo1; 
		
		
		String fileName = "arquivos/training";
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		String linha;
		String numero = "";
		
		br.readLine();
		
		
		
		while((linha = br.readLine()) != null){
			//System.out.println(linha);
			//System.out.println(linha.length());
			
			if(linha.length() == 32){
				numero += linha;
			}
			if(linha.length() != 32 && numero != ""){
				ArrayList<Integer> num = new ArrayList<Integer>();
				
				for (String s : numero.split("")) {
					num.add(Integer.parseInt(s));
				}
				cepo.add(num);
				numero = "";
				
			}
		}
		
		
		int i = 0;
		int[][] cepo_num = new int[cepo.size()][cepo.get(0).size()];
		for (ArrayList<Integer> integers : cepo) {
			int j = 0;
			for (Integer integer : integers) {
				cepo_num[i][j] = integer;
				j++;
			}
			i++;
		}
		
		double Alpha = 0.6;
		SOM_Class som = new SOM_Class(cepo.size(), MAX_CLUSTERS, Alpha, MIN_ALPHA, DECAY_RATE, VEC_LEN);
		int[][] um = {cepo_num[0], cepo_num[1], cepo_num[2], cepo_num[3]};
		int[][] dois = {cepo_num[4], cepo_num[5], cepo_num[6], cepo_num[7]};
		som.Train(um, dois);
		som.Test(um, dois);
		
		

	}

}

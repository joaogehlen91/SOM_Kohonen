/*
 * Nomes: Elias Fank, João Gehlen, Ricardo Zanuzzo
 * Disciplina: Inteligencia Artificial
 * 
 * 2017/1
 * 
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SOM_Class {
	private int trainSet[][];
	private double mapa[][][];
	private String mapaRotulado[][];
	private String mapaRotuladoTrain[][];
	private int acuracia[][];
	private int dimMatNeuronios;
	private int maxEpocas;
	private double constanteTemporal;
	private double taxaAprendizadoInicial;
	private double raioInicial;
	private int melhorI, melhorJ;


	public SOM_Class(int input_set[][], int dim, int qtEpocas, double raioInicial, double taxa){
		this.taxaAprendizadoInicial = taxa;
		this.raioInicial = raioInicial;
		this.dimMatNeuronios = dim;
		this.acuracia = new int[2][10];
		this.trainSet = input_set;
		this.mapa = criaMatrizQuadAleatorio(this.dimMatNeuronios);
		this.maxEpocas = qtEpocas;
		this.constanteTemporal = (double)(this.maxEpocas/Math.log(Math.pow(this.dimMatNeuronios,2)/2));
		this.mapaRotulado = new String[dim][dim];
		this.mapaRotuladoTrain = new String[dim][dim];
		return;
	}

	private double[][][] criaMatrizQuadAleatorio(int dim) {
		double w[][][] = new double[dim][dim][1024];
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[0].length; j++) {
				for (int k=0; k < w[0][0].length; k++)
					w[i][j][k] = new Random().nextDouble();
			}
		}
		return w;
	}

	public void treinamento(String[] label){
		double[] bmu = new double[1024];
		double raio, taxaAp;
		ProgressBar pb = new ProgressBar(System.currentTimeMillis());
		System.out.println("\nPROGRESSO DE TREINAMENTO");
		
		for (int epoca = 1; epoca <= this.maxEpocas ; epoca++) {
			pb.printProgress(this.maxEpocas, epoca);
			raio = raio_vizinhanca(epoca);
			//System.out.println("Raio:"+ raio);
			//shuffleArray(trainSet);
			taxaAp = this.taxa_aprendizado(epoca);

			for (int x = 0; x < trainSet.length; x++) {
				maisProx(trainSet[x]);
				bmu = mapa[this.melhorI][this.melhorJ];
				mapaRotuladoTrain[this.melhorI][this.melhorJ] = label[x];

				atualiza_pesos(x, bmu, taxaAp, raio);
			}
			
		}

	}
	
	public void atualiza_pesos(int x, double[] bmu, double taxaAp, double raio){
		double delta, influencia_vizinhanca;
		for(int i = 0; i< this.dimMatNeuronios; i++){
			for(int j = 0; j< this.dimMatNeuronios; j++){
				for(int k = 0; k< 1024; k++){
					influencia_vizinhanca = this.influencia_vizinhanca(bmu, this.melhorI, this.melhorJ, i, j, raio);
					delta = taxaAp * influencia_vizinhanca * (trainSet[x][k] - mapa[i][j][k]);
					mapa[i][j][k] += delta;
				}
			}
		}
	}
	

	public void teste(int test_set[][], String labels[]){
		for(int i=0;i<test_set.length;i++){
			maisProx(test_set[i]);
			mapaRotulado[this.melhorI][this.melhorJ] = labels[i];
			int n = Integer.parseInt(labels[i]);
			if(labels[i].equals(mapaRotuladoTrain[this.melhorI][this.melhorJ])){
				this.acuracia[0][n] += 1;
			}else{
				this.acuracia[1][n] += 1;
			}
		}
	}

	static void shuffleArray(int[][] ar){
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--){
			int index = rnd.nextInt(i + 1);
			int a[] = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	private double influencia_vizinhanca(double[] bmu, int i, int j, int x, int y, double raio_vizinhanca) {
		double distance = Math.sqrt(Math.pow(i-x, 2) + Math.pow(j-y, 2));
		return Math.exp(-Math.pow(distance, 2) / (2 * Math.pow(raio_vizinhanca, 2)));
	}

	private double raio_vizinhanca( int epoca) {
		double qtNeuronios = (double)(this.dimMatNeuronios*this.dimMatNeuronios);
		return qtNeuronios * this.raioInicial * Math.exp(-(double)(epoca+1) / this.constanteTemporal);
	}
	
	private double taxa_aprendizado(int epoca) {
		return Math.exp(-(double)epoca / (double)this.maxEpocas) * this.taxaAprendizadoInicial;
	}

	private void maisProx(int[] data) {
		double[] aux = new double[1024];
		for(int i=0;i<1024;i++){
			aux[i] = (double) data[i];
		}
		
		double min = Double.MAX_VALUE;
		double dist;
		
		for (int i = 0; i < this.dimMatNeuronios; i++) {
			for (int j = 0; j < this.dimMatNeuronios; j++) {
				dist = distancia(mapa[i][j], aux);

				if(dist < min){
					min = dist;
					this.melhorI = i;
					this.melhorJ = j;
				}
			}
		}
				
	}


	private double distancia(double[] a, double[] b){
		double dist=0;
		for (int i = 0; i < a.length; i++){
			dist+=Math.pow(a[i]-b[i],2);
		}
		return Math.sqrt(dist);
	}

	public void imprimeNeuronios(){
		for  (int i=0;i<this.dimMatNeuronios;i++)
			for  (int j=0;j<this.dimMatNeuronios;j++){
				System.out.println();
				imprimeNeuronio(i,j);
			}
	}

	public void imprimeNeuronio(int i, int j) {
		for(int x = 0; x<1024;x++){
			System.out.print(Math.round(mapa[i][j][x]));
			if((x+1)%32==0)
				System.out.println();
		}
		System.out.println();
	}

	public void escreveDesenhoNeurArquivo(String arquivo)throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer;
		writer = new PrintWriter(arquivo, "UTF-8");
		for (int i=0;i<this.dimMatNeuronios;i++){

			for (int j=0;j<32;j++){
				for (int k=0;k<this.dimMatNeuronios;k++){
						for(int l=(j*32); l<((j*32)+32);l++){
							writer.print(Math.round(mapa[i][k][l]));
						}
						writer.print("    ");
					}
					writer.println();
				}
			writer.println("\n");
			}
		writer.close();
	}


	public void escreveMapaArquivo(String arquivo)throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer;
		writer = new PrintWriter(arquivo, "UTF-8");
		writer.println("MAPA DA REDE");
		writer.println("Uma posição com um . significa um neurônio que não foi excitado.");
		for (int i=0; i<=(this.dimMatNeuronios*2)+4;i++) writer.print("#");
			writer.println();
		for (int i=0;i<this.dimMatNeuronios;i++){
			writer.print("## ");
			for (int j=0;j<this.dimMatNeuronios;j++){

				if(mapaRotulado[i][j] == null)
					writer.print(". ");
				else
					writer.print(mapaRotulado[i][j]+" ");
			}
			writer.print("##");
			writer.println();
		}
		for (int i=0; i<=(this.dimMatNeuronios*2)+4;i++) writer.print("#");
		writer.println("\n");
		imprimeAcuracia(arquivo, writer);
	
		writer.close();
	}
	
	public void imprimeMapaRotuladoTrain(){
		for (int i = 0; i < this.dimMatNeuronios; i++) {
			for (int j = 0; j < this.dimMatNeuronios; j++) {
				System.out.print(mapaRotuladoTrain[i][j]);
			}
			System.out.println();
		}
	}
	
	public void imprimeAcuracia(String arquivo, PrintWriter writer){
		double acumAcerto = 0;
		double acumErro = 0;
		writer.println("ACURÁCIA");
		writer.println("Acuracia por cluster:");
		for (int i = 0; i < 10; i++) {
			double pAcerto = (double)acuracia[0][i] / (double)(acuracia[0][i] + acuracia[1][i]);
			acumAcerto += (double)acuracia[0][i];
			acumErro += (double)acuracia[1][i];
			
			//System.out.println("% acertos do numero " + i +": " + ((double)Math.round(pAcerto * 100.0) / 100.0) *100.0);
			writer.println("% acertos do numero " + i +": " + pAcerto * 100.0);
			
		}
		double acuracia = acumAcerto / (acumAcerto + acumErro);
		writer.print("Acuracia da Rede: ");
		//System.out.println(( (double)Math.round(acuracia * 100.0) / 100.0) * 100.0);
		writer.println(acuracia * 100.0 + "%");
	}
		
}

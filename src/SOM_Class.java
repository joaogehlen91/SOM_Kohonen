import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

public class SOM_Class {
	private int input_set[][];
	private double w[][];
	private int nClusters;
	private int vecLen;
	private double d[]; // Network nodes.

	// About 100 iterations.
	private static double alpha = 0.6;
	private static final double DECAY_RATE = 0.993;
	private static final double MIN_ALPHA = 0.01;
	private static final double RADIUS_REDUCTION_POINT = 0.063; // Last 20% of
																// iterations.

	private static final int VEC_XLEN = 10;
	private static final int VEC_YLEN = 10;
	private static PrintWriter writer;

	public SOM_Class(int nClusters, int input_set[][]) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter("arquivos/result.txt", "UTF-8");
		this.nClusters = nClusters;
		this.input_set = input_set;
		this.d = new double[nClusters];
		this.vecLen = input_set[0].length;
		w = criaMatrizAleatorio(this.nClusters, this.input_set[0].length);
		// for(int i=0; i<w.length; i++) {
		// for(int j=0; j<w[0].length; j++) {
		// if((j+1)%32 == 0)
		// System.out.println();
		// System.out.print(w[i][j]);
		// }
		// System.out.println();
		// }
		return;
	}

	private double[][] criaMatrizAleatorio(int maxClusters, int mVecLen) {
		Random r = new Random();
		double w[][] = new double[maxClusters][mVecLen];
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[0].length; j++) {
				w[i][j] = new Random().nextDouble();
			}
		}
		return w;
	}

	// private double[][] criaMatrizAleatorio(int maxClusters, int mVecLen) {
	// Random r = new Random();
	// double w[][] = new double[maxClusters][mVecLen];
	//
	// //0
	// for(int i = 0; i < 1024; i++)
	// w[0][i] = (double) (this.input_set[0][i]);
	// //1
	// for(int i = 0; i < 1024; i++)
	// w[1][i] = (double) (this.input_set[11][i]);
	// //2
	// for(int i = 0; i < 1024; i++)
	// w[2][i] = (double) (this.input_set[5][i]);
	// //3
	// for(int i = 0; i < 1024; i++)
	// w[3][i] = (double) (this.input_set[14][i]);
	// //4
	// for(int i = 0; i < 1024; i++)
	// w[4][i] = (double) (this.input_set[3][i]);
	// //5
	// for(int i = 0; i < 1024; i++)
	// w[5][i] = (double) (this.input_set[6][i]);
	// //6
	// for(int i = 0; i < 1024; i++)
	// w[6][i] = (double) (this.input_set[4][i]);
	// //7
	// for(int i = 0; i < 1024; i++)
	// w[7][i] = (double) (this.input_set[2][i]);
	// //8
	// for(int i = 0; i < 1024; i++)
	// w[8][i] = (double) (this.input_set[9][i]);
	// //9
	// for(int i = 0; i < 1024; i++)
	// w[9][i] = (double) (this.input_set[12][i]);
	//
	// return w;
	// }

	public void train() {
		int iterations = 0;
		boolean reductionFlag = false;
		int reductionPoint = 0;
		int dMin = 0;

		while (alpha > MIN_ALPHA) {
			iterations += 1;
			System.out.println("Iterations: " + iterations+"alpha: "+ alpha );

			for (int vecNum = 0; vecNum < this.input_set.length; vecNum++) {
				// Compute input for all nodes.
				computeInput(this.input_set[vecNum]);

				// See which is smaller?
				dMin = minimum(d);

				// Update the weights on the winning unit.
				updateWeights(vecNum, dMin);

			} // VecNum

			// Reduce the learning rate.
			alpha = DECAY_RATE * alpha;

			// Reduce radius at specified point.
			if (alpha < RADIUS_REDUCTION_POINT) {
				if (reductionFlag == false) {
					reductionFlag = true;
					reductionPoint = iterations;
				}
			}
		}

		System.out.println("Iterations: " + iterations);
		System.out.println("Neighborhood radius reduced after "
				+ reductionPoint + " iterations.");

		return;
	}

	private void computeInput(int[] vectorArray) {
		clearArray(d);

		for (int i = 0; i <= (this.nClusters - 1); i++) {
			for (int j = 0; j <= (this.vecLen - 1); j++) {
				d[i] += Math.pow((w[i][j] - vectorArray[j]), 2);
			} // j
		} // i
		return;
	}

	private void updateWeights(int iEntrada, int winner){
		
//		for (int i = 0; i < vecLen; i++) {
//			w[winner][i] = w[winner][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner][i]));
//		}

		if(alpha > RADIUS_REDUCTION_POINT){
			// atualiza o vencedor
			for (int i = 0; i < vecLen; i++) {
				w[winner][i] = w[winner][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner][i]));
			}			
			// atualiza os vizinhos
			int j = winner+1;
			if(winner == w.length) j = winner-1;
			

			//w[winner] dist w[j];
			//double dist[] = new double[nClusters];
//			double dist = 0;
//			for (int i = 0; i <= (this.vecLen - 1); i++) {
//				dist += Math.pow((w[winner][i] - w[j][i]), 2);
//			}
//			dist = dist * dist;
			
			if(winner > 1 && winner < w.length -2){
				for (int i = 0; i < vecLen; i++) {
					w[winner+1][i] = w[winner+1][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner+1][i]));
					w[winner+2][i] = w[winner+2][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner+2][i]));
				}
				for (int i = 0; i < vecLen; i++) {
					w[winner-1][i] = w[winner-1][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner-1][i]));
					w[winner-2][i] = w[winner-2][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner-2][i]));
				}
			}
			
			if(winner == 0 || winner == 1){
				for (int i = 0; i < vecLen; i++) {
					w[winner+1][i] = w[winner+1][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner+1][i]));
					w[winner+2][i] = w[winner+2][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner+2][i]));
					w[winner+3][i] = w[winner+3][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner+3][i]));
					w[winner+4][i] = w[winner+4][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner+4][i]));
				}
			}
			
			if(winner == w.length || winner == w.length - 2){
				for (int i = 0; i < vecLen; i++) {
					w[winner-1][i] = w[winner-1][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner-1][i]));
					w[winner-2][i] = w[winner-2][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner-2][i]));
					w[winner-3][i] = w[winner-3][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner-3][i]));
					w[winner-4][i] = w[winner-4][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner-4][i]));
				}
			}
			
			
			
		}else{
			// atualiza o vencedor
			for (int i = 0; i < vecLen; i++) {
				w[winner][i] = w[winner][i]	+ (alpha * (this.input_set[iEntrada][i] - w[winner][i]));
			}			
		}
		
		
		
		
		
		

	}
	
	/*private void updateWeights(int vectorNumber, int dMin) {
		int y = 0;
		int PointA = 0;
		int PointB = 0;
		boolean done = false;

		for (int i = 0; i < this.vecLen; i++) {
			// Only include neighbors before radius reduction point is reached.
			// if(alpha > RADIUS_REDUCTION_POINT){
			y = 1;
			while (!done) {
				if (y == 1) { // Top row of 3.
					if (dMin > VEC_XLEN - 1) {
						PointA = dMin - VEC_XLEN - 1;
						PointB = dMin - VEC_XLEN + 1;
					} else {
						y = 2;
					}
				}
				if (y == 2) { // Middle row of 3.
					PointA = dMin - 1;
					// DMin is like an anchor position right between these two.
					PointB = dMin + 1;
				}
				if (y == 3) { // Bottom row of 3.
					if (dMin < (VEC_XLEN * (VEC_YLEN - 1))) {
						PointA = dMin + VEC_XLEN - 1;
						PointB = dMin + VEC_XLEN + 1;
					} else {
						done = true;
					}
				}

				if (!done) {
					for (int DIndex = PointA; DIndex < PointB; DIndex++) {
						// Check if anchor is at left side.
						if (dMin % VEC_XLEN == 0) {
							// Check if anchor is at top.
							if (DIndex > PointA) {
								w[DIndex][i] = w[DIndex][i]
										+ (alpha * (this.input_set[vectorNumber][i] - w[DIndex][i]));
							}
							// Check if anchor is at right side.
						} else if ((dMin + 1) % VEC_XLEN == 0) {
							// Check if anchor is at top.
							if (DIndex < PointB) {
								w[DIndex][i] = w[DIndex][i]
										+ (alpha * (this.input_set[vectorNumber][i] - w[DIndex][i]));
							}
							// Otherwise, anchor is not at either side.
						} else {
							w[DIndex][i] = w[DIndex][i]
									+ (alpha * (this.input_set[vectorNumber][i] - w[DIndex][i]));
						}
					} // DIndex
				}

				if (y == 3) {
					done = true;
				}
				y += 1; // prepare to start the next row.

			}
			
			  }else if(alpha <= RADIUS_REDUCTION_POINT){ // Update only the
			  winner. w[dMin][i] = w[dMin][i] + (alpha *
			  (this.input_set[vectorNumber][i] - w[dMin][i])); }
			 

		} // i
		return;
	}
	*/

	private void clearArray(double[] nodeArray) {
		for (int i = 0; i < this.nClusters; i++) {
			nodeArray[i] = 0.0;
		} // i
		return;
	}

	private int minimum(double[] nodeArray) {
		int winner = 0;
		boolean foundNewWinner = false;
		boolean done = false;

		while (!done) {
			foundNewWinner = false;
			for (int i = 0; i < this.nClusters; i++) {
				if (i != winner) { // Avoid self-comparison.
					if (nodeArray[i] < nodeArray[winner]) {
						winner = i;
						foundNewWinner = true;
					}
				}
			} // i

			if (foundNewWinner == false) {
				done = true;
			}
		}
		return winner;
	}

	public void printResults() {
		// int i = 0;
		// int j = 0;
		int dMin = 0;

		// double aux[] = null;
		// ArrayList<Integer> pos = new ArrayList<Integer> ();

		Map<Double, Integer> map_cluster = new HashMap<Double, Integer>();
		Map<Integer, Double> map_distancia = new HashMap<Integer, Double>();

		// Print clusters created.
		System.out.println("Clusters for training input:");
		for (int vecNum = 0; vecNum < this.input_set.length; vecNum++) {
			// Compute input.
			computeInput(this.input_set[vecNum]);

			// Arrays.sort(d);
			// See which is smaller.
			dMin = minimum(d);

			// pos.add(dMin);
			map_cluster.put(d[dMin], dMin);
			map_distancia.put(vecNum + 1, d[dMin]);

			System.out.print("\nVector (");
			System.out.print("Pattern " + vecNum + ", " /* + names[vecNum] */);
			System.out.print(") fits into category " + dMin + " w: " + d[dMin]
					+ "\n");

		} // VecNum

		// System.arraycopy( d, 0, aux, 0, d.length );
		// aux = d.clone();
		// Arrays.sort(aux);

		Map<Integer, Double> sorted_map = new HashMap<Integer, Double>();
		sorted_map = sortHashMapByValues(map_distancia);
		
		
		//Integer cluster = getKeyFromValue(map_cluster, value);

		
			
			
			for (Map.Entry<Integer, Double> entry : sorted_map.entrySet()) {
				Integer key = entry.getKey();
				Double value = entry.getValue();
				Integer cluster = getKeyFromValue(map_cluster, value);
				
				writer.println("Distacia = " + value + " Cluster = " + cluster
						+ " Entrada(1 a 1934) = " + key);
				
				imprimeNeuronio(cluster);
				
			}
			writer.close();
		
		

		// for (int i=0; i < aux.length; i++) System.out.println("Cluster[" + i
		// + "]=" + aux[i]);
		// for (Integer integer : pos) System.out.println(integer);

		// The weight matrix is HUGE, and I'd found the output easier to read
		// just by commenting out that part...
		// Print weight matrix.
		// System.out.println();
		// for(int i = 0; i < MAX_CLUSTERS - 1;i++)
		// {
		// System.out.println("Weights for Node " + i + " connections:");
		// for(int j = 0; j < VEC_LEN - 1; j++)
		// {
		// String temp = String.format("%.3f", w[i][j]);
		// System.out.print(temp + ", ");
		// } // j
		// System.out.println("\n");
		// } // i

	}

	private void imprimeNeuronio(int cluster) {
		int v=0;
		for (double s : w[cluster]) {
			v++;
			writer.print(Math.round(s));
			if(v==32){
				writer.println();
				v=0;
			}
		}
	}

	// Ordena HashMap
	public LinkedHashMap<Integer, Double> sortHashMapByValues(
			Map<Integer, Double> sorted_map) {
		ArrayList<Integer> mapKeys = new ArrayList<>(sorted_map.keySet());
		ArrayList<Double> mapValues = new ArrayList<>(sorted_map.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

		Iterator<Double> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Double val = valueIt.next();
			Iterator<Integer> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Integer key = keyIt.next();
				Double comp1 = sorted_map.get(key);
				Double comp2 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}

	public Integer getKeyFromValue(Map<Double, Integer> hm, Double value) {
		for (Map.Entry<Double, Integer> entry : hm.entrySet()) {

			if (Double.compare(entry.getKey(), value) == 0) {
				return entry.getValue();
			}
		}
		return null;
	}

}
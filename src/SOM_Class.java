import java.util.Random;


public class SOM_Class {
	private int mVectors;
	private int mVecLen;
	private int maxClusters;
	private double minAlpha;
	private double mAlpha;
	private double decayRate;
	private int mIterations;
	private double D[];
	private double w[][];
	
	public SOM_Class(int numVectors, int Maximum_Clusters, double Alpha_Start, double Minimum_Alpha, double Decay_Rate, int Vector_Length)
	{
		mVectors = numVectors;
		mVecLen = Vector_Length;
		mAlpha = Alpha_Start;
		minAlpha = Minimum_Alpha;
		decayRate = Decay_Rate;
		mIterations = 0;
		maxClusters = Maximum_Clusters;
		D = new double[maxClusters];
		w = criaMatrizAleatorio(maxClusters, mVecLen);
		
		return;
	}
	
	private double[][] criaMatrizAleatorio(int maxClusters, int mVecLen) {
		Random r = new Random();
		double w[][] = new double[maxClusters][mVecLen];
		for(int i=0; i<w.length; i++){
			for(int j=0; j<w[0].length; j++){
				double randomValue = 0 + (1 - 0) * r.nextDouble();
				w[i][j] = randomValue;
			}
		}
		return w;
	}

	public void Train(int Patterns[][], int TrainingTests[][])
	{
		int Iterations = 0;
		int i;
		int VecNum;
		int DMin;

	    while(mAlpha > minAlpha)
	    {
	        Iterations += 1;

	        for(VecNum = 0; VecNum < mVectors; VecNum++)
	        {
	            // Compute input.
	            ComputeInput(VecNum, TrainingTests);

	            // See which is smaller, D(0) or D(1)?
	            DMin = Minimum(D[0], D[1]);
	            //System.out.println("Closest is D(" + DMin + ")");

	            // Update the weights on the winning unit.
	            for(i = 0; i < mVectors; i++)
	            {
	                w[DMin][i] = w[DMin][i] + (mAlpha * (Patterns[VecNum][i] - w[DMin][i]));
	                //System.out.println(" w(" + i + ")= " + w[DMin][i]);
	            }

	        }

	        // Reduce the learning rate.
	        mAlpha = decayRate * mAlpha;

	    }
	    return;
	}
	
	public void Test(int Patterns[][], int TrainingTests[][])
	{
		int i, j;
		int VecNum;
		int DMin;
		java.text.DecimalFormat dfm = new java.text.DecimalFormat("###.000"); 

		// Print clusters created.
		System.out.println("Clusters for training input:");
		
	    for(VecNum = 0; VecNum < mVectors; VecNum++)
	    {
	        // Compute input.
	        ComputeInput(VecNum, TrainingTests);

	        // See which is smaller, D(0) or D(1)?
	        DMin = Minimum(D[0], D[1]);

	        System.out.print("\nVector ( ");
	        for(i = 0; i < mVectors; i++)
	        {
	        	System.out.print(Patterns[VecNum][i] + ", ");
	        } // i
	        System.out.println(") fits into category " + DMin);
	    } // VecNum

		// Print weight matrix.
	    System.out.println();
	    for(i = 0; i < maxClusters; i++)
	    {
	    	System.out.println("Weights for Node " + i + " connections:");
	        for(j = 0; j < mVecLen; j++)
	        {
	            System.out.print(dfm.format(w[i][j]) + ", ");
	        } // j
	        System.out.println();
	    } // i

		// Print post-training tests.
	    System.out.println("Categorized test input:");
	    for(VecNum = 0; VecNum < mVectors; VecNum++)
	    {
	        // Compute input.
	        ComputeInput(VecNum, TrainingTests);

	        // See which is smaller, D(0) or D(1)?
	        DMin = Minimum(D[0], D[1]);

	        System.out.print("\nVector ( ");
	        for(i = 0; i < mVectors; i++)
	        {
	            System.out.print(TrainingTests[VecNum][i] + ", ");
	        } // i
	        System.out.println(") fits into category " + DMin);

	    } // VecNum
	    return;
	}
	
	private void ComputeInput(int VectorNumber, int Training_Tests[][])
	{
		int i, j;
		
	    D[0] = 0.0;
	    D[1] = 0.0;
	    for(i = 0; i < maxClusters; i++)
	    {
	        for(j = 0; j < mVectors; j++)
	        {
	            D[i] += Math.pow((w[i][j] - Training_Tests[VectorNumber][j]), 2);
	            
	        } // j
	    } // i
	    return;
	}
	
	private int Minimum(double ValueA, double ValueB)
	{
		if(ValueA > ValueB){
        	return 1;
		}else{
        	return 0;
		}
	}
	
	public int Iterations()
	{
		return mIterations;
	}
	
}
import java.lang.management.*;
import java.util.*;
import java.text.*;

public class IsingTester
{		
		
	public static void main(String []args)
	{
		

		DecimalFormat numberFormat = new DecimalFormat("#.00");
		DecimalFormat numberFormat2 = new DecimalFormat("#.0000");
		
		double n=Double.parseDouble(args[0]);
		int mcs=10000;
		int burnin=mcs/10;
		
		double j=1;
		double bias=0.52;
		double metSteps=n*n;
	
		double initialTempCoefficient=0.5;
		double finalTempCoefficient=5;
		double tempStep=.1;
		int numTemps=(int)((finalTempCoefficient-initialTempCoefficient)/(tempStep))+1;
		int totalNumberOfTrialsPerTemperature=100;

		printConditions(n,mcs,metSteps,initialTempCoefficient,tempStep,finalTempCoefficient,totalNumberOfTrialsPerTemperature,args);

	
		double[] allAveragesOfAllTemps=new double[numTemps];	
		double[] tempsTested=new double[numTemps];

		int tempIndex=0;

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		long start = bean.getCurrentThreadUserTime();
		System.out.println("Temperature"+"\t"+"Magnetization");

		for (double temp=initialTempCoefficient;temp<=finalTempCoefficient;temp=temp+tempStep) //temperature loop
		{
			double kT= temp/(Math.log(1+Math.sqrt(2)));//update kT
			tempsTested[tempIndex]=temp;//keep track of temperatures in an array
			
			double[] allTrialsAtOneTemp=new double[totalNumberOfTrialsPerTemperature];


			for (int trialNumber=0;trialNumber<totalNumberOfTrialsPerTemperature;trialNumber++ ) 
			{
				
				
				Grid test;
				


				if(args[1].equalsIgnoreCase("Regular"))
				{
					test=new Grid(n,kT,j,bias,metSteps);
				}
				else if(args[1].equalsIgnoreCase("Dipole1"))
				{	
					test=new GridDipole1(n,kT,j,bias,metSteps);//create new matrix for each test
				}
				else if(args[1].equalsIgnoreCase("Dipole2"))
				{	
					test=new GridDipole2(n,kT,j,bias,metSteps);//create new matrix for each test
				}
				else if(args[1].equalsIgnoreCase("Split"))
				{	
					test=new SplitDipole(n,kT,j,bias,metSteps);//create new matrix for each test
				}
				else if(args[1].equalsIgnoreCase("Bond"))
				{	
					test=new GridBondDefect(n,kT,j,bias,metSteps);//create new matrix for each test
				}

				else
				{
					test=null;
					System.err.println("Invalid Grid Type");
					System.exit(1);

				}
				

				double[] magnetValuesAtOneTemp=new double[mcs];//keeps track of magnetization values for each temperature

				for (int k=0;k<mcs ;k++ ) //monte carlo loop
				{
					test.metropolis(metSteps);//calls metropolis
					magnetValuesAtOneTemp[k]=Math.abs((test.magnetValue));//records the magnetization value

				}

			
				
				//test.isingEnergy();//updates energy 
				double sumForAvgOneTempForOneTrial=0;
				for(int indiciesToAverage=burnin;indiciesToAverage<mcs;indiciesToAverage++)//loop to average magnetizations
				{
					sumForAvgOneTempForOneTrial=magnetValuesAtOneTemp[indiciesToAverage]+sumForAvgOneTempForOneTrial;
				}
				double avgMagnetizationAtOneTempForOneTrial=sumForAvgOneTempForOneTrial/((double)(mcs-burnin));

				
				allTrialsAtOneTemp[trialNumber]=avgMagnetizationAtOneTempForOneTrial;

				
				
			}// end of 100 trials loop
			
			double sumForAvgOneTempForAllTrials=0;
			for (int indiciesToAverage=0;indiciesToAverage<allTrialsAtOneTemp.length;indiciesToAverage++) 
			{
				sumForAvgOneTempForAllTrials=allTrialsAtOneTemp[indiciesToAverage]+sumForAvgOneTempForAllTrials;	
			}
			double avgMagnetizationAtOneTempForAllTrials=sumForAvgOneTempForAllTrials/(double)(allTrialsAtOneTemp.length);


			allAveragesOfAllTemps[tempIndex]=avgMagnetizationAtOneTempForAllTrials;
			
			System.out.printf(numberFormat.format(tempsTested[tempIndex])+"\t \t"+numberFormat2.format(Math.pow((Math.abs(allAveragesOfAllTemps[tempIndex])),2)));
			System.out.println();
			tempIndex++;

			


		}// end of temp loop
		System.out.println("Temperature"+"\t"+"Magnetization");
		for(tempIndex=0;tempIndex<tempsTested.length;tempIndex++)
		{
			System.out.printf(numberFormat.format(tempsTested[tempIndex])+"\t \t"+numberFormat2.format(Math.pow((Math.abs(allAveragesOfAllTemps[tempIndex])),2)));
			System.out.println();
			
		}

					

		long end = bean.getCurrentThreadUserTime();
		System.out.println("Computation Time: "+(end - start) / 1e+9);

	}

	public static void printConditions(double n, double mcs, double metSteps, double initialTempCoefficient, double tempStep, double finaLTempCoefficient,int totalNumberOfTrialsPerTemperature, String[]args)
	{
		System.out.println("Conditions: ");
		System.out.println("n: "+n);
		System.out.println("mcs: "+mcs);
		System.out.println("metSteps: "+metSteps);
		System.out.println("initialTempCoefficient: "+initialTempCoefficient);
		System.out.println("tempStep: "+tempStep);
		System.out.println("finalTempCoefficient: "+finaLTempCoefficient);
		System.out.println("totalNumberOfTrialsPerTemperature: "+totalNumberOfTrialsPerTemperature);
		System.out.println("Type of Test: "+args[1]);
	}
	public static double[][] sumMatries(double[][] arr1, double[][] arr2,double n)
	{
		double[][] sumOfMatrcies=new double[(int)n][(int)n];

		for(int row=0;row<arr1.length;row++)
		{
			for(int col=0;col<arr1[0].length;col++)
			{
				sumOfMatrcies[row][col]=(arr1[row][col]+arr2[row][col]);
			}
		}

		return sumOfMatrcies;
	}

	public static void printCorrelationGrid(double[][] array)
	{
		for(double [] i:array) //test to print out grid
		{
			for (double k:i) 
			{
				//System.out.print(k);
				DecimalFormat numberFormat2 = new DecimalFormat("#.0000");

				System.out.printf(numberFormat2.format(k));
				System.out.print("\t");


			}
			System.out.println();
		}
	}

	public static double[][] sumMatriesRegular(double[][] arr1, double[][] arr2,double n)
	{
		double[][] sumOfMatrcies=new double[(int)n][(int)n];

		for(int row=0;row<arr1.length;row++)
		{
			for(int col=0;col<arr1[0].length;col++)
			{
				sumOfMatrcies[row][col]=(arr1[row][col]+arr2[row][col]);
			}
		}
		return sumOfMatrcies;

	}

	public static double[][] avgMatrix(double[][] arr1,double n, int trials)
	{
		double[][] avgOfMatrcies=new double[(int)n][(int)n];

		for(int row=0;row<arr1.length;row++)
		{
			for(int col=0;col<arr1[0].length;col++)
			{
				avgOfMatrcies[row][col]=(arr1[row][col])/trials;
			}
		}
		return avgOfMatrcies;

	}


}
public class Grid
{
	public double [][] matrix;
	public double [][] neighbors;
	public double n;
	public double kT;
	public double j;
	public double bias;
	public double metSteps;
	public double magnetValue;
	public double energy;

	public double matrixSum;





	//constructor
	public Grid(double n,double kT, double j, double bias, double metSteps)
	{
		this.n=n;
		this.kT=kT;
		this.j=j;
		this.bias=bias;
		this.metSteps=metSteps;

		//following three method calls generate/calculate the initial matrix, magnetization, and energy
		generateRandomGrid();
		this.matrixSum=sum(matrix);
		neighbors=new double[(int)n][(int)n];
		sumNeighbors();

		calculateMagnetization();
		
		

	}
	//creates a random configuration of spins
	public void generateRandomGrid()
	{
		double[][] uninitialized=new double[(int)n][(int)n];
		
		for(int row=0;row<uninitialized.length;row++)
		{
			for (int col=0;col<uninitialized[0].length;col++) 
			{
				uninitialized[row][col]=bias-Math.random();

				if (uninitialized[row][col]>0) 
				{
					uninitialized[row][col]=1;	
				}
				else if(uninitialized[row][col]==0)
				{
					uninitialized[row][col]=0;	
				}
				else
				{
					uninitialized[row][col]=-1;	
				}


			}
		}
		this.matrix=uninitialized;
	}
	//calculates the average magnetization
	public void calculateMagnetization()
	{
		this.magnetValue=this.matrixSum/(n*n);
	}

	//calculates the energy
	public void isingEnergy()
	{

		double[][] multiplied = new double[matrix.length][neighbors[0].length];
      

        for (int i = 0; i < matrix.length; i++) { // aRow
            for (int j = 0; j < neighbors[0].length; j++) { // bColumn
                for (int k = 0; k < matrix[0].length; k++) { // aColumn
                    multiplied[i][j] += matrix[i][k] * neighbors[k][j];
                }
            }
        }

        this.energy= -j*(sum(multiplied)/(n*n));
	}

	//sums values in matrix
	public double sum(double[][] array)
	{
		double sumValue=0;
		for(int row=0;row<array.length;row++) //test to print out grid
		{
			for (int col=0;col<array[0].length;col++) 
			{
				sumValue=sumValue+array[row][col];


			}
		}
		return sumValue;

	}

	//prints grid –not called by any method currently
	public void printGrid(double[][] array)
	{
		for(double [] i:array) //test to print out grid
		{
			for (double k:i) 
			{
				System.out.print(k);
				System.out.print("\t");


			}
			System.out.println();
		}
	}
	public void printCorrelationGrid(double[][] array)
	{
		for(double [] i:array) //test to print out grid
		{
			for (double k:i) 
			{
				System.out.print(k/4.0);
				System.out.print("\t");


			}
			System.out.println();
		}
	}

	// method that does the metropolis steps
	public void metropolis(double t)

	{
			
		for (int k=0;k<t ;k++) 
		{
		boolean positiveGoingToNegative=false;
		int randomX=(int)(Math.random() * n);
		int randomY=(int)(Math.random() * n);	
		
		double dE = 2*j*matrix[randomX][randomY]*neighbors[randomX][randomY];
		bias=Math.exp(-dE/kT);
		if (Math.random()<=bias) 
			{
			if(matrix[randomX][randomY]<0)
			{	
				positiveGoingToNegative=false;
				matrix[randomX][randomY]= (-matrix[randomX][randomY]);
				updateNeighbors(randomX,randomY,positiveGoingToNegative);
				this.matrixSum=this.matrixSum+2;
				//isingEnergy();
	
			}
			else if(matrix[randomX][randomY]>0)
			{
				positiveGoingToNegative=true;
				matrix[randomX][randomY]= (-matrix[randomX][randomY]);	
				updateNeighbors(randomX,randomY,positiveGoingToNegative);
					
					

				this.matrixSum=this.matrixSum-2;
				//isingEnergy();
	

			}
			else
			{

			}

			
			}
		if(k%(n*n)==0)
		{
			calculateMagnetization();
			//isingEnergy();

		}
		

		}


	}

	
	public void sumNeighbors()
	{
		
		int rightIndex, leftIndex, upIndex,downIndex;
		for (int row=0; row<matrix.length;row++ ) 
		{
			for (int col=0;col<matrix.length;col++ )
			{
				rightIndex=(col+1)%matrix.length;
				leftIndex=(col-1)%matrix.length;
				if(leftIndex<0)
				{
					leftIndex=(matrix.length-1);
				}
				upIndex=(row-1)%matrix.length;
				if (upIndex<0) 
				{
					upIndex=(matrix.length-1);
				}
				downIndex=(row+1)%matrix.length;

				neighbors[row][col]=matrix[row][rightIndex]+matrix[row][leftIndex]+matrix[upIndex][col]+matrix[downIndex][col];
			}
			
			
		}
	}

	

	public void updateNeighbors(int ranX, int ranY,boolean posToNeg)
	{
		int rightIndex, leftIndex, upIndex,downIndex;
		
		rightIndex=(ranY+1)%neighbors.length;
		leftIndex=(ranY-1)%neighbors.length;

		if(leftIndex<0)
			{
			leftIndex=(neighbors.length-1);
			}
		upIndex=(ranX-1)%neighbors.length;
		if (upIndex<0) 
			{
			upIndex=(neighbors.length-1);
			}
		downIndex=(ranX+1)%neighbors.length;


		if(posToNeg)

		{	
			neighbors[ranX][rightIndex]=neighbors[ranX][rightIndex]-2;
			neighbors[ranX][leftIndex]=neighbors[ranX][leftIndex]-2;
			neighbors[upIndex][ranY]=neighbors[upIndex][ranY]-2;
			neighbors[downIndex][ranY]=neighbors[downIndex][ranY]-2;
			

		}
		if(!posToNeg)
		{
			neighbors[ranX][rightIndex]=neighbors[ranX][rightIndex]+2;
			neighbors[ranX][leftIndex]=neighbors[ranX][leftIndex]+2;
			neighbors[upIndex][ranY]=neighbors[upIndex][ranY]+2;
			neighbors[downIndex][ranY]=neighbors[downIndex][ranY]+2;
		

		}
		
	}



}

public class SplitDipole extends Grid
{
	//constructor
	public SplitDipole(double n,double kT, double j, double bias, double metSteps)
	{
		super(n,kT,j,bias,metSteps);
		matrix[0][0]=1;
		matrix[(int)n/2][(int)n/2]=-1;
		
		

	}

	// method that does the metropolis steps
	public void metropolis(double t)

	{
		
			
			for (int k=0;k<t ;k++) 
			{
			boolean positiveGoingToNegative=false;
			int randomX=(int)(Math.random() * n);
			int randomY=(int)(Math.random() * n);
			boolean pickedDipole=false;	
			if(randomX==0&&randomY==0)
			{
				pickedDipole=true;
			}
			if(randomX==(int)n/2&&randomY==(int)n/2)
			{
				pickedDipole=true;
			}
			if(!pickedDipole)
			{
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
		correlationGenerator(1);
		correlationGenerator(2);
		correlationGenerator(3);
		correlationGenerator(4);
		


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


}

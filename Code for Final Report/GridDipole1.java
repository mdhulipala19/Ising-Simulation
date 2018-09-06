public class GridDipole1 extends Grid
{
	
	//constructor
	public GridDipole1(double n,double kT, double j, double bias, double metSteps)
	{
		 super(n,kT,j,bias,metSteps);
		 this.matrix[0][0]=1;

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
}

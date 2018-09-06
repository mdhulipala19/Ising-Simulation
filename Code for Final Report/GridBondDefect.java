public class GridBondDefect extends Grid
{

	
	//constructor
	public GridBondDefect(double n,double kT, double j, double bias, double metSteps)
	{
		super(n,kT,j,bias,metSteps);
	
		sumNeighborsDefect();

	}

	// method that does the metropolis steps
	public void metropolis(double t)

	{
			for (int k=0;k<t ;k++) 
			{
			boolean positiveGoingToNegative=false;
			int randomX=(int)(Math.random() * n);
			int randomY=(int)(Math.random() * n);
			boolean pickedDefect=false;	
			if((randomX==n/2&&randomY==n/2)||(randomX==(n/2)-1&&randomY==(n/2)))
			{
				pickedDefect=true;
			}
			
			if(!pickedDefect)
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
					else
					{
						positiveGoingToNegative=true;
						matrix[randomX][randomY]= (-matrix[randomX][randomY]);	
						updateNeighbors(randomX,randomY,positiveGoingToNegative);				
						this.matrixSum=this.matrixSum-2;
						//isingEnergy();
			

					}

					
					}
				if(k%(n*n)==0)
				{
					calculateMagnetization();
					//isingEnergy();

				}

			}
			else
			{
				double dE = 2*j*matrix[randomX][randomY]*neighbors[randomX][randomY];
				bias=Math.exp(-dE/kT);
				if (Math.random()<=bias) 
					{
					if(matrix[randomX][randomY]<0)
					{	
						positiveGoingToNegative=false;
						matrix[randomX][randomY]= (-matrix[randomX][randomY]);

						updateNeighborsDefect(randomX,randomY,positiveGoingToNegative);

						
						this.matrixSum=this.matrixSum+2;
						//isingEnergy();
			
					}
					else if(matrix[randomX][randomY]>0)
					{
						positiveGoingToNegative=true;
						matrix[randomX][randomY]= (-matrix[randomX][randomY]);	
						updateNeighborsDefect(randomX,randomY,positiveGoingToNegative);
					
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
		correlationGenerator(1);
		correlationGenerator(2);
		correlationGenerator(3);
		correlationGenerator(4);
	}
		


	}
	public void sumNeighborsDefect()
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

				if(row==n/2&&col==n/2)
				{
					neighbors[row][col]=matrix[row][rightIndex]-10*matrix[row][leftIndex]+matrix[upIndex][col]+matrix[downIndex][col];
				}
				else if(row==(n/2)&&col==(n/2)-1)
				{
					neighbors[row][col]=-10*matrix[row][rightIndex]+matrix[row][leftIndex]+matrix[upIndex][col]+matrix[downIndex][col];

				}
				else
				{
					neighbors[row][col]=matrix[row][rightIndex]+matrix[row][leftIndex]+matrix[upIndex][col]+matrix[downIndex][col];

				}
			}
			
			
		}
	}
	public void updateNeighborsDefect(int ranX, int ranY,boolean posToNeg)
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

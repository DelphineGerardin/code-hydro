package hydraulique;



public class CellAngle extends Cell
{
	//Hydraulic
	double K;
	
	public CellAngle()
	{
		
	}
	
	public CellAngle(double[] x, int direction)
	{
		super(x, direction);
	}
	
	// Definition of cylindric cell by radius and height
	public CellAngle(double x, double radius, int direction)
	{
		super(x, radius, direction);
	}
	
	public CellAngle(double x, double radius, double angle, int direction)
	{
		super(x, radius, angle, direction);
	}
	
	public void computeGeometricValues()
	{
		//this.sectionValue = this.neighbours[0].section[this.neighbours[0].direction];
		//this.dxValue = this.volume/this.sectionValue;
		
		//this.hydraulicLength = this.dx[this.neighbours[0].direction]+this.neighbours[0].dx[this.neighbours[0].direction];
		
	}
	
	
	public void computeHydraulicParameters()
	{
		/*if (this.velocity != 0)
		{
			this.reynolds = this.rho*this.velocity*this.hydraulicDiameter/this.mu;
			this.reynolds = 1.0E+05;
			// Correlation Blasius
			this.darcyCoef = 0.3164*Math.pow(this.reynolds,-0.25);
			
			this.pressureLoss = darcyCoef*(this.hydraulicLength/this.hydraulicDiameter)*(this.rho*Math.pow(this.velocity,2)/2);
		}
		else
		{
			this.pressureLoss=0;
		}
		*/
		this.pressureLoss = 0;
		
	}

}
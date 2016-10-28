package hydraulique;

public class Cell 
{
	//geometruc parameters
	double[] section= new double[3];
	double sectionValue = 0;
	int direction;
	Cell[] neighbours;
	double volume;
	double[] dx = new double[3];
	///double dxValue = 0;
	double cellNumber = 0;

	
	
	//hydraulic parameters
	double velocity = 1;
	//double newVelocity = 0;
	double gravity = 9.81;
	double z =0;
	double rho = 4125.2417; //valeur à 700°C - fichier excel de Daniel
	double hydraulicDiameter = 0;
	double hydraulicLength = 0;
	double pressureLoss = 0;
	double reynolds = 0;
	double darcyCoef = 0;
	double mu = 0.01014; // valeur à 700°C - fichier excel de Daniel
	double pumpPressure = 0;
	double massFlow = 1.;
	double mass=0;
	
	
	
	public Cell()
	{
		this.neighbours = new Cell[2];
	}
	
	// Definition of the cell by x,y,z
	public Cell(double[] x, int direction)
	{
		this.direction = direction;
		this.neighbours = new Cell[2];
		for (int i= 0; i<3; i++)
		{
			this.dx[i] = x[i]/2;	
		}
		this.section[0] = x[1]*x[2];
		this.section[1] = x[0]*x[2];
		this.section[2] = x[0]*x[1];
		
		this.volume = x[0]*x[1]*x[2];
		
		
		double perimeter = 0 ;
		for (int i= 0; i<3; i++)
		{
			if (direction != i)
			{
				perimeter += 2*x[i];
			}	
		}
		this.hydraulicDiameter=4*this.section[this.direction]/perimeter;
		this.mass=this.volume*this.rho;
	}
	
	// Definition of cylindric cell by radius and height
	public Cell(double length, double radius, int direction)
	{
		this.direction = direction;
		this.neighbours = new Cell[2];
		
		for (int i=0; i<3; i++)
		{
			if (direction == i)
			{
				this.dx[i] = length/2;
				this.section[i] = Math.PI*Math.pow(radius, 2);
			}
			else
			{
				this.dx[i] = radius;
				this.section[i] = 0;
			}
		}
		this.volume = length*Math.PI*Math.pow(radius, 2);
		
		this.hydraulicDiameter=2*radius;
		this.mass=this.volume*this.rho;
	}
	
	//Definition of a cylindric portion
	public Cell(double length, double radius, double angle, int direction)
	{
		this.direction = direction;
		this.neighbours = new Cell[2];
		
		for (int i=0; i<3; i++)
		{
			if (direction == i)
			{
				this.dx[i] = length/2;
				this.section[i] = (angle/2)*Math.pow(radius, 2);
			}
			else
			{
				this.dx[i] = radius/2;
				this.section[i] = 0;
			}
		}
		this.volume = length*(angle/2)*Math.pow(radius, 2);
		
		this.hydraulicDiameter = 2*radius;
		this.mass=this.volume*this.rho;
	}
	
	//Geometry construction
	public void addNeighbours(Cell... neighbours)
	{
		for (int i=0; i<neighbours.length; i++)
		{
			this.neighbours[i]=neighbours[i];
		}
		computeGeometricValues();
	}
	
	public void computeGeometricValues()
	{
		//this.sectionValue = this.section[this.direction];
		//this.dxValue = this.volume/this.sectionValue;
		
		//this.hydraulicLength = this.dx[this.direction]+this.neighbours[0].dx[this.direction];
		this.hydraulicLength = this.dx[this.direction];
	}
	
	//Hydraulic functions
	public void computeHydraulicParameters()
	{
		if (this.velocity != 0)
		{
			// WARNING!!!
			this.reynolds = this.rho*this.velocity*this.hydraulicDiameter/this.mu;
			//this.reynolds = 1.0E+05; // WARNING!!!
			// Correlation Blasius
			this.darcyCoef = 0.3164*Math.pow(this.reynolds,-0.25);
			
			this.pressureLoss = darcyCoef*(this.hydraulicLength/this.hydraulicDiameter)*(this.rho*Math.pow(this.velocity,2)/2);
			
		}
		else
		{
			this.pressureLoss=0;
		}
		
	}
	
	public boolean compute_v()
	{
		boolean sucess = true;
		//System.out.println("dP "+ this.pressureLoss);
		//System.out.println("z "+ this.z);
		
		double v2 = Math.pow(this.neighbours[0].velocity,2)+2*this.gravity*(this.neighbours[0].z-this.z)+2*(this.pumpPressure-this.pressureLoss-this.neighbours[0].pressureLoss)/this.rho;
		if (v2>=0)
		{
			//this.newVelocity = Math.sqrt(v2);
			this.velocity = Math.sqrt(v2);
			this.massFlow = this.rho*this.velocity*this.section[this.direction];
		}
		else
		{
			sucess = false;
			System.out.println(this.cellNumber + " V²= " + v2);
			//double dV = Math.sqrt(Math.pow(this.neighbours[0].velocity,2) - v2);
			//dV = (dV > 0.) ? dV : -dV;
			//this.newVelocity = this.velocity;
			//this.newVelocity = -Math.sqrt(-v2);
			//this.neighbours[0].increaseVelocity(dV);
		}
		
		/*if(this.cellNumber==5)// WARNING!!!
			this.newVelocity = 1.0;
		*/
		return sucess;
	}
	
	/*public void update_v()
	{
		this.velocity = this.newVelocity;
		this.massFlow = this.rho*this.velocity*this.section[this.direction];
	}*/
	
	public void normalizeMassFlow(double factor)
	{
		this.massFlow *= factor;
		this.velocity *= factor;
		if (this.massFlow == 0)
		{
			this.massFlow = factor*this.rho*this.section[this.direction];
			this.velocity = factor;
		}
		
	}
	//getter
	
	//setter
	public void setHeight(double z)
	{
		this.z = z;
	}
	
	public void setCellNumber(int number)
	{
		this.cellNumber = number;
	}

	public void setPumpPressure(double P)
	{
		this.pumpPressure = P;
	}
	
	public void setMassFlow(double Qm)
	{
		this.massFlow = Qm;
		this.velocity = Qm/(this.section[direction]*this.rho);
	}
	
	public void setVelocity(double V)
	{
		this.velocity = V;
		this.massFlow = this.section[direction]*this.rho*this.velocity;
	}

}

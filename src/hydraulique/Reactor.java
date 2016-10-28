package hydraulique;


import java.util.ArrayList;

public class Reactor 
{
	
	//Atributes
	Geometry geometry;
	ArrayList<Cell> cellList;
	
	Zone heatExchanger;
	Zone core;
	Zone superiorHorizontalPipe;
	Zone superiorVerticalPipe;
	Zone inferiorHorizontalPipe;
	Zone inferiorVerticalPipe;
	
	//Hydraulic
	double tCycle = 3.9;

	public Reactor(ArrayList<Cell> cellList)
	{
		this.cellList = cellList;
		
		heatExchanger = new Zone(2);
		core = new Zone(2);
		superiorHorizontalPipe = new Zone(0);
		superiorVerticalPipe = new Zone(2);
		inferiorHorizontalPipe = new Zone(0);
		inferiorVerticalPipe = new Zone(2);
		
		this.geometry = new Geometry(heatExchanger, 
						inferiorVerticalPipe, 
						inferiorHorizontalPipe, 
						core, 
						superiorHorizontalPipe, 
						superiorVerticalPipe);
	}
	
	public void computeInitialvelocity()
	{
		
	}
	
}

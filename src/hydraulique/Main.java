package hydraulique;


import java.util.ArrayList;


public class Main {

	public static void main(String[] args) 
	{
		// Data writter initializing
		Scribe sVelocity = new Scribe("velocity");
		
		
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		Reactor msfr = new Reactor(cellList);	
		msfr.geometry.createCellList(cellList);
		
		double timeStep = 1e-6;
		double time = 0;
		
		// initial guess
		double mass = msfr.geometry.volume*cellList.get(0).rho;
		double targetMassFlow = mass/1;
		
		double P = 0;
		for (int i = 0; i< cellList.size(); i++)
		{
			cellList.get(i).setMassFlow(targetMassFlow);
		}
		for (int i = 0; i< cellList.size(); i++)
		{
			cellList.get(i).computeHydraulicParameters();
			//P += cellList.get(i).pressureLoss;
		}
		P = 1000;
		msfr.superiorHorizontalPipe.cellList.get(0).setPumpPressure(P);
		double dP;
		for(int i = 0; i< cellList.size(); i++)
		{
			System.out.println("n=" + i + "  " +cellList.get(i).velocity);
			//dP = 2*cellList.get(i).gravity*(cellList.get(i).neighbours[0].z-cellList.get(i).z)-2*(cellList.get(i).pressureLoss-cellList.get(i).neighbours[0].pressureLoss);
			dP = -2*(cellList.get(i).pressureLoss-cellList.get(i).neighbours[0].pressureLoss);
			System.out.println("n=" + i + "  " +dP);
		}
		
		//mise à l'équilibre des pressions
		// pressure equilibrium
		double dV = 0.1;
		double v = 0;
		double vOld = 0;
		double circulatingTime = 0 ;
		double normalizationFactor = 1;
		double meanMassFlow = 0;
		int counter = 0;
		int minimalIterationNumber = 100;
		int printFrequency = minimalIterationNumber/10;
		boolean endOfLoop = false; 
		boolean loopSucess = true;
		
		/*do
		{
			vOld = v;
			
			for (int i = 0; i< cellList.size(); i++)
			{
				loopSucess = cellList.get(i).compute_v();
				if(loopSucess == false)
					break;
				else
				{
					cellList.get(i).update_v();
					cellList.get(i).computeHydraulicParameters();
					//System.out.println("\ti= "+ i + " ,v= " + cellList.get(i).velocity);
				}
				
			}
			
			if(loopSucess == true)
			{				
				circulatingTime = 0;
				for (int i = 0; i< cellList.size(); i++)
				{
					cellList.get(i).update_v();
					circulatingTime += cellList.get(i).volume * cellList.get(i).rho/ cellList.get(i).massFlow;
				}
			
				meanMassFlow = mass/circulatingTime;
				//normalizationFactor = targetMassFlow/meanMassFlow;
			
				if(meanMassFlow > targetMassFlow)
					cellList.get(0).setVelocity(cellList.get(0).velocity - dV);
				else
					cellList.get(0).setVelocity(cellList.get(0).velocity + dV);
				
				P = 0;
				for(int i = 0; i< cellList.size(); i++)
				{
					//cellList.get(i).normalizeMassFlow(normalizationFactor);
					cellList.get(i).computeHydraulicParameters();
					P += cellList.get(i).pressureLoss;
				}
			
				msfr.superiorHorizontalPipe.cellList.get(0).setPumpPressure(P);
				
				v = cellList.get(0).velocity;
			}
			else
			{
				cellList.get(0).setVelocity(vOld);
				dV /= 2.;
			}
			
			if( counter < 50 )// (counter % printFrequency) == 0)
			{	
				System.out.println("i= "+ counter + " ,v= " + cellList.get(0).velocity + " d= "+ Math.abs(meanMassFlow - targetMassFlow));
				if(loopSucess)
					System.out.println("\t sucess");
				else
					System.out.println("\t fail");
			}
			
			counter++;
			time += timeStep;
			
			//if((Math.abs(v-vOld) < dV) && (v != vOld))
				//endOfLoop = true;
			
			if(counter > 50)
				endOfLoop = true;
			
			
		}while(endOfLoop==false);
		
		System.out.println("END LOOP :: " + Math.abs(meanMassFlow - targetMassFlow));
		for (int i=0; i<cellList.size(); i++)
		{
			System.out.println(cellList.get(i).velocity);
		}
		System.out.println("counter= " + counter);*/
		
		counter=0;
		double internCounter = 0;
		
		double ecart =0;
		do{
			for (int i = 0; i< cellList.size(); i++)
			{
				cellList.get(i).setMassFlow(targetMassFlow);
			}
			for (int i = 0; i< cellList.size(); i++)
			{
				cellList.get(i).computeHydraulicParameters();
			}
			
			internCounter = 0;
			endOfLoop = false;
			do
			{
				vOld = v;
				circulatingTime = 0;
				
				for (int i = 0; i< cellList.size(); i++)
				{
					cellList.get(i).computeHydraulicParameters();
				}
				
				for (int i = 0; i< cellList.size(); i++)
				{
					cellList.get(i).compute_v();
				}
				
				/*for (int i = 0; i< cellList.size(); i++)
				{
					cellList.get(i).update_v();
					if((counter%printFrequency)==0)
					{
						//System.out.println("v("+i+")UN= " + cellList.get(i).velocity);
						//System.out.println("q("+i+")UN= " + cellList.get(i).massFlow);
					}
					
				}*/
				
				v = cellList.get(3).velocity;
				
				internCounter++;
				//System.out.println("internCounter " + internCounter);
				
				
			}while(Math.abs(v-vOld)>1E-4 || internCounter<100);
			System.out.println("internCounter "+internCounter);
			System.out.println("counter "+counter);
			circulatingTime=0;
			for (int i = 0; i< cellList.size(); i++)
			{
				if(cellList.get(i).mass/ cellList.get(i).massFlow<100)
				{
					circulatingTime += cellList.get(i).mass/ cellList.get(i).massFlow;
					
				}
				else
				{
					circulatingTime +=100;
				}
			}
			meanMassFlow = mass/circulatingTime;
			normalizationFactor = targetMassFlow/meanMassFlow;
			System.out.println("calc "+meanMassFlow);
			System.out.println("target "+targetMassFlow);
			if (normalizationFactor > 1000)
			{
				normalizationFactor = 1000;
			}
			else if (normalizationFactor < 0.001)
			{
				normalizationFactor = 0.001;
			}
			//System.out.println("P: "+P);
			P=P*normalizationFactor*0.1+P*0.9;
			
			msfr.superiorHorizontalPipe.cellList.get(0).setPumpPressure(P);
			
			for (int i=0; i<cellList.size(); i++)
			{
				System.out.println(cellList.get(i).velocity);
			}
			System.out.println("P: "+P);
			ecart = meanMassFlow-targetMassFlow;
			System.out.println("ecart: "+ecart);
			if (P==0)
					break;
			
			counter++;
			
		}while(Math.abs(meanMassFlow-targetMassFlow)>1E-6);
		
		for (int i=0; i<cellList.size(); i++)
		{
			System.out.println(cellList.get(i).velocity);
		}
		System.out.println("counter= " + counter);
		System.out.println("internCounter= " + internCounter);
	/*	for (time = 0; time<1; time +=timeStep)
		{
			System.out.println("time: "+time);
			for (int i = 0; i< cellList.size(); i++)
			{
				cellList.get(i).computeHydraulicParameters();;
				//System.out.println(cellList.get(i).pressureLoss);
			}
			for (int i = 0; i< cellList.size(); i++)
			{
				cellList.get(i).compute_v();
				System.out.println(cellList.get(i).velocity);
			}
		}*/
		/*if (time > savingTime)
		{
			sTemperature.addValue(String.valueOf(time)+"	"+String.valueOf(msfr.fuelTemperature));
			
			
			savingTime = time + 0.1;
		}
	}	
		sVelocity.writeInFile();*/

	}
	
	
}

package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.example.analysis.Analysis;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Force;
import org.example.loading.NodalDisp;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.mainTCC.ReadInput;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Node;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.Supports;
import org.example.userInterface.MenuAnalysis;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

public class ToolbarButtonsService
{
    
	private static MenuViewService view = MenuViewService.getInstance() ;

	public static Structure LoadFile(String Path, String FileName)
	{
		if (!FileName.equals("")) 
		{
			Structure structure = new Structure(null, null, null);
			String[][] Input = ReadInput.ReadTxtFile(Path + FileName + ".txt");			// Loads the input file (.txt)
			if (Input != null)
			{
				structure.setName(Input[0][2]);
				List<Point3D> StructCoords = new ArrayList<>() ;
				for (int coord = 0; coord <= Input[1].length - 4; coord += 1)
				{
					String[] Line = Input[1][coord + 2].split("	");
					StructCoords.add(new Point3D(Double.parseDouble(Line[0]), Double.parseDouble(Line[1]), Double.parseDouble(Line[2]))) ;
				}
				structure.setCoords(StructCoords);
				structure.updateCenter() ;
				structure.resetMesh();

				List<Material> matTypes = new ArrayList<>() ;
				for (int mat = 0; mat <= Input[4].length - 4; mat += 1)
				{
					String[] Line = Input[4][mat + 2].split("	");
					Material newMaterial = new Material(Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])) ;
					matTypes.add(newMaterial) ;
//					MatType[mat] = new double[] {Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])};
					
				}
				List<Section> secTypes = new ArrayList<>() ;
				for (int sec = 0; sec <= Input[5].length - 4; sec += 1)
				{
					String[] Line = Input[5][sec + 2].split("	");
					secTypes.add(new Section(Double.parseDouble(Line[1]))) ;
				}
				
				for (int node = 0; node <= Input[2].length - 4; node += 1)
				{
					String[] Line = Input[2][node + 2].split("	");
					Node NewNode;
					NewNode = new Node(-1, null);
					NewNode.setID(Integer.parseInt(Line[0]));
					NewNode.setOriginalCoords(new Point3D(Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])));
					structure.getMesh().getNodes().add(NewNode) ;
				}
				for (int elem = 0; elem <= Input[3].length - 4; elem += 1)
				{
					String[] Line = Input[3][elem + 2].split("	");
					ElemType elemType = ElemType.valueOf(Line[1].toUpperCase()) ;
					Element NewElem = new Element(null, null, null, null, elemType);
					int NumberOfElemNodes = Element.shapeToNumberNodes(NewElem.getShape(), elemType);
					int[] ElemNodes = null;
					for (int elemnode = 0; elemnode <= NumberOfElemNodes - 1; elemnode += 1)
					{
						ElemNodes = Util.AddElem(ElemNodes, Integer.parseInt(Line[elemnode + 2]));
					}
					NewElem.setExternalNodes(structure.getMesh().getNodesByID(ElemNodes)) ;
					NewElem.setMat(matTypes.get(Integer.parseInt(Line[NumberOfElemNodes + 2])));
					NewElem.setSec(secTypes.get(Integer.parseInt(Line[NumberOfElemNodes + 3])));
					structure.getMesh().getElements().add(NewElem) ;
				}
				
				for (int sup = 0; sup <= Input[6].length - 4; sup += 1)
				{
					String[] Line = Input[6][sup + 2].split("	");
					Supports NewSup;
					NewSup = new Supports(-1, -1, null);
					NewSup.setID(Integer.parseInt(Line[0]));
					NewSup.setNode(Integer.parseInt(Line[1]));
					NewSup.setDoFs(new int[] {Integer.parseInt(Line[2]), Integer.parseInt(Line[3]), Integer.parseInt(Line[4]), Integer.parseInt(Line[5]), Integer.parseInt(Line[6]), Integer.parseInt(Line[7])});
					structure.addSupport(NewSup);
					structure.getMesh().getNodes().get(Integer.parseInt(Line[1])).setSup(NewSup.getDoFs());
				}

                List<Force> concLoadTypes = new ArrayList<>() ;
				for (int concload = 0; concload <= Input[7].length - 4; concload += 1)
				{
					String[] Line = Input[7][concload + 2].split("	");
					ConcLoad NewConcLoad;
					NewConcLoad = new ConcLoad(new Force());
					NewConcLoad.setId(Integer.parseInt(Line[0]));
					// NewConcLoad.setNodeID(Integer.parseInt(Line[1]));
					NewConcLoad.setForce(new Force(new double[] {Double.parseDouble(Line[2]), Double.parseDouble(Line[3]), Double.parseDouble(Line[4]), Double.parseDouble(Line[5]), Double.parseDouble(Line[6]), Double.parseDouble(Line[7])}));
					concLoadTypes.add(NewConcLoad.getForce()) ;
					//  = Util.AddElem(concLoadTypes, new double[] {NewConcLoad.getNodeID(), NewConcLoad.getForce()[0], NewConcLoad.getForce()[1], NewConcLoad.getForce()[2], NewConcLoad.getForce()[3], NewConcLoad.getForce()[4], NewConcLoad.getForce()[5]});
					MainPanel.getInstance().getCentralPanel().getLoading().addConcLoad(NewConcLoad);
				}

                double[][] DistLoadType = new double[0][] ;
				for (int distload = 0; distload <= Input[8].length - 4; distload += 1)
				{
					String[] Line = Input[8][distload + 2].split("	");
					DistLoad NewDistLoad;

					NewDistLoad = new DistLoad(-1, -1);
					NewDistLoad.setId(Integer.parseInt(Line[0]));
					// NewDistLoad.setElem(Integer.parseInt(Line[1]));
					NewDistLoad.setType(Integer.parseInt(Line[2]));
					NewDistLoad.setIntensity(Double.parseDouble(Line[3]));
					DistLoadType = Util.AddElem(DistLoadType, new double[] {-1, NewDistLoad.getType(), NewDistLoad.getIntensity()});
					
                    MainPanel.getInstance().getCentralPanel().getLoading().addDistLoad(NewDistLoad);
				}

                double[][] NodalDispType = new double[0][] ;
				for (int nodaldisp = 0; nodaldisp <= Input[9].length - 4; nodaldisp += 1)
				{
					String[] Line = Input[9][nodaldisp + 2].split("	");
					NodalDisp NewNodalDisp;
					
                    NewNodalDisp = new NodalDisp(-1, -1, null);
					NewNodalDisp.setID(Integer.parseInt(Line[0]));
					NewNodalDisp.setNode(Integer.parseInt(Line[1]));
					NewNodalDisp.setDisps(new double[] {Double.parseDouble(Line[2]), Double.parseDouble(Line[3]), Double.parseDouble(Line[4]), Double.parseDouble(Line[5]), Double.parseDouble(Line[6]), Double.parseDouble(Line[7])});
					NodalDispType = Util.AddElem(NodalDispType, new double[] {NewNodalDisp.getNode(), NewNodalDisp.getDisps()[0], NewNodalDisp.getDisps()[1], NewNodalDisp.getDisps()[2], NewNodalDisp.getDisps()[3], NewNodalDisp.getDisps()[4], NewNodalDisp.getDisps()[5]});
					
                    MainPanel.getInstance().getCentralPanel().getLoading().addNodalDisp(NewNodalDisp);
				}
                
                MenuFunctions.setConcLoadTypes(concLoadTypes) ;
                MenuFunctions.setDistLoadType(DistLoadType) ;
                MenuFunctions.setNodalDispType(NodalDispType) ;

				System.out.println("Structure loaded successfully");
				structure.printStructure(matTypes, secTypes, structure.getSupports(), MainPanel.getInstance().getCentralPanel().getLoading());
				return structure ;
			}
			else
			{
				System.out.println("Arquivo de input nâo encontrado");
				return null ;
			}
		}
		else
		{
			System.out.println("Arquivo de input nâo encontrado");
			return null ;
		}
	}

	public static void RunExample(int exampleID)
	{
		
		Structure structure = new Structure(null, null, null);
		MainPanel.getInstance().getCentralPanel().getLoading().clearLoads() ;
		// TODO reset display Central panel
		// MainPanel.getInstance().getCentralPanel().resetDisplay() ;
		view.reset() ;
		reset() ;
		if (exampleID == 0)
		{
 			structure = LoadFile(".\\Exemplos\\", "0-KR1");
		}
 		if (exampleID == 1)
		{
			structure = LoadFile(".\\Exemplos\\", "1-KR2");
		}
 		if (exampleID == 2)
		{
			structure = LoadFile(".\\Exemplos\\", "2-MR1");
		}
 		if (exampleID == 3)
		{
			structure = LoadFile(".\\Exemplos\\", "3-MR2");
		}
 		if (exampleID == 4)
 		{
			structure = LoadFile(".\\Exemplos\\", "4-R4");
 		}
 		if (exampleID == 5)
 		{
			structure = LoadFile(".\\Exemplos\\", "5-Q4");
 		}
 		if (exampleID == 6)
 		{
			structure = LoadFile(".\\Exemplos\\", "6-T3G");
 		}
 		if (exampleID == 7)
 		{
			structure = LoadFile(".\\Exemplos\\", "7-T6G");
 		}
 		if (exampleID == 8)
 		{
			structure = LoadFile(".\\Exemplos\\", "8-SM");
 		}
 		if (exampleID == 9)
 		{
			structure = LoadFile(".\\Exemplos\\", "9-SM8");
 		}
 		if (exampleID == 10)
 		{
			structure = LoadFile(".\\Exemplos\\", "10-KP3");
 		}
 		if (exampleID == 11)
 		{
			structure = LoadFile(".\\Exemplos\\", "11-SM_C");
 		}
 		if (exampleID == 12)
 		{
			structure = LoadFile(".\\Exemplos\\", "12-SM_H");
 		}
 		if (exampleID == 13)
 		{
			structure = LoadFile(".\\Exemplos\\", "13-vigadeaco");
 		}
        boolean NonlinearMat = MenuFunctions.isNonlinearMat() ;
        boolean NonlinearGeo = MenuFunctions.isNonlinearGeo() ;
        List<Force> concLoadTypes = MenuFunctions.getConcLoadTypes() ;
        double[][] DistLoadType = MenuFunctions.getDistLoadType() ;


		MainPanel.getInstance().getCentralPanel().setStructure(structure) ;
 		MenuAnalysis.CalcAnalysisParameters(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), concLoadTypes, DistLoadType);
		
        long AnalysisTime = System.currentTimeMillis();
		Analysis.run(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), NonlinearMat, NonlinearGeo, 1, 1, 1);
		MenuAnalysis.PostAnalysis(MainPanel.getInstance().getCentralPanel().getStructure(), NonlinearMat, NonlinearGeo);
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		for (Element elem : MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements())
		{
			elem.RecordResults(MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getNodes(), MainPanel.getInstance().getCentralPanel().getStructure().getU(), NonlinearMat, NonlinearGeo);
        	elem.setDeformedCoords(MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getNodes());
		}
		MainPanel.getInstance().getCentralPanel().getStructure().getResults().register(MainPanel.getInstance().getCentralPanel().getStructure().getMesh(), MainPanel.getInstance().getCentralPanel().getStructure().getSupports(), MainPanel.getInstance().getCentralPanel().getStructure().getU(), NonlinearMat, NonlinearGeo);

        MenuFunctions.setAnalysisIsComplete(true) ;

		view.reactionArrows = true;
		view.reactionValues = true;
		view.deformedStructure = true;

		double MaxDisp = Util.FindMaxAbs(MainPanel.getInstance().getCentralPanel().getStructure().getU());
		MainPanel.getInstance().getCentralPanel().updateDiagramScaleY(MaxDisp) ;
	}

	public static void reset()
	{
        MenuFunctions.setConcLoadTypes(null) ;
        MenuFunctions.setDistLoadType(null) ;
        MenuFunctions.setNodalDispType(null) ;
        MenuFunctions.setAnalysisIsComplete(false) ;
	}

}

package main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import main.utilidades.Util;
import main.view.MainPanel;
import main.loading.Loading;
import main.mainTCC.MainTCC;
import main.mainTCC.MenuFunctions;
import main.output.SaveOutput;
import main.structure.Element;
import main.structure.Material;
import main.structure.MyCanvas;
import main.structure.Node;
import main.structure.Section;
import main.structure.Structure;
import main.structure.Supports;

public abstract class MenuFile
{
	private static JMenu FileMenu = new JMenu("Arquivo");
	private static JMenuItem Save, Load;

	public static JMenu create()
	{
		FileMenu.setMnemonic(KeyEvent.VK_A);
		AddFileMenuItems() ;
		return FileMenu ;
	}
	
	public static void SaveFile(String FileName, MyCanvas MainCanvas, Structure Struct, List<Node> nodes, List<Element> elems,
			List<Supports> Sup, Loading loading, List<Material> UserDefinedMat, List<Section> UserDefinedSec)
	{
		Struct.setName(FileName);
		String[] InputSections = new String[] {
			    "Nome da estrutura",
			    "Coordenadas da estrutura",
			    "Nos",
			    "Elementos",
			    "Materiais",
			    "Secoes",
			    "Apoios",
			    "Cargas concentradas",
			    "Cargas distribuidas",
			    "Deslocamentos aplicados",
			    "Desenho"
			};
		String[][] InputVariables = new String[11][];
		InputVariables[0] = new String[] {"Nome"};
		InputVariables[1] = new String[] {"x (m)	y (m)	z (m)"};
		InputVariables[2] = new String[] {"Id	x (m)	y (m)	z (m)	Sup	Carga conc	Desl"};
		InputVariables[3] = new String[] {"Id	Tipo	No1	No2	No3	Mat	Sec"};
		InputVariables[4] = new String[] {"Id	E (GPa)	v	fu (MPa)"};
		InputVariables[5] = new String[] {"Id	Espessura (mm)"};
		InputVariables[6] = new String[] {"Id	No	Rx	Ry	Rz	Tetax	Tetay	Tetaz"};
		InputVariables[7] = new String[] {"Id	No	Fx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)"};
		InputVariables[8] = new String[] {"Id	Elem	Tipo	Valor inicial (kN)	Valor final (kN)	Dist inicial (mm)	Dist final (mm)"};
		InputVariables[9] = new String[] {"Id	No	Fx	Fy	Fz	Mx	My	Mz"};
		InputVariables[10] = new String[] {"Pos des x (m)	Pos des y (m)	Centro x (m)	Centro y (m)	Dim x (m)	Dim y (m)	Tetax	Tetay	Tetaz"};
		Object[][][] values = new Object[11][][];

		values[0] = new Object[1][1];
		values[0][0][0] = FileName;
		if (Struct.getCoords() != null)
		{
			values[1] = new Object[Struct.getCoords().size()][3];
			for (int i = 0; i <= Struct.getCoords().size() - 1; i += 1)
			{
				values[1][i][0] = Struct.getCoords().get(i).x;
				values[1][i][1] = Struct.getCoords().get(i).y;
				values[1][i][2] = Struct.getCoords().get(i).z;
			}
		}
		if (nodes != null)
		{
			values[2] = new Object[nodes.size()][7];
			for (int i = 0; i <= nodes.size() - 1; i += 1)
			{
				Node node = nodes.get(i);
				values[2][i][0] = node.getID();
				values[2][i][1] = Util.Round(node.getOriginalCoords().x, 8);
				values[2][i][2] = Util.Round(node.getOriginalCoords().y, 8);
				values[2][i][3] = Util.Round(node.getOriginalCoords().z, 8);
				values[2][i][4] = node.getSup();
				values[2][i][5] = node.getConcLoads();
				values[2][i][6] = node.getNodalDisps();
			}
		}
		if (elems != null)
		{
			values[3] = new Object[elems.size()][elems.get(0).getExternalNodes().length + 4];
			for (int i = 0; i <= elems.size() - 1; i += 1)
			{
				Element elem = elems.get(i) ;
				int NElemNodes = elem.getExternalNodes().length;
				values[3][i][0] = elem.getID();
				values[3][i][1] = elem.getType();
				for (int elemnode = 0; elemnode <= NElemNodes - 1; elemnode += 1)
				{
					values[3][i][elemnode + 2] = elem.getExternalNodes()[elemnode];
				}
				values[3][i][NElemNodes + 2] = elem.getMat();
				values[3][i][NElemNodes + 3] = elem.getSec();
			}
		}
		if (UserDefinedMat != null)
		{
			values[4] = new Object[UserDefinedMat.size()][4];
			for (int mat = 0; mat <= UserDefinedMat.size() - 1; mat += 1)
			{
				values[4][mat][0] = mat;
				values[4][mat][1] = UserDefinedMat.get(mat).getE();
				values[4][mat][2] = UserDefinedMat.get(mat).getV();
				values[4][mat][3] = UserDefinedMat.get(mat).getG();
			}
		}
		if (UserDefinedSec != null)
		{
			values[5] = new Object[UserDefinedSec.size()][2];
			for (int sec = 0; sec <= UserDefinedSec.size() - 1; sec += 1)
			{
				values[5][sec][0] = sec;
				values[5][sec][1] = UserDefinedSec.get(sec).getT();
			}
		}
		if (Sup != null)
		{
			values[6] = new Object[Sup.size()][8];
			for (int sup = 0; sup <= Sup.size() - 1; sup += 1)
			{
				values[6][sup][0] = Sup.get(sup).getID();
				values[6][sup][1] = Sup.get(sup).getNode();
				values[6][sup][2] = Sup.get(sup).getDoFs()[0];
				values[6][sup][3] = Sup.get(sup).getDoFs()[1];
				values[6][sup][4] = Sup.get(sup).getDoFs()[2];
				values[6][sup][5] = Sup.get(sup).getDoFs()[3];
				values[6][sup][6] = Sup.get(sup).getDoFs()[4];
				values[6][sup][7] = Sup.get(sup).getDoFs()[5];
			}
		}
		if (loading.getConcLoads() != null)
		{
			values[7] = new Object[loading.getConcLoads().size()][8];
			for (int load = 0; load <= loading.getConcLoads().size() - 1; load += 1)
			{
				values[7][load][0] = loading.getConcLoads().get(load).getID();
				values[7][load][1] = loading.getConcLoads().get(load).getNode();
				values[7][load][2] = loading.getConcLoads().get(load).getLoads()[0];
				values[7][load][3] = loading.getConcLoads().get(load).getLoads()[1];
				values[7][load][4] = loading.getConcLoads().get(load).getLoads()[2];
				values[7][load][5] = loading.getConcLoads().get(load).getLoads()[3];
				values[7][load][6] = loading.getConcLoads().get(load).getLoads()[4];
				values[7][load][7] = loading.getConcLoads().get(load).getLoads()[5];
			}
		}
		if (loading.getDistLoads() != null)
		{
			values[8] = new Object[loading.getDistLoads().size()][4];
			for (int load = 0; load <= loading.getDistLoads().size() - 1; load += 1)
			{
				values[8][load][0] = loading.getDistLoads().get(load).getID();
				values[8][load][1] = loading.getDistLoads().get(load).getElem();
				values[8][load][2] = loading.getDistLoads().get(load).getType();
				values[8][load][3] = loading.getDistLoads().get(load).getIntensity();
			}
		}
		if (loading.getNodalDisps() != null)
		{
			values[9] = new Object[loading.getNodalDisps().size()][8];
			for (int dist = 0; dist <= loading.getNodalDisps().size() - 1; dist += 1)
			{
				values[9][dist][0] = loading.getNodalDisps().get(dist).getID();
				values[9][dist][1] = loading.getNodalDisps().get(dist).getNode();
				values[9][dist][2] = loading.getNodalDisps().get(dist).getDisps()[0];
				values[9][dist][3] = loading.getNodalDisps().get(dist).getDisps()[1];
				values[9][dist][4] = loading.getNodalDisps().get(dist).getDisps()[2];
				values[9][dist][5] = loading.getNodalDisps().get(dist).getDisps()[3];
				values[9][dist][6] = loading.getNodalDisps().get(dist).getDisps()[4];
				values[9][dist][7] = loading.getNodalDisps().get(dist).getDisps()[5];
			}
		}
		values[10] = new Object[1][9];
		values[10][0][0] = MainCanvas.getDrawingPos()[0];
		values[10][0][1] = MainCanvas.getDrawingPos()[1];
		values[10][0][2] = MainCanvas.getCenter()[0];
		values[10][0][3] = MainCanvas.getCenter()[1];
		values[10][0][4] = Util.Round(MainCanvas.getDimension()[0], 8);
		values[10][0][5] = Util.Round(MainCanvas.getDimension()[1], 8);
		values[10][0][6] = Util.Round(MainCanvas.getAngles()[0], 8);
		values[10][0][7] = Util.Round(MainCanvas.getAngles()[1], 8);
		values[10][0][8] = Util.Round(MainCanvas.getAngles()[2], 8);
		SaveOutput.SaveStructureToTxt(FileName, InputSections, InputVariables, values);		// Save the structure in an input file (.txt)
	}

	public static void AddFileMenuItems()
	{
		/* Defining items in the menu File */
		Save = new JMenuItem("Save file", KeyEvent.VK_S);
		Load = new JMenuItem("Load file", KeyEvent.VK_L);
		Save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String filename = Menus.getInstance().getSaveLoadFile().run().getText();
				SaveFile(filename, Menus.getMainCanvas(), MainPanel.structure, MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getMesh().getElements(),
						MainPanel.structure.getSupports(), MainPanel.loading, MenuFunctions.matTypes,
						MenuFunctions.secTypes);
			}
		});
		Load.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				loadStructure() ;
			}
		});
		Save.setForeground(Menus.palette[3]);
		Load.setForeground(Menus.palette[3]);
		FileMenu.add(Save);
		FileMenu.add(Load);
	}
	

	public static void loadStructure()
	{
		
		MainPanel.structure = new Structure(null, null, null);
		MainPanel.loading.clearLoads() ;		
		MainPanel.resetDisplay() ;
		MenuFunctions.resetDisplay();
		
		String filename = Menus.getInstance().getSaveLoadFile().run().getText();
		MainPanel.structure = MenuFunctions.LoadFile("", filename);
		MainPanel.structure.updateMaxCoords() ;
		Menus.getMainCanvas().setDimension(new double[] {1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y, 1});
		Menus.getInstance().setRunAnalysis(MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading));
		Menus.getInstance().showCanvasOn() ;
		Menus.getInstance().showGrid() ;
		Menus.getInstance().showMousePos() ;
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		MenuFunctions.ElemContourView();
		MenuFunctions.SupView();
		MenuFunctions.ConcLoadsView();
		MenuFunctions.DistLoadsView();
		MenuFunctions.NodalDispsView();
		Menus.getInstance().getInstructionsPanel().CheckSteps(MainPanel.structure) ;
		Menus.getInstance().DisableButtons();
		Menus.getInstance().EnableButtons();
		Menus.getInstance().getInstructionsPanel().updateStepsCompletion() ;
	}
	
}

package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.example.analysis.Analysis;
import org.example.loading.Force;
import org.example.loading.Loading;
import org.example.mainTCC.InputDTO;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.ElemType;
import org.example.structure.Material;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.userInterface.MenuAnalysis;
import org.example.userInterface.MenuEspecial;
import org.example.utilidades.Util;
import org.junit.jupiter.api.Test;

public class EspecialTest
{
    File projectRoot = new File(System.getProperty("user.dir")).getParentFile() ;
    List<Material> matTypes = new ArrayList<>() ;
    List<Section> secTypes = new ArrayList<>() ;

    @Test
    void especialTest()
    {

        String filePath = projectRoot + File.separator + "examples" + File.separator + "Especial.txt";        
        InputDTO inputDTO = Util.LoadEspecialInput(filePath);

		for (int i = 0 ; i <= inputDTO.getInputMatTypes().length - 1 ; i += 1)
		{
			matTypes.add(new Material(inputDTO.getInputMatTypes()[i][0], inputDTO.getInputMatTypes()[i][1], inputDTO.getInputMatTypes()[i][2])) ;
		}
		
		for (int i = 0 ; i <= inputDTO.getInputSecTypes().length - 1 ; i += 1)
		{
			secTypes.add(new Section(inputDTO.getInputSecTypes()[i][0])) ;
		}

		ElemType elemType = ElemType.valueOf(inputDTO.getEspecialElemTypes()[0].toUpperCase());
        int[] MeshSize = inputDTO.getEspecialMeshSizes()[0];
        int supConfig = inputDTO.getSupConfig()[0];
        int SelConcLoad = 0;
        int SelDistLoad = 0;

        Material material = matTypes.get(0) ;
        Section section = secTypes.get(0) ;

        int ConcLoadConfig = 1;
        Structure structure = Structure.create(inputDTO.getEspecialCoords(), inputDTO.getMeshType(), MeshSize, elemType, material, matTypes, section, secTypes, supConfig) ;

        assertEquals(4, structure.getCoords().size()) ;
        assertNotNull(structure.getCenter()) ;
        assertNotNull(structure.getMinCoords()) ;
        assertNotNull(structure.getMaxCoords()) ;
        assertNotNull(structure.getShape()) ;
        assertNotNull(structure.getMesh()) ;
        assertEquals(40, structure.getSupports().size()) ;
        assertEquals(121, structure.getMesh().getNodes().size()) ;
        assertEquals(100, structure.getMesh().getElements().size()) ;

        List<Force> forces = new ArrayList<>() ;

        for (double[] force : inputDTO.getConcLoadType())
        {
            forces.add(new Force(force)) ;
        }

        Loading loading = MenuEspecial.createLoading(structure, ConcLoadConfig, MeshSize, SelConcLoad, SelDistLoad,
                                        structure.getMesh().getNodes(), forces, structure.getMesh().getElements(), inputDTO.getDistLoadType()) ;

        assertNotNull(loading) ;
        assertTrue(!loading.getDistLoads().isEmpty());
        assertEquals(4, loading.getDistLoads().get(0).getType());
        assertEquals(1000, loading.getDistLoads().get(0).getIntensity(), 0.0000001);

        MenuAnalysis.CalcAnalysisParameters(structure, loading, MenuFunctions.concLoadTypes, MenuFunctions.DistLoadType);

        assertNotNull(structure.getMesh().getNodes().get(0).getDOFType()) ;
        assertNotNull(structure.getMesh().getNodes().get(0).getDOFs()) ;
        assertNotNull(structure.getMesh().getElements().get(0).getCumDOFs()) ;
        assertNotNull(structure.getMesh().getElements().get(0).getNodeDOF()) ;

        boolean NonlinearMat = true;
        boolean NonlinearGeo = false;
        Analysis.run(structure, loading, NonlinearMat, NonlinearGeo, 10, 5, 15.743);

        assertNotNull(structure.getU()) ;
        assertEquals(0.006707725529021804, Util.FindMaxAbs(structure.getU()), 0.000001);

    }

}

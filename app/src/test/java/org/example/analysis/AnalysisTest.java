package org.example.analysis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Force;
import org.example.loading.Loading;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.MeshType;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.StructureShape;
import org.example.structure.Supports;
import org.example.userInterface.MenuAnalysis;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnalysisTest
{
    
    private Structure structure ;

    private static final double doubleTol = Math.pow(10, -6) ;


    @Test
    void analysisRunSuccessfullyConcLoad()
    {
        Structure structure = createStructure() ;
        double loadIntensity = 20 / 9.0 * 100 ; // Pode ser qualquer valor. Este é a carga equivalente à distribuída de 100, para esta malha

        double[] expectedP = new double[] {
            loadIntensity,
            0.000000,
            0.000000,
            loadIntensity,
            0.000000,
            0.000000,
            loadIntensity,
            0.000000,
            -0.000000,
            loadIntensity,
            0.000000,
            -0.000000
        } ;
        
        double[] expectedU = new double[] {
            0.000076,
            0.000055,
            0.000041,
            0.000076,
            -0.000055,
            0.000041,
            0.000076,
            0.000055,
            -0.000041,
            0.000076,
            -0.000055,
            -0.000041
        } ;
        
        addUniConcLoad(structure, loadIntensity) ;
        double[] result = Analysis.run(structure, new Loading(), false, false, 1, 1, 1) ;
    
        assertNotNull(result) ;
        assertArrayEquals(expectedP, structure.getP(), doubleTol) ;
        assertArrayEquals(expectedU, result, doubleTol) ;
        
    }

    @Test
    void analysisRunSuccessfullyDistLoad()
    {
        Structure structure = createStructure() ;
        addUniDistLoad(structure, 100) ;

        double[] expectedP = new double[] {
            222.222222,
            0.000000,
            0.000000,
            222.222222,
            0.000000,
            0.000000,
            222.222222,
            0.000000,
            -0.000000,
            222.222222,
            0.000000,
            -0.000000
        } ;
        
        double[] expectedU = new double[] {
            0.000076,
            0.000055,
            0.000041,
            0.000076,
            -0.000055,
            0.000041,
            0.000076,
            0.000055,
            -0.000041,
            0.000076,
            -0.000055,
            -0.000041
        } ;

        double[] result = Analysis.run(structure, new Loading(), false, false, 1, 1, 1) ;
    
        assertNotNull(result) ;
        assertArrayEquals(expectedP, structure.getP(), doubleTol) ;
        assertArrayEquals(expectedU, result, doubleTol) ;
        
    }

    
    private Structure createStructure()
    {
        List<Point3D> coords = new ArrayList<>() ;
        coords.add(new Point3D(0, 0, 0)) ;
        coords.add(new Point3D(4, 0, 0)) ;
        coords.add(new Point3D(4, 5, 0)) ;
        coords.add(new Point3D(0, 5, 0)) ;

        ElemType elemType = ElemType.KR1 ;
        MeshType meshType = MeshType.cartesian ;

        Material mat = new Material(200, 0.2, 167) ;
        Section sec = new Section(300) ;
        int supConfig = 1 ;
        
        structure = new Structure("Test structure 1", StructureShape.rectangular, coords) ;
        structure.createMesh(meshType, 3, 3, elemType) ;
        structure.getMesh().getElements().forEach(elem -> elem.setMat(mat)) ;
        structure.getMesh().getElements().forEach(elem -> elem.setSec(sec)) ;

        Supports[] supports = Util.AddEspecialSupports(structure.getMesh().getNodes(), Element.typeToShape(elemType), meshType, new int[] {3, 3}, supConfig);
		for (Supports sup : supports)
		{
			structure.addSupport(sup);
		}
        return structure ;
    }

    private void addUniConcLoad(Structure structure, double loadIntensity)
    {
        Loading loading = new Loading() ;
        ConcLoad concLoad = new ConcLoad(new Force(0, 0, loadIntensity, 0, 0, 0)) ;
        structure.getMesh().getNodes().forEach(node -> node.addConcLoad(concLoad)) ;

        MenuAnalysis.CalcAnalysisParameters(structure, loading, MenuFunctions.getConcLoadTypes(), MenuFunctions.getDistLoadType()) ;
    }

    private void addUniDistLoad(Structure structure, double loadIntensity)
    {
        Loading loading = new Loading() ;
        structure.getMesh().getElements().forEach(elem -> elem.addDistLoad(new DistLoad(4, loadIntensity))) ;

        MenuAnalysis.CalcAnalysisParameters(structure, loading, MenuFunctions.getConcLoadTypes(), MenuFunctions.getDistLoadType()) ;
    }

}

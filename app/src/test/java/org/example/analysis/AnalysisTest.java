package org.example.analysis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import org.example.loading.DistLoad;
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
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnalysisTest
{
    
    private Structure structure ;

    @BeforeAll
    void createStructure()
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

        Loading loading = new Loading() ;
        structure.getMesh().getElements().forEach(elem -> elem.setDistLoads(new DistLoad[] {new DistLoad(1, elem.getID(), 4, 100)})) ;

        MenuFunctions.CalcAnalysisParameters(structure, loading) ;
    }

    @Test
    void loadingVectorAssemblesSuccessfully()
    {
        
        double[] expectedResult = new double[] {
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
        assertArrayEquals(expectedResult, result, Math.pow(10, -6)) ;
        
    }

}

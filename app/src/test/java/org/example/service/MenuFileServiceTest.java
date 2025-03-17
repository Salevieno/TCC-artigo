package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.example.structure.ElemType;
import org.example.structure.Material;
import org.example.structure.MeshType;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.StructureDTO;
import org.example.utilidades.Point3D;
import org.junit.jupiter.api.Test;

public class MenuFileServiceTest
{

    @Test
    void testSaveStructure()
    {

        List<Point3D> coords = new ArrayList<>() ;
        coords.add(new Point3D(0, 0, 0)) ;
        coords.add(new Point3D(10, 0, 0)) ;
        coords.add(new Point3D(10, 5, 0)) ;
        coords.add(new Point3D(0, 5, 0)) ;

        MeshType meshType = MeshType.cartesian ;
        int[] meshSizes = new int[] {2, 5} ;
        ElemType elemType = ElemType.KP3 ;

        Material currentMaterialType = new Material(200.0, 0.5, 133.67) ;
        List<Material> matTypes = new ArrayList<>() ;
        matTypes.add(currentMaterialType) ;

        Section currentSecType = new Section(200.0) ;
        List<Section> secTypes = new ArrayList<>() ;
        secTypes.add(currentSecType) ;

        int supConfig = 1 ;
        Structure structure = Structure.create(coords, meshType, meshSizes, elemType, currentMaterialType, matTypes, currentSecType, secTypes, supConfig) ;

        MenuFileService.saveStructure("test save structure", new StructureDTO(structure)) ;
    }
}

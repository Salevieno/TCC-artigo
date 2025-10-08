package org.example.main;

import java.util.Arrays;
import java.util.List;

import org.example.structure.MeshType;
import org.example.utilidades.Point3D;

public class InputDTO
{

    List<Point3D> EspecialCoords ;
    MeshType meshType ;
    String[] EspecialElemTypes ;
    int[][] EspecialMeshSizes ;
    double[][] inputMatTypes ;    
    double[][] inputSecTypes ;
    double[][] ConcLoadType ;
    double[][] DistLoadType ;
    int[] SupConfig ;
    
    public InputDTO(List<Point3D> especialCoords, MeshType meshType, String[] especialElemTypes,
            int[][] especialMeshSizes, double[][] inputMatTypes, double[][] inputSecTypes, double[][] concLoadType,
            double[][] distLoadType, int[] supConfig)
    {
        EspecialCoords = especialCoords;
        this.meshType = meshType;
        EspecialElemTypes = especialElemTypes;
        EspecialMeshSizes = especialMeshSizes;
        this.inputMatTypes = inputMatTypes;
        this.inputSecTypes = inputSecTypes;
        ConcLoadType = concLoadType;
        DistLoadType = distLoadType;
        SupConfig = supConfig;
    }

    public List<Point3D> getEspecialCoords() { return EspecialCoords ;}
    public MeshType getMeshType() { return meshType ;}
    public String[] getEspecialElemTypes() { return EspecialElemTypes ;}
    public int[][] getEspecialMeshSizes() { return EspecialMeshSizes ;}
    public double[][] getInputMatTypes() { return inputMatTypes ;}
    public double[][] getInputSecTypes() { return inputSecTypes ;}
    public double[][] getConcLoadType() { return ConcLoadType ;}
    public double[][] getDistLoadType() { return DistLoadType ;}
    public int[] getSupConfig() { return SupConfig ;}

    @Override
    public String toString() {
        return "InputDTO [EspecialCoords=" + EspecialCoords + ", meshType=" + meshType + ", EspecialElemTypes="
                + Arrays.toString(EspecialElemTypes) + ", EspecialMeshSizes=" + Arrays.toString(EspecialMeshSizes)
                + ", inputMatTypes=" + Arrays.toString(inputMatTypes) + ", inputSecTypes="
                + Arrays.toString(inputSecTypes) + ", ConcLoadType=" + Arrays.toString(ConcLoadType) + ", DistLoadType="
                + Arrays.toString(DistLoadType) + ", SupConfig=" + Arrays.toString(SupConfig) + "]";
    }    

}

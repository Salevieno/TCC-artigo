package main.structure;

import java.util.ArrayList;
import java.util.List;

public enum MeshType
{
	cartesian,
	radial;
	
	public static String[] valuesAsString()
	{
		List<String> text = new ArrayList<>() ;
		
		for (MeshType type : MeshType.values())
		{
			text.add(type.toString()) ;
		}
		
		return text.toArray(new String[0]) ;
	}
}

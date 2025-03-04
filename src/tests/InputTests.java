package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import structure.Material;

public class InputTests
{
	@Test
	void loadStructure()
	{
	}
	
	@Test
	void loadMaterial()
	{
		Material material = Material.loadFromJson("material") ;
		assertEquals(200.0, material.getE(), Math.pow(10, -8)) ;
		assertEquals(0.2, material.getV(), Math.pow(10, -8)) ;
		assertEquals(300.0, material.getG(), Math.pow(10, -8)) ;
	}
}

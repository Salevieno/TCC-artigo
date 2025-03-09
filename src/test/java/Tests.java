// package test.java;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;

// import main.mainTCC.MenuFunctions;

// public class Tests
// {
	
// 	@Test
// 	void KR1()
// 	{
// 		MenuFunctions.ResetStructure();
// 		MenuFunctions.LoadFile("", "0-KR1") ;
// 		MenuFunctions.CalcAnalysisParameters();
// 		double[] results = MenuFunctions.RunAnalysis(MenuFunctions.Struct, MenuFunctions.Node, MenuFunctions.Elem, MenuFunctions.Struct.getSupports(), MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, 1, 1, 1) ;
// 	    double expectedResult = 1.2223975621061103 * Math.pow(10, -5) ;
// 	    assertEquals(expectedResult, results[0], Math.pow(10, -8)) ;
// 	}
	
// 	@Test
// 	void MR1()
// 	{
// 		MenuFunctions.ResetStructure();
// 		MenuFunctions.LoadFile("", "2-MR1") ;
// 		MenuFunctions.CalcAnalysisParameters();
// 		double[] results = MenuFunctions.RunAnalysis(MenuFunctions.Struct, MenuFunctions.Node, MenuFunctions.Elem, MenuFunctions.Struct.getSupports(), MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, 1, 1, 1) ;
// 	    double expectedResult = 3.7544219116614133 * Math.pow(10, -6) ;
// 	    assertEquals(expectedResult, results[0], Math.pow(10, -8)) ;
// 	}
// }

package org.example.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class SaveOutput 
{
	public static void SaveOutput(String filename, String[] sections, String[][][] vars)
	{
		try
		{	
			FileWriter fileWriter = new FileWriter (filename + " resultados.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 		
			for (int s = 0; s <= sections.length - 1; s += 1)
			{
				bufferedWriter.write(sections[s]);
				bufferedWriter.newLine();
				for (int v1 = 0; v1 <= vars[s].length - 1; v1 += 1)
				{
					for (int v2 = 0; v2 <= vars[s][v1].length - 1; v2 += 1)
					{
						bufferedWriter.write(vars[s][v1][v2] + "	");
					}
					bufferedWriter.newLine();
				}
				bufferedWriter.newLine();
				bufferedWriter.write("_____________________________________________________");
				bufferedWriter.newLine();
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			System.out.println("Resultados salvos com sucesso!");
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filename + "'");
        }
	}

}
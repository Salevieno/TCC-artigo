package Output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Results 
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

	public static void SaveStructure(String filename, String[] sections, String[][] vars, Object[][][] values)
	{
		try
		{	
			FileWriter fileWriter = new FileWriter (filename + ".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int s = 0; s <= sections.length - 1; s += 1)
			{
				bufferedWriter.write("** "+ sections[s] + " **");
				bufferedWriter.newLine();
				for (int v = 0; v <= vars[s].length - 1; v += 1)
				{
					if (v < vars[s].length - 1)
					{
						bufferedWriter.write(vars[s][v] + "	");
					}
					else
					{
						bufferedWriter.write(vars[s][v] + "");
					}
				}
				bufferedWriter.newLine();

				if (values[s] != null)
				{
					for (int v1 = 0; v1 <= values[s].length - 1; v1 += 1)
					{
						if (values[s][v1] != null)
						{
							for (int v2 = 0; v2 <= values[s][v1].length - 1; v2 += 1)
							{
								if (v2 < values[s][v1].length - 1)
								{
									bufferedWriter.write(values[s][v1][v2] + "	");
								}
								else
								{
									bufferedWriter.write(String.valueOf(values[s][v1][v2]));
								}
							}
							bufferedWriter.newLine();
						}
					}
				}
				bufferedWriter.write("_____________________________________________________");
				bufferedWriter.newLine();
				bufferedWriter.newLine();
			}
			bufferedWriter.write("$");
			bufferedWriter.close();
			System.out.println("Estrutura salva com sucesso!");
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filename + "'");
        }
	}	
}
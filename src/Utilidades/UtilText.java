package Utilidades;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class UtilText
{

	public static String[][] ReadTextFile(String fileName)
	{
		String[][] Text = null; // [Cat][Pos]
		int cat = -1;
		try
		{	
			FileReader fileReader = new FileReader (fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader); 					
			String Line = bufferedReader.readLine();
			while (!Line.equals("_"))
			{
				if (Line.contains("*"))
				{
					cat += 1;
					Text = Util.IncreaseArraySize(Text, 1);
	            }
				Text[cat] = Util.AddElem(Text[cat], Line);
				Line = bufferedReader.readLine();
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex) 
		{
            System.out.println("Unable to find file '" + fileName + "' (text file)");                
        }		
		catch(IOException ex) 
		{
            System.out.println("Error reading file '" + fileName + "' (text file)");                  
        }
		return Text;
	}

}

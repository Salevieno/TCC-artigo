package main.mainTCC;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.utilidades.Util;

public abstract class ReadInput 
{
	public static String[][] ReadTxtFile(String fileName)
	{
		String[][] Text = null; // [Cat][Pos]
		String Line;
		int cat = -1;
		try
		{	
			FileReader fileReader = new FileReader (fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader); 		
			Line = bufferedReader.readLine();
			while (!Line.contains("$"))
			{
				if (Line.contains("*"))
				{
					Text = Util.AddElem(Text, null);
					cat += 1;
	            }
				Text[cat] = Util.AddElem(Text[cat], Line);
				Line = bufferedReader.readLine();
				if (Line.isEmpty())
				{
					do
					{
						Line = bufferedReader.readLine();
					} while (Line.isEmpty());
				}
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

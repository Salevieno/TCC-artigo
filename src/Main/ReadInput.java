package Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Utilidades.Util;

public abstract class ReadInput 
{
	public ReadInput()
	{
	
	}
	
	public static String[] ReadControlKeysFile(String fileName)
	{
		String[] ControlKeys = new String[15];
		
		try
		{	
			FileReader fileReader = new FileReader (fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);	
			for (int key = 0; key <= 10 - 1; key += 1)
			{	
				ControlKeys[key] = bufferedReader.readLine();
			}
			bufferedReader.close();
		}		
		catch(FileNotFoundException ex) 
		{
            System.out.println("Unable to find file '" + fileName + "'");                
        }		
		catch(IOException ex) 
		{
            System.out.println("Error reading file '" + fileName + "'");                  
        }
		
		return ControlKeys;
	}
	
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

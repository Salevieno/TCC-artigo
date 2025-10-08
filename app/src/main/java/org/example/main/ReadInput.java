package org.example.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.example.utilidades.Util;

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
				Text[cat] = AddElem(Text[cat], Line);
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
			System.out.println("Loaded file successfully");
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
	
	public static String[] AddElem(String[] OriginalArray, String NewElem)
	{
		if (OriginalArray == null)
		{
			return new String[] {NewElem};
		}
		else
		{
			String[] NewArray = new String[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
 	
}

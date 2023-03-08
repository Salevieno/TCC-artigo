package Utilidades;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
	
	public static String[][] ReadCSVFile(String FileName, int Nrows, int StartRow, int StartCol)
	{
		BufferedReader br = null;
        String line = "";
        String separator = ",";
        int cont = 0;
        String[][] Input = new String[Nrows - StartRow][];       
        try 
        {
            br = new BufferedReader(new FileReader(FileName + ".csv"));
            line = br.readLine();
            while ((line = br.readLine()) != null) 
            {
            	if (StartRow <= cont)
            	{
                    Input[cont - StartRow] = Arrays.copyOfRange(line.split(separator), 1 + StartCol, line.split(separator).length);
            	}
                cont += 1;
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return Input;
	}
	
    public static void PrintVector (int[] vector)
    {
    	/*This function prints a vector*/
    	
    	System.out.print("[");
        for (int i = 0; i <= vector.length - 1; i++) 
        {
        	System.out.print(vector[i] + " ");
        }
        System.out.println("]");
        System.out.println();
    }
    
    public static void PrintVector (double[] vector)
    {
    	/*This function prints a vector*/
    	
    	System.out.print("[");
        for (int i = 0; i <= vector.length - 1; i++) 
        {
        	System.out.print(vector[i] + " ");
        }
        System.out.println("]");
        System.out.println();
    }

    public static void PrintVector (String[] vector)
    {
    	/*This function prints a vector*/
    	
    	System.out.print("[");
        for (int i = 0; i <= vector.length - 1; i++) 
        {
        	System.out.print(vector[i] + " ");
        }
        System.out.println("]");
        System.out.println();
    }

    public static void PrintMatrix (String[][] matrix)
    {
    	/*This function prints a matrix*/
    	
    	if (matrix != null)
    	{
            for (int i = 0; i <= matrix.length - 1; i += 1) 
            {
            	if (matrix[i] != null)
            	{
                    for (int j = 0; j <= matrix[i].length - 1; j += 1)
                    {
                        System.out.print(matrix[i][j] + " ");
                    }
                    System.out.println();
            	}
            	else
            	{
                    System.out.println("matrix[" + i + "] is null at Util -> PrintMatrix");
            	}
            }
    	}
    	else
    	{
            System.out.println("matrix is null at Util -> PrintMatrix");
    	}
        System.out.println();
    }
    
    public static void PrintMatrix (int[][] matrix)
    {
    	/*This function prints a matrix*/
    	
        for (int i = 0; i <= matrix.length - 1; i++) 
        {
            for (int j = 0; j <= matrix[i].length - 1; j++)
            {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void PrintMatrix (double[][] matrix)
    {
    	/*This function prints a matrix*/
    	
        for (int i = 0; i <= matrix.length - 1; i++) 
        {
            for (int j = 0; j <= matrix[i].length - 1; j++)
            {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

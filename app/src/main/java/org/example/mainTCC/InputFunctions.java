package org.example.mainTCC;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class InputFunctions
{
	public static <T> Object loadFromJson(String filename, Class<T> tipoClasse)
	{
		Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename + ".json"))
        {
            return gson.fromJson(reader, tipoClasse);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null ;
        }
	}
}

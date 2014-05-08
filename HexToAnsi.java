
//(c) 2014 XolioWare Interactive


public class HexToAnsi {

	// HEXADECIMAL TO ANSI CONVERTER
	// Usefull ? Quite not. But it does display in color !
	
	static int[][] ansiRGB = {
		{ 0, 0, 0 },
		{ 187, 0, 0 },
		{ 0, 187, 0 },
		{ 187, 187, 0 },
		{ 0, 0, 187 },
		{ 187, 0, 187 },
		{ 0, 187, 187 },
		{ 187, 187, 187 },
	};
	
	static String[] ansiEscape = { 
		"\u001B[0m",
		"\u001B[31m",
		"\u001B[32m",
		"\u001B[33m",
		"\u001B[34m",
		"\u001B[35m",
		"\u001B[36m",
		"\u001B[37m"
	};
	
	public static String convertToAnsi(String text)
	{
		boolean doConvert = !System.getProperty("os.name").toLowerCase().contains("windows");
		//As windows don't support ansi codes in terminal we disable it.
		
		String result = "";
		int i = 0;
		int skip = 0; // skips a few characters when it founds a hex code so it doesn't appear
		for(char c : text.toCharArray())
		{
			if(skip > 0)
			{
				skip--;
			}
			else
			{
				if(c == '#' && text.length()-i-1 >= 6 && ColorsTools.isHexOnly(text.substring(i+1, i+7)))
				{
					String colorCode = text.substring(i+1, i+7);
					int rgb[] = ColorsTools.hexToRGB(colorCode);
					if(doConvert)
						result +=getNearestAnsiOfRgb(rgb);
					skip = 6;
				}
				else
					result += c;
			}
			i++;
		}
		if(doConvert)
			result+="\u001B[0m"; // don't forget to reset the input !
		return result;
	}

	private static String getNearestAnsiOfRgb(int[] rgb) {
		int distance = 999999999; // <- should do it
		int best = 0;
		for(int i = 0; i < ansiRGB.length; i++)
		{
			int distance2 = Math.abs(ansiRGB[i][0]-rgb[0]) + Math.abs(ansiRGB[i][1]-rgb[1]) + Math.abs(ansiRGB[i][2]-rgb[2]);
			if(distance2 < distance)
			{
				best = i;
				distance = distance2;
			}
		}
		return ansiEscape[best];
	}
}

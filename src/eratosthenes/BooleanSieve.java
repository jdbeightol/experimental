package eratosthenes;

public class BooleanSieve 
{
	
	private static final String
		INFO_SIEVEMEMORY = "The sieve will require a minimum of %s of memory.\n",
		ERR_MAXVALUE = "You should never see this. Though the maximum value this implementation can " +
				"handle is 36,893,488,113,059,364,872; the maximum value a 64-bit signed long can " +
				"have is 9,223,372,036,854,775,807.",
		ERR_MEMORY = "The JVM does not have enough memory available to complete this computation.",
		ERR_SIZEMATTERS = "A calculation cannot be completed on integers less than or equal to 1.";
	
	public static void main(String[] args) 
	{
		System.out.println("[JVM] Memory Max:    " + getNamedSize(Runtime.getRuntime().maxMemory()));
		System.out.println("[JVM] Memory Total:  " + getNamedSize(Runtime.getRuntime().totalMemory()));
		System.out.println("[JVM] Memory Free:   " + getNamedSize(Runtime.getRuntime().freeMemory()));
		System.out.println();
		
		booleanSieve(Integer.MAX_VALUE / 2);
	}
	
	private static void booleanSieve(long maxValue)
	{
		int sieveLength = (int)Math.ceil(maxValue / 2.0);
		long startTime = System.nanoTime(), endTime;
		
		final long maxSqrt = (long)Math.ceil(Math.sqrt(maxValue));
		final long bytesRequired = (long)Math.ceil(maxValue / 2.0);
		
		final long totalAllocatedSize =  sieveLength;
		
		System.out.printf(INFO_SIEVEMEMORY, getNamedSize(totalAllocatedSize, true));
		
		if(bytesRequired > Integer.MAX_VALUE * 2L)
			System.out.println(ERR_MAXVALUE);
				
		else if(totalAllocatedSize > Runtime.getRuntime().maxMemory())
			System.out.println(ERR_MEMORY);
		
		else if(maxValue < 2)
			System.out.println(ERR_SIZEMATTERS);
		
		else
		{
			System.out.printf("\nSIZEREQ[%s] ALLOCATED[%d B] ARRAYSIZE[%s] MAXVALUE[%d] SQRT[%d]\n\n",
					getNamedSize(bytesRequired), totalAllocatedSize, getNamedSize(sieveLength), 
					totalAllocatedSize * 2 - 1, maxSqrt);
			
			System.out.println("Finding largest prime factor of " + maxValue);
			System.out.println("Initializing the Sieve of Eratosthenes...");
			
			boolean[] sieve = new boolean[sieveLength];
			
			System.out.print("Calculating primes...");
			
			for(long i = 3; i <= maxSqrt; i += 2)
				if(!sieve[(int)(i / 2)])
					for(long j=i*i; j <= maxValue; j += i * 2)
						sieve[(int)(j / 2)] = true;
//					for(long j=i; j * i <= maxValue; j += 2)
//						sieve[(int)(i * j / 2)] = true;
			
			endTime = System.nanoTime();				
			System.out.println("done.");
			
			boolean bool = false;
			
			for(long k = (maxValue % 2 == 1)?maxValue:maxValue - 1; k > 0 && !bool; k -= 2)
			{			
				if(maxValue == 2)
					System.out.println("Largest Prime: 2");
				
				else if(!sieve[(int)(k / 2)] && (bool = !bool) && k != 1)
					System.out.println("Largest Prime: " + k);
			}
			
			System.out.println("Computation completed in " + (endTime - startTime) / 1000000000.0 + " seconds.");
		}		
	}
	
	public static String getNamedSize(long sizeInBytes)
	{	return getNamedSize(sizeInBytes, false);	}
	
	public static String getNamedSize(long sizeInBytes, boolean longName)
	{
		long size[] = new long[2];
		
		final String[][] PREFIXES = {
			{"Byte", "Kilobyte", "Megabyte", "Gigabyte", "Terabyte", "Petabyte", "Exabyte", "Zettabyte", "Yottabyte"},
			{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"}};
		
		for(size[0] = sizeInBytes; size[0] / 1024 > 0; size[0] = Math.round(size[0] / 1024.0))
			size[1]++;
		
		return size[0] + " " + PREFIXES[(longName)?0:1][(int)size[1]] + ((longName && size[0] != 1)?"s":"");
	}
}
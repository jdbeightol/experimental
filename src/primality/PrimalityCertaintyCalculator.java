package primality;

import java.util.TreeMap;
import java.util.TreeSet;

public class PrimalityCertaintyCalculator
{
	private static final TreeMap<Long, Double> primeList = new TreeMap<Long, Double>();

	public static void main(String[] args)
	{
		long testNumber = Integer.MAX_VALUE;
		
		System.out.printf("Calculating the primality of %d.\n", testNumber);
				
		double primeChance = isPrime(testNumber);
		
		if(primeChance == 1.0)
			System.out.printf("That is definitely a prime number.\n");
		
		else if(primeChance > 0)
			System.out.printf("There is a %.4f%% chance %d is prime.\n", 100 * primeChance, testNumber);
		
		else
			System.out.printf("Nope. Not even a little prime.\n");		
	}
	
	private static void loadPrimeList()
	{
		long[] cp = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};

		for(long a : cp)
			primeList.put(a, 1.0);
	}
	
	private static void savePrimeList()
	{

	}
	
	public static double isPrime(long testNumber)
	{
		double primeChance = 0.5;
		
		if(primeList.keySet().isEmpty())
			loadPrimeList();

  		for(long x : primeList.keySet())
			if(testNumber == x)
  				primeChance = primeList.get(x);
			
			else if(x*x <= testNumber)
  				if(testNumber % x == 0)
  					primeChance = 0;

  		if(primeChance > 0 && primeChance < 1 && 2 == exponentCongruence(2, testNumber, testNumber))
  		{
//  		System.out.printf("%d is a pseudoprime number.\n\t>2^%d ≡ 2 mod %d\n",
//  				testNumber, testNumber, testNumber);
  			primeChance = 1 - 2057.0 / 5761455;
  		}
  		
  		if(primeChance > 0 && primeChance < 1)
  		{
//			System.out.printf("If %d is prime, then there is a %.2f%% chance it is a Mersenne prime.\n\t>(%d = 2^%d - 1)\n",
//					number, isPrime(exponent) * 100, number, exponent);
  			primeChance =  mersenneQualifier(testNumber) * 48.0 / 3443959;
  		}
  		
  		if(!primeList.keySet().contains(testNumber) || primeList.get(testNumber) > primeChance)
  			primeList.put(testNumber, primeChance);  		
  		
  		return primeChance;
	}
	
	private static long exponentCongruence(long number, long exponent, long modulus)
	{
		long ret = 1, max = 0;
		TreeMap<Long, Long> t = new TreeMap<Long, Long>();
		TreeSet<Long> s;
		
		for(int i = 0; i < 63; i++)
		{
			long l = exponent & (1 << i);

			if(l != 0)
				t.put(l, 0L);
		}
		
		s = new TreeSet<Long>(t.keySet());

		//Need a technique to prevent overflows.
		
		for(long j : s)
			max = (j > max)?j:max;
		
		for(long k = 1; k <= max; k *= 2)
			if(!t.containsKey(k/2))
				t.put(k,
						(Math.pow(number, k) < modulus / 2)
						?(long)Math.pow(number, k) % modulus
						:((long)Math.pow(number, k) % modulus) - modulus);
			
			else
				t.put(k, 
					((t.get(k/2) * t.get(k/2)) % modulus < modulus / 2)
					?(t.get(k/2) * t.get(k/2)) % modulus
					:((t.get(k/2) * t.get(k/2)) % modulus) - modulus);
		
		for(long m : s)
			ret = (ret * t.get(m)) % modulus;
		
		if(ret < 0)
			ret += modulus;
				
		return ret;
	}
	
	private static double mersenneQualifier(long number)
	{
		long num = number + 1;
		
		for(long exponent = 1; num % 2 == 0; exponent++)
			if((num /= 2) == 1)
				return isPrime(exponent);
		
		return 0;
	}
}
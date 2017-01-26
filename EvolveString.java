
import java.util.Random;
import java.util.Scanner;
import java.util.HashSet;

public class EvolveString
{
	public static String genString(int length)//generates random string of length
    	{
        String candidateChars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0;i<length;i++)
        {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        return sb.toString();
    }
    public static eString[] generatePopulation(int n, int m) //generates the initial population. n estrings of length m
    {
        eString population[] = new eString[n];
        for (int i=0;i<n;i++)//number of eStrings to be generated
        {
            population[i] = new eString();
            population[i].str = genString(m);
        }
        return population;
    }
    public static void printeStringArray(eString pop[])//method to print an estring array
    {
        for (int i=0;i<pop.length;i++)
        {
            System.out.println("String: " + pop[i].str + " Fitness: " + pop[i].fitness);
        }
    }
    //hamming distance is how I am evaluating the fitness of the string. It is a value that measures how close a string is to the 'perfect' string
    public static int getHammingDistance1(String perfect, String tested)//first method only getting the num of chars that are different

    {
        int distance = 0;
        for (int i=0;i<perfect.length();i++)
        {
            if (perfect.charAt(i)!=tested.charAt(i))
            {
                distance++;
            }
        }
        return distance;
    }
    public static void evaluateFitness(eString pop[], String perfectStr)
    {
        for (int i=0;i<pop.length;i++)
        {
            pop[i].fitness = (perfectStr.length()+1) - getHammingDistance1(perfectStr, pop[i].str);
        }
    }
    //method to return a new population array of eStrings that will reproduce
    //int n is number of parents we want
    public static eString[] getParents(int n, eString population)
    {
        eString newPop[] = new eString[n];
        return newPop;
    }
    public static eString[] singlePC(eString parent1, eString parent2)
    {
	eString children[] = new eString[2];	
	//population[i] = new eString();
	children[0] = new eString();
	children[1] = new eString();

	children[0].str=parent1.str.substring(0,parent1.str.length()/2);
	children[1].str=parent2.str.substring(0,parent2.str.length()/2);
	
	children[0].str+=parent2.str.substring(parent2.str.length()/2);
	children[1].str+=parent1.str.substring(parent1.str.length()/2);

	return children;
    }
    public static eString[] twoPC(eString parent1, eString parent2)
    {
	eString children[] = new eString[2];
	
	children[0] = new eString();
	children[1] = new eString();

	children[0].str = parent1.str.substring(0,parent1.str.length()/3);
	children[1].str = parent2.str.substring(0,parent2.str.length()/3);

	children[0].str += parent2.str.substring(parent2.str.length()/3,(parent2.str.length()/3)*2) +parent1.str.substring((parent1.str.length()/3)*2);
	children[1].str += parent1.str.substring(parent1.str.length()/3,(parent1.str.length()/3)*2) +parent2.str.substring((parent2.str.length()/3)*2);

	return children;
    }
    public static eString[] selectParentsTop(int n, eString[] population)//selects the top n eStrings to be parents, must be even
    {
	if (n%2!=0 || n>population.length)
	{
		System.exit(0);
	}
	eString parents[] = new eString[n];
	for (int i=0;i<n;i++)
	{
		parents[i] = population[i];
	}
	return parents;
    }
    public static eString[] fitnessProportionSelection(eString[] population)
    {
	eString parents[] = new eString[2];
	parents[0] = population[selectRandomParent(population)];
	parents[1] = population[selectRandomParent(population)];
	return parents;
    }
    public static int selectRandomParent(eString[] population)
    {
	double totalWeight = 0;
	
	for (int i=0;i<population.length;i++)
	{
		totalWeight+=population[i].fitness;
	}
	double rVal = new Random().nextDouble() * totalWeight;
	for (int i=0;i<population.length;i++)
	{
		rVal-=population[i].fitness;
		if (rVal<=0)
		{
			return i;
		}
	}
	return population.length-1;
	
	
    }
    public static String rMutate(double muteRate, eString targetStr)
    {
	Random random = new Random();
	char[] tempStr1 = targetStr.str.toCharArray();
	String tempStr2 ="";
	int charsToMutate = (int)(muteRate*targetStr.str.length());

	HashSet<Integer> set = new HashSet<Integer>();
	char cInserts[] = new char[charsToMutate];
	for (int i=0;i<charsToMutate;i++)
	{
		set.add((random.nextInt(targetStr.str.length())));
		cInserts[i] = (char)(random.nextInt(26)+'a');
	}
	int x=0;
	for (int i=0;i<targetStr.str.length();i++)
	{
		if (set.contains(i))
		{
			tempStr2+= "" + cInserts[x];
			x++;
			set.remove(i);
		}
		else
		{
			tempStr2+=tempStr1[i];
		}
	}
	return tempStr2;
    }
    public static void mutatePopulation(eString[] population, double mRate)
    {
		for (int i=0;i<population.length;i++)
		{
			population[i].str = rMutate(mRate, population[i]);
		}
    }
    public static void makeGA()
    {
	//mostly data entry from user
	Scanner sc = new Scanner(System.in);//Scanner to be used throughout
	System.out.println("Enter the string you want to evolve to. At the moment, please use a string of an even length");
        String perfectStr = sc.nextLine();//perfect string we 'evolve' towards
	System.out.println("Enter the number of the original population");
        int popNum = sc.nextInt();//eStrings in population
	
        int strLen = perfectStr.length();//length of strings
	System.out.println("Enter the number of generations you want to test");
        int generations = sc.nextInt();
	
	//booleans below signal different attributes of the GA
	boolean generational=false;
	boolean steadyState=false;
	
	boolean fitProportion=false;
	boolean topN=false;

	boolean singlePoint=false;
	boolean doublePoint=false;
	
	boolean mutation = true;
	double mutFactor=0;

	int parentsNum=0;

	
        
        eString population[] = generatePopulation(popNum, strLen);//initialize population

	System.out.println("Below is the initial population:");
	printeStringArray(population);
	
	System.out.println("Using hamming distance, we will now evaluate the fitness of the population compared to the targeted string.\n Here is the evaluated fitness for the first generation");
	
	evaluateFitness(population, perfectStr);
	eMergeSort.sort(population);
	printeStringArray(population);
	
	for (int i=0;i<generations;i++)
	{
		if (i==0)
		{
			System.out.println("Do you want to use (1)Generational or (2)Steady state?");
			int choice = sc.nextInt();
			switch (choice)
			{
				case 1:
				generational=true;
				break;
				case 2:
				steadyState=true;
				break;
				default:
			}

			System.out.println("Fitness Proportional Roullete used to select the parents\n");
			fitProportion=true;
			System.out.println("It's time to get saucy! How do you want to procede with having our strings reproduce?\n We have 2 methods available to us, (1)single point crossover or (2)double point crossover");
			choice = sc.nextInt();
			switch (choice)
			{
				case 1:
				singlePoint = true;
				break;

				case 2:
				doublePoint = true;
				break;
				default:
			}
			
		}
		eString[] newPop = new eString[popNum];//where next generation will be stored
		if (fitProportion)
		{
			if (generational)
			{
				parentsNum = popNum;
			}
			else if(steadyState && i==0)
			{
				System.out.println("How many parents do you want to use? Must enter even number");
				parentsNum = sc.nextInt();
			}
			for (int j=0;j<parentsNum;j+=2)
			{
				eString couple[] = fitnessProportionSelection(population);
				eString newPair[] = new eString[2];
				if (singlePoint)
				{
					newPair = singlePC(couple[0],couple[1]);
				}
				else
				{
					newPair= twoPC(couple[0],couple[1]);
				}
				newPop[j] = newPair[0];
				newPop[j+1] = newPair[1];
			}
			if (steadyState)
			{
				int count = popNum-parentsNum;
				for (int j=parentsNum;j<population.length;j++)
				{
					newPop[j] = population[j];
				}
			}
			population = newPop;
			System.out.println("Here we have generation" + i + " of the strings");
			evaluateFitness(population, perfectStr);
			eMergeSort.sort(population);
			printeStringArray(population);
	
			if (i==0)
			{
				System.out.println("In order to increase genetic diversity, we can add in random mutations. To skip adding a mutation factor, type 0. Otherwise, insert a double below 1.0 (1.0 is 100% mutation, 0.0 is 0% mutation");
				mutFactor = sc.nextDouble();
				if (mutFactor<=0 || mutFactor > 1.0)
				{
					mutation = false;
					System.out.println("The number you have entered is either 0 or out of bounds. Mutation skipped.");
				}
			}
			if (mutation)
			{
				System.out.println("Population mutated by " + mutFactor*100 + "%");
				mutatePopulation(population,mutFactor);
				evaluateFitness(population, perfectStr);
				eMergeSort.sort(population);
				printeStringArray(population);
			}	
		}	
	}
	
    }
    public static void main(String[] args)
    {
	makeGA();
	Scanner sc = new Scanner(System.in);
	System.out.println("\n Do you want to try again (Y)/(N)");
	
	String choice = sc.nextLine();
	switch (choice)
	{
		case "y":
			main(args);
			break;
		default:
			System.exit(0);
	}
    }
}

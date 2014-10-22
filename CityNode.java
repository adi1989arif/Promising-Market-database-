

/**
 * This class holds all the data  for each city.
 * the name of the city the state and the population
 * in each year is stored in this class.
 * 
 * @author  Sohil Shah
 * @author  Adiba Arif
 * @author  Sneha Rateria 
 * 
 */
public class CityNode {
	String city;
	String state;
	int population_in_2010;
	int population_in_2011;
	int population_in_2012;

	public CityNode(String _city, String _state, int pop_2010, int pop_2011,
			int pop_2012) {
		city = _city;
		state = _state;
		population_in_2010 = pop_2010;
		population_in_2011 = pop_2011;
		population_in_2012 = pop_2012;
	}

	/**
	 * 
	 */
	public CityNode() {
	}

	public int getPopulation2010() {
		return population_in_2010;
	}

	public String toString() {
		return "City : " + city + "   State : " + state + " Population in 2010 :  "
				+population_in_2010 +"     2011 : "+population_in_2011 +"      2012 : "
				+ population_in_2012 + " ";
	}
}

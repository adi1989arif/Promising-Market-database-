 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * This Program give the value of top 5 state and top 5 cities that have max growth
 * and top 5 cities that have min growth.
 * 
 * @author Sohil Shah
 * @author Adiba Arif
 * @author Sneha Rateria
 * 
 */
public class PromisingMarkets {
	public static CityNode[] maxGrowth; //stores the values of top 5 cities with max % growth
	public static CityNode[] minGrowth;	//stores the vaues of  top 5 cities with least % growth
	public static Map<String, Double> cummulative;			//stores the values of all states and their cummulative growth
	public static PriorityQueue<CityNode> queue;//queue to store data for each city acording to priority specified by the comparator.
	public static StateNode[] stateNode;
	public static int num_of_entries =10;
	public static int number_of_states = 5;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	public static int population = 5000;
	
	public PromisingMarkets(int n, int population, int states){
		num_of_entries = n;
		maxGrowth = new CityNode[num_of_entries];
		minGrowth = new CityNode[num_of_entries];
		queue = new PriorityQueue<>(19516,
				percentGrowth);
		number_of_states = states;
		stateNode = new StateNode[number_of_states];
		this.population = population;
	}
	 	
	public void init() {
		CityNode node;	
		String[] US_STATES = { "Alabama", "Alaska", "Arizona", "Arkansas",
				"California", "Colorado", "Connecticut", "Delaware",
				"District of Columbia", "Florida", "Georgia", "Hawaii",
				"Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
				"Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan",
				"Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska",
				"Nevada", "New Hampshire", "New Jersey", "New Mexico",
				"New York", "North Carolina", "North Dakota", "Ohio",
				"Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
				"South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
				"Vermont", "Virginia", "Washington", "West Virginia",
				"Wisconsin", "Wyoming" };

		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = null;
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/bigdata?","root","1234");
			
			statement = connect.createStatement();
			
			resultSet = statement
			          .executeQuery("select * from bigdata.Metropolitan_Populations__20102012_");
			
			System.out.println("Success");
			while(resultSet.next()){
				String place = resultSet.getString("Geography");
				String city = extractCity(place);
				String state = extractState(place);
				int ext_2010 = Integer.parseInt(resultSet.getString("2010_Population"));
				int ext_2011 = Integer.parseInt(resultSet.getString("2011_Population"));
				int ext_2012 = Integer.parseInt(resultSet.getString("2012_Population"));
				node = new CityNode(city, state, ext_2010, ext_2011, ext_2012);
				queue.add(node);
			}
			while (true) {
				CityNode cn = queue.poll();
				if (cn == null)
					break;
				addToLists(cn);
			}
			
			PriorityQueue<StateNode> stateRanking = new PriorityQueue<>(50,
					state_order);
			for (int i = 0; i < US_STATES.length - 1; i++) {
				if (cummulative.get(US_STATES[i]) == null) {
					continue;
				} else {
					double value = cummulative.get(US_STATES[i]);
					stateRanking.add(new StateNode(US_STATES[i], value));
				}
			}

			for (int i = 0; i < number_of_states; i++) {
				StateNode sn = stateRanking.poll();
				stateNode[i] = sn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Add perent growth of each city in a map.
	 * @param cn
	 */
	private static Boolean addToLists(CityNode cn) {
		if (cn.population_in_2010 > population) {
			checkForMax(cn);
			checkForLeast(cn);
		}
		if (cummulative == null) {
			cummulative = new TreeMap();
		}
		if (cummulative.containsKey(cn.state)) {
			double cur_cummulativeValue = calculateCummulative(cn);
			double prev_cummulativeValue = (double) cummulative.get(cn.state);
			double final_cummulativeValue = cur_cummulativeValue
					+ prev_cummulativeValue;
			cummulative.put(cn.state, final_cummulativeValue);
			return true;
		} else {
			cummulative.put(cn.state, (double) calculateCummulative(cn));
			return true;
		}
	}

	/** Calculates the percentage growth.
	 * @param cn
	 * @return
	 */
	private static double calculateCummulative(CityNode cn) {
		int diff = cn.population_in_2012 - cn.population_in_2010;
		double pc_growth = ((double) diff / cn.population_in_2012) * 100;
		return pc_growth;
	}

	/**Adding city to the minGrowth array
	 * @param cn
	 */
	private static Boolean checkForLeast(CityNode cn) {
		for (int i = 0; i < minGrowth.length; i++) {
			if (minGrowth[i] == null) {
				minGrowth[i] = cn;
				return true;
			}
		}
		return false;
	}

	/**Adding city to the maxGrowth array.
	 * @param cn
	 */
	private static boolean checkForMax(CityNode cn) {
		if (maxGrowth[0] == null) {
			maxGrowth[0] = new CityNode();
			maxGrowth[0] = cn;
			return true;
		} else {
			for (int i = maxGrowth.length - 1; i > 0; i--) {
				maxGrowth[i] = maxGrowth[i - 1];
			}
			maxGrowth[0] = cn;
			return true;
		}
	}
	/*
	 * This is the comparator for state node. This is used to compare states in 
	 * priority heap.
	 */
	public static Comparator<StateNode> state_order = new Comparator<StateNode>() {
		@Override
		public int compare(StateNode o1, StateNode o2) {
			if (o1.getCummulativeValue() < o2.getCummulativeValue())
				return 1;
			else if (o1.getCummulativeValue() == o2.getCummulativeValue())
				return 0;
			else if (o1.getCummulativeValue() > o2.getCummulativeValue())
				return -1;
			return 10;
		}

	};
	/*
	 * This is the comparator for city node.
	 * This is used in priority heap to orde cities according to their growth percent.
	 */
	public static Comparator<CityNode> percentGrowth = new Comparator<CityNode>() {

		@Override
		public int compare(CityNode o1, CityNode o2) {
			if (o1.population_in_2012 <= 0)
				return -1;
			if (o2.population_in_2012 <= 0)
				return 1;
			int this_diff = o1.population_in_2012 - o1.population_in_2010;
			int that_diff = o2.population_in_2012 - o2.population_in_2010;
			double this_pc_growth = ((double) this_diff / o1.population_in_2012) * 100;
			double that_pc_growth = ((double) that_diff / o2.population_in_2012) * 100;
			if (this_pc_growth < that_pc_growth)
				return -1;
			else if (this_pc_growth == that_pc_growth)
				return 0;
			else
				return 1;
		}

	};

	/**This method extracts the state name from the given string.
	 * @param strLine
	 * @return
	 */
	private static String extractState(String strLine) {
		char[] inputString = strLine.toCharArray();
		String state = "";
		for (int i = 0; i < inputString.length; i++) {
			if (inputString[i] == ',') {
				i = i + 2;
				while (i < inputString.length) {
					state = state + inputString[i];
					i++;
				}
				return state;

			}
		}
		return null;
	}

	/**
	 * This method extracts the city name from the string that is provided.
	 * The string provided has cityname, state which needs to be separated
	 * @param strLine
	 * @return city name
	 */
	private static String extractCity(String strLine) {
		char[] inputString = strLine.toCharArray();
		String city = "";
		for (int i = 0; i < inputString.length;) {
				while (inputString[i] != ',') {
					city = city + inputString[i];
					i++;
				}
				return city;
			}
		return null;
	}
}

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

	//This does nothing. Don't worry about it... testing comment 123
	public Main() {}
	
	enum Page {LOGIN, CREATE_NEW, MAIN_MENU, MOVIE_SEARCH, MOVIE_INFO, FOLLOWING, FOLLOWING_EDIT, SETTINGS, SEARCH_RESULTS};
	static Page page = Page.LOGIN;
	static String USERNAME = null;
	static EmbeddedSQL db = null;
	static Boolean dbLoaded = false;
	
	
	static ArrayList<String> searchResults = new ArrayList<String>();

	public static void main(String args[]) {
		if(args.length == 4){
			try {
				 // use postgres JDBC driver.
		         Class.forName ("org.postgresql.Driver").newInstance ();
		         // instantiate the EmbeddedSQL object and creates a physical
		         // connection.
		         String dbname = args[0];
		         String dbport = args[1];
		         String user = args[2];
		         String passwd = args[3];
		         db = new EmbeddedSQL (dbname, dbport, user, passwd);
		         dbLoaded = true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		System.out.println("Hello, Wolrd!");
		
		
		printtest2();
		
		
		//Keep promting until the system exits
		while(true){
			printPrompt();
		}
	}
	
	public static void printPrompt(){
		
		//lock repeats force user to continuously enter information in loop.
		Boolean lockRepeat = true;
		
		switch (page) {
		
		//LOGIN SCREEN
		case LOGIN:
			clearConsole();
			System.out.println("Welcome to MovieNet!");
			System.out.println("\n\n\n-=LOGIN MENU=-");
			
			lockRepeat = true;
			while (lockRepeat){
				System.out.println("Please Choose one of the following options:");
				System.out.println("1.\tLOGIN");
				System.out.println("2.\tCREATE NEW");
				System.out.println("0.\tEXIT");
				int input = getIntInput();
				
				switch (input) {
				
				case 1:
					logIn();
					lockRepeat = false;
					break;
					
				case 2:
					page = Page.CREATE_NEW;
					lockRepeat = false;
					break;
					
				case 0:
					System.out.println("Exiting... Good-Bye!");
					System.exit(0);
					break;
					
				default:
					System.out.println("Invalid input. Please try again.");
					break;
				}
			}
			break;
		
		case CREATE_NEW:
			createNewAccount();
			break;
			
		//MAIN MENU
		case MAIN_MENU:
			clearConsole();
			System.out.println("=MAIN MENU=-");
			
			lockRepeat = true;
			while (lockRepeat){
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tSEARCH MOVIES");
				System.out.println("2.\tVIEW WALL");
				System.out.println("3.\tSETTINGS");
				System.out.println("0.\tLOGOUT");
				
				int input = getIntInput();
				
				switch (input) {

				case 1:
					page = Page.MOVIE_SEARCH;
					lockRepeat = false;
					break;
					
				case 2:
					page = Page.FOLLOWING;
					lockRepeat = false;
					break;
					
				case 3:
					page = Page.SETTINGS;
					lockRepeat = false;
					break;
					
				case 0:
					page = Page.LOGIN;
					lockRepeat = false;
					break;
					
				default:
					System.out.println("Invalid input. Please try again.");
					break;
				}
			}
			break;
		
		//MOVE SEARCH PAGE
		case MOVIE_SEARCH:
			clearConsole();
			System.out.println("=MOVIE SEARCH=-");
			
			lockRepeat = true;
			while (lockRepeat){
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tSEARCH BY TITLE");
				System.out.println("2.\tSEARCH BY DIRECTOR");
				System.out.println("3.\tLIST ALL MOVIE TITLES");
				System.out.println("0.\tBACK TO MAIN MENU");
				
				int input = getIntInput();
				
				switch (input) {
				
				case 0:
					page = Page.MAIN_MENU;
					lockRepeat = false;
					break;

				case 1:
					page = Page.SEARCH_RESULTS;
					search_by(0);
					lockRepeat = false;
					break;
			
				case 2:
					page = Page.SEARCH_RESULTS;
					search_by(1);
					lockRepeat = false;
					break;

				case 3:
					page = Page.SEARCH_RESULTS;
					search_by(2);
					lockRepeat = false;
					break;

				default:
					System.out.println("Invalid input. Please try again.");
					break;
				}
			}
				
				
			break;

		//Movie Search Results
		case SEARCH_RESULTS:
			clearConsole();
			System.out.println("=Search_results=-");

			lockRepeat = true;
			while (lockRepeat){
				System.out.println("CHOOSE MOVIE TO VIEW.");
				System.out.println("0.\tBACK TO MOIVE SEARCH OPTIONS");
				for(int i = 0; i < searchResults.size(); i++){
					System.out.printf("%d.\t%s\n",i+1,searchResults.get(i));			
				}

				int input = getIntInput();
				if (input == 0) {
					page = Page.MOVIE_SEARCH;
					lockRepeat = false;
				}
				else if(input < searchResults.size()){
					page = Page.MOVIE_INFO;
				}

				else{
					System.out.println("Invalid input. Please try again.");
				}
			}
			break;
			

			
		//DETAIL MOVIE INFORMTION	
		case MOVIE_INFO:
			clearConsole();
			System.out.println("=INSERT MOVIE NAME TITLE HERE INFO=-");

			lockRepeat = true;
			while (lockRepeat){
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tORDER DVD");
				System.out.println("2.\tWATCH ONLINE");
				System.out.println("3.\tVIEW COMMENTS");
				System.out.println("4.\tWRITE A COMMENT");
				System.out.println("0.\tBACK TO MAIN MENU");
				
				int input = getIntInput();
				
				switch (input) {
				
				case 0:
					page = Page.MAIN_MENU;
					lockRepeat = false;
					break;

				default:
					System.out.println("Invalid input. Please try again.");
					break;
				}
			}
			break;
		
		//USER'S FOLLOWING WALL OF INFO
		case FOLLOWING:
			clearConsole();
			System.out.println("=FOLLOWING WALL=-");
			break;
			
		//ADD/REMOVE THE ACCOUTN USER IS FOLLOWING	
		case FOLLOWING_EDIT:
			clearConsole();
			System.out.println("=EDIT PEOPLE I AM FOLLOWING=-");
			break;
			
		//USER'S SETTINGS
		case SETTINGS:
			clearConsole();
			System.out.println("=SETTINGS=-");
			break;

		default:
			System.out.println("INVALID PAGE. LOGGING OUT.");
			logOut();
			break;
		}
	}
	
	//If there does not exist a matching pair in the database, then don't change page.
	//If the pair is in database, change USERNAME global variable to userID and
	//go to the main menu
	public static void logIn(){
		System.out.println("USERNAME:");
		String usernameInput = getStringInput();
		System.out.println("PASSWORD:");
		String passwordInput = getStringInput();

		//Validate log in
		if (validateLogin(usernameInput, passwordInput)) {
			USERNAME = usernameInput;
			page = Page.MAIN_MENU;
		}
		else{
			System.out.println("Invalid Login information!");
		}
		
	}
	
	public static void logOut(){
		USERNAME = null;
		page = Page.LOGIN;
		System.out.println("You have successfully logged out.");
		System.out.println("Good-Bye!");
		clearConsole();
	}
	
	//////////////////////////////////////////////
	/////////////Create & Delete/////////////////
	/////////////////////////////////////////////
	
	//TODO: Finish this function
	//Creates a new account. Stores the data in the database.
	//TODO: Get address and other info?
	public static void createNewAccount(){

		String usernameInput = getCheckInput("USERNAME",true);		
		String passwordInput = getCheckInput("PASSWORD",true);
		String firstInput = getCheckInput("First name",true);		
		String middleInput = getCheckInput("Middle name",false);		
		String lastInput  = getCheckInput("Last name",true);
		String emailInput  = getCheckInput("e-mail",true);	
		String street1Input = getCheckInput("Address - street (line 1)",false);
		String street2Input = getCheckInput("Address - street (line 2)",false);
		String stateInput = getCheckInput("Address - state",false);
		String countryInput = getCheckInput("Address - country",false);
		String zipInput = getCheckInput("Address - zipcode",false);
		String preferencesInput = getCheckInput("Preferred Generes",false);

		if(dbLoaded){
			dbUpdate(
					"INSERT INTO users " +
					"VALUES ('" + usernameInput + "','" + passwordInput + "','" + firstInput + "','" + middleInput + "','" + lastInput + "','" + emailInput + "','" + street1Input + "','" + street2Input + "','" + stateInput + "','" + countryInput + "','" + zipInput + "', 0)");
		}
		
		System.out.println("\nAccount Created!");
		System.out.println("Returning to the login page...");
		page = Page.LOGIN;
	}
	
	
	//////////////////////////////////////////////
	/////////////Utility Functions////////////////
	//////////////////////////////////////////////
	
	public static String getStringInput(){
		Scanner in = new Scanner(System.in);
		String result = in.nextLine();
		//in.close();
		return result;
	}
	
	public static int getIntInput(){
		Scanner in = new Scanner(System.in);
		int result = in.nextInt();
		//in.close();
		return result;
	}
	
	public static float getFloatInput(){
		Scanner in = new Scanner(System.in);
		float result = in.nextFloat();
		//in.close();
		return result;
	}

	public static void clearConsole(){
		for(int i = 0; i < 50; i++){
			System.out.printf("\n");
		}
	}

	public static String getCheckInput(String s, Boolean required){

		clearConsole();
		System.out.println("Please enter the following data:");

		Boolean valid = false; //flag to indicate if we can move to the next item
		String returnval = "";

		while(!valid){
			System.out.printf("%s: ",s);
			String Input = getStringInput();

			//When speical case passwords
			String Input2 = "";
			if(s.equals("PASSWORD")){
				System.out.printf("CONFIRM PASSWORD: ");
				Input2 = getStringInput();
			}

			//TODO: Check if it is NOT already in database
			Boolean inDatabase = false;
			if(s.equals("USERNAME")){} //speical case for username

			//Check to see if its a valid input
			if(s.equals("PASSWORD") && !Input.equals(Input2))
				{System.out.println("Passwords to not match. Try again.");}
			else if( (!Input.equals("") || !required) && Input.length() < 20 && !inDatabase)
				{returnval = Input; valid = true;}
			else if(Input.length() >= 20)
				{System.out.printf("Invalid %s, too long. Try Again.\n",s);}
			else 
				{System.out.printf("Invalid %s. Try Again.\n",s);}
		}
		return returnval;
	}

	public static void search_by(int searchType){

		searchResults.clear();

		//Search by title
		if(searchType == 0){
			System.out.printf("Enter title name: ");
			String Input = getStringInput();
			sql_getTitle(Input);		
		}
		
		//Search by directors
		else if(searchType == 1){
			System.out.printf("Enter director name: ");
			String Input = getStringInput();
			sql_getDirector(Input);
		}

		//LIST ALL titles
		else if(searchType == 2){
			sql_getAllTitles();
		}

	}

	//////////////////////////////////////////////
	/////////////SQL Functions////////////////
	//////////////////////////////////////////////

	//TODO: SQL QUERY TO RETURN movies searched by title
	public static void sql_getTitle(String Input){

		//put into array with format: [Title], [director], [genre], [Category], [price]
		//example:
		String s = "Serenity, Joss Whedon, SciFi, Movie, $5";
		String s2 = "Man of Steel, Zack Snyder, Action, Movie, $20";
		searchResults.add(s);
		searchResults.add(s2);
	}

	
	//TODO: SQL QUERY TO RETURN movies searched by director
	public static void sql_getDirector(String Input){

		//put into array with format: [Title], [director], [genre], [Category], [price]
		//example:
		String s = "Serenity, Joss Whedon, SciFi, Movie, $5";
		String s3 = "Community, Anthony Russo, Comedy, Series, $100";
		searchResults.add(s);
		searchResults.add(s3);

	}

	//TODO:: SQL QUERY TO PRINT ALL movies,sorted by title
	public static void sql_getAllTitles(){

		//put into array with format: [Title], [director], [genre], [Category], [price]
		//example:
		String s = "Serenity, Joss Whedon, SciFi, Movie, $5";
		String s2 = "Man of Steel, Zack Snyder, Action, Movie, $20";
		String s3 = "Community, Anthony Russo, Comedy, Series, $100";
		searchResults.add(s);
		searchResults.add(s2);
		searchResults.add(s3);
	}

	public static Table runQuery(String query){
		Table t = null;
		try {
			t = db.executeQuery(query);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return t;
	}
	
	public static void dbUpdate(String update){
		System.out.println("UPDATE: " + update);
		try {
			db.executeUpdate(update);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public static void testPrint(){
		Table t = null;
		try {
			t = db.executeQuery("SELECT * FROM video;");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		System.out.println(t.toString());
	}

	public static Boolean validateLogin(String userID, String password){
		Table t = runQuery("SELECT COUNT(*) FROM users WHERE user_id = '" + userID + "' AND password = '" + password + "';");
		ArrayList<String> a = t.getInfoFromColumn("count");
		if(a.isEmpty()){return false;}
		if(Integer.valueOf(a.get(0)) == 1){
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void printtest2(){
		if (dbLoaded){
			System.out.println("PRINTING TESTS");
			System.out.println(validateLogin("user1", "pass1"));
			System.out.println(validateLogin("user2", "pass1"));
			System.out.println(validateLogin("user2", "pass2"));
			System.out.println("PRINTING TESTS");
		}
		else{
			System.out.println("DB not loaded, no testing :(");
		}
	}
	
	public static void printtest3(){
		if (dbLoaded){
			Table t2 = runQuery("SELECT user_id FROM users;");
			System.out.println(t2.toString());
			Table t3 = runQuery("SELECT * FROM users WHERE user_id = 'user1' AND password = 'pass1';");
			System.out.println(t3.toString());
		}
		else{
			System.out.println("DB not loaded, no testing :(");
		}
	}
}

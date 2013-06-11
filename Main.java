import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Main {

	//This does nothing. Don't worry about it... testing comment 123
	public Main() {}
	
	enum Page {LOGIN, CREATE_NEW, MAIN_MENU, MOVIE_SEARCH, MOVIE_INFO, FOLLOWING, FOLLOWING_EDIT, SETTINGS, SEARCH_RESULTS,USER_SEARCH,WATCH_ONLINE,USER_INFO};
	static Page page = Page.LOGIN;
	static String USERNAME = null;
	static EmbeddedSQL db = null;
	static Boolean dbLoaded = false;
	static String currMovie = null;
	static String currUser = null;
	static Boolean currIsMovie = false;
	static Boolean isSuperUser = false;
	static String searchedInput = "";	

	static ArrayList<String> searchResults = new ArrayList<String>();
	static String movieInfo = "";

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
				isSuperUser = true;
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
			System.out.println("-=MAIN MENU=-");
			
			lockRepeat = true;
			while (lockRepeat){
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tSEARCH MOVIES");
				System.out.println("2.\tSEARCH USERS");
				System.out.println("3.\tVIEW WALL");
				System.out.println("4.\tSETTINGS");
				if(isSuperUser){System.out.println("5.\tADD NEW MOVIE");}
				System.out.println("0.\tLOGOUT");
				
				int input = getIntInput();
				
				switch (input) {

				case 1:
					page = Page.MOVIE_SEARCH;
					lockRepeat = false;
					break;
					
				case 2:
					page = Page.USER_SEARCH;
					lockRepeat = false;
					break;

				case 3:
					page = Page.FOLLOWING;
					lockRepeat = false;
					break;
					
				case 4:
					page = Page.SETTINGS;
					lockRepeat = false;
					break;

				case 5:
					if(isSuperUser){addNewMovie();}
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
			System.out.println("-=MOVIE SEARCH=-");
			
			lockRepeat = true;
			while (lockRepeat){
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tSEARCH BY TITLE");
				System.out.println("2.\tSEARCH BY DIRECTOR");
				System.out.println("3.\tSEARCH BY GENRE");
				System.out.println("4.\tLIST ALL MOVIE TITLES");
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
					search_by(6);
					lockRepeat = false;
					break;

				case 4:
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

		//USER SEARCH PAGE
		case USER_SEARCH:
			clearConsole();
			System.out.println("=USER SEARCH=-");
			
			lockRepeat = true;
			while (lockRepeat){
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tSEARCH BY USERNAME");
				System.out.println("2.\tSEARCH BY EMAIL");
				System.out.println("3.\tLIST ALL USERS");
				System.out.println("0.\tBACK TO MAIN MENU");
				
				int input = getIntInput();
				
				switch (input) {
				
				case 0:
					page = Page.MAIN_MENU;
					lockRepeat = false;
					break;

				case 1:
					page = Page.SEARCH_RESULTS;
					search_by(3);
					lockRepeat = false;
					break;
			
				case 2:
					page = Page.SEARCH_RESULTS;
					search_by(4);
					lockRepeat = false;
					break;

				case 3:
					page = Page.SEARCH_RESULTS;
					search_by(5);
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
			System.out.println("== Results for '" +searchedInput+ "' ==-");

			lockRepeat = true;
			while (lockRepeat){
				System.out.println("=========================================\n");
				System.out.println("CHOOSE Which TO VIEW.");
				System.out.println("0.\tBACK TO MAIN MENU");
				
				sql_printSearchResults();
				
				if(searchResults.size() == 0)
					{System.out.println("No searches found");}


				int input = getIntInput();
				if (input == 0) {
					page = Page.MAIN_MENU;
					lockRepeat = false;
				}
				else if(input -1 < searchResults.size()){
					if(currIsMovie){
						page = Page.MOVIE_INFO;
						currMovie = (searchResults.get(input-1));
					}
					else {
						page = Page.USER_INFO;
						currUser = (searchResults.get(input-1));
					}
					
					lockRepeat = false;
				}
				else{
					System.out.println("Invalid input. Please try again.\n\n");
				}
			}
			break;
			

			
		//DETAIL MOVIE INFORMTION	
		case MOVIE_INFO:
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("-=MOVIE INFO=-");
				sql_printMovieInfo(currMovie);
				System.out.println("\n Please select what you would like to view:");
				System.out.println("1.\tORDER DVD");
				System.out.println("2.\tWATCH ONLINE");
				System.out.println("3.\tVIEW COMMENTS");
				System.out.println("4.\tWRITE A COMMENT");
				System.out.println("5.\tRATE");
				System.out.println("6.\tFAVORITE");
				System.out.println("0.\tBACK TO SEARCH RESULTS");
				
				int input = getIntInput();
				
				switch (input) {
				
				case 1:
					sql_placeOrder(currMovie);
					System.out.println("Press any key to continue");
					getStringInput();
					break;

				case 2:
					if(sql_placeOrder(currMovie)){
						page = Page.WATCH_ONLINE;
						lockRepeat = false;
					}
					System.out.println("Press any key to continue");
					getStringInput();
					break;

				case 3:
					sql_printComments(currMovie);
					break;

				case 4:
					writeComment();
					break;

				case 5:
					rateMovie();
					break;

				case 6:
					favoriteMovie();
					break;
					
				case 0:
					page = Page.SEARCH_RESULTS;
					lockRepeat = false;
					break;

				default:
					System.out.println("Invalid input. Please try again.");
					break;
				}
			}
			break;

		//Searched user's info
		case USER_INFO:
			
			
			while(lockRepeat){
				clearConsole();
				System.out.println("=USER's INFO=");
				sql_printUserInfo(currUser);
				System.out.println("Select what you would like to do:");
				System.out.println("0.\tBACK TO SEARCH RESULTS");
				System.out.println("1.\tFOLLOW USER");
				if(isSuperUser){System.out.println("2.\tDELETE USER");}
				int input = getIntInput();
				switch (input) {
					case 0:
						page = Page.SEARCH_RESULTS;
						lockRepeat = false;
						break;

					case 1:
						followUser();
						break;
				
					case 2:
						if(isSuperUser){deleteUser(currUser);}
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
			sql_printMyWall();
			lockRepeat = true;
			while(lockRepeat){
				System.out.println("1.\tEDIT PEOPLE I AM FOLLOWING");
				System.out.println("0.\tBACK TO MAIN MENU");
				int input = getIntInput();
				switch (input) {
					case 1:
						page = Page.FOLLOWING_EDIT;
						lockRepeat = false;
						break;

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
			
		//ADD/REMOVE THE ACCOUTN USER IS FOLLOWING	
		case FOLLOWING_EDIT:
			clearConsole();
			System.out.println("=EDIT PEOPLE I AM FOLLOWING=-");
			lockRepeat = true;
			sql_printFollowing();
			while(lockRepeat){
				System.out.println("Select whom to stop following");
				System.out.println("0.\tBACK TO MY WALL");
				int input = getIntInput();

				if(input == 0){
					page = Page.FOLLOWING;
					lockRepeat = false;
				}

				else{
					System.out.println("Invalid input. Please try again.");
				}
			}
			break;

		case WATCH_ONLINE:
			clearConsole();
			System.out.println("-=WATCHING ONLINE=-");
			printMovie();
			System.out.println();
			lockRepeat = true;
			while(lockRepeat){
				System.out.println("0.\tBACK");
				int input = getIntInput();
				switch(input){
					case 0:
						page = Page.MOVIE_INFO;
						lockRepeat = false;
						break;
					default:
						System.out.println("Invalid input. Please try again.");
						break;
				}
			}

		break;
			
		//USER'S SETTINGS
		case SETTINGS:
			
			System.out.println("-=SETTINGS=-");
			
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("\n Please select the setting you would like to change:");
				System.out.println("1.\tCHECK BALANCE");
				System.out.println("2.\tINCREASE BALANCE");
				System.out.println("3.\tMAKE ACCOUNT PRIVATE");
				System.out.println("4.\tMAKE ACCOUNT PUBLIC");
				System.out.println("0.\tBACK");
				
				int input = getIntInput();
				
				switch (input) {

				case 1:
					sql_printBalance();
					System.out.println("Press any Key to Continue");
					getStringInput();
					break;
					
				case 2:
					sql_increaseBalance();
					System.out.println("Press any Key to Continue");
					getStringInput();
					break;
					
				case 3:
					System.out.println("Privacy Features aren't implemented in the SQL...");
					System.out.println("Press any Key to Continue");
					getStringInput();
					break;
					
				case 4:
					System.out.println("Privacy Features aren't implemented in the SQL...");
					System.out.println("Press any Key to Continue");
					getStringInput();
					break;
					
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
		System.out.printf("USERNAME: ");
		String usernameInput = getStringInput();
		System.out.printf("PASSWORD: ");
		String passwordInput = getStringInput();

		//Validate log in
		if (!dbLoaded || validateLogin(usernameInput, passwordInput)) {
			USERNAME = usernameInput;
			page = Page.MAIN_MENU;
			System.out.println("\nLogin in SUCCESS!");
			isSuperUser = sql_isSuperUser(USERNAME);
		}
		else{
			System.out.println("\nInvalid Login information!");
		}
		System.out.println("Press any Key to Continue");
		getStringInput();
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

	//Creates a new account. Stores the data in the database.
	public static void createNewAccount(){

		String usernameInput = getCheckInput("USERNAME",true,9);		
		String passwordInput = getCheckInput("PASSWORD",true,36);
		String firstInput = getCheckInput("First name",true,40);		
		String middleInput = getCheckInput("Middle name",false,40);		
		String lastInput  = getCheckInput("Last name",true,40);
		String emailInput  = getCheckInput("e-mail",true,40);	
		String street1Input = getCheckInput("Address - street (line 1)",false,40);
		String street2Input = getCheckInput("Address - street (line 2)",false,40);
		String stateInput = getCheckInput("Address - state",false,10);
		String countryInput = getCheckInput("Address - country",false,20);
		String zipInput = getCheckInput("Address - zipcode",false,20);
		String preferencesInput = getCheckInput("Preferred Generes",false,40);

		if(dbLoaded){
			dbUpdate(
					"INSERT INTO users " +
					"VALUES ('" + usernameInput + "','" + passwordInput + "','" + firstInput + "','" + middleInput + "','" + lastInput + "','" + emailInput + "','" + street1Input + "','" + street2Input + "','" + stateInput + "','" + countryInput + "','" + zipInput + "', 0)");
		}
		
		System.out.println("\nAccount Created!");
		System.out.println("Returning to the login page...");
		System.out.println("Press any key to continue");
		getStringInput();
		page = Page.LOGIN;
	}

	//todo: add directors, authors, episode ,season
	public static void addNewMovie(){
		String titleInput = getCheckInput("Title",true,50);
		int yearInput = getCheckInput2("Year",true,1500,3000);
		int online_priceInput = getCheckInput2("Online Price",true,0,9999999);
		int dvd_priceInput = getCheckInput2("DVD Price",true,0,999999);
		int votesInput = 0;
		int ratingInput = 0;

		/*
		String episodeInput = "";
		int seasonInput = 0;
			
		System.out.println("TV Show? (y/n)");
		String ismovie =  getStringInput();
		Boolean isTVSHOW = false;
		if(ismovie.substring(0,1).equals("y") || ismovie.substring(0,1).equals("Y"))
			{isTVSHOW = true;}
		
		if(isTVSHOW){
			episodeInput = getCheckInput("Episode #",true,9);
			seasonInput = getCheckInput2("Season #",true,0,999); 
		}
		*/
		if(dbLoaded){
			dbUpdate(
					"INSERT INTO video " +
					"VALUES (0,'" + titleInput + "'," + yearInput + "," + online_priceInput + "," + dvd_priceInput + ")");
		}

	
		System.out.println("\nMoive Created!");
		System.out.println("Returning to Main Menu...");
		System.out.println("Press any key to continue");
		getStringInput();
		page = Page.MAIN_MENU;
		
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

		try {
         int result = in.nextInt();
			return result;
      }
      catch (Exception e)
      {
         System.out.println("Couldn't parse input, please try again");
			return -1;
      }
	}
	
	public static float getFloatInput(){
		Scanner in = new Scanner(System.in);
		try {
         float result = in.nextInt();
			return result;
      }
      catch (Exception e)
      {
         System.out.println("Couldn't parse input, please try again");
			return -1f;
      }
	}

	public static void clearConsole(){
		for(int i = 0; i < 50; i++){
			System.out.printf("\n");
		}
	}

	public static String getCheckInput(String s, Boolean required,int maxlength){

		clearConsole();
		System.out.println("Please enter the following data:");

		Boolean valid = false; //flag to indicate if we can move to the next item
		String returnval = "";

		while(!valid){
			if(required) {System.out.printf("%s (required): ",s);}
			else {System.out.printf("%s: ",s);}
			String Input = getStringInput();

			//When speical case passwords
			String Input2 = "";
			if(s.equals("PASSWORD")){
				System.out.printf("CONFIRM PASSWORD (required): ");
				Input2 = getStringInput();
			}

			//Check to see if its a valid input
			if(s.equals("USERNAME") && sql_usernameExists(Input))
				{System.out.printf("Invalid, username already exsits");}
			else if(s.equals("PASSWORD") && !Input.equals(Input2))
				{System.out.println("Passwords to not match. Try again.");}
			else if( (Input.equals("") && required))
				{System.out.printf("Invalid %s. Must input something.\n",s);}
			else if(Input.length() > maxlength || (s.equals("USERNAME") && Input.length() > 9) ||  (s.equals("PASSWORD") && Input.length() > 36) )
				{System.out.printf("Invalid %s, too long. Try Again.\n",s);}
			else 
				{returnval = Input; valid = true;}
		}
		return returnval;
	}

	public static int getCheckInput2(String s,Boolean required, int min, int max){
		clearConsole();
		System.out.println("Please enter the following data:");

		Boolean valid = false; //flag to indicate if we can move to the next item
		int returnval = 0;

		while(!valid){
			if(required) {System.out.printf("%s (required): ",s);}
			else {System.out.printf("%s: ",s);}
			int Input = getIntInput();

			//Check to see if its a valid input
			if(Input  < min)
				{System.out.print("Invalid. Must input higher number");}
			else if(Input > max)
				{System.out.print("Invalid. Must input lower number");}
			else 
				{returnval = Input; valid = true;}
		}
		return returnval;
	}

	public static void search_by(int searchType){
		searchResults.clear();
		String Input = "";
		if(searchType == 0){ //Search by title
			currIsMovie = true;	
			System.out.printf("Enter title name: ");
			Input = getStringInput();
			sql_getTitle(Input);				
		}
		else if(searchType == 1){ //Search by directors
			currIsMovie = true;	
			System.out.printf("Enter director name: ");
			Input = getStringInput();
			sql_getDirector(Input);			
		}
		else if(searchType == 2){ //LIST ALL titles
			currIsMovie = true;
			sql_getAllTitles();				
		}
		else if(searchType == 3){ //Search by username
			currIsMovie = false;	
			System.out.printf("Enter username: ");
			Input = getStringInput();
			sql_getUsernames(Input);		
		}
		else if(searchType == 4){ //Search by email
			currIsMovie = false;
			System.out.printf("Enter Email: ");
			Input = getStringInput();
			sql_getEmails(Input);
		}
		else if(searchType == 5){ //LIST ALL USERS
			currIsMovie = false;
			sql_getAllUsers();
		}
		else if(searchType == 6){ //Search by genre
			currIsMovie = true;
			printAllGenres();	
			System.out.printf("\nSelect Genre: ");
			Input = getStringInput();
			sql_getGenre(Input);
		}
		searchedInput = Input;
	}
	
	public static Timestamp getTimeStamp(){
		return new Timestamp(Calendar.getInstance().getTime().getTime());

	}

	public static void writeComment(){
		System.out.printf("Wirte Comment:");
		String Input = getStringInput();
		if(Input.length() > 300){
			System.out.println("Invalid, Comment too long!");
		}
		else { //its valid
			sql_writeComment(Input);
			System.out.println("Comment Written!");
		}
	}

	public static void rateMovie(){
		Table t = runQuery("SELECT user_id FROM rate WHERE user_id = '" +USERNAME+ "'");
		if(t == null){
			System.out.printf("Give a rating between 0 - 10: ");
			int Input = getIntInput();
			if(Input < 0 || Input > 10){
				System.out.println("\nInvalid Rating.");
			}
			else{
				sql_rateMovie(Input);
				System.out.println("Rating Saved!");
			}
		}
		else
			{System.out.printf("You have already rated this movie.");}
		System.out.println("\nPress any Key to Continue");
		getStringInput();
	}

	public static void favoriteMovie(){
		System.out.printf("Favorite? Are you sure? (y/n)");
		String Input = getStringInput();
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			sql_favoriteMovie(currMovie);
			System.out.println("\nMovie Favorited");
		}
		else{System.out.println("\nCancled");}

		System.out.println("Press any Key to Continue");
		getStringInput();
	
	}

	public static void followUser(){
		System.out.printf("Follow? Are you sure? (y/n)");
		String Input = getStringInput();
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			sql_followUser(currUser);
			System.out.println("\nUser Followed");
		}
		else{System.out.println("\nCancled");}

		System.out.println("Press any Key to Continue");
		getStringInput();
	}

	public static Boolean deleteUser(String user_id){
		Boolean returnval = false;
		System.out.printf("DELETE USER? Are you sure? (y/n)");
		String Input = getStringInput();
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			sql_deleteUser(user_id);
			System.out.println("\nUser Deleted");
			returnval = true;
		}
		else{System.out.println("\nCancled");}

		System.out.println("Press any Key to Continue");
		getStringInput();
		return returnval;
	}

	public static void printAllGenres(){
		Table t = runQuery("SELECT genre_name FROM genre");
		String s = t.toString().substring(10);
		System.out.printf("\nAvailable Genres:");
		int lastindex = 0;
		while(lastindex != -1){
			if(lastindex != 0) System.out.printf(", ");
			lastindex++;
			System.out.printf(s.substring(lastindex,s.indexOf("\t",lastindex)));
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	

	//////////////////////////////////////////////
	/////////////SQL Functions////////////////
	//////////////////////////////////////////////

	public static void sql_getTitle(String Input){
		searchResults.clear();
		Table t = runQuery("SELECT video_id FROM video WHERE title LIKE '%" + Input + "%'");
		if(t == null) return;
		String s = t.toString().substring(9);
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			searchResults.add(s.substring(lastindex,s.indexOf("\t",lastindex)));
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	public static void sql_getDirector(String Input){
		Table t = runQuery("SELECT video_id FROM video");
		String s = t.toString().substring(9);
		searchResults.clear();
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			String id = s.substring(lastindex,s.indexOf("\t",lastindex));
			if(sql_getDirectors(id).indexOf(Input) != -1){searchResults.add(id);}
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	public static void sql_getGenre(String Input){
		Table t = runQuery("SELECT video_id FROM video");
		String s = t.toString().substring(9);
		searchResults.clear();
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			String id = s.substring(lastindex,s.indexOf("\t",lastindex));
			if(sql_getGenres(id).indexOf(Input) != -1){searchResults.add(id);}
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	public static void sql_getAllTitles(){
		Table t = runQuery("SELECT video_id FROM video");
		String s = t.toString().substring(9);
		searchResults.clear();
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			searchResults.add(s.substring(lastindex,s.indexOf("\t",lastindex)));
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	public static void sql_printSearchResults(){
		for(int i = 0; i < searchResults.size(); i++){
			
			//When its a movie search result, print the following
			if(currIsMovie){
				Table t = runQuery("SELECT * FROM video WHERE video_id =" + searchResults.get(i));
				String title = t.getInfoFromFirstTuple("title");
				String directors = sql_getDirectors(searchResults.get(i));
				System.out.println(Integer.toString(i+1) + ".\t" + title +", By: " + directors);
			}
			//When its a user search result, print the following
			else{
				Table t = runQuery("SELECT * FROM users WHERE user_id ='" + searchResults.get(i) +"'");
				String title = t.getInfoFromFirstTuple("user_id");
				String email = t.getInfoFromFirstTuple("e_mail");
				System.out.println(Integer.toString(i+1) + ".\t" +title +", " + email);

			}
		}
	}

	public static void sql_getUsernames(String Input){
		searchResults.clear();
		Table t = runQuery("SELECT user_id FROM users WHERE user_id LIKE '%" + Input + "%'");
		if(t == null) return;
		String s = t.toString().substring(8);
		
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			searchResults.add(s.substring(lastindex,s.indexOf("\t",lastindex)));
			lastindex = s.indexOf("\n",lastindex);
		}
	}


	public static void sql_getEmails(String Input){
		searchResults.clear();
		Table t = runQuery("SELECT user_id FROM users WHERE e_mail LIKE '%" + Input + "%'");
		if(t == null) return;
		String s = t.toString().substring(8);
		
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			searchResults.add(s.substring(lastindex,s.indexOf("\t",lastindex)));
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	public static void sql_getAllUsers(){

		//Add user_id into searchResults
		Table t = runQuery("SELECT user_id FROM users");
		String s = t.toString().substring(8);
		searchResults.clear();
		int lastindex = 0;
		while(lastindex != -1){
			lastindex++;
			searchResults.add(s.substring(lastindex,s.indexOf("\t",lastindex)));
			lastindex = s.indexOf("\n",lastindex);
		}
	}

	//TODO:: SQL QUERY print My wall
	public static void sql_printMyWall(){
		//Format: [time]: [username] did [activity]
		//example filler stuff
		for(int i = 0; i < 10; i++){
			String time = "time";
			String username = "username";
			String activity = "activity";
			System.out.println(time + ": " + username + " did " + activity);
		}
	}

	//TODO:: SQL QUERY print people i'm following
	public static void sql_printFollowing(){
		//Format: [username] [email]
		//example filler

	}

	public static void sql_writeComment(String comment){
		dbUpdate(
				"INSERT INTO comment " +
				"VALUES (DEFAULT,'" + USERNAME + "'," + currMovie + ",NOW(),'" + comment + "')");
		System.out.println("You have succesfully Commented on this movie!");
		System.out.println("Press any key to continue");
		getStringInput();
	}

	public static void sql_rateMovie(int rating){
		dbUpdate("INSERT INTO rate " +
			"VALUES ('"+ USERNAME + "'," + currMovie + ",NOW(),"+rating+")");
	}

	public static void sql_favoriteMovie(String movie_id){
		dbUpdate("INSERT INTO likes " +
			"VALUES ('"+ USERNAME + "'," + movie_id + ")");
	}

	//TODO:: SQL QUERY add user_id to followed user list
	public static void sql_followUser(String user_id){
	
	}

	//TODO:: SQL QUERY if username exsits in database
	public static Boolean sql_usernameExists(String s){
		
		Table t = runQuery("SELECT user_id FROM users WHERE user_id ='" +s+"'");
		return false; //return false if it doesnt exist
	}


	//TODO:: SQL QUERY delete user foreverand erver does not work atm
	public static void sql_deleteUser(String user_id){
		runQuery("DELETE FROM users WHERE user_id = '" + user_id +"'");
	}

	public static Boolean sql_isSuperUser(String id){
		Boolean returnval = false;
		Table t = runQuery("SELECT super_user_id FROM super_user WHERE super_user_id = '" + id +"'");
		if(t == null) {System.out.println("You're not a Super User!");}
		else {System.out.println("You are a Super User!"); returnval = true;};
		return returnval;
	}
	
	public static void sql_printBalance(){
		Table t = runQuery("SELECT balance FROM users WHERE user_id = '" + USERNAME + "'");
		String balance = t.getInfoFromColumn("balance").get(0);
		System.out.println("Current Balance: " + balance);
	}
	
	public static void sql_createComment(String video_id){
		System.out.println("Please type your comment (max 300 char):\n");
		String comment = getStringInput();
		
		while(comment.length() >= 300){
			System.out.println("Comment is too long Please try again:\n");
			comment = getStringInput();
		}
		
		dbUpdate(
				"INSERT INTO comment " +
				"VALUES (DEFAULT,'" + USERNAME + "'," + video_id + ",NOW(),'" + comment + "')");
		System.out.println("You have succesfully Commented on this movie!");
		System.out.println("Press any key to continue");
		getStringInput();
		
	}
	
	public static void sql_printComments(String video_id){
		Table t = runQuery(
				"SELECT user_id, comment_time, content " +
				"FROM comment " +
				"WHERE video_id = " + video_id + " " +
				"ORDER BY comment_time");
		System.out.println("\nCOMMENTS:");
		System.out.println("-------------------------------");
		if(t != null){
			for (ArrayList<String> i : t.getTuples()) {
				System.out.println(i.get(1) + "\n" + i.get(0) + " commented:\n\"" + i.get(2) +"\"\n\n");
			}
		}
		else{
			System.out.printf("No comments for this video.\nBe the first to comment!");
		}
		System.out.println("-------------------------------\n");
		System.out.println("Press any key to continue");
		getStringInput();
	}
	
	public static int sql_getBalance(){
		Table t = runQuery("SELECT balance FROM users WHERE user_id = '" + USERNAME + "'");
		String balance = t.getInfoFromColumn("balance").get(0);
		return Integer.valueOf(balance).intValue();
	}
	
	public static Boolean sql_placeOrder(String video_id){
		Table t = runQuery("SELECT * FROM video WHERE video_id =" + video_id);
		String dvd_price = t.getInfoFromFirstTuple("dvd_price");
		
		//Confirm payment
		Boolean returnval = false;
		System.out.println("This DVD costs $" + dvd_price + ". Are you sure you? (y/n)");
		sql_printBalance();
		String Input = getStringInput();
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			returnval = true;
			System.out.println("\nUser Followed");
		}
		else{System.out.println("\nCancled");}
			
		if(!returnval) return returnval;
		Integer price = Integer.valueOf(dvd_price);
		
		//Confirm suffecent funds
		if(price > sql_getBalance()){
			System.out.println("Insufficient funds! Add more to your balance through Settings.");
			return returnval;
		}
		else{
			sql_subtractFromBlance(price);
			dbUpdate("INSERT INTO orders VALUES (DEFAULT,"+ video_id + ",'" + USERNAME + "')");
			System.out.println("\nTransaction Complete!\n");
			return returnval;
		}
	}

	public static void sql_printUserInfo(String id){

		//Print the username
		Table t= runQuery("SELECT * FROM users WHERE user_id ='" + id + "'");
		String name = t.getInfoFromFirstTuple("user_id");
		System.out.println("Username: " + name);

		//Print first & last name
		String fname = t.getInfoFromFirstTuple("first_name");
		String lname = t.getInfoFromFirstTuple("last_name");
		System.out.println("Name: "+fname + " " + lname);

		//Print the email
		String email = t.getInfoFromFirstTuple("e_mail");
		System.out.println("E-Mail: " + email);

		//Print their favorited movies:
		System.out.printf("Favorites: ");
		Table t2 = runQuery("SELECT V.title FROM video V, likes L WHERE V.video_id = L.video_id AND L.user_id = '" + currUser +"'");
		if(t2 != null){
			String s = t2.toString().substring(9);
			int lastindex = 0;
				while(lastindex != -1){
				if(lastindex != 0){System.out.printf(", ");}
				lastindex++;
				System.out.printf(s.substring(lastindex,s.indexOf("\t",lastindex) ) );
				lastindex = s.indexOf("\n",lastindex);
			}
		}
		else{System.out.printf(" None");}

		System.out.println("\n==============================================\n");
	}

	
	//TODO: FINISH THESE
	public static void sql_printMovieInfo(String video_id){
		Table t = runQuery(
				"SELECT * FROM video " +
				"WHERE video_id =" + video_id);
		
		Table d = runQuery(
				"SELECT D.first_name, D.last_name " +
				"FROM director D, directed D2, video V " +
				"WHERE V.video_id =" + video_id + "AND V.video_id = D2.video_id AND D.director_id = D2.director_id");
		
		Table a2 = runQuery(
				"SELECT A.first_name, A.last_name " +
				"FROM author A, written W, video V " +
				"WHERE V.video_id =" + video_id + "AND V.video_id = W.video_id AND A.author_id = W.author_id");
		
		Table a3 = runQuery(
				"SELECT S.first_name, S.last_name " +
				"FROM star S, played P, video V " +
				"WHERE V.video_id =" + video_id + "AND V.video_id = P.video_id AND S.star_id = P.star_id");
		
		Table r = runQuery(
				"SELECT rating " +
				"FROM rate " +
				"WHERE user_id ='" + USERNAME + "' AND video_id =" + video_id);
		
		Table a = runQuery(
				"SELECT AVG(rating) AS average " +
				"FROM rate " +
				"WHERE video_id =" + video_id);
		
		Table g = runQuery(
				"SELECT G.genre_name AS genre " +
				"FROM categorize C, genre G " +
				"WHERE C.video_id =" + video_id + " AND C.genre_id = G.genre_id");
		
		Table s = runQuery(
				"SELECT S.season_number, S2.title " +
				"FROM season S, series S2, video V1 " +
				"WHERE V1.video_id =" + video_id + " AND V1.season_id = S.season_id AND S2.series_id = S.series_id");
		
		
		String title = t.getInfoFromFirstTuple("title");
		String year = t.getInfoFromFirstTuple("year");
		
		//Get List of directors
				String director = null;
				if(d != null){
					ArrayList<String> fname = d.getInfoFromColumn("first_name");
					ArrayList<String> lname = d.getInfoFromColumn("last_name");
					for (int i = 0; i < fname.size(); i++){
						if (director == null){director = String.format(fname.get(i) + " " + lname.get(i));}
						else{director = director + String.format(", " + fname.get(i) + " " + lname.get(i));}
					}
				}
				if (director == null){director = "Unknown";}
	
		//Get List of authors
		String author = null;
		if(a2 != null){
			ArrayList<String> fname2 = a2.getInfoFromColumn("first_name");
			ArrayList<String> lname2 = a2.getInfoFromColumn("last_name");
			for (int i = 0; i < fname2.size(); i++){
				if (author == null){author = String.format(fname2.get(i) + " " + lname2.get(i));}
				else{author = author + String.format(", " + fname2.get(i) + " " + lname2.get(i));}
			}
		}
		if (author == null){author = "Unknown";}
		
		//Get List of authors
		String actors = null;
		if(a3 != null){
			ArrayList<String> fname3 = a3.getInfoFromColumn("first_name");
			ArrayList<String> lname3 = a3.getInfoFromColumn("last_name");
			for (int i = 0; i < fname3.size(); i++){
				if (actors == null){actors = String.format(fname3.get(i) + " " + lname3.get(i));}
				else{actors = actors + String.format(", " + fname3.get(i) + " " + lname3.get(i));}
			}
		}
		if (actors == null){actors = "Unknown";}
		
		//Get List of genres
		String genre = null;
		if(g != null){
			ArrayList<String> genres = g.getInfoFromColumn("genre");
			for (String string : genres) {
				if (genre == null){genre = string;}
				else{genre = genre + ", " + string;}
			}
		}
		if (genre == null){genre = "Unclassified";}
		
		String views = "# of views";
		
		//Get Average Rating
		String avgRating = a.getInfoFromFirstTuple("average");
		if (avgRating == null) {avgRating = "(Not Yet Rated By Anyone)";}
		
		//Get USERS Rating
		String yourRating = "(You Have Not Yet Rated)";
		if (r != null) {
			yourRating = r.getInfoFromFirstTuple("rating");
			if (yourRating == null) {yourRating = "(Not Yet Rated)";}
		}
		
		String episodeNum = t.getInfoFromFirstTuple("episode");; //Can avoid if not tv series
		
		String seriesTitle = null;
		String seasonNum = null;
		if(s != null){
			seriesTitle = s.getInfoFromFirstTuple("title");
			seasonNum = s.getInfoFromFirstTuple("season_number"); //Can avoid if not tv series
		}
		
		String online_price = t.getInfoFromFirstTuple("online_price");
		String dvd_price = t.getInfoFromFirstTuple("dvd_price");
		
		System.out.println("Title: " + title);
		System.out.println("Year: " + year);
		System.out.println("Director: " + director);
		System.out.println("Author: " + author);
		System.out.println("Actors: " + actors);
		System.out.println("Genre: " + genre);
		
		if (seriesTitle != null) {
			System.out.println("Series Title: " + seriesTitle);
		}
		
		if (seasonNum != null) {
			System.out.println("Season Number: " + seasonNum);
		}
		
		if (episodeNum != null) {
			System.out.println("Episode Number: " + episodeNum);
		}
		
		System.out.println("Views: " + views);
		System.out.println("Average Rating: " + avgRating);
		System.out.println("Your Rating: " + yourRating);
		System.out.println("Online Price: $" + online_price);
		System.out.println("DVD Price: $" + dvd_price);
		System.out.println("================================");
		
	}

	public static String sql_getDirectors(String video_id){
		String director = null;
		Table d = runQuery(
			"SELECT D.first_name, D.last_name " +
			"FROM director D, directed D2, video V " +
			"WHERE V.video_id =" + video_id + "AND V.video_id = D2.video_id AND D.director_id = D2.director_id");

		if(d != null){
			ArrayList<String> fname = d.getInfoFromColumn("first_name");
			ArrayList<String> lname = d.getInfoFromColumn("last_name");
			for (int i = 0; i < fname.size(); i++){
				if(i != 0) director += ", ";
				if (director == null){director = String.format(fname.get(i) + " " + lname.get(i));}
				else{director = director + String.format(fname.get(i) + " " + lname.get(i));}	
			}
		}
		if (director == null){director = "Unknown";}
		return director;
	}

	public static String sql_getGenres(String video_id){
		String genre = null;
		Table g = runQuery(
			"SELECT G.genre_name AS genre " +
			"FROM categorize C, genre G " +
			"WHERE C.video_id =" + video_id + " AND C.genre_id = G.genre_id");
		if(g != null){
			ArrayList<String> genres = g.getInfoFromColumn("genre");
			for (String string : genres) {
				if (genre == null){genre = string;}
				else{genre = genre + ", " + string;}
			}
		}
		if (genre == null){genre = "Unclassified";}
		return genre;

	}
	
	public static void sql_increaseBalance(){
		System.out.println("How much would you like to add?");
		int amount = getIntInput();
		Table t = runQuery("SELECT balance FROM users WHERE user_id = '" + USERNAME + "'");
		String balance = t.getInfoFromColumn("balance").get(0);
		Integer bal = Integer.valueOf(balance) + amount;
		String balance2 = bal.toString();
		System.out.print("New Balance: " + balance2);
		dbUpdate("UPDATE users SET balance=" + balance2 + " WHERE user_id = '" + USERNAME + "'");
	}
	
	public static void sql_subtractFromBlance(int amount){
		String newBalance = new Integer(sql_getBalance() - amount).toString();
		dbUpdate("UPDATE users SET balance=" + newBalance + " WHERE user_id = '" + USERNAME + "'");
		
	}
	
	public static Table runQuery(String query){
		Table t = null;
		try {
			t = db.executeQuery(query);
		} catch (Exception e) {
			System.out.println(e.toString());
			return t;
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
	
	public static String getFirstMovie(){
		Table t = runQuery("SELECT video_id FROM video");
		String id = t.getInfoFromColumn("video_id").get(0);
		System.out.print("Obtained: " + id);
		return id;
	}

	public static String getFirstUser(){
		Table t = runQuery("SELECT user_id FROM users");
		String id = t.getInfoFromColumn("user_id").get(0);
		System.out.print("Obtained: " + id);
		return id;
	}
	
	public static void printMovie(){
		System.out.println("@@@@@@@@@@@@@@@@");
		System.out.println("@      \\    /\\");
		System.out.println("@       )  ( ')");
		System.out.println("@      (  /  )");
		System.out.println("@ jgs   \\(__)|");
		System.out.println("@@@@@@@@@@@@@@@@");
	}
}

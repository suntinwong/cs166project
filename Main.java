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
	
	enum Page {LOGIN, CREATE_NEW, MAIN_MENU, MOVIE_SEARCH, MOVIE_INFO, FOLLOWING, FOLLOWING_EDIT, SETTINGS, SEARCH_RESULTS,USER_SEARCH,WATCH_ONLINE,USER_INFO,MOVIE_COMMENTS};
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
	static ArrayList<String> commentList = new ArrayList<String>();
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
			
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("Welcome to MovieNet!");
				System.out.println("\n\n-=LOGIN MENU=-");
				System.out.println("1.\tLOGIN");
				System.out.println("2.\tCREATE NEW");
				System.out.println("0.\tEXIT");
				System.out.printf("\n Select one of the above options:");
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
					getStringInput();
					break;
				}
			}
			break;
		
		case CREATE_NEW:
			createNewAccount();
			break;
			
		//MAIN MENU
		case MAIN_MENU:		
			
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("-=MAIN MENU=-");
				System.out.println("1.\tFIND A MOVIE");
				System.out.println("2.\tFIND A USER");
				System.out.println("3.\tVIEW MY WALL");
				System.out.println("4.\tEDIT SETTINGS");
				if(isSuperUser){
					System.out.println("5.\tADD NEW MOVIE [SuperUser] ");
					System.out.println("6.\tADD NEW DIRECTOR [SuperUser] ");
					System.out.println("7.\tADD NEW AUTHOR [SuperUser] ");
					System.out.println("8.\tADD NEW ACTOR [SuperUser] ");
				}
				System.out.println("0.\tLOGOUT");
				System.out.printf("\n Select one of the above options:");
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
					
				case 6:
					if(isSuperUser){addNewDirector();}
					break;
				
				case 7:
					if(isSuperUser){addNewAuthor();}
					break;
					
				case 8:
					if(isSuperUser){addNewActor();}
					break;
				
					
				case 0:
					clearConsole();
					System.out.println("Sucessfully logged out!");
					System.out.println("Press any key to continue");
					getStringInput();
					page = Page.LOGIN;
					lockRepeat = false;
					break;
					
				default:
					System.out.println("Invalid input. Please try again.");
					getStringInput();
					break;
				}
			}
			break;
		
		//MOVE SEARCH PAGE
		case MOVIE_SEARCH:
			
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("-=MOVIE SEARCH=-");
				System.out.println("1.\tSEARCH BY TITLE");
				System.out.println("2.\tSEARCH BY DIRECTOR");
				System.out.println("3.\tSEARCH BY GENRE");
				System.out.println("4.\tLIST ALL MOVIE TITLES");
				System.out.println("0.\tBACK TO MAIN MENU");
				System.out.printf("\n Select one of the above options:");
				
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
					getStringInput();
					break;
				}
			}
				
				
			break;

		//USER SEARCH PAGE
		case USER_SEARCH:
						
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("=USER SEARCH=-");
				System.out.println("1.\tSEARCH BY USERNAME");
				System.out.println("2.\tSEARCH BY EMAIL");
				System.out.println("3.\tLIST ALL USERS");
				System.out.println("0.\tBACK TO MAIN MENU");
				System.out.printf("\n Select one of the above options:");
				
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
					getStringInput();
					break;
				}
			}
				
				
			break;

		//Movie Search Results
		case SEARCH_RESULTS:
			
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("== Results for '" +searchedInput+ "' ==-");
				sql_printSearchResults();
				if(searchResults.size() == 0)
					{System.out.println("No searches found");}

				System.out.println("0.\tBACK TO MAIN MENU");
				System.out.printf("\n Choose which TO VIEW:");

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
					getStringInput();
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
				System.out.println("1.\tORDER DVD");
				System.out.println("2.\tWATCH ONLINE");
				System.out.println("3.\tVIEW COMMENTS");
				System.out.println("4.\tWRITE A COMMENT");
				System.out.println("5.\tRATE");
				System.out.println("6.\tFAVORITE");
				System.out.println("0.\tBACK TO SEARCH RESULTS");
				System.out.printf("\n Select one of the above options:");
				
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
					page = Page.MOVIE_COMMENTS;
					lockRepeat = false;
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
					getStringInput();
					break;
				}
			}
			break;

		//Comments for specific movie
		case MOVIE_COMMENTS:
		
		lockRepeat = true;
			while(lockRepeat){
				clearConsole();
				System.out.println("=Video Comments=-");
				sql_printComments(currMovie);
				System.out.println("0.\tBACK TO MAIN MENU");
				if(isSuperUser){
					for(int i = 0; i < commentList.size(); i++)
						{System.out.println(Integer.toString(i+1) + ".\tDELETE COMMENT #" + Integer.toString(i+1)+" [SuperUser]");}
				}
				System.out.printf("\n Select one of the above options:");
				int input = getIntInput();
				
					if(input == 0){
						page = Page.MOVIE_INFO;
						lockRepeat = false;
					}
					else if(isSuperUser && input-1 < commentList.size()){
						sql_deleteComment(commentList.get(input-1));
					}

					else{
						System.out.println("Invalid input. Please try again.");
						getStringInput();
					}
				
			}
			break;

		//Searched user's info
		case USER_INFO:
			
			lockRepeat = true;
			while(lockRepeat){
				clearConsole();
				System.out.println("=USER's INFO=");
				sql_printUserInfo(currUser);
				System.out.println("0.\tBACK TO SEARCH RESULTS");
				if(!isfollowing()){System.out.println("1.\tFOLLOW USER");}
				else {System.out.println("1.\tUN-FOLLOW USER");}
				System.out.println("2.\tVIEW USER's HISTORY");
				if(isSuperUser){System.out.println("3.\tDELETE USER [SuperUser]");}
				System.out.printf(" Select one of the above options:");
				int input = getIntInput();
				switch (input) {
					case 0:
						page = Page.SEARCH_RESULTS;
						lockRepeat = false;
						break;

					case 1:
						if(!isfollowing()){followUser();}
						else {unfollowUser(currUser);}
						break;

					case 2:
						clearConsole();						
						if(!sql_printUpdates(currUser))
							{System.out.println("\nNo recet updates for this user!\n");}
						System.out.println("\nPress any key to continue");
						getStringInput();
						break;
				
					case 3:
						if(isSuperUser){
							clearConsole();
							if(deleteUser(currUser)){lockRepeat = false;}
						}
						break;

					default:
						System.out.println("Invalid input. Please try again.");
						getStringInput();
						break;
				}
			}

		break;
		
		//USER'S FOLLOWING WALL OF INFO
		case FOLLOWING:
			
			lockRepeat = true;
			while(lockRepeat){
				clearConsole();
				System.out.println("=FOLLOWING WALL=-");
				sql_printMyWall();
				System.out.println("===================================");
				System.out.println("1.\tEDIT PEOPLE I AM FOLLOWING");
				System.out.println("0.\tBACK TO MAIN MENU");
				System.out.printf("\n Select one of the above options:");
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
						getStringInput();
						break;
				}
			}
			break;
			
		//ADD/REMOVE THE ACCOUTN USER IS FOLLOWING	
		case FOLLOWING_EDIT:
			
			lockRepeat = true;
			while(lockRepeat){
				clearConsole();
				System.out.println("=EDIT PEOPLE I AM FOLLOWING=-");
				sql_printFollowing();
				System.out.println("0.\tBACK TO MY WALL");
				System.out.printf("\nSelect whom to stop following:");
				int input = getIntInput();
				if(input == 0){
					page = Page.FOLLOWING;
					lockRepeat = false;
				}
				else if(input -1 < searchResults.size()){
					unfollowUser(searchResults.get(input-1));
				}

				else{
					System.out.println("Invalid input. Please try again.");
					getStringInput();
				}
			}
			break;

		case WATCH_ONLINE:
		
			lockRepeat = true;
			while(lockRepeat){
				clearConsole();
				System.out.println("-=WATCHING ONLINE=-");
				printMovie();
				System.out.printf("\n\n Enter 0 to go back");
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
			
			
			
			lockRepeat = true;
			while (lockRepeat){
				clearConsole();
				System.out.println("-=SETTINGS=-");
				System.out.println("1.\tCHECK BALANCE");
				System.out.println("2.\tINCREASE BALANCE");
				System.out.println("3.\tMAKE ACCOUNT PRIVATE");
				System.out.println("4.\tMAKE ACCOUNT PUBLIC");
				System.out.println("5.\tDELETE MY ACCOUNT");
				System.out.println("0.\tBACK");
				System.out.printf("\n Select one of the above options:");
				
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
					
				case 5:
					if(deleteUser(USERNAME)){lockRepeat = false;}
					break;
					
				case 0:
					page = Page.MAIN_MENU;
					lockRepeat = false;
					break;
					
				default:
					System.out.println("Invalid input. Please try again.");
					getStringInput();
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
			clearConsole();
			System.out.println("\nLogin SUCCESS!");
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
		String preferencesInput = getGenreInput(true);
		
		if(dbLoaded){
			dbUpdate(
					"INSERT INTO users " +
					"VALUES ('" + usernameInput + "','" + passwordInput + "','" + firstInput + "','" + middleInput + "','" + lastInput + "','" + emailInput + "','" + street1Input + "','" + street2Input + "','" + stateInput + "','" + countryInput + "','" + zipInput + "', 0)");
		}
		
		//todo finish prefers
		/*while(){
			String preferencesInput = getGenreInput(true);
			if(!preferencesInput.equals("-1")){
				String genre_id = 
				dbUpdate("INSERT INTO prefers VALUES ('" + usernameInput+"',"+genre_id+")");
			}
		}*/
		
		System.out.println("\nAccount Created!");
		System.out.println("Returning to the login page...");
		System.out.println("Press any key to continue");
		getStringInput();
		page = Page.LOGIN;
	}

	public static void addNewMovie(){
		String titleInput = getCheckInput("Title",true,50);
		int yearInput = getCheckInput2("Year",true,1500,3000);
		String director = getCheckInput("Director",true,40);
		ArrayList<String> authors = getCheckInputArray("Author",true,40);
		ArrayList<String> actors = getCheckInputArray("Actor",true,40);
		String genre = getGenreInput(false);
		int online_priceInput = getCheckInput2("Online Price",true,0,9999999);
		int dvd_priceInput = getCheckInput2("DVD Price",true,0,999999);
		int votesInput = 0;
		int ratingInput = 0;	
		String seriesInput = null;
		String episodeInput = null;
		int seasonInput = 0;
			
		System.out.println("TV Show? (y/n)");
		String ismovie =  getStringInput();
		Boolean isTVSHOW = false;
		if(ismovie.substring(0,1).equals("y") || ismovie.substring(0,1).equals("Y"))
			{isTVSHOW = true;}
		
		if(isTVSHOW){
			seriesInput = getCheckInput("Series",true,50);
			seasonInput = getCheckInput2("Season #",true,0,999); 
			episodeInput = getCheckInput("Episode #",true,9);
		}
		
		//get video number
		Table t = runQuery("SELECT COUNT(*) FROM video");
		String vid_id = Integer.toString((Integer.parseInt(t.getInfoFromFirstTuple("count"))+1));

		//Series
		Table t5 = runQuery("SELECT series_id FROM series WHERE title ='" + seriesInput + "'");
		String series_id = null;
		if(t5 == null){
			Table temp = runQuery("SELECT COUNT(*) FROM series");
			series_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
			dbUpdate("INSERT INTO series VALUES ("+series_id +",'"+seriesInput+"')");
		}
		else	{series_id = t5.getInfoFromFirstTuple("series_id");}
		
		//season
		Table t6 = runQuery("SELECT season_id FROM season WHERE series_id ='"+series_id + "'");
		String season_id = null;
		if(t6 == null){
			Table temp = runQuery("SELECT COUNT(*) FROM season");
			season_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
			dbUpdate("INSERT INTO season VALUES ("+season_id +","+ series_id +",'"+Integer.toString(seasonInput)+"')");
		}
		else	{season_id = t6.getInfoFromFirstTuple("season_id");}
	
		//video table
		dbUpdate("INSERT INTO video VALUES "+
				"('" +vid_id+ "','" + titleInput + "'," + yearInput + "," + online_priceInput + "," + dvd_priceInput + ","
				+votesInput+"," +ratingInput + ",'" +episodeInput+ "'," +season_id + ")");
		
		//Genre
		Table t4 = runQuery("SELECT genre_id FROM genre WHERE genre_name ='"+genre+"'");
		String genre_id = t4.getInfoFromFirstTuple("genre_id");
		dbUpdate("INSERT INTO categorize VALUES ("+vid_id+","+genre_id+")");
		
		//director&directed
		String d_lname = director.substring(0,director.indexOf(","));
		String d_fname = director.substring(director.indexOf(",")+1);
		Table t2 = runQuery("SELECT * FROM director WHERE first_name = '"+ d_fname + "' AND last_name ='" +d_lname+"'");
		String d_id = null;
		if(t2 == null){
			Table temp = runQuery("SELECT COUNT(*) FROM director");
			d_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
			dbUpdate("INSERT INTO director VALUES ("+d_id +",'"+d_fname+"','"+d_lname+"')");
		}
		else	{d_id = t2.getInfoFromFirstTuple("director_id");}
		dbUpdate("INSERT INTO directed VALUES ("+vid_id+","+d_id+")");

		//authors
		for(int i = 0; i < authors.size(); i++){
			String a_lname = authors.get(i).substring(0,authors.get(i).indexOf(","));
			String a_fname = authors.get(i).substring(authors.get(i).indexOf(",")+1);
			Table t3 = runQuery("SELECT * FROM author WHERE first_name = '"+ a_fname + "' AND last_name ='" +a_lname+"'");
			String a_id = null;
			if(t3 == null){
				Table temp = runQuery("SELECT COUNT(*) FROM author");
				a_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
				dbUpdate("INSERT INTO author VALUES ("+a_id +",'"+a_fname+"','"+a_lname+"')");
			}
			else	{a_id = t3.getInfoFromFirstTuple("author_id");}
			dbUpdate("INSERT INTO written VALUES ("+vid_id+","+a_id+")");
		}

		//actors
		for(int i = 0; i < actors.size(); i++){
			String a_lname = actors.get(i).substring(0,actors.get(i).indexOf(","));
			String a_fname = actors.get(i).substring(actors.get(i).indexOf(",")+1);
			Table t3 = runQuery("SELECT * FROM star WHERE first_name = '"+ a_fname + "' AND last_name ='" +a_lname+"'");
			String a_id = null;
			if(t3 == null){
				Table temp = runQuery("SELECT COUNT(*) FROM star");
				a_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
				dbUpdate("INSERT INTO star VALUES ("+a_id +",'"+a_fname+"','"+a_lname+"')");
			}
			else	{a_id = t3.getInfoFromFirstTuple("star_id");}
			dbUpdate("INSERT INTO played VALUES ("+vid_id+","+a_id+")");
		}

		
	
		System.out.println("\nMoive Created!");
		System.out.println("Returning to Main Menu...");
		System.out.println("Press any key to continue");
		getStringInput();
		page = Page.MAIN_MENU;
	}
	
	public static void addNewDirector(){
		String inputs = getCheckInput("Director",true,40);
		String a_lname = inputs.substring(0,inputs.indexOf(","));
		String a_fname = inputs.substring(inputs.indexOf(",")+1);
		Table t3 = runQuery("SELECT * FROM director WHERE first_name = '"+ a_fname + "' AND last_name ='" +a_lname+"'");
		String a_id = null;
		if(t3 == null){
			Table temp = runQuery("SELECT COUNT(*) FROM director");
			a_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
			dbUpdate("INSERT INTO director VALUES ("+a_id +",'"+a_fname+"','"+a_lname+"')");
			System.out.println("\nDirector Created!");
		}
		else
			{System.out.println(a_fname + " " + a_lname + " Already exists!");}
		
		
		System.out.println("Returning to Main Menu...");
		System.out.println("Press any key to continue");
		getStringInput();
		page = Page.MAIN_MENU;
		
	}
	
	public static void addNewActor(){
		String inputs = getCheckInput("Actor",true,40);
		String a_lname = inputs.substring(0,inputs.indexOf(","));
		String a_fname = inputs.substring(inputs.indexOf(",")+1);
		Table t3 = runQuery("SELECT * FROM star WHERE first_name = '"+ a_fname + "' AND last_name ='" +a_lname+"'");
		String a_id = null;
		if(t3 == null){
			Table temp = runQuery("SELECT COUNT(*) FROM star");
			a_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
			dbUpdate("INSERT INTO star VALUES ("+a_id +",'"+a_fname+"','"+a_lname+"')");
			System.out.println("\nActor Created!");
		}
		else
			{System.out.println(a_fname + " " + a_lname + " Already exists!");}
		
		
		System.out.println("Returning to Main Menu...");
		System.out.println("Press any key to continue");
		getStringInput();
		page = Page.MAIN_MENU;
		
	}
	
	public static void addNewAuthor(){
		String inputs = getCheckInput("Author",true,40);
		String a_lname = inputs.substring(0,inputs.indexOf(","));
		String a_fname = inputs.substring(inputs.indexOf(",")+1);
		Table t3 = runQuery("SELECT * FROM author WHERE first_name = '"+ a_fname + "' AND last_name ='" +a_lname+"'");
		String a_id = null;
		if(t3 == null){
		Table temp = runQuery("SELECT COUNT(*) FROM author");
			a_id = Integer.toString((Integer.parseInt(temp.getInfoFromFirstTuple("count"))+1));
			dbUpdate("INSERT INTO author VALUES ("+a_id +",'"+a_fname+"','"+a_lname+"')");
			System.out.println("\nAuthor Created!");
		}
		else
			{System.out.println(a_fname + " " + a_lname + " Already exists!");}
		
		
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
			if(required) {System.out.printf("(Required),");}
			if(s.equals("Director")||s.equals("Author") ||s.equals("Actor") )
				{System.out.printf("(lastname,firstname),");}
			System.out.printf("(Max characters: %d)\n%s: ",maxlength,s);
			String Input = getStringInput();

			//When speical case passwords
			String Input2 = "";
			if(s.equals("PASSWORD")){
				System.out.printf("CONFIRM PASSWORD: ");
				Input2 = getStringInput();
			}

			//Check to see if its a valid input
			if(s.equals("USERNAME") && sql_usernameExists(Input))
				{System.out.printf("Invalid, username already exsits");}
			else if(s.equals("PASSWORD") && !Input.equals(Input2))
				{System.out.println("Passwords to not match. Try again.");}
			else if( (s.equals("Director") || s.equals("Author") || s.equals("Actor") ) && Input.indexOf(",") ==-1 )
				{System.out.println("Invalid, must follow format: lastname,firstname");}
			else if( (Input.equals("") && required))
				{System.out.printf("Invalid. Must input something.\n");}
			else if(Input.length() > maxlength || (s.equals("USERNAME") && Input.length() > 9) ||  (s.equals("PASSWORD") && Input.length() > 36) )
				{System.out.printf("Invalid. Too long. Try Again.\n");}
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
			if(required) {System.out.printf("(Required),");}
			System.out.printf("(Number between %d to %d)\n%s: ",min,max,s);
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

	public static ArrayList<String> getCheckInputArray(String s, Boolean required, int maxchar){
		ArrayList<String> list = new ArrayList<String>();
		String input = "y";
		while(input.equals("Y") || input.equals("y")){
			list.add(getCheckInput(s,required,maxchar));
			System.out.printf("Add another %s? (y/n):",s);
			input = getStringInput();
		}
		return list;
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
			System.out.printf("\n Select Genre:");
			Input = getGenreInput(false);
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
		Table t = runQuery("SELECT user_id FROM rate WHERE user_id = '" +USERNAME+ "' AND video_id =" +currMovie );
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
		System.out.printf("Press any Key to Continue");
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

	public static boolean isfollowing(){
		Table t = runQuery("SELECT user_id_to FROM follow WHERE user_id_to ='"+currUser+"' AND  user_id_from ='"+USERNAME+"'" );
		if(t == null){return false;}
		else {return true;}
	}

	public static void followUser(){
		if(currUser.equals(USERNAME)){
			System.out.println("Cannot follow yourself!");
		} 
		else{
			System.out.printf("Follow? Are you sure? (y/n)");
			String Input = getStringInput();
			if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
				sql_followUser(currUser);
				System.out.println("\nUser Followed");
			}
			else{System.out.println("\nCancled");}
		}

		System.out.println("Press any Key to Continue");
		getStringInput();
	}

	public static void unfollowUser(String id){
		
		System.out.printf("Un-Follow? Are you sure? (y/n)");
		String Input = getStringInput();
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			sql_unfollowUser(id);
			System.out.println("\nUser Un-followed");
		}
		else{System.out.println("\nCancled");}
		

		System.out.println("Press any Key to Continue");
		getStringInput();
	}

	public static Boolean deleteUser(String user_id){
		Boolean returnval = false;
		System.out.println("WARNING: THIS WILL DELETE ANYTING RELEATED TO USER!!!");
		if(user_id.equals(USERNAME)){System.out.println("NOTE: You are deleting yourself.");}
		System.out.printf("DELETE USER? Are you sure? (y/n)");
		String Input = getStringInput();
		
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			sql_deleteUser(user_id);
			System.out.println("\nUser Deleted");
			returnval = true;
			page = Page.USER_SEARCH;
			if(user_id.equals(USERNAME)){page = Page.LOGIN;}
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

	public static String getGenreInput(Boolean preferred){
		String s = "";
		Boolean valid = false;
		while(!valid){
			clearConsole();
			printAllGenres();
			System.out.printf("\n\n Select one of the above genres: ");			
			String input = getStringInput();
			Table t = runQuery("SELECT genre_name FROM genre WHERE genre_name ='"+input+"'");
			if(t!= null && preferred && input.equals(""))
				{valid = true; s = "-1";}
			else if(t != null)
				{valid = true; s = input;}
			else 
				{System.out.printf("\nIncorrect genre selection, try again"); getStringInput();}
		}
		return s;
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

	public static Boolean sql_printUpdates(String user_id){

		int returncount = 0;
		//print comments
		Table t = runQuery("SELECT content,comment_time,video_id FROM comment WHERE user_id ='"+user_id+"'" );
		if(t != null){
			returncount++;
			ArrayList<String> contents = t.getInfoFromColumn("content");
			ArrayList<String> times = t.getInfoFromColumn("comment_time");
			ArrayList<String> video_ids = t.getInfoFromColumn("video_id");
			System.out.println("-----------------------------------");
			for (int i = 0; i < times.size(); i++){
				Table temp = runQuery("SELECT title FROM video WHERE video_id = " + video_ids.get(i));		
				String video_name = temp.getInfoFromFirstTuple("title");
				System.out.println(times.get(i));
				System.out.println(user_id + " commented on " + video_name + ":");
				System.out.println(contents.get(i));
				System.out.println("-----------------------------------");
			}
		}

		//print rating
		Table t2 = runQuery("SELECT rating,rate_time,video_id FROM rate WHERE user_id ='"+user_id+"'" );
		if(t2 != null){
			returncount++;
			ArrayList<String> contents = t2.getInfoFromColumn("rating");
			ArrayList<String> times = t2.getInfoFromColumn("rate_time");
			ArrayList<String> video_ids = t2.getInfoFromColumn("video_id");
			System.out.println("-----------------------------------");
			for (int i = 0; i < times.size(); i++){
				Table temp = runQuery("SELECT title FROM video WHERE video_id = " + video_ids.get(i));		
				String video_name = temp.getInfoFromFirstTuple("title");
				System.out.println(times.get(i));
				System.out.println(user_id + " rated " + video_name + ":" + contents.get(i) + "/10");
				System.out.println("-----------------------------------");
			}
		}

		//print ordering
		Table t3 = runQuery("SELECT order_time,video_id FROM orders WHERE user_id ='"+user_id+"'" );
		if(t3 != null){
			returncount++;
			ArrayList<String> times = t3.getInfoFromColumn("order_time");
			ArrayList<String> video_ids = t3.getInfoFromColumn("video_id");
			System.out.println("-----------------------------------");
			for (int i = 0; i < times.size(); i++){
				Table temp = runQuery("SELECT title FROM video WHERE video_id = " + video_ids.get(i));		
				String video_name = temp.getInfoFromFirstTuple("title");
				System.out.println(times.get(i));
				System.out.println(user_id + " ordered/watched: " + video_name);
				System.out.println("-----------------------------------");
			}
		}
		
		//print favoriting
		Table t4 = runQuery("SELECT like_time,video_id FROM likes WHERE user_id ='"+user_id+"'" );
		if(t3 != null){
			returncount++;
			ArrayList<String> times = t4.getInfoFromColumn("like_time");
			ArrayList<String> video_ids = t4.getInfoFromColumn("video_id");
			System.out.println("-----------------------------------");
			for (int i = 0; i < times.size(); i++){
				Table temp = runQuery("SELECT title FROM video WHERE video_id = " + video_ids.get(i));		
				String video_name = temp.getInfoFromFirstTuple("title");
				System.out.println(times.get(i));
				System.out.println(user_id + " favorited: " + video_name);
				System.out.println("-----------------------------------");
			}
		}
		if(returncount == 0) return false;
		else return true;

	}

	//SQL QUERY print My wall
	public static void sql_printMyWall(){
		Table t = runQuery("SELECT user_id_to FROM follow WHERE user_id_from  ='"+USERNAME+"'" );
		int printcount = 0;
		if(t != null){
			ArrayList<String> followed_users = t.getInfoFromColumn("user_id_to");
			for(int i = 0; i < followed_users.size(); i++){
				if(sql_printUpdates(followed_users.get(i))){
					printcount++;
				}				
			}
		}
		if(printcount == 0){System.out.println("\nNo updates on your wall!\n");}

	}

	public static void sql_printFollowing(){
		Table t = runQuery("SELECT user_id_to FROM follow WHERE user_id_from ='"+USERNAME+"'");
		searchResults.clear();
		if(t == null)
			{System.out.println("You are not following anyone!");}
			
		else{
			String s = t.toString().substring(11);
			int lastindex = 0;
			while(lastindex != -1){
				lastindex++;
				searchResults.add(s.substring(lastindex,s.indexOf("\t",lastindex)));
				lastindex = s.indexOf("\n",lastindex);
			}
			for(int i = 0; i < searchResults.size(); i++)
				{System.out.println(Integer.toString(i+1) + ".\t"+ searchResults.get(i));}
			
		}
	}

	public static void sql_writeComment(String comment){
		dbUpdate(
				"INSERT INTO comment " +
				"VALUES (DEFAULT,'" + USERNAME + "'," + currMovie + ",NOW(),'" + comment + "')");
		System.out.println("You have succesfully Commented on this movie!");
		System.out.println("Press any key to continue");
		getStringInput();
	}
	
	public static void sql_deleteComment(String comment_id){
		System.out.printf("Delete Comment? Are you sure? (y/n):");
		String input = getStringInput();
		if(input.equals("y") || input.equals("Y")){
			runQuery("DELETE FROM comment WHERE comment_id = '" + comment_id +"'");
			System.out.println("Comment Deleted");
		}
		else{
			System.out.println("Action Cancelled");
			System.out.println("Press any key to continue");
			getStringInput();
		}
	}

	public static void sql_rateMovie(int rating){
		dbUpdate("INSERT INTO rate " +
			"VALUES ('"+ USERNAME + "'," + currMovie + ",NOW(),"+rating+")");
	}

	public static void sql_favoriteMovie(String movie_id){
		dbUpdate("INSERT INTO likes " +
			"VALUES ('"+ USERNAME + "'," + movie_id + ",NOW())");
	}

	public static void sql_followUser(String user_id){
		dbUpdate("INSERT INTO follow "+
			"VALUES ('"+ currUser + "','" + USERNAME + "',NOW())");
	}

	public static void sql_unfollowUser(String user_id){
		dbUpdate("DELETE FROM follow "+
			"WHERE user_id_to = '" +currUser+ "'AND user_id_from ='" +USERNAME+ "'");
	}

	public static Boolean sql_usernameExists(String s){
		
		Table t = runQuery("SELECT user_id FROM users WHERE user_id ='" +s+"'");
		if(t != null) return true;
		else return false; //return false if it doesnt exist
	}

	public static void sql_deleteUser(String user_id){
		runQuery("DELETE FROM rate WHERE user_id = '" + user_id +"'");
		runQuery("DELETE FROM comment WHERE user_id = '" + user_id +"'");
		runQuery("DELETE FROM likes WHERE user_id = '" + user_id +"'");
		runQuery("DELETE FROM super_user WHERE super_user_id = '" + user_id +"'");
		runQuery("DELETE FROM follow WHERE user_id_to  = '" + user_id +"' OR user_id_from ='" + user_id +"'");
		runQuery("DELETE FROM orders WHERE user_id = '" + user_id +"'");
		runQuery("DELETE FROM users WHERE user_id = '" + user_id +"'");
	}

	public static Boolean sql_isSuperUser(String id){
		Boolean returnval = false;
		Table t = runQuery("SELECT super_user_id FROM super_user WHERE super_user_id = '" + id +"'");
		if(t != null) {System.out.println("You are a Super User!"); returnval = true;};
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
				"SELECT user_id, comment_time, content,comment_id " +
				"FROM comment " +
				"WHERE video_id = " + video_id + " " +
				"ORDER BY comment_time");
		int comment_num = 1;
		commentList.clear();
		System.out.println("-------------------------------");
		if(t != null){
			for (ArrayList<String> i : t.getTuples()) {
				System.out.printf("Comment #%d:\n",comment_num);
				System.out.println(i.get(1) + ", " + i.get(0) + " commented:\n\"" + i.get(2) +"\"\n");
				comment_num++;
				commentList.add(i.get(3));
			}
		}
		else{
			System.out.printf("No comments for this video.\nBe the first to comment!\n");
		}
		System.out.println("-------------------------------\n");
		
	}
	
	public static int sql_getBalance(){
		Table t = runQuery("SELECT balance FROM users WHERE user_id = '" + USERNAME + "'");
		String balance = t.getInfoFromColumn("balance").get(0);
		return Integer.valueOf(balance).intValue();
	}
	
	public static Boolean sql_placeOrder(String video_id){
		
		//get name,address,email
		/*
		System.out.printf("Order DVD for yourself? (y/n):");
		String orderforme = getStringInput();
		if(orderforme.equals("y")|| orderforme.equals("Y")){ //using our defualt address
		}
		*/
		
		//Confirm payment
		Table t = runQuery("SELECT * FROM video WHERE video_id =" + video_id);
		String dvd_price = t.getInfoFromFirstTuple("dvd_price");
		Boolean returnval = false;
		System.out.println("This DVD costs $" + dvd_price + ". Are you sure you? (y/n)");
		sql_printBalance();
		String Input = getStringInput();
		if(Input.equals("yes") || Input.equals("Yes")  || Input.equals("YES") || Input.equals("y") || Input.equals("Y")){
			returnval = true;
			System.out.println("\nDVD ordered.");
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
			dbUpdate("INSERT INTO orders VALUES (DEFAULT,"+ video_id + ",'" + USERNAME + "',NOW())");
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

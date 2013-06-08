import java.util.Scanner;


public class Main {

	//This does nothing. Don't worry about it... testing comment 123
	public Main() {}
	
	enum Page {LOGIN, CREATE_NEW, MAIN_MENU, MOVIE_SEARCH, MOVIE_INFO, FOLLOWING, FOLLOWING_EDIT, SETTINGS};
	static Page page = Page.LOGIN;
	static String USERNAME = null;
	static EmbeddedSQL db = null;
	
	public static void main(String args[]) {
		if(args.length == 4){
			try {
				db = new EmbeddedSQL(args[0], args[1], args[2], args[3]);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			testPrint();
		}
		System.out.println("Hello, Wolrd!");
		
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
			System.out.println("\n\n\n-=MAIN MENU=-");
			
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
			System.out.println("\n\n\n-=MOVIE SEARCH=-");
			
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

				default:
					System.out.println("Invalid input. Please try again.");
					break;
				}
			}
				
				
			break;
			
		//DETAIL MOVIE INFORMTION	
		case MOVIE_INFO:
			System.out.println("\n\n\n-=INSERT MOVIE NAME TITLE HERE INFO=-");

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
			System.out.println("\n\n\n-=FOLLOWING WALL=-");
			break;
			
		//ADD/REMOVE THE ACCOUTN USER IS FOLLOWING	
		case FOLLOWING_EDIT:
			System.out.println("\n\n\n-=EDIT PEOPLE I AM FOLLOWING=-");
			break;
			
		//USER'S SETTINGS
		case SETTINGS:
			System.out.println("\n\n\n-=SETTINGS=-");
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

		//TODO: If login is correct, do this. otherwise exit...
		if (true) {
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
		System.out.println("\n\n\n\n\n\n\n");
	}
	
	//////////////////////////////////////////////
	/////////////Create & Delete/////////////////
	/////////////////////////////////////////////
	
	//TODO: Finish this function
	//Creates a new account. Stores the data in the database.
	//TODO: Get address and other info?
	public static void createNewAccount(){

		clearConsole();
		System.out.println("Please enter the following data:");

		String usernameInput = getCheckInput("USERNAME",true);		
		String passwordInput = getCheckInput("PASSWORD",true);
		String firstInput = getCheckInput("First name",true);		
		String middleInput = getCheckInput("Middle name",false);		
		String lastInput  = getCheckInput("Last name",true);		
		String streetInput = getCheckInput("Address - street",false);
		String stateInput = getCheckInput("Address - state",false);
		String countryInput = getCheckInput("Address - country",false);
		String zipInput = getCheckInput("Address - zipcode",false);

		//TODO actually create the account
		
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

	public static String getCheckInput(String s, Boolean required){

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

	//////////////////////////////////////////////
	/////////////Helper Functions////////////////
	//////////////////////////////////////////////

	public static void clearConsole(){
		for(int i = 0; i < 50; i++){
			System.out.printf("\n");
		}
	}
	
	public static void testPrint(){
		Table t = null;
		try {
			t = db.executeQuery("SELECT * FROM video");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		System.out.println(t);
	}
	
}

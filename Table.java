import java.util.ArrayList;

public class Table {
	private ArrayList<String> names;
	private ArrayList<ArrayList<String>> tuples;
	
	public Table(ArrayList<String> pnames) {
		names = pnames;
		tuples = new ArrayList<ArrayList<String>>();
	}
	
	public void addEntry(ArrayList<String> newEntry){
		tuples.add(newEntry);
	}
	
	public ArrayList<String> getInfoFromColumn(String name){
		int columnNum = names.indexOf(name);
		ArrayList<String> results = new ArrayList<String>();
		for (ArrayList<String> tuple : tuples) {
			results.add(tuple.get(columnNum));
		}
		return results;
	}

	@Override
	public String toString() {
		String results = new String();
		for (String name : names) {
			results = results + name + "\t";
		}
		for (ArrayList<String> tuple : tuples) {
			results = results + "\n";
			for (String data : tuple) {
				results = results + data + "\t";
			}
		}
		
		return results;
	}
	
}//end EmbeddedSQL
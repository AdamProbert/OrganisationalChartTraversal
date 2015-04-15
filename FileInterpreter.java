import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileInterpreter {
	
	/*
	 * Interprets given text file 
	 * and returns map of employees organised by managaerID
	 */
	
	public Map<Integer, ArrayList<Employee>> interpret(String filePath) {

		// Map of employees parsed from text file
		Map<Integer, ArrayList<Employee>> employees = new HashMap<Integer, ArrayList<Employee>>();

		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			System.out.println("Error creating scanner for " + filePath);
			e.printStackTrace();
		}

		// Skip top heading line
		scanner.nextLine();
		
		while (scanner.hasNextLine()) {
			
			String line = scanner.nextLine();
			String[] temp = line.split("\\|");

			int employeeID = Integer.parseInt(temp[1].trim());
			String name = temp[2].trim();
			
			Employee node;

			// If there is no managerID
			if(temp[3].startsWith("    ")){
				
				// Give root node a managerID of 0
				// Assuming Manager ID's will always start from 1
				node = new Employee(name, employeeID, 0);
				
			}
			else{
				int managerID = Integer.parseInt(temp[3].trim());
				node = new Employee(name, employeeID, managerID);	
				
			}
			
			// If map already contains managerID add new node the list
			if(employees.containsKey(node.getManagerID())){
				employees.get(node.getManagerID()).add(node);
			}
			// Else create new list with node and add to map
			else{
				ArrayList<Employee> group = new ArrayList<Employee>();
				group.add(node);
				employees.put(node.getManagerID(), group);
			}	
		}

		scanner.close();
		return employees;
	}
}

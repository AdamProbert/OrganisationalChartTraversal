import java.util.ArrayList;

/*
 * A class containing employee details
 * Used as a leaf / node of the tree. 
 */
public class Employee {

	private String name;
	private int managerID;
	private int employeeID;
	private ArrayList<Employee> children;
	private Employee parent;

	// Constructor
	public Employee(String name, int employeeID, int managerID) {

		this.name = name;
		this.employeeID = employeeID;
		this.managerID = managerID;

	}


	// Used for debugging
	public void print() {
		System.out.println("EmployeeID:" + employeeID + " || Name:" + name
				+ " || ManagerID:" + managerID);
	}

	public int getNoChildren() {
		if (children != null) {
			return children.size();
		} else {
			return 0;
		}
	}

	public Employee getParent() {
		return parent;
	}

	public void setParent(Employee parent) {
		this.parent = parent;
	}

	public ArrayList<Employee> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Employee> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public int getManagerID() {
		return managerID;
	}

	public int getEmployeeID() {
		return employeeID;
	}
}

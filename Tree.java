import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Tree {

	private Employee root;
	private String filePath;
	private String start;
	private String end;

	public Tree(String[] args) {
		this.filePath = args[0];
		
		/* 
		 * Removes all leading, trailing, and multiple white spaces
		 * I took a snippet of code from: http://stackoverflow.com/questions/3958955/how-to-remove-duplicate-white-spaces-in-string-using-java
		 * for removing runs of whitespace
		 */
		this.start = args[1].replaceAll("\\s+", " ").trim();
		this.end = args[2].replaceAll("\\s+", " ").trim();
	}

	@SuppressWarnings("unused")
	private void printTree() {

		Queue<Employee> queue = new LinkedList<Employee>();
		queue.add(root);

		while (!queue.isEmpty()) {
			Employee parent = queue.remove();

			if (parent.getNoChildren() > 0) {
				String children = "";

				for (Employee child : parent.getChildren()) {
					children += child.getName() + ", ";
					queue.add(child);
				}

				System.out.println("Parent: " + parent.getName()
						+ " || Children: "
						+ children.substring(0, children.length() - 2));
			}
		}
	}

	private void insert(ArrayList<Employee> newNodes) {

		// If no root, create root - should be first element due to Map sorting
		if (root == null) {
			root = newNodes.get(0);
			return;
		}

		// Create a queue with root node for breadth first search
		Queue<Employee> queue = new LinkedList<Employee>();
		queue.add(root);

		while (!queue.isEmpty()) {

			// Pop first node from queue
			Employee parent = queue.remove();

			// If node was the last one in queue and has no children, children
			// must be added to this node.
			if (parent.getNoChildren() == 0 && queue.isEmpty()) {
				parent.setChildren(newNodes);

				// Reference the parent for each employee
				for (Employee c : newNodes) {
					c.setParent(parent);
				}

				/*
				 * Else, if node has children: search children for a child with
				 * no children. This child receives the newNodes.
				 */

			} else {
				
				for (Employee child : parent.getChildren()) {

					// If child has children: they get added to queue for future searching 
					if (child.getNoChildren() > 0) {
						queue.add(child);

					} else {
						child.setChildren(newNodes);

						// Reference the parent for each employee
						for (Employee c : newNodes) {
							c.setParent(child);
						}
						return;
					}
				}
			}
		}
	}

	// Returns route of communication between two employees.
	private String findRoute() {
		
		// Create a queue of nodes to search for breadth first search
		Queue<Employee> queue = new LinkedList<Employee>();
		queue.add(root);

		// Arrays store the route taken from each employee parameter to the root of the tree
		// Booleans keep track of what employee's have been found
		Boolean startFound = false;
		ArrayList<Employee> startToRoot = new ArrayList<Employee>();

		Boolean endFound = false;
		ArrayList<Employee> endToRoot = new ArrayList<Employee>();

		// Search queue for both start and end employees
		while (!queue.isEmpty()) {
			Employee current = queue.remove();
			
			// If both employee parameters are the same - return when one is found
			if(start.equalsIgnoreCase(end) && current.getName().equalsIgnoreCase(start)){
				
				String outputRoute = current.getName() + " (" + current.getEmployeeID() + ") " + "-> " + current.getName() + " (" + current.getEmployeeID() + ") ";
						return outputRoute;
			}

			// If found start employee
			if (current.getName().equalsIgnoreCase(start) && !startFound) {

				startFound = true;

				// When employee found - trace back up tree to the root.
				Employee currentNode = current;

				do {
					startToRoot.add(currentNode);
					currentNode = currentNode.getParent();

				} while (currentNode != null);

			}

			// If found end employee
			else if (current.getName().equalsIgnoreCase(end) && !endFound) {

				endFound = true;

				// When employee found - trace back up tree to the root.
				Employee currentNode = current;

				do {
					endToRoot.add(currentNode);
					currentNode = currentNode.getParent();

				} while (currentNode != null);
			}

			// Reached a leaf with no children - move on
			if (current.getNoChildren() == 0) {
				continue;
			}

			else {
				// Reached a leaf with children - add all to queue
				for (Employee child : current.getChildren()) {
					queue.add(child);
				}
			}
		}
		

		if (!endFound) {
			System.out.println("Sorry, could not find " + end);
			return "";
		} else if (!startFound) {
			System.out.println("Sorry could not find " + start);
			return "";

			// If both nodes have been found search for the closest common ancestor
		} else {

			// breakLoop boolean used to exit outer loop when common ancestor found
			boolean breakLoop = false;
			for (int e = 0; e < startToRoot.size() && !breakLoop; e++) {
				for (int r = 0; r < endToRoot.size(); r++) {

					// if either nodeToRoot arrays shares a common ancestor
					if (startToRoot.get(e).getName()
							.equals(endToRoot.get(r).getName())) {

						// Remove all other ancestors from routes.
						startToRoot.subList(e, startToRoot.size()).clear();
						// r+1 to ensure one version of the common ancestor is stored.
						endToRoot.subList(r + 1, endToRoot.size()).clear();
						breakLoop = true;
						break;

					}
				}
			}
		}
		
		// Finally compile output string
		String outputRoute = "";

		for (Employee e : startToRoot) {
			outputRoute += e.getName() + " (" + e.getEmployeeID() + ") " + "-> ";
		}

		for (int i = endToRoot.size() - 1; i >= 0; i--) {
			outputRoute += endToRoot.get(i).getName() + " (" + endToRoot.get(i).getEmployeeID() + ") " + "<- ";
		}

		// Return the route, minus the last arrow.
		return outputRoute.substring(0, outputRoute.length() - 3);
	}

	private void run() {

		// Interpret given text file - returns Map of employees
		Map<Integer, ArrayList<Employee>> employees = new FileInterpreter()
				.interpret(filePath);

		// Populate tree with each list of identical managerID employees
		for (Map.Entry<Integer, ArrayList<Employee>> entry : employees
				.entrySet()) {
			insert(entry.getValue());
		}
		
		System.out.println(findRoute());
	}

	public static void main(String[] args) {
		new Tree(args).run();

	}

}

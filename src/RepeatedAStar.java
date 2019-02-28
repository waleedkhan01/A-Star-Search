import java.util.Collections;
import java.util.Stack;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RepeatedAStar extends Maze implements Serializable {
	
	public RepeatedAStar(int row, int col, int start_row, int start_col, int goal_row, int goal_col) {
		super(row, col, start_row, start_col, goal_row, goal_col);
		// TODO Auto-generated constructor stub
	}
	public boolean forward_backward(int dir, int priority) {
		boolean res = false;
		num_expansions = 0;
		
		if(dir == 0) {
			current = start;
			calc_heuristic();
			res = repeat_a_star(0, priority);
			clear_list();
			
			return res;
		}
		
		else {
			Node t = end;
			end = start;
			start = t;
			calc_heuristic();
			current = start;
			
			res = repeat_a_star(0, priority);
			clear_list();
			
			t = end;
			end = start;
			start = t;
			
			return res;
		}
	}
	
	public boolean adaptive(int start_row, int start_col, Stack<Node> expanded_list) {
		Node t;
		current = maze[start_row][start_col];
		calc_heuristic();
		boolean res;
		while(!expanded_list.isEmpty()) {
			t = expanded_list.pop();
			//int new_h = this.maze[t.row][t.col].h_val;
			this.maze[t.row][t.col].h_val
				= this.end.get_g_val()
				-t.get_g_val();
		}
		res = repeat_a_star(0, 0);
		return res;
	}
	
	public boolean repeat_a_star(int increment, int priority) {
		set_values(increment);
		
		if(isPath()) {
			return true;
		}
		
		manipulate_maze(current);
		
		if(opened_list.size() < 1) {
			return is_path;
		}
		
		if(priority == 1) {
			Collections.sort(opened_list, Node.small);
		}
		else {
			Collections.sort(opened_list, Node.big);
		}
		
		current = opened_list.get(0);
		opened_list.remove(0);
		
		repeat_a_star(increment+1, priority);
		
		set_path();
		
		return is_path;	
	}
	
	//main method for creating and storing the mazes
	
	/*public static void main(String[] args) throws IOException {
		boolean path;
		Stack<Node> expanded_list;
		RepeatedAStar new_search = new RepeatedAStar(101, 101, 0, 0, 100, 100);

			path = new_search.forward_backward(0, 0);
			//forward
			new_search.print_path(new_search.best_path);
			
			if(path) {
				
				System.out.println("Forward Success");
				//duplicate the closed list for adaptive
				expanded_list = new_search.closed_list;
				System.out.println("First run");
				System.out.print(new_search.output_maze());
				String buffer = new_search.output_maze();
				BufferedWriter file = new BufferedWriter(new FileWriter(new File("maze1-forward-backward.txt")));
				file.write(buffer.toString());
				file.flush();
				file.close();
				new_search.clear_path();
				new_search.clear_list();
				
				//backwards
				new_search.forward_backward(1, 2);
				new_search.clear_path();
				new_search.clear_list();
				
				//adaptive
				path = new_search.adaptive(0, 50, expanded_list);
				
				if(path) {
					System.out.println("Adaptive Success");
					try {
						FileOutputStream file_out = new FileOutputStream("./maze50.ser");
						ObjectOutputStream object_out = new ObjectOutputStream(file_out);
						object_out.writeObject(new_search);
						object_out.close();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					buffer = new_search.output_maze();
					file = new BufferedWriter(new FileWriter(new File("maze1-adaptive.txt")));
					file.write(buffer.toString());
					file.flush();
					file.close();
				
					//new_search.print_path(new_search.best_path);
				
					try {
						FileInputStream file_in = new FileInputStream("maze50.ser");
						ObjectInputStream object_in = new ObjectInputStream(file_in);
						RepeatedAStar m = (RepeatedAStar) object_in.readObject();
						System.out.println("Adaptive Run");
						System.out.print(m.output_maze());
						object_in.close();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
				}
		}
	}*/
	
	//main method for running pathfinding on the mazes
	
	public static void main(String [] args){
		String file_input_name = "maze1";
		String file_forward_name = file_input_name + "-forward.txt";
		String file_backward_name = file_input_name + "-backward.txt";
		String file_adaptive_name = file_input_name + "-adaptive.txt";
		Stack<Node> expanded_list;
		try {
			FileInputStream file_in = new FileInputStream(file_input_name + ".ser");
			ObjectInputStream object_in = new ObjectInputStream(file_in);
			RepeatedAStar m = (RepeatedAStar) object_in.readObject();
			m.clear_list();
			m.clear_path();
			//forward
			boolean path = m.forward_backward(0, 0);
			if(path) {
				expanded_list = m.closed_list;
				String buffer = m.output_maze();
				System.out.println("Forward Pathing Number of Expansions: " + m.closed_list.size());

				BufferedWriter file = new BufferedWriter(new FileWriter(new File(file_forward_name)));
				file.write(buffer.toString());
				file.flush();
				file.close();
				
				//backward
				m.clear_path();
				m.clear_list();
				m.forward_backward(1, 2);
				
				System.out.println("Backward Pathing Number of Expansions: " + m.closed_list.size());
				buffer = m.output_maze();

				file = new BufferedWriter(new FileWriter(new File(file_backward_name)));
				file.write(buffer.toString());
				file.flush();
				file.close();
				
				m.clear_path();
				m.clear_list();
				m.adaptive(0, 50, expanded_list);
				
				System.out.println("Adaptive Pathing Number of Expansions starting from [0, 50]: " + m.closed_list.size());
				
				buffer = m.output_maze();
				file = new BufferedWriter(new FileWriter(new File(file_adaptive_name)));
				file.write(buffer.toString());
				file.flush();
				file.close();
			}
			object_in.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

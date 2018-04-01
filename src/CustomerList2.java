
import java.io.*;
import java.util.*;
/**
 * Version 2.0
 * @author lbuga
 *
 */
public class CustomerList2 {
	
	public static int serveTime = 0;
	private static boolean pm = false;
	private static int previoushour = 0;
	private static int idletime =0;
	private static int longestIdleTime = 0;
	private static int totalCustomers = 0;
	private static int longestQueue = 0;
	private static int currentQueue = 0;
	
	public static boolean isPm() {
		return pm;
	}
	public static void setPm(boolean pm) {
		CustomerList2.pm = pm;
	}
	public static int getHour() {
		return previoushour;
	}
	public static void setPrevioushour(int hour) {
		CustomerList2.previoushour = hour;
	}

	public class Customer {
		private int id;
		private int entry_time;
		private int wait_time;
		public Customer(String info) {
			String [] lines = info.split("\n");
			String temp = "";
			for (int i=0;i<lines[0].length();i++) {
				if(Character.isDigit(lines[0].charAt(i))) {
					temp+=lines[0].charAt(i);
				}
			}
			String[] times = lines[1].split(":");
			//Parse the time into seconds
			int extra_time = 0;
			if (Integer.parseInt(times[1].trim())>previoushour && !CustomerList2.pm) {
				CustomerList2.setPrevioushour(Integer.parseInt(times[1].trim()));
			}
			else if (Integer.parseInt(times[1].trim())<previoushour && !CustomerList2.pm){
				CustomerList2.setPm(true);
			}
			int time = Integer.parseInt(times[1].trim())*3600+Integer.parseInt(times[2].trim())*60+Integer.parseInt(times[2].trim());

			if (CustomerList2.isPm()) {
				time+= 12*3600;
			}
			this.setId(Integer.parseInt(temp));
			this.setEntry_time(time);
			CustomerList2.totalCustomers++;
		}
		public void setEntry_time(int entry_time) {
			this.entry_time= entry_time;
		}
		public int getEntry_time() {
			return this.entry_time;
		}
		public void setId(int id) {
			this.id= id;
		}
		public int getId() {
			return this.id;
		}
	}
	
	public class Node{
		public Customer data;
		public Node next= null;
	}
	
	private Node first;
	public CustomerList2() {
		this.setFirst(null);
	}
	public void setFirst(Node n) {
		this.first = n;
	}
	public void addLast(Customer c) {
		Node newNode = new Node ();
		newNode.data = c;
		newNode.next= null;
		//Iterate through al the elements to add the new Node to the end of the list
		Iterator temp = new Iterator();
		while(temp.hasNext()) {
			temp.next();
		}
		
		//Add the waiting time for each new Customer added
		temp.position.next=newNode;
		if (newNode.data.getEntry_time()-temp.position.data.getEntry_time()<CustomerList2.serveTime//If time difference is less than service time
				) {
			newNode.data.wait_time=temp.position.data.wait_time+(CustomerList2.serveTime-(newNode.data.getEntry_time()-temp.position.data.getEntry_time()));
			CustomerList2.currentQueue++;
		}
		else {
			/*
			 * Look through this
			 */
			int time = newNode.data.entry_time-temp.position.data.entry_time+300;
			CustomerList2.idletime+=time;
			if (time>CustomerList2.longestIdleTime) {
				CustomerList2.longestIdleTime=time;
			}
			newNode.data.wait_time=0;
			if(CustomerList2.currentQueue>CustomerList2.longestQueue) {
				CustomerList2.longestQueue = CustomerList2.currentQueue;
				CustomerList2.currentQueue = 0;
			}
		}
		
	}
	public class Iterator implements ListIterator<Object>{

		private Node position;
		private Node previous;
		
		public Iterator() {
			position = null;
			previous = null;
					
		}
		@Override
		public void add(Object arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasNext() {
			if (this.position.next==null || first==null) {
				return false;
			}
			else {
				return true;
			}
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			this.previous = position;
			if (this.position ==null) {
				position = first;
			}
			else {
				this.position =position.next;
			}
			return position.data;//Returns a customer object with ID and entry time
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object previous() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void remove() {
			if (this.position == first) {
				//removeFirst();
			}
			else {
				//Sets the previous reference to the curent position
				previous.next = position.next;
				this.previous = updatePrevious(this.position);
			}
			
		}
		/*
		 * Updates the previous position when a node is deleted
		 */
		public Node updatePrevious(Node n) {
			Iterator temp = new Iterator();
			while (temp.position!=n) {
				temp.next();
			}
			return temp.previous;
		}

		@Override
		public void set(Object arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static void main(String[]args) throws FileNotFoundException{
		System.out.println("Hello World.");
		String f1path = "customersfile.txt";
		String f2path = "queriesfile.txt";
		FileReader customers = new FileReader(f1path);
		FileReader queries = new FileReader (f2path);
		BufferedReader readCustomers = new BufferedReader(customers);
		BufferedReader readQueries = new BufferedReader(queries);
		
		
		/*
		 * Get the constant serve time for each customer by reading the first line
		 * and parsing it as an integer
		 */
		try {
			CustomerList2.serveTime = Integer.parseInt(readCustomers.readLine());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		CustomerList2 clist = new CustomerList2();
		try {
			String temp = "";
			String thisLine = "";
			while((thisLine= readCustomers.readLine())!=null) {
				//String thisLine =readCustomers.readLine();
				//System.out.println(thisLine);
				if(temp.length()>0 && thisLine.isEmpty() ) {
					//Create a new Customer and add the customer to the Customer LinkedList
					
					clist.addLast(clist.new Customer(temp));
					
					//Reset the data read from the file
					temp = "";
				}
				else if (thisLine.isEmpty()) {
					
					continue;
				}
				else {
					temp+= thisLine;temp+="\n";
					//System.out.println(temp);
				}
				
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		String result ="";
		try {
			String thisLine = "";
			result += readQueries.readLine()+ ": "+CustomerList2.totalCustomers +"\n";
			result += readQueries.readLine()+ ": "+CustomerList2.longestIdleTime+"\n";
			result += readQueries.readLine()+ ": "+CustomerList2.idletime+"\n";
			result += readQueries.readLine()+ ": "+CustomerList2.longestQueue+"\n";
			while((thisLine= readQueries.readLine())!=null) {
				result+=thisLine + ": ";
				String find_id = "";
				for(int i=0;i<thisLine.length();i++) {
					if (Character.isDigit(thisLine.charAt(i))) {
						find_id+=thisLine.charAt(i);
					}
				}
				Iterator temp = clist.new Iterator();
				while(temp.hasNext()) {
					if (temp.position.data.getId()==Integer.parseInt(find_id)) {
						result += temp.position.data.getId()+"\n";
						break;
					}
				}
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	
	
}

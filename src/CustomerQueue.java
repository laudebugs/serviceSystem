import java.io.*;
import java.util.*;
/**
 * Version 4.2
 * @author lbugasu
 *
 */

public class CustomerQueue {
	//The serving time for each customer
	public static int serveTime = 0;
	
	private static boolean pm = false;
	private static int previoushour = 0;

	/*
	 * This is the reference to the first node on the list
	 */
	private Node first = null;
	
	public static boolean isPm() {
		return pm;
	}
	public static void setPm(boolean pm) {
		CustomerQueue.pm = pm;
	}
	public static int getHour() {
		return previoushour;
	}
	public static void setPrevioushour(int hour) {
		CustomerQueue.previoushour = hour;
	}

	public class Customer {
		private int id;
		private int entry_time;
		private int wait_time;
		public Customer() {
			
		}
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
			if (Integer.parseInt(times[1].trim())>previoushour && !CustomerQueue.pm) {
				CustomerQueue.setPrevioushour(Integer.parseInt(times[1].trim()));
			}
			else if (Integer.parseInt(times[1].trim())<previoushour && !CustomerQueue.pm){
				CustomerQueue.setPm(true);
			}
			int time = Integer.parseInt(times[1].trim())*3600+Integer.parseInt(times[2].trim())*60+Integer.parseInt(times[2].trim());

			if (CustomerQueue.isPm()) {
				time+= 12*3600;
			}
			this.setId(Integer.parseInt(temp));
			this.setEntry_time(time);
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
		public Node next = null;
	}
	
	public CustomerQueue() {
		this.setFirst(null);
	}
	/**
	 * Sets the first node to be null
	 * @param a node that is null
	 */
	public void setFirst(Node n) {
		this.first = n;
	}
	/**
	 * This method will handle several aspects of a customer's attributes in order to serve them bes
	 * These attributes are:
	 ** It's position on the linked list
	 ** The waiting time for each customer
	 * @param c
	 */
	public void addLast(Customer c) {
		//Check if the customer arrived before or on time.
		if (c.getEntry_time()<17*3600) {

			//An iterator to iterate through the Linked List
			Iterator temp = new Iterator();
			temp.position=this.first;
			
			//Create the new Node
			Node newNode = new Node ();
			newNode.data = c;
			newNode.next=null;	
			
			//If the Node is the first one to be attached, add it to the first element in the Linked List
			if(this.first==null) {
				this.first = newNode;
				temp.position = newNode;
				}
			//Iterate through the linked list to get to the end position
			else {
				while(temp.hasNext()) {
					temp.next();
				}
				//Add the new Node to the end of the linked list.
				temp.previous= temp.position;
				temp.position.next=newNode;
			}
			
			//Add the waiting time for each new Customer added
			if(newNode.data.getEntry_time()<9*3600) {
				 newNode.data.wait_time=9*3600-newNode.data.getEntry_time();
				 if(newNode!=this.first) {
					 newNode.data.wait_time+=CustomerQueue.serveTime;
				 }
				 }
			else if (newNode.data.getEntry_time()-(temp.previous.data.getEntry_time()+CustomerQueue.serveTime)<CustomerQueue.serveTime) {
				newNode.data.wait_time=temp.position.data.wait_time+(CustomerQueue.serveTime-(newNode.data.getEntry_time()-temp.previous.data.getEntry_time()));
				}
			else {
				newNode.data.wait_time=0;
			}
		}
	}
	
	/**
	 * This method gets the longest idle time of the workers
	 * @return
	 */
	
	
	/**
	 * This method returns the longest queue on the line of customers being served
	 * @return
	 */
	public int longest_queue() {
		int count1 = 0;
		int count2 = 0;
		Iterator it = new Iterator();
		it.position= this.first;
		Node start = new Node();
		while(it.hasNext()) {
			if(it.position==this.first) {
				start = it.position;
			}
			else if(it.position.data.getEntry_time()-it.previous.data.getEntry_time()>CustomerQueue.serveTime){
				start = it.position;
				if(count1>count2) {
					count2=count1;
				}
				count1=0;
			}
			else {
				count1++;
			}
			it.next();
			}
		if(it.position.data.getEntry_time()-it.previous.data.getEntry_time()<=CustomerQueue.serveTime){
			count2++;
			}
		return count2;
	}
	
	/**
	 * This method counts how many customers were served
	 * @return the total number of customers served
	 */
	public int totalCustomers() {
		int count = 0;
		Iterator it = new Iterator();
		it.position = this.first;
		while(it.hasNext()) {
			count++;
			it.next();
		}
		count++; //Add the last customer to be served
		return count;
	}
	
	public int getLongestIT() {
		int time = 0;
		int now = 0;
		Iterator it = new Iterator();
		it.position=this.first;
		int starttime = 9*3600;
		int endtime = 17*3600;
		while (it.hasNext()) {
			if(it.position==this.first) {
				now=it.position.data.getEntry_time()-starttime;
			}
			else {
				now = it.position.data.getEntry_time()-(it.previous.data.getEntry_time()+CustomerQueue.serveTime);
			}
			if (now>CustomerQueue.serveTime) {
				if(now>time) {
					time = now;
				}
			}
			it.next();
		}
		if (it.position.data.getEntry_time()-(it.previous.data.getEntry_time()+CustomerQueue.serveTime)>CustomerQueue.serveTime) {
			if(it.position.data.getEntry_time()-it.previous.data.getEntry_time()>time) {
				time = it.position.data.getEntry_time()-(it.previous.data.getEntry_time()+CustomerQueue.serveTime);
			}
		}
		if (it.position.next==null && it.position.data.getEntry_time()+CustomerQueue.serveTime<endtime) {
			time = endtime-(it.position.data.getEntry_time()+CustomerQueue.serveTime);
		}
		return time;
	}
	
	public int totalIT() {
		int time = 0;
		int now = 0;
		Iterator it = new Iterator();
		it.position=this.first;
		int starttime = 9*3600;
		int endtime = 17*3600;
		while (it.hasNext()) {
			if(it.position==this.first) {
				time+=it.position.data.getEntry_time()-starttime;
			}
			else {
				time+= it.position.data.getEntry_time()-(it.previous.data.getEntry_time()+CustomerQueue.serveTime);
			}
			if (now>CustomerQueue.serveTime) {
				if(now>time) {
					time+= now;
				}
			}
			it.next();
		}
		//Idle time before the last customer has been served
		int last = it.position.data.getEntry_time();
		if (last-(it.previous.data.getEntry_time()+CustomerQueue.serveTime)>CustomerQueue.serveTime) {
			time += last-(it.previous.data.getEntry_time()+CustomerQueue.serveTime);
		}
		//Idle time after customer is served before time ends
		if (it.position.next==null && last+CustomerQueue.serveTime<endtime) {
			time += endtime-(it.position.data.getEntry_time()+CustomerQueue.serveTime);
		}
		return time;
	}
	
	/**
	 * This is the Iterator class that iterate through all the elements in the list
	 * @author lbugasu
	 *
	 */
	public class Iterator implements ListIterator<Customer>{

		private Node position;
		private Node previous;
		
		public Iterator() {
			position = null;
			previous = null;
					
		}
		@Override
		public void add(Customer obj) {
			// TODO Auto-generated method stub
			if(this.position==null) {
				//Add the object to the first position using the AddFirst() Method
			}
			
		}

		@Override
		public boolean hasNext() {
			if (this.position.next == null) {
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

		/*
		This method moves the iterator to the next position
		*/
		@Override
		public Customer next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}

			this.previous = this.position;
			this.position = this.previous.next;
			return previous.data;//Returns a customer object with ID and entry time
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Customer previous() {
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
				removeFirst();
			}
			else {
				//Sets the previous reference to the curent position
				previous.next = position.next;
				/*
				 * Implement a method that updates the previous in an iterator
				 */
				//this.previous = updatePrevious(this.position);
			}
			this.position = this.previous;
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
		public void set(Customer c) {
			// TODO Auto-generated method stub
			
		}
		public void removeFirst() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static void main(String[]args) throws FileNotFoundException{
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
			CustomerQueue.serveTime = Integer.parseInt(readCustomers.readLine());
			} 
		catch (NumberFormatException e1) {
			e1.printStackTrace();
			} 
		catch (IOException e1) {
			e1.printStackTrace();
			}
		CustomerQueue clist = new CustomerQueue();
		try {
			String temp = "";
			String thisLine = "";
			while((thisLine= readCustomers.readLine())!=null) {

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
					temp+= thisLine;
					temp+="\n";
					}
			}
			
			//Add the last customer on the Queue- since the loop will not add them
			clist.addLast(clist.new Customer(temp));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		String result ="";

		//Read the queries:
		try {
			String thisLine = "";

			while((thisLine= readQueries.readLine())!=null) {
				
				if (thisLine.contains("NUMBER-OF-CUSTOMERS-SERVED")){
					result += thisLine+ ": "+clist.totalCustomers() +"\n";
				}
				else if (thisLine.contains("LONGEST-BREAK")) {
					result += thisLine+ ": "+clist.getLongestIT()+"\n";
				}
				else if(thisLine.contains("IDLE-TIME")) {
					result += thisLine+ ": "+clist.totalIT()+"\n";
				}
				else if(thisLine.contains("MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME")) {
					result += thisLine+ ": "+clist.longest_queue()+"\n";
				}
				else {
					result+=thisLine + ": ";
					String find_id = "";
					for(int i=0;i<thisLine.length();i++) {
						if (Character.isDigit(thisLine.charAt(i))) {
							find_id+=thisLine.charAt(i);
						}
					}
					
					Iterator temp = clist.new Iterator();
					temp.position=clist.first;
	
					while(temp.hasNext()) {
						if (temp.position.data.getId()==Integer.parseInt(find_id)) {
							result += temp.position.data.wait_time+"\n";
						}
						temp.next();
					}
					if(!temp.hasNext() && temp.position.data.getId()==Integer.parseInt(find_id)) {
						result += temp.position.data.wait_time+"\n";
					}
				}
				thisLine ="";
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}
}

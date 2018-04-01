package serviceSystem;

public class CustomerList {

	public class Customer {
	
		private int id;
		private int entry_time;
		public Customer(int id, int entry_time) {
			this.setEntry_time(entry_time);
			this.setId(id);
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
		public Customer next= null;
	}
	
	private Node first;
	public CustomerList() {
		this.setFirst(null);
	}
	public void setFirst(Node n) {
		this.first = n;
	}
	public static void main(String[]args){
		System.out.println("Hello World.");
	}
}

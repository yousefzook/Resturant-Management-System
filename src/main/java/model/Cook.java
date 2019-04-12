package model;

import java.util.ArrayList;

import javafx.util.Pair;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class Cook {

	@Getter
	@Setter
	public List<Order> assignedOrders;
	@Getter
	@Setter
	public int id;
	@Getter
	@Setter
	public String firstName, lastName;

	public Cook(int id, String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		assignedOrders = new ArrayList<Order>();
	}

	public boolean acceptOrder(Order o) {
		return true;
	}

	public boolean setIrderState(Order o, OrderState s) {
		return true;
	}
}

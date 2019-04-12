package model;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.util.Pair;
import lombok.Data;
import lombok.Getter;

@Data
public class Dish {

	@Getter
	int rate, id, timeToPrepare, rateCount;
	@Getter
	String name, description;
	@Getter
	float price;
	@Getter
	byte[] image;

	public Dish(int id, String name, String description, float price, int rate, int rateCount, int timeToPrepare,
			byte[] image) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.rate = rate;
		this.rateCount = rateCount;
		this.timeToPrepare = timeToPrepare;
		this.image = image;
	}

	public ArrayList<Pair<String, String>> getDBAttributes() {
		ArrayList<Pair<String, String>> attrs = new ArrayList<Pair<String, String>>();
		attrs.add(new Pair<String, String>("id", Integer.toString(this.id)));
		attrs.add(new Pair<String, String>("name", this.name));
		attrs.add(new Pair<String, String>("price", Float.toString(this.price)));
		attrs.add(new Pair<String, String>("description", this.description));
		attrs.add(new Pair<String, String>("rate", Integer.toString(this.rate)));
		attrs.add(new Pair<String, String>("rate_count", Integer.toString(this.rateCount)));
		attrs.add(new Pair<String, String>("time_to_prepare_in_minutes", Integer.toString(this.timeToPrepare)));
		attrs.add(new Pair<String, String>("image", Arrays.toString(this.image)));
		return attrs;
	}
}

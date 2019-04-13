package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class Dish {

    private Integer id, timeToPrepare, rateCount=1;

    private String name, description;

    private Float rate=5F, price;

    private String imagePath;

    public ArrayList<Pair<String, String>> getDBAttributes() {
        ArrayList<Pair<String, String>> attrs = new ArrayList<Pair<String, String>>();
        attrs.add(new ImmutablePair<String, String>("did", Integer.toString(this.id)));
        System.out.println("000000000000000000000000000000000000000000000000000000000000000000000000000");
        attrs.add(new ImmutablePair<String, String>("name", "'" + this.name + "'"));
        attrs.add(new ImmutablePair<String, String>("price", Float.toString(this.price)));
        attrs.add(new ImmutablePair<String, String>("description", "'" + this.description + "'"));
        attrs.add(new ImmutablePair<String, String>("rate", Float.toString(this.rate)));
        attrs.add(new ImmutablePair<String, String>("rate_count", Integer.toString(this.rateCount)));
        attrs.add(new ImmutablePair<String, String>("time_to_prepare_in_minutes", Integer.toString(this.timeToPrepare)));
        attrs.add(new ImmutablePair<String, String>("image_path", this.imagePath));
        return attrs;
    }

}

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

    private int id, timeToPrepare, rateCount;

    private String name, description;

    private float rate, price;

    private byte[] image;

    private String imagePath;

    public Dish() {
    }

    public ArrayList<Pair<String, String>> getDBAttributes() {
        ArrayList<Pair<String, String>> attrs = new ArrayList<Pair<String, String>>();
        attrs.add(new ImmutablePair<>("did", Integer.toString(this.id)));
        attrs.add(new ImmutablePair<String, String>("name", "'" + this.name + "'"));
        attrs.add(new ImmutablePair<String, String>("price", Float.toString(this.price)));
        attrs.add(new ImmutablePair<String, String>("description", "'" + this.description + "'"));
        attrs.add(new ImmutablePair<String, String>("rate", Float.toString(this.rate)));
        attrs.add(new ImmutablePair<String, String>("rate_count", Integer.toString(this.rateCount)));
        attrs.add(new ImmutablePair<String, String>("time_to_prepare_in_minutes", Integer.toString(this.timeToPrepare)));
        StringBuilder img = new StringBuilder();
        for (byte b : this.image)
            img.append(Byte.toString(b));
        attrs.add(new ImmutablePair<String, String>("image", img.toString()));
        return attrs;
    }

}

package glass.brew.napkin.model;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class Selection {

    /**
     *
     */
    private int id;
    private Food food_a;
    private Food food_b;

    /**
     *
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood_a() {
        return food_a;
    }

    public void setFood_a(Food food_a) {
        this.food_a = food_a;
    }

    public Food getFood_b() {
        return food_b;
    }

    public void setFood_b(Food food_b) {
        this.food_b = food_b;
    }

}

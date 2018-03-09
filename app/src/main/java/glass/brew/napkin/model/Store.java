package glass.brew.napkin.model;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class Store {

    private int id;
    private String name;
    private String tel;

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

package mytunes.be;

public class Artist {

    private String name;
    private int id;

    public Artist(String name, int id){
        this.name = name;
        this.id = id;
    }


    @Override
    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

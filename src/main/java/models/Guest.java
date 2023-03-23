package models;

public class Guest {
    private long id;
    private String name;

    public Guest(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Guest(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

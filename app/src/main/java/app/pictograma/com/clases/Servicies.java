package app.pictograma.com.clases;

public class Servicies {

    private int id;
    private String name;
    private int imagen;

    public Servicies(int id, String name, int imagen) {
        this.id = id;
        this.name = name;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}

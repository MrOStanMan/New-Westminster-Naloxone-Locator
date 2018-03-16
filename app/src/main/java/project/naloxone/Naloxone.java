package project.naloxone;

/**
 * Created by John on 2017-11-25.
 */

public class Naloxone implements java.io.Serializable {

    private String json_featuretype;
    private String Name;
    private String Description;
    private String Category;
    private String Hours;
    private String Location;
    private String PC;
    private String Phone;
    private String Email;
    private String Website;
    private String X;
    private String Y;


    public Naloxone(String json_featuretype, String name, String description, String category, String hours, String location, String PC, String phone, String email, String website, String x, String y) {
        this.json_featuretype = json_featuretype;
        Name = name;
        Description = description;
        Category = category;
        Hours = hours;
        Location = location;
        this.PC = PC;
        Phone = phone;
        Email = email;
        Website = website;
        X = x;
        Y = y;
    }

    public String getJson_featuretype() {
        return json_featuretype;
    }

    public void setJson_featuretype(String json_featuretype) {
        this.json_featuretype = json_featuretype;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }
}

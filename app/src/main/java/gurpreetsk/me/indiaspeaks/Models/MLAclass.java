package gurpreetsk.me.indiaspeaks.Models;

/**
 * Created by daman on 20/8/16.
 */
public class MLAclass {

    String name, email;

    public MLAclass(){}

    public MLAclass(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

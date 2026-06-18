package uy.edu.um.entities;

public class User {

    private int weight;
    private int uid;
    private String alias;
    private UserType type;

    public User(int uid, String alias, UserType type) {
        this.uid = uid;
        this.alias = alias;
        this.type = type;
    }


    public int getUid() {
        return uid;
    }

    public String getAlias() {
        return alias;
    }

    public UserType getType() {
        return type;
    }

    public int getWeight() {
        if (type == UserType.ADMIN) {
            return 2;
        }
        return 1;
    }


}

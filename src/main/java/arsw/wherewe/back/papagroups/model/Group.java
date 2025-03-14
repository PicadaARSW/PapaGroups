package arsw.wherewe.back.papagroups.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Group model class
 */
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String admin;
    private List<String> members;
    private String code;

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Group(String id, String admin, List<String> members, String code) {
        this.id = id;
        this.admin = admin;
        this.members = members;
        this.code = code;
    }

    public Group() {
    }


}
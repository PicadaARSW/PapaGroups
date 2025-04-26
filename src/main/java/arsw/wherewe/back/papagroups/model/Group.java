package arsw.wherewe.back.papagroups.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Group model class
 */
@Document(collection = "groups")
@Schema(description = "Group entity representing a group in the system")
public class Group {
    @Id
    @Schema(description = "Unique identifier of the group", example = "507f1f77bcf86cd799439011")
    private String id;
    @Schema(description = "ID of the group admin", example = "admin123")
    private String admin;
    @Schema(description = "Name of the group", example = "Study Group")
    private String nameGroup;
    @Schema(description = "List of member IDs in the group", example = "[\"user1\", \"user2\"]")
    private List<String> members;
    @Schema(description = "Unique code to join the group", example = "ABC123")
    private String code;
    @Schema(description = "Date and time of the last code update", example = "2023-10-01T10:00:00")
    private LocalDateTime lastCodeUpdate;

    @Schema(description = "Date and time of the next code update", example = "2023-10-04T10:00:00")
    private LocalDateTime nextCodeUpdate;

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

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public LocalDateTime getLastCodeUpdate() {
        return lastCodeUpdate;
    }

    public void setLastCodeUpdate(LocalDateTime lastCodeUpdate) {
        this.lastCodeUpdate = lastCodeUpdate;
    }

    public LocalDateTime getNextCodeUpdate() {
        return nextCodeUpdate;
    }

    public void setNextCodeUpdate(LocalDateTime nextCodeUpdate) {
        this.nextCodeUpdate = nextCodeUpdate;
    }

    public Group(String id, String admin,String nameGroup, List<String> members, String code, LocalDateTime lastCodeUpdate, LocalDateTime nextCodeUpdate) {
        this.id = id;
        this.admin = admin;
        this.nameGroup = nameGroup;
        this.members = members;
        this.code = code;
        this.lastCodeUpdate = lastCodeUpdate;
        this.nextCodeUpdate = nextCodeUpdate;
    }

    public Group() {
    }


}
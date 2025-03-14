package arsw.wherewe.back.papagroups.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GroupTests {

    @Test
    void createGroupWithValidData() {
        Group group = new Group("1", "adminId", List.of("adminId"), "code123");

        assertEquals("1", group.getId());
        assertEquals("adminId", group.getAdmin());
        assertEquals(1, group.getMembers().size());
        assertEquals("adminId", group.getMembers().get(0));
        assertEquals("code123", group.getCode());
    }

    @Test
    void createGroupWithEmptyConstructor() {
        Group group = new Group();
        group.setId("1");
        group.setAdmin("adminId");
        group.setMembers(List.of("adminId"));
        group.setCode("code123");

        assertEquals("1", group.getId());
        assertEquals("adminId", group.getAdmin());
        assertEquals(1, group.getMembers().size());
        assertEquals("adminId", group.getMembers().get(0));
        assertEquals("code123", group.getCode());
    }

    @Test
    void addMemberToGroup() {
        Group group = new Group("1", "adminId", List.of("adminId"), "code123");
        group.setMembers(List.of("adminId", "user1"));

        assertEquals(2, group.getMembers().size());
        assertTrue(group.getMembers().contains("user1"));
    }

    @Test
    void removeMemberFromGroup() {
        Group group = new Group("1", "adminId", List.of("adminId", "user1"), "code123");
        group.setMembers(List.of("adminId"));

        assertEquals(1, group.getMembers().size());
        assertFalse(group.getMembers().contains("user1"));
    }

    @Test
    void setGroupCode() {
        Group group = new Group();
        group.setCode("newCode");

        assertEquals("newCode", group.getCode());
    }

    @Test
    void setGroupAdmin() {
        Group group = new Group();
        group.setAdmin("newAdmin");

        assertEquals("newAdmin", group.getAdmin());
    }
}
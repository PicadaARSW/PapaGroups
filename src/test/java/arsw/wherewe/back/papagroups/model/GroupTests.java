package arsw.wherewe.back.papagroups.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GroupTests {

    @Test
    void createGroupWithValidData() {
        Group group = new Group("1", "adminId", "Amigos", List.of("adminId"), "code123", LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        assertEquals("1", group.getId());
        assertEquals("adminId", group.getAdmin());
        assertEquals("Amigos", group.getNameGroup());
        assertEquals(1, group.getMembers().size());
        assertEquals("adminId", group.getMembers().get(0));
        assertEquals("code123", group.getCode());
        assertNotNull(group.getLastCodeUpdate());
        assertNotNull(group.getNextCodeUpdate());
        assertTrue(group.getNextCodeUpdate().isAfter(group.getLastCodeUpdate()));
        assertEquals(3, group.getNextCodeUpdate().getDayOfYear() - group.getLastCodeUpdate().getDayOfYear());
    }

    @Test
    void createGroupWithEmptyConstructor() {
        Group group = new Group();
        group.setId("1");
        group.setAdmin("adminId");
        group.setMembers(List.of("adminId"));
        group.setCode("code123");
        group.setNameGroup("Amigos");
        group.setLastCodeUpdate(LocalDateTime.now());
        group.setNextCodeUpdate(LocalDateTime.now().plusDays(3));

        assertEquals("1", group.getId());
        assertEquals("adminId", group.getAdmin());
        assertEquals(1, group.getMembers().size());
        assertEquals("adminId", group.getMembers().get(0));
        assertEquals("code123", group.getCode());
        assertEquals("Amigos", group.getNameGroup());
        assertNotNull(group.getLastCodeUpdate());
        assertNotNull(group.getNextCodeUpdate());
    }

    @Test
    void addMemberToGroup() {
        Group group = new Group("1", "adminId", "Amigos",List.of("adminId"), "code123", LocalDateTime.now(), LocalDateTime.now().plusDays(3));
        group.setMembers(List.of("adminId", "user1"));

        assertEquals(2, group.getMembers().size());
        assertTrue(group.getMembers().contains("user1"));
    }

    @Test
    void removeMemberFromGroup() {
        Group group = new Group("1", "adminId", "Amigos",List.of("adminId", "user1"), "code123", LocalDateTime.now(), LocalDateTime.now().plusDays(3));
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

    @Test
    void setGroupName() {
        Group group = new Group();
        group.setNameGroup("newName");

        assertEquals("newName", group.getNameGroup());
    }

    @Test
    void setGroupMembers() {
        Group group = new Group();
        group.setMembers(List.of("member1", "member2"));

        assertEquals(2, group.getMembers().size());
        assertTrue(group.getMembers().contains("member1"));
        assertTrue(group.getMembers().contains("member2"));
    }

    @Test
    void setGroupId() {
        Group group = new Group();
        group.setId("newId");

        assertEquals("newId", group.getId());
    }

    @Test
    void setGroupLastCodeUpdate() {
        Group group = new Group();
        LocalDateTime now = LocalDateTime.now();
        group.setLastCodeUpdate(now);

        assertEquals(now, group.getLastCodeUpdate());
    }

    @Test
    void setGroupNextCodeUpdate() {
        Group group = new Group();
        LocalDateTime now = LocalDateTime.now();
        group.setNextCodeUpdate(now);

        assertEquals(now, group.getNextCodeUpdate());
    }
}
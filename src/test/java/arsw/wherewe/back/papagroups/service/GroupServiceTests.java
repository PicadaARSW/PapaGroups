package arsw.wherewe.back.papagroups.service;

import arsw.wherewe.back.papagroups.dto.GroupDTO;
import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GroupServiceTests {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGroupSuccessfully() {
        // Setup: Create a GroupDTO for input and a Group for the repository
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setAdmin("adminId");

        Group group = new Group();
        group.setAdmin("adminId");
        group.setCode("code123"); // Simulate the generated code
        group.setMembers(List.of("adminId")); // Set members as per service logic

        when(groupRepository.save(any(Group.class))).thenReturn(group);

        // Execute: Call the service method with GroupDTO
        GroupDTO createdGroupDTO = groupService.createGroup(groupDTO);

        // Verify: Check the returned GroupDTO
        assertNotNull(createdGroupDTO.getCode());
        assertEquals(1, createdGroupDTO.getMembers().size());
        assertEquals("adminId", createdGroupDTO.getMembers().get(0));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void getGroupsSuccessfully() {
        // Setup: Create a list of Group entities for the repository
        List<Group> groups = List.of(new Group(), new Group());

        when(groupRepository.findAll()).thenReturn(groups);

        // Execute: Call the service method
        List<GroupDTO> result = groupService.getGroups();

        // Verify: Check the returned list of GroupDTOs
        assertEquals(2, result.size());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void getGroupByIdSuccessfully() {
        // Setup: Create a Group entity for the repository
        Group group = new Group();
        group.setId("groupId");

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));

        // Execute: Call the service method
        GroupDTO result = groupService.getGroupById("groupId");

        // Verify: Check the returned GroupDTO
        assertNotNull(result);
        assertEquals("groupId", result.getId());
        verify(groupRepository, times(1)).findById("groupId");
    }

    @Test
    void getGroupByIdNotFound() {
        when(groupRepository.findById("groupId")).thenReturn(Optional.empty());

        // Execute: Call the service method
        GroupDTO result = groupService.getGroupById("groupId");

        // Verify: Check that the result is null
        assertNull(result);
        verify(groupRepository, times(1)).findById("groupId");
    }

    @Test
    void joinGroupUserAlreadyMember() {
        // Setup: Create a Group entity with a member
        Group group = new Group();
        group.setCode("code123");
        group.setMembers(List.of("user1"));

        when(groupRepository.findAll()).thenReturn(List.of(group));

        // Execute & Verify: Expect an exception when the user is already a member
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.joinGroup("code123", "user1");
        });

        assertEquals("El usuario ya pertenece al grupo", exception.getMessage());
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void joinGroupNotFound() {
        when(groupRepository.findAll()).thenReturn(List.of());

        // Execute & Verify: Expect an exception when the group is not found
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.joinGroup("code123", "user1");
        });

        assertEquals("El grupo no existe", exception.getMessage());
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void getGroupsByUserIdSuccessfully() {
        // Setup: Create Group entities with members
        Group group1 = new Group();
        group1.setMembers(List.of("user1", "user2"));
        Group group2 = new Group();
        group2.setMembers(List.of("user1"));
        Group group3 = new Group();
        group3.setMembers(List.of("user3"));

        when(groupRepository.findAll()).thenReturn(List.of(group1, group2, group3));

        // Execute: Call the service method
        List<GroupDTO> result = groupService.getGroupsByUserId("user1");

        // Verify: Check the returned list of GroupDTOs
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getMembers().contains("user1") && dto.getMembers().contains("user2")));
        assertTrue(result.stream().anyMatch(dto -> dto.getMembers().contains("user1") && dto.getMembers().size() == 1));
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void getGroupsByUserIdNoGroupsFound() {
        // Setup: Create Group entities with no matching members
        Group group1 = new Group();
        group1.setMembers(List.of("user2"));
        Group group2 = new Group();
        group2.setMembers(List.of("user3"));

        when(groupRepository.findAll()).thenReturn(List.of(group1, group2));

        // Execute: Call the service method
        List<GroupDTO> result = groupService.getGroupsByUserId("user1");

        // Verify: Check that the result is empty
        assertTrue(result.isEmpty());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void joinGroupSuccessfully() {
        // Setup: Create a Group entity with no members
        Group group = new Group();
        group.setCode("code123");
        group.setMembers(new ArrayList<>());

        // Simulate the updated group after adding a member
        Group updatedGroup = new Group();
        updatedGroup.setCode("code123");
        updatedGroup.setMembers(List.of("user1"));

        when(groupRepository.findAll()).thenReturn(List.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(updatedGroup);

        // Execute: Call the service method
        GroupDTO result = groupService.joinGroup("code123", "user1");

        // Verify: Check the returned GroupDTO
        assertNotNull(result);
        assertTrue(result.getMembers().contains("user1"));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void leaveAllGroupsSuccessfully() {
        Group group1 = new Group();
        group1.setAdmin("user1");
        group1.setMembers(new ArrayList<>(List.of("user1", "user2")));

        Group group2 = new Group();
        group2.setAdmin("user3");
        group2.setMembers(new ArrayList<>(List.of("user1", "user3")));

        when(groupRepository.findAll()).thenReturn(List.of(group1, group2));
        when(groupRepository.save(any(Group.class))).thenReturn(group1, group2);

        boolean result = groupService.leaveAllGroups("user1");

        assertTrue(result);
        assertFalse(group1.getMembers().contains("user1"));
        assertFalse(group2.getMembers().contains("user1"));
        verify(groupRepository, times(2)).save(any(Group.class));
    }

    @Test
    void leaveAllGroupsNoGroupsFound() {
        when(groupRepository.findAll()).thenReturn(List.of());

        boolean result = groupService.leaveAllGroups("user1");

        assertFalse(result);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void leaveAllGroupsAdminReassigned() {
        Group group = new Group();
        group.setAdmin("user1");
        group.setMembers(new ArrayList<>(List.of("user1", "user2", "user3")));

        when(groupRepository.findAll()).thenReturn(List.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        boolean result = groupService.leaveAllGroups("user1");

        assertTrue(result);
        assertFalse(group.getMembers().contains("user1"));
        assertNotEquals("user1", group.getAdmin());
        assertTrue(group.getMembers().contains(group.getAdmin()));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void leaveAllGroupsAdminReassignedWithSingleOtherMember() {
        Group group = new Group();
        group.setAdmin("user1");
        group.setMembers(new ArrayList<>(List.of("user1", "user2")));

        when(groupRepository.findAll()).thenReturn(List.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        boolean result = groupService.leaveAllGroups("user1");

        assertTrue(result);
        assertFalse(group.getMembers().contains("user1"));
        assertEquals("user2", group.getAdmin());
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void updateGroupCodesSuccessfully() {
        Group group1 = new Group();
        group1.setCode("oldCode1");
        group1.setLastCodeUpdate(LocalDateTime.now().minusDays(4));
        group1.setNextCodeUpdate(LocalDateTime.now().minusHours(1));

        Group group2 = new Group();
        group2.setCode("oldCode2");
        group2.setLastCodeUpdate(LocalDateTime.now().minusDays(3));
        group2.setNextCodeUpdate(LocalDateTime.now().plusHours(2));

        when(groupRepository.findAll()).thenReturn(List.of(group1, group2));
        when(groupRepository.save(any(Group.class))).thenReturn(group1, group2);

        groupService.updateGroupCodes();

        assertNotEquals("oldCode1", group1.getCode());
        assertTrue(group1.getLastCodeUpdate().isBefore(group1.getNextCodeUpdate()));
        assertTrue(group1.getNextCodeUpdate().isAfter(LocalDateTime.now()));

        assertEquals("oldCode2", group2.getCode());
        verify(groupRepository, times(1)).save(group1);
        verify(groupRepository, never()).save(group2);
    }

    @Test
    void updateGroupCodesNoGroupsFound() {
        when(groupRepository.findAll()).thenReturn(List.of());

        groupService.updateGroupCodes();

        verify(groupRepository, never()).save(any(Group.class));
    }



    @Test
    void expelMemberSuccessfully() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("adminId");
        group.setMembers(new ArrayList<>(List.of("adminId", "userId")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        GroupDTO result = groupService.expelMember("groupId", "userId", "adminId");

        assertNotNull(result);
        assertFalse(group.getMembers().contains("userId"));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void expelMemberFailsWhenNotAdmin() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("adminId");
        group.setMembers(new ArrayList<>(List.of("adminId", "userId")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));

        GroupDTO result = groupService.expelMember("groupId", "userId", "userId");

        assertNull(result);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void expelMemberFailsWhenUserNotInGroup() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("adminId");
        group.setMembers(new ArrayList<>(List.of("adminId")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));

        GroupDTO result = groupService.expelMember("groupId", "userId", "adminId");

        assertNull(result);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void expelMemberFailsWhenGroupNotFound() {
        when(groupRepository.findById("groupId")).thenReturn(Optional.empty());

        GroupDTO result = groupService.expelMember("groupId", "userId", "adminId");

        assertNull(result);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void leaveGroupSuccessfullyRemovesUser() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("adminId");
        group.setMembers(new ArrayList<>(List.of("adminId", "userId")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        boolean result = groupService.leaveGroup("groupId", "userId");

        assertTrue(result);
        assertFalse(group.getMembers().contains("userId"));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void leaveGroupSuccessfullyDeletesGroupWhenLastMember() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("userId");
        group.setMembers(new ArrayList<>(List.of("userId")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));

        boolean result = groupService.leaveGroup("groupId", "userId");

        assertTrue(result);
        verify(groupRepository, times(1)).deleteById("groupId");
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void leaveGroupSuccessfullyReassignsAdmin() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("adminId");
        group.setMembers(new ArrayList<>(List.of("adminId", "userId", "user2")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        boolean result = groupService.leaveGroup("groupId", "adminId");

        assertTrue(result);
        assertFalse(group.getMembers().contains("adminId"));
        assertNotEquals("adminId", group.getAdmin());
        assertTrue(group.getMembers().contains(group.getAdmin()));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void leaveGroupFailsWhenGroupNotFound() {
        when(groupRepository.findById("groupId")).thenReturn(Optional.empty());

        boolean result = groupService.leaveGroup("groupId", "userId");

        assertFalse(result);
        verify(groupRepository, never()).save(any(Group.class));
        verify(groupRepository, never()).deleteById(anyString());
    }

    @Test
    void leaveGroupFailsWhenUserNotInGroup() {
        Group group = new Group();
        group.setId("groupId");
        group.setAdmin("adminId");
        group.setMembers(new ArrayList<>(List.of("adminId", "user2")));

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));

        boolean result = groupService.leaveGroup("groupId", "userId");

        assertFalse(result);
        verify(groupRepository, never()).save(any(Group.class));
        verify(groupRepository, never()).deleteById(anyString());
    }
}
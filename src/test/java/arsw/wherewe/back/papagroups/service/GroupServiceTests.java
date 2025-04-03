package arsw.wherewe.back.papagroups.service;

import arsw.wherewe.back.papagroups.dto.GroupDTO;
import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}
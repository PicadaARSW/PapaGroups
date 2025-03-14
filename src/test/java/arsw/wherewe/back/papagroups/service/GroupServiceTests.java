package arsw.wherewe.back.papagroups.service;

import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GroupServiceTests {

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
        Group group = new Group();
        group.setAdmin("adminId");

        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group createdGroup = groupService.createGroup(group);

        assertNotNull(createdGroup.getCode());
        assertEquals(1, createdGroup.getMembers().size());
        assertEquals("adminId", createdGroup.getMembers().get(0));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void getGroupsSuccessfully() {
        List<Group> groups = List.of(new Group(), new Group());

        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> result = groupService.getGroups();

        assertEquals(2, result.size());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void getGroupByIdSuccessfully() {
        Group group = new Group();
        group.setId("groupId");

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));

        Group result = groupService.getGroupById("groupId");

        assertNotNull(result);
        assertEquals("groupId", result.getId());
        verify(groupRepository, times(1)).findById("groupId");
    }

    @Test
    void getGroupByIdNotFound() {
        when(groupRepository.findById("groupId")).thenReturn(Optional.empty());

        Group result = groupService.getGroupById("groupId");

        assertNull(result);
        verify(groupRepository, times(1)).findById("groupId");
    }



    @Test
    void joinGroupUserAlreadyMember() {
        Group group = new Group();
        group.setCode("code123");
        group.setMembers(List.of("user1"));

        when(groupRepository.findAll()).thenReturn(List.of(group));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.joinGroup("code123", "user1");
        });

        assertEquals("El usuario ya pertenece al grupo", exception.getMessage());
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void joinGroupNotFound() {
        when(groupRepository.findAll()).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.joinGroup("code123", "user1");
        });

        assertEquals("El grupo no existe", exception.getMessage());
        verify(groupRepository, never()).save(any(Group.class));
    }
}
package arsw.wherewe.back.papagroups.controller;

import arsw.wherewe.back.papagroups.dto.GroupDTO;
import arsw.wherewe.back.papagroups.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GroupControllerTests {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void createGroupSuccessfully() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setAdmin("adminId");

        when(groupService.createGroup(any(GroupDTO.class))).thenReturn(groupDTO);

        mockMvc.perform(post("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"admin\":\"adminId\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin").value("adminId"));

        verify(groupService, times(1)).createGroup(any(GroupDTO.class));
    }

    @Test
    void getGroupsSuccessfully() throws Exception {
        List<GroupDTO> groups = List.of(new GroupDTO(), new GroupDTO());

        when(groupService.getGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/v1/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(groupService, times(1)).getGroups();
    }

    @Test
    void getGroupByIdSuccessfully() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId("groupId");

        when(groupService.getGroupById("groupId")).thenReturn(groupDTO);

        mockMvc.perform(get("/api/v1/groups/groupId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("groupId"));

        verify(groupService, times(1)).getGroupById("groupId");
    }

    @Test
    void getGroupsByUserIdSuccessfully() throws Exception {
        GroupDTO group1 = new GroupDTO();
        group1.setMembers(List.of("user1", "user2"));
        GroupDTO group2 = new GroupDTO();
        group2.setMembers(List.of("user1"));

        when(groupService.getGroupsByUserId("user1")).thenReturn(List.of(group1, group2));

        mockMvc.perform(get("/api/v1/groups/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].members").isArray())
                .andExpect(jsonPath("$[1].members").isArray());

        verify(groupService, times(1)).getGroupsByUserId("user1");
    }

    @Test
    void getGroupsByUserIdNoGroupsFound() throws Exception {
        when(groupService.getGroupsByUserId("user1")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/groups/user/user1"))
                .andExpect(status().isNoContent());

        verify(groupService, times(1)).getGroupsByUserId("user1");
    }

    @Test
    void joinGroupSuccessfully() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId("groupId");

        when(groupService.joinGroup("groupCode", "userId")).thenReturn(groupDTO);

        mockMvc.perform(post("/api/v1/groups/join/groupCode/userId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("groupId"));

        verify(groupService, times(1)).joinGroup("groupCode", "userId");
    }

    @Test
    void joinGroupUserAlreadyInGroupOrGroupNotFound() throws Exception {
        when(groupService.joinGroup("groupCode", "userId")).thenReturn(null);

        mockMvc.perform(post("/api/v1/groups/join/groupCode/userId"))
                .andExpect(status().isBadRequest());

        verify(groupService, times(1)).joinGroup("groupCode", "userId");
    }
}
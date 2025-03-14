package arsw.wherewe.back.papagroups.controller;

import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GroupControllerTests {

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
        Group group = new Group();
        group.setAdmin("adminId");

        when(groupService.createGroup(any(Group.class))).thenReturn(group);

        mockMvc.perform(post("/api/v1/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"admin\":\"adminId\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin").value("adminId"));

        verify(groupService, times(1)).createGroup(any(Group.class));
    }

    @Test
    void getGroupsSuccessfully() throws Exception {
        List<Group> groups = List.of(new Group(), new Group());

        when(groupService.getGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/v1/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(groupService, times(1)).getGroups();
    }

    @Test
    void getGroupByIdSuccessfully() throws Exception {
        Group group = new Group();
        group.setId("groupId");

        when(groupService.getGroupById("groupId")).thenReturn(group);

        mockMvc.perform(get("/api/v1/groups/groupId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("groupId"));

        verify(groupService, times(1)).getGroupById("groupId");
    }








}
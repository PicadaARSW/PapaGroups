package arsw.wherewe.back.papagroups.controller;

import arsw.wherewe.back.papagroups.dto.GroupDTO;
import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Group controller class for group operations
 */
@RestController
@RequestMapping("/api/v1/groups")
@Tag(name = "Groups", description = "Group management API endpoints")
public class GroupController {


    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Create a new group", description = "Creates a new group with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group created successfully",
                    content = @Content(schema = @Schema(implementation = Group.class))),
            @ApiResponse(responseCode = "400", description = "Invalid group data provided")
    })
    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        GroupDTO newGroup = groupService.createGroup(groupDTO);
        if (newGroup == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newGroup);
    }

    @GetMapping
    @Operation(summary = "Get all groups", description = "Retrieves a list of all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of groups",
                    content = @Content(schema = @Schema(implementation = Group.class))),
            @ApiResponse(responseCode = "204", description = "No groups found")
    })
    public ResponseEntity<List<GroupDTO>> getGroups() {
        List<GroupDTO> groups = groupService.getGroups();
        if (!groups.isEmpty()) {
            return ResponseEntity.ok(groups);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get group by ID", description = "Retrieves a group by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found",
                    content = @Content(schema = @Schema(implementation = Group.class))),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable("id") String id) {
        GroupDTO group = groupService.getGroupById(id);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/join/{code}/{userId}")
    @Operation(summary = "Join a group", description = "Allows a user to join a group using a group code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully joined the group",
                    content = @Content(schema = @Schema(implementation = Group.class))),
            @ApiResponse(responseCode = "400", description = "User already in group or group not found")
    })
    public ResponseEntity<GroupDTO> joinGroup(@PathVariable("code") String code, @PathVariable("userId") String userId) {
        try {
            GroupDTO group = groupService.joinGroup(code, userId);
            if (group != null) {
                return ResponseEntity.ok(group);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (IllegalArgumentException e) {
            // include an error message
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get groups by user ID", description = "Retrieves all groups a user belongs to")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's groups",
                    content = @Content(schema = @Schema(implementation = Group.class))),
            @ApiResponse(responseCode = "204", description = "No groups found for the user")
    })
    public ResponseEntity<List<GroupDTO>> getGroupsByUserId(@PathVariable("userId") String userId) {
        List<GroupDTO> groups = groupService.getGroupsByUserId(userId);
        if (!groups.isEmpty()) {
            return ResponseEntity.ok(groups);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/leave-all/{userId}")
    @Operation(summary = "Remove user from all groups", description = "Removes a user from all groups they belong to, reassigning admin if necessary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully removed from all groups"),
            @ApiResponse(responseCode = "204", description = "User not found in any groups")
    })
    public ResponseEntity<Void> leaveAllGroups(@PathVariable("userId") String userId) {
        boolean removed = groupService.leaveAllGroups(userId);
        if (removed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

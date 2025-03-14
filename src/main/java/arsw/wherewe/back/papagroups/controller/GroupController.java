package arsw.wherewe.back.papagroups.controller;

import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @GetMapping
    public List<Group> getGroups() {
        return groupService.getGroups();
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable("id") String id) {
        return groupService.getGroupById(id);
    }

    @PostMapping("/join/{code}/{userId}")
    public Group joinGroup(@PathVariable("code") String code, @PathVariable("userId") String userId){
        return groupService.joinGroup(code, userId);
    }
}

package arsw.wherewe.back.papagroups.service;

import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {


    private GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group createGroup(Group group) {
        group.setCode(generateCode());
        group.setMembers(List.of(group.getAdmin()));
        return groupRepository.save(group);
    }

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(String id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group joinGroup(String code, String userId){
        Optional<Group> groupOptional = groupRepository.findAll().stream()
                .filter(g -> g.getCode().equals(code))
                .findFirst();

        if(groupOptional.isPresent()){
            Group group = groupOptional.get();
            if (!group.getMembers().contains(userId)){
                group.getMembers().add(userId);
                return groupRepository.save(group);
            }else {
                throw new IllegalArgumentException("El usuario ya pertenece al grupo");
            }
        }else {
            throw new IllegalArgumentException("El grupo no existe");
        }
    }

    private String generateCode(){
        return UUID.randomUUID().toString().substring(0, 6);
    }


    /**
     * Get all groups by userId
     * @param userId String user id
     */
    public List<Group> getGroupsByUserId(String userId) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getMembers().contains(userId))
                .toList();
    }

}
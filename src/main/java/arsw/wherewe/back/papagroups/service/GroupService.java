package arsw.wherewe.back.papagroups.service;

import arsw.wherewe.back.papagroups.dto.GroupDTO;
import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Group service class for group operations
 */
@Service
public class GroupService {


    private GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // Map Group to GroupDTO
    private GroupDTO toGroupDTO(Group group) {
        return new GroupDTO(
                group.getId(),
                group.getAdmin(),
                group.getNameGroup(),
                group.getMembers(),
                group.getCode()
        );
    }

    // Map GroupDTO to Group
    private Group toGroup(GroupDTO groupDTO) {
        return new Group(
                groupDTO.getId(),
                groupDTO.getAdmin(),
                groupDTO.getNameGroup(),
                groupDTO.getMembers(),
                groupDTO.getCode()
        );
    }

    public GroupDTO createGroup(GroupDTO groupDTO) {
        Group group = toGroup(groupDTO);
        group.setCode(generateCode());
        group.setMembers(List.of(group.getAdmin()));
        Group savedGroup = groupRepository.save(group);
        return toGroupDTO(savedGroup);
    }

    public List<GroupDTO> getGroups() {
        return groupRepository.findAll().stream()
                .map(this::toGroupDTO)
                .collect(Collectors.toList());
    }

    public GroupDTO getGroupById(String id) {
        Group group = groupRepository.findById(id).orElse(null);
        return group != null ? toGroupDTO(group) : null;
    }

    public GroupDTO joinGroup(String code, String userId) {
        Optional<Group> groupOptional = groupRepository.findAll().stream()
                .filter(g -> g.getCode().equals(code))
                .findFirst();

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            if (!group.getMembers().contains(userId)) {
                group.getMembers().add(userId);
                Group updatedGroup = groupRepository.save(group);
                return toGroupDTO(updatedGroup);
            } else {
                throw new IllegalArgumentException("El usuario ya pertenece al grupo");
            }
        } else {
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
    public List<GroupDTO> getGroupsByUserId(String userId) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getMembers().contains(userId))
                .map(this::toGroupDTO)
                .collect(Collectors.toList());
    }

}
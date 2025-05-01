package arsw.wherewe.back.papagroups.service;

import arsw.wherewe.back.papagroups.dto.GroupDTO;
import arsw.wherewe.back.papagroups.model.Group;
import arsw.wherewe.back.papagroups.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Group service class for group operations
 */
@Service
public class GroupService {

    private SecureRandom random = new SecureRandom();

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
                group.getCode(),
                group.getLastCodeUpdate(),
                group.getNextCodeUpdate()
        );
    }

    // Map GroupDTO to Group
    private Group toGroup(GroupDTO groupDTO) {
        return new Group(
                groupDTO.getId(),
                groupDTO.getAdmin(),
                groupDTO.getNameGroup(),
                groupDTO.getMembers(),
                groupDTO.getCode(),
                groupDTO.getLastCodeUpdate(),
                groupDTO.getNextCodeUpdate()
        );
    }

    public GroupDTO createGroup(GroupDTO groupDTO) {
        Group group = toGroup(groupDTO);
        String newCode = generateCode();
        LocalDateTime now = LocalDateTime.now();
        group.setCode(newCode);
        group.setMembers(List.of(group.getAdmin()));
        group.setLastCodeUpdate(now);
        group.setNextCodeUpdate(now.plusHours(48));
        Group savedGroup = groupRepository.save(group);
        return toGroupDTO(savedGroup);
    }

    public List<GroupDTO> getGroups() {
        return groupRepository.findAll().stream()
                .map(this::toGroupDTO)
                .toList();
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
                .toList();
    }

    /**
     * Leave all groups for a user
     * @param userId String user id
     * @return boolean true if the user left all groups, false if no groups were found
     */
    public boolean leaveAllGroups(String userId) {
        List<Group> userGroups = groupRepository.findAll().stream()
                .filter(g -> g.getMembers().contains(userId))
                .toList();

        if (userGroups.isEmpty()) {
            return false; // No groups found for the user
        }

        for (Group group: userGroups) {
            if (isAdmin(userId, group) && group.getMembers().size() > 1) {
                reassignAdmin(group, userId);
            } else if (isAdmin(userId, group) && group.getMembers().size() == 1) {
                groupRepository.deleteById(group.getId());
                continue;
            }
            group.getMembers().remove(userId);
            groupRepository.save(group);
        }
        return true;
    }

    /**
     * Check if the user is admin of the group
     * @param userId String user id
     * @param group Group
     * @return boolean true if the user is admin of the group, false otherwise
     */
    private boolean isAdmin(String userId, Group group) {
        return group.getAdmin().equals(userId);
    }

    /**
     * Check if the user is a member of the group
     * @param userId
     * @param group
     * @return boolean true if the user is a member of the group, false otherwise
     */
    private boolean isMember(String userId, Group group) {
        return group.getMembers().contains(userId);
    }

    /**
     * Reassign aleatory the admin of the group
     * @param group Group
     */
    private void reassignAdmin(Group group, String userId) {
        List<String> otherMembers = group.getMembers().stream()
                .filter(member -> !member.equals(userId))
                .toList();
        String newAdmin = otherMembers.get(random.nextInt(otherMembers.size()));
        group.setAdmin(newAdmin);
    }

    /**
     * Leave a specific group for a user
     * @param groupId String group id
     * @param userId String user id
     * @return GroupDTO if the user left the group, null if group or user not found
     */
    public boolean leaveGroup(String groupId, String userId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            if (group.getMembers().contains(userId)) {
                if (isAdmin(userId, group) && group.getMembers().size() > 1) {
                    reassignAdmin(group, userId);
                }
                group.getMembers().remove(userId);
                if (group.getMembers().isEmpty()) {
                    groupRepository.deleteById(group.getId());
                    return true;
                }
                groupRepository.save(group);
                return true;
            }
        }
        return false;
    }

    /**
     * Expel a member from a group (Only admin can do this)
     * @param groupId String group id
     * @param userId String user id to expel
     * @param adminId String admin id
     * @return GroupDTO if the user was expelled, null if group, user or admin not valid
     */
    public GroupDTO expelMember(String groupId, String userId, String adminId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            if(isAdmin(adminId, group) && isMember(userId, group) && !isAdmin(userId, group)) {
                group.getMembers().remove(userId);
                Group updatedGroup = groupRepository.save(group);
                return toGroupDTO(updatedGroup);
            }
        }
        return null;
    }

    /**
     * Scheduled task to update group codes every hour
     * This method checks all groups and updates their codes if the current time is after the next code update time.
     */
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void updateGroupCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<Group> allGroups = groupRepository.findAll();
        for (Group group : allGroups) {
            if (now.isAfter(group.getNextCodeUpdate())) {
                group.setCode(generateCode());
                group.setLastCodeUpdate(now);
                group.setNextCodeUpdate(now.plusHours(48));
                groupRepository.save(group);
            }
        }
    }

}
package arsw.wherewe.back.papagroups.repository;

import arsw.wherewe.back.papagroups.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {


}

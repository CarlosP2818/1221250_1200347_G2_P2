package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.UserJpa;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.Optional;

@Component
public class UserJpaMapper {

    private SpringDataUserRepository repo;
    private IdGenerator idGenerator;

    @Autowired
    public UserJpaMapper(SpringDataUserRepository injectedRepo, IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
        this.repo = injectedRepo;
    }

    public User toDomain(UserJpa jpa) {
        if (jpa == null) return null;

        User user = User.fromEncodedPassword(jpa.getUsername(), jpa.getPassword());
        user.setEnabled(jpa.isEnabled());

        if (jpa.getName() != null)
            user.setName(jpa.getName().getName());

        if (jpa.getAuthorities() != null && !jpa.getAuthorities().isEmpty())
            jpa.getAuthorities().stream()
                    .filter(Role.class::isInstance) // garante que é Role
                    .map(Role.class::cast)          // faz o cast
                    .forEach(user::addAuthority);;

        return user;
    }

    public UserJpa toJpa(User user) {
        if (user == null) return null;

        if (repo != null) {
            Optional<UserJpa> existing = repo.findByUsername(user.getUsername());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        UserJpa jpa = new UserJpa(idGenerator.generateId(), user.getUsername(), user.getPassword());
        jpa.setEnabled(user.isEnabled());

        if (user.getName() != null) {
            jpa.setName(user.getName().getName());
        }

        if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
            for (Role r : user.getAuthorities()) {
                jpa.addAuthority(r);
            }
        }

        return jpa;
    }
}

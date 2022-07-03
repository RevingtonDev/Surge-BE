package dev.revington;

import dev.revington.entity.User;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.util.Roles;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static volatile int ID = 0;

    @Autowired
    UserRepository repo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ID = repo.findAllByIdSortDescending().get(0).get_id() + 1;

        if(!repo.findByEmail("root@surge.com").isEmpty())
            return;

        User root = new User();
        root.setId(ID++);
        root.setFirstName("root");
        root.setLastName("user");
        root.setEmail("root@surge.com");
        root.setPassword(Crypto.getMD5("root"));
        root.setStatus(true);
        root.setTemporary(false);
        root.setAccountType(Roles.ADMIN.label);
        repo.save(root);
    }
}

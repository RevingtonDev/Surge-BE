package dev.revington;

import dev.revington.entity.User;
import dev.revington.repo.UserRepository;
import dev.revington.security.Crypto;
import dev.revington.variables.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

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
        List<User> users = repo.findAllByIdSortDescending();
        ID = users.isEmpty() ? 0 : users.get(0).getId() + 1;

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
        root.setAccountType(Parameter.ADMIN);
        repo.save(root);
    }
}

package org.productsstore.products.repositories;

import org.productsstore.products.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailEquals(String email);

    User save(User user);
}

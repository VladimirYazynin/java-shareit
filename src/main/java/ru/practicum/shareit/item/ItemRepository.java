package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long id);

    Item save(Item item);

    Collection<Item> findByOwnerId(Long userId);

    Collection<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String searchInName,
            String searchInDescription
    );

}

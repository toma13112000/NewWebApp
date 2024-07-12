package org.example.repository;

import org.example.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    // Дополнительные методы для поиска объявлений по заголовку
    List<Advertisement> findByTitleContaining(String title);

    // Дополнительные методы для поиска объявлений по типу
    List<Advertisement> findByType(String type);
}

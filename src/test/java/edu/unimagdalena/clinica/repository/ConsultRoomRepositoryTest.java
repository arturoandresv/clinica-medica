package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.ConsultRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultRoomRepositoryTest {

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    @Test
    void shouldSaveAndFinConsultRoom() {
        ConsultRoom consultRoom = ConsultRoom.builder()
                .name("Consult Room A")
                .floor(2)
                .description("First room for consults")
                .build();

        ConsultRoom saved = consultRoomRepository.save(consultRoom);
        Optional<ConsultRoom> result = consultRoomRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Consult Room A", result.get().getName());
        assertEquals(2, result.get().getFloor());
    }

    @Test
    void shouldFindAllConsultRooms() {
        ConsultRoom room1 = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consult Room A")
                .floor(2)
                .description("First room for consults")
                .build());

        ConsultRoom room2 = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consult Room B")
                .floor(2)
                .description("Second room for consults")
                .build());

        List<ConsultRoom> result = consultRoomRepository.findAll();

        assertFalse(result.isEmpty());
        assertTrue(result.contains(room1));
        assertTrue(result.contains(room2));
    }

    @Test
    void shouldUpdateConsultRoom() {
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consult Room A")
                .floor(2)
                .description("First room for consults")
                .build());

        room.setName("Consult Room Z");
        room.setFloor(4);
        room.setDescription("Last room for consults");

        ConsultRoom updated = consultRoomRepository.save(room);

        assertEquals("Consult Room Z", updated.getName());
        assertEquals(4, updated.getFloor());
    }

    @Test
    void shouldDeleteConsultRoom() {
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consult Room A")
                .floor(2)
                .description("First room for consults")
                .build());

        Long id = room.getId();
        consultRoomRepository.deleteById(id);

        assertFalse(consultRoomRepository.findById(id).isPresent());
    }

}
package org.example.pensionatkademina.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private RoomType type;
    private RoomSize size;

    @OneToMany(mappedBy = "room")
    private List<Booking> booking;

}



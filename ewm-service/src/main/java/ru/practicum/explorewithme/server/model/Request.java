package ru.practicum.explorewithme.server.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explorewithme.server.model.enums.RequestState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    @Column
    private LocalDateTime created;
    @Column
    @Enumerated(EnumType.STRING)
    private RequestState status;
}

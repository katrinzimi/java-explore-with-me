package ru.practicum.explorewithme.server.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explorewithme.server.model.enums.EventState;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String annotation;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Embedded
    private Location location;

    @Column()
    private boolean paid;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Column()
    @Builder.Default
    private boolean requestModeration = true;

    @JoinColumn(name = "initiator_id")
    @ManyToOne
    private User initiator;

//    @Column
//    private Integer views;

    @Column()
    private LocalDateTime createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;
}

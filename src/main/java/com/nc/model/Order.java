package com.nc.model;

import com.nc.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "hardware_id")
    private Hardware hardware;
    @Column(name = "count")
    private int countOrder;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Order() {
    }

    public Order(Person person, Hardware hardware, int countOrder, Status status) {
        this.person = person;
        this.hardware = hardware;
        this.countOrder = countOrder;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", person_id=" + person.getId() +
                ", hardware_id=" + hardware.getId() +
                ", countOrder=" + countOrder +
                ", status=" + status.toString() +
                '}';
    }
}

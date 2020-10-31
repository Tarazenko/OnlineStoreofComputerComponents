package com.nc.service.Impl;

import com.nc.enums.Status;
import com.nc.model.Hardware;
import com.nc.model.Order;
import com.nc.model.Person;
import com.nc.repository.OrderRepository;
import com.nc.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final static Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);
    @Autowired
    OrderRepository dao;
    @Autowired
    HardwareServiceImpl hardwareService;
    @Autowired
    PersonServiceImpl personService;

    @Override
    public List<Order> findAll() {
        LOGGER.info("Taking data from the database (All orders)");
        return dao.findAll();
    }

    @Override
    public Order findById(long id) {
        LOGGER.info("Taking data from the database (Order by ID)");
        return dao.findById(id);
    }

    @Override
    public List<Order> findByPerson_Id(long id) {
        LOGGER.info("Taking data from the database (Orders by user ID)");
        return dao.findByPerson_Id(id);
    }

    @Override
    public List<Order> findByPerson(Person person) {
        LOGGER.info("Taking data from the database (Orders for the user)");
        return dao.findByPerson(person);
    }

    @Override
    public void save(Order order) {
        LOGGER.info("Writing order data to the database\n" + order.toString());
        dao.save(order);
    }

    @Override
    public void update(Order order) {
        Order oldOrder = dao.findById(order.getId());
        order.setHardware(oldOrder.getHardware());
        order.setCountOrder(oldOrder.getCountOrder());
        order.setPerson(oldOrder.getPerson());
        LOGGER.info("Updating order data in the database\n" + order.toString());
        dao.save(order);
    }

    @Override
    public void delete(long id) {
        Order order = dao.findById(id);
        if (order.getStatus().equals(Status.IN_PROCESSING)) {
            Hardware hardware = hardwareService.findById(order.getHardware().getId());
            hardware.setCountHardware(hardware.getCountHardware() + order.getCountOrder());
            hardwareService.save(hardware);
        }
        LOGGER.info("Removing order data from the database\n" + order.toString());
        dao.deleteById(id);
    }

    @Override
    public boolean createOneOrderForAll(long idHardware, int count) {
        Hardware hardware = hardwareService.findById(idHardware);
        Person person = personService.findAuthenticationPerson();
        Order orderExist = dao.findByHardwareAndPersonAndStatus(hardware, person, Status.IN_CART);
        if (orderExist != null && orderExist.getCountOrder() >= hardware.getCountHardware())
            return false;
        if (hardware.getCountHardware() == 0) {
            return false;
        } else {
            if (orderExist != null) {
                orderExist.setCountOrder(orderExist.getCountOrder() + count);
                LOGGER.info("Updating order data in the database\n" + orderExist.toString());
                dao.save(orderExist);
            } else {
                Order order = new Order(person, hardware, count, Status.IN_CART);
                LOGGER.info("Writing order data to the database\n" + order.toString());
                dao.save(order);
            }
            return true;
        }
    }

    @Override
    public double createTotalCost(List<Order> orders) {
        LOGGER.info("Finding the total amount of the order.");
        return orders.stream()
                .mapToDouble(item -> item.getHardware().getPriceHardware() * item.getCountOrder()).sum();
    }

    @Override
    @Transactional
    public void deleteAllByPersonId(long id) {
        LOGGER.info("Delete orders by user ID");
        dao.deleteAllByPersonId(id);
    }

    @Override
    public boolean checkoutOrdersForUser() {
        boolean isAllValidate = true;
        Person person = personService.findAuthenticationPerson();
        List<Order> orders = dao.findByPersonAndStatus(person, Status.IN_CART);
        orders.forEach(order -> order.setStatus(Status.IN_PROCESSING));
        orders.forEach(order -> {
            if (order.getCountOrder() <= order.getHardware().getCountHardware())
                order.getHardware()
                        .setCountHardware(order.getHardware().getCountHardware() - order.getCountOrder());
            else
                order.setCountOrder(0);
        });
        List<Order> deleteOrders = orders.stream().filter(order -> order.getCountOrder() == 0).collect(Collectors.toList());
        if (deleteOrders.size() != 0) {
            dao.deleteAll(deleteOrders);
            isAllValidate = false;
        }
        orders.removeIf(order -> order.getCountOrder() == 0);
        List<Hardware> hardwares = orders.stream().map(Order::getHardware).collect(Collectors.toList());
        hardwareService.saveAll(hardwares);
        LOGGER.info("Updating order data in the database");
        dao.saveAll(orders);
        return isAllValidate;
    }

    @Override
    public void changeStatusOnDelivered(long id) {
        Order order = dao.findById(id);
        order.setStatus(Status.DELIVERED);
        LOGGER.info("Updating order data in the database");
        dao.save(order);
    }

    @Override
    public List<Order> findByPersonAndStatus(Person person, Status status) {
        LOGGER.info("Taking data from the database (Orders for a specific user and status)");
        return dao.findByPersonAndStatus(person, status);
    }
}

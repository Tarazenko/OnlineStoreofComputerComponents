package com.nc.service.Impl;

import com.nc.enums.Role;
import com.nc.enums.Status;
import com.nc.model.Hardware;
import com.nc.model.Order;
import com.nc.model.Person;
import com.nc.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private HardwareServiceImpl hardwareService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Person person;
    private Order order;
    private Hardware hardware;

    @Before
    public void init() {
        person = new Person();
        person.setActive(false);
        person.setId(1);
        person.setNamePerson("Ilya");
        person.setSurnamePerson("Tarasenko");
        person.setLoginPerson("ilysha");
        person.setRole(Role.USER);



        hardware = new Hardware();
        hardware.setCountHardware(3);
        hardware.setName("Compik");
        hardware.setPriceHardware(20);

        order = new Order();
        order.setHardware(hardware);
        order.setPerson(person);
        order.setId(1);
        order.setStatus(Status.IN_CART);
        order.setCountOrder(3);
    }

    @Test
    public void changeStatusOnDeliveredChangingStatus() {
        doReturn(order).when(orderRepository).findById(1);
        orderService.changeStatusOnDelivered(1);
        assertThat(order.getStatus(),is(Status.DELIVERED));
        verify(orderRepository).save(any());
    }

    @Test
    public void createTotalCostTest() {
        double totalPrice = orderService.createTotalCost(Arrays.asList(order));
        double expectPrice = 60;
        assertEquals(expectPrice,totalPrice,2);
    }

    @Test
    public void deleteStatusInProgress() {
        order.setStatus(Status.IN_PROCESSING);
        doReturn(hardware).when(hardwareService).findById(anyLong());
        doReturn(order).when(orderRepository).findById(anyLong());
        orderService.delete(1);
        verify(hardwareService).save(hardware);
        verify(orderRepository,atLeastOnce()).deleteById(any());
    }

    @Test
    public void deleteStatusInCart() {
        doReturn(hardware).when(hardwareService).findById(anyLong());
        doReturn(order).when(orderRepository).findById(anyLong());
        orderService.delete(1);
        verify(hardwareService, never()).save(hardware);
        verify(orderRepository,atLeastOnce()).deleteById(any());
    }
}
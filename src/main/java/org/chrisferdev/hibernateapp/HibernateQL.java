package org.chrisferdev.hibernateapp;

import com.mysql.cj.xdevapi.Client;
import jakarta.persistence.EntityManager;
import org.chrisferdev.hibernateapp.dominio.ClienteDto;
import org.chrisferdev.hibernateapp.entity.Cliente;
import org.chrisferdev.hibernateapp.util.JpaUtil;

import java.util.List;

public class HibernateQL {
    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();
        System.out.println("========= consultar todos ==========");
        List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("======== consulta por id ========");
        Cliente cliente = em.createQuery("SELECT c FROM Cliente c WHERE c.id=:id", Cliente.class)
                .setParameter("id", 1L)
                .getSingleResult();
        System.out.println(cliente);

        System.out.println("====== consulta solo el nombre por id ======");
        String nombreCliente = em.createQuery("SELECT c.nombre FROM Cliente c WHERE c.id=:id", String.class)
                .setParameter("id", 2L)
                .getSingleResult();
        System.out.println(nombreCliente);

        System.out.println("=========consulta por campos personalizados =========");
        Object[] objectoCliente = em.createQuery("SELECT c.id, c.nombre, c.apellido FROM Cliente c WHERE c.id=:id", Object[].class)
                .setParameter("id", 1L)
                .getSingleResult();
        Long id = (Long) objectoCliente[0];
        String nombre = (String) objectoCliente[1];
        String apellido = (String) objectoCliente[2];
        System.out.println("id" + id + ",nombre=" + nombre + ",apellido=" + apellido);

        System.out.println("=========consulta por campos personalizados lista =========");
        List<Object[]> registros = em.createQuery("SELECT c.id, c.nombre, c.apellido FROM Cliente c", Object[].class)
                .getResultList();
//        for(Object[] reg : registros){
        registros.forEach(reg -> {
            Long idCli = (Long) reg[0];
            String nombreCli = (String) reg[1];
            String apellidoCli = (String) reg[2];
            System.out.println("id" + idCli + ",nombre=" + nombreCli + ",apellido=" + apellidoCli);
        });
//        }

        System.out.println("======== consulta por cliente y forma de pago ========");
        registros = em.createQuery("SELECT c, c.formaPago FROM Cliente c", Object[].class)
                        .getResultList();
        registros.forEach(reg -> {
            Cliente c = (Cliente) reg[0];
            String formaPago = (String) reg[1];
            System.out.println("formaPago=" + formaPago + "," + c);
        });

        System.out.println("===== consulta que puebla y devuelve objeto entity de una clase personalizada");
        clientes = em.createQuery("SELECT NEW Cliente(c.nombre, c.apellido) FROM Cliente c", Cliente.class)
                        .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("===== consulta que puebla y devuelve objeto de una clase personalizada");
        List<ClienteDto> clientesDto = em.createQuery("SELECT NEW org.chrisferdev.hibernateapp.dominio.ClienteDto(c.nombre, c.apellido) FROM Cliente c", ClienteDto.class)
                .getResultList();
        clientesDto.forEach(System.out::println);

        System.out.println("====== consulta con nombres de clientes =======");
        List<String> nombres = em.createQuery("SELECT c.nombre FROM Cliente c", String.class)
                        .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("====== consulta con nombres unicos de clientes ======");
        nombres = em.createQuery("SELECT DISTINCT(c.nombre) FROM Cliente c", String.class)
                        .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("======= consulta con formas de pago unicas");
        List<String> formasPago = em.createQuery("SELECT DISTINCT(c.formaPago) FROM Cliente c", String.class)
                        .getResultList();
        formasPago.forEach(System.out::println);

        System.out.println("======= consulta con numero de formas de pago unicas");
        Long totalFormasPago = em.createQuery("SELECT COUNT(DISTINCT(c.formaPago)) FROM Cliente c", Long.class)
                .getSingleResult();
        System.out.println(totalFormasPago);

        em.close();
    }
}

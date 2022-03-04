package fr.formation.inti.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.inti.entity.Employee;

public interface EmployeDao extends JpaRepository<Employee, Integer>{

}

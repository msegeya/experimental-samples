package org.farrukh.experiments.spring.orm.hibernate3.dao;

import org.farrukh.experiments.spring.orm.dao.IEmployeeDao;
import org.farrukh.experiments.spring.orm.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao extends AbstractHibernateDao<Employee> implements IEmployeeDao {
    
    public EmployeeDao() {
        super(Employee.class);
    }

}

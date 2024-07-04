package com.hzjy.hzjycheckIn.config;

import com.hzjy.hzjycheckIn.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeContext {

    private static final ThreadLocal<Employee> EMPLOYEE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setEmployee(Employee employee) {
        EMPLOYEE_THREAD_LOCAL.set(employee);
    }

    public static Employee getEmployee() {
        return EMPLOYEE_THREAD_LOCAL.get();
    }

    public static void removeUser() {
        EMPLOYEE_THREAD_LOCAL.remove();
    }


}

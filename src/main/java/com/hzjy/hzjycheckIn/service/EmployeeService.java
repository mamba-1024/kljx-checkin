package com.hzjy.hzjycheckIn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzjy.hzjycheckIn.dto.AuditRejectDTO;
import com.hzjy.hzjycheckIn.dto.EmployeeMonthTotalTO;
import com.hzjy.hzjycheckIn.dto.MonthExportDTO;
import com.hzjy.hzjycheckIn.dto.backend.EmployeeAttendanceVO;
import com.hzjy.hzjycheckIn.dto.query.EmployeeQueryQO;
import com.hzjy.hzjycheckIn.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  员工服务接口
 * </p>
 *
 * @author ketong
 * @since 2023-04-02 11:03:55
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 根据微信OpenID获取员工信息
     * 
     * @param openid 微信OpenID
     * @return 员工信息，如果不存在返回null
     */
    Employee getEmployeeByOpenId(String openid);

    /**
     * 根据手机号获取员工信息
     * 
     * @param phone 员工手机号
     * @return 员工信息，如果不存在返回null
     */
    Employee getEmployeeByPhone(String phone);

    /**
     * 根据Token获取员工信息
     * 
     * @param token 用户Token
     * @return 员工信息，如果不存在返回null
     */
    Employee getEmployeeByToken(String token);

    /**
     * 分页查询员工列表
     * 
     * @param employeeQueryQO 员工查询条件对象
     * @return 分页员工列表
     */
    Page<Employee> listEmployee(EmployeeQueryQO employeeQueryQO);

    /**
     * 启用员工
     * 
     * @param id 员工ID
     * @return 操作是否成功
     */
    Boolean enable(int id);

    /**
     * 停用员工
     * 
     * @param id 员工ID
     * @return 操作是否成功
     */
    Boolean disable(int id);

    /**
     * 员工离职
     * 
     * @param id 员工ID
     * @return 操作是否成功
     */
    Boolean resign(int id);

    /**
     * 员工在职
     * 
     * @param id 员工ID
     * @return 操作是否成功
     */
    Boolean incumbency(int id);

    /**
     * 清空员工积分
     * 
     * @param id 员工ID
     * @return 操作是否成功
     */
    Boolean clearPoints(int id);

    /**
     * 审核通过员工
     * 
     * @param id 员工ID
     * @return 操作是否成功
     */
    Boolean auditPass(int id);

    /**
     * 审核拒绝员工
     * 
     * @param auditRejectDTO 审核拒绝信息
     * @return 操作是否成功
     */
    Boolean auditReject(AuditRejectDTO auditRejectDTO);

    /**
     * 分页查询待审核员工列表
     * 
     * @param employeeQueryQO 员工查询条件对象
     * @return 分页待审核员工列表
     */
    Page<Employee> listEmployeeAuditList(EmployeeQueryQO employeeQueryQO);

    /**
     * 查询昨日员工统计数据
     * 包括：昨日启用员工数、昨日停用员工数、昨日新增员工数
     * 
     * @return 员工统计信息Map
     */
    Map<String,Long> selectLastDayCount();

    /**
     * 查询积分排名前十的员工
     * 
     * @return 积分排名前十的员工列表
     */
    List<Employee> selectBig10();

    /**
     * 统计待审核员工数量
     * 
     * @return 待审核员工数量
     */
    Integer countWaitAudit();

    /**
     * 查询员工等级分布统计
     * 
     * @return 等级分布统计信息
     */
    List<Map<String, Object>> queryLevelCount();

    /**
     * 查询员工月度统计信息
     * 
     * @param monthExportDTO 月度查询条件
     * @return 员工月度统计信息列表
     */
    List<EmployeeMonthTotalTO> queryMonth(MonthExportDTO monthExportDTO);
}

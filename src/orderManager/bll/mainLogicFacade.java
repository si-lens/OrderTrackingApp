package orderManager.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import orderManager.be.DepartmentTask;
import orderManager.be.IDepartment;
import orderManager.be.IOrder;
import orderManager.be.IProductionOrder;
import orderManager.be.IWorker;

public interface mainLogicFacade
{

    void changeStatus(IProductionOrder prodOrd, IDepartment department) throws SQLException;

    List<DepartmentTask> getDepartmentTaskByOrderNumber(IOrder order) throws SQLException, ParseException;

    List<IDepartment> getDepartments();

    List<IProductionOrder> getProducionOrdersByDepartment(IDepartment department) throws SQLException, ParseException;

    List<IWorker> getWorkers() throws SQLException;

    void readFile(String path) throws IOException, SQLException;

    void setDepartments() throws SQLException;
    
}

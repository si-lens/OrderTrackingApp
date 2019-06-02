package orderManager.bll;

import orderManager.be.*;
import orderManager.dal.availableWorkersDAO;
import orderManager.dal.jsonReaderMK2;
import orderManager.dal.productionOrdersDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Observable;

public class mainLogicClass extends Observable implements mainLogicFacade
{

    private availableWorkersDAO awDAO;
    private productionOrdersDAO pDAO;
    private List<IDepartment> departments;

    public mainLogicClass() throws SQLException
    {
        awDAO = new availableWorkersDAO();
        pDAO = new productionOrdersDAO();
        setDepartments();
    }

    @Override
    public void readFile(String path) throws IOException, SQLException
    {
        jsonReaderMK2.readFile(path);
    }

    @Override
    public List<IWorker> getWorkers() throws SQLException
    {
        return awDAO.getWorkers();
    }

    @Override
    public void setDepartments() throws SQLException
    {
        departments = pDAO.getDepartments();
    }

    @Override
    public List<DepartmentTask> getDepartmentTaskByOrderNumber(IOrder order) throws SQLException, ParseException
    {
        return pDAO.getDepartmentTasksByOrderNumber(order);
    }

    @Override
    public List<IDepartment> getDepartments()
    {
        return departments;
    }

    @Override
    public List<IProductionOrder> getProducionOrdersByDepartment(IDepartment department) throws SQLException, ParseException
    {
        List<IProductionOrder> poList = pDAO.getDepartmentContent(department);
        for (IProductionOrder po : poList)
        {
            for (int i = 0; i < po.getDepartmentTasks().size(); i++)
            {
                IDepartmentTask depFirst;
                IDepartmentTask depSecond;
                if (i == 0)
                {
                    depFirst = null;
                    depSecond = po.getDepartmentTasks().get(i);
                } else
                {
                    depFirst = po.getDepartmentTasks().get(i - 1);
                    depSecond = po.getDepartmentTasks().get(i);
                }
                setColorsForProgressBar(depFirst, depSecond, false);
            }
        }
        return poList;
    }

    @Override
    public void changeStatus(IProductionOrder prodOrd, IDepartment department) throws SQLException
    {
        pDAO.changeStatus(prodOrd, department);
    }

    public void setColorsForProgressBar(IDepartmentTask depFirst, IDepartmentTask depSecond, boolean isTest) throws ParseException
    {
        if (!isTest)
        {
            if (depFirst != null)
            {
                if (depSecond.getFinishedOrder() && depFirst.getProgressBar().getStatus() != CustomProgressBar.Status.BEHIND)
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.DONE);
                } else if (getBeforeNow(depSecond) && (depFirst.getProgressBar().getStatus() != CustomProgressBar.Status.BEHIND && depFirst.getProgressBar().getStatus() != CustomProgressBar.Status.NOT_STARTED) && !depSecond.getFinishedOrder())
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.ALL_GOOD);
                } else if (getBeforeNow(depSecond) && (depFirst.getProgressBar().getStatus() == CustomProgressBar.Status.BEHIND || depFirst.getProgressBar().getStatus() == CustomProgressBar.Status.NOT_STARTED) && !depSecond.getFinishedOrder())
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.NOT_STARTED);
                } else if (!getBeforeNow(depSecond) && !depSecond.getFinishedOrder())
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.BEHIND);
                }
            } else
            {
                if (depSecond.getFinishedOrder())
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.DONE);
                } else if (getBeforeNow(depSecond) && !depSecond.getFinishedOrder())
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.ALL_GOOD);
                } else if (!getBeforeNow(depSecond) && !depSecond.getFinishedOrder())
                {
                    depSecond.setProgressBar(CustomProgressBar.Status.BEHIND);
                }
            }
        } else if (isTest)
        {
            if (depFirst != null)
            {
                if (depSecond.getFinishedOrder() && depFirst.getProgressBar().getStatus() != CustomProgressBar.Status.BEHIND)
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.DONE);
                } else if (getBeforeNow(depSecond) && (depFirst.getProgressBar().getStatus() != CustomProgressBar.Status.BEHIND && depFirst.getProgressBar().getStatus() != CustomProgressBar.Status.NOT_STARTED) && !depSecond.getFinishedOrder())
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.ALL_GOOD);
                } else if (getBeforeNow(depSecond) && (depFirst.getProgressBar().getStatus() == CustomProgressBar.Status.BEHIND || depFirst.getProgressBar().getStatus() == CustomProgressBar.Status.NOT_STARTED) && !depSecond.getFinishedOrder())
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.NOT_STARTED);
                } else if (!getBeforeNow(depSecond) && !depSecond.getFinishedOrder())
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.BEHIND);
                }
            } else
            {
                if (depSecond.getFinishedOrder())
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.DONE);
                } else if (getBeforeNow(depSecond) && !depSecond.getFinishedOrder())
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.ALL_GOOD);
                } else if (!getBeforeNow(depSecond) && !depSecond.getFinishedOrder())
                {
                    depSecond.setTestProgressBar(CustomProgressBar.Status.BEHIND);
                }
            }
        }
    }

    private boolean getBeforeNow(IDepartmentTask dt)
    {
        return LocalDate.now().isBefore(dt.getEndDate().toLocalDate());
    }
}

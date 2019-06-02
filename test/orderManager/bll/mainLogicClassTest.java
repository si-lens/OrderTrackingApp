package orderManager.bll;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import orderManager.be.CustomProgressBar.Status;
import orderManager.be.Department;
import orderManager.be.DepartmentTask;
import orderManager.be.IDepartment;
import orderManager.be.IDepartmentTask;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class mainLogicClassTest
{
    ArrayList<IDepartmentTask> depTask;
    mainLogicClass instance;
    Status expResult;
    Status result;
    
    public mainLogicClassTest()
    {        
        IDepartment dep1 = new Department("Halvfab");
        IDepartment dep2 = new Department("Bertel");
        IDepartment dep3 = new Department("Baelg");
        IDepartment dep4 = new Department("Montage 1");
        IDepartment dep5 = new Department("Montage 2");
        
        Date date0 = Date.valueOf(LocalDate.of(2019, Month.APRIL, 1));
        Date date1 = Date.valueOf(LocalDate.of(2019, Month.APRIL, 21));
        Date date2 = Date.valueOf(LocalDate.of(2019, Month.MAY, 2));
        Date date3 = Date.valueOf(LocalDate.of(2019, Month.MAY, 15));
        Date date4 = Date.valueOf(LocalDate.of(2019, Month.MAY, 30));
        Date date5 = Date.valueOf(LocalDate.of(2019, Month.JUNE, 10));
        
        IDepartmentTask dt1 = new DepartmentTask(1, date0, date1, true, dep1, null);
        IDepartmentTask dt2 = new DepartmentTask(2, date1, date2, true, dep2, null);
        IDepartmentTask dt3 = new DepartmentTask(3, date2, date3, false, dep3, null);
        IDepartmentTask dt4 = new DepartmentTask(4, date3, date4, false, dep4, null);
        IDepartmentTask dt5 = new DepartmentTask(5, date4, date5, false, dep5, null);
        
        depTask = new ArrayList();
        depTask.add(dt1);
        depTask.add(dt2);
        depTask.add(dt3);
        depTask.add(dt4);
        depTask.add(dt5);
    }

    @Before
    public void setup() throws SQLException, ParseException
    {
        instance = new mainLogicClass();
        
        for (int i = 0; i < depTask.size(); i++)
            {
                IDepartmentTask depFirst;
                IDepartmentTask depSecond;
                if (i == 0)
                {
                    depFirst = null;
                    depSecond = depTask.get(i);
                } else
                {
                    depFirst = depTask.get(i - 1);
                    depSecond = depTask.get(i);
                }
                instance.setColorsForProgressBar(depFirst, depSecond, true);
            }
    }

    @Test
    public void testFirstStatus() throws Exception
    {
        System.out.println("testFirstStatus");
        
        expResult = Status.DONE;
        result = depTask.get(0).getProgressBar().getStatus();
        assertEquals("Problem in the first status!", expResult, result);
    }
    
    @Test
    public void testSecondStatus() throws Exception
    {
        System.out.println("testSecondStatus");
        
        expResult = Status.DONE;
        result = depTask.get(1).getProgressBar().getStatus();
        assertEquals("Problem in the second status!", expResult, result);
    }
    
    @Test
    public void testThirdStatus() throws Exception
    {
        System.out.println("testThirdStatus");
        
        expResult = Status.BEHIND;
        result = depTask.get(2).getProgressBar().getStatus();
        assertEquals("Problem in the third status!", expResult, result);
    }
    
    @Test
    public void testFourthStatus() throws Exception
    {
        System.out.println("testFourthStatus");
        
        expResult = Status.BEHIND;
        result = depTask.get(3).getProgressBar().getStatus();
        assertEquals("Problem in the fourth status!", expResult, result);
    }
    
    @Test
    public void testFifthStatus() throws Exception
    {
        System.out.println("testFifthStatus");
        
        expResult = Status.NOT_STARTED;
        result = depTask.get(4).getProgressBar().getStatus();
        assertEquals("Problem in the fifth status!", expResult, result);
    }
    
}

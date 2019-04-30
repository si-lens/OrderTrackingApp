''' <summary>
''' Defines an interface of data from multiple data-sources.
''' This interface acts as an adapter for BelMaker models such as Order and Customer.
''' </summary>
Public Interface IProductionOrder

    ''' <summary>
    ''' Defines relevant data of the order.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Order As IOrder

    ''' <summary>
    ''' Defines relevant data of the delivery.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Delivery As IDelivery

    ''' <summary>
    ''' Defines data of the customer.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Customer As ICustomer

    ''' <summary>
    ''' Defines the tasks of departments.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property GetDepartmentTasks As IEnumerable(Of IDepartmentTask)

    ''' <summary>
    ''' Adds a task to this production order.
    ''' </summary>
    ''' <param name="Task">The task to be added.</param>
    Sub AddDepartmentTask(Task As IDepartmentTask)

    ''' <summary>
    ''' Removes the task to this production order.
    ''' </summary>
    ''' <param name="Task">The task to be removed.</param>
    Sub RemoveDepartmentTask(Task As IDepartmentTask)

End Interface

''' <summary>
''' Defines a customer.
''' </summary>
Public Interface ICustomer

    ''' <summary>
    ''' Defines the name of the customer.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Name As String

End Interface

''' <summary>
''' Defines an interface for a production worker.
''' </summary>
Public Interface IWorker

    ''' <summary>
    ''' Defines the name of the given production worker.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Name As String

    ''' <summary>
    ''' Defines the initials for this employee.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Initials As String

    ''' <summary>
    ''' Defines the salary number of the given employee.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property SalaryNumber As Integer

End Interface

''' <summary>
''' Defines the delivery terms.
''' </summary>
Public Interface IDelivery

    ''' <summary>
    ''' Defines the time of which the order is to be delivered.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property DeliveryTime As DateTime

End Interface

''' <summary>
''' Defines data of an Order.
''' </summary>
Public Interface IOrder

    ''' <summary>
    ''' Defines the unique order number.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property OrderNumber As String

End Interface

''' <summary>
''' Defines a department.
''' </summary>
Public Interface IDepartment

    ''' <summary>
    ''' Defines the name of the department.
    ''' </summary>
    ReadOnly Property Name As String

End Interface

''' <summary>
''' Defines a task for a given department.
''' </summary>
Public Interface IDepartmentTask

    ''' <summary>
    ''' Raised when a worker has been added to this order.
    ''' </summary>
    Event WorkerAdded As EventHandler

    ''' <summary>
    ''' Raised when a worker has been removed from this order.
    ''' </summary>
    Event WorkerRemoved As EventHandler

    ''' <summary>
    ''' Defines the active workers on this given production order.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property ActiveWorkers As IEnumerable(Of IWorker)

    ''' <summary>
    ''' Defines the name of the department.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property Department As IDepartment

    ''' <summary>
    ''' Defines if the order has been finished.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property FinishedOrder As Boolean

    ''' <summary>
    ''' Defines the start date for this department.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property StartDate As DateTime

    ''' <summary>
    ''' Defines the end date for this department.
    ''' </summary>
    ''' <returns></returns>
    ReadOnly Property EndDate As DateTime

    ''' <summary>
    ''' Adds a worker as an active worker on this department task.
    ''' </summary>
    ''' <param name="Worker">Defines the worker to be added.</param>
    Sub AddWorker(Worker As IWorker)

    ''' <summary>
    ''' Removes a worker from this department tasks active workers.
    ''' </summary>
    ''' <param name="Worker"></param>
    Sub RemoveWorker(Worker As IWorker)

End Interface
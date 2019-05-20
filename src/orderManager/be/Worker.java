package orderManager.be;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Worker extends RecursiveTreeObject<Worker> implements IWorker {

  private String initials;
  private String name;
  private long salary;
  private int id;


  public Worker(int id, String name, String initials, long salary) {
    this.initials = initials;
    this.name = name;
    this.salary = salary;
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getInitials() {
    return initials;
  }

  @Override
  public long getSalary() {
    return salary;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return name;
  }
}

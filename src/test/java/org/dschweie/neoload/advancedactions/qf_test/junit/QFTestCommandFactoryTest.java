package org.dschweie.neoload.advancedactions.qf_test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestExecuteAction;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestStartAction;
import com.neotys.extensions.action.ActionParameter;

public class QFTestCommandFactoryTest
{

  protected static String buildCommandline(List<String> parameters)
  {
    String retval = "";
    Iterator<String> it = parameters.iterator();
    while(it.hasNext())
      retval = retval.concat(it.next()).concat(" ");
    return retval.trim();
  }

  @Test
  public void test()
  {
    QFTestStartAction instance = new QFTestStartAction();
    List<ActionParameter> parameters = instance.getDefaultActionParameters();
    List<String> command = QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters);
    System.out.println(QFTestCommandFactoryTest.buildCommandline(command));
  }

  @Test
  public void testJVMSimple()
  {
    List<ActionParameter> parameters = new ArrayList<>();
    parameters.add(new ActionParameter("jvm", "-Duser.language=en"));
    List<String> command = QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters);
    assertTrue(QFTestCommandFactoryTest.buildCommandline(command).contains(" -J-Duser.language=en "));
  }

  @Test
  public void testJVMSimpleWithJ()
  {
    List<ActionParameter> parameters = new ArrayList<>();
    parameters.add(new ActionParameter("jvm", "-J-Xmx1024m"));
    List<String> command = QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters);
    assertTrue(QFTestCommandFactoryTest.buildCommandline(command).contains(" -J-Xmx1024m "));
  }

  @Test
  public void testJVMMultiple()
  {
    List<ActionParameter> parameters = new ArrayList<>();
    parameters.add(new ActionParameter("jvm", "-Duser.language=en"));
    parameters.add(new ActionParameter("jvm", "-Xmx1024m"));
    List<String> command = QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters);
    assertTrue(QFTestCommandFactoryTest.buildCommandline(command).contains(" -J-Duser.language=en "));
    assertTrue(QFTestCommandFactoryTest.buildCommandline(command).contains(" -J-Xmx1024m "));
    System.out.println(QFTestCommandFactoryTest.buildCommandline(command));
  }

  @Test
  public void testStartCleanEmpty()
  {
    List<ActionParameter> parameters = new ArrayList<>();
    parameters.add(new ActionParameter("startclean", ""));
    List<String> command = QFTestCommandFactory.buildCommand(QFTestExecuteAction.TYPE, parameters);
    System.out.println(QFTestCommandFactoryTest.buildCommandline(command));
    assertTrue(QFTestCommandFactoryTest.buildCommandline(command).contains(" -startclean"));
  }

  @Test
  public void testStartCleanAnything()
  {
    List<ActionParameter> parameters = new ArrayList<>();
    parameters.add(new ActionParameter("startclean", "srgferf"));
    List<String> command = QFTestCommandFactory.buildCommand(QFTestExecuteAction.TYPE, parameters);
    System.out.println(QFTestCommandFactoryTest.buildCommandline(command));
    assertTrue(QFTestCommandFactoryTest.buildCommandline(command).contains(" -startclean"));
  }

  @Test
  public void testStartCleanFalse()
  {
    List<ActionParameter> parameters = new ArrayList<>();
    parameters.add(new ActionParameter("startclean", "false"));
    List<String> command = QFTestCommandFactory.buildCommand(QFTestExecuteAction.TYPE, parameters);
    System.out.println(QFTestCommandFactoryTest.buildCommandline(command));
    assertFalse(QFTestCommandFactoryTest.buildCommandline(command).contains(" -startclean"));
  }


}

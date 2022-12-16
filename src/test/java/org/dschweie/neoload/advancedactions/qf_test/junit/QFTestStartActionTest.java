package org.dschweie.neoload.advancedactions.qf_test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestStartAction;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestStopAction;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.SampleResult;

import junit.framework.Assert;

public class QFTestStartActionTest
{
  //@Test
  public void startandstopDaemon()
  {
    List<ActionParameter> actionParameters = new ArrayList<ActionParameter>();
    actionParameters.add(new ActionParameter("daemonport", "3300"));
    actionParameters.add(new ActionParameter("daemonhost", "127.0.0.1"));

    Action action = new QFTestStartAction();
    try
    {
      System.out.println("Launch QF-Test-Daemon");
      SampleResult result = action.getEngineClass().newInstance().execute(null, actionParameters);
      assertFalse("Launch QF-Test-Daemon", result.isError());
    } catch (InstantiationException | IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      System.out.println(e.getLocalizedMessage());
    }
    try
    {
      System.out.println("Terminate QF-Test-Daemon");
      assertFalse("Terminate QF-Test-Daemon", QFTestStopAction.class.newInstance().getEngineClass().newInstance().execute(null, actionParameters).isError());
    } catch (InstantiationException | IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      System.out.println(e.getLocalizedMessage());
    }
  }

}

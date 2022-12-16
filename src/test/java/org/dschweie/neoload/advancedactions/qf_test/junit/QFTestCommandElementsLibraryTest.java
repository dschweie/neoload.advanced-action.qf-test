package org.dschweie.neoload.advancedactions.qf_test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary;
import org.dschweie.neoload.advancedactions.qf_test.command.worker.QFTestCommandWorkerPing;
import org.junit.Test;

import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestPingAction;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.SampleResult;


public class QFTestCommandElementsLibraryTest
{
  @Test
  public void test_getParameterValue()
  {
    List<ActionParameter> parameters = (new QFTestPingAction()).getDefaultActionParameters();
    assertEquals("localhost", QFTestCommandElementsLibrary.getParameterValue(parameters, "daemonhost", "unknown"));
    assertEquals("3543", QFTestCommandElementsLibrary.getParameterValue(parameters, "daemonport", "unknown"));
  }
  
  
  public void executePing()
  {
     final Action action = new QFTestPingAction();
     try
    {
      final SampleResult result = action.getEngineClass().newInstance().execute(null, action.getDefaultActionParameters());
      //assertEquals(false, result.isError());
    } catch (InstantiationException | IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Test
  public void test_generationPingCommand()
  {
    final Action action = new QFTestPingAction();
    List<String> command = QFTestCommandWorkerPing.buildCommand(action.getDefaultActionParameters());
    assertNotNull(command);
  }
  
}

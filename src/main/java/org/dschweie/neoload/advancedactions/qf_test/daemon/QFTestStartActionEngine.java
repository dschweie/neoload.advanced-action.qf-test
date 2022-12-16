package org.dschweie.neoload.advancedactions.qf_test.daemon;

import java.io.IOException;
import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.AbstractQFTestActionEngine;
import org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory;
import org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

/**
 *  \brief      Engine-Klasse für die Advanced Action zum Start eines QF-Test-Daemon
 *
 *  QF-Test kann in einem Daemon-Modus betrieben werden. In diesem Modus
 *  wird QF-Test als RPC-Dienst einmal gestartet und kann dann Testfälle oder
 *  Testfallsätze ausführen.
 *
 *  Der Vorteil des Daemon ist, dass am Ende des Testskripts QF-Test und ein
 *  gestarteter Client nicht automatisch beendet werden. Dieses reduziert die
 *  Zeit für die Durchführung, da zu Beginn nicht immer neu gestartet und zum
 *  Ende alles beendet werden muss.
 *
 *  Als Nachteil ist zu nennen, dass grundsätzlich jeder auf dem Rechner
 *  QF-Test-Skripte zur Ausführung bringen kann, wenn er weiß, wo der
 *  QF-Test Daemon läuft.
 *
 *  \warning    Der QF-Test Daemon sollte nur in einer geschätzten Umgebung
 *              betreiben werden und wann immer möglich sollte der QF-Test
 *              Daemon immer auf dem Lastgenerator betrieben werden.
 *
 *  @author     dirk.schweier
 */
public class QFTestStartActionEngine extends AbstractQFTestActionEngine
{

   /**
    *  \brief    Schnittstelle für NeoLoad zur Ausführung der Advanced Action
    *
    *  Diese Methode muss für NeoLoad bereitgestellt werden, damit eine Advanced
    *  Action zur Laufzeit von NeoLoad aufgerufen werden kann.
    *
    *  Konkret wird mit dieser Methode ein QF-Test Daemon gestartet.
    *
    *  \warning   Da über diese Advanced Action ein QF-Test Daemon gestartet
    *             wird, der über das unsichere RPC-Protokoll arbeitet, sollte
    *             der Dienst nur direkt auf dem Lastgenerator und in einer
    *             sicheren Umgebung gestartet werden.
    *
    *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
    *                          über die die Methode Zugriff auf Informationen
    *                          von NeoLoad zur Laufzeit hat.
    *  @param    parameters    In diesem Parameter wird von der Satz an
    *                          Parametern übergeben, die der Anwender zu der
    *                          Advanced Action in NeoLoad erfasst hat.
    *                          Es werden konkrete Werte übergeben. Das bedeutet,
    *                          dass Variablen von NeoLoad bereits ersetzt sind.
    *
    *  @return   Die Methode liefert eine Instanz der Klasse SampleRequest
    *            zurück, die alle Informationen zur Ausführung und dem Ergebnis
    *            der Ausführung beinhaltet.
    */
  @Override
  public SampleResult execute(Context context, List<ActionParameter> parameters)
  {
    SampleResult    result      = new SampleResult();
    SampleResult    pingResult  = null;
    boolean         flagPingAck = false;
    long            timeout     = Long.parseLong(QFTestCommandElementsLibrary.getParameterValue(parameters, "timeout", "300000"));

    try
    {
      this.process = new ProcessBuilder(QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters)).start();

      result.sampleStart();

//      while (     ( this.process.isAlive()                                          )
//          &&  ( timeout > ( System.currentTimeMillis()-result.getTimestamp() )  )
//          &&  ( null == pingResult?true:pingResult.isError()                    )
//        )
      while (     ( timeout > ( System.currentTimeMillis()-result.getTimestamp() )  )
              &&  ( null == pingResult?true:pingResult.isError()                    )
            )
      { //  inside this loop the action is waiting for running daemon instance
        if(this.process.isAlive())
          pingResult = this.executeSubprocess(context, QFTestCommandFactory.buildCommand(QFTestPingAction.TYPE, parameters));
        else
          this.process = new ProcessBuilder(QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters)).start();
      }
      result.sampleEnd();

      //  Collecting the data for the result of this action
      if( this.process.isAlive() &&  !pingResult.isError() )
      { //  in this case the daemon is running and Ping was successful
        result.setStatusCode("0");
      }
      else
      {
        if(this.process.isAlive())
        { // in this case the process is not alive, so
          this.process.destroyForcibly();
          result = getErrorResult(context, result, "QFT-DAEMON-UNAVAILABLE", "QF-Test Daemon is not available.", null);
        }
        else
          result = getErrorResult(context, result, "QFT-DAEMON-NOTRUNNING", "QF-Test Daemon is not running.", null);
      }
      result.setRequestContent(this.buildProcessCallToString(QFTestCommandFactory.buildCommand(QFTestStartAction.TYPE, parameters)).concat("\n").concat(pingResult.getRequestContent()));
      result.setResponseContent("<startDaemon>\n<exitcode>".concat(result.getStatusCode()).concat("</exitcode>\n</startDaemon>\n<pingDaemon>\n").concat(pingResult.getResponseContent()).concat("\n</pingDaemon>"));
    }
    catch (IOException e)
    {
      getErrorResult(context, result, "-1", "Exception occurred while starting the QF-Test Daemon (".concat(QFTestCommandElementsLibrary.getParameterValue(parameters, "daemonhost", "<loadgenerator>")).concat(":").concat(QFTestCommandElementsLibrary.getParameterValue(parameters, "daemonport", "3543")).concat(")"), e);
    }

    result.setError(!"0".equals(result.getStatusCode()));

    return result;
  }

}

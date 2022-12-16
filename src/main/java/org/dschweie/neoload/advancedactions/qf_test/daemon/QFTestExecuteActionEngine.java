package org.dschweie.neoload.advancedactions.qf_test.daemon;

import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.AbstractQFTestActionEngine;
import org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

/**
 *  \brief      Engine-Klasse für die Advanced Action zur Ausführung eines Testskripts durch QF-Test
 *
 *  Ein Instanz dieser Klasse führt den Aufruf von QF-Test durch, um aus QF-Test
 *  heraus ein Testskript auszuführen.
 *
 *  @author     dirk.schweier
 */
public class QFTestExecuteActionEngine extends AbstractQFTestActionEngine
{

  /**
   *  \brief    Schnittstelle für NeoLoad zur Ausführung der Advanced Action
   *
   *  Diese Methode muss für NeoLoad bereitgestellt werden, damit eine Advanced
   *  Action zur Laufzeit von NeoLoad aufgerufen werden kann.
   *
   *  Konkret übergibt diese Methode an einen laufenden QF-Test Daemon einen
   *  Testfallknoten oder einen Testfallsatzknoten, der durch den QF-Test-Daemon
   *  ausgeführt werden soll.
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
    parameters.add(new ActionParameter("-variable", "NEOLOAD-USERPATH=".concat("context.getCurrentVirtualUser().getId()")));
    return this.executeProcess(context, QFTestCommandFactory.buildCommand(QFTestExecuteAction.TYPE, parameters));
  }

}

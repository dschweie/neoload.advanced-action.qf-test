package org.dschweie.neoload.advancedactions.qf_test.daemon;

import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.AbstractQFTestActionEngine;
import org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

/**
 *  \brief      Engine-Klasse für die Advanced Action zum Stoppen eines QF-Test-Daemon
 *
 *  Mit einer Instanz dieser Klasse wird ein QF-Test Daemon gestoppt.
 *
 *  @author     dirk.schweier
 */
public class QFTestStopActionEngine extends AbstractQFTestActionEngine
{
  /**
   *  \brief    Schnittstelle für NeoLoad zur Ausführung der Advanced Action
   *
   *  Diese Methode muss für NeoLoad bereitgestellt werden, damit eine Advanced
   *  Action zur Laufzeit von NeoLoad aufgerufen werden kann.
   *
   *  Konkret wird mit dieser Methode ein QF-Test Daemon gestoppt.
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
    return this.executeProcess(context, QFTestCommandFactory.buildCommand(QFTestStopAction.TYPE, parameters));
  }

}

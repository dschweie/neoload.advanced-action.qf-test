package org.dschweie.neoload.advancedactions.qf_test.command.worker;

import java.util.List;
import java.util.Vector;

import org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary;
import com.neotys.extensions.action.ActionParameter;

/**
 *  \brief    Konkreter Arbeiter der Fabrik QFTestCommandFactory zum Ausführen eines Testskripts
 *
 *  Die übersetzung der ActionParameter in Optionen für den Aufruf von QF-Test
 *  wurde mit dem Entwurfsmuster Fabrik implementiert.
 *
 *  Diese Klasse ist der konkrete Arbeiter, der die Parameter zur Ausführung
 *  eines Testskripts als Produkt erzeugt. Das Testskript wird durch einen
 *  QF-Test Daemon ausgeführt, der über geeignete AchtionParameter angesprochen
 *  wird.
 *
 *  @author   dirk.schweier
 *  @since    0.1.0
 *
 *  \see      org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory
 */
public class QFTestCommandWorkerTestCallDaemon
{
  /**
   *  \brief  Worker-Methode zur Erstellung des konkreten Kommandos.
   *
   *  Diese Methode bildet die Parameter für den Kommandozeilenaufruf mit dem
   *  ein Testskript über einen QF-Test Daemon ausgeführt werden kann.
   *
   *  @param  parameters      In dem Parameter wird die Liste der
   *                          ActionParameter erwartet, aus denen dann ein
   *                          konkreter Aufruf von QF-Test erzeugt wird.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste mit Parametern
   *          für den Aufruf von QF-Test über die Kommandozeile.
   */
  public static List<String> buildCommand(List<ActionParameter> parameters)
  {
    final List<String>  command  = new Vector<String>();

    command.addAll(QFTestCommandElementsLibrary.getQFTestCBatchCall(parameters));

    command.add("-calldaemon");
    command.add("-nomessagewindow");

    command.addAll(QFTestCommandElementsLibrary.getJavaVMParameters(parameters));
    command.addAll(QFTestCommandElementsLibrary.getDaemonDestinationElements(parameters));
    command.addAll(QFTestCommandElementsLibrary.getSecuritySettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getOptionsSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getExitcodeSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getLogSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getVerboseSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getCleanSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getVariableSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getTestCaseSettings(parameters));

    return command;
  }

}

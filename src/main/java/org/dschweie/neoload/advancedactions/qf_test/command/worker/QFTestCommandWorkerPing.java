package org.dschweie.neoload.advancedactions.qf_test.command.worker;

import java.util.List;
import java.util.Vector;

import org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary;
import com.neotys.extensions.action.ActionParameter;

/**
 *  \brief    Konkreter Arbeiter der Fabrik QFTestCommandFactory zur Erstellung des Ping-Kommandos
 *
 *  Die übersetzung der ActionParameter in Optionen für den Aufruf von QF-Test
 *  wurde mit dem Entwurfsmuster Fabrik implementiert.
 *
 *  Diese Klasse ist der konkrete Arbeiter, der die Parameter für ein
 *  Ping-Kommando als Produkt erzeugt.
 *
 *  @author   dirk.schweier
 *  @since    0.1.0
 *
 *  \see      org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory
 */
public class QFTestCommandWorkerPing
{
  /**
   *  \brief  Worker-Methode zur Erstellung des konkreten Kommandos.
   *
   *  Diese Methode bildet die Parameter für den Kommandozeilenaufruf mit dem
   *  geprüft werden kann, ob unter einem bestimmten Port unter einer
   *  bestimmten IP ein QF-Test Daemon läuft.
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
    command.add("-ping");
    return command;
  }

}

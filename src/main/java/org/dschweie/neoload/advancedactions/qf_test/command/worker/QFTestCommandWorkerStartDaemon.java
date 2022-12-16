package org.dschweie.neoload.advancedactions.qf_test.command.worker;

import java.util.List;
import java.util.Vector;

import org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary;
import com.neotys.extensions.action.ActionParameter;

/**
 *  \brief    Konkreter Arbeiter der Fabrik QFTestCommandFactory zum Starten eines QF-Test Daemon
 *
 *  Die übersetzung der ActionParameter in Optionen für den Aufruf von QF-Test
 *  wurde mit dem Entwurfsmuster Fabrik implementiert.
 *
 *  Diese Klasse ist der konkrete Arbeiter, der die Parameter den Start eines
 *  QF-Test Daemon als Produkt erzeugt.
 *
 *  @author   dirk.schweier
 *  @since    0.1.0
 *
 *  \see      org.dschweie.neoload.advancedactions.qf_test.command.QFTestCommandFactory
 */
public class QFTestCommandWorkerStartDaemon
{

  /**
   *  \brief  Worker-Methode zur Erstellung des konkreten Kommandos.
   *
   *  Diese Methode bildet die Parameter für den Kommandozeilenaufruf mit dem
   *  ein QF-Test Daemon gestartet werden kann.
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

    command.addAll(QFTestCommandElementsLibrary.getQFTestBatchCall(parameters));
    command.add("-daemon");
    command.addAll(QFTestCommandElementsLibrary.getJavaVMParameters(parameters));
    command.addAll(QFTestCommandElementsLibrary.getDaemonDestinationElements(parameters));
    command.addAll(QFTestCommandElementsLibrary.getSecuritySettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getLicenseSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getOptionsSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getPluginDirSettings(parameters));
    command.add("-nomessagewindow");
    command.addAll(QFTestCommandElementsLibrary.getConfigSettings(parameters));
    command.addAll(QFTestCommandElementsLibrary.getVariableSettings(parameters));

    return command;
  }

}
